package com.alibaba.webx.restful.uri;

import java.util.Comparator;

public class PathPatternComparator implements Comparator<PathPattern> {

    @Override
    public int compare(PathPattern o1, PathPattern o2) {
        return UriTemplate.COMPARATOR.compare(o1.template, o2.template);
    }
}
