package com.alibaba.webx.restful.model.finder;

import java.net.URI;
import java.util.Set;



interface UriSchemeResourceFinderFactory {

    /**
     * Get the set of supported URI schemes.
     * 
     * @return the supported URI schemes.
     */
    Set<String> getSchemes();

    ResourceFinder create(URI u);
}
