package com.alibaba.webx.restful.process;

import javax.ws.rs.MessageProcessingException;

public class MessageBodyProcessingException extends MessageProcessingException {

    private static final long serialVersionUID = 2093175681702118380L;

    public MessageBodyProcessingException(String message, Throwable cause){
        super(message, cause);
    }

    public MessageBodyProcessingException(String message){
        super(message);
    }
}
