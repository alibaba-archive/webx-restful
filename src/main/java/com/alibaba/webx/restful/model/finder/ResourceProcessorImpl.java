package com.alibaba.webx.restful.model.finder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassReader;

import com.alibaba.webx.restful.util.ReflectionUtils;

public final class ResourceProcessorImpl implements ResourceProcessor {

    private final static Log              LOG     = LogFactory.getLog(ResourceProcessorImpl.class);

    final ClassLoader                     classloader;

    private Map<Class<?>, ClassInfo>      classes = new LinkedHashMap<Class<?>, ClassInfo>();

    private Class<? extends Annotation>[] annotations;

    public ResourceProcessorImpl(ClassLoader classloader, Class<? extends Annotation>... annotations){
        this.classloader = classloader;
        this.annotations = annotations;
    }

    public Map<Class<?>, ClassInfo> getAnnotatedClasses() {
        return classes;
    }

    // ScannerListener
    public boolean accept(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        return name.endsWith(".class");
    }

    public void process(String name, InputStream in) throws IOException {
        AnnotatedClassVisitor classVisitor = new AnnotatedClassVisitor(annotations);

        ClassReader classReader = new ClassReader(in);
        classReader.accept(classVisitor, 0);

        if (classVisitor.isAnnotated() && classVisitor.isScoped()) {
            ClassInfo classInfo = classVisitor.getClassInfo();
            Class<?> clazz;
            try {
                String className = classInfo.getName().replaceAll("/", ".");
                clazz = ReflectionUtils.classForNameWithException(className);
            } catch (ClassNotFoundException e) {
                LOG.error(e.getMessage(), e);
                return;
            }

            this.classes.put(clazz, classInfo);
        }
    }
}
