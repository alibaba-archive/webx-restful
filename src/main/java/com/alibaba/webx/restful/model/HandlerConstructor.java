package com.alibaba.webx.restful.model;

import java.lang.reflect.Constructor;
import java.util.List;

public final class HandlerConstructor implements Parameterized {

    private final Constructor<?>  constructor;
    private final List<Parameter> parameters;

    public HandlerConstructor(Constructor<?> constructor, List<Parameter> parameters){
        this.constructor = constructor;
        this.parameters = parameters;
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
}
