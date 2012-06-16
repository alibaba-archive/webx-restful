package com.alibaba.webx.restful.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.internal.inject.ServiceProviders;
import com.alibaba.webx.restful.message.internal.MessageBodyFactory;
import com.alibaba.webx.restful.model.BasicValidator;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceModelIssue;
import com.alibaba.webx.restful.model.ResourceModelValidator;
import com.alibaba.webx.restful.util.ApplicationContextUtils;

public class ApplicationHandler {

    private final static Log        LOG = LogFactory.getLog(ApplicationHandler.class);

    private final ApplicationConfig config;

    private ApplicationContext      applicationContext;

    public ApplicationHandler(Application application, ApplicationContext applicationContext){
        ApplicationContextUtils.setApplicationContext(applicationContext);

        this.config = (ApplicationConfig) application;
        this.applicationContext = applicationContext;

        initialize();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private void initialize() {
        config.lock();

        final List<ResourceModelIssue> resourceModelIssues = new LinkedList<ResourceModelIssue>();

        final Map<String, Resource.Builder> pathToResourceBuilderMap = new HashMap<String, Resource.Builder>();
        final List<Resource.Builder> resourcesBuilders = new LinkedList<Resource.Builder>();
        for (Class<?> c : config.getClasses()) {
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

        for (Resource programmaticResource : config.getResources()) {
            Resource.Builder builder = pathToResourceBuilderMap.get(programmaticResource.getPath());
            if (builder != null) {
                builder.mergeWith(programmaticResource);
            } else {
                resourcesBuilders.add(Resource.builder(programmaticResource));
            }
        }

        ServiceProviders providers = null;

        {
            Map map = applicationContext.getBeansOfType(ServiceProviders.class);
            Iterator iter = map.values().iterator();
            if (iter.hasNext()) {
                providers = (ServiceProviders) iter.next();
            }
        }

        final MessageBodyFactory workers = new MessageBodyFactory(providers);

        final List<Resource> result = new ArrayList<Resource>(resourcesBuilders.size());
        ResourceModelValidator validator = new BasicValidator(resourceModelIssues, workers);

        for (Resource.Builder rb : resourcesBuilders) {
            final Resource r = rb.build();
            result.add(r);
            validator.validate(r);
        }
        processIssues(validator);

    }

    private void processIssues(ResourceModelValidator validator) {
        // TODO
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();

    }
}
