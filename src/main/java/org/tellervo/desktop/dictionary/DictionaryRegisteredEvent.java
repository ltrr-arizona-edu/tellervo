/**
 * Created at Sep 22, 2010, 11:25:35 AM
 */
package org.tellervo.desktop.dictionary;

import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * Dispatched when a dictionary is registered.  Get the
 * name with {@link #getValue()}, and the dictionary
 * with {@link #dictionary}.
 * @author Daniel
 *
 */
public class DictionaryRegisteredEvent extends ObjectEvent<String> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The actual dictionary.
	 */
	public final MVCArrayList<?> dictionary;
	
	/**
	 * @param argKey
	 * @param argValue
	 */
	public DictionaryRegisteredEvent(String argDictionaryName, MVCArrayList<?> argDictionary) {
		super(Dictionary.DICTIONARY_REGISTERED, argDictionaryName);
		dictionary = argDictionary;
	}
}
