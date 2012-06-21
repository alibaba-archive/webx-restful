package com.alibaba.webx.restful.model;

import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.core.GenericType;

import com.alibaba.webx.restful.util.ClassTypePair;
import com.alibaba.webx.restful.util.ReflectionUtils;

public final class Invocable implements Parameterized {

    private final MethodHandler   handler;
    private final Method          handlingMethod;
    private final List<Parameter> parameters;
    private final GenericType<?>  responseType;

    public Invocable(MethodHandler handler, Method handlingMethod){
        this.handler = handler;
        this.handlingMethod = handlingMethod;

        final Class<?> handlerClass = handler.getHandlerClass();
        final ClassTypePair ctPair = ReflectionUtils.resolveGenericType(handlerClass,
                                                                        handlingMethod.getDeclaringClass(),
                                                                        handlingMethod.getReturnType(),
                                                                        handlingMethod.getGenericReturnType());
        this.responseType = GenericType.of(ctPair.rawClass(), ctPair.type());

        this.parameters = null; // TODO
    }

    /**
     * Get the model of the resource method handler that will be used to invoke the {@link #getHandlingMethod() handling
     * resource method} on.
     * 
     * @return resource method handler model.
     */
    public MethodHandler getHandler() {
        return handler;
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

    @Override
    public String toString() {
        return "Invocable{" + "handler=" + handler + ", handlingMethod=" + handlingMethod + ", parameters="
               + parameters + ", responseType=" + responseType + '}';
    }
}
