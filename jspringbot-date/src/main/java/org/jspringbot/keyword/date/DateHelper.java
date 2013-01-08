/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jspringbot.keyword.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jspringbot.syntax.HighlightRobotLogger;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;


public class DateHelper {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(DateHelper.class);

    private DateTime current = new DateTime();

    private DateTimeZone currentTimeZone = DateTimeZone.getDefault();

    private String formatterPattern = "yyyy-MM-dd HH:mm:ss zz";

    public void setDateTimeZone(String timeZoneId) {
        LOG.keywordAppender().appendProperty("Time Zone ID", timeZoneId);

        currentTimeZone = DateTimeZone.forID(timeZoneId);
    }

    public void setDateTimeFormat(String pattern) {
        LOG.keywordAppender().appendProperty("Pattern", pattern);

        DateTimeFormat.forPattern(pattern);
        this.formatterPattern = pattern;
    }

    public DateTime getCurrent() {
        return current;
    }


    public DateTimeZone getCurrentTimeZone() {
        return currentTimeZone;
    }

    public String getFormatterPattern() {
        return formatterPattern;
    }

    public String parseDateTime(String dateStr) {
        return parseDateTime(dateStr, formatterPattern);
    }

    public Date getUtilDate() {
        DateTime dt = current.withZone(currentTimeZone);

        return new Date(dt.getMillis());
    }

    public java.sql.Date getSQLDate() {
        DateTime dt = current.withZone(currentTimeZone);

        return new java.sql.Date(dt.getMillis());
    }

    public Time getSQLTime() {
        DateTime dt = current.withZone(currentTimeZone);

        return new Time(dt.getMillis());
    }

    public Timestamp getSQLTimestamp() {
        DateTime dt = current.withZone(currentTimeZone);

        return new Timestamp(dt.getMillis());
    }

    public DateTime getParseDateTime(String dateStr) {
        return getParseDateTime(dateStr, formatterPattern);
    }

    public DateTime getParseDateTime(String dateStr, String pattern) {
        LOG.keywordAppender()
                .appendProperty("Date String", dateStr)
                .appendProperty("Parse Pattern", pattern);

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
        return dateTimeFormatter.parseDateTime(dateStr);
    }

    public String parseDateTime(String dateStr, String pattern) {
        LOG.keywordAppender()
                .appendProperty("Date String", dateStr)
                .appendProperty("Parse Pattern", pattern);

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
        current = dateTimeFormatter.parseDateTime(dateStr);

        // show the log for print
        return formatDateTime();
    }

    public String formatDateTime(String pattern) {
        LOG.keywordAppender()
                .appendProperty("Print Pattern", formatterPattern)
                .appendProperty("Print Time Zone ID", currentTimeZone.getID());

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
        DateTime dt = current.withZone(currentTimeZone);

        String formattedValue = dateTimeFormatter.print(dt);

        LOG.keywordAppender().appendProperty("Print Result", formattedValue);

        return formattedValue;
    }


    public String formatDateTime() {
        return formatDateTime(formatterPattern);
    }

    public void resetDateTime() {
        current = new DateTime();

        // show the log for print
        formatDateTime();
    }

    public void plusDays(int days) {
        LOG.keywordAppender().appendProperty("Added Days", days);

        current = current.plusDays(days);

        // show the log for print
        formatDateTime();
    }

    public void minusDays(int days) {
        LOG.keywordAppender().appendProperty("Subtract Days", days);

        current = current.minusDays(days);

        // show the log for print
        formatDateTime();
    }
}
