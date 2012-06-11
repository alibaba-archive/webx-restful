package com.alibaba.webx.restful.support.webx3;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.Valve;

public class WebX3RestfulValve implements Valve, InitializingBean, ApplicationContextAware {

    @Autowired(required = true)
    private HttpServletRequest request;

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {

    }

    public void invoke(PipelineContext pipelineContext) throws Exception {
        pipelineContext.invokeNext();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
