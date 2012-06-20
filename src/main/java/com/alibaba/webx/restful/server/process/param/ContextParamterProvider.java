package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public abstract class ContextParamterProvider extends AbstractParameterProvider implements ParameterProvider {

    public ContextParamterProvider(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                                  Type paremeterType, Annotation[] parameterAnnotations){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations);
    }

    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        throw new UnsupportedOperationException();
    }
}
