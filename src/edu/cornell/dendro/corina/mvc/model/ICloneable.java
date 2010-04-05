/**
 * 2:37:58 PM, Apr 5, 2010
 */
package edu.cornell.dendro.corina.mvc.model;

/**
 *
 * @author daniel
 */
public interface ICloneable {

	/**
	 * Clones from another object
	 * @param argOther
	 */
	public void cloneFrom(ICloneable argOther);
	
	/**
	 * Full clone of this object.  Not a shallow copy.
	 * @return
	 */
	public ICloneable clone();
}
