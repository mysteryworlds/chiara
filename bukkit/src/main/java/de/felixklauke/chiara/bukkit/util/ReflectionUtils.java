package de.felixklauke.chiara.bukkit.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static Object getFieldValue(Object object, String fieldName) throws IllegalAccessException, NoSuchFieldException {

        Class<?> objectClass = object.getClass();
        Field declaredField = objectClass.getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        return declaredField.get(object);
    }
}
