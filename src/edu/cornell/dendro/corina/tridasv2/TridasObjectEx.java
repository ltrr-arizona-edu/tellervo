package edu.cornell.dendro.corina.tridasv2;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.math.NumberUtils;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;

public class TridasObjectEx extends TridasObject {
	private static final long serialVersionUID = 1L;
	
	/** How many child series do I have (vmeasurements) */
	@XmlTransient
	private Integer mySeriesCount;
	/** How many child series do my children and I have? */
	@XmlTransient
	private Integer childSeriesCount;
	/** Corina lab code */
	@XmlTransient
	private String labCode;
	/** Parent object */
	@XmlTransient
	private TridasObjectEx parentObject;
	
	/**
	 * Determines if this is a top level object (has no parent)
	 * @return true if this is a top level object
	 */
	public boolean isTopLevelObject() {
		return (parentObject == null);
	}
	
	/**
	 * Get the parent object
	 * @return The parent object, or null if it doesn't exist
	 */
	public TridasObjectEx getParent() {
		return parentObject;
	}
	
	/**
	 * Determines if this has any children
	 * @return true of there are any sub-object children
	 */
	public boolean hasChildren() {
		return isSetObjects();
	}
	
	/**
	 * Does this object have a lab code?
	 * @return true if getLabCode would return a valid labcode
	 */
	public boolean hasLabCode() {
		return (labCode != null);
	}
	
	/**
	 * Get the number of series that this particular object has
	 * @return the count of child series that are directly descended from this object
	 */
	@XmlTransient
	public Integer getSeriesCount() {
		return this.mySeriesCount;
	}
	
	/**
	 * Get the number of objects that this series and its children have
	 * @return the count of child series that are descended from this object and its children
	 */
	@XmlTransient
	public Integer getChildSeriesCount() {
		return this.childSeriesCount;
	}
	
	/**
	 * Get the lab code, or (n/a) if there is no lab code
	 * @return The "lab code" of this object
	 */
	@XmlTransient
	public String getLabCode() {
		return (labCode != null) ? labCode : "(n/a)";
	}
	
	/**
	 * Get the parent lab code, or (n/a) if there is no code
	 * @return The "lab code" of the parent of this object
	 */
	@XmlTransient
	public String getParentLabCode() {
		if (parentObject!=null)		
			return (parentObject.getLabCode() != null) ? parentObject.getLabCode() : null;
		else
			return null;
	}

	/**
	 * Get a text representation of this site
	 * @return A string in the form of '[CODE] title'
	 */
	public String toTitleString() {
		if(labCode == null || labCode.equals(title))
			return title;
		
		return "[" + labCode + "] " + title;
	}
	
	/**
	 * Get a text representation of this site
	 * @return A string in the form of '[PARENTCODE] [CODE] title'
	 */
	public String toTitleStringWithParentCode() {	
		// No parent
		if (getParentLabCode()==null) return toTitleString();
		
		String returnStr = null;
			
		returnStr = "[" + getParentLabCode() + "] ";
		
		if(labCode == null || labCode.equals(title))
			returnStr += title;
		else
			returnStr += "[" + labCode + "] " + title;
		 
		return returnStr;
	}
		
	
	/**
	 * This is called after unmarshalling is complete.
	 * We need to populate any of our values.
	 * 
	 * Note: It is guaranteed a that all child entities are populated.
	 * It is NOT guaranteed that parent entities are.
	 * 
	 * @param unmarshaller
	 * @param parent
	 */
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		// go through our generic fields and populate things...
		// we check if it exists first because otherwise getGenericField creates a list
		if (isSetGenericFields()) {
			for (TridasGenericField f : getGenericFields()) {
				String fieldName = f.getName();

				// handle lab code
				if ("corina.objectLabCode".equals(fieldName)) {
					this.labCode = f.getValue();
				}
				// series count
				else if ("corina.countOfChildSeries".equals(fieldName)) {
					this.mySeriesCount = this.childSeriesCount = 
						NumberUtils.toInt(f.getValue());
				}
			}
		}

		// now, build up any object tree counts and set parents
		// note we only have to do this at depth 1! Our children should have
		// already had this function called on them!
		if(isSetObjects()) {
			for(TridasObject o : getObjects()) {
				if(o instanceof TridasObjectEx) {
					((TridasObjectEx)o).parentObject = this;
					
					// make sure we start out with a zero count, if we don't have one already
					if(this.childSeriesCount == null)
						this.childSeriesCount = 0;
					
					// then, add any child series we might have
					if(((TridasObjectEx)o).childSeriesCount != null)
						this.childSeriesCount += ((TridasObjectEx)o).childSeriesCount;
				}
			}
		}
	}

	@Override
    public Object createCopy() {
        return new TridasObjectEx();
    }
}
