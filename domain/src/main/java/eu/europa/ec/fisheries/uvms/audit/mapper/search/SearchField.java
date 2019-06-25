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
package eu.europa.ec.fisheries.uvms.audit.mapper.search;


import java.time.Instant;

public enum SearchField {

    // @formatter:off
    USER("username", "USER", SearchTables.AUDIT, String.class),
    OPERATION("operation", "OPERATION", SearchTables.AUDIT, String.class),
    TYPE("objectType", "TYPE", SearchTables.AUDIT, String.class),
    TO_DATE("timestamp", "TIMESTAMP", SearchTables.AUDIT, Instant.class),
    FROM_DATE("timestamp", "TIMESTAMP", SearchTables.AUDIT, Instant.class);
    // @formatter:on

    private final String fieldName;
    private final String fieldValue;
    private final SearchTables searchTables;
    private Class clazz;

    SearchField(String fieldName, String fieldValue, SearchTables searchTables, Class clazz) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.searchTables = searchTables;
        this.clazz = clazz;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SearchTables getSearchTables() {
        return searchTables;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public Class getClazz() {
        return clazz;
    }

}