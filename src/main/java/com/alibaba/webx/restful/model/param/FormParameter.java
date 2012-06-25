package com.alibaba.webx.restful.model.param;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.process.WebxRestfulRequestContext;

public class FormParameter extends LiteralParameter implements Parameter {

    public FormParameter(String name, TypeConverter typeConverter, Object defaultValue){
        super(name, typeConverter, defaultValue);
    }

    @Override
    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        HttpServletRequest httpRequest = requestContext.getHttpRequest();
        return httpRequest.getParameter(getName());
    }

    @Override
    public Source getSource() {
        return Source.FORM;
    }

}
