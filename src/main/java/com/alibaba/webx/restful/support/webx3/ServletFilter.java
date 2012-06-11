package com.alibaba.webx.restful.support.webx3;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ServletFilter implements Filter {

    private WebApplicationContext applicationContext;

    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();

        Assert.notNull(servletContext);

        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        Assert.notNull(applicationContext);

        this.applicationContext = applicationContext;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
    }

    public WebApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void destroy() {
        this.applicationContext = null;
    }

}
