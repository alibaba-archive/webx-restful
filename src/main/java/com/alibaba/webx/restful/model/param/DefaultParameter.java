package com.alibaba.webx.restful.model.param;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.process.RestfulRequestContext;

public class DefaultParameter extends LiteralParameter implements Parameter {

    public DefaultParameter(String name, TypeConverter typeConverter, Object defaultValue){
        super(name, typeConverter, defaultValue);
    }

    @Override
    public String getLiteralValue(RestfulRequestContext requestContext) {
        String name = getName();
        String value = requestContext.getPathVariables().get(name);

        if (value == null) {
            HttpServletRequest httpRequest = requestContext.getHttpRequest();
            value = httpRequest.getParameter(name);
        }

        return value;
    }

    @Override
    public Source getSource() {
        return Source.UNKNOWN;
    }

}
