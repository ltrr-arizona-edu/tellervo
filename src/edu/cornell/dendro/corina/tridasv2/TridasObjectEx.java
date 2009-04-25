package edu.cornell.dendro.corina.tridasv2;

import javax.xml.bind.annotation.XmlTransient;

import org.tridas.schema.TridasObject;

public class TridasObjectEx extends TridasObject {
	/** How many child series do I have (vmeasurements) */
	@XmlTransient
	private int mySeriesCount;
	/** How many child series do my children and I have? */
	@XmlTransient
	private int childSeriesCount;
}
