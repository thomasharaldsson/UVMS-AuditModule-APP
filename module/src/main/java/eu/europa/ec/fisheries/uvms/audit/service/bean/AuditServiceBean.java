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

import eu.europa.ec.fisheries.schema.audit.search.v1.AuditLogListQuery;
import eu.europa.ec.fisheries.schema.audit.source.v1.CreateAuditLogResponse;
import eu.europa.ec.fisheries.schema.audit.source.v1.GetAuditLogListByQueryResponse;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.dto.ListResponseDto;
import eu.europa.ec.fisheries.uvms.audit.service.mapper.AuditLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AuditServiceBean {

    final static Logger LOG = LoggerFactory.getLogger(AuditServiceBean.class);

    @Inject
    private AuditDomainModelBean model;

    /**
     * {@inheritDoc}
     *
     * @param query
     * @return GetAuditLogListByQueryResponse
     */
    public GetAuditLogListByQueryResponse getList(AuditLogListQuery query) {
        ListResponseDto auditLogs = model.getAuditListByQuery(query);
        return AuditLogMapper.mapAuditListResponseToAuditLogListByQuery(auditLogs);

    }

    /**
     * {@inheritDoc}
     *
     * @param auditLogType
     * @return CreateAuditLogResponse
     */
    public CreateAuditLogResponse createAuditLog(AuditLogType auditLogType) {
            AuditLogType auditLog = model.createAuditLog(auditLogType);
            return AuditLogMapper.mapAuditLogTypeToAuditLogResponse(auditLog);

    }

}