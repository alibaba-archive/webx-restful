package com.alibaba.webx.restful.model.converter;

public class StringConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return literalValue;
    }

}
