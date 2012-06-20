package com.alibaba.webx.restful.server.process.converter;


public class LongConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return Long.parseLong(literalValue);
    }

}
