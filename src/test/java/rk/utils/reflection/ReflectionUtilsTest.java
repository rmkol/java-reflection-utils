package rk.utils.reflection;

import org.junit.jupiter.api.Test;
import rk.utils.reflection.exception.FieldNotFoundError;
import rk.utils.reflection.object.ClassWithNoFields;
import rk.utils.reflection.object.Customer;
import rk.utils.reflection.object.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilsTest {

    static Customer customer;

    static {
        customer = new Customer();
        customer.setId("id");
        customer.setName("name");
        customer.setDepartment("department");
        customer.setNumber(555);
        customer.responsibilities = new ArrayList<>();
        customer.tasks = new HashMap<>();
    }

    @Test
    void getFieldValueByName() {
        assertEquals(customer.getId(), ReflectionUtils.getFieldValue("id", customer),
                "wrong id field value");
        assertEquals(customer.getName(), ReflectionUtils.getFieldValue("name", customer),
                "wrong name field value");
        assertEquals(customer.getDepartment(), ReflectionUtils.getFieldValue("department", customer),
                "wrong department field value");
        assertEquals(customer.getNumber(), ReflectionUtils.getFieldValue("number", customer),
                "wrong name field value");
    }

    @Test
    void getFieldValueByNameWrongArguments() {
        assertThrows(FieldNotFoundError.class, () ->
                ReflectionUtils.getFieldValue("age", customer));
        assertThrows(IllegalArgumentException.class, () ->
                ReflectionUtils.getFieldValue("age", null));
    }

    @Test
    void getFieldValue() {
        Field field = getField("number", Customer.class);
        assertEquals(customer.getNumber(), ReflectionUtils.getFieldValue(field, customer),
                "wrong number field value");
    }

    @Test
    void getFieldValueNullObject() {
        Field field = getField("number", Customer.class);
        assertThrows(IllegalArgumentException.class, () ->
                ReflectionUtils.getFieldValue(field, null));
    }

    @Test
    void getAllFieldsOfClass() {
        List<Field> fields = ReflectionUtils.getAllFieldsOf(Customer.class);
        assertEquals(8, fields.size(), "wrong number of fields");
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("id")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("name")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("number")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("department")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("CODE")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("CUSTOMER_CODE")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("responsibilities")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("tasks")));

        fields = ReflectionUtils.getAllFieldsOf(User.class);
        assertEquals(3, fields.size(), "wrong number of fields");
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("id")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("name")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("CODE")));

        fields = ReflectionUtils.getAllFieldsOf(ClassWithNoFields.class);
        assertEquals(0, fields.size(), "wrong number of fields");
    }

    @Test
    void isStaticField() {
        Field staticField = getField("CUSTOMER_CODE", Customer.class);
        assertTrue(ReflectionUtils.isStaticField(staticField));

        Field numberField = getField("number", Customer.class);
        assertFalse(ReflectionUtils.isStaticField(numberField));
    }

    @Test
    void findFieldByName() {
        Optional<Field> field = ReflectionUtils.findFieldByName("id", customer);
        assertTrue(field.isPresent(), "field wasn't found");
        assertEquals("id", field.get().getName());

        field = ReflectionUtils.findFieldByName("name", customer);
        assertTrue(field.isPresent(), "field wasn't found");
        assertEquals("name", field.get().getName());

        field = ReflectionUtils.findFieldByName("number", customer);
        assertTrue(field.isPresent(), "field wasn't found");
        assertEquals("number", field.get().getName());

        field = ReflectionUtils.findFieldByName("department", customer);
        assertTrue(field.isPresent(), "field wasn't found");
        assertEquals("department", field.get().getName());

        //static fields
        field = ReflectionUtils.findFieldByName("CUSTOMER_CODE", customer);
        assertTrue(field.isPresent(), "field wasn't found");
        assertEquals("CUSTOMER_CODE", field.get().getName());

        field = ReflectionUtils.findFieldByName("CODE", customer);
        assertTrue(field.isPresent(), "field wasn't found");
        assertEquals("CODE", field.get().getName());
    }

    @SuppressWarnings("SameParameterValue")
    static Field getField(String fieldName, Class class_) {
        try {
            return class_.getField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getFieldByName() {
        Field field = ReflectionUtils.getFieldByName("name", User.class);
        assertNotNull(field);
        assertEquals("name", field.getName());
        //inherited field
        field = ReflectionUtils.getFieldByName("name", Customer.class);
        assertNotNull(field);
        assertEquals("name", field.getName());
        //non existing field
        assertThrows(FieldNotFoundError.class, () -> ReflectionUtils.getFieldByName("age", User.class));
        //static field
        field = ReflectionUtils.getFieldByName("CUSTOMER_CODE", Customer.class);
        assertNotNull(field);
        assertEquals("CUSTOMER_CODE", field.getName());
        //private field
        assertThrows(FieldNotFoundError.class, () -> ReflectionUtils.getFieldByName("id", Customer.class));
    }

    @Test
    void setFieldValue() {
        ReflectionUtils.setFieldValue(
                ReflectionUtils.getFieldByName("name", Customer.class),
                customer,
                "John"
        );
        assertEquals("John", customer.getName());
        //final field
        assertThrows(RuntimeException.class, () -> ReflectionUtils.setFieldValue(
                ReflectionUtils.getFieldByName("CUSTOMER_CODE", Customer.class),
                customer,
                "1212")
        );
    }

    @Test
    void getAllFieldsOfObject() {
        List<Field> fields = ReflectionUtils.getAllFieldsOf(customer);
        assertEquals(8, fields.size());
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("id")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("name")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("number")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("department")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("CODE")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("CUSTOMER_CODE")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("responsibilities")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("tasks")));
    }

    @Test
    void isMapObject() {
        assertTrue(ReflectionUtils.isMap(customer.tasks));
        assertFalse(ReflectionUtils.isMap(customer.name));
    }

    @Test
    void isMapField() {
        assertTrue(
                ReflectionUtils.isMap(ReflectionUtils.getFieldByName("tasks", Customer.class))
        );
        assertFalse(
                ReflectionUtils.isMap(ReflectionUtils.getFieldByName("number", Customer.class))
        );
    }

    @Test
    void isCollectionObject() {
        assertTrue(ReflectionUtils.isCollection(customer.responsibilities));
        assertFalse(ReflectionUtils.isCollection(customer.tasks));
    }

    @Test
    void isCollectionField() {
        assertTrue(
                ReflectionUtils.isCollection(ReflectionUtils.getFieldByName("responsibilities", Customer.class))
        );
        assertFalse(
                ReflectionUtils.isCollection(ReflectionUtils.getFieldByName("number", Customer.class))
        );
    }
}