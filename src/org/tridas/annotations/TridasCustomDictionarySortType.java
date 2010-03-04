package org.tridas.annotations;

public enum TridasCustomDictionarySortType {
	/** Sort by lastname, firstname */
	LASTNAME_FIRSTNAME,
	/** Sort by NORMAL, if it exists, then VALUE */
	NORMAL_OR_VALUE,
	/** Don't sort (default) */
	NONE
}
