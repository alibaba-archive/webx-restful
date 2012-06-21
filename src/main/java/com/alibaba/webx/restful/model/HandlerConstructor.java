package com.alibaba.webx.restful.model;

import java.lang.reflect.Constructor;
import java.util.List;

import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public final class HandlerConstructor implements Parameterized {

    private final Class<?>        handlerClass;
    private final Constructor<?>  constructor;
    private final List<Parameter> parameters;

    public HandlerConstructor(Constructor<?> constructor, List<Parameter> parameters){
        this.handlerClass = constructor.getDeclaringClass();
        this.constructor = constructor;
        this.parameters = parameters;
    }

    public Class<?> getHandlerClass() {
        return handlerClass;
    }

    /**
     * Get the underlying java constructor.
     * 
     * @return underlying java constructor.
     */
    public Constructor<?> getConstructor() {
        return constructor;
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public boolean requiresEntity() {
        for (Parameter p : getParameters()) {
            if (Parameter.Source.ENTITY == p.getSource()) {
                return true;
            }
        }
        return false;
    }

    public Object createInstance(WebxRestfulRequestContext requestContext) throws Exception {
        Object[] constructArgs = new Object[parameters.size()];
        for (int i = 0; i < constructArgs.length; ++i) {
            Parameter parameter = parameters.get(i);
            constructArgs[i] = parameter.getParameterValue(requestContext);
        }
        Object resourceInstance = constructor.newInstance(constructArgs);
        return resourceInstance;
    }
}
