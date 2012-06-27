package com.alibaba.webx.restful.util;

import java.util.HashMap;
import java.util.Map;

public class Maps {

    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
}
