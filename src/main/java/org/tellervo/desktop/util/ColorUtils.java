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
// 
// This file is part of Corina.
// 
// Tellervo is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package org.tellervo.desktop.util;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.Editor;


/**
   Various helpful utilities which operate on Colors.

   @see java.awt.Color

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class ColorUtils {
	private final static Logger log = LoggerFactory.getLogger(ColorUtils.class);

    // don't instantiate me
    private ColorUtils() { }

    /** Blend 2 colors equally.
	@param c1 first color
	@param c2 second color
	@return a new Color which is c1 and c2 blended in equal amounts
    */
    public static Color blend(Color c1, Color c2) {
        return new Color((c1.getRed() + c2.getRed()) / 2,
                         (c1.getGreen() + c2.getGreen()) / 2,
                         (c1.getBlue() + c2.getBlue()) / 2);
    }

    /** Blend 2 colors in unequal amounts.  The fractions f1, f2 indicate
	how much of each color to use; it's assumed that f1+f2=1.0 (or
	to within Java's tolerance).

	<p>For example, the call blend(Color.red, 0.1f, Color.white, 0.9f)
	returns a new Color which is 10% red and 90% white.</p>

	@param c1 first color
	@param f1 fraction of first color to use
	@param c2 second color
	@param f2 fraction of second color to use
	@return a new Color which is c1 and c2 blended in unequal amounts
    */
    public static Color blend(Color c1, float f1, Color c2, float f2) {
        return new Color((int) (f1*c1.getRed() + f2*c2.getRed()),
                         (int) (f1*c1.getGreen() + f2*c2.getGreen()),
                         (int) (f1*c1.getBlue() + f2*c2.getBlue()));
    }

    /** Make a color transparent.
	@param c the color to make transparent
	@param alpha the alpha value to use, as an int (0=transparent,
	255=opaque)
	@return a transparent version of the input color
    */
    public static Color addAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    /** Make a color transparent.
	@param c the color to make transparent
	@param alpha the alpha value to use, as a float
	(0.0f=transparent, 1.0f=opaque)
	@return a transparent version of the input color
    */
    public static Color addAlpha(Color c, float alpha) {
        return addAlpha(c, Math.round(255*alpha));
    }

    /** Return true if the color is really dark.  (Technically, if its
	brightness is less than 10%.)

	<p>For example, if you're drawing black text on a region of
	color, you can use this method to determine when to switch to
	white.</p>
	@param c the color to look at
	@return true, if c is really dark, else false
    */
    public static boolean reallyDark(Color c) {
	synchronized (hue_sat_bri) {
	    Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hue_sat_bri);
	    return (hue_sat_bri[2] < 0.10f); // pick a value... 10%
	}
    }
    // i'm assuming i prefer to pay a synchronization penalty
    // rather than a garbage-creation penalty.  maybe i'm just
    // paranoid from seeing how bad the garbage penalty is.
    private static float hue_sat_bri[] = new float[3];

    /**
        Convert a color to grayscale.

        <p>Taken from
        <a href="http://www.photo.net/digital/editing/bwconvert/">http://www.photo.net/digital/editing/bwconvert/</a>:</p>

<pre>
    luminance (B&W) = (0.299 x RED) + (0.587 x GREEN) + (0.114 x BLUE)
</pre>

        <p>(<a href="http://developer.gimp.org/api/1.3/libgimpcolor/libgimpcolor-GimpRGB.html">GIMP's
        values</a> are 0.30, 0.59, and 0.11, which are pretty close.)</p>

        @param c the color
        @return the color as grayscale
    */
    public static Color grayscale(Color c) {
	int value = (int) (0.299 * c.getRed() +
			   0.587 * c.getGreen() +
			   0.114 * c.getBlue());
	return new Color(value, value, value);
	// PERF: we'll get lots of these -- cache them?
    }
    
    
    /**
	 * Generates a red to blue color ramp with of 'n' colors
	 * 
	 * @param n
	 * @return
	 */
	public static Color[] generateColorRamp(int n)
	{
		Color[] cols = new Color[n];
		for(int i = 0; i < n; i++)
		{
			cols[i] = Color.getHSBColor((float) i / (float) (n*1.5), 0.75f, 1.0f);
		}
		return cols;
	}
	
	/**
	 * Get a heat amp color (red to blue) based on the value and the max value in the series
	 *  
	 * @param max
	 * @param value
	 * @return
	 */
	public static Color generateHeatMapColor(float max, float value)
	{
		return generateHeatMapColor(max, null, value);
		
	}

	/**
	 * Get a heat amp color (red to blue) based on the value and the max value in the series.  
	 * If value is below min then null is returned
	 * 
	 *  
	 * @param max
 	 * @param min
	 * @param value
	 * @return
	 */
	public static Color generateHeatMapColor(float max, Double min, float value)
	{
		//log.debug("Getting color for "+value+" when max is "+max);
		
		
		
		int categories = 100;
		Color[] cols = generateColorRamp(categories);
		
		if(value>=max) {
			log.debug("Value is bigger than max so return red");
			return cols[0];
		}
		if(min!=null && value<min) {
			log.debug("Value is below min so return null");
			return null;
		}
		
		float divisions = max/categories;
		int cat  = (int) (categories-(value/divisions))-1;	
		
		
		
		
		try{
			log.debug("Getting color for "+value+" when max is "+max + "  -  returning color category "+cat);

			return cols[cat];
			
		}
		catch (IndexOutOfBoundsException e)
		{
			if(cat<0) {
				log.debug("Category value " + cat + " is below zero so return the first category (red)");
				return cols[0];
			}
			if(cat>categories) {
				log.debug("Category value " + cat + " is beyond the number of categories " + categories + " so return last category (blue)");
				return cols[categories-1];

			}
		}

		
		return null;
		
	}
}
