package net.ruoxu.task.bean;

/**
 * Created by wangli on 16/11/30.
 */
public class TestBean {
    private String name;
    private String age;

    public TestBean(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
