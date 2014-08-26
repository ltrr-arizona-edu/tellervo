package org.tellervo.desktop.odk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ODKParser {

	private boolean failedParse = false;
	private String errorMessage;
	private Document doc;
	
	public ODKParser(File file, Class<? extends ITridas> clazz) throws FileNotFoundException, IOException, Exception
	{
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
		
		if(clazz.equals(TridasObject.class))
		{
			if(getFieldValue("PlotSubplotID")==null)
			{
				errorMessage = "No object code field";
				failedParse = true;
				return;
			}
			
			if(getFieldValue("TreeNO")!=null)
			{
				errorMessage = "File contains tree info so is being ignored";
				failedParse = true;
				return;
			}
			
			if(getFieldValue("SampleID")!=null)
			{
				errorMessage = "File contains sample info so is being ignored";
				failedParse = true;
				return;
			}
		}
		else if (clazz.equals(TridasElement.class))
		{
			if(getFieldValue("TreeNO")==null)
			{
				errorMessage = "No element code field";
				failedParse = true;
				return;
			}

		}
		else if (clazz.equals(TridasSample.class))
		{
			if(getFieldValue("SampleID")==null)
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
		
		
	}

	public String getParseErrorMessage()
	{
		return errorMessage;
	}
	
	public boolean isValidODKFile() {
		return !failedParse;
	}
	
	public Object getFieldValue(String field)
	{
		
		NodeList nList = doc.getElementsByTagName(field);
		 
		if(nList.getLength()==0) return null;
	
		try{
			return nList.item(0).getTextContent();
		} catch (Exception e)
		{
			System.out.println("Error getting tag text");
			return null;
		}
		
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
	
	private String[] getSpaceDelimitedFieldParts(String fieldname)
	{
		String field = (String) getFieldValue(fieldname);
		
		if(field==null) return null;
		
		return field.split(" ");
	}
	
}
