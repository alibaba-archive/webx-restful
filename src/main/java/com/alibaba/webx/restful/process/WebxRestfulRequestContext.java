package com.alibaba.webx.restful.process;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
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

import com.alibaba.webx.restful.internal.WebxRestfulMessageBodyWorkerProvider;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.spi.MessageBodyWorkerProvider;

public class WebxRestfulRequestContext implements ContainerRequestContext {

    private final HttpServletRequest  httpRequest;
    private final HttpServletResponse httpResponse;

    private Resource                  resource;
    private ResourceMethod            resourceMethod;

    private MatchResult               resourceMatchResult;
    private MatchResult               resourceMethodMatchResult;

    private Exception                 exception;
    private Object                    returnObject;

    private HttpHeaders               httpHeaders     = null;

    private WebxRestfulRequest        request         = null;
    private SecurityContext           securityContext = null;

    private Date                      date;

    private UriInfo                   uriInfo;

    private String                    method;

    private Map<String, Object>       properties      = null;

    private MessageBodyWorkerProvider workers;

    public WebxRestfulRequestContext(HttpServletRequest request, HttpServletResponse response,
                                     WebxRestfulMessageBodyWorkerProvider workers){
        super();
        this.httpRequest = request;
        this.httpResponse = response;
        this.date = new Date();

        this.uriInfo = new WebxRestfulUriInfo(request);

        this.workers = workers;
    }

    public MessageBodyWorkerProvider getWorkers() {
        return workers;
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
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        return properties;
    }

    @Override
    public UriInfo getUriInfo() {
        return this.uriInfo;
    }

    @Override
    public void setRequestUri(URI requestUri) throws IllegalStateException {

    }

    @Override
    public void setRequestUri(URI baseUri, URI requestUri) throws IllegalStateException {

    }

    @Override
    public String getMethod() {
        if (this.method == null) {
            return this.httpRequest.getMethod();
        }

        return method;
    }

    @Override
    public void setMethod(String method) throws IllegalStateException {
        this.method = method;
    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        return this.getHttpHeaders().getRequestHeaders();
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Locale getLanguage() {
        return this.getHttpHeaders().getLanguage();
    }

    @Override
    public int getLength() {
        return this.getHttpHeaders().getLength();
    }

    @Override
    public MediaType getMediaType() {
        return this.getHttpHeaders().getMediaType();
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        return this.getHttpHeaders().getAcceptableMediaTypes();
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        return this.getHttpHeaders().getAcceptableLanguages();
    }

    @Override
    public Map<String, Cookie> getCookies() {
        return this.getHttpHeaders().getCookies();
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
        return securityContext;
    }

    @Override
    public void setSecurityContext(SecurityContext context) {
        this.securityContext = context;
    }

    @Override
    public void abortWith(Response response) {
        // TODO Auto-generated method stub

    }

    @Override
    public Request getRequest() {
        if (request == null) {
            request = new WebxRestfulRequest(httpRequest);
        }
        return request;
    }

}
