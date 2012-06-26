package com.alibaba.webx.restful.bvt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.webx.restful.model.uri.PathPattern;


public class PathPatternTest extends TestCase {
    public void test_0 () throws Exception {
        PathPattern pattern = new PathPattern("/orders/{id}/{name}");
        
        List<String> variables = pattern.getTemplate().getTemplateVariables();
        
        Assert.assertEquals("id", variables.get(0));
        Assert.assertEquals("name", variables.get(1));
        
        List<String> values = new ArrayList<String>();
        pattern.match("/orders/123/jobs", values);
        
        Assert.assertEquals("123", values.get(0));
        Assert.assertEquals("jobs", values.get(1));
        
        Map<String, String> map = new HashMap<String, String>();
        pattern.getTemplate().match("/orders/234/ljw", map);
        
        Assert.assertEquals("234", map.get("id"));
        Assert.assertEquals("ljw", map.get("name"));
    }
}
