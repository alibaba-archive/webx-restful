package com.alibaba.webx.restful.process.providers;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.EntityTag;

import com.alibaba.webx.restful.spi.HeaderDelegateProvider;

public class CookieEntityTagProvider implements HeaderDelegateProvider<EntityTag> {

    private final static CookieEntityTagProvider instance = new CookieEntityTagProvider();

    public final static CookieEntityTagProvider getInstance() {
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
        return type == Cookie.class;
    }

}
