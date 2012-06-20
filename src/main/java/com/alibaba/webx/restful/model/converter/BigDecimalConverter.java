package com.alibaba.webx.restful.model.converter;

import java.math.BigDecimal;


public class BigDecimalConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return new BigDecimal(literalValue);
    }

}
