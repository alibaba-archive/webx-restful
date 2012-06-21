package com.alibaba.webx.restful.server;

import java.io.IOException;

public class WebxRestfulProcessException extends IOException {

    private static final long serialVersionUID = 1L;

    public WebxRestfulProcessException(String message){
        super(message);
    }

    public WebxRestfulProcessException(String message, Throwable cause){
        super(message, cause);
    }
}
