package com.alibaba.webx.restful.model.finder;

import java.io.InputStream;
import java.util.Iterator;


public interface ResourceFinder extends Iterator<String> {

    /**
     * Open current resource.
     *
     * @return input stream from which current resource can be loaded.
     */
    InputStream open();

    /**
     * Reset the {@link ResourceFinder} instance.
     * <p/>
     * Upon calling this method the implementing class MUST reset its internal state to the initial state.
     */
    void reset();

    /**
     * {@inheritDoc}
     * <p/>
     * This operation is not supported by {@link ResourceFinder} & throws {@link UnsupportedOperationException}
     * when invoked.
     */
    @Override
    void remove();
}
