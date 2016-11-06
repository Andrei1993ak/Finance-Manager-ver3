package com.gmail.a93ak.andrei19.finance30.model.annotations.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBIntegerPrimaryKey {
    String value() default "INTEGER PRIMARY KEY";
}
