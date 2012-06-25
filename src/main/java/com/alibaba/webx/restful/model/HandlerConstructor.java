package com.alibaba.webx.restful.model;

import java.lang.reflect.Constructor;
import java.util.List;

import com.alibaba.webx.restful.process.WebxRestfulRequestContext;

public final class HandlerConstructor {

    private final Class<?>             handlerClass;
    private final Constructor<?>       constructor;
    private final List<Parameter>      parameters;

    private final List<AutowireSetter> setters;

    public HandlerConstructor(Constructor<?> constructor, List<Parameter> parameters, List<AutowireSetter> setters){
        this.handlerClass = constructor.getDeclaringClass();
        this.constructor = constructor;
        this.parameters = parameters;
        this.setters = setters;
    }

    public List<AutowireSetter> getSetters() {
        return setters;
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

    public List<Parameter> getParameters() {
        return parameters;
    }

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
        
        for (AutowireSetter setter : setters) {
            Parameter parameter = setter.getParameter();
            Object propertyValue = parameter.getParameterValue(requestContext);
            setter.getMethod().invoke(resourceInstance, propertyValue);
        }
        
        return resourceInstance;
    }
}
