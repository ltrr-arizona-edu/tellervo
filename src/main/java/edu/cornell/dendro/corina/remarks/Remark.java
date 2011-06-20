package edu.cornell.dendro.corina.remarks;

import javax.swing.Icon;

import org.tridas.schema.TridasValue;

public interface Remark {
	/** Get a display name for this remark */
	public String getDisplayName();
	
	/** Apply a remark to the given value */
	public void applyRemark(TridasValue value);

	/** Remove a remark from the given tridas value */
	public void removeRemark(TridasValue value);
	
	/** Disable a remark on the given tridas value */
	public void overrideRemark(TridasValue value);
	
	/**
	 * @param value
	 * @return true if the remark is set on this value
	 */
	public boolean isRemarkSet(TridasValue value);
	
	/**
	 * @param value
	 * @return true if the remark was inherited
	 */
	public boolean isRemarkInherited(TridasValue value);
	
	/**
	 * @return an associated Icon, or null
	 */
	public Icon getIcon();
}
