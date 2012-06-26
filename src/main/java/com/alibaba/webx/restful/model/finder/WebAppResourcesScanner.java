package com.alibaba.webx.restful.model.finder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.servlet.ServletContext;

public class WebAppResourcesScanner implements ResourceFinder {

    // private final String[] paths;
    private final ServletContext sc;
    private ResourceFinderStack  resourceFinderStack = new ResourceFinderStack();
    private static String[]      paths               = new String[] { "/WEB-INF/lib/", "/WEB-INF/classes/" };

    public WebAppResourcesScanner(final ServletContext sc){
        this.sc = sc;

        processPaths(paths);
    }

    @SuppressWarnings("unchecked")
    private void processPaths(String... paths) {
        for (final String path : paths) {

            final Set<String> resourcePaths = sc.getResourcePaths(path);
            if (resourcePaths == null) {
                break;
            }

            resourceFinderStack.push(new ResourceFinder() {

                private Deque<String> resourcePathsStack = new LinkedList<String>() {

                                                             private static final long serialVersionUID = 3109256773218160485L;

                                                             {
                                                                 for (String resourcePath : resourcePaths) {
                                                                     push(resourcePath);
                                                                 }
                                                             }
                                                         };
                private String        current;
                private String        next;

                @Override
                public boolean hasNext() {
                    while (next == null && !resourcePathsStack.isEmpty()) {
                        next = resourcePathsStack.pop();

                        if (next.endsWith("/")) {
                            processPaths(next);
                            next = null;
                        } else if (next.endsWith(".jar")) {
                            try {
                                resourceFinderStack.push(new JarFileScanner(sc.getResourceAsStream(next), ""));
                            } catch (IOException ioe) {
                                throw new ResourceFinderException(ioe);
                            }
                            next = null;
                        }
                    }

                    return next != null;
                }

                @Override
                public String next() {
                    if (next != null || hasNext()) {
                        current = next;
                        next = null;
                        return current;
                    }

                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public InputStream open() {
                    return sc.getResourceAsStream(current);
                }

                @Override
                public void reset() {
                    throw new UnsupportedOperationException();
                }
            });

        }
    }

    @Override
    public boolean hasNext() {
        return resourceFinderStack.hasNext();
    }

    @Override
    public String next() {
        return resourceFinderStack.next();
    }

    @Override
    public void remove() {
        resourceFinderStack.remove();
    }

    @Override
    public InputStream open() {
        return resourceFinderStack.open();
    }

    @Override
    public void reset() {
        resourceFinderStack = new ResourceFinderStack();
        processPaths(paths);
    }
}
