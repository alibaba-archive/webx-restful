package com.alibaba.webx.restful.model.finder;

import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ResourceFinderStack implements ResourceFinder {

    private final Deque<ResourceFinder> stack   = new LinkedList<ResourceFinder>();
    private ResourceFinder              current = null;

    @Override
    public boolean hasNext() {
        if (current == null) {
            if (!stack.isEmpty()) {
                current = stack.pop();
            } else {
                return false;
            }
        }

        if (current.hasNext()) {
            return true;
        } else {
            if (!stack.isEmpty()) {
                current = stack.pop();
                return hasNext();
            } else {
                return false;
            }
        }
    }

    @Override
    public String next() {
        if (hasNext()) {
            return current.next();
        }

        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        current.remove();
    }

    @Override
    public InputStream open() {
        return current.open();
    }

    public void push(ResourceFinder iterator) {
        stack.push(iterator);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
