package com.alibaba.webx.restful.model;

import java.lang.reflect.Method;

public class InstanceSetter {

    private final Method    method;
    private final Parameter parameter;

    public InstanceSetter(Method method, Parameter parameter){
        this.method = method;
        this.parameter = parameter;
    }

    public Method getMethod() {
        return method;
    }

    public Parameter getParameter() {
        return parameter;
    }
}
