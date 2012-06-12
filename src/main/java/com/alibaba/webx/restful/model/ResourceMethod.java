package com.alibaba.webx.restful.model;

import java.util.List;

import javax.ws.rs.core.MediaType;

public class ResourceMethod {

    // HttpMethod
    private final String          httpMethod;
    // Routed
    private final String          path;
    // Consuming & Producing
    private final List<MediaType> consumedTypes;
    private final List<MediaType> producedTypes;

    public ResourceMethod(String httpMethod, String path, List<MediaType> consumedTypes, List<MediaType> producedTypes){
        super();
        this.httpMethod = httpMethod;
        this.path = path;
        this.consumedTypes = consumedTypes;
        this.producedTypes = producedTypes;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public List<MediaType> getConsumedTypes() {
        return consumedTypes;
    }

    public List<MediaType> getProducedTypes() {
        return producedTypes;
    }

}
