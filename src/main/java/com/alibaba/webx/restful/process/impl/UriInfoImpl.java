package com.alibaba.webx.restful.process.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class UriInfoImpl implements UriInfo {

    private final String             path;
    private final HttpServletRequest httpRequest;
    private transient URI            requestURI = null;

    public UriInfoImpl(HttpServletRequest httpRequest){
        this(httpRequest, getPath(httpRequest));
    }

    public UriInfoImpl(HttpServletRequest httpRequest, String path){
        if (path.endsWith(".json")) {
            this.path = path.substring(0, path.length() - 5);
        } else {
            this.path = path;
        }
        this.httpRequest = httpRequest;
    }

    static String getPath(HttpServletRequest httpRequest) {
        String servletPath = httpRequest.getServletPath();
        if (servletPath.length() == 0) {
            servletPath = "/";
        }
        String contextPath = httpRequest.getContextPath();
        String requestURI = httpRequest.getRequestURI();

        return requestURI.substring(servletPath.length() + contextPath.length());
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getPath(boolean decode) {
        return path;
    }

    @Override
    public List<PathSegment> getPathSegments() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PathSegment> getPathSegments(boolean decode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getRequestUri() {
        if (requestURI == null) {
            try {
                requestURI = new URI(httpRequest.getRequestURI());
            } catch (URISyntaxException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        return requestURI;
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getAbsolutePath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getBaseUri() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters(boolean decode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getMatchedURIs() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getMatchedURIs(boolean decode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Object> getMatchedResources() {
        // TODO Auto-generated method stub
        return null;
    }

}
