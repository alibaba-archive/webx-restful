package com.alibaba.webx.restful.internal.scanning;

import java.net.URI;
import java.util.Set;

import com.alibaba.webx.restful.server.ResourceFinder;


interface UriSchemeResourceFinderFactory {

    /**
     * Get the set of supported URI schemes.
     * 
     * @return the supported URI schemes.
     */
    Set<String> getSchemes();

    ResourceFinder create(URI u);
}
