package com.alibaba.webx.restful.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.WriterInterceptor;

public interface MessageBodyWorkerProvider {
    public Set<WriterInterceptor> getWriterInterceptors();
    
    <T> MessageBodyWriter<T> getMessageBodyWriter(Class<?> type, Type genericType, Annotation annotations[],
                                                  MediaType mediaType);
}
