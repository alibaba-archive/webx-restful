package com.alibaba.webx.restful.internal.inject;

import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.Sets;

public class Providers {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Set<T> getProviders(ApplicationContext services, Class<T> contract) {
        Map beans = services.getBeansOfType(contract);

        return Sets.newLinkedHashSet(beans.values());
    }
    
    public static void bind(ApplicationContext context, String beanName, Object object) {
        bind((ConfigurableApplicationContext) context, beanName, object);
    }

    public static void bind(ConfigurableApplicationContext context, String beanName, Object obj) {
        context.getBeanFactory().registerSingleton(beanName, obj);
    }
}
