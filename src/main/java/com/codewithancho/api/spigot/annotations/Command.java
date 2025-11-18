package com.codewithancho.api.spigot.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();
    String description() default "";
    String usage() default "";
    String permission() default "";
    String[] aliases() default {};
    boolean playerOnly() default false;
}