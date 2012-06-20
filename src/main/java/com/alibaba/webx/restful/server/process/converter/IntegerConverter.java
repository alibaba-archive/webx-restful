package com.alibaba.webx.restful.server.process.converter;


public class IntegerConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return Integer.parseInt(literalValue);
    }

}
