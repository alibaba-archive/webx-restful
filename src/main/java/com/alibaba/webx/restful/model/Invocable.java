package com.alibaba.webx.restful.model;

import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.core.GenericType;

import com.alibaba.webx.restful.process.RestfulRequestContext;

public final class Invocable {

    private final InstanceConstructor constructor;
    private final Method              method;
    private final List<Parameter>     parameters;
    private final GenericType<?>      responseType;

    public Invocable(InstanceConstructor instanceConstructor, Method method, List<Parameter> parameters){
        this.constructor = instanceConstructor;
        this.method = method;

        this.responseType = GenericType.of(method.getReturnType(), method.getGenericReturnType());

        this.parameters = parameters;
    }

    public InstanceConstructor getConstructor() {
        return constructor;
    }

    public Method getMethod() {
        return method;
    }

    public GenericType<?> getResponseType() {
        return responseType;
    }

    public boolean requiresEntity() {
        for (Parameter p : getParameters()) {
            if (Parameter.Source.ENTITY == p.getSource()) {
                return true;
            }
        }
        return false;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Object[] getArguments(RestfulRequestContext requestContext) throws Exception {
        Object[] args = new Object[parameters.size()];
        for (int i = 0; i < args.length; ++i) {
            Parameter parameter = parameters.get(i);
            args[i] = parameter.getParameterValue(requestContext);
        }

        return args;
    }

    public Object invoke(Object instance, Object[] args) throws Exception {
        Object returnObject = method.invoke(instance, args);
        return returnObject;
    }
    
    public Object createInstance(RestfulRequestContext requestContext) throws Exception {
        return this.constructor.createInstance(requestContext);
    }
}
