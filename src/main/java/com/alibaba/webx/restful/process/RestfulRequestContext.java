package com.alibaba.webx.restful.process;

import java.util.Map;
import java.util.regex.MatchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;

public interface RestfulRequestContext extends ContainerRequestContext {

    HttpServletRequest getHttpRequest();

    HttpServletResponse getHttpResponse();

    Map<String, String> getPathVariables();

    Resource getResource();

    void setResource(Resource resource);

    ResourceMethod getResourceMethod();

    void setResourceMethod(ResourceMethod resourceMethod);

    MatchResult getResourceMatchResult();

    void setResourceMatchResult(MatchResult resourceMatchResult);

    MatchResult getResourceMethodMatchResult();

    void setResourceMethodMatchResult(MatchResult resourceMethodMatchResult);
}
