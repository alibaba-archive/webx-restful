package com.alibaba.webx.restful.model.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.DefaultValue;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public abstract class ParameterAdapter implements Parameter {

    private final Resource       resource;
    private final ResourceMethod resourceMethod;
    private final Class<?>       paremeterClass;
    private final Type           paremeterType;
    private final Annotation[]   parameterAnnotations;
    private String               defaultLiteralValue;

    private final TypeConverter  typeConverter;

    public ParameterAdapter(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
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

    @Override
    public Source getSource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

}
