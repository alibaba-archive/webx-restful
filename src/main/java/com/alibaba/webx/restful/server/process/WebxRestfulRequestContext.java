package com.alibaba.webx.restful.server.process;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.MatchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;

public class WebxRestfulRequestContext implements ContainerRequestContext {

    private final HttpServletRequest  httpRequest;
    private final HttpServletResponse httpResponse;

    private transient String          path;

    private Resource                  resource;
    private ResourceMethod            resourceMethod;

    private MatchResult               resourceMatchResult;
    private MatchResult               resourceMethodMatchResult;

    private Exception                 exception;
    private Object                    returnObject;

    private HttpHeaders               httpHeaders = null;

    public WebxRestfulRequestContext(HttpServletRequest request, HttpServletResponse response){
        super();
        this.httpRequest = request;
        this.httpResponse = response;
    }

    public HttpHeaders getHttpHeaders() {
        if (httpHeaders == null) {
            httpHeaders = new WebxRestfulHttpHeaders(httpRequest);
        }
        return httpHeaders;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getPath() {
        if (path == null) {
            String servletPath = httpRequest.getServletPath();
            if (servletPath.length() == 0) {
                servletPath = "/";
            }
            String contextPath = httpRequest.getContextPath();
            String requestURI = httpRequest.getRequestURI();

            path = requestURI.substring(servletPath.length() + contextPath.length());
        }

        return path;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ResourceMethod getResourceMethod() {
        return resourceMethod;
    }

    public void setResourceMethod(ResourceMethod resourceMethod) {
        this.resourceMethod = resourceMethod;
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public MatchResult getResourceMatchResult() {
        return resourceMatchResult;
    }

    public void setResourceMatchResult(MatchResult resourceMatchResult) {
        this.resourceMatchResult = resourceMatchResult;
    }

    public MatchResult getResourceMethodMatchResult() {
        return resourceMethodMatchResult;
    }

    public void setResourceMethodMatchResult(MatchResult resourceMethodMatchResult) {
        this.resourceMethodMatchResult = resourceMethodMatchResult;
    }

    @Override
    public Map<String, Object> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UriInfo getUriInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRequestUri(URI requestUri) throws IllegalStateException {

    }

    @Override
    public void setRequestUri(URI baseUri, URI requestUri) throws IllegalStateException {

    }

    @Override
    public String getMethod() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMethod(String method) throws IllegalStateException {
        // TODO Auto-generated method stub

    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Locale getLanguage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MediaType getMediaType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Cookie> getCookies() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasEntity() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public InputStream getEntityStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityStream(InputStream input) {
        // TODO Auto-generated method stub

    }

    @Override
    public SecurityContext getSecurityContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSecurityContext(SecurityContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void abortWith(Response response) {
        // TODO Auto-generated method stub

    }

    @Override
    public Request getRequest() {
        // TODO Auto-generated method stub
        return null;
    }

}
