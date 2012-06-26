package com.alibaba.webx.restful.model.finder;

public class ResourceFinderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceFinderException(){
        super();
    }

    public ResourceFinderException(String message){
        super(message);
    }

    public ResourceFinderException(String message, Throwable cause){
        super(message, cause);
    }

    public ResourceFinderException(Throwable cause){
        super(cause);
    }
}
