package com.alibaba.webx.restful.model;

import java.util.List;

public interface Parameterized {

    public List<Parameter> getParameters();

    /**
     * Provides information on presence of an entity parameter.
     * 
     * @return true if entity parameter is present, false otherwise
     */
    public boolean requiresEntity();
}
