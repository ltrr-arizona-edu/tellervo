package org.tellervo.desktop.odk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.wsi.tellervo.TridasElementTemporaryCacher;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.Certainty;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

public class ODKParser {

	private boolean failedParse = false;
	private String errorMessage = "";
	private Document doc;
	private File file;
	private static final Logger log = LoggerFactory.getLogger(ODKParser.class);

	public enum ODKFileType {OBJECTS, ELEMENTS_AND_SAMPLES};
	
	
	public ODKParser(File f) throws FileNotFoundException, IOException, Exception
	{
		file = f;

		
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			
			dBuilder = dbFactory.newDocumentBuilder();
			
			doc = dBuilder.parse(file);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			failedParse = true;
			errorMessage = e.getMessage();
			return;
		} catch (SAXException e) {
			failedParse = true;
			errorMessage = e.getMessage();
			return;
		}
		
		
		if(getFieldValueAsString("tridas_object_code")==null)
		{
			errorMessage = "No object code field";
			failedParse = true;
			return;
		} 
		
		//
		
		/*if(clazz.equals(TridasObject.class))
		{
			if(getFieldValueAsString("tridas_object_code")==null)
			{
				errorMessage = "No object code field";
				failedParse = true;
				return;
			} 
			else
			{
				log.debug("---"+doc.getElementsByTagName("tridas_object_code").item(0).getTextContent());
			}
			
			if(getFieldValueAsString("TreeNO")!=null  || getFieldValueAsString("tridas_element_title")!=null)
			{
				errorMessage = "File contains tree info so is being ignored";
				failedParse = true;
				return;
			}
			
			if(getFieldValueAsString("SampleID")!=null|| getFieldValueAsString("tridas_sample_title")!=null)
			{
				errorMessage = "File contains sample info so is being ignored";
				failedParse = true;
				return;
			}
		}
		else if (clazz.equals(TridasElement.class))
		{
			if(getFieldValueAsString("TreeNO")==null && getFieldValueAsString("tridas_element_title")==null)
			{
				errorMessage = "No element code field";
				failedParse = true;
				return;
			}

		}
		else if (clazz.equals(TridasSample.class))
		{
			if(getFieldValueAsString("SampleID")==null && getFieldValueAsString("tridas_sample_title")==null)
			{
				errorMessage = "No sample code field";
				failedParse = true;
				return;
			}
		}
		else
		{
			throw new Exception("ODK parser only supports TRiDaS objects, elements and samples");
		}
		*/
		
	}
	
	
	/**
	 * Determine if file contains information about an object, or elements & samples
	 * @return
	 */
	public ODKFileType getFileType()
	{
		
		if(getFieldValueAsString("tridas_object_code")!=null)
		{
			if(getFieldValueAsString("SampleID")!=null || getFieldValueAsString("tridas_sample_title")!=null)
			{
				return ODKFileType.ELEMENTS_AND_SAMPLES;
			}
			else
			{
				return ODKFileType.OBJECTS;
			}
		}	
		
		return null;
		
		
	}

	public String getParseErrorMessage()
	{
		return errorMessage;
	}
	
	public boolean isValidODKFile() {
		return !failedParse;
	}
	
	public String getFieldValueAsString(String field)
	{
		if(doc.getElementsByTagName(field).getLength()==0) 
		{
			//log.info("Field '"+field+"' not found in ODK file");
			return null;
		}
		try{
			String contents = doc.getElementsByTagName(field).item(0).getTextContent();
			if(contents==null || contents.length()==0) {
				return null;
			}
			else
			{
				return contents;
			}
		} catch (Exception e)
		{
			log.warn("Error getting tag text for field: "+field);
			return null;
		}
		
	}
		
	public Integer getFieldValueAsInteger(String field)
	{
		
		if(doc.getElementsByTagName(field).getLength()==0) 
		{
			//log.info("Field '"+field+"' not found in ODK file");
			return null;
		}
		
		NodeList nList = doc.getElementsByTagName(field);
		 
		if(nList.getLength()==0) return null;
	
		try{
			if(nList.item(0).getNodeValue()!=null)
			{
				Integer intval = Integer.parseInt(nList.item(0).getNodeValue());
				return intval;
			}
		} catch (Exception e)
		{
			log.warn("Error getting number from tag field: "+field+". The value was "+nList.item(0).getNodeValue());
			return null;
		}
		return null;
		
	}
	
	public Double getFieldValueAsDouble(String field)
	{
		
		if(doc.getElementsByTagName(field).getLength()==0) 
		{
			//log.info("Field '"+field+"' not found in ODK file");
			return null;
		}
		
		NodeList nList = doc.getElementsByTagName(field);
		 
		if(nList.getLength()==0) return null;
	
		try{
			Double dblval = Double.parseDouble(nList.item(0).getNodeValue());
			return dblval;
		} catch (Exception e)
		{
			log.warn("Error getting number from tag field: "+field);
			return null;
		}
		
	}
	
	public String getFieldValueAsStringFromNodeList(String field, NodeList nList)
	{
			
		if(doc.getElementsByTagName(field).getLength()==0) 
		{
			//log.info("Field '"+field+"' not found in ODK file");
			return null;
		}
		
		
		
		try{
		    for (int i=0; i < nList.getLength(); i++) {
		        Node subnode = nList.item(i);
		        		        
		        if (subnode.getNodeType() == Node.ELEMENT_NODE) {
		        	Element eElement = (Element) subnode; 
		        	
		        	if(subnode.getNodeName().equals(field))
		        	{
		        		String val = eElement.getTextContent();
		        		if(val!=null) return val;
		        	}
		        	
		        }
		    }
			
			
		} catch (Exception e)
		{
			log.warn("Error getting tag text from field: "+field);
			return null;
		}
		
		return null;
		
	}
	
	
	public NodeList getNodeListByName(String field)
	{
		NodeList nList = doc.getElementsByTagName(field);
		
		if(nList.getLength()==0) return null;
		
		return nList;
	}
	
	public Double getLatitude(String fieldname)
	{
		String[] parts = getSpaceDelimitedFieldParts(fieldname);
		if(parts==null) return null;
		try{
			return Double.parseDouble(parts[0]);
		} catch (NumberFormatException e)
		{
			return null;
		}
	}
	
	public Double getLatitude(String fieldname1, String fieldname2)
	{
		Double one = getLatitude(fieldname1);
		Double two = getLatitude(fieldname2);
		
		if(one!=null) return one;
		if(two!=null) return two;
		return null;
	}
	
	public Double getLongitude(String fieldname1, String fieldname2)
	{
		Double one = getLongitude(fieldname1);
		Double two = getLongitude(fieldname2);
		
		if(one!=null) return one;
		if(two!=null) return two;
		return null;
	}
	
	public Double getLongitude(String fieldname)
	{
		String[] parts = getSpaceDelimitedFieldParts(fieldname);
		if(parts==null) return null;
	
		if(parts.length>1) 
		{
			try{
				return Double.parseDouble(parts[0]);
			} catch (NumberFormatException e) {}
		}
		return null;
	}
	
	public Double getElevation(String fieldname)
	{
		String[] parts = getSpaceDelimitedFieldParts(fieldname);
		if(parts==null) return null;
	
		if(parts.length>2) 
		{
			try{
				return Double.parseDouble(parts[2]);
			} catch (NumberFormatException e) {}
		}
		return null;
	}
	
	public Double getElevation(String fieldname1, String fieldname2)
	{
		Double one = getElevation(fieldname1);
		Double two = getElevation(fieldname2);
		
		if(one!=null) return one;
		if(two!=null) return two;
		return null;	
	}
	
	public Double getError(String fieldname)
	{
		String[] parts = getSpaceDelimitedFieldParts(fieldname);
		if(parts==null) return null;
	
		if(parts.length>1) 
		{
			try{
				return Double.parseDouble(parts[3]);
			} catch (NumberFormatException e) {}
		}
		return null;
	}
	
	public Double getError(String fieldname1, String fieldname2)
	{
		Double one = getError(fieldname1);
		Double two = getError(fieldname2);
		
		if(one!=null) return one;
		if(two!=null) return two;
		return null;
	}
	
	private String[] getSpaceDelimitedFieldParts(String fieldname)
	{
		String field = (String) getFieldValueAsString(fieldname);
		
		if(field==null) return null;
		
		return field.split(" ");
	}
	
	public File getFile()
	{
		return file;
	}
	
	public org.tridas.schema.Date getDate()
	{

		try {
		
			Date dob=null;
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String enddate = getFieldValueAsString("end");
			if(enddate==null) return null;
			if(enddate.length()<10) return null;
			
			dob = df.parse( enddate.substring(0, 10));
			 
			
			GregorianCalendar gcal = new GregorianCalendar();

			gcal.setTime(dob);
			
			XMLGregorianCalendar xmlGregCal = App.datatypeFactory.newXMLGregorianCalendarDate(
					gcal.get(Calendar.YEAR),
					gcal.get(Calendar.MONTH),
					gcal.get(Calendar.DAY_OF_MONTH),
					DatatypeConstants.FIELD_UNDEFINED);
			
			org.tridas.schema.Date date = new org.tridas.schema.Date();
			date.setValue(xmlGregCal);
			date.setCertainty(Certainty.EXACT);
			
			return date;
		}  catch (Exception e)
		{
			log.debug("Error parsing date");
		} 
		
		return null;
	}
	
	public TridasElement getTridasElement(TridasElementTemporaryCacher cache, String objectCodeField, String elementCodeField)
	{

		TridasElement element = cache.getTridasElement(getFieldValueAsString(objectCodeField).toString(), getFieldValueAsString(elementCodeField).toString());
		if(element==null)
		{
			errorMessage+="The parent element to this sample ("+getFieldValueAsString(objectCodeField).toString()+"-"+getFieldValueAsString(elementCodeField).toString()+") does not exist in the database.  Skipping this sample.<br/>";
		}
		
		return element;
	}
	
	public TridasElement getChrisTridasElement(TridasElementTemporaryCacher cache, String objectCodeField, String elementCodeField, String objectCodeField2, String elementCodeField2)
	{
		TridasElement element1 = getTridasElement(cache, objectCodeField, elementCodeField);
		TridasElement element2 = getTridasElement(cache, objectCodeField2, elementCodeField2);
		
		
		if(element1!=null) return element1;
		if(element2!=null) return element2;

		return null;

	}
	public static String formatFileNameForReport(File file)
	{	
		return "<b>"+file.getAbsoluteFile()+"</b>";	
	}

	
	public HashMap<String, String> getAllFields()
	{
		HashMap<String, String> map = new HashMap<String, String>();

	    NodeList nodeList = doc.getElementsByTagName("*");
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node node = nodeList.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	        	
	        	try{
		        	String name = node.getNodeName();
		        	if(name.equals("meta") || name.equals("instanceName") || name.equals("data")) continue;
		        	String value = node.getFirstChild().getNodeValue();
		        	
		        	
		        	
		        	//if(name.startsWith("tridas_")) name = name.substring(7);
		        	//if(name.startsWith("tellervo.user.")) name = name.substring(14);
		        	
		        	map.put(name, value);
	        	} catch (Exception e)
	        	{
	        		
	        	}
	        }
	    }
	    
	    return map;
	}
	
	public ArrayList<String> getMediaFileFields()
	{
		ArrayList<String> mediaFileList = new ArrayList<String>();

		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {

				try {
					String name = node.getNodeName();
					if (name.equals("meta") || name.equals("instanceName")
							|| name.equals("data"))
						continue;
					String value = node.getFirstChild().getNodeValue();

					if (name.startsWith("tellervo.user.image")
							|| name.startsWith("tellervo.user.video")
							|| name.startsWith("tellervo.user.audio")
							|| name.startsWith("tridas_object_file_photo")
							|| name.startsWith("tridas_object_file_sound")
							|| name.startsWith("tridas_object_file_video")
							|| name.startsWith("tridas_element_file_photo")
							|| name.startsWith("tridas_element_file_sound")
							|| name.startsWith("tridas_element_file_video")
							|| name.startsWith("tridas_sample_file_photo")
							|| name.startsWith("tridas_sample_file_sound")
							|| name.startsWith("tridas_sample_file_video")) {
						mediaFileList.add(value);
					}
				} catch (Exception e) {

				}
			}
		}

		return mediaFileList;
		    
	}
	

	
}
