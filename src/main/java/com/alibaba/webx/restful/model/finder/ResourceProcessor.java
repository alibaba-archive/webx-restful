package com.alibaba.webx.restful.model.finder;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceProcessor {

    boolean accept(String name);

    void process(String name, InputStream in) throws IOException;

}
