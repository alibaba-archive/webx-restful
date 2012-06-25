package com.alibaba.webx.restful.model;

import java.util.List;

import com.alibaba.webx.restful.model.uri.PathPattern;

public final class Resource {

    private final String               name;
    private final String               path;
    private final PathPattern          pathPattern;
    private final boolean              isRoot;
    private final List<ResourceMethod> resourceMethods;
    private final List<ResourceMethod> subResourceMethods;
    private final List<ResourceMethod> subResourceLocators;

    public Resource(final String name, final String path, final boolean isRoot,
                    final List<ResourceMethod> resourceMethods, final List<ResourceMethod> subResourceMethods,
                    final List<ResourceMethod> subResourceLocators){

        this.name = name;
        this.path = path;
        this.isRoot = isRoot;

        this.pathPattern = (!isRoot || path == null || path.isEmpty()) ? PathPattern.OPEN_ROOT_PATH_PATTERN : new PathPattern(
                                                                                                                              path,
                                                                                                                              PathPattern.RightHandPath.capturingZeroOrMoreSegments);

        this.resourceMethods = resourceMethods;
        this.subResourceMethods = subResourceMethods;
        this.subResourceLocators = subResourceLocators;
    }

    /**
     * Check if this resource model models a JAX-RS root resource.
     * 
     * @return {@code true}, if this is a model of a JAX-RS root resource, {@code false} otherwise.
     */
    public boolean isRootResource() {
        return isRoot;
    }

    public String getPath() {
        return path;
    }

    public PathPattern getPathPattern() {
        return pathPattern;
    }

    /**
     * Get the resource name.
     * <p/>
     * If the resource was constructed from a JAX-RS annotated resource class, the resource name will be set to the
     * {@link Class#getName() fully-qualified name} of the resource class.
     * 
     * @return reference JAX-RS resource handler class.
     */
    public String getName() {
        return name;
    }

    /**
     * Provides a non-null list of resource methods available on the resource.
     * 
     * @return non-null abstract resource method list.
     */
    public List<ResourceMethod> getResourceMethods() {
        return resourceMethods;
    }

    /**
     * Provides a non-null list of sub-resource methods available on the resource.
     * 
     * @return non-null abstract sub-resource method list.
     */
    public List<ResourceMethod> getSubResourceMethods() {
        return subResourceMethods;
    }

    /**
     * Provides a non-null list of sub-resource locators available on the resource.
     * 
     * @return non-null abstract sub-resource locator list.
     */
    public List<ResourceMethod> getSubResourceLocators() {
        return subResourceLocators;
    }

}
