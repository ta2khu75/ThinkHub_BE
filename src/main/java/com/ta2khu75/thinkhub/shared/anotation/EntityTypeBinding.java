package com.ta2khu75.thinkhub.shared.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ta2khu75.thinkhub.shared.enums.EntityType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityTypeBinding {
    EntityType value();
}
