package com.alibaba.webx.restful.process.providers;

import javax.ws.rs.core.Link;

import com.alibaba.webx.restful.spi.HeaderDelegateProvider;

public class LinkProvider implements HeaderDelegateProvider<Link> {

    private final static LinkProvider instance = new LinkProvider();

    public final static LinkProvider getInstance() {
        return instance;
    }

    @Override
    public Link fromString(String value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString(Link value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type == Link.class;
    }

}
