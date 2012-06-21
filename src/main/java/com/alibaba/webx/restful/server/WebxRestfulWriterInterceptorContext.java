package com.alibaba.webx.restful.server;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.MessageProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import com.alibaba.webx.restful.message.internal.WebxRestfulResponse;

public class WebxRestfulWriterInterceptorContext implements WriterInterceptorContext {

    private final MessageBodyWorkerProvider   workers;

    private final WebxRestfulResponse         response;

    private final Iterator<WriterInterceptor> iterator;

    public WebxRestfulWriterInterceptorContext(MessageBodyWorkerProvider workers, WebxRestfulResponse response){
        this.workers = workers;
        this.response = response;

        Set<WriterInterceptor> interceptorSet = workers.getWriterInterceptors();
        List<WriterInterceptor> interceptors = new ArrayList<WriterInterceptor>(interceptorSet.size() + 1);
        interceptors.addAll(interceptorSet);
        interceptors.add(new TerminalWriterInterceptor(workers));

        this.iterator = interceptors.iterator();
    }

    @Override
    public Map<String, Object> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return response.getAnnotations();
    }

    @Override
    public void setAnnotations(Annotation[] annotations) {
        response.setAnnotations(annotations);
    }

    @Override
    public Class<?> getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setType(Class<?> type) {
        // TODO Auto-generated method stub

    }

    @Override
    public Type getGenericType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGenericType(Type genericType) {
        // TODO Auto-generated method stub

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
            throw new WebxRestfulProcessException("nextInterceptor is null");
        }
        nextInterceptor.aroundWriteTo(this);
    }

    @Override
    public Object getEntity() {
        return response.getEntity();
    }

    @Override
    public void setEntity(Object entity) {
        response.setEntity(entity);
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return response.getHttpResponse().getOutputStream();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void setOutputStream(OutputStream os) {
        throw new UnsupportedOperationException();
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

    public static class TerminalWriterInterceptor implements WriterInterceptor {

        private final MessageBodyWorkerProvider workers;

        public TerminalWriterInterceptor(MessageBodyWorkerProvider workers){
            this.workers = workers;
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
            final MessageBodyWriter writer = workers.getMessageBodyWriter(context.getType(), context.getGenericType(),
                                                                          context.getAnnotations(),
                                                                          context.getMediaType());

            if (writer == null) {
                throw new MessageProcessingException("messageBodyWriter not found, mediaType " + context.getMediaType()
                                                     + ", type " + context.getType() + ", genericType"
                                                     + context.getGenericType());
            }

            writer.writeTo(context.getEntity(), context.getType(), context.getGenericType(), context.getAnnotations(),
                           context.getMediaType(), context.getHeaders(), context.getOutputStream());
        }
    }
}
