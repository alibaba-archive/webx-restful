package com.alibaba.webx.restful.model;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

public class MethodHandler {

    private final Class<?>           handlerClass;
    private final HandlerConstructor handlerConstructor;

    public MethodHandler(final Class<?> handlerClass, HandlerConstructor handlerConstructor){
        this.handlerClass = handlerClass;
        this.handlerConstructor = handlerConstructor;
    }

    public Class<?> getHandlerClass() {
        return handlerClass;
    }

    public HandlerConstructor getHandlerConstructor() {
        return handlerConstructor;
    }

    public Object getInstance(ApplicationContext applicationContext) {
        Object instance = applicationContext.getAutowireCapableBeanFactory().autowire(this.handlerClass,
                                                                                      AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT,
                                                                                      true);
        return instance;
    }

}
