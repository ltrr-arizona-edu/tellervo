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
	
	public static String createUUID() {
		if(!loaded)
			return null;

		return generateUUID();
        }

	private static native String generateUUID();
	private static boolean loaded = false;

	static {
		try {
			System.loadLibrary("cpgdb_uuid");
			System.out.println("Successfully loaded native UUID driver.");
			loaded = true;
		} catch (UnsatisfiedLinkError e) {
			System.out.println("Unable to load native UUID driver.");
			loaded = false;
		}
	}
}

