package com.alibaba.webx.restful.process.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import com.alibaba.webx.restful.process.ApplicationHandler;
import com.alibaba.webx.restful.process.ProcessException;

public class WriterInterceptorContextImpl implements WriterInterceptorContext {

    private final ApplicationHandler          handler;

    private final ResponseImpl                response;

    private final Iterator<WriterInterceptor> iterator;

    private Class<?>                          type;
    private Type                              genericType;

    private Map<String, Object>               properties = null;

    private Annotation[]                      annotations;
    private Object                            entity;
    private OutputStream                      outputStream;

    public WriterInterceptorContextImpl(ApplicationHandler handler, List<WriterInterceptor> interceptors,
                                        ResponseImpl response) throws IOException{
        this.handler = handler;
        this.response = response;

        this.annotations = response.getAnnotations();
        this.entity = response.getEntity();
        this.outputStream = response.getHttpResponse().getOutputStream();

        this.iterator = interceptors.iterator();
    }

    @Override
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
        return response.getMediaType();
    }

    @Override
    public void setMediaType(MediaType mediaType) {
        response.setMediaType(mediaType);
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
        return response.getHeaders();
    }

    public WriterInterceptor getNextInterceptor() {
        if (!iterator.hasNext()) {
            return null;
        }
        return iterator.next();
    }

}
