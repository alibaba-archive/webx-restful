package com.alibaba.webx.restful.model.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TypeConverterFactoryImpl implements TypeConverterFactory {

    private final static TypeConverterFactoryImpl instance = new TypeConverterFactoryImpl();

    public static TypeConverterFactoryImpl getInstance() {
        return instance;
    }

    @Override
    public TypeConverter create(Class<?> clazz, Type type, Annotation[] annotations) {
        TypeConverter typeConverter;
        if (byte.class == clazz) {
            typeConverter = new ByteConverter();
        } else if (short.class == clazz) {
            typeConverter = new ShortConverter();
        } else if (int.class == clazz) {
            typeConverter = new IntegerConverter();
        } else if (long.class == clazz) {
            typeConverter = new LongConverter();
        } else if (float.class == clazz) {
            typeConverter = new FloatConverter();
        } else if (double.class == clazz) {
            typeConverter = new DoubleConverter();
        } else if (BigInteger.class == clazz) {
            typeConverter = new BigIntegerConverter();
        } else if (BigDecimal.class == clazz) {
            typeConverter = new BigDecimalConverter();
        } else if (String.class == clazz) {
            typeConverter = new StringConverter();
        } else {
            typeConverter = new JSONConverter(type);
        }

        return typeConverter;
    }

}
