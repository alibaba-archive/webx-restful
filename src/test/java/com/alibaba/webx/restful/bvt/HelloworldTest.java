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

import com.alibaba.webx.restful.WebxRestfulServletFilter;
import com.alibaba.webx.restful.model.ServerProperties;
import com.alibaba.webx.restful.process.WebxRestfulComponent;
import com.alibaba.webx.restful.util.ApplicationContextUtils;

public class HelloworldTest extends TestCase {

    private ApplicationContext       applicationContext;
    private MockServletContext       servletContext;
    private WebxRestfulServletFilter filter;
    private WebxRestfulComponent     component;

    protected void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("hello-spring.xml");
        servletContext = new MockServletContext();

        ApplicationContextUtils.setApplicationContext(servletContext, applicationContext);

        MockFilterConfig filterConfig = new MockFilterConfig(servletContext);

        filterConfig.addInitParameter(ServerProperties.PROVIDER_PACKAGES,
                                      "com.alibaba.webx.restful.examples.helloworld");

        filter = new WebxRestfulServletFilter();
        filter.init(filterConfig);

        component = filter.getComponent();
    }

    public void test_hello() throws Exception {

        Assert.assertNotNull(component);

        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setServletPath("/rest");
        request.setContextPath("/jersey-study");
        request.setRequestURI("/jersey-study/rest/helloworld/now");
        request.setServerPort(8080);
        request.setServerName("localhost");

        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);
        
        String content = response.getContentAsString();
        System.out.println(content);
    }
}
