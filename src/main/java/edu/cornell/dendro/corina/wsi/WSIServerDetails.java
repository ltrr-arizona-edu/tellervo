package edu.cornell.dendro.corina.wsi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.wsi.util.WSCookieStoreHandler;

/**
 * Class for polling the Corina WS to check the server is valid and the check
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
		VALID,
		URL_NOT_RESPONDING,
		URL_NOT_CORINA_WS,
		NOT_CHECKED;
		
	}
	
	private static final Logger log = LoggerFactory.getLogger(WSIServerDetails.class);
	private String majorversion = "";
	private String minorversion = "";
	private String revision = "";
	private WSIServerStatus status = WSIServerStatus.NOT_CHECKED;
	
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
	    URLConnection      urlConn; 
	    BufferedReader    dis = null;
	    DefaultHttpClient client = new DefaultHttpClient();

	    try {
	    	
			String path = App.prefs.getPref("corina.webservice.url", "invalid-url!");
			
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
				    	  if(strparts.length>0) parseVersionStr(strparts[1]);
				    	  
				    	  status = WSIServerStatus.VALID;
				    	  return true;
				      }
				}
			}
			else
			{
				log.error("Web service URL is invalid");
				status = WSIServerStatus.URL_NOT_RESPONDING;
				return false;
			}
			

	    } catch (ClientProtocolException e) {
			log.error("Web service URL is invalid");
			status = WSIServerStatus.URL_NOT_RESPONDING;
			return false;
		} catch (IOException e) {
			log.error("Web service URL is invalid");
			status = WSIServerStatus.URL_NOT_RESPONDING;
			return false;
		} catch (URISyntaxException e) {
			log.error("Web service URL is malformed");
			status = WSIServerStatus.MALFORMED_URL;
			return false;
		} catch (Exception e) {
			log.error("Unable to obtain version information from server: " + e.getLocalizedMessage());
			status = WSIServerStatus.URL_NOT_CORINA_WS;
			return false;
		}
	
			/*
		    urlConn = url.openConnection(); 
		    urlConn.setDoInput(true); 
		    urlConn.setUseCaches(false);
	

		    dis = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

		    String s;
		    
		    while ((s = dis.readLine()) != null)
		    { 
		      if(s.contains("<wsVersion>"))
		      {
		    	  String[] strparts = s.split("<[/]*wsVersion>");
		    	  if(strparts.length>0) parseVersionStr(strparts[0]);
		    	  
		    	  status = WSIServerStatus.VALID;
		    	  return true;
		      }
		    } 
	    
	    
		} catch (MalformedURLException e) {
			log.error("Web service URL is malformed");
			status = WSIServerStatus.MALFORMED_URL;
			return false;
		} catch (IOException e) {
			log.error("Web service URL is invalid");
			status = WSIServerStatus.URL_NOT_RESPONDING;
			return false;

		} catch (Exception e) {
			log.error("Unable to obtain version information from server: " + e.getLocalizedMessage());
			status = WSIServerStatus.URL_NOT_CORINA_WS;
			return false;
		} */finally
		{
			try {
				if(dis!=null)
				{
					dis.close();
				}
			} catch (IOException e) { }
		}
	
		status = WSIServerStatus.VALID;

		return true;
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
	private void parseVersionStr(String str) throws Exception
	{
		if(str==null) throw new Exception("Server version is null");
		
		if(str=="") throw new Exception("Server version is null");
		
		String[] versionparts = str.split("\\.");
		
		if(versionparts.length==0)
		{
			throw new Exception("Server version invalid");
		}
		
		if(versionparts.length>=1)
		{
			majorversion = versionparts[0];
		}
		if(versionparts.length>=2)
		{
			minorversion = versionparts[1];
		}
		if(versionparts.length>=3)
		{
			revision = versionparts[2];
		}
		
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
			str+="."+revision;
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
			return true;
		}
		
		return false;
	}
	
	/**
	 * Get the major version number for the server
	 * 
	 * @return
	 */
	public String getMajorVersion()
	{
		return majorversion;
	}
	
	/**
	 * Get the minor version number for the server
	 * 
	 * @return
	 */
	public String getMinorVersion()
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
