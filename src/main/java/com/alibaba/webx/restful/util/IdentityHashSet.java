package com.alibaba.webx.restful.util;

import java.util.AbstractSet;
import java.util.IdentityHashMap;
import java.util.Iterator;

public class IdentityHashSet<E> extends AbstractSet<E> {

    private transient IdentityHashMap<E, Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object                  PRESENT = new Object();

    public IdentityHashSet(){
        map = new IdentityHashMap<E, Object>();
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    public void clear() {
        map.clear();
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            IdentityHashSet<E> newSet = (IdentityHashSet<E>) super.clone();
            newSet.map = (IdentityHashMap<E, Object>) map.clone();
            return newSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
