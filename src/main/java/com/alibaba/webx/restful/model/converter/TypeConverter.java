package com.alibaba.webx.restful.model.converter;


public interface TypeConverter {
    Object convert(String literalValue) throws TypeConvertException;
}
