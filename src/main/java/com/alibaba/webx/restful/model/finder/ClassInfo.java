package com.alibaba.webx.restful.model.finder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.webx.restful.util.ClassUtils;

public class ClassInfo {

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

    public MethodInfo getMethodInfo(Member member) {
        if (member instanceof Constructor<?>) {
            return getMethodInfo((Constructor<?>) member);
        }

        return getMethodInfo((Method) member);
    }

    public MethodInfo getMethodInfo(Constructor<?> constructor) {
        String desc = ClassUtils.getDesc(constructor);

        for (MethodInfo item : this.methods) {
            if (!item.getName().equals("<init>")) {
                continue;
            }

            if (desc.equals(item.getDesc())) {
                return item;
            }
        }

        return null;
    }

    public MethodInfo getMethodInfo(Method method) {
        String desc = ClassUtils.getDesc(method);

        for (MethodInfo item : this.methods) {
            if (!item.getName().equals(method.getName())) {
                continue;
            }

            if (desc.equals(item.getDesc())) {
                return item;
            }
        }

        return null;
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