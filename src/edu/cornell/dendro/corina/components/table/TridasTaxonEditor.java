/**
 * Created at Aug 22, 2010, 3:51:24 PM
 */
package edu.cornell.dendro.corina.components.table;

import org.tridas.schema.ControlledVoc;

import edu.cornell.dendro.corina.dictionary.Dictionary;

/**
 * @author Daniel
 *
 */
public class TridasTaxonEditor extends AbstractComboBoxEditor{

	protected String[] getComboBoxOptions() {
		ControlledVoc[] vocs = Dictionary.getDictionaryAsArrayList("taxonDictionary").toArray(new ControlledVoc[0]);
		String[] names = new String[vocs.length];
		for(int i=0; i<vocs.length; i++){
			names[i] = vocs[i].getNormal();
		}
		return names;
	}

	protected Object getValueFromString(String argString) {
		ControlledVoc[] vocs = Dictionary.getDictionaryAsArrayList("taxonDictionary").toArray(new ControlledVoc[0]);
		for(ControlledVoc voc : vocs){
			if(argString.equals(voc.getNormal())){
				return voc;
			}
		}
		System.err.println("Could not find object type with normal: "+argString);
		return null;
	}
}