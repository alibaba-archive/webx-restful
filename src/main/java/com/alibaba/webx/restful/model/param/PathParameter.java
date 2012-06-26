package com.alibaba.webx.restful.model.param;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.process.WebxRestfulRequestContext;

public class PathParameter extends LiteralParameter implements Parameter {

    public PathParameter(String name, TypeConverter typeConverter, Object defaultValue){
        super(name, typeConverter, defaultValue);
    }

    @Override
    public String getLiteralValue(WebxRestfulRequestContext requestContext) {
        String varName = this.getName();
        String value = requestContext.getPathVariables().get(varName);
        return value;
    }

    @Override
    public Source getSource() {
        return Source.PATH;
    }

}
