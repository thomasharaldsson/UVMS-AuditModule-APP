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
package eu.europa.ec.fisheries.uvms.audit;

import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.entity.component.AuditLog;

import java.time.Instant;

public class MockData {
    private static final String DATE_TIME_PATTERN= "yyyy-MM-dd HH:mm:ss.SSS";

    public static AuditLogType getModel(int id) {
        AuditLogType dto = new AuditLogType();
        dto.setUsername("Username");
        dto.setOperation("Operation");
        dto.setObjectType("ObjectType");
        dto.setAffectedObject("AffectedObject");
        return dto;
    }

    public static AuditLog getEntity(long id) {
        Instant timestamp = Instant.now();
        AuditLog entity = new AuditLog();
        entity.setId(id);
        entity.setUsername("Username");
        entity.setOperation("Operation");
        entity.setObjectType("ObjectType");
        entity.setAffectedObject("AffectedObject");
        entity.setTimestamp(timestamp);
        entity.setUpdated(timestamp);
        entity.setUpdatedBy("UpdatedBy");
        return entity;
    }

}