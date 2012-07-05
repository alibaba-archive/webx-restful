package com.alibaba.webx.restful.process;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.MessageProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.model.ApplicationImpl;
import com.alibaba.webx.restful.model.Invocable;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.uri.PathPattern;
import com.alibaba.webx.restful.process.impl.ContainerRequestContextImpl;
import com.alibaba.webx.restful.process.impl.ResponseImpl;
import com.alibaba.webx.restful.process.impl.WriterInterceptorContextImpl;
import com.alibaba.webx.restful.util.ApplicationContextUtils;
import com.alibaba.webx.restful.util.ClassUtils;

public class ApplicationHandler {

    private final ApplicationImpl      config;

    private final ApplicationContext   applicationContext;

    private List<MessageBodyWriter<?>> messageBodyWriters = new ArrayList<MessageBodyWriter<?>>();
    private Set<WriterInterceptor>     writeInterceptors  = new LinkedHashSet<WriterInterceptor>();

    public ApplicationHandler(Application application, ApplicationContext applicationContext){
        ApplicationContextUtils.setApplicationContext(applicationContext);

        this.config = (ApplicationImpl) application;
        this.applicationContext = applicationContext;

        initialize();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("rawtypes")
    private void initialize() {
        messageBodyWriters.add(new JSONMessageBodyWriter());

        Map map = applicationContext.getBeansOfType(MessageBodyWriter.class);

        for (Object item : map.values()) {
            MessageBodyWriter writer = (MessageBodyWriter) item;
            messageBodyWriters.add(writer);
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response, UriInfo uri) throws IOException {
        ContainerRequestContextImpl requestContext = createRequestContext(request, response, uri);

        service(requestContext);
    }

    public ContainerRequestContextImpl createRequestContext(HttpServletRequest request, HttpServletResponse response,
                                                            UriInfo uri) {
        ContainerRequestContextImpl requestContext = new ContainerRequestContextImpl(request, response, uri);
        match(requestContext);
        return requestContext;
    }

    @SuppressWarnings({ "rawtypes" })
    public void service(RestfulRequestContext requestContext) throws IOException {

        if (requestContext.getResourceMethod() == null) {
            throw new ProcessException("no resource matched");
        }

        ResourceMethod resourceMethod = requestContext.getResourceMethod();

        if (resourceMethod == null) {
            throw new ProcessException("resourceMethod not match : " + requestContext.getUriInfo().getPath());
        }

        Object returnObject = invoke(requestContext, resourceMethod);

        ResponseBuilder responseBuilder = Response.ok();

        Annotation[] annotations = resourceMethod.getAnnotations();
        GenericType responseType = resourceMethod.getResponseType();
        responseBuilder.entity(returnObject, responseType.getType(), annotations);

        ResponseImpl response = (ResponseImpl) responseBuilder.build();
        response.setHttpResponse(requestContext.getHttpResponse());

        writeResponse(requestContext, response);
    }

    private Object invoke(RestfulRequestContext requestContext, ResourceMethod resourceMethod) throws ProcessException {
        Invocable invocable = resourceMethod.getInvocable();

        Object resourceInstance = null;
        try {
            resourceInstance = invocable.createInstance(requestContext);
        } catch (Exception e) {
            throw new ProcessException("createResourceInstance error", e);
        }

        Object[] args = null;
        try {
            args = invocable.getArguments(requestContext);
        } catch (Exception e) {
            throw new ProcessException("get resourceMethod's arguemnts error", e);
        }

        Object returnObject = null;
        try {
            returnObject = invocable.invoke(resourceInstance, args);
        } catch (Exception e) {
            throw new ProcessException("invoke resourceMethod error", e);
        }
        return returnObject;
    }

    public void writeResponse(RestfulRequestContext requestContext, ResponseImpl response) throws IOException {
        Set<WriterInterceptor> interceptorSet = getWriterInterceptors();

        if (interceptorSet.size() == 0) {
            aroundWrite(response);
            return;
        }

        List<WriterInterceptor> interceptors = new ArrayList<WriterInterceptor>(interceptorSet.size() + 1);
        interceptors.addAll(interceptorSet);
        interceptors.add(new TerminalWriterInterceptor());

        WriterInterceptorContextImpl writeContext = new WriterInterceptorContextImpl(interceptors);

        writeContext.setAnnotations(response.getAnnotations());
        writeContext.setEntity(response.getEntity());
        writeContext.setOutputStream(response.getHttpResponse().getOutputStream());
        writeContext.setMediaType(response.getMediaType());
        writeContext.setHeaders(response.getHeaders());

        try {
            writeContext.proceed();
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MessageProcessingException(ex.getMessage(), ex);
        }
    }

    private void match(RestfulRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();

        List<Resource> matchedResources = new ArrayList<Resource>();
        List<MatchResult> matchedResults = new ArrayList<MatchResult>();

        for (Resource resource : config.getResources()) {
            PathPattern pathPattern = resource.getPathPattern();
            MatchResult matchResult = pathPattern.match(path);
            if (matchResult != null) {
                matchedResources.add(resource);
                matchedResults.add(matchResult);
            }
        }

        for (int i = 0, size = matchedResources.size(); i < size; ++i) {
            ResourceMethod matchedResourceMethod = null;
            MatchResult resourceMethodResult = null;

            Resource resource = matchedResources.get(i);
            MatchResult matchResult = matchedResults.get(i);

            String methodPath = matchResult.group(matchResult.groupCount());

            for (ResourceMethod resourceMethod : resource.getSubResourceMethods()) {
                PathPattern pathPattern = resourceMethod.getPathPattern();

                resourceMethodResult = pathPattern.match(methodPath);
                if (resourceMethodResult != null) {
                    matchedResourceMethod = resourceMethod;
                    break;
                }
            }

            if (matchedResourceMethod == null) {
                for (ResourceMethod resourceMethod : resource.getResourceMethods()) {
                    matchedResourceMethod = resourceMethod;
                    break;
                }
            }

            if (matchedResourceMethod != null) {
                requestContext.setResource(resource);
                requestContext.setResourceMatchResult(matchResult);
                requestContext.setResourceMethod(matchedResourceMethod);
                requestContext.setResourceMethodMatchResult(resourceMethodResult);
                break;
            }
        }
    }

    public Set<WriterInterceptor> getWriterInterceptors() {
        return this.writeInterceptors;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void aroundWrite(ResponseImpl response) throws IOException {
        Type genericType = response.getDeclaredType();
        Class<?> type = ClassUtils.getClass(genericType);
        Annotation[] annotations = response.getAnnotations();
        MediaType mediaTye = response.getMediaType();

        Object entity = response.getEntity();
        MultivaluedMap<String, Object> headers = response.getHeaders();
        OutputStream outputStream = response.getHttpResponse().getOutputStream();

        final MessageBodyWriter writer = this.getMessageBodyWriter(type, genericType, annotations, mediaTye);

        if (writer == null) {
            String message = "messageBodyWriter not found, mediaType " + mediaTye + ", type " + type;
            throw new MessageProcessingException(message);
        }

        writer.writeTo(entity, type, genericType, annotations, mediaTye, headers, outputStream);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Class<?> type = context.getType();
        Type genericType = context.getGenericType();
        Annotation[] annotations = context.getAnnotations();
        MediaType mediaTye = context.getMediaType();

        Object entity = context.getEntity();
        MultivaluedMap<String, Object> headers = context.getHeaders();
        OutputStream outputStream = context.getOutputStream();

        final MessageBodyWriter writer = this.getMessageBodyWriter(type, genericType, annotations, mediaTye);

        if (writer == null) {
            String message = "messageBodyWriter not found, mediaType " + mediaTye + ", type " + type + ", genericType "
                             + genericType;
            throw new MessageProcessingException(message);
        }

        writer.writeTo(entity, type, genericType, annotations, mediaTye, headers, outputStream);
    }

    @SuppressWarnings("unchecked")
    public <T> MessageBodyWriter<T> getMessageBodyWriter(Class<T> type, Type genericType, Annotation[] annotations,
                                                         MediaType mediaType) {
        for (MessageBodyWriter<?> item : this.messageBodyWriters) {
            if (item.isWriteable(type, genericType, annotations, mediaType)) {
                return (MessageBodyWriter<T>) item;
            }
        }
        return null;
    }

    public class TerminalWriterInterceptor implements WriterInterceptor {

        public TerminalWriterInterceptor(){
        }

        @Override
        public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
            aroundWriteTo(context);
        }
    }

    public <T> MessageBodyReader<T> getMessageBodyReader(Class<T> type, Type genericType, Annotation[] annotations,
                                                         MediaType mediaType) {
        // TODO Auto-generated method stub
        return null;
    }

    public <T extends Throwable> ExceptionMapper<T> getExceptionMapper(Class<T> type) {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> ContextResolver<T> getContextResolver(Class<T> contextType, MediaType mediaType) {
        // TODO Auto-generated method stub
        return null;
    }
}
