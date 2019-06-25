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
package eu.europa.ec.fisheries.uvms.audit.service.Message.bean;

import eu.europa.ec.fisheries.schema.audit.source.v1.*;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.audit.service.AuditService;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = MessageConstants.QUEUE_AUDIT_EVENT, activationConfig = {
    @ActivationConfigProperty(propertyName = MessageConstants.MESSAGING_TYPE_STR, propertyValue = MessageConstants.CONNECTION_TYPE),
    @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_TYPE_STR, propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
    @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_STR, propertyValue = MessageConstants.QUEUE_AUDIT_EVENT_NAME),
    @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_JNDI_NAME, propertyValue = MessageConstants.QUEUE_AUDIT_EVENT),
    @ActivationConfigProperty(propertyName =  MessageConstants.CONNECTION_FACTORY_JNDI_NAME, propertyValue = MessageConstants.CONNECTION_FACTORY)
})
public class AuditMessageConsumerBean implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(AuditMessageConsumerBean.class);

    @EJB
    private AuditConfigMessageProducerBean producer;

    @EJB
    private AuditService auditService;

    @Override
    public void onMessage(Message message) {
        LOG.debug("Received MessageRecievedEvent:{}", message);
        TextMessage requestMessage = (TextMessage) message;
        try {
            AuditDataSourceMethod method = AuditDataSourceMethod.fromValue(requestMessage.getStringProperty(MessageConstants.JMS_FUNCTION_PROPERTY));
            if(method == null){
                AuditBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(requestMessage, AuditBaseRequest.class);
                method = baseRequest.getMethod();
            }
            switch (method) {
                case CREATE:
                    CreateAuditLogRequest auditLogRequest = JAXBMarshaller.unmarshallTextMessage(requestMessage, CreateAuditLogRequest.class);
                    AuditLogType auditLog = auditLogRequest.getAuditLog();
                    auditService.createAuditLog(auditLog);
                    break;
                case PING:
                    PingResponse pingResponse = new PingResponse();
                    pingResponse.setResponse("pong");
                    String response = JAXBMarshaller.marshallJaxBObjectToString(pingResponse);
                    producer.sendResponseMessageToSender(requestMessage, response);
                    break;
                default:
                    LOG.warn("No such method exists:{}", method);
                    break;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
