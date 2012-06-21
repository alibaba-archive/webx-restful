package com.alibaba.webx.restful.server;

public class ResourceConfigException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceConfigException(){
        super();
    }

    public ResourceConfigException(String message){
        super(message);
    }

    public ResourceConfigException(String message, Throwable cause){
        super(message, cause);
    }

}
