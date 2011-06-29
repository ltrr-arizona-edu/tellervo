/*******************************************************************************
 * Copyright (C) 2003-2011 Ken Harris and Peter Brewer.
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
 * 	   Ken Harris   - Initial implementation
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina;

import java.io.*;

public class Build {
    // don't instantiate me
    private Build() {
    }

    /**
       The date and time Corina was compiled.

       <p>This is computed at compile-time by running <code>date "+%Y
       %B %e %l:%M %p (%Z)"</code>, which returns a string like "23
       October 6 5:06 PM (EDT)".  This string is stored in the file
       Timestamp, which is added to the final Corina.jar file.</p>
    */
    public final static String TIMESTAMP = loadTimestamp();
    
    
    /**
     * The current SVN revision number stored in a file in Corina.jar 
     */
    public final static String REVISION_NUMBER = loadRevisionNumber();

    public final static String COMPLETE_VERSION_NUMBER = loadCompleteVersionNumber();


    // read the first line of the text file "Timestamp", included in
    // this jar, and return it
    private static String loadTimestamp() {
	try {
	    ClassLoader cl = edu.cornell.dendro.corina.Build.class.getClassLoader();
	    InputStream is = cl.getResourceAsStream("Timestamp");
      if (is != null) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	      try {
   	      return br.readLine();
        } finally {
          br.close();
        }
      }
	} catch (IOException ioe) {
	    new edu.cornell.dendro.corina.gui.Bug(ioe);
	}

	return "Unknown";
    }
    
      
    // read the first line of the text file "Revision", included in
    // this jar, and return it
    private static String loadRevisionNumber() {
	try {
	    ClassLoader cl = edu.cornell.dendro.corina.Build.class.getClassLoader();
	    InputStream is = cl.getResourceAsStream("Revision");
      if (is != null) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	      try {
   	      return br.readLine();
        } finally {
          br.close();
        }
      }
	} catch (IOException ioe) {
	    new edu.cornell.dendro.corina.gui.Bug(ioe);
	}

	return "Unknown";
    }
    
    private static String loadCompleteVersionNumber(){
    	
    	if(Build.REVISION_NUMBER!="Unknown")
    	{
    		return Build.VERSION+"."+Build.REVISION_NUMBER;
    	}
    	else
    	{
    		return Build.VERSION;
    	}

    }

    /** Version string. */
    public final static String VERSION = "2.12"; // beta

    /** Year (for copyright). */
    public final static String YEAR = "2001-2011";

    /** Author's name (for copyright). */
    public final static String AUTHOR = "Peter Brewer, Chris Dunham, Dan Girshovich, Aaron Hamid, Ken Harris, Drew Kalina, Rocky Li, Lucas Madar, Daniel Murphy, Robert 'Mecki' Pohl and Kit Sturgeon";
}
