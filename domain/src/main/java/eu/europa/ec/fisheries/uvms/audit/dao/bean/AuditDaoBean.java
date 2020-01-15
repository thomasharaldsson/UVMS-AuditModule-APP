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

import eu.europa.ec.fisheries.uvms.audit.entity.component.AuditLog;
import eu.europa.ec.fisheries.uvms.audit.mapper.search.SearchValue;;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class AuditDaoBean {

    final static Logger LOG = LoggerFactory.getLogger(AuditDaoBean.class);

    @PersistenceContext(unitName = "auditPU")
    protected EntityManager em;

    public Long getAuditListSearchCount(String countSql, List<SearchValue> searchKeyValues)  {
        LOG.debug("SQL QUERY IN LIST COUNTNG: " + countSql);
        TypedQuery<Long> query = em.createQuery(countSql, Long.class);

        setQueryParameters(searchKeyValues, query);

        return query.getSingleResult();
    }

    public List<AuditLog> getAuditListPaginated(Integer page, Integer listSize, String sql, List<SearchValue> searchKeyValues) {
        LOG.debug("SQL QUERY IN LIST PAGINATED: " + sql);
        TypedQuery<AuditLog> query = em.createQuery(sql, AuditLog.class);

        setQueryParameters(searchKeyValues, query);

        query.setFirstResult(listSize * (page - 1));
        query.setMaxResults(listSize);
        return query.getResultList();
    }

    private <T> void setQueryParameters(List<SearchValue> searchKeyValues, TypedQuery<T> query) {
        for (SearchValue searchValue : searchKeyValues) {
            switch (searchValue.getField()) {
            case FROM_DATE:
                query.setParameter("fromDate", DateUtils.stringToDate(searchValue.getValue()));
                break;
            case TO_DATE:
                query.setParameter("toDate", DateUtils.stringToDate(searchValue.getValue()));
                break;
            }
        }
    }

    public AuditLog createAuditLogEntity(AuditLog auditLog) {
        em.persist(auditLog);
        return auditLog;
    }

}