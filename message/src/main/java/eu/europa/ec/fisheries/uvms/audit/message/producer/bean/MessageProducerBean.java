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

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.*;

@Stateless
public class MessageProducerBean implements MessageProducer, ConfigMessageProducer {

    final static Logger LOG = LoggerFactory.getLogger(MessageProducerBean.class);

    @Resource(mappedName = MessageConstants.QUEUE_DATASOURCE_INTERNAL)
    private Queue localDbQueue;

    @Resource(mappedName = MessageConstants.AUDIT_RESPONSE_QUEUE)
    private Queue responseQueue;

    @Resource(mappedName = ConfigConstants.CONFIG_MESSAGE_IN_QUEUE)
    private Queue configQueue;
    
//    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
//    private ConnectionFactory connectionFactory;
//
//    private Connection connection = null;
//    private Session session = null;

    private static final int CONFIG_TTL = 30000;

    @Inject
    JMSConnectorBean connector;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws AuditMessageException {
        try {
//            connectToQueue();
            Session session = connector.getNewSession();
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(responseQueue);
            message.setText(text);

            switch (queue) {
            case INTERNAL:
                getProducer(session, localDbQueue).send(message);
                break;
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
//        } finally {
//            disconnectQueue();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendMessageBackToRecipient(TextMessage requestMessage, String returnMessage) throws AuditMessageException {
        try {

            LOG.info("[ Sending message back to recipient on queue ] {}", requestMessage.getJMSReplyTo());

//            connectToQueue();
            Session session = connector.getNewSession();
            TextMessage message = session.createTextMessage();
            message.setJMSCorrelationID(message.getJMSMessageID());
            message.setJMSDestination(requestMessage.getJMSReplyTo());
            message.setText(returnMessage);

            getProducer(session, message.getJMSDestination()).send(message);

        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new AuditMessageException("[ Error when sending message. ]", e);
//        } finally {
//            disconnectQueue();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sendConfigMessage(String text) throws ConfigMessageException {
        try {
            return sendDataSourceMessage(text, DataSourceQueue.CONFIG);
        }
        catch (AuditMessageException e) {
            LOG.error("[ Error when sending config message. ] {}", e.getMessage());
            throw new ConfigMessageException("[ Error when sending config message. ]");
        }
    }

//    private void connectToQueue() throws JMSException {
//        connection = connectionFactory.createConnection();
//        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        connection.start();
//    }
//
//    private void disconnectQueue() {
//        try {
//            if (connection != null) {
//                connection.stop();
//                connection.close();
//            }
//        } catch (JMSException e) {
//            LOG.error("[ Error when closing JMS connection ] {}", e.getMessage());
//        }
//    }

    private javax.jms.MessageProducer getProducer(Session session, Destination destination) throws JMSException {
        javax.jms.MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.setTimeToLive(60000L);
        return producer;
    }

}