package rk.utils.reflection;

import org.junit.jupiter.api.Test;
import rk.utils.reflection.exception.FieldNotFoundError;
import rk.utils.reflection.object.ClassWithNoFields;
import rk.utils.reflection.object.Customer;
import rk.utils.reflection.object.User;

import java.lang.reflect.Field;
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
        assertEquals(6, fields.size(), "wrong number of fields");
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("id")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("name")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("number")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("department")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("CODE")));
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals("CUSTOMER_CODE")));

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
}