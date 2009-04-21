package edu.cornell.dendro.corina.tridas;

import java.util.HashMap;
import java.util.Map;

/**
 * An extensible way to convert from any tridas units into corina-native units (1/100mm)
 * 
 * @author lucasm
 *
 */

public enum TridasUnits {
	MICROMETER("micrometres", -1),
	HUNDREDTHMILLIMETER("1/100 millimetres", 0),	
	TENTHMILLIMETER("1/10 millimetres", 1),
	MILLIMETER("millimetres", 2),
	CENTIMETER("centimetres", 3),
	METER("metres", 4);
	
	/**
	 * Convert the values of this incoming number into Corina format
	 * 
	 * @param value
	 * @return
	 */
	public float processValue(Number value) {
		float multiplicand = (float) Math.pow(10.0, exponent);
		return value.floatValue() * multiplicand;
	}

	@Override
	public String toString() {
		return officialRepresentation;
	}
	
	/** The XSD spec name for this type of measurement */
	private final String officialRepresentation;
	/** The exponent to convert from Corina form to this (value * 10^exp) */
	private final int exponent;
	
	/**
	 * @param officialRepresentation
	 * @param exponent
	 */
	private TridasUnits(String officialRepresentation, int exponent) {
		this.officialRepresentation = officialRepresentation;
		this.exponent = exponent;
	}
	
	/**
	 * Get an enum representation of this tridas1.x official tag
	 * @param representation
	 */
	public static TridasUnits getUnitsForOfficialRepresentation(String representation) {
		return officialToEnumMap.get(representation);
	}
	
	private static Map<String, TridasUnits> officialToEnumMap = new HashMap<String, TridasUnits>(8);
	static {
		for(TridasUnits unit : TridasUnits.values())
			officialToEnumMap.put(unit.officialRepresentation, unit);
	}
}
