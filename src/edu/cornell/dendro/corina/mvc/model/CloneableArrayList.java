/**
 * 2:47:16 PM, Apr 5, 2010
 */
package edu.cornell.dendro.corina.mvc.model;

import java.util.ArrayList;

/**
 * Cloneable and dirtyable Array List.  Will clone all objects that
 * implement {@link ICloneable}, with shallow copys of everything else
 * @author daniel
 */
public class CloneableArrayList<E extends Object> extends ArrayList<E> implements ICloneable, IDirtyable {

	private boolean dirty = false;
	
	@Override
	public boolean add(E e) {
		dirty = true;
		return super.add(e);
	}
	
	@Override
	public void clear() {
		if(size() > 0){
			dirty = true;
		}
		super.clear();
	}
	
	@Override
	public E remove(int index) {
		dirty = true;
		return super.remove(index);
	}
	
	@Override
	public boolean remove(Object o) {
		dirty = true;
		return super.remove( o);
	}
	
	@Override
	public E set(int index, E element) {
		dirty = true;
		return super.set( index, element);
	}
	
	@Override
	public void cloneFrom( ICloneable argOther) {
		ArrayList<E> other = (ArrayList<E>) argOther;
		clear();
		for(E e : other){
			if(e instanceof ICloneable){
				add((E) ((ICloneable) e).clone());
			}else{
				add(e);
			}
		}
	}

	@Override
	public ICloneable clone(){
		CloneableArrayList<E> other = new CloneableArrayList<E>();
		other.cloneFrom(this);
		return other;
	}

	/**
	 * Just sets the dirty to false;
	 */
	@Override
	public boolean clean() {
		boolean oldDirty = dirty;
		dirty = false;
		return oldDirty;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Just sets the dirty variable;
	 */
	@Override
	public boolean setDirty( boolean argDirty) {
		boolean oldDirty = dirty;
		dirty = argDirty;
		return oldDirty;
	}

	@Override
	public void updateDirty( boolean argIsDirty) {
		dirty = argIsDirty || dirty;
	}
}
