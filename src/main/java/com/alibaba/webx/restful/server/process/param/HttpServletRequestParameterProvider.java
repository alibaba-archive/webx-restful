package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;
import com.alibaba.webx.restful.server.process.converter.TypeConverter;

public class HttpServletRequestParameterProvider extends ContextParamterProvider {

    public HttpServletRequestParameterProvider(Resource resource, ResourceMethod resourceMethod,
                                               Class<?> paremeterClass, Type paremeterType,
                                               Annotation[] parameterAnnotations, TypeConverter typeConverter){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations, typeConverter);
    }

    @Override
    public Object getParameterValue(WebxRestfulRequestContext requestContext) {
        return requestContext.getHttpRequest();
    }

}
