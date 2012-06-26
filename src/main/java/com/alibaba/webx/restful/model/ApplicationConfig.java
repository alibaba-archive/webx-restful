package com.alibaba.webx.restful.model;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.model.finder.FilesScanner;
import com.alibaba.webx.restful.model.finder.PackageNamesScanner;
import com.alibaba.webx.restful.model.finder.ResourceFinder;
import com.alibaba.webx.restful.model.finder.ResourceProcessorImpl;
import com.alibaba.webx.restful.model.finder.ResourceProcessorImpl.ClassInfo;
import com.alibaba.webx.restful.model.finder.ResourceProcessorImpl.MethodInfo;
import com.alibaba.webx.restful.model.param.AutowiredParameter;
import com.alibaba.webx.restful.model.param.HttpServletRequestParameter;
import com.alibaba.webx.restful.model.param.HttpServletResponseParameter;
import com.alibaba.webx.restful.model.param.ParameterProviderImpl;
import com.alibaba.webx.restful.spi.ParameterProvider;
import com.alibaba.webx.restful.util.ClassUtils;
import com.alibaba.webx.restful.util.ReflectionUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ApplicationConfig extends Application {

    private static final Log          LOG           = LogFactory.getLog(ApplicationConfig.class);
    //
    private transient Set<Class<?>>   cachedClasses = null;
    //
    private final Set<Class<?>>       classes;
    private final Set<ResourceFinder> resourceFinders;
    //
    private final Set<Resource>       resources;
    private final Map<String, Object> properties;

    //
    //
    private ClassLoader               classLoader   = null;

    //

    public ApplicationConfig(){
        this.classLoader = ReflectionUtils.getContextClassLoader();

        this.classes = Sets.newHashSet();
        this.resources = Sets.newHashSet();

        this.properties = Maps.newHashMap();

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
        return resources;
    }

    public void addResources(List<Resource> resources) {
        this.resources.addAll(resources);
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
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
    public boolean addFinder(ResourceFinder resourceFinder) {
        return this.resourceFinders.add(resourceFinder);
    }

    /**
     * Add properties to {@code ResourceConfig}. If any of the added properties exists already, he values of the
     * existing properties will be replaced with new values.
     * 
     * @param properties properties to add.
     * @return updated resource configuration instance.
     */
    public final void addProperties(Map<String, Object> properties) {
        this.properties.putAll(properties);
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

    public void init(ApplicationContext applicationContxt) {
        ParameterProvider parameterProvider = new ParameterProviderImpl(applicationContxt);

        Set<Class<?>> result = new HashSet<Class<?>>();

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

        Map<Class<?>, ClassInfo> scanResult = scanResources();

        result.addAll(scanResult.keySet());
        result.addAll(classes);

        cachedClasses = result;

        for (Map.Entry<Class<?>, ClassInfo> entry : scanResult.entrySet()) {
            Resource resource = buildResource(applicationContxt, parameterProvider, entry.getKey(), entry.getValue());

            if (resource == null) {
                continue;
            }

            this.addResource(resource);
        }
    }

    public static boolean isAcceptable(Class<?> c) {
        if (Modifier.isAbstract(c.getModifiers())) {
            return false;
        }

        if (c.isPrimitive()) {
            return false;
        }

        if (c.isAnnotation()) {
            return false;
        }

        if (c.isInterface()) {
            return false;
        }

        if (c.isLocalClass()) {
            return false;
        }

        if (c.isMemberClass()) {
            return false;
        }

        if (Modifier.isStatic(c.getModifiers())) {
            return false;
        }

        return true;
    }

    private Resource buildResource(ApplicationContext applicationContxt, ParameterProvider parameterProvider,
                                   Class<?> clazz, ClassInfo classInfo) {

        if (!isAcceptable(clazz)) {
            return null;
        }

        Path pathAnnotation = clazz.getAnnotation(Path.class);

        InstanceConstructor handlerConstructor;
        try {
            handlerConstructor = createHandlerConstructor(applicationContxt, parameterProvider, clazz, classInfo);
        } catch (Exception e) {
            LOG.error("load resourceClass error. class '" + clazz.getName() + "'", e);
            return null;
        }

        if (handlerConstructor == null) {
            LOG.error("load resourceClass error, constructor not found. class '" + clazz.getName() + "'");
            return null;
        }

        String name = clazz.getName();
        boolean isRoot = true;
        String path = pathAnnotation.value();
        List<ResourceMethod> resourceMethods = new ArrayList<ResourceMethod>();
        List<ResourceMethod> subResourceMethods = new ArrayList<ResourceMethod>();
        List<ResourceMethod> subResourceLocators = new ArrayList<ResourceMethod>();

        List<Method> declaredMethods = getAllDeclaredMethods(clazz);
        for (Method method : declaredMethods) {
            String httpMethod = getHttpMethod(method);
            String methodPath = getMethodPath(method);

            if (httpMethod == null && methodPath == null) {
                continue;
            }

            Collection<MediaType> consumedTypes = getConsumesMediaTypes(method);
            Collection<MediaType> producedTypes = getProducesMediaTypes(method);

            List<Parameter> invokeParameters = createParameters(parameterProvider, clazz, classInfo, method);
            Invocable invocable = new Invocable(handlerConstructor, method, invokeParameters);

            ResourceMethod resourceMethod = new ResourceMethod(httpMethod, methodPath, consumedTypes, producedTypes,
                                                               invocable);

            if (methodPath == null) {
                resourceMethods.add(resourceMethod);
            } else if (httpMethod == null) {
                subResourceLocators.add(resourceMethod);
            } else {
                subResourceMethods.add(resourceMethod);
            }
        }

        Resource resource = new Resource(name, path, isRoot, resourceMethods, subResourceMethods, subResourceLocators);

        // final Suspend suspend = am.getAnnotation(Suspend.class);

        return resource;
    }

    private static InstanceConstructor createHandlerConstructor(ApplicationContext applicationContxt,
                                                                ParameterProvider parameterProvider, Class<?> clazz,
                                                                ClassInfo classInfo) throws Exception {

        Constructor<?> constructor = null;
        for (Constructor<?> item : clazz.getConstructors()) {
            if (Modifier.isPublic(item.getModifiers())) {
                constructor = item;
                break;
            }
        }

        if (constructor == null) {
            return null;
        }

        List<Parameter> parameters = createParameters(parameterProvider, clazz, classInfo, constructor);

        List<InstanceSetter> autowireSetters = createSetters(applicationContxt, parameterProvider, clazz);

        return new InstanceConstructor(constructor, parameters, autowireSetters);
    }

    static List<InstanceSetter> createSetters(ApplicationContext applicationContext,
                                              ParameterProvider parameterProvider, Class<?> clazz) throws Exception {
        List<InstanceSetter> autowireSetters = new ArrayList<InstanceSetter>();

        List<Method> declaredMethods = getAllDeclaredMethods(clazz);
        for (Method method : declaredMethods) {
            if (method.getParameterTypes().length != 1) {
                continue;
            }

            if (!method.getName().startsWith("set")) {
                continue;
            }

            if (method.getName().length() < 4) {
                continue;
            }

            if (!Character.isUpperCase(method.getName().charAt(3))) {
                continue;
            }

            if (method.getAnnotation(Path.class) != null) {
                continue;
            }

            if (method.getAnnotation(GET.class) != null) {
                continue;
            }

            if (method.getAnnotation(POST.class) != null) {
                continue;
            }

            if (method.getAnnotation(DELETE.class) != null) {
                continue;
            }

            if (method.getAnnotation(PUT.class) != null) {
                continue;
            }

            String propertyName = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);

            Class<?> setterClass = method.getParameterTypes()[0];

            Autowired autowired = method.getAnnotation(Autowired.class);
            Qualifier qualifier = method.getAnnotation(Qualifier.class);

            Annotation[] annotations = method.getAnnotations();
            if (autowired == null) {
                Field field = ClassUtils.getField(clazz, propertyName);

                if (field != null) {
                    autowired = field.getAnnotation(Autowired.class);
                    qualifier = field.getAnnotation(Qualifier.class);
                    if (field.getAnnotations().length != 0) {
                        annotations = field.getAnnotations();
                    }
                }
            }

            if (annotations.length == 0) {
                annotations = method.getParameterAnnotations()[0];
            }
            Type paramType = method.getGenericParameterTypes()[0];

            Parameter parameter = parameterProvider.createParameter(clazz, method, propertyName, setterClass,
                                                                    paramType, annotations);

            InstanceSetter setter = new InstanceSetter(method, parameter);
            autowireSetters.add(setter);
        }
        return autowireSetters;
    }

    @SuppressWarnings("rawtypes")
    private static List<Parameter> createParameters(ParameterProvider parameterProvider, Class<?> clazz,
                                                    ClassInfo classInfo, Member member) {
        MethodInfo methodInfo = classInfo.getMethodInfo(member);

        Class<?>[] parameterClasses;
        Type[] parameterTypes;
        Annotation[][] annotationArrays;
        if (member instanceof Constructor<?>) {
            parameterClasses = ((Constructor) member).getParameterTypes();
            parameterTypes = ((Constructor) member).getGenericParameterTypes();
            annotationArrays = ((Constructor) member).getParameterAnnotations();
        } else {
            parameterClasses = ((Method) member).getParameterTypes();
            parameterTypes = ((Method) member).getGenericParameterTypes();
            annotationArrays = ((Method) member).getParameterAnnotations();
        }

        int parametersLength = parameterTypes.length;
        List<Parameter> parameters = new ArrayList<Parameter>(parametersLength);
        for (int i = 0; i < parametersLength; ++i) {
            Class<?> paramClass = parameterClasses[i];
            Type paramType = parameterTypes[i];
            Annotation[] annotations = annotationArrays[i];
            String name = methodInfo.getParameterNames().get(i);

            Parameter parameter = parameterProvider.createParameter(clazz, member, name, paramClass, paramType,
                                                                    annotations);
            parameters.add(parameter);
        }
        return parameters;
    }

    private static String getHttpMethod(Method method) {
        HttpMethod httpMethodAnnotation = null;

        for (Annotation annotation : method.getAnnotations()) {
            httpMethodAnnotation = annotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethodAnnotation != null) {
                break;
            }
        }

        String httpMethod;
        if (httpMethodAnnotation != null) {
            httpMethod = httpMethodAnnotation.value();
        } else {
            httpMethod = null;
        }
        return httpMethod;
    }

    private static String getMethodPath(Method method) {
        String methodPath;
        Path methodPathAnnotation = method.getAnnotation(Path.class);
        if (methodPathAnnotation != null) {
            methodPath = methodPathAnnotation.value();
        } else {
            methodPath = null;
        }
        return methodPath;
    }

    private static Collection<MediaType> getProducesMediaTypes(Method method) {
        Collection<MediaType> producedTypes = new ArrayList<MediaType>();
        {
            Produces producesAnnotation = method.getAnnotation(Produces.class);
            if (producesAnnotation != null) {
                for (String item : producesAnnotation.value()) {
                    MediaType mediaType = MediaType.valueOf(item);
                    producedTypes.add(mediaType);
                }
            }
        }
        return producedTypes;
    }

    private static Collection<MediaType> getConsumesMediaTypes(Method method) {
        Collection<MediaType> consumedTypes = new ArrayList<MediaType>();
        {
            Consumes consumesAnnotation = method.getAnnotation(Consumes.class);
            if (consumesAnnotation != null) {
                for (String item : consumesAnnotation.value()) {
                    MediaType mediaType = MediaType.valueOf(item);
                    consumedTypes.add(mediaType);
                }
            }
        }
        return consumedTypes;
    }

    private static List<Method> getAllDeclaredMethods(Class<?> c) {
        List<Method> l = new ArrayList<Method>();
        while (c != null && c != Object.class) {
            l.addAll(Arrays.asList(c.getDeclaredMethods()));
            c = c.getSuperclass();
        }
        return l;
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, ClassInfo> scanResources() {
        Set<ResourceFinder> rfs = new HashSet<ResourceFinder>(resourceFinders);
        String[] packageNames = parsePropertyValue(ServerProperties.PROVIDER_PACKAGES);
        if (packageNames != null) {
            rfs.add(new PackageNamesScanner(packageNames));
        }

        String[] classPathElements = parsePropertyValue(ServerProperties.PROVIDER_CLASSPATH);
        if (classPathElements != null) {
            rfs.add(new FilesScanner(classPathElements));
        }

        Class<? extends Annotation>[] annotations = (Class<? extends Annotation>[]) new Class<?>[] { Path.class,
                Provider.class };
        ResourceProcessorImpl resourceProcessor = new ResourceProcessorImpl(classLoader, annotations);
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

        Map<Class<?>, ClassInfo> processResult = resourceProcessor.getAnnotatedClasses();
        return processResult;
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

}
