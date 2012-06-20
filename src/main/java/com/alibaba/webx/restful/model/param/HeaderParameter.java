package com.alibaba.webx.restful.model.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;

import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class HeaderParameter extends ParameterAdapter {

    private String      headerName;
    private HeaderParam annotation;

    public HeaderParameter(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                               Type paremeterType, Annotation[] parameterAnnotations, TypeConverter typeConverter){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations, typeConverter);

        for (Annotation item : parameterAnnotations) {
            if (item.getClass() == HeaderParam.class) {
                this.annotation = (HeaderParam) item;
            }
        }

        Assert.notNull(annotation);

        headerName = annotation.value();
    }

    @Override
    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        HttpServletRequest httpRequest = requestContext.getHttpRequest();
        return httpRequest.getHeader(headerName);
    }

}
