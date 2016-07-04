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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.audit.search.v1.AuditLogListQuery;
import eu.europa.ec.fisheries.schema.audit.source.v1.CreateAuditLogResponse;
import eu.europa.ec.fisheries.schema.audit.source.v1.GetAuditLogListByQueryResponse;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.audit.message.consumer.MessageConsumer;
import eu.europa.ec.fisheries.uvms.audit.message.exception.AuditMessageException;
import eu.europa.ec.fisheries.uvms.audit.message.producer.MessageProducer;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.audit.service.AuditService;
import eu.europa.ec.fisheries.uvms.audit.service.exception.AuditServiceException;

@Stateless
public class AuditServiceBean implements AuditService {

    final static Logger LOG = LoggerFactory.getLogger(AuditServiceBean.class);

    @EJB
    MessageConsumer consumer;

    @EJB
    MessageProducer producer;

    /**
     * {@inheritDoc}
     *
     * @param query
     * @return GetAuditLogListByQueryResponse
     * @throws AuditServiceException
     */
    @Override
    public GetAuditLogListByQueryResponse getList(AuditLogListQuery query) throws AuditServiceException {
        try {
            LOG.info("Get list invoked in service layer");
            String request = AuditDataSourceRequestMapper.mapGetListByQuery(query);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            if (response == null) {
                LOG.error("[ Error when getting list, response from JMS Queue is null ]");
                throw new AuditServiceException("[ Error when getting list, response from JMS Queue is null ]");
            }
            return AuditDataSourceResponseMapper.mapToGetAuditListByQueryResponse(response);
        } catch (AuditModelMarshallException | AuditMessageException | JMSException e) {
            LOG.error("[ Error when getting audit list by query ] {}", e.getMessage());
            throw new AuditServiceException("[ Error when getting audit list by query ]", e);
        } catch (ModelMapperException e) {
            LOG.error("[ Error when getting audit list by query ] {}", e.getMessage());
            throw new AuditServiceException("[ Error when getting audit list by query ]", e);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param auditLogType
     * @return CreateAuditLogResponse
     * @throws AuditServiceException
     */
    @Override
    public CreateAuditLogResponse createAuditLog(AuditLogType auditLogType) throws AuditServiceException {
        try {
            LOG.info("Create audit log invoked in service layer");
            String request = AuditDataSourceRequestMapper.mapCreateAuditLog(auditLogType);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            if (response == null) {
                LOG.error("[ Error when creating audit log, response from JMS Queue is null ]");
                throw new AuditServiceException("[ Error when creating audit log, response from JMS Queue is null ]");
            }
            return AuditDataSourceResponseMapper.mapToCreateAuditLogResponse(response);
        } catch (AuditModelMarshallException | AuditMessageException | JMSException e) {
            LOG.error("[ Error when creating audit log ] {}", e.getMessage());
            throw new AuditServiceException("[ Error when creating audit log ]", e);
        } catch (ModelMapperException e) {
            LOG.error("[ Error when creating audit log ] {}", e.getMessage());
            throw new AuditServiceException("[ Error when creating audit log ]", e);
        }

    }

}