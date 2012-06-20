package com.alibaba.webx.restful.model.converter;


public class FloatConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return Float.parseFloat(literalValue);
    }

}
