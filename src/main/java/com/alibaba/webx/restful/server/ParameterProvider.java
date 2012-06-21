package com.alibaba.webx.restful.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import com.alibaba.webx.restful.model.Parameter;


public interface ParameterProvider {
    Parameter createParameter(Class<?> clazz, Member method, String name, Class<?> paramClass, Type paramType, Annotation[] annotations);
}
