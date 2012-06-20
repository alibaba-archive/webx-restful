package com.alibaba.webx.restful.server.process.converter;

import java.math.BigDecimal;


public class BIgDecimalConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return new BigDecimal(literalValue);
    }

}
