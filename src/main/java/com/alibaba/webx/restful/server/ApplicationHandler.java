package com.alibaba.webx.restful.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceModelIssue;

public class ApplicationHandler {

    private final static Log        LOG = LogFactory.getLog(ApplicationHandler.class);

    private final ApplicationConfig configuration;

    public ApplicationHandler(Application application){
        this.configuration = ApplicationConfig.forApplication(application);

        initialize();
    }

    private void initialize() {
        configuration.lock();

        final List<ResourceModelIssue> resourceModelIssues = new LinkedList<ResourceModelIssue>();

        final Map<String, Resource.Builder> pathToResourceBuilderMap = new HashMap<String, Resource.Builder>();
        final List<Resource.Builder> resourcesBuilders = new LinkedList<Resource.Builder>();
        for (Class<?> c : configuration.getClasses()) {
            Path path = Resource.getPath(c);
            if (path != null) { // root resource
                try {
                    final Resource.Builder builder = Resource.builder(c, resourceModelIssues);
                    resourcesBuilders.add(builder);
                    pathToResourceBuilderMap.put(path.value(), builder);
                } catch (IllegalArgumentException ex) {
                    LOG.warn(ex.getMessage());
                }
            }
        }
    }
}
