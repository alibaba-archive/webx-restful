package com.alibaba.webx.restful.message;

import javax.ws.rs.MessageProcessingException;

/**
 * Jersey exception signaling that error occurred during reading or writing message body (entity).
 * 
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */
public class MessageBodyProcessingException extends MessageProcessingException {

    private static final long serialVersionUID = 2093175681702118380L;

    /**
     * Creates new instance initialized with exception cause.
     * 
     * @param cause Exception cause.
     */
    public MessageBodyProcessingException(Throwable cause){
        super(cause);
    }

    /**
     * Creates new instance initialized with exception message and exception cause.
     * 
     * @param message Message.
     * @param cause Exception cause.
     */
    public MessageBodyProcessingException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Creates new instance initialized with exception message.
     * 
     * @param message Message.
     */
    public MessageBodyProcessingException(String message){
        super(message);
    }
}
