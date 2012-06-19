package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;

public abstract class AbstractParameterProvider implements ParameterProvider {

    private final Resource       resource;
    private final ResourceMethod resourceMethod;
    private final Class<?>       paremeterClass;
    private final Type           paremeterType;
    private final Annotation[]   parameterAnnotations;

    public AbstractParameterProvider(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                                     Type paremeterType, Annotation[] parameterAnnotations){
        this.resource = resource;
        this.resourceMethod = resourceMethod;
        this.paremeterClass = paremeterClass;
        this.paremeterType = paremeterType;
        this.parameterAnnotations = parameterAnnotations;
    }

    public ResourceMethod getResourceMethod() {
        return resourceMethod;
    }

    public Class<?> getParemeterClass() {
        return paremeterClass;
    }

    public Type getParemeterType() {
        return paremeterType;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public Resource getResource() {
        return resource;
    }

}
