package com.alibaba.webx.restful.process;

import java.net.URI;
import java.util.Date;
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

import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.internal.inject.Providers;
import com.alibaba.webx.restful.message.MediaTypeProvider;
import com.alibaba.webx.restful.message.Responses;
import com.alibaba.webx.restful.message.VariantListBuilder;
import com.alibaba.webx.restful.spi.HeaderDelegateProvider;
import com.alibaba.webx.restful.util.ApplicationContextUtils;

public class WebxRestfulRuntimeDelegateImpl extends javax.ws.rs.ext.RuntimeDelegate {

    private Set<HeaderDelegateProvider>      hps;
    private Map<Class<?>, HeaderDelegate<?>> map;

    public WebxRestfulRuntimeDelegateImpl(){
        try {
            ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();

            // MediaTypeProvider
            Providers.bind(applicationContext, "webx-restful-mediaTypeProvider", new MediaTypeProvider());
            hps = Providers.getProviders(applicationContext, HeaderDelegateProvider.class);

            /**
             * Construct a map for quick look up of known header classes
             */
            map = new WeakHashMap<Class<?>, HeaderDelegate<?>>();
            map.put(EntityTag.class, _createHeaderDelegate(EntityTag.class));
            map.put(MediaType.class, _createHeaderDelegate(MediaType.class));
            map.put(CacheControl.class, _createHeaderDelegate(CacheControl.class));
            map.put(NewCookie.class, _createHeaderDelegate(NewCookie.class));
            map.put(Cookie.class, _createHeaderDelegate(Cookie.class));
            map.put(URI.class, _createHeaderDelegate(URI.class));
            map.put(Date.class, _createHeaderDelegate(Date.class));
            map.put(String.class, _createHeaderDelegate(String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UriBuilder createUriBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseBuilder createResponseBuilder() {
        return Responses.empty();
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

        return _createHeaderDelegate(type);
    }

    @SuppressWarnings("unchecked")
    private <T> HeaderDelegate<T> _createHeaderDelegate(Class<T> type) {
        for (HeaderDelegateProvider hp : hps) {
            if (hp.supports(type)) {
                return hp;
            }
        }

        return null;
    }

}
