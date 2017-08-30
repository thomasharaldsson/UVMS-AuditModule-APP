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
package eu.europa.ec.fisheries.uvms.audit.dao.bean;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import eu.europa.ec.fisheries.uvms.audit.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.audit.dao.AuditDao;
import eu.europa.ec.fisheries.uvms.audit.dao.exception.AuditDaoException;
import eu.europa.ec.fisheries.uvms.audit.entity.component.AuditLog;
import eu.europa.ec.fisheries.uvms.audit.mapper.search.SearchValue;
import eu.europa.ec.fisheries.uvms.audit.util.DateUtil;

@Stateless
public class AuditDaoBean extends Dao implements AuditDao {

    final static Logger LOG = LoggerFactory.getLogger(AuditDaoBean.class);

    @Override
    public void deleteEntity(Long id) throws AuditDaoException {
        LOG.info("Delete Entity not implemented yet.");
        throw new AuditDaoException("Not implemented yet", null);
    }

    @Override
    public Long getAuditListSearchCount(String countSql, List<SearchValue> searchKeyValues) throws AuditDaoException {
        LOG.debug("SQL QUERY IN LIST COUNTNG: " + countSql);
        TypedQuery<Long> query = em.createQuery(countSql, Long.class);

        for (SearchValue searchValue : searchKeyValues) {
            switch (searchValue.getField()) {
            case FROM_DATE:
                query.setParameter("fromDate", DateUtil.parseToUTCDate(searchValue.getValue()));
                break;
            case TO_DATE:
                query.setParameter("toDate", DateUtil.parseToUTCDate(searchValue.getValue()));
                break;
            }
        }

        return query.getSingleResult();
    }

    @Override
    public List<AuditLog> getAuditListPaginated(Integer page, Integer listSize, String sql, List<SearchValue> searchKeyValues)
            throws AuditDaoException {
        try {
            LOG.debug("SQL QUERY IN LIST PAGINATED: " + sql);
            TypedQuery<AuditLog> query = em.createQuery(sql, AuditLog.class);

            for (SearchValue searchValue : searchKeyValues) {
                switch (searchValue.getField()) {
                case FROM_DATE:
                    query.setParameter("fromDate", DateUtil.parseToUTCDate(searchValue.getValue()));
                    break;
                case TO_DATE:
                    query.setParameter("toDate", DateUtil.parseToUTCDate(searchValue.getValue()));
                    break;
                }
            }

            query.setFirstResult(listSize * (page - 1));
            query.setMaxResults(listSize);

            return query.getResultList();
        } catch (IllegalArgumentException e) {
            LOG.error("[ Error getting movement list paginated ] {}", e.getMessage());
            throw new AuditDaoException("[ Error when getting list ] ", e);
        } catch (Exception e) {
            LOG.error("[ Error getting movement list paginated ]  {}", e.getMessage());
            throw new AuditDaoException("[ Error when getting list ] ", e);
        }
    }

    @Override
    public AuditLog createAuditLogEntity(AuditLog auditLog) throws AuditDaoException {
        try {
            em.persist(auditLog);
            return auditLog;
        } catch (Exception e) {
            LOG.error("[ Error when creating. ] {}", e.getMessage());
            throw new AuditDaoException("[ Error when creating. ]", e);
        }
    }

}