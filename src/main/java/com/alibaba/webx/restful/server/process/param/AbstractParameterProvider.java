package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.ws.rs.DefaultValue;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;
import com.alibaba.webx.restful.server.process.converter.BigDecimalConverter;
import com.alibaba.webx.restful.server.process.converter.BigIntegerConverter;
import com.alibaba.webx.restful.server.process.converter.ByteConverter;
import com.alibaba.webx.restful.server.process.converter.DoubleConverter;
import com.alibaba.webx.restful.server.process.converter.FloatConverter;
import com.alibaba.webx.restful.server.process.converter.IntegerConverter;
import com.alibaba.webx.restful.server.process.converter.JSONConverter;
import com.alibaba.webx.restful.server.process.converter.LongConverter;
import com.alibaba.webx.restful.server.process.converter.ShortConverter;
import com.alibaba.webx.restful.server.process.converter.StringConverter;
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
                                     Type paremeterType, Annotation[] parameterAnnotations){
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

        if (byte.class == paremeterClass) {
            typeConverter = new ByteConverter();
        } else if (short.class == paremeterClass) {
            typeConverter = new ShortConverter();
        } else if (int.class == paremeterClass) {
            typeConverter = new IntegerConverter();
        } else if (long.class == paremeterClass) {
            typeConverter = new LongConverter();
        } else if (float.class == paremeterClass) {
            typeConverter = new FloatConverter();
        } else if (double.class == paremeterClass) {
            typeConverter = new DoubleConverter();
        } else if (BigInteger.class == paremeterClass) {
            typeConverter = new BigIntegerConverter();
        } else if (BigDecimal.class == paremeterClass) {
            typeConverter = new BigDecimalConverter();
        } else if (String.class == paremeterClass) {
            typeConverter = new StringConverter();
        } else {
            typeConverter = new JSONConverter(paremeterType);
        }
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
