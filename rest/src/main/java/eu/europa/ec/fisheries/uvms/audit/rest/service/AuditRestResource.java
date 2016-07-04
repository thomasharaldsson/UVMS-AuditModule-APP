/*
﻿﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.
 
This file is part of the Integrated Data Fisheries Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a copy
of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.audit.rest.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.audit.search.v1.AuditLogListQuery;
import eu.europa.ec.fisheries.uvms.audit.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.audit.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.audit.service.AuditService;
import eu.europa.ec.fisheries.uvms.audit.service.dto.AuditListResponseDto;
import eu.europa.ec.fisheries.uvms.audit.service.exception.AuditServiceException;
import eu.europa.ec.fisheries.uvms.rest.security.RequiresFeature;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;

@Path("/audit")
@Stateless
public class AuditRestResource {

    final static Logger LOG = LoggerFactory.getLogger(AuditRestResource.class);

    @EJB
    AuditService serviceLayer;

    /**
     *
     * @responseMessage 200 Audit list successfully retrieved
     * @responseMessage 500 Error when retrieving the list values
     *
     * @summary Gets a list of audit entries filtered by a query
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/list")
    @RequiresFeature(UnionVMSFeature.viewAudit)
    public ResponseDto<AuditListResponseDto> getListByQuery(AuditLogListQuery query) {
        LOG.info("Get list invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.getList(query), ResponseCode.OK);
        } catch (AuditServiceException | NullPointerException ex) {
            LOG.error("[ Error when getting list. ]", ex);
            return new ResponseDto(ex.getMessage(), ResponseCode.ERROR);
        }
    }

}