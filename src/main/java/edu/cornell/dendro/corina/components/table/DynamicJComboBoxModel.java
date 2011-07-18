/**
 * Created at Sep 9, 2010, 4:35:08 PM
 */
package edu.cornell.dendro.corina.components.table;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.MutableComboBoxModel;


/**
 * Copied from {@link DefaultComboBoxModel}, and added more methods for adding elements to the list
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("unchecked")
public class DynamicJComboBoxModel<E> extends AbstractListModel implements MutableComboBoxModel, Serializable{
	private static final long serialVersionUID = 1L;
	
	private final Vector<E> objects;
    private E selectedObject;

    /**
     * Constructs an empty DefaultComboBoxModel.
     */
    public DynamicJComboBoxModel() {
        objects = new Vector<E>();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of objects.
     *
     * @param items  an array of Object objects
     */
	public DynamicJComboBoxModel(final E items[]) {
        objects = new Vector<E>();
        objects.ensureCapacity( items.length );

        int i,c;
        for ( i=0,c=items.length;i<c;i++ )
            objects.addElement(items[i]);

        if ( getSize() > 0 ) {
            selectedObject = getElementAt( 0 );
        }
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param v  a Vector object ...
     */
    public DynamicJComboBoxModel(Vector<E> v) {
        objects = v;

        if ( getSize() > 0 ) {
            selectedObject = getElementAt( 0 );
        }
    }

    // implements javax.swing.ComboBoxModel
    /**
     * Set the value of the selected item. The selected item may be null.
     * <p>
     * @param anObject The combo box value or null for no selection.
     */
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals( anObject )) ||
	    selectedObject == null && anObject != null) {
	    selectedObject = (E) anObject;
	    fireContentsChanged(this, -1, -1);
        }
    }

    // implements javax.swing.ComboBoxModel
    public E getSelectedItem() {
        return selectedObject;
    }

    // implements javax.swing.ListModel
    public int getSize() {
        return objects.size();
    }

    // implements javax.swing.ListModel
    public E getElementAt(int index) {
        if ( index >= 0 && index < objects.size() )
            return objects.elementAt(index);
        else
            return null;
    }

    /**
     * Returns the index-position of the specified object in the list.
     *
     * @param anObject  
     * @return an int representing the index position, where 0 is 
     *         the first position
     */
    public int getIndexOf(Object anObject) {
        return objects.indexOf(anObject);
    }

    // implements javax.swing.MutableComboBoxModel
    public void addElement(Object anObject) {
        objects.addElement((E)anObject);
        fireIntervalAdded(this,objects.size()-1, objects.size()-1);
        if ( objects.size() == 1 && selectedObject == null && anObject != null ) {
            setSelectedItem( anObject );
        }
    }
    
    public void addElements(int argIndex, Collection<E> argElements){
    	if(argElements == null){
    		return;
    	}
    	
    	objects.addAll(argIndex, argElements);
        fireIntervalAdded(this, argIndex, argIndex + argElements.size() - 1);  
        
        if ( objects.size() == 1 && selectedObject == null) {
        	setSelectedItem(objects.get(0));
        }
    }
    
    public void addElements(Collection<E> argElements){
    	if(argElements == null){
    		return;
    	}
    	
    	int oldSize = argElements.size();
    	objects.addAll(argElements);
        fireIntervalAdded(this,oldSize, objects.size()-1);  
        
        if ( objects.size() == 1 && selectedObject == null) {
        	setSelectedItem(objects.get(0));
        }
    }

    // implements javax.swing.MutableComboBoxModel
    public void insertElementAt(Object anObject,int index) {
        objects.insertElementAt((E)anObject,index);
        fireIntervalAdded(this, index, index);
    }
    
    public void setElementAt(E anObject, int index){
    	objects.setElementAt(anObject, index);
    	fireContentsChanged(this, index, index);
    }
    
    // implements javax.swing.MutableComboBoxModel
    public void removeElementAt(int index) {
        if ( getElementAt( index ) == selectedObject ) {
            if ( index == 0 ) {
                setSelectedItem( getSize() == 1 ? null : getElementAt( index + 1 ) );
            }
            else {
                setSelectedItem( getElementAt( index - 1 ) );
            }
        }

        objects.removeElementAt(index);

        fireIntervalRemoved(this, index, index);
    }
    
    public void sort(Comparator<E> argComparator){
    	Collections.sort(objects, argComparator);
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if ( index != -1 ) {
            removeElementAt(index);
        }
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
	    if ( objects.size() > 0 ) {
	            int firstIndex = 0;
	            int lastIndex = objects.size() - 1;
	            objects.removeAllElements();
		    selectedObject = null;
	            fireIntervalRemoved(this, firstIndex, lastIndex);
	        } else {
		    selectedObject = null;
		}
    }
}
