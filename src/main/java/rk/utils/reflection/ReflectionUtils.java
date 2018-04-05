package rk.utils.reflection;

import rk.utils.reflection.exception.FieldAccessError;
import rk.utils.reflection.exception.FieldNotFoundError;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

//todo doc
public class ReflectionUtils {
    //todo add test
    public static Field getFieldByName(String name, Class class_) throws FieldNotFoundError {
        try {
            return class_.getField(name);
        } catch (NoSuchFieldException e) {
            throw new FieldNotFoundError(name);
        }
    }

    //todo add test
    public static void setFieldValue(Field field, Object fieldHolder, Object value) {
        try {
            field.setAccessible(true);
            field.set(fieldHolder, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(String fieldName, Object fieldHolder)
            throws FieldNotFoundError {
        if (fieldHolder == null) {
            throw new IllegalArgumentException("field holder cannot be null");
        }
        Optional<Field> field = findFieldByName(fieldName, fieldHolder);
        if (field.isPresent()) {
            return getFieldValue(field.get(), fieldHolder);
        }
        throw new FieldNotFoundError(fieldName);
    }

    public static Object getFieldValue(Field field, Object fieldHolder) {
        if (fieldHolder == null) {
            throw new IllegalArgumentException("field holder cannot be null");
        }
        try {
            field.setAccessible(true);
            return field.get(fieldHolder);
        } catch (Exception e) {
            throw new FieldAccessError(e);
        }
    }

    //todo add test
    public static List<Field> getAllFieldsOf(Object object) {
        return getAllFieldsOf(object.getClass());
    }

    /**
     * Collects all fields of a class including fields
     * of all super classes.
     */
    public static List<Field> getAllFieldsOf(Class<?> class_) {
        List<Field> fields = new ArrayList<>(Arrays.asList(class_.getDeclaredFields()));
        Class<?> superClass;
        while ((superClass = class_.getSuperclass()) != null) {
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
            class_ = superClass;
        }
        return fields;
    }

    //todo add test
    public static boolean isMap(Object object) {
        return Map.class.isAssignableFrom(object.getClass());
    }

    //todo add test
    public static boolean isCollection(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }

    //todo add test
    public static boolean isCollection(Object object) {
        return Collection.class.isAssignableFrom(object.getClass());
    }

    //todo add test
    public static boolean isMap(Field field) {
        return Map.class.isAssignableFrom(field.getType());
    }

    //todo add test
    public static boolean isStaticField(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * Looks for a field with a specific name inside some object.
     *
     * @param fieldName   name of the field being searched
     * @param fieldHolder field holder object which supposed to have a field with provided name
     */
    public static Optional<Field> findFieldByName(String fieldName, Object fieldHolder) {
        return getAllFieldsOf(fieldHolder.getClass())
                .stream()
                .filter(field -> field.getName().equals(fieldName))
                .findFirst();
    }
}
