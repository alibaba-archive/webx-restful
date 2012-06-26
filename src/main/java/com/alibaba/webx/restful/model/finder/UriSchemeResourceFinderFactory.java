package com.alibaba.webx.restful.model.finder;

import java.net.URI;
import java.util.Set;

interface UriSchemeResourceFinderFactory {

    Set<String> getSchemes();

    ResourceFinder create(URI u);
}
