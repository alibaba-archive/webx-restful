package com.alibaba.webx.restful.internal.inject;

import java.util.Set;

public class ServiceProviders {

    private final Set<Class<?>> providerClasses;
    private final Set<Object>   providerInstances;

    public ServiceProviders(Set<Class<?>> providerClasses, Set<Object> providerInstances){
        super();
        this.providerClasses = providerClasses;
        this.providerInstances = providerInstances;
    }

    public Set<Class<?>> getProviderClasses() {
        return providerClasses;
    }

    public Set<Object> getProviderInstances() {
        return providerInstances;
    }

}
