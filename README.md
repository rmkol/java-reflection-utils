# Java Reflection Utils
A set of utility methods which simplify working with Java Reflection API.
The library is very lightweight (~ **5kb**) and has no external dependencies.  
The minimum Java version supported: **8**.

### Examples
Getting specific field by name:
```
Field field = ReflectionUtils.getFieldByName("email", Customer.class);
```

Setting field value:
```
Field emailField = ReflectionUtils.getFieldByName("email", Customer.class);
ReflectionUtils.setFieldValue(emailField, customer, "newemail@mail.com"); //no checked exceptions, no field unlocking
```

Getting all class fields including fields of superclasses:
```
class User {
    public String id;
}

class Customer extends User {
    public String email;
}

List<Field> allFields = ReflectionUtils.getAllFieldsOf(Customer.class); //result list will contain both 'id' and 'name' fields
```

Quickly checking field/object type:
```
Map map = new HashMap<String, String>();
ReflectionUtils.isMap(map); //true
ReflectionUtils.isCollection(map); //false
...
```

For more examples please see **rk.utils.reflection.ReflectionUtilsTest**.

### Building
Build the project just as a regular Maven project: `mvn package`.