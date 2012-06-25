package com.alibaba.webx.restful.model.finder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.alibaba.webx.restful.server.ApplicationConfig;
import com.alibaba.webx.restful.server.ServerProperties;

/**
 * A scanner that recursively scans directories and jar files. Files or jar entries are reported to a
 * {@link ResourceProcessor}.
 * 
 * @author Paul Sandoz
 */
public class FilesScanner implements ResourceFinder {

    private ResourceFinderStack resourceFinderStack = new ResourceFinderStack();

    /**
     * Scan from a set of packages.
     * 
     * @param files a {@link String} containing package names.
     */
    public FilesScanner(final String files){
        this(new String[] { files });
    }

    private final File[] files;

    /**
     * Scan from a set of packages.
     * 
     * @param fileNames an array of package names.
     */
    public FilesScanner(final String[] fileNames){
        files = new File[ApplicationConfig.getElements(fileNames, ServerProperties.COMMON_DELIMITERS).length];
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(fileNames[i]);
        }

        for (final File f : files) {
            processFile(f);
        }
    }

    void processFile(final File f) {

        if (f.getName().endsWith(".jar") || f.getName().endsWith(".zip")) {
            try {
                resourceFinderStack.push(new JarFileScanner(new FileInputStream(f), ""));
            } catch (IOException e) {
                // logging might be sufficient in this case
                throw new ResourceFinderException(e);
            }

        } else {
            resourceFinderStack.push(new FilesScannerResourceFinder(this, f));
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
        this.resourceFinderStack = new ResourceFinderStack();

        for (File f : files) {
            processFile(f);
        }
    }
}
