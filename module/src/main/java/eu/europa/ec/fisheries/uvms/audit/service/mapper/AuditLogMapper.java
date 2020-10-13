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
package eu.europa.ec.fisheries.uvms.audit.service.mapper;

import eu.europa.ec.fisheries.schema.audit.source.v1.CreateAuditLogResponse;
import eu.europa.ec.fisheries.schema.audit.source.v1.GetAuditLogListByQueryResponse;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.dto.ListResponseDto;
import eu.europa.ec.fisheries.uvms.audit.service.entity.component.AuditLog;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;

import java.time.Instant;


public class AuditLogMapper {


    public static AuditLogType toModel(AuditLog auditlog) {
        AuditLogType model = new AuditLogType();
        model.setAffectedObject(auditlog.getAffectedObject());
        model.setOperation(auditlog.getOperation());
        model.setObjectType(auditlog.getObjectType());
        model.setTimestamp(DateUtils.dateToEpochMilliseconds(auditlog.getTimestamp()));
        model.setUsername(auditlog.getUsername());
        model.setComment(auditlog.getComment());
        return model;
    }

    public static AuditLog toEntity(AuditLogType auditLogType) {
        Instant nowDateUTC = Instant.now();
        AuditLog auditlog = new AuditLog();
        auditlog.setUsername(auditLogType.getUsername());
        auditlog.setOperation(auditLogType.getOperation());
        auditlog.setObjectType(auditLogType.getObjectType());
        auditlog.setTimestamp(nowDateUTC);
        auditlog.setAffectedObject(auditLogType.getAffectedObject());
        auditlog.setUpdated(nowDateUTC);
        auditlog.setUpdatedBy(auditLogType.getUsername());
        auditlog.setComment(auditLogType.getComment());
        return auditlog;
    }


    public static GetAuditLogListByQueryResponse mapAuditListResponseToAuditLogListByQuery(ListResponseDto responseDto) {
        GetAuditLogListByQueryResponse response = new GetAuditLogListByQueryResponse();
        response.getAuditLog().addAll(responseDto.getAuditLogList());
        response.setCurrentPage(responseDto.getCurrentPage());
        response.setTotalNumberOfPages(responseDto.getTotalNumberOfPages());
        return response;
    }

    public static CreateAuditLogResponse mapAuditLogTypeToAuditLogResponse(AuditLogType auditLog) {
        CreateAuditLogResponse response = new CreateAuditLogResponse();
        response.setAuditLog(auditLog);
        return response;
    }

}