package com.alibaba.webx.restful.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.webx.restful.model.finder.PackageNamesScanner;
import com.alibaba.webx.restful.model.finder.ResourceFinder;
import com.alibaba.webx.restful.model.finder.ResourceProcessorImpl;
import com.alibaba.webx.restful.model.finder.ResourceProcessorImpl.ClassInfo;

public class ResourceUtils {

    private static final Log LOG = LogFactory.getLog(ResourceUtils.class);

    public static Map<Class<?>, ClassInfo> scanResources(String[] packageNames) {
        Set<ResourceFinder> finders = new HashSet<ResourceFinder>();
        if (packageNames != null) {
            finders.add(new PackageNamesScanner(packageNames));
        }

        Class<? extends Annotation>[] annotations = (Class<? extends Annotation>[]) new Class<?>[] { Path.class,
                Provider.class };

        ClassLoader classLoader = ReflectionUtils.getContextClassLoader();

        ResourceProcessorImpl resourceProcessor = new ResourceProcessorImpl(classLoader, annotations);
        for (ResourceFinder resourceFinder : finders) {
            while (resourceFinder.hasNext()) {
                final String next = resourceFinder.next();

                if (resourceProcessor.accept(next)) {
                    try {
                        resourceProcessor.process(next, resourceFinder.open());
                    } catch (IOException e) {
                        // TODO L10N
                        LOG.warn("Unable to process {" + next + "}", e);
                    }
                }
            }
        }

        Map<Class<?>, ClassInfo> processResult = resourceProcessor.getAnnotatedClasses();
        return processResult;
    }
}
