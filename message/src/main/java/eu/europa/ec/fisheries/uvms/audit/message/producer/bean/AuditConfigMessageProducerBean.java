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
import eu.europa.ec.fisheries.uvms.audit.message.consumer.bean.AuditConsumerBean;
import eu.europa.ec.fisheries.uvms.audit.message.exception.AuditMessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageProducer;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@LocalBean
public class AuditConfigMessageProducerBean extends AbstractProducer implements ConfigMessageProducer {

    final static Logger LOG = LoggerFactory.getLogger(AuditConfigMessageProducerBean.class);

    @EJB
    private AuditConsumerBean auditConsumer;

    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws AuditMessageException {
        String corrId = null;
        try {
            switch (queue) {
                case INTEGRATION:
                    LOG.error("[ERROR] Nothing configured for this queue!");
                    break;
                case CONFIG:
                    corrId = sendModuleMessage(text, auditConsumer.getDestination());
                    break;
            }
            return corrId;
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {0}", e.getMessage());
            throw new AuditMessageException("[ Error when sending message. ]", e);
        }
    }

    public void sendMessageBackToRecipient(TextMessage requestMessage, String returnMessage) throws AuditMessageException {
        try {
            LOG.info("[INFO] Sending message back to recipient on queue {}", requestMessage.getJMSReplyTo());
            sendResponseMessageToSender(requestMessage, returnMessage);
        } catch (Exception e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new AuditMessageException("[ Error when sending message. ]", e);
        }
    }

    @Override
    public String sendConfigMessage(String text) throws ConfigMessageException {
        try {
            return sendDataSourceMessage(text, DataSourceQueue.CONFIG);
        } catch (AuditMessageException e) {
            LOG.error("[ Error when sending config message. ] {}", e.getMessage());
            throw new ConfigMessageException("[ Error when sending config message. ]");
        }
    }

    @Override
    public String getDestinationName() {
        return eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants.QUEUE_CONFIG;
    }
}
