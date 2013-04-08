package org.tellervo.desktop.versioning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class VersionUtil {

	/**
	 * Parse the version string returned from the server to extract
	 * major, minor and revision details.
	 * 
	 * @param str
	 * @throws Exception
	 */
	public static String[] parseVersionStr(String str) throws Exception
	{
		if(str==null) throw new Exception("Version information returned from server is null");
		
		if(str=="") throw new Exception("Version information returned from server is null");
		
		String[] versionparts = str.split("\\.");
		
		if(versionparts.length==0)
		{
			throw new Exception("Version information returned from server is invalid");
		}
		
		return versionparts;
	
	}

	/**
	 * 
	 * @param earliestMajor
	 * @param earliestMinor
	 * @param earliestRevision
	 * @param majorversion
	 * @param minorversion
	 * @param revision
	 * @return
	 */
	public static Boolean compareVersionNumbers(Integer earliestMajor, Integer earliestMinor, String earliestRevision,
												Integer majorversion, Integer minorversion, String revision)
	{
		
		if(majorversion<earliestMajor)
		{
			return false;
		}
		else if (majorversion>earliestMajor)
		{
			return true;
		}
		else
		{
			if(minorversion<earliestMinor)
			{
				return false;
			}
			else if (minorversion>earliestMinor)
			{
				return true;
			}
			else
			{
				if(revision.compareTo(earliestRevision)<0)
				{
					return false;
				}
				else
				{
					return true;
				}
				
			}
			
		}
	}

	/**
	 * Query the tridas.org server for the latest available build number.
	 * Returns null on IO error.
	 * 
	 * @return
	 */
	static String getAvailableVersion(String urlOfVersionInfo)
	{
		URL url;
	
		try {
			url = new URL(urlOfVersionInfo);
		} catch (MalformedURLException e) {
			return null;
		}
		
		try {
	        URLConnection conn = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String inputLine;
	
	        while ((inputLine = in.readLine()) != null) 
	        	return inputLine;
	        in.close();
			 
		} catch (IOException e) {
			return null;
		}
				
		return null;	
	}

}
