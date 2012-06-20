package com.alibaba.webx.restful.model.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;

import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class FormParameter extends ParameterAdapter {

    private String    parameterName;
    private FormParam annotation;

    public FormParameter(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                             Type paremeterType, Annotation[] parameterAnnotations, TypeConverter typeConverter){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations, typeConverter);

        for (Annotation item : parameterAnnotations) {
            if (item.getClass() == FormParam.class) {
                this.annotation = (FormParam) item;
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
