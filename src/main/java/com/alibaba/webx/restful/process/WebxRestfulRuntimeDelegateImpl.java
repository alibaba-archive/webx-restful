package com.alibaba.webx.restful.process;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.spi.HeaderDelegateProvider;
import com.alibaba.webx.restful.util.ApplicationContextUtils;

public class WebxRestfulRuntimeDelegateImpl extends javax.ws.rs.ext.RuntimeDelegate {

    private final static Log                 LOG = LogFactory.getLog(WebxRestfulRuntimeDelegateImpl.class);

    private Map<Class<?>, HeaderDelegate<?>> map;

    @SuppressWarnings("unchecked")
    public WebxRestfulRuntimeDelegateImpl(){
        try {
            Set<HeaderDelegateProvider<?>> hps = new HashSet<HeaderDelegateProvider<?>>();

            ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
            if (applicationContext != null) {
                Map<?, ?> beanMap = applicationContext.getBeansOfType(HeaderDelegate.class);
                hps.addAll((Collection<HeaderDelegateProvider<?>>) beanMap.values());
            }

            map = new WeakHashMap<Class<?>, HeaderDelegate<?>>();
            map.put(EntityTag.class, findHeaderDelegate(hps, EntityTag.class));
            map.put(MediaType.class, findHeaderDelegate(hps, MediaType.class));
            map.put(CacheControl.class, findHeaderDelegate(hps, CacheControl.class));
            map.put(NewCookie.class, findHeaderDelegate(hps, NewCookie.class));
            map.put(Cookie.class, findHeaderDelegate(hps, Cookie.class));
            map.put(URI.class, findHeaderDelegate(hps, URI.class));
            map.put(Date.class, findHeaderDelegate(hps, Date.class));
            map.put(String.class, findHeaderDelegate(hps, String.class));

        } catch (Exception e) {
            LOG.error("init WebxRestfulRuntimeDelegateImpl error", e);
        }
    }

    @Override
    public UriBuilder createUriBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseBuilder createResponseBuilder() {
        return new WebxRestfulResponseBuilder();
    }

    @Override
    public VariantListBuilder createVariantListBuilder() {
        return new VariantListBuilder();
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException,
                                                                               UnsupportedOperationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type parameter cannot be null");
        }

        @SuppressWarnings("unchecked")
        HeaderDelegate<T> delegate = (HeaderDelegate<T>) map.get(type);
        if (delegate != null) {
            return delegate;
        }

        return null;
    }

    private HeaderDelegate<?> findHeaderDelegate(Set<HeaderDelegateProvider<?>> hps, Class<?> type) {
        for (HeaderDelegateProvider<?> hp : hps) {
            if (hp.supports(type)) {
                return hp;
            }
        }

        return null;
    }

}
