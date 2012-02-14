/**
 * Created at Aug 22, 2010, 3:55:23 PM
 */
package org.tellervo.desktop.components.table;

import java.util.Comparator;

import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer;
import org.tridas.schema.ControlledVoc;


/**
 * Basically a helper class, so you don't have to write all this mess every time.
 * @author Daniel
 *
 */
public class ControlledVocDictionaryComboBox extends DynamicJComboBox<ControlledVoc>{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public ControlledVocDictionaryComboBox(String argDictionary){
		super(Dictionary.getMutableDictionary(argDictionary), new Comparator<ControlledVoc>(){
			@Override
			public int compare(ControlledVoc argO1, ControlledVoc argO2) {
				return argO1.getNormal().compareToIgnoreCase(argO2.getNormal());
			}
		});
		this.setKeySelectionManager(new DynamicKeySelectionManager(){
			/**
			 * @see org.tellervo.desktop.components.table.DynamicKeySelectionManager#convertToString(java.lang.Object)
			 */
			@Override
			public String convertToString(Object argO) {
				if(argO == null){
					return "";
				}
				ControlledVoc cv = (ControlledVoc) argO;
				return cv.getNormal();
			}
		});
		this.setRenderer(new ControlledVocRenderer());
	}
}