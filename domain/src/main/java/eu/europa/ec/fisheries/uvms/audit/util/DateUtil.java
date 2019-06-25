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
package eu.europa.ec.fisheries.uvms.audit.util;

import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    final static String FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    public static Instant parseToUTCDate(String dateString) throws IllegalArgumentException {
        if (dateString != null) {

            return ZonedDateTime.parse(dateString, java.time.format.DateTimeFormatter.ofPattern(FORMAT)).toInstant();
        } else
            return null;
    }

    public static String parseUTCDateToString(Instant date) {
        if (date != null) {
            return date.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(FORMAT));
        }
        return null;
    }
}