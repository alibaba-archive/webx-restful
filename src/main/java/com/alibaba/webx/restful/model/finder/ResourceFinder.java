package com.alibaba.webx.restful.model.finder;

import java.io.InputStream;
import java.util.Iterator;

public interface ResourceFinder extends Iterator<String> {

    InputStream open();

    void reset();

    @Override
    void remove();
}
