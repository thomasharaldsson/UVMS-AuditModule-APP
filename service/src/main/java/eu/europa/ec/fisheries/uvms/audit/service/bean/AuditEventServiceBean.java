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
package eu.europa.ec.fisheries.uvms.audit.service.bean;

import eu.europa.ec.fisheries.schema.audit.source.v1.AuditBaseRequest;
import eu.europa.ec.fisheries.schema.audit.source.v1.CreateAuditLogRequest;
import eu.europa.ec.fisheries.schema.audit.source.v1.GetAuditLogListByQueryRequest;
import eu.europa.ec.fisheries.schema.audit.source.v1.PingResponse;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.audit.message.event.MessageRecievedEvent;
import eu.europa.ec.fisheries.uvms.audit.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.audit.message.exception.AuditMessageException;
import eu.europa.ec.fisheries.uvms.audit.message.producer.bean.AuditConfigMessageProducerBean;
import eu.europa.ec.fisheries.uvms.audit.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.audit.service.AuditEventService;
import eu.europa.ec.fisheries.uvms.audit.service.AuditService;
import eu.europa.ec.fisheries.uvms.audit.service.exception.AuditServiceException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class AuditEventServiceBean implements AuditEventService {

    final static Logger LOG = LoggerFactory.getLogger(AuditEventServiceBean.class);

    @Inject
    @ErrorEvent
    private Event<EventMessage> errorEvent;

    @EJB
    private AuditConfigMessageProducerBean producer;

    @EJB
    private AuditService auditService;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void getData(@Observes @MessageRecievedEvent EventMessage message) {
        LOG.info("Received MessageRecievedEvent:{}", message);
        TextMessage requestMessage = message.getJmsMessage();
        try {
            AuditBaseRequest baseRequest = JAXBMarshaller.unmarshallTextMessage(requestMessage, AuditBaseRequest.class);
            switch (baseRequest.getMethod()) {
                case CREATE:
                    CreateAuditLogRequest auditLogRequest = JAXBMarshaller.unmarshallTextMessage(requestMessage, CreateAuditLogRequest.class);
                    AuditLogType auditLog = auditLogRequest.getAuditLog();
                    auditService.createAuditLog(auditLog);
                    break;
                case AUDITLOG_LIST:
                    GetAuditLogListByQueryRequest getAuditLogRequest = JAXBMarshaller.unmarshallTextMessage(requestMessage, GetAuditLogListByQueryRequest.class);
                    auditService.getList(getAuditLogRequest.getQuery());
                    break;
                case PING:
                    PingResponse pingResponse = new PingResponse();
                    pingResponse.setResponse("pong");
                    String response = JAXBMarshaller.marshallJaxBObjectToString(pingResponse);
                    producer.sendMessageBackToRecipient(requestMessage, response);
                    break;
                default:
                    LOG.warn("No such method exists:{}", baseRequest.getMethod());
                    break;
            }
        } catch (ModelMapperException | AuditServiceException | AuditMessageException e) {
            errorEvent.fire(new EventMessage(message.getJmsMessage(), "Exception when sending response back to recipient : " + e.getMessage()));
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void returnError(@Observes @ErrorEvent EventMessage message) {
        LOG.info("Received Error RecievedEvent but no logic is implemented yet:{}", message);
    }

}