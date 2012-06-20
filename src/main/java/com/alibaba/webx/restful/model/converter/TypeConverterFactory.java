package com.alibaba.webx.restful.model.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface TypeConverterFactory {

    TypeConverter create(Class<?> clazz, Type type, Annotation[] annotations);
}
