package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;

import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class HeaderParamProvider extends AbstractParameterProvider {

    private String      headerName;
    private HeaderParam annotation;

    public HeaderParamProvider(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                               Type paremeterType, Annotation[] parameterAnnotations){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations);

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
