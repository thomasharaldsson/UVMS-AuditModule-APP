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
package eu.europa.ec.fisheries.uvms.audit.bean;

import eu.europa.ec.fisheries.schema.audit.search.v1.AuditLogListQuery;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.dao.bean.AuditDaoBean;
import eu.europa.ec.fisheries.uvms.audit.dto.ListResponseDto;
import eu.europa.ec.fisheries.uvms.audit.entity.component.AuditLog;
import eu.europa.ec.fisheries.uvms.audit.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.audit.mapper.search.SearchFieldMapper;
import eu.europa.ec.fisheries.uvms.audit.mapper.search.SearchValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AuditDomainModelBean {

    final static Logger LOG = LoggerFactory.getLogger(AuditDomainModelBean.class);

    @Inject
    private AuditDaoBean auditDao;


    public ListResponseDto getAuditListByQuery(AuditLogListQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Audit list query is null");
        }

        if (query.getPagination() == null || query.getPagination().getListSize() == null || query.getPagination().getPage() == null) {
            throw new IllegalArgumentException("Pagination in audit query is null");
        }
        if (query.getAuditSearchCriteria() == null) {
            throw new IllegalArgumentException("No search criterias in audit list query");
        }

        ListResponseDto response = new ListResponseDto();
        List<AuditLogType> auditList = new ArrayList<>();

        Integer page = query.getPagination().getPage().intValue();
        Integer listSize = query.getPagination().getListSize().intValue();

        List<SearchValue> searchKeyValues = SearchFieldMapper.mapSearchField(query.getAuditSearchCriteria());

        String sql = SearchFieldMapper.createSelectSearchSql(searchKeyValues, true);
        String countSql = SearchFieldMapper.createCountSearchSql(searchKeyValues, true);

        Long numberMatches = auditDao.getAuditListSearchCount(countSql, searchKeyValues);

        List<AuditLog> movementEntityList = auditDao.getAuditListPaginated(page, listSize, sql, searchKeyValues);
        for (AuditLog entity : movementEntityList) {
            auditList.add(AuditLogMapper.toModel(entity));
        }

        int numberOfPages = (int) (numberMatches / listSize);
        if (numberMatches % listSize != 0) {
            numberOfPages += 1;
        }

        response.setTotalNumberOfPages(new BigInteger("" + numberOfPages));
        response.setCurrentPage(query.getPagination().getPage());
        response.setAuditLogList(auditList);
        return response;
    }

    public AuditLogType createAuditLog(AuditLogType auditLogType) {
        AuditLog auditLog = AuditLogMapper.toEntity(auditLogType);
        auditLog = auditDao.createAuditLogEntity(auditLog);
        return AuditLogMapper.toModel(auditLog);
    }

}