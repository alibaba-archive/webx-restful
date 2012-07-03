package com.alibaba.webx.restful.process.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.MessageProcessingException;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

public class ResponseImpl extends Response {

    private StatusType                         status;
    private Object                             entity;
    private Annotation[]                       annotations;
    private Type                               declaredType;
    private Set<String>                        allowMethods;
    private MultivaluedHashMap<String, Object> headers;

    private HttpServletResponse                httpResponse;

    public ResponseImpl(StatusType status, Object entity, Annotation[] annotations, Type declaredType,
                        Set<String> allowMethods, MultivaluedHashMap<String, Object> headers){
        super();
        this.status = status;
        this.entity = entity;
        this.annotations = annotations;
        this.declaredType = declaredType;
        this.allowMethods = allowMethods;
        this.headers = headers;
    }

    public Type getDeclaredType() {
        return declaredType;
    }

    public Set<String> getAllowMethods() {
        return allowMethods;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpServletResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    @Override
    public int getStatus() {
        return status.getStatusCode();
    }

    @Override
    public StatusType getStatusInfo() {
        return status;
    }

    @Override
    public Object getEntity() throws IllegalStateException {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public MultivaluedHashMap<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public <T> T readEntity(Class<T> entityType) throws MessageProcessingException, IllegalStateException {
        return null;
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType) throws MessageProcessingException, IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T readEntity(Class<T> entityType, Annotation[] annotations) throws MessageProcessingException,
                                                                          IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) throws MessageProcessingException,
                                                                                IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasEntity() {
        return entity != null;
    }

    @Override
    public boolean bufferEntity() throws MessageProcessingException {
        return entity != null;
    }

    @Override
    public void close() throws MessageProcessingException {

    }

    @Override
    public String getHeader(String name) {
        Object first = headers.getFirst(name);
        if (first == null) {
            return null;
        }
        return first.toString();
    }

    @Override
    public MediaType getMediaType() {
        return (MediaType) this.headers.getFirst(HttpHeaders.CONTENT_TYPE);
    }

    public void setMediaType(MediaType mediaType) {
        if (mediaType == null) {
            this.headers.remove(HttpHeaders.CONTENT_TYPE);
        } else {
            this.headers.putSingle(HttpHeaders.CONTENT_TYPE, mediaType);
        }
    }

    @Override
    public Locale getLanguage() {
        return (Locale) this.headers.getFirst(HttpHeaders.CONTENT_LANGUAGE);
    }

    @Override
    public int getLength() {
        Object length = this.headers.getFirst(HttpHeaders.CONTENT_LENGTH);
        if (length == null) {
            return -1;
        }
        return 0;
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityTag getEntityTag() {
        return (EntityTag) this.headers.getFirst(HttpHeaders.ETAG);
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getLastModified() {
        return (Date) this.headers.getFirst(HttpHeaders.LAST_MODIFIED);
    }

    @Override
    public URI getLocation() {
        return (URI) this.headers.getFirst(HttpHeaders.LOCATION);
    }

    @Override
    public Set<Link> getLinks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasLink(String relation) {
        return false;
    }

    @Override
    public Link getLink(String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Builder getLinkBuilder(String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getAllowedMethods() {
        return null;
    }

}
