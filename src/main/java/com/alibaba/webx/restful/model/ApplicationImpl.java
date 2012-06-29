package com.alibaba.webx.restful.model;

import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.alibaba.webx.restful.util.ConcurrentIdentityHashMap;

public class ApplicationImpl extends Application {

    private static final Object                               PRESENT   = new Object();

    private final ConcurrentIdentityHashMap<Resource, Object> resources = new ConcurrentIdentityHashMap<Resource, Object>();

    private final ConcurrentIdentityHashMap<Object, Object>   instances = new ConcurrentIdentityHashMap<Object, Object>();

    public ApplicationImpl(){
    }

    public final Set<Resource> getResources() {
        return resources.keySet();
    }

    public void addResources(List<Resource> resources) {
        for (Resource item : resources) {
            this.resources.put(item, PRESENT);
        }
    }

    public void addResource(Resource resource) {
        this.resources.put(resource, PRESENT);
    }

    public Set<Object> getSingletons() {
        return instances.keySet();
    }

    public Object addSingleton(Object instance) {
        return this.instances.put(instance, PRESENT);
    }
}
