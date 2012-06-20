package com.alibaba.webx.restful.server.process.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.CookieParam;

import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class CookieParamProvider extends AbstractParameterProvider {

    private String      cookieName;
    private CookieParam annotation;

    public CookieParamProvider(Resource resource, ResourceMethod resourceMethod, Class<?> paremeterClass,
                               Type paremeterType, Annotation[] parameterAnnotations){
        super(resource, resourceMethod, paremeterClass, paremeterType, parameterAnnotations);

        for (Annotation item : parameterAnnotations) {
            if (item.getClass() == CookieParam.class) {
                this.annotation = (CookieParam) item;
            }
        }

        Assert.notNull(annotation);

        cookieName = annotation.value();
    }

    @Override
    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        HttpServletRequest httpRequest = requestContext.getHttpRequest();
        
        String value = null;
        for (Cookie cookie : httpRequest.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                value = cookie.getValue();
                break;
            }
        }
        
        return value;
    }

}
