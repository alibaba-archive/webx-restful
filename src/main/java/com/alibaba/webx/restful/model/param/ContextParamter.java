package com.alibaba.webx.restful.model.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public abstract class ContextParamter extends ParameterAdapter implements Parameter {

    public ContextParamter(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                                   Type paremeterType, Annotation[] parameterAnnotations, TypeConverter typeConverter){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations, typeConverter);
    }

    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        throw new UnsupportedOperationException();
    }
}
