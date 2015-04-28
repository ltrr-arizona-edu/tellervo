package org.tellervo.desktop.dccd;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

public class DCCDClient {

	private String username;
	private String pwd;
	private String baseurl;
	
	
	public DCCDClient()
	{

			baseurl = "http://localhost:1405/dccd-rest/rest/";
		
	}
	
	public void setBaseURL(String baseURL)
	{
		baseurl = baseURL;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	private String getAuthString()
	{
		String str= username+":"+pwd;
		return Base64.encodeBase64String(str.getBytes());
	}
	
	private void getAllProjects()
	{
		
	}
	
	
}
