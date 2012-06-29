package com.alibaba.webx.restful.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.alibaba.webx.restful.model.uri.PathPattern;

public class ResourceMethod implements ResourceInfo {

    /**
     * Resource method classification based on the recognized JAX-RS resource method types.
     */
    public static enum JaxrsType {
        RESOURCE_METHOD {

            @Override
            PathPattern createPatternFor(String pathTemplate) {
                // template is ignored.
                return PathPattern.END_OF_PATH_PATTERN;
            }
        },

        SUB_RESOURCE_METHOD {

            @Override
            PathPattern createPatternFor(String pathTemplate) {
                return new PathPattern(pathTemplate, PathPattern.RightHandPath.capturingZeroSegments);
            }
        },

        SUB_RESOURCE_LOCATOR {

            @Override
            PathPattern createPatternFor(String pathTemplate) {
                return new PathPattern(pathTemplate, PathPattern.RightHandPath.capturingZeroOrMoreSegments);
            }
        };

        /**
         * Create a proper matching path pattern from the provided template for the selected method type.
         * 
         * @param pathTemplate method path template.
         * @return method matching path pattern.
         */
        abstract PathPattern createPatternFor(String pathTemplate);

        private static JaxrsType classify(String httpMethod, String methodPath) {
            if (httpMethod != null) {
                if (!httpMethod.isEmpty()) {
                    if (methodPath == null || methodPath.isEmpty() || "/".equals(methodPath)) {
                        return RESOURCE_METHOD;
                    } else {
                        return SUB_RESOURCE_METHOD;
                    }
                }
            } else if (!methodPath.isEmpty()) {
                return SUB_RESOURCE_LOCATOR;
            }

            String message = String.format("Unknown resource method model type: HTTP method = '%s', method path = '%s'.",
                                           httpMethod, methodPath);
            throw new IllegalStateException(message);
        }
    }

    // JAX-RS method type
    private final JaxrsType       type;
    // HttpMethod
    private final String          httpMethod;
    // Routed
    private final String          path;
    private final PathPattern     pathPattern;
    // Consuming & Producing
    private final List<MediaType> consumedTypes;
    private final List<MediaType> producedTypes;

    // Invocable
    private final Invocable       invocable;

    public ResourceMethod(final String httpMethod, final String path, final Collection<MediaType> consumedTypes,
                          final Collection<MediaType> producedTypes, final Invocable invocable){

        this.type = JaxrsType.classify(httpMethod, path);

        this.httpMethod = (httpMethod == null) ? httpMethod : httpMethod.toUpperCase();

        this.path = path;
        this.pathPattern = type.createPatternFor(path);

        this.consumedTypes = Collections.unmodifiableList(new ArrayList<MediaType>(consumedTypes));
        this.producedTypes = Collections.unmodifiableList(new ArrayList<MediaType>(producedTypes));
        this.invocable = invocable;
    }

    public JaxrsType getType() {
        return type;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public Invocable getInvocable() {
        return invocable;
    }

    public String getPath() {
        return path;
    }

    public PathPattern getPathPattern() {
        return pathPattern;
    }

    public List<MediaType> getConsumedTypes() {
        return consumedTypes;
    }

    public List<MediaType> getProducedTypes() {
        return producedTypes;
    }

    @Override
    public Method getResourceMethod() {
        return this.getInvocable().getMethod();
    }

    @Override
    public Class<?> getResourceClass() {
        return this.getInvocable().getConstructor().getHandlerClass();
    }

    public GenericType<?> getResponseType() {
        return this.getInvocable().getResponseType();
    }
    
    public Annotation[] getAnnotations() {
        return this.getInvocable().getAnnotations();
    }
}
