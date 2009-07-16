package org.tridas.interfaces;

/**
 * Interface to be inherited by classes extending ControlledVoc
 * that support a normalTridas tag.
 * 
 * @author Lucas Madar
 *
 * @param <T>
 */

public interface NormalTridasVoc<T extends Enum<T>> {
	/** Get the NormalTridas enum */
	public T getNormalTridas();
	/** Set the NormalTridas enum */
	public void setNormalTridas(T value);
	/** Check to see if this voc has a normal tridas enum set */
	public boolean isSetNormalTridas();
}
