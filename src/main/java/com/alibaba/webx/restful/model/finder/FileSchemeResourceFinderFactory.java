package com.alibaba.webx.restful.model.finder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

class FileSchemeResourceFinderFactory implements UriSchemeResourceFinderFactory {

    @Override
    public Set<String> getSchemes() {
        return Collections.singleton("file");
    }

    FileSchemeResourceFinderFactory(){
    }

    @Override
    public FileSchemeScanner create(URI uri) {
        return new FileSchemeScanner(uri);
    }

    private class FileSchemeScanner implements ResourceFinder {

        private ResourceFinderStack resourceFinderStack = null;

        private FileSchemeScanner(final URI uri){
            resourceFinderStack = new ResourceFinderStack();
            processFile(new File(uri.getPath()));
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
            throw new UnsupportedOperationException();
        }

        private void processFile(final File f) {
            resourceFinderStack.push(new ResourceFinder() {

                @SuppressWarnings("serial")
                Stack<File>  files = new Stack<File>() {

                                       {
                                           if (f.isDirectory()) {
                                               for (File file : f.listFiles()) {
                                                   push(file);
                                               }
                                           } else {
                                               push(f);
                                           }
                                       }
                                   };

                private File current;
                private File next;

                @Override
                public boolean hasNext() {
                    while (next == null && !files.empty()) {
                        next = files.pop();

                        if (next.isDirectory()) {
                            processFile(next);
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
                        return current.getName();
                    }

                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public InputStream open() {
                    try {
                        return new FileInputStream(current);
                    } catch (FileNotFoundException e) {
                        throw new ResourceFinderException(e);
                    }
                }

                @Override
                public void reset() {
                    throw new UnsupportedOperationException();
                }
            });
        }
    }
}
