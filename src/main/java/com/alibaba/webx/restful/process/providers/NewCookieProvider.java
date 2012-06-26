package com.alibaba.webx.restful.process.providers;

import javax.ws.rs.core.EntityTag;

import com.alibaba.webx.restful.spi.HeaderDelegateProvider;

public class NewCookieProvider implements HeaderDelegateProvider<EntityTag> {

    private final static NewCookieProvider instance = new NewCookieProvider();

    public final static NewCookieProvider getInstance() {
        return instance;
    }

    @Override
    public EntityTag fromString(String value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString(EntityTag value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean supports(Class<?> type) {
        // TODO Auto-generated method stub
        return false;
    }

}
