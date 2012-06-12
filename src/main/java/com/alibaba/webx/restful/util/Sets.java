package com.alibaba.webx.restful.util;

import java.util.LinkedHashSet;
import java.util.Set;


public class Sets {
    public static <T> Set<T> newLinkedHashSet() {
        return new LinkedHashSet<T>();
    }
}
