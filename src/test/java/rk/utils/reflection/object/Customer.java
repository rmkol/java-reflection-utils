/*
 * Created by Roman Kolesnik on 2018-03-14
 */
package rk.utils.reflection.object;

import java.util.List;
import java.util.Map;

public class Customer extends User {

    public static final String CUSTOMER_CODE = "C_CODE";

    public Integer number;
    public List<String> responsibilities;
    public Map<String,String> tasks;
    private String department;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
