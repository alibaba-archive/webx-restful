package com.alibaba.webx.restful.model.converter;


public class ShortConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return Short.parseShort(literalValue);
    }

}
