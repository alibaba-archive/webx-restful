package com.alibaba.webx.restful.server.process.converter;


public interface TypeConverter {
    Object convert(String literalValue) throws TypeConvertException;
}
