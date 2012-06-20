package com.alibaba.webx.restful.server.process.converter;

public class StringConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return literalValue;
    }

}
