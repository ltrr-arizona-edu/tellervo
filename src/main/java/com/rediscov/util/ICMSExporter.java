package com.rediscov.util;

import gov.nasa.worldwind.geom.Position;

import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.TridasMarker;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tridas.io.I18n;
import org.tridas.io.util.DateUtils;
import org.tridas.io.util.FileHelper;
import org.tridas.io.util.IOUtils;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.spatial.GMLPointSRSHandler;
import org.tridas.spatial.SpatialUtils;
import org.xml.sax.SAXException;

import com.rediscov.schema.NewDataSet;
import com.rediscov.schema.NormalClass1;
import com.rediscov.schema.NormalClass2;
import com.rediscov.schema.NormalClass3;
import com.rediscov.schema.NormalClass4;
import com.rediscov.schema.NormalCondition;
import com.rediscov.schema.NormalDendroSample;
import com.rediscov.schema.NormalObjectStatus;
import com.rediscov.schema.NormalStorageUnit;
import com.rediscov.schema.RediscoveryExport;
import com.rediscov.schema.StrBoolean;

public class ICMSExporter {
	protected final static Logger log = LoggerFactory.getLogger(ICMSExporter.class);

	private TridasObject object;
	private File file;
	
	public ICMSExporter(TridasObject o, File file)
	{
		setTridasObject(o);
		setOutputFile(file);
	}
	
	public void setTridasObject(TridasObject o)
	{
		this.object = o;
	}
	
	public void setOutputFile(File file)
	{
		this.file=file;
	}
		
	public void saveToDisk() throws Exception
	{
		
		ArrayList<RediscoveryExport> records  = (ArrayList<RediscoveryExport>) getRecordsForObject(this.object);
		
		if(records==null || records.size()==0)
		{
			throw new Exception("No records to export");
		}
		
			
		try {
			String[] xmlstr = saveToString(records);

			FileHelper helper = new FileHelper();
			helper.saveStrings(file.getAbsolutePath(), xmlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert.error("Failed", "Failed to export to ICMS XML file.  Check logs of details.");
		}
		
		
	}
	
	private static List<RediscoveryExport> getRecordsForObject(TridasObject object)
	{
		ArrayList<RediscoveryExport> records = new ArrayList<RediscoveryExport>();
		
		// we want an object return here, so we get a list of object->elements->samples when we use comprehensive
    	SearchParameters sampparam = new SearchParameters(SearchReturnObject.SAMPLE);
    	sampparam.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, object.getIdentifier().getValue().toString());
		EntitySearchResource<TridasObject> sampresource = new EntitySearchResource<TridasObject>(sampparam, TridasObject.class);
		sampresource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(sampresource);
		sampresource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			return null;
		}
		
		List<TridasObject> objList = sampresource.getAssociatedResult();
		
		if(objList==null || objList.size()==0) return null;
				
		for(TridasObject o : objList)
		{
			records.addAll(recursingAdd(o));
		}
				
		return records;
	}
	
	private static  List<RediscoveryExport> recursingAdd(TridasObject o)
	{
		ArrayList<RediscoveryExport> records = new  ArrayList<RediscoveryExport>();
		
		if(o.isSetObjects())
		{
			for(TridasObject o2 : o.getObjects())
			{
				records.addAll(recursingAdd(o2));
			}
		}
		
		if(o.isSetElements())
		{
			for(TridasElement e : o.getElements())
			{
				records.addAll(getRecords(o, e));
			}
		}
		
		return records;
		
	}
	
	private static List<RediscoveryExport> getRecords(TridasObject o, TridasElement e)
	{
		ArrayList<RediscoveryExport> records = new ArrayList<RediscoveryExport>();
		
		if(!o.isSetElements()) return null;
		
		for(TridasSample s : e.getSamples())
		{
			RediscoveryExportEx rec = new RediscoveryExportEx();
			
			// Constants
			rec.setCtrlProp(StrBoolean.N);
			rec.setClass1(NormalClass1.ARCHEOLOGY);
			rec.setClass3(NormalClass3.VEGETAL);
			rec.setClass4(NormalClass4.WOOD);
			rec.setObjectNom(NormalDendroSample.DENDRO___SAMPLE);
			rec.setObjectStatus(NormalObjectStatus.STORAGE_____INCOMING___LOAN);
			rec.setStorageUnit(NormalStorageUnit.EA);
			rec.setCondition(NormalCondition.COM___GD);

			// TRiDaS/Tellervo fields
			rec.setDescription(s.getDescription());
			rec.setSiteName(o.getTitle());
			rec.setObjectPart(s.getType().getValue());
			rec.setITRDBSpeciesCode(e.getTaxon().getNormal());
			rec.setOtherNumbers(TridasUtils.getGenericFieldValueByName(s, "tellervo.externalID"));

			// ICMS specific fields
			rec.setAccessionCode(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.ACCESSION_CODE));
			rec.setBarkCode(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.OUTER_CODE));
			rec.setCatalogCode(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.CATALOG_CODE));
			rec.setCulturalID(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.CULTURAL_ID));
			rec.setFieldSite(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.FIELD_SITE));
			rec.setFldSpecimen(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.FIELD_SPECIMEN));
			rec.setHistCultPer(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.HIST_CULT_PER));
			rec.setCataloger(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.CATALOGER));
			rec.setIdentDate(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.IDENT_DATE));
			rec.setStateSite(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.STATE_SITE));
			rec.setPithCode(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.INNER_CODE));
			
			
			// Unused fields - output as empty elements
			rec.setMeasurements("");
			rec.setUTMCoords("");
			rec.setParts("");
			
			// Fields that require casting
			if(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.STATUS_DATE)!="")
			{
				try{
					rec.setStatusDate(BigInteger.valueOf(Integer.parseInt(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.STATUS_DATE))));
				} catch (NumberFormatException ex)
				{
					rec.setStatusDate(null);
					log.debug("Failed to convert status date ("+TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.STATUS_DATE)+") to a BigInteger value");
				}
			}
			else
			{
				log.debug("Failed to convert status dat ("+TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.STATUS_DATE)+") to a BigInteger value");				
				rec.setStatusDate(null);
			}
			
			if(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.FIRST_YEAR)!="")
			{
				try{
					rec.setInnerRingDate(BigInteger.valueOf(Integer.parseInt(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.FIRST_YEAR))));
				} catch (NumberFormatException ex)
				{
					rec.setInnerRingDate(BigInteger.ZERO);
					log.debug("Failed to convert inner ring date ("+TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.FIRST_YEAR)+") to a BigInteger value");
				}
			}
			else
			{
				log.debug("Failed to convert inner ring date ("+TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.FIRST_YEAR)+") to a BigInteger value");				
				rec.setInnerRingDate(BigInteger.ZERO);
			}
			
			if(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.ITEM_COUNT)!="")
			{
				try{
				rec.setItemCount(BigDecimal.valueOf(Integer.parseInt(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.ITEM_COUNT))));
				} catch (NumberFormatException ex)
				{
					rec.setItemCount(BigDecimal.ZERO);
					log.debug("Failed to convert item count ("+TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.ITEM_COUNT)+") to a BigDecimal value");

				}
			}
			else
			{
				log.debug("Failed to convert item count ("+TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.ITEM_COUNT)+") to a BigDecimal value");
				rec.setItemCount(BigDecimal.ZERO);
			}
			
			if(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.LAST_YEAR)!="")
			{
				try{
				rec.setOuterRingDate(BigInteger.valueOf(Integer.parseInt(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.LAST_YEAR))));
				} catch (NumberFormatException ex)
				{
					rec.setOuterRingDate(BigInteger.ZERO);
					log.debug("Failed to convert outer ring date ("+TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.LAST_YEAR)+") to a BigInteger value");
				}
			}
			else
			{
				log.debug("Failed to convert outer ring date ("+TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.LAST_YEAR)+") to a BigInteger value");
				rec.setOuterRingDate(BigInteger.ZERO);
			}
			


			rec.setOrigin(" ");
			if(e.isSetLocation())
			{
				TridasLocation loc = e.getLocation();
				
				if(loc.isSetLocationComment()) rec.setWithinSite(e.getLocation().getLocationComment());
				
				if(loc.isSetLocationGeometry() && loc.getLocationGeometry().isSetPoint())
				{			
					GMLPointSRSHandler tph = new GMLPointSRSHandler(loc.getLocationGeometry().getPoint());
					if(tph.getWGS84LatCoord()!=null && tph.getWGS84LongCoord()!=null)
					{
						String coords = tph.getWGS84LatCoord() +"/"+tph.getWGS84LongCoord();
						rec.setLatLonCoords(coords);
					}
				}
				
				if(loc.isSetAddress())
				{
					TridasAddress add = loc.getAddress();				
					String origin = "";
					if(add.isSetCityOrTown())
					{
						origin += add.getCityOrTown();
					}
					origin+="__";
					
					if(add.isSetAddressLine2())
					{
						origin += add.getAddressLine2();
					}
					origin+="__";					
					
					if(add.isSetStateProvinceRegion())
					{
						origin += add.getStateProvinceRegion();
					}
					origin+="__";					
					
					if(add.isSetCountry())
					{
						origin += add.getCountry();
					}
					rec.setOrigin(origin);	
				}
			}

			if(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.CATALOG_OVERRIDE_DATE)!="")
			{
				rec.setCatalogDate(TridasUtils.getGenericFieldValueByName(s, RediscoveryExportEx.CATALOG_OVERRIDE_DATE));
			}
			else
			{
				rec.setCatalogDate(DateUtils.getFormattedDateTime(s.getCreatedTimestamp(), new SimpleDateFormat("mm/dd/yyyy")));
			}
			

			
			
			// TODO
			rec.setClass2(NormalClass2.UNKNOWN); 
			rec.setCollector("");
			rec.setIdentifiedBy("");
			rec.setLocation("");
			
			
			
			
			records.add(rec);
		}

		
		return records;
	}
	
	private String[] saveToString(List<RediscoveryExport> records) throws Exception{
		
		NewDataSet dataset  = new NewDataSet();
		dataset.getICMSRecord().addAll(records);				
		
		// Validate output against schema first
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL file = IOUtils.getFileInJarURL("schemas/icms.xsd");
		Schema schema = null;
		
		if(file == null){
			log.error("Could not find schema file");
		}else{
			try {
				schema = factory.newSchema(file);
			} catch (SAXException e) {
				log.error("Error getting ICMS schema for validation, not using.", e);
				throw new Exception(I18n.getText("fileio.errorGettingSchema"));
			}
		}
		
		StringWriter swriter = new StringWriter();

		// Marshaller code goes here...
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance("com.rediscov.schema");
			Marshaller m = jc.createMarshaller();
			if (schema != null) {
				m.setSchema(schema);
			}
			m.marshal(dataset, swriter);

		} catch (Exception e) {
			log.error("Jaxb error", e);
			
			String cause = e.getCause().getMessage();
			if(cause!=null)
			{
				throw new Exception(I18n.getText("fileio.jaxbError")+ " " + cause);
			}
			else
			{
				throw new Exception(I18n.getText("fileio.jaxbError"));
			}

		}
		

		return swriter.getBuffer().toString().split("\n");
		
	}
	
	public static void exportObject()
	{
		File file = null;
		TridasObject object = null;
		
		
		// Get object to export
		TridasEntityPickerDialog dialog = new TridasEntityPickerDialog();
		object = (TridasObject) dialog.pickSpecificEntity(App.mainWindow, "Choose object to export", TridasObject.class);
		if(object==null) return;
		
		
		// Get output file
		File lastFolder = null;
		try{
			lastFolder = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
		} catch (Exception e){}
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML file", "xml");
		JFileChooser fc = new JFileChooser(lastFolder);
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showOpenDialog(App.mainWindow);
			
		// Get details from user
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	        
	    	file = fc.getSelectedFile();
	    		        
			// Remember this folder for next time
			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, file.getPath());
					    
	    } else {
	    	return;
	    }
	    
	    
	    
		
		try {
			ICMSExporter exporter = new ICMSExporter(object, file);
			exporter.saveToDisk();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
}
