package com.alibaba.webx.restful.support.webx3;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.Valve;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.alibaba.citrus.webx.WebxComponent;

public class RestfulValve implements Valve {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private WebxComponent      component;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public WebxComponent getComponent() {
        return component;
    }

    public void setComponent(WebxComponent component) {
        this.component = component;
    }

    @Override
    public void invoke(PipelineContext pipelineContext) throws Exception {
        TurbineRunDataInternal rundata = (TurbineRunDataInternal) getTurbineRunData(request);

        rundata.getAction();

        System.out.println();
    }

}
