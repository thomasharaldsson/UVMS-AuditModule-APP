/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.audit.message.producer.bean;

import eu.europa.ec.fisheries.uvms.audit.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.audit.message.constants.MessageConstants;
import eu.europa.ec.fisheries.uvms.audit.message.exception.AuditMessageException;
import eu.europa.ec.fisheries.uvms.audit.message.producer.MessageProducer;
import eu.europa.ec.fisheries.uvms.config.constants.ConfigConstants;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Stateless
public class MessageProducerBean implements MessageProducer, ConfigMessageProducer {

    final static Logger LOG = LoggerFactory.getLogger(MessageProducerBean.class);

    private Queue responseQueue;
    private Queue configQueue;

    private static final int CONFIG_TTL = 30000;

    @EJB
    JMSConnectorBean connector;

    @PostConstruct
    public void init() {
        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (Exception e) {
            LOG.error("Failed to get InitialContext",e);
            throw new RuntimeException(e);
        }
        responseQueue = lookupQueue(ctx, MessageConstants.AUDIT_RESPONSE_QUEUE);
        configQueue = lookupQueue(ctx, ConfigConstants.CONFIG_MESSAGE_IN_QUEUE);
    }

    private Queue lookupQueue(InitialContext ctx, String queue) {
        try {
            return (Queue)ctx.lookup(queue);
        } catch (NamingException e) {
            //if we did not find the queue we might need to add java:/ at the start
            LOG.debug("Queue lookup failed for " + queue);
            String wfQueueName = "java:/"+ queue;
            try {
                LOG.debug("trying " + wfQueueName);
                return (Queue)ctx.lookup(wfQueueName);
            } catch (Exception e2) {
                LOG.error("Queue lookup failed for both " + queue + " and " + wfQueueName);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws AuditMessageException {
        try {
            Session session = connector.getNewSession();
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(responseQueue);
            message.setText(text);

            switch (queue) {
                case INTEGRATION:
                    break;
                case CONFIG:
                    getProducer(session, configQueue).send(message);
                    break;
            }

            return message.getJMSMessageID();
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {0}", e.getMessage());
            throw new AuditMessageException("[ Error when sending message. ]", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendMessageBackToRecipient(TextMessage requestMessage, String returnMessage) throws AuditMessageException {
        try {

            LOG.info("[ Sending message back to recipient on queue ] {}", requestMessage.getJMSReplyTo());
            Session session = connector.getNewSession();
            TextMessage message = session.createTextMessage();
            message.setJMSCorrelationID(message.getJMSMessageID());
            message.setJMSDestination(requestMessage.getJMSReplyTo());
            message.setText(returnMessage);

            getProducer(session, message.getJMSDestination()).send(message);

        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new AuditMessageException("[ Error when sending message. ]", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendConfigMessage(String text) throws ConfigMessageException {
        try {
            return sendDataSourceMessage(text, DataSourceQueue.CONFIG);
        } catch (AuditMessageException e) {
            LOG.error("[ Error when sending config message. ] {}", e.getMessage());
            throw new ConfigMessageException("[ Error when sending config message. ]");
        }
    }

    private javax.jms.MessageProducer getProducer(Session session, Destination destination) throws JMSException {
        javax.jms.MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.setTimeToLive(60000L);
        return producer;
    }

}
