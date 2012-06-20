package com.alibaba.webx.restful.model;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.springframework.util.Assert;

import com.alibaba.webx.restful.uri.PathPattern;
import com.alibaba.webx.restful.util.IdentityHashSet;
import com.google.common.collect.Lists;

/**
 * Model of a single resource component.
 * <p>
 * Resource component model represents a collection of {@link ResourceMethod methods} grouped under the same parent
 * request path template. {@code Resource} class is also the main entry point to the programmatic resource modeling API
 * that provides ability to programmatically extend the existing JAX-RS annotated resource classes or build new resource
 * models that may be utilized by Jersey runtime.
 * </p>
 * <p>
 * For example:
 * 
 * <pre>
 * &#64;Path("hello")
 * public class HelloResource {
 *      &#64;GET
 *      &#64;Produces("text/plain")
 *      public String sayHello() {
 *          return "Hello!";
 *      }
 * }
 * 
 * ...
 * 
 * // Register the annotated resource.
 * ResourceConfig resourceConfig = new ResourceConfig(HelloResource.class);
 * 
 * // Add new "hello2" resource using the annotated resource class
 * // and overriding the resource path.
 * Resource.Builder resourceBuilder =
 *         Resource.builder(HelloResource.class, new LinkedList&lt;ResourceModelIssue&gt;())
 *         .path("hello2");
 * 
 * // Add a new (virtual) sub-resource method to the "hello2" resource.
 * resourceBuilder.addMethod("GET")
 *         .path("world")
 *         .produces("text/plain")
 *         .handledBy(new Inflector&lt;Request, String&gt;() {
 *                 &#64;Override
 *                 public String apply(Request request) {
 *                     return "Hello World!";
 *                 }
 *         });
 * 
 * // Register the new programmatic resource in the application's configuration.
 * resourceConfig.addResources(resourceBuilder.build());
 * </pre>
 * 
 * The following table illustrates the supported requests and provided responses for the application configured in the
 * example above.
 * <table>
 * <tr>
 * <th>Request</th>
 * <th>Response</th>
 * <th>Method invoked</th>
 * </tr>
 * <tr>
 * <td>{@code "GET /hello"}</td>
 * <td>{@code "Hello!"}</td>
 * <td>{@code HelloResource.sayHello()}</td>
 * </tr>
 * <tr>
 * <td>{@code "GET /hello2"}</td>
 * <td>{@code "Hello!"}</td>
 * <td>{@code HelloResource.sayHello()}</td>
 * </tr>
 * <tr>
 * <td>{@code "GET /hello2/world"}</td>
 * <td>{@code "Hello World!"}</td>
 * <td>{@code Inflector.apply()}</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public final class Resource implements Routed, ResourceModelComponent {

    /**
     * Check if the class is acceptable as a JAX-RS provider or resource.
     * <p/>
     * Method returns {@code false} if the class is either
     * <ul>
     * <li>abstract</li>
     * <li>interface</li>
     * <li>annotation</li>
     * <li>primitive</li>
     * <li>local class</li>
     * <li>non-static member class</li>
     * </ul>
     * 
     * @param c class to be checked.
     * @return {@code true} if the class is an acceptable JAX-RS provider or resource, {@code false} otherwise.
     */
    public static boolean isAcceptable(Class<?> c) {
        return !((c.getModifiers() & Modifier.ABSTRACT) != 0 || c.isPrimitive() || c.isAnnotation() || c.isInterface()
                 || c.isLocalClass() || (c.isMemberClass() && (c.getModifiers() & Modifier.STATIC) == 0));
    }

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

    @Override
    public String getPath() {
        return path;
    }

    @Override
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

    @Override
    public void accept(ResourceModelVisitor visitor) {
        visitor.visitResourceClass(this);
    }

    @Override
    public String toString() {
        return "Resource {" + ((path == null) ? "[unbound], " : "\"" + path + "\", ") + resourceMethods.size()
               + " resource methods, " + subResourceMethods.size() + " sub-resource methods, " + subResourceLocators
               + " sub-resource locators" + '}';
    }

    @Override
    public List<? extends ResourceModelComponent> getComponents() {
        List<ResourceMethod> components = new LinkedList<ResourceMethod>();

        components.addAll(getResourceMethods());
        components.addAll(getSubResourceMethods());
        components.addAll(getSubResourceLocators());

        return components;
    }
}
