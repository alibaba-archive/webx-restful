package com.alibaba.webx.restful.internal.inject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class Providers {

    public static void bind(ApplicationContext context, String beanName, Object object) {
        bind((ConfigurableApplicationContext) context, beanName, object);
    }

    public static void bind(ConfigurableApplicationContext context, String beanName, Object obj) {
        context.getBeanFactory().registerSingleton(beanName, obj);
    }
}
