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
import eu.europa.ec.fisheries.uvms.audit.AuditDomainModel;
import eu.europa.ec.fisheries.uvms.audit.dao.AuditDao;
import eu.europa.ec.fisheries.uvms.audit.dao.exception.AuditDaoException;
import eu.europa.ec.fisheries.uvms.audit.dao.exception.AuditDaoMappingException;
import eu.europa.ec.fisheries.uvms.audit.dto.ListResponseDto;
import eu.europa.ec.fisheries.uvms.audit.entity.component.AuditLog;
import eu.europa.ec.fisheries.uvms.audit.mapper.Mapper;
import eu.europa.ec.fisheries.uvms.audit.mapper.search.SearchFieldMapper;
import eu.europa.ec.fisheries.uvms.audit.mapper.search.SearchValue;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelException;
import eu.europa.ec.fisheries.uvms.audit.model.exception.InputArgumentException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class AuditDomainModelBean implements AuditDomainModel {

    final static Logger LOG = LoggerFactory.getLogger(AuditDomainModelBean.class);

    @EJB
    private AuditDao dao;

    @EJB
    private Mapper mapper;

    @Override
    public ListResponseDto getAuditListByQuery(AuditLogListQuery query) throws AuditModelException, InputArgumentException {
        if (query == null) {
            throw new InputArgumentException("Audit list query is null");
        }

        if (query.getPagination() == null || query.getPagination().getListSize() == null || query.getPagination().getPage() == null) {
            throw new InputArgumentException("Pagination in audit query is null");
        }
        if (query.getAuditSearchCriteria() == null) {
            throw new InputArgumentException("No search criterias in audit list query");
        }

        try {
            ListResponseDto response = new ListResponseDto();
            List<AuditLogType> auditList = new ArrayList<>();

            Integer page = query.getPagination().getPage().intValue();
            Integer listSize = query.getPagination().getListSize().intValue();

            List<SearchValue> searchKeyValues = SearchFieldMapper.mapSearchField(query.getAuditSearchCriteria());

            String sql = SearchFieldMapper.createSelectSearchSql(searchKeyValues, true);
            String countSql = SearchFieldMapper.createCountSearchSql(searchKeyValues, true);

            Long numberMatches = dao.getAuditListSearchCount(countSql, searchKeyValues);

            List<AuditLog> movementEntityList = dao.getAuditListPaginated(page, listSize, sql, searchKeyValues);
            for (AuditLog entity : movementEntityList) {
                auditList.add(mapper.toModel(entity));
            }

            int numberOfPages = (int) (numberMatches / listSize);
            if (numberMatches % listSize != 0) {
                numberOfPages += 1;
            }

            response.setTotalNumberOfPages(new BigInteger("" + numberOfPages));
            response.setCurrentPage(query.getPagination().getPage());
            response.setAuditLogList(auditList);
            return response;
        } catch (AuditDaoMappingException | AuditDaoException | ParseException ex) {
            LOG.error("[ Error when getting movement by query :{}] {} ", query, ex.getMessage());
            throw new AuditModelException(ex.getMessage(), ex);
        }
    }

    @Override
    public AuditLogType createAuditLog(AuditLogType auditLogType) throws AuditModelException, InputArgumentException {
        try {
            AuditLog auditLog = mapper.toEntity(auditLogType);
            auditLog = dao.createAuditLogEntity(auditLog);
            return mapper.toModel(auditLog);
        } catch (AuditDaoException | AuditDaoMappingException e) {
            LOG.error("[ Error when creating audit log:{} ] {}", auditLogType, e.getMessage());
            throw new AuditModelException("Could not create audit log.", e);
        }
    }

}