package com.alibaba.webx.restful.model;

import com.alibaba.webx.restful.process.WebxRestfulRequestContext;

public class SingletonInstanceConstructor implements InstanceConstructor {

    private Object   object;
    private Class<?> clazz;

    public SingletonInstanceConstructor(Class<?> clazz, Object object){
        super();
        this.clazz = clazz;
        this.object = object;
    }

    @Override
    public Object createInstance(WebxRestfulRequestContext requestContext) throws Exception {
        return object;
    }

    @Override
    public Class<?> getHandlerClass() {
        return clazz;
    }

}
