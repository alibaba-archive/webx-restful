package com.alibaba.webx.restful.bvt;

import junit.framework.Assert;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.webx.restful.examples.helloworld.HelloworldTestBase;

public class OrderTest extends HelloworldTestBase {

    public void test_orders() throws Exception {

        Assert.assertNotNull(component);

        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setServletPath("/rest");
        request.setContextPath("/study");
        request.setRequestURI("/study/rest/orders/123");
        request.setServerPort(8080);
        request.setServerName("localhost");

        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        String content = response.getContentAsString();
        
        JSONObject json = JSON.parseObject(content);
        Assert.assertEquals(123, json.get("id"));
        Assert.assertEquals("name_123", json.get("name"));
    }
}
