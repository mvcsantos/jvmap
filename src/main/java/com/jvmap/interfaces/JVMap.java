package main.java.com.jvmap.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to map remote dto's (Data Transfer Objects) to our local dao (Data Access Object)
 *
 * @author marcussantos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use in method only.
public @interface JVMap {

    /**
     * Name of the destiny field, must correspond to the mapName.
     * @return
     */
    String mapTo()   default "";

    /**
     * Name used to map to in the destiny object
     * @return
     */
    String mapName() default "";
}
