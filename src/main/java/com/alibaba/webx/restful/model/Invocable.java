package com.alibaba.webx.restful.model;

import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.core.GenericType;

import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public final class Invocable implements Parameterized {

    private final HandlerConstructor handlerConstructor;
    private final Method             handlingMethod;
    private final List<Parameter>    parameters;
    private final GenericType<?>     responseType;

    public Invocable(HandlerConstructor handlerConstructor, Method handlingMethod, List<Parameter> parameters){
        this.handlerConstructor = handlerConstructor;
        this.handlingMethod = handlingMethod;

        this.responseType = GenericType.of(handlingMethod.getReturnType(), handlingMethod.getGenericReturnType());

        this.parameters = parameters;
    }

    public HandlerConstructor getHandlerConstructor() {
        return handlerConstructor;
    }

    /**
     * Getter for the Java method
     * 
     * @return corresponding Java method
     */
    public Method getHandlingMethod() {
        return handlingMethod;
    }

    /**
     * Get the resource method response type.
     * <p/>
     * The returned value provides information about the raw Java class as well as the Type information that contains
     * additional generic declaration information for generic Java class types.
     * 
     * @return resource method response type information.
     */
    public GenericType<?> getResponseType() {
        return responseType;
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

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    public Object[] getArguments(WebxRestfulRequestContext requestContext) throws Exception {
        Object[] args = new Object[parameters.size()];
        for (int i = 0; i < args.length; ++i) {
            Parameter parameter = parameters.get(i);
            args[i] = parameter.getParameterValue(requestContext);
        }

        return args;
    }

    public Object invoke(Object instance, Object[] args) throws Exception {
        Object returnObject = handlingMethod.invoke(instance, args);
        return returnObject;
    }
}
