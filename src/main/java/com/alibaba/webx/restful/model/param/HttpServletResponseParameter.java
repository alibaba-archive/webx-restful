package com.alibaba.webx.restful.model.param;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.process.RestfulRequestContext;

public class HttpServletResponseParameter implements Parameter {

    @Override
    public Object getParameterValue(RestfulRequestContext requestContext) {
        return requestContext.getHttpResponse();
    }

    @Override
    public Source getSource() {
        return Source.CONTEXT;
    }

}
