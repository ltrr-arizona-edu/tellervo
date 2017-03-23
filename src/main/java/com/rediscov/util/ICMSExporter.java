package com.rediscov.util;

import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
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
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIBox;
import org.tridas.io.I18n;
import org.tridas.io.exceptions.ImpossibleConversionException;
import org.tridas.io.util.FileHelper;
import org.tridas.io.util.IOUtils;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
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
			RediscoveryExportEx rec = new RediscoveryExportEx();
			
			rec.setCtrlProp(StrBoolean.N);
			rec.setClass1(NormalClass1.ARCHEOLOGY);
			rec.setClass3(NormalClass3.VEGETAL);
			rec.setClass4(NormalClass4.WOOD);
			rec.setObjectNom(NormalDendroSample.DENDRO___SAMPLE);
			rec.setObjectStatus(NormalObjectStatus.STORAGE_____INCOMING___LOAN);
			rec.setStorageUnit(NormalStorageUnit.EA);
			rec.setCondition(NormalCondition.COM___GD);
			rec.setAccessionCode(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.ACCESSION_CODE).getValue());
			rec.setBarkCode(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.OUTER_CODE).getValue());
			rec.setCatalogCode(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.CATALOG_CODE).getValue());
			rec.setCulturalID(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.CULTURAL_ID).getValue());
			rec.setFieldSite(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.FIELD_SITE).getValue());
			rec.setFldSpecimen(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.FIELD_SPECIMEN).getValue());
			rec.setHistCultPer(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.HIST_CULT_PER).getValue());
			rec.setInnerRingDate(BigInteger.valueOf(Integer.parseInt(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.FIRST_YEAR).getValue())));
			rec.setItemCount(BigDecimal.valueOf(Integer.parseInt(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.ITEM_COUNT).getValue())));
			rec.setStateSite(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.STATE_SITE).getValue());
			rec.setPithCode(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.INNER_CODE).getValue());
			rec.setOuterRingDate(BigInteger.valueOf(Integer.parseInt(TridasUtils.getGenericFieldByName(s, RediscoveryExportEx.LAST_YEAR).getValue())));

			if(e.isSetLocation())
			{
				TridasLocation loc = e.getLocation();
				
				if(loc.isSetLocationComment()) rec.setWithinSite(e.getLocation().getLocationComment());
				
				if(loc.isSetLocationGeometry())
				{
					
				}
				
				if(loc.isSetAddress())
				{
					TridasAddress add = loc.getAddress();
					
					String origin=add.getCityOrTown()+"__"+add.getAddressLine2()+"__"+add.getStateProvinceRegion()+"__"+add.getCountry();
					rec.setOrigin(origin);	
				}
			}
			rec.setDescription(s.getDescription());
			rec.setSiteName(o.getTitle());
			rec.setITRDBSpeciesCode(e.getTaxon().getNormal());

			
			
			rec.setCatalogDate("1");
			rec.setCataloger("1");
			rec.setClass2(NormalClass2.UNKNOWN); 
			rec.setCollector("1");
			rec.setIdentDate("1");
			rec.setIdentifiedBy("1");
			rec.setLatLonCoords("1");
			rec.setLocation("1");
			rec.setMeasurements("1");
			rec.setObjectPart("1");
			rec.setOtherNumbers("1");
			rec.setParts("1");
			rec.setStatusDate(BigInteger.ONE);
			rec.setUTMCoords("1");
			
			
			
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
