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
package edu.cornell.dendro.corina.wsi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;
import edu.cornell.dendro.corina.wsi.util.WSCookieStoreHandler;

/**
 * Class for polling the Corina WS to check the server is valid and to check
 * version information.
 * 
 * @author pwb48
 *
 */
public class WSIServerDetails {

	/**
	 * Status of Corina Webservice Server
	 * @author pwb48
	 *
	 */
	public enum WSIServerStatus
	{
		MALFORMED_URL,
		URL_NOT_RESPONDING,
		URL_NOT_CORINA_WS,
		NOT_CHECKED,
		TOO_OLD,
		STATUS_ERROR,
		VALID;
		
	}
	
	private static final Logger log = LoggerFactory.getLogger(WSIServerDetails.class);
	private Integer majorversion = null;
	private Integer minorversion = null;
	private String revision = "";
	private WSIServerStatus status = WSIServerStatus.NOT_CHECKED;
	private String errMessage= null;
	
	public WSIServerDetails()
	{
		pingServer();
	}
	
	/**
	 * Ping the server again to update status
	 * 
	 * @return
	 */
	public boolean pingServer()
	{
	    
		URI                url; 
	    BufferedReader    dis = null;
	    DefaultHttpClient client = new DefaultHttpClient();

	    try {
	    	
			String path = App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid-url!");
			
			url = new URI(path.trim());
	    	
			// load cookies
			client.setCookieStore(WSCookieStoreHandler.getCookieStore().toCookieStore());
			
			// are we using https? should we allow self-signed certs?
			if(url.getScheme().equals("https")) 
				WebJaxbAccessor.setSelfSignableHTTPSScheme(client);
	
			
			HttpGet req = new HttpGet(url);
			
			HttpResponse response = client.execute(req);
			
			if(response.getStatusLine().getStatusCode() == 200)
			{
				InputStream responseIS = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(responseIS));
				String s = "";
				while ((s = reader.readLine()) != null)
				{
				      if(s.contains("<wsVersion>"))
				      {
				    	  String[] strparts = s.split("<[/]*wsVersion>");
				    	  if(strparts.length>0) parserThisServerVersion(strparts[1]);
				    	  
				    	  status = WSIServerStatus.VALID;
				    	  return true;
				      }
				}
			}
			else
			{
				errMessage="Web service URL is invalid";
				log.debug(errMessage);
				status = WSIServerStatus.URL_NOT_RESPONDING;
				return false;
			}
			

	    } catch (ClientProtocolException e) {
	    	errMessage="Web service URL is invalid";
			log.debug(errMessage);
			status = WSIServerStatus.URL_NOT_RESPONDING;
			return false;
		} catch (IOException e) {
			errMessage="Web service URL is invalid";
			log.debug(errMessage);
			status = WSIServerStatus.URL_NOT_RESPONDING;
			return false;
		} catch (URISyntaxException e) {
			errMessage="Web service URL is malformed";
			log.debug(errMessage);
			status = WSIServerStatus.MALFORMED_URL;
			return false;
		} catch (IllegalStateException e){
			errMessage="Web service URL is malformed";
			log.debug(errMessage);
			status = WSIServerStatus.MALFORMED_URL;
			return false;			
		} catch (Exception e) {
			errMessage="Unable to obtain version information from server: " + e.getLocalizedMessage();
			log.debug(errMessage);
			status = WSIServerStatus.URL_NOT_CORINA_WS;
			return false;
		} finally{
			try {
				if(dis!=null)
				{
					dis.close();
				}
			} catch (IOException e) { }
		}
	
		status = WSIServerStatus.URL_NOT_CORINA_WS;

		return false;
	}
	
	/**
	 * Get the status of the Corina server
	 * @return
	 */
	public WSIServerStatus getWSIServerStatus()
	{
		return status;
	}
	
	/**
	 * Parse the version string returned from the server to extract
	 * major, minor and revision details.
	 * 
	 * @param str
	 * @throws Exception
	 */
	private String[] parseVersionStr(String str) throws Exception
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
	
	
	private void parserThisServerVersion(String str) throws Exception
	{
		String[] versionparts = parseVersionStr(str);
		
		try{
			if(versionparts.length>=1)
			{
				majorversion = Integer.parseInt(versionparts[0]);
			}
			if(versionparts.length>=2)
			{
				minorversion = Integer.parseInt(versionparts[1]);
			}
			if(versionparts.length>=3)
			{
				revision = versionparts[2];
			}
		} catch (NumberFormatException e)
		{
			throw new Exception("Server version is invalid");
		}
		
		isServerValid();
		
	}
	
	/**
	 * Get the Corina server version as a full string
	 * 
	 * @return
	 */
	public String getWSIVersion()
	{
		String str = "";
		
		if(!majorversion.equals(""))
		{
			str+=majorversion;
		}
		if(!minorversion.equals(""))
		{
			str+="."+minorversion;
		}
		if(!revision.equals(""))
		{
			str+=" rev. "+revision;
		}
		
		return str;

	}
	
	/**
	 * Is the server a valid Corina WS?
	 * 
	 * @return
	 */
	public Boolean isServerValid()
	{		
		
		if(status==WSIServerStatus.VALID)
		{
			try {
				String[] earliestServerVersion = parseVersionStr(App.earliestServerVersionSupported);
				Integer earliestMajor = null;
				Integer earliestMinor = null;
				String earliestRevision = null;
				
				
				if(earliestServerVersion.length>=1)
				{
					earliestMajor = Integer.parseInt(earliestServerVersion[0]);
				}
				if(earliestServerVersion.length>=2)
				{
					earliestMinor = Integer.parseInt(earliestServerVersion[1]);
				}
				if(earliestServerVersion.length>=3)
				{
					earliestRevision = earliestServerVersion[2];
				}
				
				if(majorversion<earliestMajor)
				{
					setTooOldErrorMessage();
					return false;
				}
				else
				{
					if(minorversion<earliestMinor)
					{
						setTooOldErrorMessage();
						return false;
					}
					else
					{
						if(earliestRevision.compareTo(earliestRevision)<0)
						{
							setTooOldErrorMessage();
							return false;
						}
						
					}
					
				}
				
				
				
			}
			catch (Exception e) {
				errMessage = "Supported server versions not specified correctly in code";
				log.debug(errMessage);
				return false;
			}
			
			
			return true;
		}
		
		return false;
	}
	
	private void setTooOldErrorMessage()
	{
		errMessage = "The version of the Corina server ("+ getWSIVersion()+") that\n"+
		"you are trying to connect to is too old for this client.\n"+
		"The server needs to be version "+ App.earliestServerVersionSupported + " or later.";
		log.debug(errMessage);
		status = WSIServerStatus.TOO_OLD;
	}
	
	public String getErrorMessage()
	{
		return errMessage;
	}
	
	
	
	/**
	 * Get the major version number for the server
	 * 
	 * @return
	 */
	public Integer getMajorVersion()
	{
		return majorversion;
	}
	
	/**
	 * Get the minor version number for the server
	 * 
	 * @return
	 */
	public Integer getMinorVersion()
	{
		return minorversion;
	}
	
	/**
	 * Get the revision number for the server
	 * @return
	 */
	public String getRevision()
	{
		return revision;
	}
}
