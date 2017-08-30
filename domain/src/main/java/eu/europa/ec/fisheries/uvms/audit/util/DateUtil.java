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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.LoggerFactory;

public class DateUtil {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    final static String FORMAT = "yyyy-MM-dd HH:mm:ss Z";
    private static final String DATE_TIME_SQL_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATE_TIME_JAVA_FORMAT= "EE MMM dd HH:mm:ss zz yyyy";

    public static java.sql.Timestamp getDateFromString(String inDate) throws ParseException {
        Date date = parseToUTCDate(inDate);
        return new java.sql.Timestamp(date.getTime());
    }

    public static Date parseToUTCDate(String dateString) throws IllegalArgumentException {
        try {
            if (dateString != null) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT).withOffsetParsed();
                DateTime dateTime = formatter.withZoneUTC().parseDateTime(dateString);
                GregorianCalendar cal = dateTime.toGregorianCalendar();
                return cal.getTime();
            } else
                return null;
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public static String parseUTCDateToString(Date date) {
        String dateString = null;
        if (date != null) {
            DateFormat df = new SimpleDateFormat(FORMAT);
            dateString = df.format(date);
        }
        return dateString;
    }

    public static Date parseTimestamp(XMLGregorianCalendar timestamp) {
        if (timestamp != null) {
            return timestamp.toGregorianCalendar().getTime();
        }
        return null;
    }

    public static XMLGregorianCalendar parseTimestamp(Date timestamp) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(timestamp);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException ex) {
        }
        return xmlCalendar;
    }

    public static XMLGregorianCalendar getXMLGregorianCalendarInUTC(Date dateTimeInUTC){
        if (dateTimeInUTC != null) {
            GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat sdf = null;
            if (dateTimeInUTC instanceof Timestamp){
                sdf = new SimpleDateFormat(DATE_TIME_SQL_FORMAT);
            }else {
                sdf = new SimpleDateFormat(DATE_TIME_JAVA_FORMAT, Locale.US);
            }
            try {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date theDate = sdf.parse(dateTimeInUTC.toString());
                calendar.setTime(theDate);
                return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            } catch (DatatypeConfigurationException e) {
                LOG.error("[ Error when getting XML Gregorian calendar. ] ", e);
            } catch (ParseException e) {
                LOG.error("Could not parse dateTimeInUTC: "+dateTimeInUTC.toString());
            }
        }
        return null;
    }
}