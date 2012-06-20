package com.alibaba.webx.restful.server.internal.scanning;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.springframework.util.Assert;

import com.alibaba.webx.restful.util.ReflectionUtils;

public final class ResourceProcessorImpl implements ResourceProcessor {

    private final ClassLoader              classloader;

    private final Map<Class<?>, ClassInfo> classes;

    private final Set<String>              annotations;

    private final AnnotatedClassVisitor    classVisitor;

    public ResourceProcessorImpl(ClassLoader classloader, Class<? extends Annotation>... annotations){
        this.classloader = classloader;
        this.classes = new LinkedHashMap<Class<?>, ClassInfo>();
        this.annotations = getAnnotationSet(annotations);
        this.classVisitor = new AnnotatedClassVisitor();
    }

    public Map<Class<?>, ClassInfo> getAnnotatedClasses() {
        return classes;
    }

    private Set<String> getAnnotationSet(Class<? extends Annotation>... annotations) {
        Set<String> a = new HashSet<String>();
        for (Class<?> c : annotations) {
            a.add("L" + c.getName().replaceAll("\\.", "/") + ";");
        }
        return a;
    }

    // ScannerListener
    public boolean accept(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        return name.endsWith(".class");
    }

    public void process(String name, InputStream in) throws IOException {
        ClassReader classReader = new ClassReader(in);
        classReader.accept(classVisitor, 0);
    }

    private final class AnnotatedClassVisitor implements ClassVisitor {

        /**
         * True if the class has the correct scope
         */
        private boolean   isScoped;
        /**
         * True if the class has the correct declared annotations
         */
        private boolean   isAnnotated;

        private ClassInfo classInfo;

        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            classInfo = new ClassInfo(version, access, name, signature, superName, interfaces);

            isScoped = (access & Opcodes.ACC_PUBLIC) != 0;
            isAnnotated = false;
        }

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            isAnnotated |= annotations.contains(desc);
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
            if (isScoped && isAnnotated) {
                // Correctly scoped and annotated
                // add to the set of matching classes.
                Class<?> clazz = getClassForName(classInfo.getName().replaceAll("/", "."));

                classes.put(clazz, classInfo);
            }
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
            if (!isAnnotated) {
                return null;
            }

            if ("<init>".equals(name)) {
                return null;
            }

            return new ResourceMethodVisitor(access, name, desc, signature, exceptions);
        }

        private Class<?> getClassForName(String className) {
            try {
                return ReflectionUtils.classForNameWithException(className, classloader);
            } catch (ClassNotFoundException ex) {
                String s = "A class file of the class name, " + className
                           + "is identified but the class could not be found";
                throw new RuntimeException(s, ex);
            }
        }

        private class ResourceMethodVisitor implements MethodVisitor {

            private MethodInfo    methodInfo;

            private transient int localVariableIndex = -1;

            public ResourceMethodVisitor(int access, String name, String desc, String signature, String[] exceptions){
                methodInfo = new MethodInfo();
                methodInfo.access = access;
                methodInfo.name = name;
                methodInfo.desc = desc;
                methodInfo.signature = signature;
                methodInfo.exceptions = exceptions;
            }

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                // Ljavax/ws/rs/GET;
                return null;
            }

            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                localVariableIndex++;

                if (localVariableIndex == 0) {
                    if (!Modifier.isStatic(methodInfo.access)) {
                        Assert.isTrue("this".equals(name));
                        return;
                    }
                }

                methodInfo.parameterNames.add(name);
            }

            @Override
            public void visitEnd() {
                classInfo.getMethods().add(methodInfo);
            }

            @Override
            public AnnotationVisitor visitAnnotationDefault() {
                return null;
            }

            @Override
            public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
                return null;
            }

            @Override
            public void visitAttribute(Attribute attr) {

            }

            @Override
            public void visitCode() {

            }

            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {

            }

            @Override
            public void visitInsn(int opcode) {

            }

            @Override
            public void visitIntInsn(int opcode, int operand) {

            }

            @Override
            public void visitVarInsn(int opcode, int var) {

            }

            @Override
            public void visitTypeInsn(int opcode, String desc) {

            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {

            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc) {

            }

            @Override
            public void visitJumpInsn(int opcode, Label label) {

            }

            @Override
            public void visitLabel(Label label) {

            }

            @Override
            public void visitLdcInsn(Object cst) {

            }

            @Override
            public void visitIincInsn(int var, int increment) {

            }

            @Override
            public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {

            }

            @Override
            public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {

            }

            @Override
            public void visitMultiANewArrayInsn(String desc, int dims) {

            }

            @Override
            public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {

            }

            @Override
            public void visitLineNumber(int line, Label start) {

            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {

            }

        }
    }

    public static class ClassInfo {

        private final int        version;
        private final int        access;
        private final String     name;
        private final String     signature;
        private final String     superName;
        private final String[]   interfaces;

        private List<MethodInfo> methods = new ArrayList<MethodInfo>();

        public ClassInfo(int version, int access, String name, String signature, String superName, String[] interfaces){
            super();
            this.version = version;
            this.access = access;
            this.name = name;
            this.signature = signature;
            this.superName = superName;
            this.interfaces = interfaces;
        }

        public List<MethodInfo> getMethods() {
            return methods;
        }

        public int getVersion() {
            return version;
        }

        public int getAccess() {
            return access;
        }

        public String getName() {
            return name;
        }

        public String getSignature() {
            return signature;
        }

        public String getSuperName() {
            return superName;
        }

        public String[] getInterfaces() {
            return interfaces;
        }

    }

    public static class MethodInfo {

        private int          access;
        private String       name;
        private String       desc;
        private String       signature;
        private String[]     exceptions;

        private List<String> parameterNames = new ArrayList<String>();

        public int getAccess() {
            return access;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public String getSignature() {
            return signature;
        }

        public String[] getExceptions() {
            return exceptions;
        }

        public List<String> getParameterNames() {
            return parameterNames;
        }

    }

}
