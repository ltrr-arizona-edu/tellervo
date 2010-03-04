package org.tridas.annotations;

public enum TridasCustomDictionaryType {
	/** Dictionary uses a custom generic field (via identifierField) for identifiers */
	CORINA_GENERICID,
	/** Dictionary uses a Tridas ControlledVoc format */
	CORINA_CONTROLLEDVOC,
	/** Dictionary is just a plain text reference */
	CORINA_PLAIN,
	UNKNOWN
}
