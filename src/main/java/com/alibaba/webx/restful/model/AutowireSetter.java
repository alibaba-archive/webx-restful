package com.alibaba.webx.restful.model;

import java.lang.reflect.Method;

public class AutowireSetter {

    private final Method    method;
    private final Parameter parameter;

    public AutowireSetter(Method method, Parameter parameter){
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
