package edu.cornell.dendro.corina.gis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.tridas.schema.DateTime;
import org.tridas.schema.ObjectFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GPXParser {

	private ArrayList<GPXWaypoint> lstWaypoints;
		
	public GPXParser(BufferedReader buff) throws IOException
	{
		lstWaypoints = new ArrayList<GPXWaypoint>();
		
		parse(buff);
	}
	
	public GPXParser(String filename) throws FileNotFoundException, IOException
	{
		lstWaypoints = new ArrayList<GPXWaypoint>();
		 BufferedReader in
		   = new BufferedReader(new FileReader(filename));

		parse(in);
	}
	
	public ArrayList<GPXWaypoint> getWaypoints()
	{
		return lstWaypoints;
	}
	
	public static void main(String args[]) 
	{
		String filename = "/Users/peterbrewer/Desktop/gpx.gpx";
		GPXParser gpx = null;
		
		try {
			gpx = new GPXParser(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(GPXWaypoint wpt : gpx.getWaypoints())
		{
			System.out.println("Waypoint '"+wpt.getName()+"'"+
					" Lat: "+wpt.getLatitude()+
					" Lon: "+wpt.getLongitude()+
					" at elevation "+wpt.getElevation());
		}
		
	}
	
	private void parse(Reader reader) throws IOException
	{
		// Parse into document
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;
		try {
			 db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IOException("Error parsing GPX file");
		}
		
		InputSource is = new InputSource();
        is.setCharacterStream(reader);
        
        try {
			doc = db.parse(is);
		} catch (SAXException e) {
			throw new IOException("Error parsing GPX file");
		} catch (IOException e) {
			throw new IOException("Error parsing GPX file");
		}
		
		NodeList records = doc.getElementsByTagName("wpt");
		
		for (int i=0; i<records.getLength(); i++)
		{		
			Element record = (Element) records.item(i);
			lstWaypoints.add(new GPXWaypoint(record));
		}
	}
	
	

	
	public class GPXWaypoint{
		
		private Double latitude;
		private Double longitude;
		private Double elevation;
		private Date date;
		private String name;
		
		
		public GPXWaypoint(Element wpt){
			
			if(wpt== null) return;
			
			if(wpt.hasAttribute("lat"))
			{
				latitude = Double.valueOf(wpt.getAttribute("lat"));
			}
			if(wpt.hasAttribute("lon"))
			{
				longitude = Double.valueOf(wpt.getAttribute("lon"));
			}
			
			NodeList values = wpt.getChildNodes();
			
			for (int i=0; i<=values.getLength(); i++)
			{		
				Element tag;
				
				if(values.item(i) instanceof Element)
				{
					tag = (Element) values.item(i);
				}
				else
				{
					continue;
				}
				
				if(tag.getNodeName().equals("name"))
				{
					name = tag.getFirstChild().getNodeValue();
				}
				else if(tag.getNodeName().equals("ele"))
				{
					elevation = Double.valueOf(tag.getFirstChild().getNodeValue());
				}
				else if(tag.getNodeName().equals("time"))
				{
					date = DatatypeConverter.parseDateTime(tag.getFirstChild().getNodeValue()).getTime();
				}
			}
		
			
		}
		
		public Double getLatitude()
		{
			return latitude;
		}
		
		public Double getLongitude()
		{
			return longitude;
		}
		
		public Double getElevation()
		{
			return elevation;
		}
		
		public String getName()
		{
			return name;
		}
		
		public Date getDate()
		{
			return date;
		}
		
		public String toString()
		{
			return name;
		}
	}
	
	
}
