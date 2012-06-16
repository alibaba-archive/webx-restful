package com.alibaba.webx.restful.server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.webx.restful.message.internal.LocalizationMessages;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.server.internal.scanning.FilesScanner;
import com.alibaba.webx.restful.server.internal.scanning.PackageNamesScanner;
import com.alibaba.webx.restful.server.internal.scanning.ResourceProcessorImpl;
import com.alibaba.webx.restful.util.ReflectionUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ApplicationConfig extends Application {

    private static final Log          LOG                  = LogFactory.getLog(ApplicationConfig.class);
    //
    private transient Set<Class<?>>   cachedClasses        = null;
    private transient Set<Object>     cachedSingletons     = null;
    private transient Set<Object>     cachedSingletonsView = null;
    //
    private final Set<Class<?>>       classes;
    private final Set<Object>         singletons;
    private final Set<ResourceFinder> resourceFinders;
    //
    private final Set<Resource>       resources;
    private final Set<Resource>       resourcesView;
    private final Map<String, Object> properties;
    private final Map<String, Object> propertiesView;
    //
    //
    private ClassLoader               classLoader          = null;
    //
    private InternalState             internalState        = new Mutable();

    public ApplicationConfig(){
        this.classLoader = ReflectionUtils.getContextClassLoader();

        this.classes = Sets.newHashSet();
        this.singletons = Sets.newHashSet();
        this.resources = Sets.newHashSet();
        this.resourcesView = Collections.unmodifiableSet(this.resources);

        this.properties = Maps.newHashMap();
        this.propertiesView = Collections.unmodifiableMap(this.properties);

        this.resourceFinders = Sets.newHashSet();
    }

    void lock() {

    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Set<Class<?>> getClasses() {
        return cachedClasses;
    }

    public final Set<Resource> getResources() {
        return resourcesView;
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

    /**
     * Add a {@link ResourceFinder} to {@code ResourceConfig}.
     * 
     * @param resourceFinder {@link ResourceFinder}
     * @return updated resource configuration instance.
     */
    public final ApplicationConfig addFinder(ResourceFinder resourceFinder) {
        return internalState.addFinder(resourceFinder);
    }

    /**
     * Add properties to {@code ResourceConfig}. If any of the added properties exists already, he values of the
     * existing properties will be replaced with new values.
     * 
     * @param properties properties to add.
     * @return updated resource configuration instance.
     */
    public final ApplicationConfig addProperties(Map<String, Object> properties) {
        return internalState.addProperties(properties);
    }

    public final Object getProperty(String name) {
        return properties.get(name);
    }

    public final boolean isProperty(String name) {
        if (properties.containsKey(name)) {
            Object value = properties.get(name);
            if (value instanceof Boolean) {
                return Boolean.class.cast(value);
            } else {
                return Boolean.parseBoolean(value.toString());
            }
        }

        return false;
    }

    private void invalidateCache() {

    }

    public void init() {
        cachedClasses = loadClasses();
    }

    private Set<Class<?>> loadClasses() {
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

        ResourceProcessorImpl resourceProcessor = new ResourceProcessorImpl(classLoader, Path.class, Provider.class);
        for (ResourceFinder resourceFinder : rfs) {
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

        result.addAll(resourceProcessor.getAnnotatedClasses());
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

    private interface InternalState {

        ApplicationConfig addClasses(Set<Class<?>> classes);

        ApplicationConfig addResources(Set<Resource> resources);

        ApplicationConfig addFinder(ResourceFinder resourceFinder);

        ApplicationConfig addProperties(Map<String, Object> properties);

        ApplicationConfig addSingletons(Set<Object> singletons);

        ApplicationConfig setClassLoader(ClassLoader classLoader);

        ApplicationConfig setProperty(String name, Object value);

        ApplicationConfig setApplication(Application application);
    }

    private class Immutable implements InternalState {

        @Override
        public ApplicationConfig addClasses(Set<Class<?>> classes) {
            throw new IllegalStateException(LocalizationMessages.RC_NOT_MODIFIABLE());
        }

        @Override
        public ApplicationConfig addResources(Set<Resource> resources) {
            throw new IllegalStateException(LocalizationMessages.RC_NOT_MODIFIABLE());
        }

        @Override
        public ApplicationConfig addFinder(ResourceFinder resourceFinder) {
            throw new IllegalStateException(LocalizationMessages.RC_NOT_MODIFIABLE());
        }

        @Override
        public ApplicationConfig addProperties(Map<String, Object> properties) {
            throw new IllegalStateException(LocalizationMessages.RC_NOT_MODIFIABLE());
        }

        @Override
        public ApplicationConfig addSingletons(Set<Object> singletons) {
            throw new IllegalStateException(LocalizationMessages.RC_NOT_MODIFIABLE());
        }

        @Override
        public ApplicationConfig setClassLoader(ClassLoader classLoader) {
            throw new IllegalStateException(LocalizationMessages.RC_NOT_MODIFIABLE());
        }

        @Override
        public ApplicationConfig setProperty(String name, Object value) {
            throw new IllegalStateException(LocalizationMessages.RC_NOT_MODIFIABLE());
        }

        @Override
        public ApplicationConfig setApplication(Application application) {
            throw new IllegalStateException(LocalizationMessages.RC_NOT_MODIFIABLE());
        }
    }

    private class Mutable implements InternalState {

        @Override
        public ApplicationConfig addClasses(Set<Class<?>> classes) {
            invalidateCache();
            ApplicationConfig.this.classes.addAll(classes);
            return ApplicationConfig.this;
        }

        @Override
        public ApplicationConfig addResources(Set<Resource> resources) {
            ApplicationConfig.this.resources.addAll(resources);
            return ApplicationConfig.this;
        }

        @Override
        public ApplicationConfig addFinder(ResourceFinder resourceFinder) {
            invalidateCache();
            ApplicationConfig.this.resourceFinders.add(resourceFinder);
            return ApplicationConfig.this;
        }

        @Override
        public ApplicationConfig addProperties(Map<String, Object> properties) {
            invalidateCache();
            ApplicationConfig.this.properties.putAll(properties);
            return ApplicationConfig.this;
        }

        @Override
        public ApplicationConfig addSingletons(Set<Object> singletons) {
            invalidateCache();
            ApplicationConfig.this.singletons.addAll(singletons);
            return ApplicationConfig.this;
        }

        @Override
        public ApplicationConfig setClassLoader(ClassLoader classLoader) {
            invalidateCache();
            ApplicationConfig.this.classLoader = classLoader;
            return ApplicationConfig.this;
        }

        @Override
        public ApplicationConfig setProperty(String name, Object value) {
            invalidateCache();
            ApplicationConfig.this.properties.put(name, value);
            return ApplicationConfig.this;
        }

        @Override
        public ApplicationConfig setApplication(Application application) {
            invalidateCache();
            return ApplicationConfig.this._setApplication(application);
        }
    }
}
