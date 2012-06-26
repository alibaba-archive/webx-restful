package com.alibaba.webx.restful.model.finder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Stack;

final class FilesScannerResourceFinder implements ResourceFinder {

    private final FilesScanner filesScanner;
    Stack<File>                files;
    private File               current;
    private File               next;

    @SuppressWarnings("serial")
    FilesScannerResourceFinder(FilesScanner filesScanner, final File f){
        this.filesScanner = filesScanner;
        files = new Stack<File>() {
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
    }

    @Override
    public boolean hasNext() {
        while (next == null && !files.empty()) {
            next = files.pop();

            if (next.isDirectory() || next.getName().endsWith(".jar") || next.getName().endsWith(".zip")) {
                this.filesScanner.processFile(next);
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
    }
}
