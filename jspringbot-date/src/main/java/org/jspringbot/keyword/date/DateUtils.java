package org.jspringbot.keyword.date;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.base.BaseDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jspringbot.spring.ApplicationContextHolder;
import org.jspringbot.syntax.HighlightRobotLogger;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DateUtils {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(DateUtils.class);

    private static final Pattern AMEND_DATE_PATTERN = Pattern.compile("([\\+\\-])([0-9]+)([mdDMyYSshH])");

    private static DateHelper getHelper() {
        return ApplicationContextHolder.get().getBean(DateHelper.class);
    }

    public static java.util.Date toUtilDate(String... args) {
        if(args != null && args.length > 1) {
            parse(args);
        }

        return getHelper().getUtilDate();
    }

    public static Date toSQLDate(String... args) {
        if(args != null && args.length > 1) {
            parse(args);
        }

        return getHelper().getSQLDate();
    }

    public static Time toSQLTime(String... args) {
        if(args != null && args.length > 1) {
            parse(args);
        }

        return getHelper().getSQLTime();
    }

    public static Timestamp toSQLTimestamp(String... args) {
        if(args != null && args.length > 1) {
            parse(args);
        }

        return getHelper().getSQLTimestamp();
    }

    public static String parse(String... args) {
        if(args.length == 2) {
            return getHelper().parseDateTime(args[0], args[1]);
        } else if(args.length == 1) {
            return getHelper().parseDateTime(args[0]);
        }

        throw new IllegalArgumentException("Expected invocation parse(date_str, parsePattern) or parse(date_str).");
    }

    public static String current(String... args) {
        return format(getHelper().getCurrent(), "current", args);
    }

    public static String midnight(String... args) {
        return format(new DateMidnight(), "midnight", args);
    }

    public static String now(String... args) {
        return format(new DateTime(), "now", args);
    }

    private static String format(BaseDateTime dt, String method, String... args) {
        DateTime dateTime = dt.toDateTime();


        if(args == null || args.length == 0) {
            return format(dateTime);
        }

        Matcher matcher = AMEND_DATE_PATTERN.matcher(args[0]);


        boolean firstArgAmend = false;
        int startIndex = 0;
        while(matcher.find(startIndex)) {
            firstArgAmend = true;
            char operator = matcher.group(1).charAt(0);
            int amount = Integer.parseInt(matcher.group(2));
            char time = matcher.group(3).charAt(0);

            switch (time) {
                case 'y':
                case 'Y':
                    switch (operator) {
                        case '+': dateTime = dateTime.plusYears(amount); break;
                        case '-': dateTime = dateTime.minusYears(amount); break;
                    }
                    break;
                case 'M':
                    switch (operator) {
                        case '+': dateTime = dateTime.plusMonths(amount); break;
                        case '-': dateTime = dateTime.minusMonths(amount); break;
                    }
                    break;
                case 'D':
                case 'd':
                    switch (operator) {
                        case '+': dateTime = dateTime.plusDays(amount); break;
                        case '-': dateTime = dateTime.minusDays(amount); break;
                    }
                    break;
                case 'H':
                case 'h':
                    switch (operator) {
                        case '+': dateTime = dateTime.plusHours(amount); break;
                        case '-': dateTime = dateTime.minusHours(amount); break;
                    }
                    break;
                case 'm':
                    switch (operator) {
                        case '+': dateTime = dateTime.plusMinutes(amount); break;
                        case '-': dateTime = dateTime.minusMinutes(amount); break;
                    }
                    break;
                case 's':
                case 'S':
                    switch (operator) {
                        case '+': dateTime = dateTime.plusSeconds(amount); break;
                        case '-': dateTime = dateTime.minusSeconds(amount); break;
                    }
                    break;
            }

            startIndex = matcher.end();
        }

        if(firstArgAmend && args.length == 1) {
            return format(dateTime);
        } else if(!firstArgAmend && args.length == 1) {
            return format(dateTime, args[0]);
        } else if(args.length > 2) {
            return format(dateTime, args[1]);
        }

        throw new IllegalArgumentException(String.format("Expected invocation %s() or %s(format) or %s(amend_date, format) or %s(amend_date).", method, method, method, method));
    }

    private static String format(DateTime dateTime) {
        return format(dateTime, getHelper().getFormatterPattern());
    }

    private static String format(DateTime dateTime, String formatPattern) {
        DateTimeZone currentTimeZone = getHelper().getCurrentTimeZone();

        LOG.keywordAppender()
                .appendProperty("Print Pattern", formatPattern)
                .appendProperty("Print Time Zone ID", currentTimeZone);

        DateTime formatDt = dateTime.withZone(currentTimeZone);

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatPattern);
        return dateTimeFormatter.print(formatDt);
    }
}
