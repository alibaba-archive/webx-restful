package com.alibaba.webx.restful.model;

import com.alibaba.webx.restful.process.WebxRestfulRequestContext;

public interface InstanceConstructor {

    Object createInstance(WebxRestfulRequestContext requestContext) throws Exception;

    Class<?> getHandlerClass();
}
