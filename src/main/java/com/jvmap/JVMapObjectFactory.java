package main.java.com.jvmap;

import main.java.com.jvmap.interfaces.JVMap;
import main.java.com.jvmap.dao.Person;
import main.java.com.jvmap.dto.RemoteObjA;
import main.java.com.jvmap.dto.RemoteObjB;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Object factory can instantiate any kind of object as long as the annotations from the origin object (mapTo) matches
 * with the destiny object (mapName)which must also have a default constructor.
 *
 * In order to generate objects as intended, the origin and destiny objects must be correctly annotated and the data
 * types from the origin and destiny field must match. As the getters and setters are used by the reflections, the
 * objects must also have the designated getters and setters.
 *
 *
 *
 * @author marcussantos
 */

public class JVMapObjectFactory {

    private LinkedList<Class> remoteClass;
    private LinkedList<Class> localClass;
    private Map<Class, Map<String, CallableMethod>> remoteClassCallables;
    private Map<Class, Map<String, CallableMethod>> localClassCallables;

    public JVMapObjectFactory() {

        remoteClass = new LinkedList<>();
        localClass = new LinkedList<>();

        remoteClassCallables = new HashMap<>();
        localClassCallables  = new HashMap<>();

        initClassMappings();
        initCallableGetterAndSetters(remoteClass, MethodType.GET);
        initCallableGetterAndSetters(localClass, MethodType.SET);
    }

    private void initClassMappings() {
        // Remote classes to be mapped
        remoteClass.add(RemoteObjA.class);
        remoteClass.add(RemoteObjB.class);
        // Local classes
        localClass.add(Person.class);

        // Add a Class here to get the annotations of each object
        // which have mapping properties
    }

    private void initCallableGetterAndSetters(LinkedList<Class> classCallables, MethodType methodType ) {
        for (Class class_ : classCallables) {
            HashMap<String,CallableMethod> mappings = new HashMap<>();
            for (Field field : class_.getDeclaredFields()) {
                try {
                    String fieldName = field.getName();
                    String targetName = methodType == MethodType.GET ? field.getAnnotation(JVMap.class).mapTo() : field
                            .getAnnotation(JVMap.class).mapName();
                    mappings.put( targetName, new CallableMethod(fieldName, methodType, class_, field.getType() ) );
                } catch (NullPointerException e) {
                    System.out.println("No mapping available for field "+field.getName());
                }
            }
            if (methodType == MethodType.GET) {
                this.remoteClassCallables.put(class_, mappings);
            } else {
                this.localClassCallables.put(class_, mappings);
            }
        }
    }

    /**
     * Instantiate an object of a given Class from one or more annotated objects
     * @param objectClass
     * @param args
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public Object generateObject(Class objectClass, Object... args) throws Exception {

        if (args.length <= 0) {
            throw new InvalidParameterException("");
        }

        Object newObject = objectClass.getDeclaredConstructor().newInstance();
        Map<String, CallableMethod> callablesDestinyObject = localClassCallables.get(objectClass);

        for (Object obj : args) {
            Map<String, CallableMethod> callableMapOrigin = remoteClassCallables.get(obj.getClass());
            if (callableMapOrigin == null) {
                throw new Exception();
            }
            for (Map.Entry entry : callableMapOrigin.entrySet()) {
                String mapName = (String) entry.getKey();
                CallableMethod callableGetMethod = (CallableMethod) entry.getValue();
                CallableMethod callableSetMethod = callablesDestinyObject.get(mapName);

                Object arg = callableGetMethod.invoke(obj);
                callableSetMethod.invoke(newObject, arg);
            }
        }

        return newObject;
    }

    /**
     * The purpose of this class is to recycle the reflections in order to only
     * create them once per field
     *
     * NOTE: Class fields with prefixes e.g. mName may interfere with the generation of
     * name of the setter/getter function.
     *
     * @author marcussantos
     */
    class CallableMethod {

        private Method method;
        private Class<?> parameterType;
        private String fieldName; // Target or origin field name
        private Class optClass;

        public CallableMethod(String fieldName, MethodType methodType, Class class_, Class<?> parameterType) {
            this.parameterType = parameterType;
            this.fieldName = fieldName;
            this.optClass = class_;

            String cap = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String methodPrefix = methodType == MethodType.GET ? MethodType.GET.toString().toLowerCase() : MethodType.SET.toString().toLowerCase();

            try {
                if (methodType == MethodType.GET) {
                    method = class_.getMethod(methodPrefix+cap);
                } else if (parameterType != null) {
                    method = class_.getMethod(methodPrefix+cap, parameterType);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public Object invoke(Object obj, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            if (method != null && obj.getClass() == optClass ) {
                return method.invoke(obj, args);
            } else {
                // TODO: Create exception for this case
                throw new NoSuchMethodException();
            }
        }
    }
}

enum MethodType {
    SET,
    GET
}


