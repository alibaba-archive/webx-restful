package com.alibaba.webx.restful.model;

import com.alibaba.webx.restful.process.RestfulRequestContext;

public interface InstanceConstructor {

    Object createInstance(RestfulRequestContext requestContext) throws Exception;

    Class<?> getHandlerClass();
}
