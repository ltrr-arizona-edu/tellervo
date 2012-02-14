/**
 * Created at Aug 22, 2010, 3:55:23 PM
 */
package edu.cornell.dendro.corina.components.table;

import java.util.Comparator;

import org.tridas.schema.ControlledVoc;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer;

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
			 * @see edu.cornell.dendro.corina.components.table.DynamicKeySelectionManager#convertToString(java.lang.Object)
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