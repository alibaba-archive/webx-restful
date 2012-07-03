package com.alibaba.webx.restful.process.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class HttpHeadersImpl implements HttpHeaders {

    private final HttpServletRequest httpRequest;

    private final List<MediaType>    acceptableMediaTypes = new ArrayList<MediaType>(1);
    private final List<Locale>       acceptableLanguages  = new ArrayList<Locale>(1);
    private MediaType                mediaType            = null;
    private Locale                   language             = null;
    private Map<String, Cookie>      cookies              = null;
    private Date                     date;

    public HttpHeadersImpl(HttpServletRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    @Override
    public List<String> getRequestHeader(String name) {
        List<String> headers = new ArrayList<String>(1);

        Enumeration<?> e = httpRequest.getHeaders(name);

        while (e.hasMoreElements()) {
            String value = (String) e.nextElement();
            headers.add(value);
        }

        return headers;
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>();

        Enumeration<?> nameEnum = httpRequest.getHeaderNames();
        while (nameEnum.hasMoreElements()) {
            String name = (String) nameEnum.nextElement();

            Enumeration<?> e = httpRequest.getHeaders(name);

            while (e.hasMoreElements()) {
                String value = (String) e.nextElement();
                map.add(name, value);
            }
        }

        return map;
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        return acceptableMediaTypes;
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        return acceptableLanguages;
    }

    @Override
    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public Locale getLanguage() {
        return language;
    }

    @Override
    public Map<String, Cookie> getCookies() {
        if (cookies == null) {
            Map<String, Cookie> map = new HashMap<String, Cookie>();
            for (javax.servlet.http.Cookie item : httpRequest.getCookies()) {
                Cookie cookie = new Cookie(item.getName(), item.getValue(), item.getPath(), item.getDomain(),
                                           item.getVersion());
                map.put(item.getName(), cookie);
            }
            cookies = map;
        }
        return cookies;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public int getLength() {
        return httpRequest.getContentLength();
    }

    @Override
    public String getHeaderString(String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
