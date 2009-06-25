package edu.cornell.dendro.corina.tridasv2;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.math.NumberUtils;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;

public class TridasObjectEx extends TridasObject {
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
	 * @return
	 */
	public boolean isTopLevelObject() {
		return (parentObject == null);
	}
	
	/**
	 * Determines if this has any children
	 * @return
	 */
	public boolean hasChildren() {
		return isSetObjects();
	}
	
	/**
	 * Does this object have a lab code?
	 * @return
	 */
	public boolean hasLabCode() {
		return (labCode != null);
	}
	
	/**
	 * Get the number of series that this particular object has
	 * @return
	 */
	@XmlTransient
	public Integer getSeriesCount() {
		return this.mySeriesCount;
	}
	
	/**
	 * Get the number of objects that this series and its children have
	 * @return
	 */
	@XmlTransient
	public Integer getChildSeriesCount() {
		return this.childSeriesCount;
	}
	
	/**
	 * Get the lab code, or (n/a) if there is no lab code
	 * @return
	 */
	@XmlTransient
	public String getLabCode() {
		return (labCode != null) ? labCode : "(n/a)";
	}

	/**
	 * Get a text representation of this site
	 * @return
	 */
	public String toTitleString() {
		if(labCode == null || labCode.equals(title))
			return title;
		
		return "[" + labCode + "] " + title;
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
}
