/**
 * 2:20:29 PM, Apr 5, 2010
 */
package edu.cornell.dendro.corina.mvc.model;

/**
 * For use with models.
 * @author daniel
 */
public interface IDirtyable {
	
	/**
	 * Returns if the object is dirty
	 * @return
	 */
	public boolean isDirty();
	
	/**
	 * Called every time a value is set to update
	 * if a model is dirty yet.  After this is called
	 * with false, a model will be always dirty until
	 * {@link #clean()} or {@link #setDirty(boolean)}
	 * @param argIsDirty
	 */
	public void updateDirty(boolean argIsDirty);
	
	/**
	 * Sets if the model is dirty or not.  If called
	 * with false, the clean objects are cloned from the
	 * current working objects.  Saves changes to the model.
	 * @param argDirty
	 * @return the previous dirty value
	 */
	public boolean setDirty(boolean argDirty);
	
	/**
	 * Cleans the model from the stored clean objects.
	 * Equivilant of clicking cancel, reverts changes.
	 * @return if the model was dirty
	 */
	public boolean clean();
}
