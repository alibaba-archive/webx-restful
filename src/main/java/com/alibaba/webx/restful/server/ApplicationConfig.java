package com.alibaba.webx.restful.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ws.rs.core.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.server.internal.scanning.AnnotationAcceptingListener;
import com.alibaba.webx.restful.server.internal.scanning.FilesScanner;
import com.alibaba.webx.restful.server.internal.scanning.PackageNamesScanner;
import com.alibaba.webx.restful.util.ReflectionUtils;

public class ApplicationConfig extends Application {

    private final static Log          LOG         = LogFactory.getLog(ApplicationConfig.class);

    private ClassLoader               classLoader = null;
    private final Set<Class<?>>       classes;
    private final Set<Resource>       resources;
    private final Set<ResourceFinder> resourceFinders;
    private final Map<String, Object> properties;

    public ApplicationConfig(){
        this.classLoader = ReflectionUtils.getContextClassLoader();

        this.classes = new HashSet<Class<?>>();
        this.resources = new HashSet<Resource>();
        this.resourceFinders = new HashSet<ResourceFinder>();
        this.properties = new HashMap<String, Object>();
    }
    
    void lock() {
        
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public Set<ResourceFinder> getResourceFinders() {
        return resourceFinders;
    }

    ApplicationConfig _setApplication(Application app) {
        throw new UnsupportedOperationException();
    }

    Application _getApplication() {
        return this;
    }

    private void invalidateCache() {
        // this.cachedClasses = null;
        // this.cachedClassesView = null;
        // this.cachedSingletons = null;
        // this.cachedSingletonsView = null;
    }

    Set<Class<?>> _getClasses() {
        Set<Class<?>> result = new HashSet<Class<?>>();

        Set<ResourceFinder> rfs = new HashSet<ResourceFinder>(resourceFinders);

        // classes registered via configuration property
        String[] classNames = parsePropertyValue(ServerProperties.PROVIDER_CLASSNAMES);
        if (classNames != null) {
            for (String className : classNames) {
                try {
                    result.add(classLoader.loadClass(className));
                } catch (ClassNotFoundException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

        String[] packageNames = parsePropertyValue(ServerProperties.PROVIDER_PACKAGES);
        if (packageNames != null) {
            rfs.add(new PackageNamesScanner(packageNames));
        }

        String[] classPathElements = parsePropertyValue(ServerProperties.PROVIDER_CLASSPATH);
        if (classPathElements != null) {
            rfs.add(new FilesScanner(classPathElements));
        }

        AnnotationAcceptingListener afl = AnnotationAcceptingListener.newJaxrsResourceAndProviderListener(classLoader);
        for (ResourceFinder resourceFinder : rfs) {
            while (resourceFinder.hasNext()) {
                final String next = resourceFinder.next();

                if (afl.accept(next)) {
                    try {
                        afl.process(next, resourceFinder.open());
                    } catch (IOException e) {
                        // TODO L10N
                        LOG.warn("Unable to process {" + next + "}", e);
                    }
                }
            }
        }

        result.addAll(afl.getAnnotatedClasses());
        result.addAll(classes);
        return result;
    }

    private String[] parsePropertyValue(String propertyName) {
        String[] classNames = null;
        final Object o = properties.get(propertyName);
        if (o != null) {
            if (o instanceof String) {
                classNames = ApplicationConfig.getElements((String) o, ServerProperties.COMMON_DELIMITERS);
            } else if (o instanceof String[]) {
                classNames = ApplicationConfig.getElements((String[]) o, ServerProperties.COMMON_DELIMITERS);
            }
        }
        return classNames;
    }

    /**
     * Get a canonical array of String elements from a String array where each entry may contain zero or more elements
     * separated by ';'.
     * 
     * @param elements an array where each String entry may contain zero or more ';' separated elements.
     * @return the array of elements, each element is trimmed, the array will not contain any empty or null entries.
     */
    public static String[] getElements(String[] elements) {
        // keeping backwards compatibility
        return getElements(elements, ";");
    }

    /**
     * Get a canonical array of String elements from a String array where each entry may contain zero or more elements
     * separated by characters in delimiters string.
     * 
     * @param elements an array where each String entry may contain zero or more delimiters separated elements.
     * @param delimiters string with delimiters, every character represents one delimiter.
     * @return the array of elements, each element is trimmed, the array will not contain any empty or null entries.
     */
    public static String[] getElements(String[] elements, String delimiters) {
        List<String> es = new LinkedList<String>();
        for (String element : elements) {
            if (element == null) continue;
            element = element.trim();
            if (element.length() == 0) continue;
            for (String subElement : getElements(element, delimiters)) {
                if (subElement == null || subElement.length() == 0) continue;
                es.add(subElement);
            }
        }
        return es.toArray(new String[es.size()]);
    }

    /**
     * Get a canonical array of String elements from a String that may contain zero or more elements separated by
     * characters in delimiters string.
     * 
     * @param elements a String that may contain zero or more delimiters separated elements.
     * @param delimiters string with delimiters, every character represents one delimiter.
     * @return the array of elements, each element is trimmed.
     */
    private static String[] getElements(String elements, String delimiters) {
        String regex = "[";
        for (char c : delimiters.toCharArray())
            regex += Pattern.quote(String.valueOf(c));
        regex += "]";

        String[] es = elements.split(regex);
        for (int i = 0; i < es.length; i++) {
            es[i] = es[i].trim();
        }
        return es;
    }

    public static ApplicationConfig forApplication(Application application) {
        return (ApplicationConfig) application;
    }

}
