/*
 * Bamboo - A Program Analysis Framework for Java
 *
 * Copyright (C) 2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Bamboo is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package bamboo.pta.element;

public interface Obj {

    Kind getKind();

    Type getType();

    /**
     * For NORMAL and ARRAY, returns the allocation site.
     * For STRING_CONSTANT, returns the string constant.
     * For CLASS_CONSTANT, returns the class type.
     * For SPECIAL and ARTIFICIAL, the return value
     * depends on concrete implementation.
     */
    Object getAllocation();

    /**
     * @return the method containing the allocation site of this object.
     * Returns null for Some special objects, e.g., string constants,
     * which do not have such method.
     */
    Method getContainerMethod();

    enum Kind {
        NORMAL, // normal objects created by allocation sites
        STRING_CONSTANT, // string constants
        CLASS, // objects of java.lang.Class
        METHOD, // objects of java.lang.reflect.Method
        FIELD, // objects of java.lang.reflect.Field
        CONSTRUCTOR, // objects of java.lang.reflect.Constructor
        REFLECTIVE_OBJECT, // reflectively-created objects
        SPECIAL, // represents special objects (heap sensitivity
        // is not applied to these objects)
        ARTIFICIAL, // represents the non-exist objects
    }
}
