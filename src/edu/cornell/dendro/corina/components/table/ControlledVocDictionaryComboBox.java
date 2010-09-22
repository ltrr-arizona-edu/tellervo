/**
 * Created at Aug 22, 2010, 3:55:23 PM
 */
package edu.cornell.dendro.corina.components.table;

import java.util.ArrayList;

import org.tridas.schema.ControlledVoc;

import com.dmurph.mvc.IEventListener;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.dictionary.DictionaryRegisteredEvent;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer.Behavior;

/**
 * Basically a helper class, so you don't have to write all this mess every time.
 * @author Daniel
 *
 */
public class ControlledVocDictionaryComboBox extends DynamicJComboBox<ControlledVoc> implements IEventListener{
	private static final long serialVersionUID = 1L;
	
	public String dictionaryName;
	public ArrayList<ControlledVoc> data = null;
	private final Object dataLock = new Object();
	
	public ControlledVocDictionaryComboBox(String argDictionary){
		this(argDictionary, Behavior.DEFAULT);
	}

	public ControlledVocDictionaryComboBox(String argDictionary,final Behavior argBehavior){
		super(null, new IDynamicJComboBoxInterpreter<ControlledVoc>() {
			public String getStringValue(ControlledVoc voc) {
				switch(argBehavior) {
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
		});
		setKeySelectionManager(new HTMLKeySelectionManager());
		dictionaryName = argDictionary;
		MVC.addEventListener(Dictionary.DICTIONARY_REGISTERED, this);

		synchronized (dataLock) {
			data = Dictionary.getMutableDictionary(argDictionary);
			refreshData();
		}
	}
	
	/**
	 * @see edu.cornell.dendro.corina.components.table.DynamicJComboBox#getData()
	 */
	@Override
	public ArrayList<ControlledVoc> getData() {
		return data;
	}

	/**
	 * @see com.dmurph.mvc.IEventListener#eventReceived(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void eventReceived(MVCEvent argEvent) {
		DictionaryRegisteredEvent event = (DictionaryRegisteredEvent) argEvent;
		if(event.getValue().equals(dictionaryName)){
			synchronized (dataLock) {
				data = (ArrayList<ControlledVoc>) event.dictionary;
				refreshData();
			}
		}
	}
}