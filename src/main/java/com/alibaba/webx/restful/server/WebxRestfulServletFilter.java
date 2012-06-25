package com.alibaba.webx.restful.server;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.util.ApplicationContextUtils;

public class WebxRestfulServletFilter implements Filter {

    public static final String   JAXRS_APPLICATION_CLASS = "javax.ws.rs.Application";
    public static final String   PROVIDER_WEB_APP        = "jersey.config.servlet.provider.webapp";

    private WebxRestfulComponent component               = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext applicationContxt = ApplicationContextUtils.getApplicationContext(filterConfig.getServletContext());
        
        ApplicationConfig applicationConfig = createResourceConfig(filterConfig, applicationContxt);

        component = new WebxRestfulComponent(applicationConfig, applicationContxt);
    }

    public WebxRestfulComponent getComponent() {
        return component;
    }

    private ApplicationConfig createResourceConfig(FilterConfig filterConfig, ApplicationContext applicationContxt) throws ServletException {
        final Map<String, Object> initParams = getInitParams(filterConfig);

        final ApplicationConfig applicationConfig = new ApplicationConfig().addProperties(initParams);

        final String webapp = (String) applicationConfig.getProperty(PROVIDER_WEB_APP);
        if (webapp != null && !"false".equals(webapp)) {
            applicationConfig.addFinder(new WebAppResourcesScanner(filterConfig.getServletContext()));
        }
        
        ApplicationContextUtils.setApplicationContext(applicationContxt);

        applicationConfig.init(applicationContxt);

        return applicationConfig;
    }

    private Map<String, Object> getInitParams(FilterConfig webConfig) {
        Map<String, Object> props = new HashMap<String, Object>();
        Enumeration<?> names = webConfig.getInitParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            props.put(name, webConfig.getInitParameter(name));
        }
        return props;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        ApplicationHandler handler = component.getHandler();

        handler.service(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {

    }

}
