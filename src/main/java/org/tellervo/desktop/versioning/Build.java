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
package org.tellervo.desktop.versioning;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public class Build {

    /** Version string. */
    private final static String VERSION = Build.class.getPackage().getImplementationVersion(); // beta

    /** Year (for copyright). */
    public final static String YEAR = "2001-2013";

    /** Author's name (for copyright). */
    public final static String AUTHOR = "Peter Brewer, Chris Dunham, Dan Girshovich, Aaron Hamid, Ken Harris, Drew Kalina, Rocky Li, Lucas Madar, Daniel Murphy, Robert 'Mecki' Pohl, Paul Sheppard and Kit Sturgeon";

	/** Most primitive server version supported by this client should be a three part string e.g 1.1.1 */
	public static final String earliestServerVersionSupported = "1.0.9";
	
	// don't instantiate me
    private Build() {
    }

    /**
       The date and time Tellervo was compiled.

       <p>This is computed at compile-time by running <code>date "+%Y
       %B %e %l:%M %p (%Z)"</code>, which returns a string like "23
       October 6 5:06 PM (EDT)".  This string is stored in the file
       Timestamp, which is added to the final Tellervo jar file.</p>
    */
    public final static String TIMESTAMP = loadTimestamp();
    
    
    /**
     * The current SVN revision number stored in a file in Tellervo jar file 
     */
    public final static String REVISION_NUMBER = loadRevisionNumber();

    public final static String COMPLETE_VERSION_NUMBER = getCompleteVersionNumber();



    private static String loadTimestamp() 
    {
    	String timestamp = "Unknown";
    	Enumeration<?> resEnum;
    	try {
    		resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
    		while (resEnum.hasMoreElements()) {
    			try {
    				URL url = (URL)resEnum.nextElement();
    				InputStream is = url.openStream();
    				if (is != null) {
    					Manifest manifest = new Manifest(is);
    					Attributes mainAttribs = manifest.getMainAttributes();
    					timestamp = mainAttribs.getValue("Implementation-Build-Timestamp");
    					if(timestamp != null  && timestamp.length()>0) {
    						return timestamp;
    					}
    				}
    			}
    			catch (Exception e) {
    				// Silently ignore wrong manifests on classpath?
    			}
    		}
    	} catch (IOException e1) {
    		// Silently ignore wrong manifests on classpath?
    	}
    	return "Unknown";
    }
    
      
    @SuppressWarnings("rawtypes")
	private static String loadRevisionNumber()
    {
    	String revision = "Unknown";
    	Enumeration resEnum;
    	try {
    		resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
    		while (resEnum.hasMoreElements()) {
    			try {
    				URL url = (URL)resEnum.nextElement();
    				InputStream is = url.openStream();
    				if (is != null) {
    					Manifest manifest = new Manifest(is);
    					Attributes mainAttribs = manifest.getMainAttributes();
    					revision = mainAttribs.getValue("Implementation-Build");
    					if(revision != null  && revision.length()>0) {
    						return revision;
    					}
    				}
    			}
    			catch (Exception e) {
    				// Silently ignore wrong manifests on classpath?
    			}
    		}
    	} catch (IOException e1) {
    		// Silently ignore wrong manifests on classpath?
    	}
    	return "Unknown";
    }
    
    
    public final static String getCompleteVersionNumber()
    {
    	
    	if(Build.REVISION_NUMBER!="Unknown")
    	{
    		return Build.VERSION+"."+Build.REVISION_NUMBER;
    	}
    	else
    	{
    		return Build.VERSION;
    	}

    }
    
    public final static String getUTF8Version()
    {
    	if(Build.VERSION==null)
    	{
    		return "DEV. SNAPSHOT";
    		//return "0.0.1";
    	}
  
    	return Build.VERSION;
    }
    
    public final static String getVersion()
    {
    	if(Build.VERSION==null)
    	{
    		return "development";
    		
    	}
    	
    	// Replace -SNAPSHOT with beta symbol
    	if(Build.VERSION.contains("-SNAPSHOT"))
    	{
    		return Build.VERSION.replace("-SNAPSHOT", "\u03B2");
    	}
    	return Build.VERSION;
    }
    

}
