package com.alibaba.webx.restful.model.finder;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;

class VfsSchemeResourceFinderFactory implements UriSchemeResourceFinderFactory {

    public Set<String> getSchemes() {
        return new HashSet<String>(Arrays.asList("vfsfile", "vfszip", "vfs"));
    }

    VfsSchemeResourceFinderFactory(){
    }

    @Override
    public VfsSchemeScanner create(final URI uri) {
        ResourceFinderStack resourceFinderStack = new ResourceFinderStack();

        if (!uri.getScheme().equalsIgnoreCase("vfszip")) {
            resourceFinderStack.push(new FileSchemeResourceFinderFactory().create(UriBuilder.fromUri(uri).scheme("file").build()));
        } else {

            final String su = uri.toString();
            final int webInfIndex = su.indexOf("/WEB-INF/classes");
            if (webInfIndex != -1) {
                final String war = su.substring(0, webInfIndex);
                final String path = su.substring(webInfIndex + 1);

                final int warParentIndex = war.lastIndexOf('/');
                final String warParent = su.substring(0, warParentIndex);

                // Check is there is a war within an ear
                // If so we need to load the ear then obtain the InputStream
                // of the entry to the war
                if (warParent.endsWith(".ear")) {
                    final String warName = su.substring(warParentIndex + 1, war.length());
                    try {

                        final JarFileScanner jarFileScanner = new JarFileScanner(
                                                                                 new URL(warParent.replace("vfszip",
                                                                                                           "file")).openStream(),
                                                                                 "");

                        while (jarFileScanner.hasNext()) {
                            if (jarFileScanner.next().equals(warName)) {

                                resourceFinderStack.push(new JarFileScanner(
                                                                            new FilterInputStream(jarFileScanner.open()) {

                                                                                // This is required so that the
                                                                                // underlying ear
                                                                                // is not closed

                                                                                @Override
                                                                                public void close() throws IOException {
                                                                                }
                                                                            }, ""));
                            }
                        }

                    } catch (IOException e) {
                        throw new ResourceFinderException("IO error when scanning war " + uri, e);
                    }
                } else {
                    try {
                        resourceFinderStack.push(new JarFileScanner(
                                                                    new URL(war.replace("vfszip", "file")).openStream(),
                                                                    path));
                    } catch (IOException e) {
                        throw new ResourceFinderException("IO error when scanning war " + uri, e);
                    }
                }
            } else {
                try {
                    resourceFinderStack.push(new JarFileScanner(new URL(su).openStream(), ""));
                } catch (IOException e) {
                    throw new ResourceFinderException("IO error when scanning jar " + uri, e);
                }
            }
        }

        return new VfsSchemeScanner(resourceFinderStack);
    }

    private class VfsSchemeScanner implements ResourceFinder {

        private final ResourceFinderStack resourceFinderStack;

        private VfsSchemeScanner(final ResourceFinderStack resourceFinderStack){
            this.resourceFinderStack = resourceFinderStack;
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
            resourceFinderStack.next();
        }

        @Override
        public InputStream open() {
            return resourceFinderStack.open();
        }

        @Override
        public void reset() {
            resourceFinderStack.reset();
        }
    }
}
