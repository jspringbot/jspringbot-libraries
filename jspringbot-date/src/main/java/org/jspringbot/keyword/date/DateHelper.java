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

public class DateHelper {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(DateHelper.class);

    private DateTime current = new DateTime();

    private DateTimeZone currentTimeZone = DateTimeZone.getDefault();

    private String formatterPattern = "yyyy-MM-dd HH:mm:ss zz";

    public void setDateTimeZone(String timeZoneId) {

        LOG.createAppender()
                .appendBold("Set Date Time Zone:")
                .appendProperty("Time Zone ID", timeZoneId)
                .log();

        currentTimeZone = DateTimeZone.forID(timeZoneId);
    }

    public void setDateTimeFormat(String pattern) {
        LOG.createAppender()
                .appendBold("Set Date Time Format:")
                .appendProperty("Pattern", pattern)
                .log();

        DateTimeFormat.forPattern(pattern);
    }

    public String printDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatterPattern);
        DateTime dt = current.withZone(currentTimeZone);

        String formattedValue = dateTimeFormatter.print(dt);

        LOG.createAppender()
                .appendBold("Print Date Time:")
                .appendProperty("Pattern", formatterPattern)
                .appendProperty("Time Zone ID", currentTimeZone.getID())
                .appendProperty("Print", formattedValue)
                .log();

        return formattedValue;
    }

    public void resetDateTime() {
        current = new DateTime();

        LOG.createAppender()
                .appendBold("Reset Date Time:")
                .appendProperty("Pattern", formatterPattern)
                .appendProperty("Time Zone ID", currentTimeZone.getID())
                .appendProperty("Print", printDateTime())
                .log();
    }

    public void plusDays(int days) {
        current = current.plusDays(days);

        LOG.createAppender()
                .appendBold("Add Date Time Days:")
                .appendProperty("Days", days)
                .appendProperty("Print", printDateTime())
                .log();
    }

    public void minusDays(int days) {
        current = current.minusDays(days);

        LOG.createAppender()
                .appendBold("Subtract Date Time Days:")
                .appendProperty("Days", days)
                .appendProperty("Print", printDateTime())
                .log();
    }
}
