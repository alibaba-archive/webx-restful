package com.alibaba.webx.restful.model.finder;

import java.lang.reflect.Modifier;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.springframework.util.Assert;


public class ResourceMethodVisitor implements MethodVisitor {

    /**
     * 
     */
    private final AnnotatedClassVisitor annotatedClassVisitor;

    private MethodInfo                  methodInfo;

    private transient int               localVariableIndex = -1;

    public ResourceMethodVisitor(AnnotatedClassVisitor annotatedClassVisitor, int access, String name, String desc,
                                 String signature, String[] exceptions){
        this.annotatedClassVisitor = annotatedClassVisitor;
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
        this.annotatedClassVisitor.getClassInfo().getMethods().add(methodInfo);
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
