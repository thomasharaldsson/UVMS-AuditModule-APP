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
package eu.europa.ec.fisheries.uvms.audit.dao;

import java.util.List;

import javax.ejb.Local;

import eu.europa.ec.fisheries.uvms.audit.dao.exception.AuditDaoException;
import eu.europa.ec.fisheries.uvms.audit.entity.component.AuditLog;
import eu.europa.ec.fisheries.uvms.audit.mapper.search.SearchValue;

@Local
public interface AuditDao {

    /**
     * Delete entity from database
     *
     * @param auditId
     * @throws AuditDaoException
     */
    public void deleteEntity(Long auditId) throws AuditDaoException;

    /**
     * Get count of searched entities
     *
     * @return
     * @throws AuditDaoException
     */
    public Long getAuditListSearchCount(String countSql, List<SearchValue> searchKeyValues) throws AuditDaoException;

    /**
     *
     * Gets a subset of the audit logs based on page and list size
     *
     * @param page
     * @param listSize
     * @param sql
     * @param searchKeyValues
     * @return
     * @throws AuditDaoException
     */
    public List<AuditLog> getAuditListPaginated(Integer page, Integer listSize, String sql, List<SearchValue> searchKeyValues)
            throws AuditDaoException;

    /**
     * Create entity in database
     *
     * @param auditLog
     * @return
     * @throws AuditDaoException
     */
    public AuditLog createAuditLogEntity(AuditLog auditLog) throws AuditDaoException;

}