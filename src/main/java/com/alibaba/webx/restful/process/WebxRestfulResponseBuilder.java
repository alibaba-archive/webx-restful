package com.alibaba.webx.restful.process;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Variant;

import com.alibaba.webx.restful.process.impl.ResponseImpl;
import com.alibaba.webx.restful.process.impl.StatusImpl;


public class WebxRestfulResponseBuilder extends ResponseBuilder {

    private StatusType                         status;
    private Object                             entity;
    private Annotation[]                       annotations;
    private GenericType<?>                     declaredType;
    private Set<String>                        allowMethods = new HashSet<String>(1);
    private MultivaluedHashMap<String, Object> headers      = new MultivaluedHashMap<String, Object>();

    public WebxRestfulResponseBuilder(){
        this(javax.ws.rs.core.Response.Status.NO_CONTENT);
    }

    public WebxRestfulResponseBuilder(StatusType status){
        this.status = status;
    }

    @Override
    public Response build() {
        ResponseImpl response = new ResponseImpl(status, entity, annotations, declaredType, allowMethods,
                                                               headers);
        return response;
    }

    @Override
    public ResponseBuilder clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public ResponseBuilder status(int status) {
        StatusType result = Status.fromStatusCode(status);
        if (result == null) {
            result = new StatusImpl(status, "");
        }
        this.status = result;

        return this;
    }

    @Override
    public ResponseBuilder entity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public ResponseBuilder entity(Object entity, Annotation[] annotations) {
        this.entity = entity;
        this.annotations = annotations;
        return this;
    }

    @Override
    public <T> ResponseBuilder entity(T entity, GenericType<? super T> declaredType, Annotation[] annotations) {
        this.entity = entity;
        this.declaredType = declaredType;
        this.annotations = annotations;
        return this;
    }

    @Override
    public ResponseBuilder allow(String... methods) {
        for (String method : methods) {
            allowMethods.add(method);
        }
        return this;
    }

    @Override
    public ResponseBuilder allow(Set<String> methods) {
        allowMethods.addAll(methods);
        return this;
    }

    @Override
    public ResponseBuilder cacheControl(CacheControl cacheControl) {
        return headerSingle(HttpHeaders.CACHE_CONTROL, cacheControl);
    }

    public MultivaluedHashMap<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public ResponseBuilder encoding(String encoding) {
        return headerSingle(HttpHeaders.CONTENT_ENCODING, encoding);
    }

    @Override
    public ResponseBuilder header(String name, Object value) {
        this.getHeaders().add(name, value);
        return this;
    }

    public Response.ResponseBuilder headerSingle(String name, Object value) {
        this.getHeaders().putSingle(name, value);
        return this;
    }

    @Override
    public ResponseBuilder replaceAll(MultivaluedMap<String, Object> headers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseBuilder language(String language) {
        return headerSingle(HttpHeaders.CONTENT_LANGUAGE, language);
    }

    @Override
    public ResponseBuilder language(Locale language) {
        return headerSingle(HttpHeaders.CONTENT_LANGUAGE, language);
    }

    @Override
    public ResponseBuilder type(MediaType type) {
        return headerSingle(HttpHeaders.CONTENT_TYPE, type);
    }

    @Override
    public ResponseBuilder type(String type) {
        return type(type == null ? null : MediaType.valueOf(type));
    }

    @Override
    public ResponseBuilder variant(Variant variant) {
        if (variant == null) {
            type((MediaType) null);
            language((String) null);
            encoding(null);
            return this;
        }

        type(variant.getMediaType());
        language(variant.getLanguage());
        encoding(variant.getEncoding());

        return this;
    }

    @Override
    public ResponseBuilder contentLocation(URI location) {
        return headerSingle(HttpHeaders.CONTENT_LOCATION, location);
    }

    @Override
    public ResponseBuilder cookie(NewCookie... cookies) {
        if (cookies != null) {
            for (NewCookie cookie : cookies) {
                header(HttpHeaders.SET_COOKIE, cookie);
            }
        } else {
            header(HttpHeaders.SET_COOKIE, null);
        }
        return this;
    }

    @Override
    public ResponseBuilder expires(Date expires) {
        return headerSingle(HttpHeaders.EXPIRES, expires);
    }

    @Override
    public ResponseBuilder lastModified(Date lastModified) {
        return headerSingle(HttpHeaders.LAST_MODIFIED, lastModified);
    }

    @Override
    public ResponseBuilder location(URI location) {
        return headerSingle(HttpHeaders.LOCATION, location);
    }

    @Override
    public ResponseBuilder tag(EntityTag tag) {
        return headerSingle(HttpHeaders.ETAG, tag);
    }

    @Override
    public ResponseBuilder tag(String tag) {
        return tag(tag == null ? null : new EntityTag(tag));
    }

    @Override
    public ResponseBuilder variants(Variant... variants) {
        for (Variant variant : variants) {
            variant(variant);
        }
        return this;
    }

    @Override
    public ResponseBuilder variants(List<Variant> variants) {
        for (Variant variant : variants) {
            variant(variant);
        }
        return this;
    }

    @Override
    public ResponseBuilder links(Link... links) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseBuilder link(URI uri, String rel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseBuilder link(String uri, String rel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
