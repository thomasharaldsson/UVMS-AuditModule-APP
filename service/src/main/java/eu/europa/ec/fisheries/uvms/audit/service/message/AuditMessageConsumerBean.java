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
package eu.europa.ec.fisheries.uvms.audit.service.message;

import eu.europa.ec.fisheries.schema.audit.source.v1.*;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.audit.service.bean.AuditServiceBean;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_TYPE_STR, propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
    @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_STR, propertyValue = MessageConstants.QUEUE_AUDIT_EVENT)
})
public class AuditMessageConsumerBean implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(AuditMessageConsumerBean.class);

    @EJB
    private AuditConfigMessageProducerBean producer;

    @Inject
    private AuditServiceBean auditService;

    @Override
    public void onMessage(Message message) {
        LOG.debug("Received MessageRecievedEvent:{}", message);
        TextMessage requestMessage = (TextMessage) message;
        try {
            String methodString = requestMessage.getStringProperty(MessageConstants.JMS_FUNCTION_PROPERTY);
            if(methodString == null){
                AuditBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(requestMessage, AuditBaseRequest.class);
                methodString = baseRequest.getMethod().toString();
            }
            AuditDataSourceMethod method = AuditDataSourceMethod.fromValue(methodString);
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
