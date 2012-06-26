package com.alibaba.webx.restful.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.model.ApplicationConfig;
import com.alibaba.webx.restful.model.Invocable;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.uri.PathPattern;
import com.alibaba.webx.restful.spi.MessageBodyWorkerProvider;
import com.alibaba.webx.restful.util.ApplicationContextUtils;

public class ApplicationHandler {

    private final ApplicationConfig              config;

    private ApplicationContext                   applicationContext;

    private WebxRestfulMessageBodyWorkerProvider workers = new WebxRestfulMessageBodyWorkerProvider();

    public ApplicationHandler(Application application, ApplicationContext applicationContext){
        ApplicationContextUtils.setApplicationContext(applicationContext);

        this.config = (ApplicationConfig) application;
        this.applicationContext = applicationContext;

        initialize();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("rawtypes")
    private void initialize() {
        workers.addMessageBodyWriter(new JSONMessageBodyWriter());

    }

    public void service(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        WebxRestfulRequestContext requestContext = new WebxRestfulRequestContext(httpRequest, httpResponse,
                                                                                 this.workers);

        match(requestContext);

        WebxRestfulResponse response = process(requestContext);

        writeResponse(requestContext, response);
    }

    private WebxRestfulResponse process(WebxRestfulRequestContext requestContext) throws WebxRestfulProcessException {
        ResourceMethod resourceMethod = requestContext.getResourceMethod();

        if (resourceMethod == null) {
            throw new WebxRestfulProcessException("resourceMethod not match : " + requestContext.getUriInfo().getPath());
        }

        Invocable invocable = resourceMethod.getInvocable();

        Object resourceInstance = null;
        try {
            resourceInstance = invocable.getConstructor().createInstance(requestContext);
        } catch (Exception e) {
            throw new WebxRestfulProcessException("createResourceInstance error", e);
        }

        Object[] args = null;
        try {
            args = invocable.getArguments(requestContext);
        } catch (Exception e) {
            throw new WebxRestfulProcessException("get resourceMethod's arguemnts error", e);
        }

        Object returnObject = null;
        try {
            returnObject = invocable.invoke(resourceInstance, args);
        } catch (Exception e) {
            throw new WebxRestfulProcessException("invoke resourceMethod error", e);
        }

        ResponseBuilder responseBuilder;

        if (returnObject == null) {
            responseBuilder = Response.noContent();
        } else {
            responseBuilder = Response.ok(returnObject);
        }

        WebxRestfulResponse response = (WebxRestfulResponse) responseBuilder.build();
        response.setHttpResponse(requestContext.getHttpResponse());
        return response;
    }

    public void writeResponse(WebxRestfulRequestContext requestContext, WebxRestfulResponse response)
                                                                                                     throws IOException {
        MessageBodyWorkerProvider workers = requestContext.getWorkers();
        WebxRestfulWriterInterceptorContext interceptorContext = new WebxRestfulWriterInterceptorContext(workers,
                                                                                                         response);

        try {
            interceptorContext.proceed();
        } catch (Exception ex) {
            throw new MessageBodyProcessingException(ex.getMessage(), ex);
        }
    }

    private void match(WebxRestfulRequestContext requestContext) {
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
}
