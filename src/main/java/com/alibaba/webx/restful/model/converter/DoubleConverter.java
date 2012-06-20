package com.alibaba.webx.restful.model.converter;


public class DoubleConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return Double.parseDouble(literalValue);
    }

}
