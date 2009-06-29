/**
 * 
 */
package org.tridas.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Lucas Madar
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TridasUICategory {
	TridasUICategoryType category() default TridasUICategoryType.DEFAULT;
	String custom();
}
