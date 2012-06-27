package com.alibaba.webx.restful.model.finder;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AnnotatedClassVisitor implements ClassVisitor {

    private final Set<String> annotations;
    private boolean           isScoped;
    private boolean           isAnnotated;

    private ClassInfo         classInfo;

    public AnnotatedClassVisitor(Class<? extends Annotation>... annotations){
        this.annotations = getAnnotationSet(annotations);
    }

    private Set<String> getAnnotationSet(Class<? extends Annotation>... annotations) {
        Set<String> a = new HashSet<String>();
        for (Class<?> c : annotations) {
            a.add("L" + c.getName().replaceAll("\\.", "/") + ";");
        }
        return a;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        classInfo = new ClassInfo(version, access, name, signature, superName, interfaces);

        isScoped = (access & Opcodes.ACC_PUBLIC) != 0;
        isAnnotated = false;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        isAnnotated |= this.annotations.contains(desc);
        return null;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        // If the name of the class that was visited is equal
        // to the name of this visited inner class then
        // this access field needs to be used for checking the scope
        // of the inner class
        if (classInfo.getName().equals(name)) {
            isScoped = (access & Opcodes.ACC_PUBLIC) != 0;

            // Inner classes need to be statically scoped
            isScoped &= (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
        }
    }

    public void visitEnd() {

    }

    public boolean isScoped() {
        return isScoped;
    }

    public boolean isAnnotated() {
        return isAnnotated;
    }

    public void visitOuterClass(String string, String string0, String string1) {
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return null;
    }

    public void visitSource(String string, String string0) {
    }

    public void visitAttribute(Attribute attribute) {
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new ResourceMethodVisitor(this, access, name, desc, signature, exceptions);
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

}
