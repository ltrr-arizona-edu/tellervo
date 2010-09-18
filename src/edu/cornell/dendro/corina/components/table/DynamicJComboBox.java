/**
 * Created at Aug 20, 2010, 2:58:08 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Stack;

import javax.swing.JComboBox;

import com.dmurph.mvc.model.MVCArrayList;

/**
 * This class is for use inside of JTables, where data might have to be loaded or modified
 * dynamically
 * 
 * @author Daniel Murphy
 */
public class DynamicJComboBox<E> extends JComboBox implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private DynamicJComboBoxModel model;
	private final MVCArrayList<?> data;
	private final IDynamicJComboBoxInterpretter<E> interpretter;
	
	// for performance
	private final Stack<ObjectWrapper<E>> pool = new Stack<ObjectWrapper<E>>();
	
	public DynamicJComboBox(MVCArrayList<E> argData) {
		this(argData, new IDynamicJComboBoxInterpretter<E>() {
			@Override
			public String getStringValue(Object argComponent) {
				if(argComponent == null){
					return null;
				}
				return argComponent.toString();
			}
		});
	}
	
	public DynamicJComboBox(MVCArrayList<E> argData, IDynamicJComboBoxInterpretter<E> argInterpretter) {
		data = argData;
		interpretter = argInterpretter;
		model = new DynamicJComboBoxModel();
		setModel(model);
		
		argData.addPropertyChangeListener(this);
		for (Object o : data) {
			String s = interpretter.getStringValue((E)o);
			if (s == null) {
				continue;
			}
			ObjectWrapper<E> wrapper = getWrapper();
			wrapper.object = (E)o;
			wrapper.string = s;
			model.addElement(wrapper);
		}
	}
	
	public synchronized void addToFront(E argNewObj) {
		String s = interpretter.getStringValue(argNewObj);
		if (s == null) {
			return;
		}
		ObjectWrapper<E> wrapper = getWrapper();
		wrapper.object = argNewObj;
		wrapper.string = s;
		model.insertElementAt(wrapper, 0);
	}
	
	public synchronized void change(E argOld, E argNew) {
		String s1 = interpretter.getStringValue(argOld);
		String s2 = interpretter.getStringValue(argNew);
		if (s2 == null || s1 == null) {
			return;
		}
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			ObjectWrapper<E> objWrapper = (ObjectWrapper<E>) model.getElementAt(i);
			if (objWrapper.string.equals(s1)) {
				objWrapper.string = s2;
				objWrapper.object = argNew;
				model.setElementAt(objWrapper, i);
				return;
			}
		}
	}
	
	/**
	 * @see javax.swing.JComboBox#getSelectedItem()
	 */
	@Override
	public Object getSelectedItem() {
		ObjectWrapper<E> objWrapper = (ObjectWrapper<E>)super.getSelectedItem();
		if(objWrapper == null){
			return null;
		}
		return objWrapper.object;
	}
	
	public synchronized void remove(E argVal) {
		String s = interpretter.getStringValue(argVal);
		if (s == null) {
			return;
		}
		for(int i=0; i<model.getSize();i ++){
			ObjectWrapper<E> objWrapper = (ObjectWrapper<E>) model.getElementAt(i);
			if(objWrapper.object.equals(argVal)){
				model.removeElementAt(i);
				recycleWrapper(objWrapper);
				return;
			}
		}
	}
	
	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent argEvt) {
		String prop = argEvt.getPropertyName();
		if (prop.equals(MVCArrayList.ADDED)) {
			addToFront((E)argEvt.getNewValue());
		}
		else if (prop.equals(MVCArrayList.CHANGED)) {
			change((E)argEvt.getOldValue(),(E) argEvt.getNewValue());
		}
		else if (prop.equals(MVCArrayList.REMOVED)) {
			remove((E)argEvt.getOldValue());
		}
	}
	
	private ObjectWrapper<E> getWrapper(){
		if(pool.isEmpty()){
			pool.push(new ObjectWrapper<E>());
			pool.push(new ObjectWrapper<E>());
			pool.push(new ObjectWrapper<E>());
			pool.push(new ObjectWrapper<E>());
		}
		return pool.pop();
	}
	
	private void recycleWrapper(ObjectWrapper<E> argWrapper){
		pool.push(argWrapper);
	}
	
	protected static class ObjectWrapper<E>{
		public E object;
		public String string;
		public String toString(){
			return string;
		}
	}
}
