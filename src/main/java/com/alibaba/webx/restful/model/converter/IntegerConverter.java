package com.alibaba.webx.restful.model.converter;


public class IntegerConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return Integer.parseInt(literalValue);
    }

}
