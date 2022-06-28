package org.tdwg;

import java.io.File;
import java.io.StringWriter;
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

import org.purl.dc.elements._1.SimpleLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tdwg.rs.dwc.xsd.simpledarwincore.SimpleDarwinRecord;
import org.tdwg.rs.dwc.xsd.simpledarwincore.SimpleDarwinRecordSet;
import org.tellervo.desktop.core.App;
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
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.xml.sax.SAXException;

import com.rediscov.util.ICMSExporter;

public class DWCExporter {
	protected final static Logger log = LoggerFactory.getLogger(ICMSExporter.class);

	private TridasObject object;
	private File file;
	
	public DWCExporter(TridasObject o, File file)
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
		
		ArrayList<SimpleDarwinRecord> records  = (ArrayList<SimpleDarwinRecord>) getRecordsForObject(this.object);
		
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
	
	private static List<SimpleDarwinRecord> getRecordsForObject(TridasObject object)
	{
		ArrayList<SimpleDarwinRecord> records = new ArrayList<SimpleDarwinRecord>();
		
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
	
	private static  List<SimpleDarwinRecord> recursingAdd(TridasObject o)
	{
		ArrayList<SimpleDarwinRecord> records = new  ArrayList<SimpleDarwinRecord>();
		
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
	
	private static SimpleLiteral getSimpleLiteral(String s)
	{
		SimpleLiteral sl = new SimpleLiteral();
		ArrayList<String> al = new ArrayList<String>();
		al.add(s);
		
		return sl;
	}
	
	private static List<SimpleDarwinRecord> getRecords(TridasObject o, TridasElement e)
	{
		ArrayList<SimpleDarwinRecord> records = new ArrayList<SimpleDarwinRecord>();
		
		if(!o.isSetElements()) return null;
		
		for(TridasSample s : e.getSamples())
		{
			SimpleDarwinRecord rec = new SimpleDarwinRecord();
			
			
			rec.setIdentificationID(s.getIdentifier().getValue());
			

			//TODO
			// Many fields to add here!
			rec.setModified(getSimpleLiteral(DateUtils.getFormattedDateTime(s.getLastModifiedTimestamp(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.Z"))));
			rec.setLanguage(getSimpleLiteral("en"));
			rec.setScientificName(e.getTaxon().getNormal());
			
			records.add(rec);
		}

		
		return records;
	}
	
	private String[] saveToString(List<SimpleDarwinRecord> records) throws Exception{
		
		SimpleDarwinRecordSet dataset = new SimpleDarwinRecordSet();
		dataset.setSimpleDarwinRecords(records);
		
		// Validate output against schema first
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL file = IOUtils.getFileInJarURL("schemas/tdwg_dwc_simple.xsd");
		Schema schema = null;
		
		if(file == null){
			log.error("Could not find schema file");
		}else{
			try {
				schema = factory.newSchema(file);
			} catch (SAXException e) {
				log.error("Error getting TDWG Simple Darwin Core schema for validation, not using.", e);
				throw new Exception(I18n.getText("fileio.errorGettingSchema"));
			}
		}
		
		StringWriter swriter = new StringWriter();

		// Marshaller code goes here...
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance("org.tdwg.rs.dwc.xsd.simpledarwincore");
			Marshaller m = jc.createMarshaller();
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new DWCNamespacePrefixMapper());

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
			DWCExporter exporter = new DWCExporter(object, file);
			exporter.saveToDisk();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
