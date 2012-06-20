package com.alibaba.webx.restful.server.process.converter;


public class FloatConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return Float.parseFloat(literalValue);
    }

}
