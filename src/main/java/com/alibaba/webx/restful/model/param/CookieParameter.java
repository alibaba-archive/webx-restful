package com.alibaba.webx.restful.model.param;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.process.WebxRestfulRequestContext;

public class CookieParameter extends LiteralParameter implements Parameter {

    public CookieParameter(String name, TypeConverter typeConverter, Object defaultValue){
        super(name, typeConverter, defaultValue);
    }

    @Override
    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        HttpServletRequest httpRequest = requestContext.getHttpRequest();

        String cookieName = getName();

        String value = null;
        for (Cookie cookie : httpRequest.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                value = cookie.getValue();
                break;
            }
        }

        return value;
    }

    @Override
    public Source getSource() {
        return Source.COOKIE;
    }

}
