package com.rediscov.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.tridasv2.ui.LocationGeometry;
import org.tridas.io.I18n;
import org.tridas.io.TridasIO;
import org.tridas.io.util.DateUtils;
import org.tridas.io.util.FileHelper;
import org.tridas.io.util.IOUtils;
import org.tridas.io.util.ITRDBTaxonConverter;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.DateTime;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocation;
import org.tridas.spatial.SpatialUtils;
import org.tridas.spatial.SpatialUtils.UTMDatum;
import org.tridas.util.TridasObjectEx;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.jhlabs.map.proj.Projection;
import com.rediscov.schema.NewDataSet;
import com.rediscov.schema.NormalClass1;
import com.rediscov.schema.NormalClass2;
import com.rediscov.schema.NormalClass3;
import com.rediscov.schema.NormalClass4;
import com.rediscov.schema.NormalDendroSample;
import com.rediscov.schema.NormalObjectStatus;
import com.rediscov.schema.NormalStorageUnit;
import com.rediscov.schema.RediscoveryExport;
import com.rediscov.schema.StrBoolean;

import edu.emory.mathcs.backport.java.util.Collections;

/*************************
 * FIELDS TO HANDLE
 * CtrlProp - CONSTANT - N  
 * Class_1 - CONSTANT - ARCHEOLOGY
 * Class_2 - Autohandle through HistCultPer field 
 * Class_3 - CONSTANT - VEGETAL 
 * Class_4 - CONSTANT - WOOD
 * Object_NOM - CONSTANT - DENDRO SAMPLE 
Catalog -> New field
Parts
Accession -> New field
 * Location -> Parsed -> Box name
 * Object_Status - CONSTANT - STORAGE - INCOMING LOAN
Status_Date -> New field
Item_Count 
 * Storage_Unit - Constant - EA
Description
Measurements
Other_Numbers 
Condition -> New field
Cataloger -> New field
Catalog_Date -> New field? Sample.createdTimestamp
Identified_By -> New field
Ident_Date -> New field
Field_Site
State_Site
 * Site_Name -> Object.title
 * Within_Site -> Element.location.description?
 * Origin -> parsed -> element.location.address.addressLine2, stateProvinceRegion, country
UTM_Z_E_N  -> Ignore?
 * Lat_LongN_W  -> element.location.locationGeometry
Hist_Cult_Per -> New field
Cultural_ID -> New field
Fld_Specimen -> New field
 * Collector -> sample.sampledBy
 * Object_Part -> parsed -> element.type and element.shape
User_1 
User_2 
User_3 
User_4 
 * User_5 -> Parsed -> taxon ControlledVoc
*************************8*/


@XmlRootElement(name = "RediscoveryExport")
public class RediscoveryExportEx extends RediscoveryExport {

	private static final long serialVersionUID = 1L;
	protected final static Logger log = LoggerFactory.getLogger(RediscoveryExportEx.class);

	
	public RediscoveryExportEx()
	{
		
	}
		
	/**
	 * Set all ICMS fields that are constant for dendrochronology
	 */
	public void setConstantFields()
	{
		this.setCtrlProp(StrBoolean.N);
		this.setClass1(NormalClass1.ARCHEOLOGY);
		this.setClass3(NormalClass3.VEGETAL);
		this.setClass4(NormalClass4.WOOD);
		this.setObjectNom(NormalDendroSample.DENDRO___SAMPLE);
		this.setObjectStatus(NormalObjectStatus.STORAGE_____INCOMING___LOAN);
		this.setStorageUnit(NormalStorageUnit.EA);
		
	}
	
	/**
	 * Extract the county from the Origin field
	 * 
	 * @return
	 */
	public String getCounty()
	{
		String origin = this.getOrigin();
		String[] parts = origin.split("__");
		if(parts.length==4)
		{
			return parts[1].trim();
		}
		
		log.error("Unable to extract County information from origin field from '"+this.getCatalogCode()+"'");

		return null;
		
	}
	
	/**
	 * Extract the State from the Origin field
	 * 
	 * @return
	 */
	public String getState()
	{
		String origin = this.getOrigin();
		String[] parts = origin.split("__");
		if(parts.length==4)
		{
			return parts[2].trim();
		}
		
		log.error("Unable to extract State information from origin field '"+this.getCatalogCode()+"'");
		
		return null;
		
	}
	
	/**
	 * Extract the Country from the Origin field
	 * 
	 * @return
	 */
	public String getCountry()
	{
		String origin = this.getOrigin();
		String[] parts = origin.split("__");
		if(parts.length==4)
		{
			return parts[3].trim();
		}
		
		log.error("Unable to extract Country information from origin field '"+this.getCatalogCode()+"'");

		return null;
		
	}
	
	public ControlledVoc getTaxon()
	{
		String taxon = this.getITRDBSpeciesCode();
		
		if(taxon!=null)
		{
			return ITRDBTaxonConverter.getControlledVocFromCode(taxon);
		}
		
		log.error("Unable to convert taxon field to true taxon");

		return null;
	}
	
	public DateTime getCatalogDateAsDateTime()
	{
		return DateUtils.parseDateTimeFromNaturalString(this.getCatalogDate());		
	}
	

	/**
	 * Parse the location field to return the box code
	 * 
	 * @return
	 */
	public String getBoxCode()
	{
		String location = this.getLocation();
		
		location = location.replace("U OF A LTRR/BOX ", "");
		location = location.replace("U OF A LTRR/BOX", "");
		
		return location;
		
	}
	
	/**
	 * Parse the 'ObjectPart' field to determine the Element type
	 * 
	 * @return
	 */
	public ControlledVoc getElementType()
	{
		String part = this.getObjectPart();
		
		
		
		return null;
	}
	
	
	/**
	 * TODO 
	 * 
	 * @return
	 */
	public String getObjectCode() throws Exception
	{
		String[] parts = getOtherNumberParts();
		
		if(parts==null) {
			throw new Exception("Failed to get object code for '"+this.getCatalogCode()+"' from OtherNumbers field ("+this.getOtherNumbers()+")");
			
		}
		
		if(parts.length==2 || parts.length==3)
		{
			return parts[0].trim();
		}
		
		throw new Exception("Failed to get object code for '"+this.getCatalogCode()+"' from OtherNumbers field ("+this.getOtherNumbers()+")");

	}
	
	public String getSampleCode()
	{
		String[] parts = getOtherNumberParts();
		
		if(parts==null) {
			return null;
		}
		
		if(parts.length==3)
		{
			return parts[2].trim();
		}
		

		return null;
	}
	
	private String[] getOtherNumberParts()
	{
		String str = this.getOtherNumbers();
		
		if(str==null || str.trim().length()==0) return null;
		
		str = str.replace("LTRR", "");
		str = str.replace("CATALOG", "");
		str = str.replace("LTRR CATALOG #: ", "");
		str = str.replace("#", "");
		str = str.replace(":", "");
		str = str.trim();
		
		String[] parts = str.split("-");
		
		if(parts.length>=2)
		{
			return parts;
		}
		
		
		return null;
	}
	
	/**
	 * TODO 
	 * 
	 * @return
	 */
	public String getElementCode()
	{
		String[] parts = getOtherNumberParts();
		if(parts==null) {
			return null;
		}

		if(parts.length==2 || parts.length==3)
		{
			return parts[1].trim();
		}
		
		return null;
	}
	
	public TridasObjectEx getTridasObject()
	{
		
		
		return null;
	}
	
	private TridasLocation getTridasLocationFromLatLon()
	{
		TridasLocation loc;
		String utm = this.getUTMCoords().trim();
		
		if(utm==null || utm.length()==0)
		{
			return null;
		} 
		
		String[] parts = utm.split("/");
		
		if(parts.length==3)
		{
			try{
				Integer zone = Integer.parseInt(parts[0].trim());
				Integer easting = Integer.parseInt(parts[1].trim());
				Integer northing = Integer.parseInt(parts[2].trim());
				UTMDatum datum = SpatialUtils.UTMDatum.NAD27;
				
				loc = SpatialUtils.getLocationGeometryFromUTM(datum, zone, easting, northing);
				if(loc!=null)
				{
					return loc;
				}
				else
				{
					log.error("Failed to project UTM string: "+utm);
					return null;
				}
				
			}
			catch (NumberFormatException e)
			{
				log.error("Failed to parse numbers from UTM string: "+utm);
				return null;
			}
		}
		else
		{
			log.error("Invalid number of parts in UTM string: "+utm);
			return null;
		}
		
	}
	
	
	
	public TridasLocation getTridasLocationFromUTM(UTMDatum datum)
	{
		TridasLocation loc;
		String utm = this.getUTMCoords().trim();
		
		if(utm==null || utm.length()==0)
		{
			return null;
		} 
		
		String[] parts = utm.split("/");
		
		if(parts.length==3)
		{
			try{
				Integer zone = Integer.parseInt(parts[0].trim());
				Integer easting = Integer.parseInt(parts[1].trim());
				Integer northing = Integer.parseInt(parts[2].trim());
			
				
				loc = SpatialUtils.getLocationGeometryFromUTM(datum, zone, easting, northing);
				if(loc!=null)
				{
					return loc;
				}
				else
				{
					log.error("Failed to project UTM string: "+utm);
					return null;
				}
				
			}
			catch (NumberFormatException e)
			{
				log.error("Failed to parse numbers from UTM string: "+utm);
				return null;
			}
		}
		else
		{
			log.error("Invalid number of parts in UTM string: "+utm);
			return null;
		}
		
	}
	
	public static List<RediscoveryExport> getICMSRecordsFromXMLFile(String filename, boolean logErrors)
	{
		StringBuilder fileString = new StringBuilder();
		StringReader reader;
		FileHelper fileHelper = new FileHelper();

		String[] argFileString = null;
		if (TridasIO.getReadingCharset() != null) {
			try {
				argFileString = fileHelper.loadStrings(filename,
						TridasIO.getReadingCharset());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (TridasIO.isCharsetDetection()) {
				argFileString = fileHelper
						.loadStringsFromDetectedCharset(filename);
			} else {
				argFileString = fileHelper.loadStrings(filename);
			}
		}

		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		URL file = IOUtils.getFileInJarURL("schemas/icms.xsd");
		Schema schema = null;
		if (file == null) {
			log.error(I18n.getText("Schema missing"));
			return null;
		} else {
			// Next try to load the schema to validate
			try {
				schema = factory.newSchema(file);
			} catch (Exception e) {
				log.error(I18n.getText("Schema missing",
						e.getLocalizedMessage()));
				return null;
			}

			// Build the string array into a FileReader
			Boolean firstLine = true;

			for (String s : argFileString) {
				if (firstLine) {
					fileString.append(s.replaceFirst("^[^<]*", "") + "\n");
					firstLine = false;
				} else {
					fileString.append(s + "\n");
				}
			}
			reader = new StringReader(fileString.toString());

			// Do the validation
			Validator validator = schema.newValidator();
			StreamSource source = new StreamSource();
			source.setReader(reader);
			try {
				MyErrorHandler eh = new MyErrorHandler();
				eh.logErrors= logErrors;
				validator.setFeature("http://xml.org/sax/features/validation",true);
				validator.setFeature("http://apache.org/xml/features/validation/schema",true);
				validator.setErrorHandler(eh);
				validator.validate(source);
				
				HashSet<Integer> errors = eh.getLineErrors();
				
				List<Integer> list = new ArrayList<Integer>(errors);
				Collections.sort(list);
				
				if(logErrors)
				{
					for(Integer i : list)
					{
						log.error("Error found on line "+i.toString());
					}
				}

			} catch (SAXException ex) {
				log.error(ex.getLocalizedMessage());
				return null;
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());

				return null;
			}
		}

		// All should be ok so now unmarshall to Java classes
		JAXBContext jc;
		reader = new StringReader(fileString.toString());
		try {
			jc = JAXBContext.newInstance("com.rediscov.schema");
			Unmarshaller u = jc.createUnmarshaller();
			// Read the file into the project

			Object root = u.unmarshal(reader);
			ArrayList<RediscoveryExport> lst = new ArrayList<RediscoveryExport>();
			
			if(root instanceof NewDataSet)
			{
				
				lst.addAll(((NewDataSet) root).getICMSRecord());
				
				//log.debug("Total of "+lst.size()+ " ICMS records read ");
				return lst;

			}
			
			
		} catch (Exception e){
			log.error(e.getLocalizedMessage());

			return null;
		}
		
		return null;
	}

	 private static class MyErrorHandler extends DefaultHandler {
		 
		 private HashMap<Integer, String> errors = new HashMap<Integer, String>();
		 private HashSet<Integer> lineerrs = new HashSet<Integer>();
		 public boolean logErrors = true;
		 
		 
	      public void warning(SAXParseException e) throws SAXException {
	        if(logErrors) log.warn(printInfo(e));
	      }
	      public void error(SAXParseException e) throws SAXException {
	    	  if(logErrors) log.error(printInfo(e));
	      }
	      public void fatalError(SAXParseException e) throws SAXException {
	    	  if(logErrors) log.error(printInfo(e));
	      }
	      private String printInfo(SAXParseException e) {
	    	  String msg = "\n";
	         msg+="   Line number  : "+e.getLineNumber()+"\n";
	         msg+="   Column number: "+e.getColumnNumber()+"\n";
	         msg+="   Message      : "+e.getMessage()+"\n";
	         
	         errors.put(e.getLineNumber(), e.getMessage());
	         lineerrs.add(e.getLineNumber());
	         return msg;
	      }
	      
	      public HashMap<Integer, String> getErrors()
	      {
	    	  return errors;
	      }
	      
	      public HashSet<Integer> getLineErrors()
	      {
	    	  return lineerrs;
	      }
	   }
	
}
