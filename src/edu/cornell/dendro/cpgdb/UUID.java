package edu.cornell.dendro.cpgdb;

/*
 * UUID class
 * 
 * This class is a wrapper for native code which interfaces with the C-based
 * UUID library that comes with linux. 
 * 
 * Essentially, generateUUID returns a 36-character String format UUID for use in databasing.
 */

public class UUID {
	public static native String generateUUID();
}

