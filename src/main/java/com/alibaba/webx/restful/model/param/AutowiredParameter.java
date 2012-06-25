package com.alibaba.webx.restful.model.param;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.converter.TypeConvertException;
import com.alibaba.webx.restful.process.WebxRestfulRequestContext;

public class AutowiredParameter implements Parameter {

    private final Object object;

    public AutowiredParameter(Object object){
        this.object = object;
    }

    @Override
    public Source getSource() {
        return Source.AUTO_WIRED;
    }

    @Override
    public Object getParameterValue(WebxRestfulRequestContext requestContext) throws TypeConvertException {
        return object;
    }

    public Object getObject() {
        return object;
    }

}
