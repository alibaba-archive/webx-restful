package com.alibaba.webx.restful.process;

import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Provider
public class JSONMessageBodyWriter<T> implements MessageBodyWriter<T> {

    public JSONMessageBodyWriter(){

    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }

        if (mediaType == MediaType.APPLICATION_JSON_TYPE) {
            return true;
        }

        String subtype = mediaType.getSubtype();
        return "json".equalsIgnoreCase(subtype) || subtype.endsWith("+json");
    }

    @Override
    public long getSize(T object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(T object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
                                                                                              throws java.io.IOException,
                                                                                              javax.ws.rs.WebApplicationException {
        String encoding = (String) httpHeaders.getFirst(HttpHeaders.CONTENT_ENCODING);

        if (encoding == null) {
            // TODO from httpRequest
        }

        if (encoding == null) {
            encoding = "UTF-8";
        }

        SerializerFeature[] features = new SerializerFeature[] { SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.BrowserCompatible, SerializerFeature.WriteDateUseDateFormat };

        byte[] bytes = toJSONBytes(object, encoding, features);

        entityStream.write(bytes);
    }

    public static final byte[] toJSONBytes(Object object, String encoding, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }

            serializer.write(object);

            return out.toBytes(encoding);
        } finally {
            out.close();
        }
    }
}
