package edu.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,
	ElementType.METHOD,
	ElementType.CONSTRUCTOR,
	ElementType.ANNOTATION_TYPE})
public @interface TargetConfig {
	String value();
}
