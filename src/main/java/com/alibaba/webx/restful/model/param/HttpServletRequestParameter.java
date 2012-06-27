package com.alibaba.webx.restful.model.param;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.process.RestfulRequestContext;

public class HttpServletRequestParameter implements Parameter {

    @Override
    public Object getParameterValue(RestfulRequestContext requestContext) {
        return requestContext.getHttpRequest();
    }

    @Override
    public Source getSource() {
        return Parameter.Source.CONTEXT;
    }


}
