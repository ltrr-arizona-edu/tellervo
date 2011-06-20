package edu.cornell.dendro.corina.sample;

/**
 * This interface implements support for adding/setting resource properties
 * 
 * @author Lucas Madar
 */

public interface ResourcePropertySupport {
	/**
	 * Set a server query property for loading
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void setLoadProperty(String propertyName, Object value);

	/**
	 * Set a server query property for saving
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void setSaveProperty(String propertyName, Object value);
}
