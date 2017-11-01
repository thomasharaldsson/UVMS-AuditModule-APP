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
package eu.europa.ec.fisheries.uvms.audit.mapper;

import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.dao.exception.AuditDaoMappingException;
import eu.europa.ec.fisheries.uvms.audit.entity.component.AuditLog;
import eu.europa.ec.fisheries.uvms.audit.util.DateUtil;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.util.Date;

@Stateless
public class MapperBean implements Mapper {

    final static Logger LOG = LoggerFactory.getLogger(MapperBean.class);

    @Override
    public AuditLogType toModel(AuditLog auditlog) throws AuditDaoMappingException {
        try {
            AuditLogType model = new AuditLogType();
            model.setAffectedObject(auditlog.getAffectedObject());
            model.setOperation(auditlog.getOperation());
            model.setObjectType(auditlog.getObjectType());
            model.setTimestamp(DateUtil.getXMLGregorianCalendarInUTC(auditlog.getTimestamp()));
            model.setUsername(auditlog.getUsername());
            model.setComment(auditlog.getComment());
            return model;
        } catch (Exception e) {
            LOG.error("[ Error when mapping to model. ] {}", e.getMessage());
            throw new AuditDaoMappingException("[ Error when mapping to model. ]", e);
        }
    }

    @Override
    public AuditLog toEntity(AuditLogType auditLogType) throws AuditDaoMappingException {
        try {
            Date nowDateUTC = DateUtils.getNowDateUTC();
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
        } catch (Exception e) {
            LOG.error("[ Error when mapping to entity. ] {}", e.getMessage());
            throw new AuditDaoMappingException("[ Error when mapping to entity. ]", e);
        }
    }

}