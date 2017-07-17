package project.examples.advice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
public @interface TimeLogger {
    enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    };

    boolean logBefore() default true;
    boolean logAfter() default true;

    Level level() default Level.DEBUG;
}