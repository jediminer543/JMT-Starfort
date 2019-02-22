package org.jmt.starfort.ui.gui.window.debug;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares a method to be callable by the debug inspector for the purposes of debuging
 * 
 * Usefull for stuffs
 * 
 * @author jediminer543
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface DebugInspectable {

}
