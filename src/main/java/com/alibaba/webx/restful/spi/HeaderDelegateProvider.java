package com.alibaba.webx.restful.spi;

import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public interface HeaderDelegateProvider<T> extends HeaderDelegate<T> {

    /**
     * Ascertain if the Provider supports a particular type.
     * 
     * @param type the type that is to be supported.
     * @return true if the type is supported, otherwise false.
     */
    boolean supports(Class<?> type);
}
