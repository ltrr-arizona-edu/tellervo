package org.tellervo.desktop.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasLocation;
import org.tridas.spatial.GMLPointSRSHandler;

public class GeonamesUtil {
	private static final Logger log = LoggerFactory.getLogger(GeonamesUtil.class);

	private static String GEONAMES_URL = "http://api.geonames.org";
	private static String GEONAMES_USR = "tellervo";
	
	
	public static TridasAddress getAddressForLocation(TridasLocation location)
	{
		if(location==null) return null;
		if(!location.isSetLocationGeometry()) return null;
		if(!location.getLocationGeometry().isSetPoint()) return null;
		
		TridasAddress address = new TridasAddress();
		
		GMLPointSRSHandler tph = new GMLPointSRSHandler(location.getLocationGeometry().getPoint());

		log.debug("Looking up lat: "+tph.getWGS84LatCoord()+", long: "+tph.getWGS84LongCoord()+" in the Geonames webservice");
				
		String url = GEONAMES_URL+"/"+"findNearbyJSON"+"?lat="+tph.getWGS84LatCoord()+"&featureClpi"
				+ "ass=a&lng="+tph.getWGS84LongCoord()+"&username="+GEONAMES_USR;
		
		log.debug("Geonames URL : "+url);
		
		JSONParser parser = new JSONParser();
		try {
			
			URL url2 = new URL(url);
			HttpURLConnection con = (HttpURLConnection) url2.openConnection();
			 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			  
			   Object obj = parser.parse(in);  
			  
			   JSONObject jsonObject = (JSONObject) obj;  
			  			  
			   JSONArray list = (JSONArray) jsonObject.get("geonames");  
			   Iterator<JSONObject> iterator = list.iterator();  
			   while (iterator.hasNext()) {  
				   JSONObject item = iterator.next();
				   
				   address.setCountry((String) item.get("countryName"));
				   address.setCityOrTown((String) item.get("toponymName"));
				   
				   return address;
			   }  
			  
			  } catch (FileNotFoundException e) {  
			  } catch (IOException e) {  
			  } catch (ParseException e) {  
			  }  

		return  null;
	}
	
	public static String getCountryForLocation(TridasLocation location)
	{		
		TridasAddress address = getAddressForLocation(location);
		if(address==null) return null;
		
		if(!address.isSetCountry()) return null;
				
				
		return address.getCountry();
	}
	
	public static String getCityForLocation(TridasLocation location)
	{		
		TridasAddress address = getAddressForLocation(location);
		if(address==null) return null;
		
		if(!address.isSetCityOrTown()) return null;
				
				
		return address.getCityOrTown();
	}
}
