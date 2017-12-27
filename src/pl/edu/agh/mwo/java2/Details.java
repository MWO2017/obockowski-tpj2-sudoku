package pl.edu.agh.mwo.java2;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(CLASS)
@Target(TYPE)
public @interface Details {
	String author() default "Olimp Bockowski";
	int currentRevision();
}
