package com.alibaba.webx.restful.model;

import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.alibaba.webx.restful.util.Sets;

public class ApplicationConfig extends Application {

    private final Set<Resource> resources;

    public ApplicationConfig(){
        this.resources = Sets.newHashSet();
    }

    public final Set<Resource> getResources() {
        return resources;
    }

    public void addResources(List<Resource> resources) {
        this.resources.addAll(resources);
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }

}
