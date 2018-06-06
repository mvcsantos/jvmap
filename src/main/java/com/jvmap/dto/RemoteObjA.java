package main.java.com.jvmap.dto;

import main.java.com.jvmap.interfaces.JVMap;

public class RemoteObjA {

    @JVMap(mapTo = "name")
    private String name;

    public RemoteObjA() {
    }

    public RemoteObjA(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
