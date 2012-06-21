package com.alibaba.webx.restful.model.param;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;

public class HttpServletRequestParameter implements Parameter {

    @Override
    public Object getParameterValue(WebxRestfulRequestContext requestContext) {
        return requestContext.getHttpRequest();
    }

    @Override
    public Source getSource() {
        return Parameter.Source.CONTEXT;
    }


}
