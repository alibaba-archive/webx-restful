package com.alibaba.webx.restful.server.process.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements TypeConverter {

    private String pattern;

    @Override
    public Object convert(String literalValue) throws TypeConvertException {
        if (literalValue.length() == 0) {
            return null;
        }

        if (literalValue.equals("now()")) {
            return new Date();
        }

        SimpleDateFormat dateFormat;
        if (pattern != null) {
            dateFormat = new SimpleDateFormat(pattern);
        } else {
            int spaceCount = 0;
            int dotCount = 0;
            for (int i = 0; i < literalValue.length(); ++i) {
                char ch = literalValue.charAt(i);
                if (ch == ' ') {
                    spaceCount++;
                } else if (ch == '.') {
                    dotCount++;
                }
            }

            if (spaceCount == 0) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            } else if (dotCount == 0) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            }
        }

        try {
            return dateFormat.parse(literalValue);
        } catch (ParseException e) {
            throw new TypeConvertException(e.getMessage(), e);
        }
    }

}
