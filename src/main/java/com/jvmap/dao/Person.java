package main.java.com.jvmap.dao;

import main.java.com.jvmap.interfaces.JVMap;

public class Person {
    @JVMap(mapName = "name")
    private String name;
    @JVMap(mapName = "age")
    private int age;

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "ObjA{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
