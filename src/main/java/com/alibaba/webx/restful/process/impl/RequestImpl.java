package com.alibaba.webx.restful.process.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Variant;

public class RequestImpl implements javax.ws.rs.core.Request {

    private final HttpServletRequest httpRequest;

    public RequestImpl(HttpServletRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    @Override
    public String getMethod() {
        return httpRequest.getMethod();
    }

    @Override
    public Variant selectVariant(List<Variant> variants) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder evaluatePreconditions(EntityTag eTag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder evaluatePreconditions(Date lastModified) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder evaluatePreconditions(Date lastModified, EntityTag eTag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder evaluatePreconditions() {
        throw new UnsupportedOperationException();
    }

}
