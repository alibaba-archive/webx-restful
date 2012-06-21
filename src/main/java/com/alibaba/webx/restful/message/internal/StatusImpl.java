package com.alibaba.webx.restful.message.internal;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class StatusImpl implements StatusType {

    private int    code;
    private String reason;
    private Family family;

    public StatusImpl(int code, String reason){
        this.code = code;
        this.reason = reason;
        this.family = Family.familyOf(code);
    }

    @Override
    public int getStatusCode() {
        return code;
    }

    @Override
    public String getReasonPhrase() {
        return reason;
    }

    @Override
    public String toString() {
        return reason;
    }

    @Override
    public Family getFamily() {
        return family;
    }
}
