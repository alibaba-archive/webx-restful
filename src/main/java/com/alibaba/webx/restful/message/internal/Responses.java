package com.alibaba.webx.restful.message.internal;

import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;


public class Responses {
    /**
     * Create a response builder with no response entity & status code set to
     * {@link Status#NO_CONTENT}.
     *
     * @return response builder instance.
     */
    public static ResponseBuilder empty() {
        throw new UnsupportedOperationException();
    }
}
