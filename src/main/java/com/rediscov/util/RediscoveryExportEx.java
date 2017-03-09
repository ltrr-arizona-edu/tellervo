package com.rediscov.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.I18n;
import org.tridas.io.TridasIO;
import org.tridas.io.util.DateUtils;
import org.tridas.io.util.FileHelper;
import org.tridas.io.util.IOUtils;
import org.tridas.io.util.ITRDBTaxonConverter;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.DateTime;
import org.tridas.schema.TridasLocation;
import org.tridas.spatial.SpatialUtils;
import org.tridas.spatial.SpatialUtils.UTMDatum;
import org.tridas.util.TridasObjectEx;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.rediscov.schema.NewDataSet;
import com.rediscov.schema.NormalClass1;
import com.rediscov.schema.NormalClass3;
import com.rediscov.schema.NormalClass4;
import com.rediscov.schema.NormalDendroSample;
import com.rediscov.schema.NormalObjectStatus;
import com.rediscov.schema.NormalStorageUnit;
import com.rediscov.schema.RediscoveryExport;
import com.rediscov.schema.StrBoolean;

import edu.emory.mathcs.backport.java.util.Collections;

/****************************************
 * FIELDS TO HANDLE - CULTURAL RECORDS
 * **************************************
 * CtrlProp 
 * - ICMS Form Location - Registration tab
 * - CONSTANT - N
 * - Whether sample is an item of value, firearm etc.  For us always 'n'.
 *   
 * Class_1
 * - ICMS Form Location - Registration tab 
 * - CONSTANT - ARCHEOLOGY
 * - Discipline.  For us always 'ARCHAEOLOGY'
 * 
 * Class_2 
 * - ICMS Form Location - Registration tab
 * - Autohandle through HistCultPer field
 *  One of Historic, Prehistoric of Unknown.  No clear distinction for this field given in help text.
 *  The three options in the dictionary though overlap entirely in meaning with HistCultPer.  I suggest
 *  we autopopulate this field from HistCultPer. But how to handle continuously used site?
 *  
 * Class_3 
 * - ICMS Form Location - Registration tab
 * - CONSTANT - VEGETAL
 * Always VEGETAL for us 
 *  
 * Class_4 
 * - ICMS Form Location - Registration tab
 * - CONSTANT - WOOD
 * Always WOOD for us
 *  
 * Object_NOM 
 * - ICMS Form Location - Registration tab
 * - CONSTANT - DENDRO SAMPLE
 * Always DENDRO SAMPLE for us, although various typos and variants in ICMS export. Standardise?
 *  
 * Catalog -> new field at sample level
 * - ICMS Form Location - Registration tab
 * A 3 part 12 character field.  
 *   - 1-4 = Alpha - four letter park code
 *   - 5 = Alpha - Single character A,B,C code denoting collection designation, or blank if NA.  
 *                 Always seems to be blank for us
 *   - 6-12 = Numeric - sequential numbers assigned to each 'record'
 * 
 * Accession -> New field at sample level?  or ignore?
 * - ICMS Form Location - Registration tab
 * A 3 part 10 character field
 *    - 1-4 = Alpha - four letter park code
 *    - 5 = always a hyphen!
 *    - 6-10 = Numeric - Five digit sequential number 
 * Jenna says... "The center # (center accession #) field is only present in ICMS for NPS regional centers 
 * (i.e. WACC). That's why we don't have it over at LTRR. You can ignore it." 
 * 
 * Alt_Name - IGNORE
 * - ICMS Form Location - Registration tab
 * Not filled in
 *  
 * Location -> Parsed -> Box name
 * - ICMS Form Location - Registration tab
 * "Enter the physical storage location of hte object, starting with the most general location. For example, enter
 * the building number or name, the room number, the cabinet number, and the shelf number.  For specimens stored
 * outside the park, enter the name of the institution."
 * This field can be parsed to determined box name.  Typical entry is:
 * U OF A LTRR/BOX 537
 *  
 * Object_Status - parse for sample.samplestatus (NS)
 * - ICMS Form Location - Registration tab 
 * "Enter the current status of the object by choosing an entry from the table"
 * According to Jenna this field should always be "STORAGE - INCOMING LOAN" but there are a number of variants in
 * the export.  An option for "MISSING" is include in the ICMS dictionary which we should probably utilise
 * 
 * Status_Date -> New field at sample level
 * - ICMS Form Location - Registration tab 
 * "Enter the 4-digit fiscal year for which the status applies" [presumably it means the 'object_status' field]
 * 
 * Item_Count
 * - ICMS Form Location - Registration tab
 * "Enter 1 for a single object, even if the object has component parts. Example:
 *   1 teapot with lid = 1 item
 *   100 sherds = 100 items"
 * Logically this should be 1 for all, but it's not.  Seems this could be used to help us work out where an
 * ICMS records represents multiple samples.   
 *  
 * Storage_Unit - Constant - EA
 * - ICMS Form Location - Registration tab
 * "Enter the storage unit for bulk objects e.g. BAG, BOX, LF"
 * Should be always EA for us
 * 
 * Description
 * - ICMS Form Location - Registration tab
 * "Enter the description of the object.  The description should provide enough information to identify the object 
 * from others.  Do not use unathorized abbreviations or codes."
 * Currently this field contains duplicate information from other fields in long hand e.g.
 *   LTRR CATALOG #: AZ-181
 *   SEQUENCE #:31B-55
 *   SPECIES: JUNIPER
 *   FORM: FRAGMENT
 *   DATE: NOT DATED
 * Field is not consistent enough to be automatically parsed.  How should we use? 
 * 
 * Artist_Maker - IGNORE
 * - ICMS Form Location - Catalog tab
 * Not relevant
 * 
 * Maint_Cycle - IGNORE
 * - ICMS Form Location - Catalog tab
 * Not relevant
 * 
 * Manufact_Date - IGNORE
 * - ICMS Form Location - Catalog tab
 * Not relevant
 * 
 * Material - IGNORE
 * - ICMS Form Location - Catalog tab
 * Not relevant
 * 
 * Use_Date - IGNORE
 * - ICMS Form Location - Catalog tab
 * Not relevant  
 * 
 * Measurements
 * - ICMS Form Location - Catalog tab
 * "Formatted Memo field.  The field will expand into four subfields: Dimensions, weight, volume, other. An 
 * underline seperates the subfield entries on the screen."
 * Could theoretically parse this to fit element dimensions, but I suspect they really mean sample dimensions?
 * Ignore? 
 * 
 * Other_Numbers -> External id (NS)
 * - ICMS Form Location - Catalog tab
 * "Record other numbers assigned to the object, such as catalog numbers from a previous owner.  If known, 
 * indicate a source for the other number.  Note: the archaeology specialty screen contains separate fields
 * for field specimen numbers and previous catalog numbers.
 *  
 * Condition -> New field at sample level?
 * - ICMS Form Location - Catalog tab
 * "Enter the condition of the object using one term from each of the two criteria groups:
 *  - Group I - COM (complete); INC (incomplete); FRG (fragment)
 *  - Group II - EX (excellent); GD (good); FR (fair); PR (poor)"
 * Overlaps somewhat with the way LTRR has used the PARTS field.  Also a good deal of non-standard terms
 * in export. 
 * 
 * Condition_Desc IGNORE
 * - ICMS Form Location - Catalog tab
 * Not used
 * 
 * Cataloger -> New field at sample level?
 * - ICMS Form Location - Catalog tab
 * "Enter the full name, last name first, of the person who cataloged the object."
 * Not consistently used in export. * 
 * 
 * Catalog_Date -> Sample.createdTimestamp?
 * - ICMS Form Location - Catalog tab
 * "Enter the numeric month, day and full year the object was cataloged"
 * Seems a good match for sample.createdtimestamp, but this is not overridable by the user.
 * 
 * Identified_By -> New field? Ignore?
 * - ICMS Form Location - Catalog tab
 * "Enter the name of person, last name first, who identified the object"
 * Not clear what 'identified' means in terms of dendro samples?  Ignore?
 * 
 * Ident_Date -> New field? Ignore?
 * - ICMS Form Location - Catalog tab
 * "Enter the date of identification"
 * Ignore?
 * 
 * Field_Site -> New field at sample level
 * - ICMS Form Location - Prof/Manf tab
 * "Record any field site number that the investigator assigned to the archaeological site.  This is
 * the site from which the object was originally recovered.  Do not record the state site number
 * in this field"
 * 
 * State_Site -> New field
 * - ICMS Form Location - Prof/Manf tab
 * "Enter the number assigned to the site within the relevant state archaeological inventory This is 
 * the site from which the object was originally recovered.  If a state site number has not be 
 * assigned, enter UNASSIGNED"
 * 
 * Site_Name -> Object.title
 * - ICMS Form Location - Prof/Manf tab
 * "For archaeology, enter the distinctive name of the location where the material was collected"
 *  
 * Within_Site -> Element.location.description?
 * - ICMS Form Location - Prof/Manf tab
 * "For archaeology, enter the specific within site provenience of the object.  Example: P5, L M7, D 25"
 * 
 * Origin -> parsed -> object.location.address.addressLine2, stateProvinceRegion, country
 * - ICMS Form Location - Prof/Manf tab
 * "Repeating formatted memo field.  The field will expand into four repeatable subfields: City,
 * County; State; and Country... Multiple terms are separated by double underscores(__), rows are 
 * separated by columns (||)"
 * City never seems to be used in our circumstances.  TRiDaS doesn't have a 'county' field as it
 * is largely US specific, but we can used addressLine2 for this.
 * 
 * UTM_Z_E_N  -> object.location.locationGeometry
 * - ICMS Form Location - Prof/Manf tab
 * "Enter the UTM coordinates for the collection site, if the collector provides these data."
 * The entry is limited to digits, and should be formatted like this:
 *  05/291000/4264000 for UTM zone 5 291000E, 4264000N.  There is a separate datum field but
 *  this is not included in WACC's export.  Not indication of which datum to use, so must assume
 *  it's variable. Tellervo can project these into standard LatLon WGS84 but there will be 
 *  error if we pick the wrong datum.  Also how to handle conflicting coordinates with LatLon?
 * 
 * Lat_LongN_W  -> object.location.locationGeometry
 * - ICMS Form Location - Prof/Manf tab
 * "Enter the standard latitude and longitude for the collection site, if the collector provides 
 * these data".
 * Field is lightly formatted but is largely a mess.  Mix of DMS, DD.  Tellervo/DendroFileIO 
 * spatial parsing will do it's best to extract meaningful info.
 * 
 * Hist_Cult_Per -> New field
 * - ICMS Form Location - Prof/Manf tab
 * "Historic/Cultural Period.  Enter a distinctive stylistic or historical period.  
 * Example: Colonial Pueblo III"
 * This field can hold multiple periods.  Dictionary specified (but not included in my version).  
 * Various non-dictionary terms used in export  
 * 
 * Cultural_ID -> New field
 * - ICMS Form Location - Prof/Manf tab
 * "Enter the cultural affiliation of the material or the person(s) or group who manufactured
 * the object.  Examples: Pennsylvania Dutch; Pima; Anasazi."
 * 
 * Cult_of_Use - IGNORE
 * - ICMS Form Location - Prof/Manf tab
 * Not used
 * 
 * NAGPRA - IGNORE
 * - ICMS Form Location - Prof/Manf tab
 * Not used
 * 
 * Place_of_Manuf - IGNORE
 * - ICMS Form Location - Prof/Manf tab
 * Not used 
 * 
 * Fld_Specimen -> New field? or concatenate Object-Element-Sample codes?
 * - ICMS Form Location - Archaeology tab
 * "Enter the field specimen number(s) assigned to the object.  If known, indicate
 * the source of the field specimen number by giving the name of the person who 
 * assigned it"
 * 
 * Type_Name - IGNORE
 * - ICMS Form Location - Prof/Manf tab
 * Not used
 * 
 * Collector -> sample.sampledBy (NS) and sample.samplingDate
 * - ICMS Form Location - Archaeology tab
 * "The field will expand into two subfields: collector and collection date. An underline
 * seperates the subfield entries on the screen.  Collector: enter the full name of the
 * person, lastname, first, who collected the material.  Collection date: flexible 
 * date field. Enter the date on which the collector collected the material"
 * 
 * Parts
 * - ICMS Form Location - Archaeology tab
 * "Enter the appropriate term for the part of the object that is present e.g. lid, handle, rim, lock (gun)"
 * The dictionary created by LTRR staff contains a mix of sample.type and element.shape. 
 * e.g. 5/8 IN CORE; 7/8 IN CORE; FRAG; CHCL FRAG; BE.  
 * Another complication is the inclusion of multiple samples
 * e.g.  1 IN CORE, XS 
 * NB NOT INCLUDED IN WACC EXPORT!
 * 
 * Color - IGNORE
 * - ICMS Form Location - Archaeology tab
 * Not relevant 
 * 
 * ***************
 * USER fields defined by LTRR 
 * 
 * User_1 (inner ring date)
 * User_2 (inner-ring code)
 * User_3 (outer ring date)
 * User_4 (outer ring code)
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
