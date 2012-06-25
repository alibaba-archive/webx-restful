package com.alibaba.webx.restful.spi;

import javax.ws.rs.ext.ExceptionMapper;

public interface ExceptionMappers {

    /**
     * Get an exception mapping provider for a particular class of exception. Returns the provider whose generic type is
     * the nearest superclass of {@code type}.
     * 
     * @param <T> type of the exception handled by the exception mapping provider.
     * @param type the class of exception.
     * @return an {@link ExceptionMapper} for the supplied type or {@code null} if none is found.
     */
    public <T extends Throwable> ExceptionMapper<T> find(Class<T> type);
}
