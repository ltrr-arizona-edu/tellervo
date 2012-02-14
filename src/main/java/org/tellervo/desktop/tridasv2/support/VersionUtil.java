/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.tridasv2.support;

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
