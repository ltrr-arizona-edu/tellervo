/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.support;

/**
 * @author Lucas Madar
 *
 */
public class VersionUtil {
	private VersionUtil() {
		// don't instantiate!
	}

	/**
	 * Implement the following algorithm:
	 * <p>
	 * [null] => 1<br>
	 * bob => bob2<br>
	 * pwb3 => pwb4<br>
	 * 5 => 6<br>
	 * 
	 * @param origVersion
	 * @return a string, guaranteed to not be null 
	 */
	public static String nextVersion(String origVersion) {
		
		// no version? simple!
		if(origVersion == null || origVersion.length() == 0) {
			return "1";
		}

		// it doesn't end with a number
		if(!Character.isDigit(origVersion.charAt(origVersion.length() - 1))) {
			//System.out.println("VERSION: " + origVersion + " => " + origVersion + "2");
			return origVersion + "2";
		}
		
		// ok, take the suffix and increment it by one
		int firstDigitIdx = origVersion.length() - 1;
		while(firstDigitIdx > 0 && Character.isDigit(origVersion.charAt(firstDigitIdx - 1)))
			firstDigitIdx--;
		
		String prefix = origVersion.substring(0, firstDigitIdx);
		String suffix = origVersion.substring(firstDigitIdx);
		
		Integer n = Integer.valueOf(suffix);
		n++;
	
		String ret = prefix + n.toString();
		// System.out.println("VERSION: " + origVersion + " => " + ret);
		return ret;
	}
}
