package com.alibaba.webx.restful.process;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.alibaba.webx.restful.model.ApplicationConfig;

public class WebxRestfulComponent {

    private final ApplicationConfig  config;
    private final ApplicationHandler handler;

    public WebxRestfulComponent(ApplicationConfig config, ApplicationContext applicationContext){
        Assert.notNull(applicationContext);

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
