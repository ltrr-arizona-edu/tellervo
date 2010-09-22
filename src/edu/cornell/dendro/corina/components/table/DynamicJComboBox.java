/**
 * Created at Aug 20, 2010, 2:58:08 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
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
	
	public static enum DynamicJComboBoxStyle{
		/**
		 * List is sorted alphabetically.
		 */
		ALPHA_SORT,
		/**
		 * New objects are added to the end.
		 */
		ADD_NEW_TO_END,
		/**
		 * New object are added to the front.
		 */
		ADD_NEW_TO_BEGINNING
	}
	
	
	private DynamicJComboBoxModel model;
	private final MVCArrayList<E> data;
	private final IDynamicJComboBoxInterpreter<E> interpretter;
	private final Object lock = new Object();
	private DynamicJComboBoxStyle style;
	
	
	// for performance
	private final Stack<ObjectWrapper<E>> pool = new Stack<ObjectWrapper<E>>();
	
	/**
	 * Constracts a dynamic combo box with the given data and
	 * default style of {@link DynamicJComboBoxStyle#ALPHA_SORT}.
	 * @param argData
	 */
	public DynamicJComboBox(MVCArrayList<E> argData) {
		this(argData, new IDynamicJComboBoxInterpreter<E>() {
			@Override
			public String getStringValue(Object argComponent) {
				if(argComponent == null){
					return null;
				}
				return argComponent.toString();
			}
		}, DynamicJComboBoxStyle.ALPHA_SORT);
	}
	
	public DynamicJComboBox(MVCArrayList<E> argData, DynamicJComboBoxStyle argStyle) {
		this(argData, new IDynamicJComboBoxInterpreter<E>() {
			@Override
			public String getStringValue(Object argComponent) {
				if(argComponent == null){
					return null;
				}
				return argComponent.toString();
			}
		}, argStyle);
	}
	
	/**
	 * Constructs a dynamic combo box with the given data and data interpreter.
	 * @param argData
	 * @param argInterpretter
	 */
	public DynamicJComboBox(MVCArrayList<E> argData, IDynamicJComboBoxInterpreter<E> argInterpretter) {
		this(argData, argInterpretter, DynamicJComboBoxStyle.ALPHA_SORT);
	}
	
	/**
	 * Constructs with the given data, interpreter, and style.
	 * @param argData
	 * @param argInterpretter
	 * @param argStyle
	 */
	public DynamicJComboBox(MVCArrayList<E> argData, IDynamicJComboBoxInterpreter<E> argInterpretter, DynamicJComboBoxStyle argStyle) {
		data = argData;
		style = argStyle;
		interpretter = argInterpretter;
		model = new DynamicJComboBoxModel();
		setModel(model);
		
		if(data != null){
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
	}
    
    /**
     * Gets the rendering style of this combo box.  Default style is
     * {@link DynamicJComboBoxStyle#ALPHA_SORT}. 
     * @return
     */
    public DynamicJComboBoxStyle getStyle(){
    	return style;
    }
    
    /**
     * Gets the data list.  This is used to access
     * data with {@link #refreshData()}, so override
     * if you want to customize what the data is (sending
     * null to the contructor is a good idea in that case)
     * @return
     */
    public ArrayList<E> getData(){
    	return data;
    }
    
    /**
     * @see javax.swing.JComboBox#processKeyEvent(java.awt.event.KeyEvent)
     */
    @Override
    public void processKeyEvent(KeyEvent argE) {
    	if(argE.getKeyChar() == KeyEvent.VK_BACK_SPACE){
    		System.out.println("backspace!");
    		setSelectedItem(null);
    		super.hidePopup();
    	}else{
    		super.processKeyEvent(argE);
    	}
    }
    
    /**
     * Sets the style of this combo box
     * @param argStyle
     */
    public void setStyle(DynamicJComboBoxStyle argStyle){
    	style = argStyle;
    	if(style == DynamicJComboBoxStyle.ALPHA_SORT){
    		model.sort();
    	}
    }
    
    public void refreshData(){
    	synchronized (lock) {
    		// remove all elements
    		model.removeAllElements();
        	for(E e: getData()){
        		ObjectWrapper<E> wrapper = getWrapper();
        		wrapper.object = e;
        		wrapper.string = interpretter.getStringValue(e);
        		model.addElement(wrapper);
        	}
        	if(style == DynamicJComboBoxStyle.ALPHA_SORT){
            	model.sort();
        	}
		}
    }
    
	private void add(E argNewObj) {
		String s = interpretter.getStringValue(argNewObj);
		if (s == null) {
			return;
		}
		synchronized (lock) {
			switch(style){
				case ALPHA_SORT:{
					ObjectWrapper<E> wrapper = getWrapper();
					wrapper.object = argNewObj;
					wrapper.string = s;
					boolean inserted = false;
					for(int i=0; i<model.getSize(); i++){
						ObjectWrapper<E> wrapper2 = (ObjectWrapper<E>) model.getElementAt(i);
						if(wrapper2.string.compareTo(s) > 0){
							model.insertElementAt(wrapper, i);
							inserted = true;
							break;
						}
					}
					if(!inserted){
						model.addElement(wrapper);
					}
					break;
				}
				case ADD_NEW_TO_BEGINNING:{
					ObjectWrapper<E> wrapper = getWrapper();
					wrapper.object = argNewObj;
					wrapper.string = s;
					model.insertElementAt(wrapper, 0);
					break;
				}
				case ADD_NEW_TO_END:{
					ObjectWrapper<E> wrapper = getWrapper();
					wrapper.object = argNewObj;
					wrapper.string = s;
					model.addElement(wrapper);
				}
			}
			
		}
	}
	
	private void change(E argOld, E argNew) {
		String s1 = interpretter.getStringValue(argOld);
		String s2 = interpretter.getStringValue(argNew);
		if (s2 == null || s1 == null) {
			return;
		}
		synchronized (lock) {
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
	}
	
	/**
	 * @see javax.swing.JComboBox#getSelectedItem()
	 */
	@Override
	public Object getSelectedItem() {
		synchronized (lock) {
			ObjectWrapper<E> objWrapper = (ObjectWrapper<E>)super.getSelectedItem();
			if(objWrapper == null){
				return null;
			}
			return objWrapper.object;
		}
	}
	
	private void remove(E argVal) {
		String s = interpretter.getStringValue(argVal);
		if (s == null) {
			return;
		}
		synchronized (lock) {
			for(int i=0; i<model.getSize();i ++){
				ObjectWrapper<E> objWrapper = (ObjectWrapper<E>) model.getElementAt(i);
				if(objWrapper.object.equals(argVal)){
					model.removeElementAt(i);
					recycleWrapper(objWrapper);
					return;
				}
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
			add((E)argEvt.getNewValue());
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
	
	protected static class ObjectWrapper<E> implements Comparable<ObjectWrapper>{
		public E object;
		public String string;
		public String toString(){
			return string;
		}
		
		/**
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ObjectWrapper argO) {
			return (this.string.compareTo(argO.string));
		}
	}
}
