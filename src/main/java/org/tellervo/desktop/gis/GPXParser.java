/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gis;

import java.io.BufferedReader;
import java.io.File;
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
	
	public GPXParser(File infile) throws FileNotFoundException, IOException
	{
		lstWaypoints = new ArrayList<GPXWaypoint>();
		BufferedReader in = new BufferedReader(new FileReader(infile));
		parse(in);
	}
	
	public ArrayList<GPXWaypoint> getWaypoints()
	{
		return lstWaypoints;
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
	
	

	
	public class GPXWaypoint implements Comparable<GPXWaypoint>{
		
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

		@Override
		public int compareTo(GPXWaypoint o) {
			
			if(this.name==null) return 1;
			if(o.getName()==null) return 0;
			
			return this.toString().compareTo(o.toString());

		}
	}
	
	
}
