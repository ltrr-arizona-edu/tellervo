package edu.cornell.dendro.corina.mvc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author daniel
 */
public abstract class AbstractModel implements IDirtyable{
	protected final PropertyChangeSupport propertyChangeSupport;

	private boolean dirty = false;
	
    public AbstractModel(){
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
	@Override
	public boolean isDirty(){
		return dirty;
	}
	
	@Override
	public void updateDirty(boolean argIsDirty){
		dirty = dirty || argIsDirty;
	}
	
	@Override
	public boolean setDirty(boolean argDirty){
		boolean oldDirty = dirty;
		if(dirty == argDirty){
			return dirty;
		}
		
		if(!dirty){
			save();
		}
		dirty = argDirty;
		return oldDirty;
	}
	
	
	@Override
	public boolean clean(){
		boolean oldDirty = dirty;
		revert();
		dirty = false;
		return oldDirty;
	}
	
	/**
	 * Revert the model to the clean objects.
	 */
	protected abstract void revert();
	
	/**
	 * Save the working objects to the clean objects
	 */
	protected abstract void save();
/*	
	// why do i need these here? otherwise it throws a weird error
	@Override
	public abstract void cloneFrom(ICloneable argOther);
	
	@Override
	public abstract ICloneable clone();*/

}
