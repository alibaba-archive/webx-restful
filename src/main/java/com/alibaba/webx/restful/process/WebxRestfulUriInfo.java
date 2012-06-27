package com.alibaba.webx.restful.process;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class WebxRestfulUriInfo implements UriInfo {

    private String path;

    public WebxRestfulUriInfo(HttpServletRequest httpRequest){
        this(getPath(httpRequest));
    }

    public WebxRestfulUriInfo(String path){
        if (path.endsWith(".json")) {
            this.path = path.substring(0, path.length() - 5);
        } else {
            this.path = path;
        }
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getAbsolutePath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getBaseUri() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        // TODO Auto-generated method stub
        return null;
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
