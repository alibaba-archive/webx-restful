package com.alibaba.webx.restful.model.finder;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class JarFileScanner implements ResourceFinder {

    private final JarInputStream jarInputStream;
    private final String         parent;

    public JarFileScanner(InputStream inputStream, String parent) throws IOException{
        this.jarInputStream = new JarInputStream(inputStream);
        this.parent = parent;
    }

    private JarEntry next = null;

    @Override
    public boolean hasNext() {
        if (next == null) {
            try {
                do {
                    this.next = jarInputStream.getNextJarEntry();
                } while (next != null && (next.isDirectory() || !next.getName().startsWith(parent)));
            } catch (IOException e) {
                Logger.getLogger(JarFileScanner.class.getName()).log(Level.CONFIG,
                                                                     "Unable to read the next jar entry.", e);
                return false;
            } catch (SecurityException e) {
                Logger.getLogger(JarFileScanner.class.getName()).log(Level.CONFIG,
                                                                     "Unable to read the next jar entry.", e);
                return false;
            }
        }

        if (next == null) {
            try {
                jarInputStream.close();
            } catch (IOException e) {
                Logger.getLogger(JarFileScanner.class.getName()).log(Level.FINE, "Unable to close jar file.", e);
            }

            return false;
        }

        return true;
    }

    @Override
    public String next() {
        if (next != null || hasNext()) {
            final String name = next.getName();
            next = null;
            return name;
        }

        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream open() {
        return jarInputStream;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
