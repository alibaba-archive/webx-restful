package com.alibaba.webx.restful.server.process.param;

import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public interface ParameterProvider {
    Object getParameterValue(WebxRestfulRequestContext requestContext);
}
