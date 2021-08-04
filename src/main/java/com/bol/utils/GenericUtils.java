package com.bol.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * using generic types in enum converter and treeRepository we need to set their exact type
 *
 * @author Omid Rostami
 */
public final class GenericUtils {
    public static final int FIRST_INDEX = 0;

    private GenericUtils() {
    }

    public static Type extract(Class<?> c) {
        return extract(c, FIRST_INDEX);
    }

    public static Type extract(Class<?> c, int index) {
        Type[] generics = ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments();
        return generics[index];
    }
}
