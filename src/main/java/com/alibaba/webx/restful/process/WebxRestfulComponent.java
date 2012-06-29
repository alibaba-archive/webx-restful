package com.alibaba.webx.restful.process;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.ApplicationImpl;

public class WebxRestfulComponent {

    private final ApplicationImpl  config;
    private final ApplicationHandler handler;

    public WebxRestfulComponent(ApplicationImpl config, ApplicationContext applicationContext){
        Assert.notNull(applicationContext);

        this.config = config;
        handler = new ApplicationHandler(config, applicationContext);
    }

    public ApplicationImpl getConfig() {
        return config;
    }

    public ApplicationHandler getHandler() {
        return handler;
    }

}
