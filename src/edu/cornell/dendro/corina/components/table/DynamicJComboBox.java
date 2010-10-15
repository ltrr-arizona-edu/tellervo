/**
 * Created at Aug 20, 2010, 2:58:08 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
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
	
	
	private DynamicJComboBoxModel<E> model;
	private final MVCArrayList<E> data;
	private final IDynamicJComboBoxFilter<E> filter;
	private final Object lock = new Object();
	private DynamicJComboBoxStyle style;
	private Comparator<E> comparator = null;
	
	
	/**
	 * Constracts a dynamic combo box with the given data and
	 * default style of {@link DynamicJComboBoxStyle#ALPHA_SORT}.
	 * @param argData
	 */
	public DynamicJComboBox(MVCArrayList<E> argData, Comparator<E> argComparator) {
		this(argData, new IDynamicJComboBoxFilter<E>() {
			public boolean showItem(E argComponent) {
				return true;
			};
		}, DynamicJComboBoxStyle.ALPHA_SORT, argComparator);
	}
	
	public DynamicJComboBox(MVCArrayList<E> argData, DynamicJComboBoxStyle argStyle) {
		this(argData, new IDynamicJComboBoxFilter<E>() {
			public boolean showItem(E argComponent) {
				return true;
			};
		}, argStyle, null);
	}
	
	/**
	 * Constructs a dynamic combo box with the given data and data interpreter.
	 * @param argData
	 * @param argFilter
	 */
	public DynamicJComboBox(MVCArrayList<E> argData, IDynamicJComboBoxFilter<E> argFilter, Comparator<E> argComparator) {
		this(argData, argFilter, DynamicJComboBoxStyle.ALPHA_SORT, null);
	}
	
	/**
	 * Constructs with the given data, interpreter, and style.
	 * @param argData
	 * @param argFilter
	 * @param argStyle
	 */
	public DynamicJComboBox(MVCArrayList<E> argData, IDynamicJComboBoxFilter<E> argFilter, DynamicJComboBoxStyle argStyle, Comparator<E> argComparator) {
		data = argData;
		style = argStyle;
		filter = argFilter;
		comparator = argComparator;
		model = new DynamicJComboBoxModel<E>();
		setModel(model);
		
		if(data != null){
			argData.addPropertyChangeListener(this);
			for (E o : data) {
				if(filter.showItem(o)){
					model.addElement(o);
				}
			}
			// start with allowing the comparator to be null, in case they intend to set it later. and call refreshData()
			if(style == DynamicJComboBoxStyle.ALPHA_SORT && comparator != null){
				model.sort(comparator);
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
     * Sets the comparator used for the {@link DynamicJComboBoxStyle#ALPHA_SORT} style.
     * @param argComparator
     */
    public void setComparator(Comparator<E> argComparator) {
		this.comparator = argComparator;
	}

	public Comparator<E> getComparator() {
		return comparator;
	}

	/**
     * @see javax.swing.JComboBox#processKeyEvent(java.awt.event.KeyEvent)
     */
    @Override
    public void processKeyEvent(KeyEvent argE) {
    	if(argE.getKeyChar() == KeyEvent.VK_BACK_SPACE || argE.getKeyChar() == KeyEvent.VK_DELETE){
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
    		if(comparator == null){
				throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
    		}
    		model.sort(comparator);
    	}
    }
    
    public void refreshData(){
    	synchronized (lock) {
    		// remove all elements
    		model.removeAllElements();
        	for(E e: getData()){
        		if(filter.showItem(e)){
        			model.addElement(e);
        		}
        	}
        	if(style == DynamicJComboBoxStyle.ALPHA_SORT){
        		if(comparator == null){
					throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
        		}
            	model.sort(comparator);
        	}
		}
    }
    
	private void add(E argNewObj) {
		boolean b = filter.showItem(argNewObj);
		if (b == false) {
			return;
		}
		synchronized (lock) {
			switch(style){
				case ALPHA_SORT:{
					if(comparator == null){
						throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
					}
					boolean inserted = false;
					for(int i=0; i<model.getSize(); i++){
						E e = model.getElementAt(i);
						if(comparator.compare(e, argNewObj) > 0){
							model.insertElementAt(argNewObj, i);
							inserted = true;
							break;
						}
					}
					if(!inserted){
						model.addElement(argNewObj);
					}
					break;
				}
				case ADD_NEW_TO_BEGINNING:{
					model.insertElementAt(argNewObj, 0);
					break;
				}
				case ADD_NEW_TO_END:{
					model.addElement(argNewObj);
				}
			}
			
		}
	}
	
	private void addAll(Collection<E> argNewObjects) {
		LinkedList<E> filtered = new LinkedList<E>();
		
		Iterator<E> it = argNewObjects.iterator();
		while(it.hasNext()){
			E e = it.next();
			if(filter.showItem(e)){
				filtered.add(e);
			}
		}
		if(filtered.size() == 0){
			return;
		}
		synchronized (lock) {
			switch(style){
				case ALPHA_SORT:{
					if(comparator == null){
						throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
					}
					model.addElements(filtered);
					break;
				}
				case ADD_NEW_TO_BEGINNING:{
					model.addElements(0, filtered);
					break;
				}
				case ADD_NEW_TO_END:{
					model.addElements(filtered);
				}
			}
			
		}
	}
	
	private void change(E argOld, E argNew) {
		boolean so = filter.showItem(argOld);
		boolean sn = filter.showItem(argNew);
		if(!sn){
			remove(argOld);
			return;
		}
		if(!so){
			if(sn){
				add(argNew);
				return;
			}else{
				return;
			}
		}
		synchronized (lock) {
			int size = model.getSize();
			for (int i = 0; i < size; i++) {
				E e = model.getElementAt(i);
				if (e == argOld) {
					model.setElementAt(argNew, i);
					return;
				}
			}
			if(style == DynamicJComboBoxStyle.ALPHA_SORT){
				if(comparator == null){
					throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
				}
				model.sort(comparator);
			}
		}
	}
	
	private void remove(E argVal) {
		boolean is = filter.showItem(argVal);
		if (!is) {
			return;
		}
		synchronized (lock) {
			for(int i=0; i<model.getSize();i ++){
				E e = model.getElementAt(i);
				if(e == argVal){
					model.removeElementAt(i);
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
		else if(prop.equals(MVCArrayList.ADDED_ALL)){
			addAll((Collection<E>) argEvt.getNewValue());
		}
		else if (prop.equals(MVCArrayList.CHANGED)) {
			change((E)argEvt.getOldValue(),(E) argEvt.getNewValue());
		}
		else if (prop.equals(MVCArrayList.REMOVED)) {
			remove((E)argEvt.getOldValue());
		}
		else if(prop.equals(MVCArrayList.REMOVED_ALL)){
			model.removeAllElements();
		}
	}
}
