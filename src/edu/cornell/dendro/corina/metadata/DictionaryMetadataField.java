/**
 * 
 */
package edu.cornell.dendro.corina.metadata;

import edu.cornell.dendro.corina.webdbi.ResourceEvent;
import edu.cornell.dendro.corina.webdbi.ResourceEventListener;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.*;

import java.util.List;
/**
 * @author Lucas Madar
 *
 */
public class DictionaryMetadataField extends MetadataField implements
		ResourceEventListener {
	
	public DictionaryMetadataField(String variableName, String dictionaryName, boolean editable) {
		super(variableName, editable);
		
		App.dictionary.addResourceEventListener(this);
		
		this.dictionaryName = dictionaryName;
		this.hasSetValues = true;
		
		myDictionary = App.dictionary.getDictionary(dictionaryName);
		loadTables();
	}

	private String dictionaryName;
	private volatile List myDictionary; 
	
	private void loadTables() {
	}
	
	public String[] getValues() {
		return null;
	}
	
	public int getListSize() {
		return myDictionary.size();
	}
	
	public String getListItemValue(int index) {
		return ((BasicDictionaryElement) myDictionary.get(index)).getInternalRepresentation();
	}

	public String getListItemDescription(int index) {
		return ((BasicDictionaryElement) myDictionary.get(index)).getValue() + " [dict: " + getListItemValue(index) + "]";
	}
	
	public void resourceChanged(ResourceEvent re) {
		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
			myDictionary = App.dictionary.getDictionary(dictionaryName);
			loadTables();
		}
	}
}
