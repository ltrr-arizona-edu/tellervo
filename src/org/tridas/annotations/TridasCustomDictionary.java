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
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface TridasCustomDictionary {
	/** The custom dictionary type (e.g. Corina), or perhaps some other source */
	TridasCustomDictionaryType type();
	
	/** The name of the custom directory, given the type namespace */
	String dictionary();
	
	/** The String identifier field name given in a generic field for the computer-based ID */
	String identifierField() default "";
	
	/** The sort method */
	TridasCustomDictionarySortType sortType() default TridasCustomDictionarySortType.NONE;
}
