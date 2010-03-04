package org.tridas.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Lucas Madar
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface TridasEditProperties {
	/** Is this information provided by the server but not modifiable by the user? */
	boolean	readOnly() default false;
	
	/** Is this information intended for machine processing only (and not user viewable?) */
	boolean machineOnly() default false;
	
	/** Is this the farthest we should traverse in building an editor? 
	 * (for example, a timestamp should not expand into value and certainty)
	 */
	boolean finalType() default false;
}
