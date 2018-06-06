package main.java.com.jvmap.dto;

import main.java.com.jvmap.interfaces.JVMap;

public class RemoteObjB {

    private String name;
    @JVMap(mapTo = "age")
    private int age;

    public RemoteObjB(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
