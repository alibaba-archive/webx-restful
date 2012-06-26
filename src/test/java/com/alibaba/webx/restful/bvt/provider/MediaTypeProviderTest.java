package com.alibaba.webx.restful.bvt.provider;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.webx.restful.process.providers.MediaTypeProvider;


public class MediaTypeProviderTest extends TestCase {
    public void test_json() throws Exception {
        Assert.assertSame(MediaType.APPLICATION_XML_TYPE, MediaTypeProvider.getInstance().fromString(MediaType.APPLICATION_XML));
        Assert.assertSame(MediaType.APPLICATION_JSON_TYPE, MediaTypeProvider.getInstance().fromString(MediaType.APPLICATION_JSON));
    }
}
