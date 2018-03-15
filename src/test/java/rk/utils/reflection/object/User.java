/*
 * Created by Roman Kolesnik on 2018-03-14
 */
package rk.utils.reflection.object;

public class User {

    private static final String CODE = "CODE";

    public String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}