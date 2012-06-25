package com.alibaba.webx.restful.message;

import java.text.ParseException;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.alibaba.webx.restful.spi.HeaderDelegateProvider;
import com.alibaba.webx.restful.util.StringBuilderUtils;

public class MediaTypeProvider implements HeaderDelegateProvider<MediaType> {

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

        try {
            return valueOf(HttpHeaderReader.newInstance(header));
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Error parsing media type '" + header + "'", ex);
        }
    }

    public static MediaType valueOf(HttpHeaderReader reader) throws ParseException {
        // Skip any white space
        reader.hasNext();

        // Get the type
        String type = reader.nextToken();
        reader.nextSeparator('/');
        // Get the subtype
        String subType = reader.nextToken();

        Map<String, String> params = null;

        if (reader.hasNext()) {
            params = HttpHeaderReader.readParameters(reader);
        }

        return new MediaType(type, subType, params);
    }
}
