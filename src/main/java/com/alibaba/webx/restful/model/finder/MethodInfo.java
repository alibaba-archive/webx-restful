package com.alibaba.webx.restful.model.finder;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo {

    int          access;
    String       name;
    String       desc;
    String       signature;
    String[]     exceptions;

    List<String> parameterNames = new ArrayList<String>();

    public int getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getSignature() {
        return signature;
    }

    public String[] getExceptions() {
        return exceptions;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

}