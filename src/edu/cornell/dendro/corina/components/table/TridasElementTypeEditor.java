/**
 * Created at Aug 21, 2010, 10:21:17 PM
 */
package edu.cornell.dendro.corina.components.table;

import org.tridas.schema.ControlledVoc;

import edu.cornell.dendro.corina.dictionary.Dictionary;

/**
 * @author Daniel
 *
 */
public class TridasElementTypeEditor extends AbstractComboBoxEditor {
	private static final long serialVersionUID = 1L;
	
	protected String[] getComboBoxOptions() {
		ControlledVoc[] vocs = Dictionary.getDictionaryAsArrayList("elementTypeDictionary").toArray(new ControlledVoc[0]);
		String[] names = new String[vocs.length];
		for(int i=0; i<vocs.length; i++){
			names[i] = vocs[i].getNormal();
		}
		return names;
	}

	protected Object getValueFromString(String argString) {
		ControlledVoc[] vocs = Dictionary.getDictionaryAsArrayList("elementTypeDictionary").toArray(new ControlledVoc[0]);
		for(ControlledVoc voc : vocs){
			if(argString.equals(voc.getNormal())){
				return voc;
			}
		}
		System.err.println("Could not find element type with normal: "+argString);
		return null;
	}
}
