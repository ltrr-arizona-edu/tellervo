//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.spatial;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.StringTokenizer;

import java.text.DecimalFormat;

/**
   A (latitude, longitude) location on the earth.

   <p>Latitudes run from -90 to 90 degrees; longitudes run from -180 to 180 degrees.
   Positive latitudes are north; positive longitudes are east.</p>

   <p>The accessors are a bit different than most classes, because there are two
   fairly natural ways to access latitude and longitude angles.

   <ul>
     <li>The first is how you'd read or write them, with degrees, minutes, and seconds,
     like "38&deg;15'N".

     <li>The second is more natural for computation, as a floating point number, like
     38.25.
   </ul>

   <p>Since there are times you'd need both, both are provided.  There are 8
   accessors total, for each combination of get/set, latitude/longitude, and
   integer/floating point.  The methods that end with "...AsDegrees()" deal with
   floating point numbers, as degrees, and the methods that end with "...AsSeconds()"
   deal with integers, as seconds (60 seconds to a minute, 60 minutes to a degree).
   (Note that 1 second is about 30 meters at the equator.  You'll never need
   sub-second resolution.)</p>

   <p>(It might be nice to have immutable Locations sometimes, but when rendering maps
   you need speed, speed, speed, and one of the best ways to make Java go faster is to
   generate less garbage for the GC to have to deal with.)</p>
 
   <p>ISO 6709 provides an ASCII-only, locale-independent way to store
   latitude, longitude, and altitude.  ISO is
   <a href="http://www.iso.org/iso/en/CatalogueDetailPage.CatalogueDetail?CSNUMBER=13152">charging
   30 bucks for it</a>, even though it's only a 3-page PDF.  Sigh.
   But you can get <a href="http://www.ftp.uni-erlangen.de/pub/doc/ISO/ISO-6709-summary">the
   Cliff notes here</a>, or <a href="http://www.ftp.uni-erlangen.de/pub/doc/ISO/iso-6709.pdf">the
   whole thing here</a>.</p>

   <h2>Left to do:</h2>
   <ul>
     <li>Extend parseISO6709() to parse any ISO-6709 format
     <li>Decide if this class is threadsafe or not.
     <li>Location.copy() is a bad idea; get rid of it
   </ul>
 
   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public final class Location implements Cloneable {

    //
    // CONSTANTS
    //

    /**
       Radius of the earth in kilometers.  From Miller & Schroeer,
       <i>College Physics</i>, 6th ed: 6.38x10<sup>3</sup> km.
    */
    public final static float EARTH_RADIUS = 6.38e3f; // 6.38 x 10^6 m

    // limits on latitude and longitude, in seconds
    private static final int MIN_LATITUDE = -90 * 3600;
    private static final int MAX_LATITUDE = 90 * 3600;
    private static final int MIN_LONGITUDE = -180 * 3600;
    private static final int MAX_LONGITUDE = 180 * 3600 - 1;

    // L10n of N/S/E/W -- i'll use these inside a loop, later,
    // so i'll just keep them around.
    private static final String NORTH = I18n.getText("north");
    private static final String SOUTH = I18n.getText("south");
    private static final String EAST = I18n.getText("east");
    private static final String WEST = I18n.getText("west");
    
    //
    // FIELDS
    //

    // in seconds; positive latitudes are north, positive longitudes are east
    private int latitude = 0;
    private int longitude = 0;

    //
    // CLONING
    //

    /**
       Make a clone of this Location.

       @return a Location with the same latitude and longitude as this one
    */
    @Override
	public Object clone() {
        Location clone = new Location();
        clone.latitude = this.latitude;
        clone.longitude = this.longitude;
        return clone;
    }

    /**
       Copy the (latitude, longitude) from one location to another.
        
       @param target the Location to copy from
       @param source the Location to copy to
    */
    public static void copy(Location target, Location source) {
        target.latitude = source.latitude;
        target.longitude = source.longitude;
    }

    //
    // ACCESSORS
    //

    /**
       Return the latitude, in degrees.
        
       @return the latitude, in degrees
    */
    public float getLatitudeAsDegrees() {
        return latitude / 3600f;
    }
    /**
       Return the longitude, in degrees.

       @return the longitude, in degrees
    */
    public float getLongitudeAsDegrees() {
        return longitude / 3600f;
    }

    /**
       Set the latitude, in degrees.

       @param degrees the new latitude, in degrees
    */
    public void setLatitudeAsDegrees(float degrees) {
        int seconds = Math.round(degrees * 3600);
        if (seconds < MIN_LATITUDE)
            seconds = MIN_LATITUDE;
        else if (seconds > MAX_LATITUDE)
            seconds = MAX_LATITUDE;
        this.latitude = seconds;
    }
    /**
       Set the longitude, in degrees.

       @param degrees the new longitude, in degrees
    */
    public void setLongitudeAsDegrees(float degrees) {
        int seconds = Math.round(degrees * 3600);

        // need to make sure the longitude is between -180 and 179 degrees.
        // 180 degrees, for example, gets sent back down to -180.
        // if i were doing degrees, i'd add 180, mod 360, sub 180.
        // so just do that in seconds.
        seconds += 180 * 3600;
        seconds %= 360 * 3600;
        if (seconds <= 0)
            seconds += 180 * 3600;
        else
            seconds -= 180 * 3600;
        
        this.longitude = seconds;
    }

    /**
       Get the latitude, in seconds.

       @return the latitude, in seconds
    */
    public int getLatitudeAsSeconds() {
        return latitude;
    }
    /**
       Get the longitude, in seconds.

       @return the longitude, in seconds
    */
    public int getLongitudeAsSeconds() {
        return longitude;
    }

    /**
       Set the latitude, in seconds.

       @param seconds the new latitude, in seconds
    */
    public void setLatitudeAsSeconds(int seconds) {
        if (seconds < MIN_LATITUDE)
            seconds = MIN_LATITUDE;
        else if (seconds > MAX_LATITUDE)
            seconds = MAX_LATITUDE;
        this.latitude = seconds;
    }
    /**
       Set the longitude, in seconds.

       @param seconds the new longitude, in seconds
    */
    public void setLongitudeAsSeconds(int seconds) {
        // make sure longitude is between -180 and 179 degrees.
        // see setLongitudeAsDegrees().
        seconds += 180 * 3600;
        seconds %= 360 * 3600;
        if (seconds <= 0)
            seconds += 180 * 3600;
        else
            seconds -= 180 * 3600;

        // PERF: make sure these are computed at compile-time, not run-time.

        // FIXME: extract method normalizeLongitude() (lat, too, while you're at it)

        this.longitude = seconds;
    }

    //
    // CONSTRUCTORS
    //

    /** Default location: 0&deg;N 0&deg;E. */
    public Location() {
        // do nothing
    }

    /**
        Constructor, given a String.  The input format is perfectly
	compatible with the output of toString(), but is lenient in
	case users type in a location and can't type the degree-sign
        &mdash; any gap between numbers works, so "34*56' N 11 22W" will
	work just fine.  It also accepts ISO-6709 format strings
        (but only the "+DDMMSS+DDDMMSS/" version so far).
        
	@param string the String to parse
        @exception NumberFormatException if the location can't be parsed
    */
    public Location(String string) throws NumberFormatException {
        setLocation(string);
    }

    public void setLocation(String string) throws NumberFormatException {
      if (isISO6709(string))
        parseISO6709(string);
      else
        parseString(string);
    }

    private void parseString(String string) throws NumberFormatException {
        String whitespace = " \t\n\r";
        String degreeMinuteSecond = "\u00B0*'\"";
        StringTokenizer tok = new StringTokenizer(string, whitespace + degreeMinuteSecond);

        int multiplier = 3600; // mulitply by this to make seconds
        int total = 0; // total of the seconds so far

        // we'll barf unless we have exactly one of each.
        int latitudeCount = 0, longitudeCount = 0;
        
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();

            try {
                // it's a number: multiply it, to make it seconds, and add it.
                // (if we get too many numbers, like "12*34'56''78'''N",
                // multiplier=0 after 3 of them, so it's automatically ignored.)
                int number = Integer.parseInt(token);
                total += number * multiplier;
                multiplier /= 60;
            } catch (NumberFormatException nfe) {
                // it's not a number: it's N/S/E/W (if not, ignore it).
                // assume, in any given locale, that compass points never
                // differ from each other only by case.  users will love you.
                String compassPoint = token.toUpperCase();

                if (compassPoint.equals(NORTH)) {
                    setLatitudeAsSeconds(total); latitudeCount++;
                } else if (compassPoint.equals(SOUTH)) {
                    setLatitudeAsSeconds(-total); latitudeCount++;
                } else if (compassPoint.equals(EAST)) {
                    setLongitudeAsSeconds(total); longitudeCount++;
                } else if (compassPoint.equals(WEST)) {
                    setLongitudeAsSeconds(-total); longitudeCount++;
                }

                // reset multiplier, total
                multiplier = 3600;
                total = 0;
            }
        }

        // a location has exactly one latitude, and one longitude.  double-check this.
        if (latitudeCount != 1 || longitudeCount != 1)
            throw new NumberFormatException("bad number of terms in location!");
    }

    // this method only knows how to parse the "+DDMMSS+DDDMMSS/" format so far
    // (i.e., the format that toISO6709() returns)
    private void parseISO6709(String string) throws NumberFormatException {
        if (string.length() != 16)
            throw new NumberFormatException("don't know how to parse all ISO-6709 locations yet!");

        boolean north = (string.charAt(0) == '+');
        int latDegs = Integer.parseInt(string.substring(1, 3));
        int latMins = Integer.parseInt(string.substring(3, 5));
        int latSecs = Integer.parseInt(string.substring(5, 7));
        setLatitudeAsSeconds((north ? +1 : -1) * (latDegs*3600 + latMins*60 + latSecs));

        boolean east = (string.charAt(7) == '+');
        int longDegs = Integer.parseInt(string.substring(8, 11));
        int longMins = Integer.parseInt(string.substring(11, 13));
        int longSecs = Integer.parseInt(string.substring(13, 15));
        setLongitudeAsSeconds((east ? +1 : -1) * (longDegs*3600 + longMins*60 + longSecs));
    }
    
    //
    // TOSTRING
    //

    /*
     * For location editor
     */
    
    public String getEasyString() {
        // extract degrees and minutes, latitude and longitude
        int latDegs = Math.abs(getLatitudeAsSeconds()) / 3600;
        int latMins = Math.abs(getLatitudeAsSeconds()) / 60 - latDegs * 60;
        int longDegs = Math.abs(getLongitudeAsSeconds()) / 3600;
        int longMins = Math.abs(getLongitudeAsSeconds()) / 60 - longDegs * 60;
        
	// hemispheres
	String latHemi = (latitude > 0 ? NORTH : SOUTH);
	String longHemi = (longitude > 0 ? EAST : WEST);

	// assemble it into a string
	return latDegs + "," + latMins + "," + latHemi + "," +
               longDegs + "," + longMins + "," + longHemi;
    	
    }
    
    public String getLatitudeAsString() {
        // extract degrees and minutes
        int latDegs = Math.abs(getLatitudeAsSeconds()) / 3600;
        int latMins = Math.abs(getLatitudeAsSeconds()) / 60 - latDegs * 60;
        // hemisphere
    	String latHemi = (latitude > 0 ? NORTH : SOUTH);

    	return latDegs + DEGREE_SIGN + latMins + "'" + latHemi;
    }

    public String getLongitudeAsString() {
        // extract degrees and minutes
        int longDegs = Math.abs(getLongitudeAsSeconds()) / 3600;
        int longMins = Math.abs(getLongitudeAsSeconds()) / 60 - longDegs * 60;
        
        // hemispheres
        String longHemi = (longitude > 0 ? EAST : WEST);
        
    	return longDegs + DEGREE_SIGN + longMins + "'" + longHemi;
    }
    
    /**
       Return this location as a nicely-formatted string.  The string returned by this
       method is locale-dependent, and should only be used for presentation to the user,
       not for long-term storage; for that, see the toISO6709() method.

       @see #toISO6709
     
       @return this location as a string
    */
    @Override
	public String toString() {
    	return getLatitudeAsString() + " " + getLongitudeAsString();
    }

    // unicode DEGREE SIGN character
    public static final String DEGREE_SIGN = "\u00B0";

    // is this string an ISO-6709 location?
    private boolean isISO6709(String check) {
        return (check.startsWith("+") || check.startsWith("-")) && check.endsWith("/");
    }

    /**
       Convert this location to an ISO-6709 string.  (See above for where to ind ISO-6709
       information.)

       <p>This method always uses the ISO-6709 format <code>+DDMMSS+DDDMMSS/</code>.</p>
        
       @return this Location as an ISO-6709 string
    */
    public String toISO6709() {
        StringBuffer buf = new StringBuffer();

        DecimalFormat twoDigits = new DecimalFormat("00");
        DecimalFormat threeDigits = new DecimalFormat("000");
        
        // latitude
        buf.append(latitude < 0 ? "-" : "+");
        int latDegs = Math.abs(getLatitudeAsSeconds()) / 3600;
        int latMins = Math.abs(getLatitudeAsSeconds()) / 60 - latDegs * 60;
        int latSecs = Math.abs(getLatitudeAsSeconds()) - latDegs * 3600 - latMins * 60;
        buf.append(twoDigits.format(latDegs));
        buf.append(twoDigits.format(latMins));
        buf.append(twoDigits.format(latSecs));

        // longitude
        buf.append(longitude < 0 ? "-" : "+");
        int longDegs = Math.abs(getLongitudeAsSeconds()) / 3600;
        int longMins = Math.abs(getLongitudeAsSeconds()) / 60 - longDegs * 60;
        int longSecs = Math.abs(getLongitudeAsSeconds()) - longDegs * 3600 - longMins * 60;
        buf.append(threeDigits.format(longDegs));
        buf.append(twoDigits.format(longMins));
        buf.append(twoDigits.format(longSecs));
        
        // altitude
        // -- if i want altitude later, add it here, as "+AAA..."

        // terminator
        buf.append("/");

        return buf.toString();
    }

    //
    // EQUALS and HASHCODE
    //

    /**
       Check if an object is equal to this Location.  This tests equality to
       second accuracy.

       @param object the other Location to compare this against
       @return true, if the object is a Location and equal to this, else false
    */
    @Override
	public boolean equals(Object object) {
        // make sure it's a Location
        if (!(object instanceof Location))
            return false;

        // check seconds
        Location location = (Location) object;
        return (latitude == location.latitude && longitude == location.longitude);
    }

    /**
       A hashcode for Locations.  (Since I define equals(), I need to define hashCode().)

       @return a hash code for this Location
    */
    @Override
	public int hashCode() {
        return latitude*latitude*latitude + 3*longitude*longitude*longitude;
    }

    
    public boolean valid() {
    	return (!(latitude == 0 && longitude == 0));
    }
}
