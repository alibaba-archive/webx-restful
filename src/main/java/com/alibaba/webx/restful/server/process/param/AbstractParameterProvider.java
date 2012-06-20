package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.DefaultValue;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;
import com.alibaba.webx.restful.server.process.converter.TypeConverter;

public abstract class AbstractParameterProvider implements ParameterProvider {

    private final Resource       resource;
    private final ResourceMethod resourceMethod;
    private final Class<?>       paremeterClass;
    private final Type           paremeterType;
    private final Annotation[]   parameterAnnotations;
    private String               defaultLiteralValue;

    private final TypeConverter  typeConverter;

    public AbstractParameterProvider(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                                     Type paremeterType, Annotation[] parameterAnnotations, TypeConverter typeConverter){
        this.resource = resource;
        this.resourceMethod = resourceMethod;
        this.paremeterClass = paremeterClass;
        this.paremeterType = paremeterType;
        this.parameterAnnotations = parameterAnnotations;

        for (Annotation item : parameterAnnotations) {
            if (item.getClass() == DefaultValue.class) {
                this.defaultLiteralValue = ((DefaultValue) item).value();
            }
        }

        this.typeConverter = typeConverter;
    }

    public TypeConverter getTypeConverter() {
        return typeConverter;
    }

    public abstract String getLiteralValue(WebxRestfulRequestContext requestContext);

    public Object getParameterValue(WebxRestfulRequestContext requestContext) {
        String literalValue = getLiteralValue(requestContext);

        if (literalValue == null) {
            literalValue = defaultLiteralValue;
        }

        throw new UnsupportedOperationException();
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
