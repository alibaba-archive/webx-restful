package com.alibaba.webx.restful.bvt;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.webx.restful.model.uri.PathPattern;


public class PathPatternTest extends TestCase {
    public void test_0 () throws Exception {
        PathPattern pattern = new PathPattern("/orders/{id}/{name}");
        
        List<String> list = new ArrayList<String>();
        pattern.match("/orders/123/jobs", list);
        System.out.println(list);
        
    }
}
