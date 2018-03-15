package rk.utils.reflection;

import rk.utils.reflection.exception.FieldAccessError;
import rk.utils.reflection.exception.FieldNotFoundError;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReflectionUtils {
    public static Field getFieldByName(String name, Class class_) throws FieldNotFoundError {
        try {
            return class_.getField(name);
        } catch (NoSuchFieldException e) {
            throw new FieldNotFoundError(name);
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

    /**
     * Collects all fields of a class including fields
     * of all super classes.
     */
    public static List<Field> getAllClassFields(Class<?> class_) {
        List<Field> fields = new ArrayList<>(Arrays.asList(class_.getDeclaredFields()));
        Class<?> superClass;
        while ((superClass = class_.getSuperclass()) != null) {
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
            class_ = superClass;
        }
        return fields;
    }

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
        return getAllClassFields(fieldHolder.getClass())
                .stream()
                .filter(field -> field.getName().equals(fieldName))
                .findFirst();
    }
}
