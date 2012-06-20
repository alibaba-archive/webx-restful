package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.PathParam;

import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;
import com.alibaba.webx.restful.server.process.converter.TypeConverter;

public class PathParamProvider extends AbstractParameterProvider {

    private String    parameterName;
    private PathParam annotation;

    public PathParamProvider(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                             Type paremeterType, Annotation[] parameterAnnotations, TypeConverter typeConverter){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations, typeConverter);

        for (Annotation item : parameterAnnotations) {
            if (item.getClass() == PathParam.class) {
                this.annotation = (PathParam) item;
            }
        }

        Assert.notNull(annotation);

        parameterName = annotation.value();
    }

    @Override
    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        throw new UnsupportedOperationException(); // TODO
    }

    public String getParameterName() {
        return parameterName;
    }

    public PathParam getAnnotation() {
        return annotation;
    }

}
