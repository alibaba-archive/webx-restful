package com.alibaba.webx.restful.model.param;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class HttpServletResponseParameter implements Parameter {

    @Override
    public Object getParameterValue(WebxRestfulRequestContext requestContext) {
        return requestContext.getHttpResponse();
    }

    @Override
    public Source getSource() {
        return Source.CONTEXT;
    }

}
