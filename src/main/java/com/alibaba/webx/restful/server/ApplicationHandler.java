package com.alibaba.webx.restful.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.internal.inject.ServiceProviders;
import com.alibaba.webx.restful.message.internal.MessageBodyFactory;
import com.alibaba.webx.restful.model.Invocable;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.server.process.WebxRestfulRequestContext;
import com.alibaba.webx.restful.uri.PathPattern;
import com.alibaba.webx.restful.util.ApplicationContextUtils;

public class ApplicationHandler {

    private final static Log        LOG = LogFactory.getLog(ApplicationHandler.class);

    private final ApplicationConfig config;

    private ApplicationContext      applicationContext;

    public ApplicationHandler(Application application, ApplicationContext applicationContext){
        ApplicationContextUtils.setApplicationContext(applicationContext);

        this.config = (ApplicationConfig) application;
        this.applicationContext = applicationContext;

        initialize();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private void initialize() {
        config.lock();

        ServiceProviders providers = null;

        {
            Map map = applicationContext.getBeansOfType(ServiceProviders.class);
            Iterator iter = map.values().iterator();
            if (iter.hasNext()) {
                providers = (ServiceProviders) iter.next();
            }
        }

        final MessageBodyFactory workers = new MessageBodyFactory(providers);

    }

    public void service(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        WebxRestfulRequestContext requestContext = new WebxRestfulRequestContext(httpRequest, httpResponse);

        match(requestContext);

        Response response;
        try {
            response = process(requestContext);
        } catch (WebxRestfulProcessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Response process(WebxRestfulRequestContext requestContext) throws WebxRestfulProcessException {
        ResourceMethod resourceMethod = requestContext.getResourceMethod();

        Invocable invocable = resourceMethod.getInvocable();

        Object resourceInstance = null;
        try {
            resourceInstance = invocable.getHandlerConstructor().createInstance(requestContext);
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
        
        Response response = responseBuilder.build();
        return response;
    }

    public Object[] getParameterValues(WebxRestfulRequestContext requestContext) {
        ResourceMethod resourceMethod = requestContext.getResourceMethod();
        Invocable invocable = resourceMethod.getInvocable();
        Method method = invocable.getHandlingMethod();

        int argsLength = method.getParameterTypes().length;
        Object[] args = new Object[argsLength];
        for (int i = 0; i < argsLength; ++i) {
            Class<?> clazz = method.getParameterTypes()[i];
            Annotation[] annatations = method.getParameterAnnotations()[i];

            boolean isContext = false;
            for (Annotation item : annatations) {
                if (item.annotationType() == Context.class) {
                    isContext = true;
                    break;
                }
            }

            Object arg = null;
            if (isContext) {
                arg = getContextParameter(requestContext, clazz);
            }

        }

        return args;
    }

    private Object getContextParameter(WebxRestfulRequestContext requestContext, Class<?> clazz) {
        if (clazz == HttpServletRequest.class) {
            return requestContext.getHttpRequest();
        }

        if (clazz == HttpServletResponse.class) {
            return requestContext.getHttpResponse();
        }

        if (clazz == HttpHeaders.class) {
            return requestContext.getHttpHeaders();
        }

        if (clazz == SecurityContext.class) {
            return requestContext.getSecurityContext();
        }

        if (clazz == UriInfo.class) {
            return requestContext.getUriInfo();
        }

        if (clazz == Request.class) {
            return requestContext.getRequest();
        }

        throw new RuntimeException("TODO"); // TODO
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
