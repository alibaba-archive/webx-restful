package com.alibaba.webx.restful.server.process.converter;


public class ByteConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return Byte.parseByte(literalValue);
    }

}
