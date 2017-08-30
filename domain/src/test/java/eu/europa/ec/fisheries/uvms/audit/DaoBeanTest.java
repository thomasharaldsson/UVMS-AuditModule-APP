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

import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import eu.europa.ec.fisheries.uvms.audit.dao.bean.AuditDaoBean;
import eu.europa.ec.fisheries.uvms.audit.dao.exception.AuditDaoException;
import eu.europa.ec.fisheries.uvms.audit.entity.component.AuditLog;

@RunWith(MockitoJUnitRunner.class)
public class DaoBeanTest {

    @Mock
    EntityManager em;

    @InjectMocks
    private AuditDaoBean dao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAuditLogEntity() throws AuditDaoException {
        AuditLog auditLog = new AuditLog();
        dao.createAuditLogEntity(auditLog);
        verify(em).persist(auditLog);
    }

    @Test
    public void testDeleteVessel() throws AuditDaoException {
        // em.remove(arg0);
    }

    // @Test
    // public Long getAuditListSearchCount(String countSql, List<SearchValue>
    // searchKeyValues) throws AuditDaoException {}

}