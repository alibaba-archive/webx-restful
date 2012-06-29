package com.alibaba.webx.restful.process;

import java.io.IOException;

public class ProcessException extends IOException {

    private static final long serialVersionUID = 1L;

    public ProcessException(String message){
        super(message);
    }

    public ProcessException(String message, Throwable cause){
        super(message, cause);
    }
}
