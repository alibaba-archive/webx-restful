package com.alibaba.webx.restful.server.process.converter;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;

public class JSONConverter implements TypeConverter {

    private final Type type;

    public JSONConverter(Type type){
        this.type = type;
    }

    @Override
    public Object convert(String literalValue) throws TypeConvertException {
        Object value = JSON.parseObject(literalValue, type);
        return value;
    }

    public Type getType() {
        return type;
    }

}
