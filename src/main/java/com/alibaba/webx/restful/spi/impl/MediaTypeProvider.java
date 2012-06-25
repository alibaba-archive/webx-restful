package com.alibaba.webx.restful.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.alibaba.webx.restful.spi.HeaderDelegateProvider;
import com.alibaba.webx.restful.util.StringBuilderUtils;

public class MediaTypeProvider implements HeaderDelegateProvider<MediaType> {

    private final static MediaTypeProvider instance = new MediaTypeProvider();

    public static MediaTypeProvider getInstance() {
        return instance;
    }

    public List<MediaType> mediaTypes = new ArrayList<MediaType>();

    public MediaTypeProvider(){
        mediaTypes.add(MediaType.APPLICATION_ATOM_XML_TYPE);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        mediaTypes.add(MediaType.APPLICATION_JSON_TYPE);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        mediaTypes.add(MediaType.APPLICATION_SVG_XML_TYPE);
        mediaTypes.add(MediaType.APPLICATION_XHTML_XML_TYPE);
        mediaTypes.add(MediaType.APPLICATION_XML_TYPE);
        mediaTypes.add(MediaType.MULTIPART_FORM_DATA_TYPE);
        mediaTypes.add(MediaType.TEXT_HTML_TYPE);
        mediaTypes.add(MediaType.TEXT_PLAIN_TYPE);
        mediaTypes.add(MediaType.TEXT_XML_TYPE);
        mediaTypes.add(MediaType.WILDCARD_TYPE);
    }

    @Override
    public boolean supports(Class<?> type) {
        return MediaType.class.isAssignableFrom(type);
    }

    @Override
    public String toString(MediaType header) {
        StringBuilder b = new StringBuilder();
        b.append(header.getType()).append('/').append(header.getSubtype());
        for (Map.Entry<String, String> e : header.getParameters().entrySet()) {
            b.append("; ").append(e.getKey()).append('=');
            StringBuilderUtils.appendQuotedIfNonToken(b, e.getValue());
        }
        return b.toString();
    }

    @Override
    public MediaType fromString(String header) {
        if (header == null) {
            throw new IllegalArgumentException("Media type is null");
        }

        int slashIndex = header.indexOf('/');
        if (slashIndex == -1) {
            throw new IllegalArgumentException("'/' not found");
        }

        String type = header.substring(0, slashIndex);
        String rest = header.substring(slashIndex + 1);

        if (rest == null) {
            rest = "*";
        }

        int semiIndex = rest.indexOf(';');
        if (semiIndex != -1) {
            throw new IllegalArgumentException("support format : " + header);
        }

        String subType = rest;

        for (MediaType item : this.mediaTypes) {
            if (item.getType().equals(type) && item.getSubtype().endsWith(subType)) {
                return item;
            }
        }

        return new MediaType(type, subType);
    }

}
