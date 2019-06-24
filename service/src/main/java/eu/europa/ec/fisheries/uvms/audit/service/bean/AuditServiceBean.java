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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.audit.search.v1.AuditLogListQuery;
import eu.europa.ec.fisheries.schema.audit.source.v1.CreateAuditLogResponse;
import eu.europa.ec.fisheries.schema.audit.source.v1.GetAuditLogListByQueryResponse;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.AuditDomainModel;
import eu.europa.ec.fisheries.uvms.audit.dto.ListResponseDto;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelException;
import eu.europa.ec.fisheries.uvms.audit.model.exception.InputArgumentException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.audit.service.AuditService;
import eu.europa.ec.fisheries.uvms.audit.service.exception.AuditServiceException;

@Stateless
public class AuditServiceBean implements AuditService {

    final static Logger LOG = LoggerFactory.getLogger(AuditServiceBean.class);

    @EJB
    private AuditDomainModel model;

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
            ListResponseDto auditLogs = model.getAuditListByQuery(query);
            if (auditLogs == null) {
                LOG.error("[ Error when getting list, response from JMS Queue is null ]");
                throw new AuditServiceException("[ Error when getting list, response from JMS Queue is null ]");
            }
            return AuditLogMapper.mapAuditListResponseToAuditLogListByQuery(auditLogs);
        } catch (AuditModelException | InputArgumentException e) {
            LOG.error("[ Error when getting audit list by query {}] {}",query, e.getMessage());
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
            AuditLogType auditLog = model.createAuditLog(auditLogType);
            if (auditLog == null) {
                LOG.error("[ Error when creating audit log, response from JMS Queue is null ]");
                throw new AuditServiceException("[ Error when creating audit log, response from JMS Queue is null ]");      //eh what?
            }
            return AuditLogMapper.mapAuditLogTypeToAuditLogResponse(auditLog);
        } catch (AuditModelException | InputArgumentException e) {
            LOG.error("[ Error when creating audit log ] {}", e.getMessage());
            throw new AuditServiceException("[ Error when creating audit log ]", e);
        }

    }

}