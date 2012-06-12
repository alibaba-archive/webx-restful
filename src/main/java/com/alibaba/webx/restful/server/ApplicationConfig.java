package com.alibaba.webx.restful.server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.alibaba.webx.restful.util.ReflectionUtils;

public class ApplicationConfig extends Application {

    private ClassLoader               classLoader = null;
    private final Set<Class<?>>       classes;
    private final Set<ResourceFinder> resourceFinders;

    public ApplicationConfig(){
        this.classLoader = ReflectionUtils.getContextClassLoader();

        this.classes = new HashSet<Class<?>>();
        this.resourceFinders = new HashSet<ResourceFinder>();
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public Set<ResourceFinder> getResourceFinders() {
        return resourceFinders;
    }

}
