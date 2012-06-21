package com.alibaba.webx.restful.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.WriterInterceptor;

public class WebxRestfulMessageBodyWorkerProvider implements MessageBodyWorkerProvider {

    private List<MessageBodyWriter<?>> messageBodyWriters = new ArrayList<MessageBodyWriter<?>>();
    private Set<WriterInterceptor>     writeInterceptors  = new LinkedHashSet<WriterInterceptor>();

    @Override
    public Set<WriterInterceptor> getWriterInterceptors() {
        return writeInterceptors;
    }

    public List<MessageBodyWriter<?>> getMessageBodyWriters() {
        return messageBodyWriters;
    }
    
    public void addMessageBodyWriter(MessageBodyWriter<?> messageWriter) {
        this.messageBodyWriters.add(messageWriter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> MessageBodyWriter<T> getMessageBodyWriter(Class<?> type, Type genericType, Annotation[] annotations,
                                                         MediaType mediaType) {
        for (MessageBodyWriter<?> item : this.messageBodyWriters) {
            if (item.isWriteable(type, genericType, annotations, mediaType)) {
                return (MessageBodyWriter<T>) item;
            }
        }
        return null;
    }

}
