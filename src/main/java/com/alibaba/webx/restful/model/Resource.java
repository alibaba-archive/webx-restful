package com.alibaba.webx.restful.model;

import java.util.List;

public class Resource {

    private final String               name;
    private final String               path;
    private final boolean              isRoot;
    private final List<ResourceMethod> resourceMethods;
    private final List<ResourceMethod> subResourceMethods;
    private final List<ResourceMethod> subResourceLocators;

    private Resource(final String name, final String path, final boolean isRoot,
                     final List<ResourceMethod> resourceMethods, final List<ResourceMethod> subResourceMethods,
                     final List<ResourceMethod> subResourceLocators){

        this.name = name;
        this.path = path;
        this.isRoot = isRoot;

        this.resourceMethods = resourceMethods;
        this.subResourceMethods = subResourceMethods;
        this.subResourceLocators = subResourceLocators;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public List<ResourceMethod> getResourceMethods() {
        return resourceMethods;
    }

    public List<ResourceMethod> getSubResourceMethods() {
        return subResourceMethods;
    }

    public List<ResourceMethod> getSubResourceLocators() {
        return subResourceLocators;
    }

}
