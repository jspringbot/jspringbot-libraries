package org.jspringbot.keyword.date;

import org.jspringbot.Keyword;
import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
@KeywordInfo(name = "Get Current Date", description = "Returns Current Date with the given pattern and timezone.", parameters = {"dateFormat","timezone=Asia/Manila"})
public class GetCurrentDate implements Keyword {

    @Override
    public Object execute(Object[] params) throws Exception {
        String pattern = String.valueOf(params[0]);
        String timezone = String.valueOf(params[1]);
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = new Date();

        return dateFormat.format(date);
    }
}