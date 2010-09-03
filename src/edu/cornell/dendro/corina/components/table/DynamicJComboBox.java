/**
 * Created at Aug 20, 2010, 2:58:08 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.dmurph.mvc.model.MVCArrayList;

/**
 * This class is for use inside of JTables, where data might have to be loaded or modified dynamically
 * @author Daniel
 *
 */
public class DynamicJComboBox extends JComboBox implements PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	
	private final DefaultComboBoxModel model;
	private final MVCArrayList<?> data;
	private final IDynamicJComboBoxInterpretter interpretter;
	
	public DynamicJComboBox(MVCArrayList<String> argData){
		this(argData, new IDynamicJComboBoxInterpretter() {
			@Override
			public String getStringValue(Object argComponent) {
				return (String) argComponent;
			}
		});
	}
	
	public DynamicJComboBox(MVCArrayList<?> argData, IDynamicJComboBoxInterpretter argInterpretter){
		data = argData;
		interpretter = argInterpretter;
		model = new DefaultComboBoxModel();
		setModel(model);
		
		argData.addPropertyChangeListener(this);
		resetData();
	}
	
	public void resetData(){
		// remove and re-add all the elements
		model.removeAllElements();
		for(Object o : data){
			String s = interpretter.getStringValue(o);
			if(s == null){
				continue;
			}
			model.addElement(s);
		}
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent argEvt) {
		String prop = argEvt.getPropertyName();
		if(prop.equals(MVCArrayList.SIZE) || prop.equals(MVCArrayList.CHANGED)){
			resetData();
		}
	}
}
