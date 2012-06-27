package com.alibaba.webx.restful.util;

import java.util.ArrayList;
import java.util.Collection;


public class Lists {
    public static <T> ArrayList<T> newArrayList(Collection<T> items) {
        return new ArrayList<T>(items);
    }
}
