package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;

import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class FormParamProvider extends AbstractParameterProvider {

    private String    parameterName;
    private FormParam annotation;

    public FormParamProvider(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                             Type paremeterType, Annotation[] parameterAnnotations){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations);

        for (Annotation item : parameterAnnotations) {
            if (item.getClass() == FormParam.class) {
                this.annotation = (FormParam) item;
            }
        }

        Assert.notNull(annotation);

        parameterName = annotation.value();
    }

    @Override
    public Object getParameterValue(WebxRestfulRequestContext requestContext) {
        HttpServletRequest httpRequest = requestContext.getHttpRequest();
        return httpRequest.getParameter(parameterName);
    }

}
