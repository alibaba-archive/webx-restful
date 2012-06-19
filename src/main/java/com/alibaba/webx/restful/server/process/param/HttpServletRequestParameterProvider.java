package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class HttpServletRequestParameterProvider extends ContextParamterProvider {

    public HttpServletRequestParameterProvider(Resource resource, ResourceMethod resourceMethod,
                                               Class<?> paremeterClass, Type paremeterType,
                                               Annotation[] parameterAnnotations){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations);
    }

    @Override
    public Object getParameterValue(WebxRestfulRequestContext requestContext) {
        return requestContext.getHttpRequest();
    }

}
