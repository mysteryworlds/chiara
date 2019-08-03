package de.felixklauke.chiara.bukkit.util;

import java.lang.reflect.Field;
import java.util.Objects;

public class ReflectionUtils {

  public static Object getFieldValue(Object object, String fieldName)
      throws IllegalAccessException, NoSuchFieldException {

    Objects.requireNonNull(object, "Cannot extract value of null object.");
    Objects.requireNonNull(fieldName);

    Class<?> objectClass = object.getClass();
    Field declaredField = objectClass.getDeclaredField(fieldName);
    declaredField.setAccessible(true);
    return declaredField.get(object);
  }
}
