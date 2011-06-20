/**
 * 
 */
package edu.cornell.dendro.corina.util;

import java.awt.Color;

/**
 * @author Lucas
 *
 * Utility class to link color names to values
 */

public class ColorPair {
	public String colorName;
	public Color colorVal;
	
	public ColorPair(String colorName, Color colorVal) {
		this.colorName = colorName;
		this.colorVal = colorVal;
	}
};
