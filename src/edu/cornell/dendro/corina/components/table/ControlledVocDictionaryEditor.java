/**
 * Created at Aug 22, 2010, 3:55:23 PM
 */
package edu.cornell.dendro.corina.components.table;

import java.io.Serializable;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComboBox.KeySelectionManager;

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
	
	/**
	 * @see edu.cornell.dendro.corina.components.table.AbstractComboBoxEditor#populateComboBox(javax.swing.JComboBox, java.lang.String[])
	 */
	@Override
	protected void populateComboBox(JComboBox argComboBox, String[] argItems) {
		super.populateComboBox(argComboBox, argItems);

		if(behavior != Behavior.NORMAL_ONLY && behavior != Behavior.VALUE){
			argComboBox.setKeySelectionManager(new KeySelectionManager() {
	
				@Override
				public int selectionForKey(char aKey, ComboBoxModel aModel) {
					int i,c;
		            int currentSelection = -1;
		            Object selectedItem = aModel.getSelectedItem();
		            String v;
		            String pattern;
		            System.out.println("calling!");
	
		            if ( selectedItem != null ) {
		                for ( i=0,c=aModel.getSize();i<c;i++ ) {
		                    if ( selectedItem == aModel.getElementAt(i) ) {
		                        currentSelection  =  i;
		                        break;
		                    }
		                }
		            }
	
		            pattern = ("" + aKey).toLowerCase();
		            aKey = pattern.charAt(0);
	
		            for ( i = ++currentSelection, c = aModel.getSize() ; i < c ; i++ ) {
		                Object elem = aModel.getElementAt(i);
						if (elem != null && elem.toString() != null) {
						    v = elem.toString().toLowerCase();
						    v = v.substring(v.indexOf('>')+1);
						    if ( v.length() > 0 && v.charAt(0) == aKey )
							return i;
						}
		            }
	
		            for ( i = 0 ; i < currentSelection ; i ++ ) {
		                Object elem = aModel.getElementAt(i);
						if (elem != null && elem.toString() != null) {
						    v = elem.toString().toLowerCase();
						    v = v.substring(v.indexOf('>')+1);
						    if ( v.length() > 0 && v.charAt(0) == aKey )
							return i;
						}
		            }
		            return -1;
				}
			});
		}
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