package com.rediscov.util;

import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.seriesidentity.IdentifySeriesPanel;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.util.labels.ui.SampleLabelPrintingUI;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.CurationStatus;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIBox;
import org.tridas.io.I18n;
import org.tridas.io.exceptions.ImpossibleConversionException;
import org.tridas.io.formats.heidelberg.HeidelbergToTridasDefaults.DefaultFields;
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
	
		if(objList.get(0).isSetElements())
		{
			for(TridasElement e : objList.get(0).getElements())
			{
				records.addAll(getRecords(object, e));
			}
		}
			
		for(TridasObject o : objList.get(0).getObjects())
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
			WSIBox box = App.dictionary.getBoxByID(TridasUtils.getGenericFieldByName(s, "tellervo.boxID").getValue());
			
			RediscoveryExportEx rec = new RediscoveryExportEx();
			
			rec.setConstantFields();
			rec.setAccessionCode(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.ACCESSION_CODE).getValue());
			rec.setBarkCode(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.OUTER_CODE).getValue());
			rec.setCatalogCode(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.CATALOG_CODE).getValue());
			rec.setCataloger(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.CATALOGER).getValue());
			rec.setCulturalID(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.CULTURAL_ID).getValue());
			rec.setFieldSite(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.FIELD_SITE).getValue());
			rec.setFldSpecimen(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.FIELD_SPECIMEN).getValue());
			rec.setHistCultPer(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.HIST_CULT_PER).getValue());
			rec.setInnerRingDate(BigInteger.valueOf(Integer.parseInt(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.FIRST_YEAR).getValue())));
			rec.setOuterRingDate(BigInteger.valueOf(Integer.parseInt(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.LAST_YEAR).getValue())));
			rec.setItemCount(BigDecimal.valueOf(Integer.parseInt(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.ITEM_COUNT).getValue())));
			rec.setStateSite(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.STATE_SITE).getValue());
			rec.setPithCode(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.INNER_CODE).getValue());
			rec.setIdentifiedBy(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.IDENTIFIED_BY).getValue());
			rec.setIdentDate(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.IDENT_DATE).getValue());
			rec.setStatusDate(BigInteger.valueOf(Integer.parseInt(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.STATUS_DATE).getValue())));
			rec.setOtherNumbers(TridasUtils.getGenericFieldByName(s, "tellervo.externalID").getValue());
					

			if(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.CATALOG_OVERRIDE_DATE).getValue()!=null)
			{
				rec.setCatalogDate(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.CATALOG_OVERRIDE_DATE).getValue());
			}
			else
			{
				rec.setCatalogDate(DateUtils.getFormattedDateTime(s.getCreatedTimestamp(), new SimpleDateFormat("mm/dd/yyyy 12:00:00")));
			}
			
			if(e.isSetLocation())
			{
				TridasLocation loc = e.getLocation();
				
				if(loc.isSetLocationComment()) rec.setWithinSite(e.getLocation().getLocationComment());
				
				if(loc.isSetLocationGeometry())
				{
					rec.setLatLonCoords(" ");
					rec.setUTMCoords(" ");
					
					try{
						
						GMLPointSRSHandler tph = new GMLPointSRSHandler(loc.getLocationGeometry().getPoint());
						
						if(tph.hasPointData())
						{
							rec.setLatLonCoords(tph.getWGS84LatCoord()+" "+tph.getWGS84LongCoord());
						}
					
					} catch (Exception ex){	}
					
				}
				
				if(loc.isSetAddress())
				{
					TridasAddress add = loc.getAddress();
					
					String origin="";
					if(add.isSetCityOrTown()){
						origin = origin+add.getCityOrTown().toUpperCase();
					}
					origin = origin+"__";
					
					if(add.isSetAddressLine2()){
						origin = origin+add.getAddressLine2().toUpperCase();
					}
					origin = origin+"__";
					
					if(add.isSetStateProvinceRegion()){
						origin = origin+add.getStateProvinceRegion().toUpperCase();
					}
					origin = origin+"__";
					
					if(add.isSetCountry()){
						origin = origin+add.getCountry().toUpperCase();
					}
					rec.setOrigin(origin);	
				}
			}
			rec.setDescription(s.getDescription());
			rec.setSiteName(o.getTitle());
			rec.setITRDBSpeciesCode(e.getTaxon().getNormal());
			rec.setObjectPart(s.getType().getNormal());
			
			if(box!=null)
			{
				rec.setLocation("U OF A LTRR/BOX "+box.getTitle());
			}
			
			String curationstatus = TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.ACCESSION_CODE).getValue();
			if(curationstatus==null)
			{
				rec.setObjectStatus(NormalObjectStatus.STORAGE_____INCOMING___LOAN);
			}
			else if (curationstatus.equals(CurationStatus.DESTROYED))
			{
				rec.setObjectStatus(NormalObjectStatus.DEACC_____DESTRUCTIVE___ANALYSIS);
			}
			else if (curationstatus.equals(CurationStatus.MISSING))
			{
				rec.setObjectStatus(NormalObjectStatus.MISSING);
			}
			else if (curationstatus.equals(CurationStatus.ON___DISPLAY))
			{
				rec.setObjectStatus(NormalObjectStatus.EXHIBIT_____INCOMING___LOAN);
			}
			else if (curationstatus.equals(CurationStatus.RETURNED___TO___OWNER))
			{
				rec.setObjectStatus(NormalObjectStatus.DEACC_____RETURN___TO___RIGHTFUL___OWNER);
			}
			else if (curationstatus.equals(CurationStatus.ACTIVE___RESEARCH))
			{
				rec.setObjectStatus(NormalObjectStatus.STORAGE_____INCOMING___LOAN);
			}
			else if (curationstatus.equals(CurationStatus.ON___LOAN))
			{
				rec.setObjectStatus(NormalObjectStatus.STORAGE_____INCOMING___LOAN);
			}
			else 
			{
				rec.setObjectStatus(NormalObjectStatus.STORAGE_____INCOMING___LOAN);
			}

			
			String collectorstr = "";
			// collector name
			collectorstr = collectorstr+"__";
			if(s.getSamplingDate()!=null)
			{
				collectorstr = collectorstr+DateUtils.getFormattedDate(s.getSamplingDate(), new SimpleDateFormat("mm/dd/yyyy"));
			}
			rec.setCollector(collectorstr);
			
			
	
			
			
			
			
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
