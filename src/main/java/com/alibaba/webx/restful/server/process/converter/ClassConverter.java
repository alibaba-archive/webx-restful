package com.alibaba.webx.restful.server.process.converter;

public class ClassConverter implements TypeConverter {

    @Override
    public Object convert(String literalValue) throws TypeConvertException {
        if (literalValue == null) {
            return null;
        }

        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(literalValue);

            return clazz;
        } catch (ClassNotFoundException e) {
            throw new TypeConvertException(e.getMessage(), e);
        }
    }

}
