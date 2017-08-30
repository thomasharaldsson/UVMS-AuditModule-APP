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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DomainModelBeanTest {

    @Test
    public void dummy() {

    }

    // @Mock
    // AuditDao dao;
    //
    // @Mock
    // Mapper mapper;
    //
    // @InjectMocks
    // private AuditDomainModelBean model;
    //
    // @Before
    // public void setUp() {
    // MockitoAnnotations.initMocks(this);
    // }
    //
    // @Test
    // public void testCreateModel() throws AuditModelException,
    // AuditDaoException, AuditDaoMappingException {
    // Integer id = 1;
    //
    // MyModel vessel = MockData.getModel(id.intValue());
    //
    // MyEntity entity = new MyEntity();
    // entity.setEntityId(id);
    //
    // when(mapper.toEntity(vessel)).thenReturn(entity);
    // when(dao.createEntity(any(MyEntity.class))).thenReturn(entity);
    // when(mapper.toModel(any(MyEntity.class))).thenReturn(vessel);
    //
    // MyModel result = model.create(vessel);
    // assertEquals(id.toString(), result.getId());
    // }
    //
    // @Test
    // public void testGetList() throws AuditModelException, AuditDaoException,
    // AuditDaoMappingException {
    // Integer id = 1;
    // MyModel dto = new MyModel();
    // dto.setId("" + id);
    //
    // when(mapper.toModel(any(MyEntity.class))).thenReturn(dto);
    //
    // List<MyEntity> allList = new ArrayList<>();
    // when(dao.getListAll()).thenReturn(allList);
    //
    // ListCriteria criteria = new ListCriteria();
    // List<MyModel> responseList = model.getList(criteria);
    // assertSame(allList.size(), responseList.size());
    //
    // allList = new ArrayList<>();
    // MyEntity vessel = new MyEntity();
    // vessel.setEntityId(id);
    // allList.add(vessel);
    //
    // when(dao.getListAll()).thenReturn(allList);
    //
    // responseList = model.getList(criteria);
    // assertSame(allList.size(), responseList.size());
    // String resultId = responseList.get(0).getId();
    // assertEquals(id.toString(), resultId);
    // }
    //
    // @Test
    // public void testGetModelById() throws AuditDaoException,
    // AuditModelException, AuditDaoMappingException {
    // Integer id = 1;
    // MyEntity entity = new MyEntity();
    // entity.setEntityId(id);
    //
    // MyModel dto = new MyModel();
    // dto.setId("" + id);
    //
    // when(mapper.toModel(any(MyEntity.class))).thenReturn(dto);
    // when(dao.getEntityById(id.toString())).thenReturn(entity);
    //
    // MyModel result = model.getById("" + id);
    // String internalId = result.getId();
    // assertEquals(id.toString(), internalId);
    // }

}