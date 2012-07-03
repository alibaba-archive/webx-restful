package com.alibaba.webx.restful.process.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import com.alibaba.webx.restful.process.ProcessException;

public class WriterInterceptorContextImpl implements WriterInterceptorContext {

    private final Iterator<WriterInterceptor> iterator;

    private Class<?>                          type;
    private Type                              genericType;

    private Map<String, Object>               properties = null;

    private Annotation[]                      annotations;
    private Object                            entity;
    private OutputStream                      outputStream;
    private MediaType                         mediaType;
    private MultivaluedMap<String, Object>    headers;

    public WriterInterceptorContextImpl(List<WriterInterceptor> interceptors) throws IOException{

        this.iterator = interceptors.iterator();
    }

    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }

        return properties;
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public Type getGenericType() {
        return genericType;
    }

    @Override
    public void setGenericType(Type genericType) {
        this.genericType = genericType;
    }

    @Override
    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public void proceed() throws IOException {
        WriterInterceptor nextInterceptor = getNextInterceptor();
        if (nextInterceptor == null) {
            throw new ProcessException("nextInterceptor is null");
        }
        nextInterceptor.aroundWriteTo(this);
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Object entity) {
        this.entity = entity;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void setOutputStream(OutputStream os) {
        this.outputStream = os;
    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(MultivaluedMap<String, Object> headers) {
        this.headers = headers;
    }

    public WriterInterceptor getNextInterceptor() {
        if (!iterator.hasNext()) {
            return null;
        }
        return iterator.next();
    }

    @Override
    public Object getProperty(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration<String> getPropertyNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setProperty(String name, Object object) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeProperty(String name) {
        // TODO Auto-generated method stub
        
    }

}
