/**
 * Created at Aug 22, 2010, 3:55:23 PM
 */
package edu.cornell.dendro.corina.components.table;

import java.util.HashMap;

import org.tridas.schema.ControlledVoc;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer.Behavior;

/**
 * @author Daniel
 *
 */
public class ControlledVocDictionaryEditor extends AbstractComboBoxEditor{
	

	private final HashMap<String, ControlledVoc> map = new HashMap<String, ControlledVoc>();
	private final Behavior behavior;
	private final String dictString;
	
	public ControlledVocDictionaryEditor(String argDictionary){
		behavior = Behavior.DEFAULT;
		dictString = argDictionary;
	}

	public ControlledVocDictionaryEditor(String argDictionary, Behavior argBehavior){
		behavior = argBehavior;
		dictString = argDictionary;
	}
	
	protected String convertToString(ControlledVoc voc) {
		
		switch(behavior) {
		case DEFAULT: {
			if(voc.isSetNormal()) {
				if(voc.isSetNormalStd())
					return "<html>" + voc.getNormal() + " (<i>" + voc.getNormalStd() + "</i>)";
				
				return voc.getNormal();
			}
			return voc.getValue();
		}
		
		case NORMAL: {
			if(voc.isSetNormal()) {
				if(voc.isSetNormalStd())
					return "<html>" + voc.getNormal() + " (<i>" + voc.getNormalStd() + "</i>)";
				
				return voc.getNormal();
			}
			return "";
		}

		case NORMAL_ONLY:
			if(voc.isSetNormal())
				return voc.getNormal();
			return "";
			
		case VALUE:
			if(voc.isSetValue())
				return voc.getValue();	
			return "";
		}

		
		return voc.toString();
	}
	
	protected String[] getComboBoxOptions() {
		ControlledVoc[] vocs = Dictionary.getDictionaryAsArrayList(dictString).toArray(new ControlledVoc[0]);
		String[] names = new String[vocs.length];
		for(int i=0; i<vocs.length; i++){
			names[i] = convertToString(vocs[i]);
			map.put(names[i], vocs[i]);
		}
		return names;
	}

	protected Object getValueFromString(String argString) {
		return map.get(argString);
	}
}