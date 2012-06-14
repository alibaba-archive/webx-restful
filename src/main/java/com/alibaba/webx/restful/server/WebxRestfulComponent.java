package com.alibaba.webx.restful.server;

import junit.framework.Assert;

import org.springframework.context.ApplicationContext;

public class WebxRestfulComponent {

    private ApplicationConfig  config;
    private ApplicationHandler handler;

    public WebxRestfulComponent(ApplicationConfig config, ApplicationContext applicationContext){
        Assert.assertNotNull(applicationContext);
        
        this.config = config;
        handler = new ApplicationHandler(config, applicationContext);
    }

    public ApplicationConfig getConfig() {
        return config;
    }

    public ApplicationHandler getHandler() {
        return handler;
    }
}
