package com.alibaba.webx.restful.bvt;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import com.alibaba.webx.restful.server.ServerProperties;
import com.alibaba.webx.restful.server.WebxRestfulComponent;
import com.alibaba.webx.restful.server.WebxRestfulServletFilter;
import com.alibaba.webx.restful.util.ApplicationContextUtils;

public class HelloworldTest extends TestCase {

    public void test_hello() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("hello-spring.xml");

        MockServletContext servletContext = new MockServletContext();
        
        ApplicationContextUtils.setApplicationContext(servletContext, applicationContext);
        
        MockFilterConfig filterConfig = new MockFilterConfig(servletContext);
        
        filterConfig.addInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.alibaba.webx.restful.examples.helloworld");

        WebxRestfulServletFilter filter = new WebxRestfulServletFilter();
        filter.init(filterConfig);
        
        WebxRestfulComponent component = filter.getComponent();
        
        Assert.assertNotNull(component);
        
        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.setRequestURI("/jersey-study/rest/helloworld");
        request.setServerPort(8080);
        request.setServerName("localhost");
        
        MockFilterChain chain = new MockFilterChain();
        
        filter.doFilter(request, response, chain);
    }
}
