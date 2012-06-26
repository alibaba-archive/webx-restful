package com.alibaba.webx.restful.model.uri;

import java.util.Comparator;

public class PathPatternComparator implements Comparator<PathPattern> {

    private final static PathPatternComparator instance = new PathPatternComparator();

    public final static PathPatternComparator getInstance() {
        return instance;
    }

    @Override
    public int compare(PathPattern o1, PathPattern o2) {
        return UriTemplate.COMPARATOR.compare(o1.template, o2.template);
    }
}
