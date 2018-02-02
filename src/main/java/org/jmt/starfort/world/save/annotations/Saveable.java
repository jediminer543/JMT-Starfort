package org.jmt.starfort.world.save.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares a type as save-able using the default polict.
 * 
 * Doing so means that it will be saved during world save
 * 
 * @author jediminer543
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Saveable {

}
