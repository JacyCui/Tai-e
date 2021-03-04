/*
 * Tai-e: A Program Analysis Framework for Java
 *
 * Copyright (C) 2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Tai-e is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package pascal.taie.frontend.soot;

import pascal.taie.java.classes.Modifier;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class Modifiers {

    private static final Map<Integer, Set<Modifier>> modMap = new HashMap<>();

    private Modifiers() {
    }

    static Set<Modifier> convert(int modifiers) {
        return modMap.computeIfAbsent(modifiers, m -> {
            Set<Modifier> result = EnumSet.noneOf(Modifier.class);
            if (soot.Modifier.isAbstract(m)) {
                result.add(Modifier.ABSTRACT);
            }
            if (soot.Modifier.isFinal(m)) {
                result.add(Modifier.FINAL);
            }
            if (soot.Modifier.isInterface(m)) {
                result.add(Modifier.INTERFACE);
            }
            if (soot.Modifier.isNative(m)) {
                result.add(Modifier.NATIVE);
            }
            if (soot.Modifier.isPrivate(m)) {
                result.add(Modifier.PRIVATE);
            }
            if (soot.Modifier.isProtected(m)) {
                result.add(Modifier.PROTECTED);
            }
            if (soot.Modifier.isPublic(m)) {
                result.add(Modifier.PUBLIC);
            }
            if (soot.Modifier.isStatic(m)) {
                result.add(Modifier.STATIC);
            }
            if (soot.Modifier.isSynchronized(m)) {
                result.add(Modifier.SYNCHRONIZED);
            }
            if (soot.Modifier.isTransient(m)) {
                result.add(Modifier.TRANSIENT);
            }
            if (soot.Modifier.isVolatile(m)) {
                result.add(Modifier.VOLATILE);
            }
            if (soot.Modifier.isStrictFP(m)) {
                result.add(Modifier.STRICTFP);
            }
            if (soot.Modifier.isAnnotation(m)) {
                result.add(Modifier.ANNOTATION);
            }
            if (soot.Modifier.isEnum(m)) {
                result.add(Modifier.ENUM);
            }
            if (soot.Modifier.isSynthetic(m)) {
                result.add(Modifier.SYNTHETIC);
            }
            return result;
        });
    }
}
