package com.alibaba.webx.restful.model.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.model.converter.TypeConverterFactory;
import com.alibaba.webx.restful.model.converter.TypeConverterFactoryImpl;
import com.alibaba.webx.restful.server.ParameterProvider;

public class ParameterProviderImpl implements ParameterProvider {

    private final static Log     LOG                   = LogFactory.getLog(ParameterProviderImpl.class);
    private TypeConverterFactory typeConverterProvider = new TypeConverterFactoryImpl();

    @Override
    public Parameter createParameter(Class<?> clazz, Member method, String name, Class<?> paramClass, Type paramType,
                                     Annotation[] annotations) {

        Context contextAnnotation = null;

        DefaultValue defaultValueAnnotation = null;

        CookieParam cookieParam = null;
        FormParam formParam = null;
        QueryParam queryParam = null;
        PathParam pathParam = null;

        for (Annotation annotation : annotations) {
            Class<?> annotationType = annotation.annotationType();
            if (annotationType == Context.class) {
                contextAnnotation = (Context) annotation;

            } else if (annotationType == DefaultValue.class) {
                defaultValueAnnotation = (DefaultValue) annotation;

            } else if (annotationType == CookieParam.class) {
                cookieParam = (CookieParam) annotation;
            } else if (annotationType == FormParam.class) {
                formParam = (FormParam) annotation;
            } else if (annotationType == QueryParam.class) {
                queryParam = (QueryParam) annotation;
            } else if (annotationType == PathParam.class) {
                pathParam = (PathParam) annotation;
            }
        }

        if (paramClass == HttpServletRequest.class) {
            if (contextAnnotation == null) {
                LOG.warn("HttpServletRequest parameter not contains @Context annotation: " + method.toString());
            }
            return new HttpServletRequestParameter();
        }

        if (paramClass == HttpServletResponse.class) {
            if (contextAnnotation == null) {
                LOG.warn("HttpServletRequest parameter not contains @Context annotation: " + method.toString());
            }
            return new HttpServletResponseParameter();
        }

        TypeConverter typeConverter = typeConverterProvider.create(paramClass, paramType, annotations);
        Object defaultValue = getDefaultValue(method, defaultValueAnnotation, typeConverter);

        if (cookieParam != null) {
            String cookieName = cookieParam.value();
            return new CookieParameter(cookieName, typeConverter, defaultValue);
        }

        if (formParam != null) {
            String paramName = formParam.value();
            return new FormParameter(paramName, typeConverter, defaultValue);
        }

        if (queryParam != null) {
            String paramName = queryParam.value();
            return new QueryParameter(paramName, typeConverter, defaultValue);
        }

        if (pathParam != null) {
            String paramName = pathParam.value();
            return new PathParameter(paramName, typeConverter, defaultValue);
        }

        return new DefaultParameter(name, typeConverter, defaultValue);
    }

    private Object getDefaultValue(Member method, DefaultValue defaultValueAnnotation, TypeConverter typeConverter) {
        Object defaultValue = null;
        if (defaultValueAnnotation != null) {
            String defaultLiteralValue = defaultValueAnnotation.value();
            try {
                defaultValue = typeConverter.convert(defaultLiteralValue);
            } catch (Exception e) {
                LOG.error("parse defaultValue error : " + method);
            }
        }
        return defaultValue;
    }
}
