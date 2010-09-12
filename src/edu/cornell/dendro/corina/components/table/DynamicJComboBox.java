/**
 * Created at Aug 20, 2010, 2:58:08 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;

import com.dmurph.mvc.model.MVCArrayList;

/**
 * This class is for use inside of JTables, where data might have to be loaded or modified
 * dynamically
 * 
 * @author Daniel Murphy
 */
public class DynamicJComboBox extends JComboBox implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private DynamicJComboBoxModel model;
	private final MVCArrayList<?> data;
	private final IDynamicJComboBoxInterpretter interpretter;
	
	public DynamicJComboBox(MVCArrayList<String> argData) {
		this(argData, new IDynamicJComboBoxInterpretter() {
			@Override
			public String getStringValue(Object argComponent) {
				return (String) argComponent;
			}
		});
	}
	
	public DynamicJComboBox(MVCArrayList<?> argData, IDynamicJComboBoxInterpretter argInterpretter) {
		data = argData;
		interpretter = argInterpretter;
		model = new DynamicJComboBoxModel();
		setModel(model);
		
		argData.addPropertyChangeListener(this);
		for (Object o : data) {
			String s = interpretter.getStringValue(o);
			if (s == null) {
				continue;
			}
			model.addElement(s);
		}
	}
	
	public synchronized void addToFront(Object argNewObj) {
		String s = interpretter.getStringValue(argNewObj);
		if (s == null) {
			return;
		}
		model.insertElementAt(s, 0);
	}
	
	public synchronized void change(Object argOld, Object argNew) {
		String s1 = interpretter.getStringValue(argOld);
		String s2 = interpretter.getStringValue(argNew);
		if (s2 == null || s1 == null) {
			return;
		}
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			String s = model.getElementAt(i).toString();
			if (s.equals(s1)) {
				model.setElementAt(s2, i);
				return;
			}
		}
	}
	
	public synchronized void remove(Object argVal) {
		String s = interpretter.getStringValue(argVal);
		if (s == null) {
			return;
		}
		model.removeElement(s);
	}
	
	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent argEvt) {
		String prop = argEvt.getPropertyName();
		if (prop.equals(MVCArrayList.ADDED)) {
			addToFront(argEvt.getNewValue());
		}
		else if (prop.equals(MVCArrayList.CHANGED)) {
			change(argEvt.getOldValue(), argEvt.getNewValue());
		}
		else if (prop.equals(MVCArrayList.REMOVED)) {
			remove(argEvt.getOldValue());
		}
	}
}
