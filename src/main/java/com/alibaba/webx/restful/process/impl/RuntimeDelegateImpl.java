package com.alibaba.webx.restful.process.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.webx.restful.model.uri.UriBuilderImpl;
import com.alibaba.webx.restful.process.providers.CacheControlProvider;
import com.alibaba.webx.restful.process.providers.CookieEntityTagProvider;
import com.alibaba.webx.restful.process.providers.EntityTagProvider;
import com.alibaba.webx.restful.process.providers.LinkProvider;
import com.alibaba.webx.restful.process.providers.MediaTypeProvider;
import com.alibaba.webx.restful.process.providers.NewCookieProvider;
import com.alibaba.webx.restful.spi.HeaderDelegateProvider;

public class RuntimeDelegateImpl extends javax.ws.rs.ext.RuntimeDelegate {

    private final static Log                           LOG = LogFactory.getLog(RuntimeDelegateImpl.class);

    private ConcurrentMap<Class<?>, HeaderDelegate<?>> map = new ConcurrentHashMap<Class<?>, HeaderDelegate<?>>();

    private volatile Set<HeaderDelegateProvider<?>>    hps = new HashSet<HeaderDelegateProvider<?>>();

    public RuntimeDelegateImpl(){
        try {
            hps.add(MediaTypeProvider.getInstance());
            hps.add(EntityTagProvider.getInstance());
            hps.add(CookieEntityTagProvider.getInstance());
            hps.add(CacheControlProvider.getInstance());
            hps.add(NewCookieProvider.getInstance());
            hps.add(LinkProvider.getInstance());
        } catch (Throwable error) {
            LOG.error(error.getMessage(), error);
        }
    }

    @Override
    public UriBuilder createUriBuilder() {
        return new UriBuilderImpl();
    }

    @Override
    public ResponseBuilder createResponseBuilder() {
        return new ResponseBuilderImpl();
    }

    @Override
    public VariantListBuilderImpl createVariantListBuilder() {
        return new VariantListBuilderImpl();
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException,
                                                                               UnsupportedOperationException {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type parameter cannot be null");
        }

        HeaderDelegate<T> delegate = (HeaderDelegate<T>) map.get(type);
        if (delegate != null) {
            return delegate;
        }

        delegate = (HeaderDelegate<T>) findHeaderDelegate(type);
        if (delegate != null) {
            map.putIfAbsent(type, delegate);
        }

        delegate = (HeaderDelegate<T>) map.get(type);

        return delegate;
    }

    private synchronized HeaderDelegate<?> findHeaderDelegate(Class<?> type) {
        if (hps == null) {
            hps = new HashSet<HeaderDelegateProvider<?>>();
        }
        for (HeaderDelegateProvider<?> hp : hps) {
            if (hp.supports(type)) {
                return hp;
            }
        }

        return null;
    }

}
