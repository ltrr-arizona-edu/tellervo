package edu.cornell.dendro.corina.sample;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum SampleType {
	@XmlEnumValue("Unknown") UNKNOWN(false, "Unknown"), // being imported, perhaps?
	@XmlEnumValue("UnknownDerived") UNKNOWN_DERIVED(true, "UnknownDerived"),
	
	@XmlEnumValue("Raw") DIRECT(false, "Raw"),
	@XmlEnumValue("Sum") SUM(true, "Sum"),
	@XmlEnumValue("Index") INDEX(true, "Index"),
	@XmlEnumValue("Clean") CLEAN(true, "Clean"),
	@XmlEnumValue("Redate") REDATE(true, "Redate"),
	@XmlEnumValue("Crossdate") CROSSDATE(true, "Crossdate"),
	@XmlEnumValue("Truncate") TRUNCATE(true, "Truncate"),
	
	@XmlEnumValue("LegacyClean") LEGACYCLEAN(true, "LegacyClean");
	
	private final boolean derived;
	private final String value;
	
	private SampleType(boolean derived, String value) {
		this.derived = derived;
		this.value = value;
	}
	
	
	public final boolean isDerived() {
		return derived;
	}
	
	public final boolean isUnknown() {
		return (this == UNKNOWN || this == UNKNOWN_DERIVED);
	}
	
	public final String toString() {
		return value;
	}

	public static SampleType fromString(String name) {
		// some of them aren't clean... ugh.
		for(SampleType v : SampleType.values()) {
			if(v.value.equalsIgnoreCase(name))
				return v;
		}
			
		return UNKNOWN;
	}
}
