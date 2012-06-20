package com.alibaba.webx.restful.server.process.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface TypeConverterFactory {

    TypeConverter create(Class<?> clazz, Type type, Annotation[] annotations);
}
