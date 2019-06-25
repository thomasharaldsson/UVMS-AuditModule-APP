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
package eu.europa.ec.fisheries.uvms.audit.entity.component;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.Instant;


@Entity
@Table(name = "auditlog")
@XmlRootElement
public class AuditLog implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "audit_id")
    private Long id;

    // The user connected to the entry when logged (as opposed to updatedBy,
    // which would be someone fiddling with the entry once it has been logged)
    @Size(max = 60)
    @Column(name = "audit_user")
    private String username;

    @Size(max = 100)
    @Column(name = "audit_operation")
    private String operation;

    @Size(max = 60)
    @Column(name = "audit_type")
    private String objectType;

    @Column(name = "audit_timestamp")
    private Instant timestamp;

    @Size(max = 500)
    @Column(name = "audit_affectedobject")
    private String affectedObject;

    @Basic(optional = false)
    @NotNull
    @Column(name = "audit_updattim")
    private Instant updated;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "audit_upuser")
    private String updatedBy;

    @Size(max = 500)
    @Column(name = "audit_comment")
    private String comment;

    @PrePersist
    @PreUpdate
    public void prePersisOrUpdate(){
        if (StringUtils.isEmpty(updatedBy)){
            setUpdatedBy("UNKNOWN");
        }
        if (updated == null){
            setUpdated(Instant.now());
        }
    }
    
    public AuditLog() {
    }

    public AuditLog(Long moveId) {
        this.id = moveId;
    }

    public AuditLog(Long id, Instant updated, String updatedBy) {
        this.id = id;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getAffectedObject() {
        return affectedObject;
    }

    public void setAffectedObject(String affectedObject) {
        this.affectedObject = affectedObject;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditLog)) {
            return false;
        }
        AuditLog other = (AuditLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditEntry [id=" + id + ", username=" + username + ", operation=" + operation + ", objectType=" + objectType + ", timestamp="
                + timestamp + ", affectedObject=" + affectedObject + "]";
    }

}