package com.alibaba.webx.restful.model.param;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.process.WebxRestfulRequestContext;

public class HeaderParameter extends LiteralParameter implements Parameter {

    public HeaderParameter(String name, TypeConverter typeConverter, Object defaultValue){
        super(name, typeConverter, defaultValue);
    }

    @Override
    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        HttpServletRequest httpRequest = requestContext.getHttpRequest();
        return httpRequest.getHeader(getName());
    }

    @Override
    public Source getSource() {
        return Source.HEADER;
    }

}
