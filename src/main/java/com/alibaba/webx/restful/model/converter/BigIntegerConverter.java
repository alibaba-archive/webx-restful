package com.alibaba.webx.restful.model.converter;

import java.math.BigInteger;


public class BigIntegerConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) {
        return new BigInteger(literalValue);
    }

}
