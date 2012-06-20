package com.alibaba.webx.restful.model.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class HttpServletRequestParameter extends ContextParamter {

    public HttpServletRequestParameter(Resource resource, ResourceMethod resourceMethod,
                                               Class<?> paremeterClass, Type paremeterType,
                                               Annotation[] parameterAnnotations, TypeConverter typeConverter){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations, typeConverter);
    }

    @Override
    public Object getParameterValue(WebxRestfulRequestContext requestContext) {
        return requestContext.getHttpRequest();
    }

}
