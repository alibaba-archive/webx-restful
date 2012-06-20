package com.alibaba.webx.restful.model.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class QueryParameter extends ParameterAdapter {

    private String     parameterName;
    private QueryParam annotation;

    public QueryParameter(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                              Type paremeterType, Annotation[] parameterAnnotations, TypeConverter typeConverter){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations, typeConverter);

        for (Annotation item : parameterAnnotations) {
            if (item.getClass() == QueryParam.class) {
                this.annotation = (QueryParam) item;
            }
        }

        Assert.notNull(annotation);

        parameterName = annotation.value();
    }

    @Override
    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        HttpServletRequest httpRequest = requestContext.getHttpRequest();
        return httpRequest.getParameter(parameterName);
    }

}
