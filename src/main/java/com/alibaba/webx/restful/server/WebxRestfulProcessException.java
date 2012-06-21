package com.alibaba.webx.restful.server;

public class WebxRestfulProcessException extends Exception {

    private static final long serialVersionUID = 1L;

    public WebxRestfulProcessException(String message){
        super(message);
    }

    public WebxRestfulProcessException(String message, Throwable cause){
        super(message, cause);
    }
}
