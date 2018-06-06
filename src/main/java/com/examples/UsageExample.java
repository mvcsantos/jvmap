package main.java.com.examples;

import main.java.com.jvmap.JVMapObjectFactory;
import main.java.com.jvmap.dao.Person;
import main.java.com.jvmap.dto.RemoteObjA;
import main.java.com.jvmap.dto.RemoteObjB;

public class UsageExample {

    public static final void main(String[] args) {
        JVMapObjectFactory objectFactory = new JVMapObjectFactory();

        RemoteObjA remoteObjA = new RemoteObjA("Maria");
        RemoteObjB remoteObjB = new RemoteObjB("Joao", 15);

        try {
            Person person = (Person) objectFactory.generateObject(Person.class, remoteObjA, remoteObjB);
            System.out.println(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
