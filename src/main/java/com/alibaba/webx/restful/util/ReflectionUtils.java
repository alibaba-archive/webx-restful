package com.alibaba.webx.restful.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ReflectionUtils {

    public static <T> Class<T> classForNameWithException(String name) throws ClassNotFoundException {
        return classForNameWithException(name, getContextClassLoader());
    }

    /**
     * Get the Class from the class name.
     * 
     * @param <T> class type.
     * @param name the class name.
     * @param cl the class loader to use, if null then the defining class loader of this class will be utilized.
     * @return the Class, otherwise null if the class cannot be found.
     * @throws ClassNotFoundException if the class cannot be found.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> classForNameWithException(String name, ClassLoader cl) throws ClassNotFoundException {
        if (cl != null) {
            try {
                return (Class<T>) Class.forName(name, false, cl);
            } catch (ClassNotFoundException ex) {
            }
        }
        return (Class<T>) Class.forName(name);
    }

    /**
     * Get the context class loader.
     * 
     * @return the context class loader, otherwise null security privileges are not set.
     */
    public static ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

            @Override
            public ClassLoader run() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException ex) {
                }
                return cl;
            }
        });
    }

    /**
     * Set a method to be accessible.
     * 
     * @param m the method to be set as accessible
     */
    public static void setAccessibleMethod(final Method m) {
        if (Modifier.isPublic(m.getModifiers())) {
            return;
        }

        AccessController.doPrivileged(new PrivilegedAction<Object>() {

            @Override
            public Object run() {
                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }
                return m;
            }
        });
    }

    /**
     * Prevents instantiation.
     */
    private ReflectionUtils(){
    }
}
