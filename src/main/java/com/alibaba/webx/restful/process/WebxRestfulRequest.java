package com.alibaba.webx.restful.process;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Variant;

public class WebxRestfulRequest implements javax.ws.rs.core.Request {

    private final HttpServletRequest httpRequest;

    public WebxRestfulRequest(HttpServletRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    @Override
    public String getMethod() {
        return httpRequest.getMethod();
    }

    @Override
    public Variant selectVariant(List<Variant> variants) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseBuilder evaluatePreconditions(EntityTag eTag) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseBuilder evaluatePreconditions(Date lastModified) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseBuilder evaluatePreconditions(Date lastModified, EntityTag eTag) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseBuilder evaluatePreconditions() {
        // TODO Auto-generated method stub
        return null;
    }

}
