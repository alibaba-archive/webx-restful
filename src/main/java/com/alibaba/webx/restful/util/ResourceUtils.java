package com.alibaba.webx.restful.util;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.alibaba.webx.restful.Constants;
import com.alibaba.webx.restful.model.InstanceConstructor;
import com.alibaba.webx.restful.model.MultiInstanceConstructor;
import com.alibaba.webx.restful.model.InstanceSetter;
import com.alibaba.webx.restful.model.Invocable;
import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.SingletonInstanceConstructor;
import com.alibaba.webx.restful.model.finder.ClassInfo;
import com.alibaba.webx.restful.model.finder.MethodInfo;
import com.alibaba.webx.restful.model.finder.PackageNamesScanner;
import com.alibaba.webx.restful.model.finder.ResourceFinder;
import com.alibaba.webx.restful.model.finder.ResourceProcessorImpl;
import com.alibaba.webx.restful.spi.ParameterProvider;

public class ResourceUtils {

    private static final Log LOG = LogFactory.getLog(ResourceUtils.class);

    public static String[] parsePropertyValue(Object property) {
        String[] classNames = null;
        final Object o = property;
        if (o != null) {
            if (o instanceof String) {
                classNames = getElements((String) o, Constants.COMMON_DELIMITERS);
            } else if (o instanceof String[]) {
                classNames = getElements((String[]) o, Constants.COMMON_DELIMITERS);
            }
        }
        return classNames;
    }

    public static Map<Class<?>, ClassInfo> scanResources(String[] packageNames) {
        return scanResources(new ArrayList<ResourceFinder>(), packageNames);
    }

    @SuppressWarnings("unchecked")
    public static Map<Class<?>, ClassInfo> scanResources(Collection<ResourceFinder> finders, String[] packageNames) {
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

    public static Resource buildResource(ApplicationContext applicationContxt, ParameterProvider parameterProvider,
                                         Class<?> clazz, ClassInfo classInfo, Object resouceInstance) {

        if (!isAcceptable(clazz)) {
            return null;
        }

        Path pathAnnotation = clazz.getAnnotation(Path.class);

        InstanceConstructor handlerConstructor;
        if (resouceInstance == null) {
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
        } else {
            handlerConstructor = new SingletonInstanceConstructor(clazz, resouceInstance);
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

    private static MultiInstanceConstructor createHandlerConstructor(ApplicationContext applicationContxt,
                                                                    ParameterProvider parameterProvider,
                                                                    Class<?> clazz, ClassInfo classInfo)
                                                                                                        throws Exception {

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

        return new MultiInstanceConstructor(constructor, parameters, autowireSetters);
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

            Annotation[] annotations = method.getAnnotations();
            Field field = ClassUtils.getField(clazz, propertyName);

            if (field != null) {
                if (field.getAnnotations().length != 0) {
                    annotations = field.getAnnotations();
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

    private static List<Method> getAllDeclaredMethods(Class<?> c) {
        List<Method> l = new ArrayList<Method>();
        while (c != null && c != Object.class) {
            l.addAll(Arrays.asList(c.getDeclaredMethods()));
            c = c.getSuperclass();
        }
        return l;
    }
}
