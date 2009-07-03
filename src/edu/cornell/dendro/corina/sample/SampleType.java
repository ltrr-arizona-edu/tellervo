package edu.cornell.dendro.corina.sample;

public enum SampleType {
	UNKNOWN(false), // being imported, perhaps?
	DIRECT(false),
	UNKNOWN_DERIVED(true),
	SUM(true),
	INDEX(true),
	CLEAN(true),
	REDATE(true),
	LEGACYCLEAN(true),
	CROSSDATE(true);
	
	private SampleType(boolean derived) {
		this.derived = derived;
	}
	
	private final boolean derived;
	
	public final boolean isDerived() {
		return derived;
	}
	
	public final boolean isUnknown() {
		return this == UNKNOWN || this == UNKNOWN_DERIVED;
	}
	
	public final String toString() {
		switch(this) {
		case UNKNOWN:
			return "Unknown";
		case UNKNOWN_DERIVED:
			return "UnknownDerived";
		case DIRECT:
			return "Raw";
		case SUM:
			return "Sum";
		case INDEX:
			return "Index";
		case CLEAN:
			return "Clean";
		case REDATE:
			return "Redate";
		case LEGACYCLEAN:
			return "Legacy";
		case CROSSDATE:
			return "Cross";
		default:
			return "";
		}
	}

	public static SampleType fromString(String name) {
		try {
			return valueOf(name.toUpperCase());
		} catch (IllegalArgumentException ie) {
			return UNKNOWN;
		}
	}
}
