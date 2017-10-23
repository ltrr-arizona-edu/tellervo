package com.rediscov.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.commons.lang.WordUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.DictionaryUtil;
import org.tellervo.desktop.util.TridasManipUtil;
import org.tellervo.desktop.wsi.Resource;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;
import org.tellervo.desktop.wsi.tellervo.FriendlyExceptionTranslator;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoAssociatedResource;
import org.tellervo.desktop.wsi.tellervo.TellervoEntityAssociatedResource;
import org.tellervo.desktop.wsi.tellervo.TellervoMultiResourceAccessPanel;
import org.tellervo.desktop.wsi.tellervo.TellervoResource;
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
import org.tellervo.schema.WSIEntity;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.schema.WSIUserDefinedField;
import org.tridas.io.util.DateUtils;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.MVCArrayList;
import com.rediscov.schema.RediscoveryExport;

public class ICMSImporter implements PropertyChangeListener{

	List<RediscoveryExport> records;
	protected final static Logger log = LoggerFactory.getLogger(ICMSImporter.class);

	HashMap<String, TridasProject> projectHash = new HashMap<String, TridasProject>();
	HashMap<String, UUID> objectHash = new HashMap<String, UUID>();
	HashMap<String, UUID> elementHash = new HashMap<String, UUID>();
	HashMap<String, UUID> sampleHash = new HashMap<String, UUID>();
	ControlledVoc defaultObjectType;
	ControlledVoc defaultElementType;
	ControlledVoc defaultTaxon;
	TellervoMultiResourceAccessPanel progress;
	JDialog d;
	

	private String filename;
	
	
	/**
	 * Indicates whether the user defined fields required for importing ICMS data are present
	 * in this database or not.
	 * 
	 * @return
	 */
	public static boolean isDatabaseICMSCapable()
	{
		
		String[] icmsfieldnames = {
				RediscoveryExportEx.ITEM_COUNT,
				RediscoveryExportEx.CATALOG_CODE,
				RediscoveryExportEx.ACCESSION_CODE,
				RediscoveryExportEx.STATUS_DATE,
				RediscoveryExportEx.CATALOGER,
				RediscoveryExportEx.CATALOG_OVERRIDE_DATE,
				RediscoveryExportEx.FIELD_SITE,
				RediscoveryExportEx.STATE_SITE,
				RediscoveryExportEx.HIST_CULT_PER,
				RediscoveryExportEx.CULTURAL_ID,
				RediscoveryExportEx.FIELD_SPECIMEN,
				RediscoveryExportEx.OUTER_CODE,
				RediscoveryExportEx.INNER_CODE,
				RediscoveryExportEx.BARK_YEAR,
				RediscoveryExportEx.FIRST_YEAR,
				RediscoveryExportEx.LAST_RING_UNDER_BARK,
				RediscoveryExportEx.LAST_YEAR,
				RediscoveryExportEx.PITH_PRESENT,
				RediscoveryExportEx.PITH_YEAR,
		};
		
		for(String fieldname : icmsfieldnames)
		{
			if(doesDatabaseContainUserDefinedField(fieldname)==false)
			{
				log.debug("Database doesn't include the user defined field "+fieldname+" so ICMS features are unavailable");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Indicates whether the user defined field with the specified name is present in this database
	 * 
	 * @param fieldname
	 * @return
	 */
	private static boolean doesDatabaseContainUserDefinedField(String fieldname)
	{
		MVCArrayList<WSIUserDefinedField> fields = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");

		for(WSIUserDefinedField field : fields)
		{
			if(field.getName().equals(fieldname)) return true;
		}

		return false;
		
	}
	
	public ICMSImporter(String filename)
	{
		this.filename = filename;
		
		List<ControlledVoc> dictItems = Dictionary.getMutableDictionary("objectTypeDictionary");	
		for(ControlledVoc item : dictItems)
		{
			if(item.getNormal().equals("Site"))
			{
				defaultObjectType = item;
			}
		}		
		
		dictItems = Dictionary.getMutableDictionary("elementTypeDictionary");	
		for(ControlledVoc item : dictItems)
		{
			if(item.getNormal().equals("Unknown"))
			{
				defaultElementType = item;
			}
		}
		
		dictItems = Dictionary.getMutableDictionary("taxonDictionary");	
		for(ControlledVoc item : dictItems)
		{
			if(item.getNormal().equals("Plantae"))
			{
				defaultTaxon = item;
			}
		}	
	}
	
	public void doImport()
	{

		

		
		d = new JDialog();
		d.setIconImage(Builder.getApplicationIcon());
		d.setTitle("Import ICMS Records");
		d.setLayout(new BorderLayout());
		
		progress = new TellervoMultiResourceAccessPanel(100);
		
		d.add(progress, BorderLayout.CENTER);
		d.pack();
		d.setSize(new Dimension(450, 120));
		d.setLocationRelativeTo(null);
		d.setModal(true);
		

		
		final Task task = new Task();
		
		task.addPropertyChangeListener(this);
		
		
		progress.getBtnCancel().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				task.cancel(true);
				
				d.setVisible(false);
				
			}
			
		});
		
		task.execute();
		d.setVisible(true);
		
		
		
	}
		
	
	private static String numberToAlphaSequence(int i) {
	    return i < 0 ? "" : numberToAlphaSequence((i / 26) - 1) + (char)(65 + i % 26);
	}
	
	private TridasProject getProject(String code)
	{
		if(projectHash.containsKey(code))
		{
			// Seen previously in this import so just return
			return projectHash.get(code);
		}
		
		// Not seen this yet, so check if it's in the DB
		
		
		
		
		
		return null;
	}

    class Task extends SwingWorker<Void, String> {
            	
    	int successfulCount = 0;
    	int i = 0;
    	Exception failException = null;
    	String importErrorLog = "";
    	
    	/*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() throws Exception {

        	
        	//statuslabel.setText("Loading XML File");
        	setProgress(0);
        	
        	publish("Reading import file");
    		try {
    			records = RediscoveryExportEx.getICMSRecordsFromXMLFileQuietly(filename);
    		} catch (Exception e1) {
    			//new BugDialog(e1);
    			//return null;
    			throw e1;
    		}
        	
    		for(RediscoveryExport record : records)
    		{
    			i++;
    			
    			if (isCancelled()) {
                    log.debug("Task cancelled");
                    return null;
                }
    			
    			
    			Double curr = (double) i;
    			
    			Double total = (double) records.size();
    			
    			Double prog = (curr/total) * 100;
    			
    			
    			log.debug("i="+curr+"; record count="+total+"; progress="+prog);
    			setProgress(prog.intValue());
    			
    			RediscoveryExportEx rec = new RediscoveryExportEx(record);
    			
    			if(rec.getUnImportableReason()!=null)
    			{
    				String error = "Ignoring record '"+rec.getCatalogCode()+"' because it is not importable: "+rec.getUnImportableReason();
    				log.debug(error);
    				importErrorLog += error+System.lineSeparator(); 
    				continue;
    			}
    			else
    			{
    				//log.debug("Importing record '"+rec.getCatalogCode()+"'");
    				publish("Importing:  '"+rec.getCatalogCode()+"'");
    			}

    			try {
    				TridasProject project = Dictionary.getTridasProjectByID("b30edb64-5a2b-11e5-9dbb-7b3f84ef785a");
    				TridasObjectEx park = Dictionary.getTridasObjectByCode(rec.getObjectCode());
    				if(park==null)
    				{
    					
    					//statuslabel.setText("Looking up "+rec.getObjectCode());
    					
    					// Object not known so create
    					park = new TridasObjectEx();
    					park.setTitle(rec.getObjectCode());
    					
    					
    					TridasUtils.setObjectCode(park, rec.getObjectCode());
    					TridasUtils.setProject(park, project);
    					park.setType(defaultObjectType);
		
    					EntityResource<TridasObjectEx> resource = new EntityResource<TridasObjectEx>(park, TellervoRequestType.CREATE, TridasObjectEx.class);
 					
    					resource.addResourceEventListener(new ResourceEventListener(){
							@Override
							public void resourceChanged(ResourceEvent re) {
								if(re.getEventType()==ResourceEvent.RESOURCE_QUERY_FAILED) failException = re.getAttachedException();
							}
    					});
    					
    					resource.query();
    					resource.queryThread.join();
    					EntitySearchResource<TridasObjectEx> resource2 = null;
    					if(failException!=null)	{
    						resource = null;
    						
    						if(failException.getLocalizedMessage().contains("unique_parent-title"))
    						{
        						// Creation of object failed due to duplicate record constraint.
        						// Try searching for the existing record instead
    							
    							failException = null;
    							SearchParameters param = new SearchParameters(SearchReturnObject.OBJECT);
    					    	param.addSearchConstraint(SearchParameterName.TOPOBJECTCODE, SearchOperator.EQUALS, TridasUtils.getGenericFieldValueByName(park, TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE));
    							resource2 = new EntitySearchResource<TridasObjectEx>(param, TridasObjectEx.class);
    		 					    												
    							resource2.addResourceEventListener(new ResourceEventListener(){
    								@Override
    								public void resourceChanged(ResourceEvent re) {
    									if(re.getEventType()==ResourceEvent.RESOURCE_QUERY_FAILED) failException = re.getAttachedException();
    								}
    	    					});
    	    					
    							resource2.query();
    							resource2.queryThread.join();
    	    					if(failException!=null)	{
    	    						String error =  "Error located object "+rec.getObjectCode()+System.lineSeparator();
    	    						error += failException.getLocalizedMessage()+System.lineSeparator();
    	    						error += "Skipping record"+System.lineSeparator();
    	    						log.debug(error);
    	    						importErrorLog += error;
    	    						continue;
    	    					}
    						}
    						else
    						{
    						
	    						String error =  "Error creating object "+rec.getObjectCode()+System.lineSeparator();
	    						error += failException.getLocalizedMessage()+System.lineSeparator();
	    						error += "Skipping record"+System.lineSeparator();
	    						log.debug(error);
	    						importErrorLog += error;
	    						continue;
    						}
    					}

						try{
							if(resource !=null)
							{
								park = resource.getAssociatedResult();
							}
							else if (resource2 !=null)
							{
								if(resource2.getAssociatedResult().size()==0)
								{
	   	    						String error =  "Error located object "+rec.getObjectCode()+System.lineSeparator();
    	    						error += failException.getLocalizedMessage()+System.lineSeparator();
    	    						error += "Skipping record"+System.lineSeparator();
    	    						log.debug(error);
    	    						importErrorLog += error;
    	    						continue;
								}
								else if(resource2.getAssociatedResult().size()>1)
								{
	   	    						String error =  "Error located object "+rec.getObjectCode()+".  Multiple matches found."+System.lineSeparator();
    	    						error += failException.getLocalizedMessage()+System.lineSeparator();
    	    						error += "Skipping record"+System.lineSeparator();
    	    						log.debug(error);
    	    						importErrorLog += error;
    	    						continue;
								}
								else
								{
									park=  resource2.getAssociatedResult().get(0);
								}
							}
						} catch (Exception e)
						{
							String error =  "Object "+rec.getObjectCode()+" not found"+System.lineSeparator();
    						error += e.getLocalizedMessage()+System.lineSeparator();
    						error += "Skipping record";
    						log.debug(error);
    						importErrorLog += error;
							continue;
						}
						App.tridasObjects.addTridasObject(park);
   
    				}

    				TridasObjectEx site = null;
    				if(park.isSetObjects())
    				{
    					for(TridasObject s : park.getObjects())
    					{
    						if(s.getTitle().equals(rec.getPrettySiteName()))
    						{
    							site = (TridasObjectEx) s;
    						}
    					}
    				}
    				
    				if(site==null)
    				{
    					// Site not known, so create
    				
    					site = new TridasObjectEx();
    					Integer count = 1;
    					String countstr = "";
    					if(park.isSetObjects())
    					{
    						count = park.getObjects().size()+1;
    					}
    					if(count>1) {
    						countstr = count+"";
    					}
    					TridasUtils.setObjectCode(site, rec.getSubObjectCode()+countstr);
    					site.setTitle(rec.getPrettySiteName());
    					site.setType(defaultObjectType);

    					EntityResource<TridasObjectEx> resource = new EntityResource<TridasObjectEx>(site, park, TridasObjectEx.class);
    						
    					resource.addResourceEventListener(new ResourceEventListener(){
							@Override
							public void resourceChanged(ResourceEvent re) {
								if(re.getEventType()==ResourceEvent.RESOURCE_QUERY_FAILED) failException = re.getAttachedException();
							}
    					});
    					
    					
    					resource.query();
    					resource.queryThread.join();
    					EntitySearchResource<TridasObjectEx> resource2 = null;
    					if(failException!=null)	{
    						
    						// Creation of object failed due to duplicate record constraint.
    						// Try searching for the existing record instead
    						resource = null;
    						
    						if(failException.getLocalizedMessage().contains("unique_parent-title"))
    						{
    							failException = null;
    							SearchParameters param = new SearchParameters(SearchReturnObject.OBJECT);
    					    	param.addSearchConstraint(SearchParameterName.OBJECTCODE, SearchOperator.EQUALS, TridasUtils.getGenericFieldValueByName(site, TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE));
    					    	param.addSearchConstraint(SearchParameterName.PARENTOBJECTID, SearchOperator.EQUALS, park.getIdentifier().getValue());
    							resource2 = new EntitySearchResource<TridasObjectEx>(param, TridasObjectEx.class);
    		 					    												
    							resource2.addResourceEventListener(new ResourceEventListener(){
    								@Override
    								public void resourceChanged(ResourceEvent re) {
    									if(re.getEventType()==ResourceEvent.RESOURCE_QUERY_FAILED) failException = re.getAttachedException();
    								}
    	    					});
    	    					
    							resource2.query();
    							resource2.queryThread.join();
    	    					if(failException!=null)	{
    	    						String error =  "Error located object "+rec.getObjectCode()+System.lineSeparator();
    	    						error += failException.getLocalizedMessage()+System.lineSeparator();
    	    						error += "Skipping record"+System.lineSeparator();
    	    						log.debug(error);
    	    						importErrorLog += error;
    	    						continue;
    	    					}
    						}
    						else
    						{
    						
	    						String error =  "Error creating object "+rec.getObjectCode()+System.lineSeparator();
	    						error += failException.getLocalizedMessage()+System.lineSeparator();
	    						error += "Skipping record"+System.lineSeparator();
	    						log.debug(error);
	    						importErrorLog += error;
	    						continue;
    						}
    					}
 
    					try{
    						if(resource !=null)
							{
    							site = resource.getAssociatedResult();
							}
							else if (resource2 !=null)
							{
								if(resource2.getAssociatedResult().size()==0)
								{
	   	    						String error =  "Error located object "+rec.getObjectCode()+System.lineSeparator();
    	    						error += failException.getLocalizedMessage()+System.lineSeparator();
    	    						error += "Skipping record"+System.lineSeparator();
    	    						log.debug(error);
    	    						importErrorLog += error;
    	    						continue;
								}
								else if(resource2.getAssociatedResult().size()>1)
								{
	   	    						String error =  "Error located object "+rec.getObjectCode()+".  Multiple matches found."+System.lineSeparator();
    	    						error += failException.getLocalizedMessage()+System.lineSeparator();
    	    						error += "Skipping record"+System.lineSeparator();
    	    						log.debug(error);
    	    						importErrorLog += error;
    	    						continue;
								}
								else
								{
									site=  resource2.getAssociatedResult().get(0);
								}
							}
						} catch (Exception e)
						{
							String error =  "Object "+rec.getObjectCode()+" not found"+System.lineSeparator();
    						error += e.getLocalizedMessage()+System.lineSeparator();
    						error += "Skipping record";
    						log.debug(error);
    						importErrorLog += error;
							continue;
						}

        				park.getObjects().add(site);
					
    				}
    			
    				//***********************************
    				// ELEMENT
    				//***********************************
    				
    				TridasElement element = new TridasElement();
    				element.setTitle(rec.getCatalogCodeNumber());
    				element.setType(defaultElementType);
    				ControlledVoc taxon = rec.getTaxon();
    				taxon = DictionaryUtil.getControlledVocForName(taxon.getNormal(), "taxonDictionary");
    				if(taxon==null) taxon = defaultTaxon;
    				element.setTaxon(taxon);
    				
    				TridasLocation loc = rec.getTridasLocation();
    				if(loc!=null)
    				{
    					element.setLocation(loc);
    				}
    				
    				
    				EntityResource<TridasElement> resource = new EntityResource<TridasElement>(element, site, TridasElement.class);
								
					resource.addResourceEventListener(new ResourceEventListener(){
						@Override
						public void resourceChanged(ResourceEvent re) {
							if(re.getEventType()==ResourceEvent.RESOURCE_QUERY_FAILED) failException = re.getAttachedException();
						}
					});
    				
					resource.query();
					resource.queryThread.join();
					EntitySearchResource<TridasElement> resource2 = null;
					if(failException!=null)	{
						
						// Creation of element failed due to duplicate record constraint.
						// Try searching for the existing record instead
						resource = null;
						
						if(failException.getLocalizedMessage().contains("unique_parentobject-code"))
						{
							failException = null;
							SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
					    	param.addSearchConstraint(SearchParameterName.OBJECTCODE, SearchOperator.EQUALS, TridasUtils.getGenericFieldValueByName(site, TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE));
					    	param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, element.getTitle());
							resource2 = new EntitySearchResource<TridasElement>(param, TridasElement.class);
		 					    												
							resource2.addResourceEventListener(new ResourceEventListener(){
								@Override
								public void resourceChanged(ResourceEvent re) {
									if(re.getEventType()==ResourceEvent.RESOURCE_QUERY_FAILED) failException = re.getAttachedException();
								}
	    					});
	    					
							resource2.query();
							resource2.queryThread.join();
	    					if(failException!=null)	{
	    						String error =  "Error located element "+rec.getCatalogCodeNumber()+System.lineSeparator();
	    						error += failException.getLocalizedMessage()+System.lineSeparator();
	    						error += "Skipping record"+System.lineSeparator();
	    						log.debug(error);
	    						importErrorLog += error;
	    						continue;
	    					}
						}
						else
						{
							String error =  "Error creating element "+rec.getCatalogCodeNumber()+System.lineSeparator();
							error += failException.getLocalizedMessage()+System.lineSeparator();
							error += "Skipping record";
							log.debug(error);
							importErrorLog += error;
							continue;
						}
					}
					
					 
					try{
						if(resource !=null)
						{
							element = resource.getAssociatedResult();
						}
						else if (resource2 !=null)
						{
							if(resource2.getAssociatedResult().size()==0)
							{
   	    						String error =  "Error located element "+rec.getCatalogCodeNumber()+System.lineSeparator();
	    						error += failException.getLocalizedMessage()+System.lineSeparator();
	    						error += "Skipping record"+System.lineSeparator();
	    						log.debug(error);
	    						importErrorLog += error;
	    						continue;
							}
							else if(resource2.getAssociatedResult().size()>1)
							{
   	    						String error =  "Error located element "+rec.getCatalogCodeNumber()+".  Multiple matches found."+System.lineSeparator();
	    						error += failException.getLocalizedMessage()+System.lineSeparator();
	    						error += "Skipping record"+System.lineSeparator();
	    						log.debug(error);
	    						importErrorLog += error;
	    						continue;
							}
							else
							{
								element =  resource2.getAssociatedResult().get(0);
							}
						}
					} catch (Exception e)
					{
						String error =  "Element "+rec.getCatalogCodeNumber()+" not found"+System.lineSeparator();
						error += e.getLocalizedMessage()+System.lineSeparator();
						error += "Skipping record";
						log.debug(error);
						importErrorLog += error;
						continue;
					}

    				//***********************************
    				// SAMPLE
    				//***********************************
    							
    				int title=-1;
    				ArrayList<RediscoverySubSample> types = rec.getSubSamples();
    				for(RediscoverySubSample subsample : types )
    				{ 
    					title++;
    					TridasSample sample = new TridasSample();
    					sample.setTitle(numberToAlphaSequence(title));
    					sample.setType(subsample.sampleType);
    					sample.setDescription(rec.getDescription());
    					if(rec.doesItemCountNeedChecking())
    					{
    						sample.setComments("This record was imported from ICMS.  There was ambiguity in the itemCount so this sample needs to be checked manually");
    					}
    					if(rec.getCollectorDate()!=null)
    					{
    						sample.setSamplingDate(rec.getCollectorDate());
    					}
    					
    					sample.getGenericFields().add(TridasManipUtil.createGenericField("tellervo.curationStatus", CurationStatus.ARCHIVED.value(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField("tellervo.externalID", rec.getOtherNumbers(), "xs:string"));

    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.ITEM_COUNT, subsample.itemCount+"", "xs:int"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.CATALOG_CODE, rec.getCatalogCode(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.ACCESSION_CODE, rec.getAccessionCode(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.STATUS_DATE, rec.getStatusDate().intValue()+"", "xs:int"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.CATALOGER, rec.getCataloger(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.CATALOG_OVERRIDE_DATE, rec.getCatalogDate(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.FIELD_SITE, rec.getFieldSite(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.STATE_SITE, rec.getStateSite(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.HIST_CULT_PER, rec.getHistCultPer(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.CULTURAL_ID, rec.getCulturalID(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.FIELD_SPECIMEN, rec.getFldSpecimen(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.OUTER_CODE, rec.getBarkCode(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.INNER_CODE, rec.getPithCode(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.IDENTIFIED_BY, rec.getIdentifiedBy(), "xs:string"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.IDENT_DATE, rec.getIdentDate(), "xs:string"));

    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.FIRST_YEAR, rec.getInnerRingDate()+"", "xs:int"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.LAST_YEAR, rec.getOuterRingDate()+"", "xs:int"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.PITH_PRESENT, rec.isPithPresent().toString(), "xs:boolean"));
    					sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.LAST_RING_UNDER_BARK, rec.isLastRingUnderBarkPresent().toString(), "xs:boolean"));
    					if(rec.isPithPresent())
    					{
    						sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.PITH_YEAR, rec.getInnerRingDate()+"", "xs:int"));
    					}
    					if(rec.isLastRingUnderBarkPresent())
    					{
    						sample.getGenericFields().add(TridasManipUtil.createGenericField(RediscoveryExportEx.BARK_YEAR, rec.getInnerRingDate()+"", "xs:int"));
    					}
    					
    					/**
    					"ltrr.unmeasuredinnerrings"
    					"ltrr.unmeasuredouterrings"
    					**/
    					
    			
    					// Try and get box from dictionary if possible
    					WSIBox box = Dictionary.getBoxByCode(rec.getCleanBoxName());
    					
    					if(box==null)
    					{
    						// No box in dictionary so create a new one
    						
    						WSIBox bx = new WSIBox();
    						bx.setTitle(rec.getCleanBoxName());
    						bx.setCurationLocation("U OF A LTRR");
    						
    						EntityResource<WSIBox> resource3 = new EntityResource<WSIBox>(bx, TellervoRequestType.CREATE, WSIBox.class);
    						
        					resource3.addResourceEventListener(new ResourceEventListener(){
    							@Override
    							public void resourceChanged(ResourceEvent re) {
    								if(re.getEventType()==ResourceEvent.RESOURCE_QUERY_FAILED) failException = re.getAttachedException();
    							}
        					});
    						
    						resource3.query();
        					resource3.queryThread.join();
        					
        					if(failException!=null)	{
        						String error =  "Error creating box "+rec.getCleanBoxName()+System.lineSeparator();
        						error += failException.getLocalizedMessage()+System.lineSeparator();
        						error += "Skipping record";
        						log.debug(error);
        						importErrorLog += error;
        						continue;
        						
        					}
        					
        					try{
        						box = resource3.getAssociatedResult();
        					}
    						catch (Exception e)
    						{
    							String error =  "Box "+rec.getCleanBoxName()+" not found"+System.lineSeparator();
    							error += e.getLocalizedMessage()+System.lineSeparator();
    							error += "Skipping record";
    							log.debug(error);
    							importErrorLog += error;
    							continue;
    						}

    						
    						// Add it to the dictionary for next time
    						Dictionary.getMutableDictionary("boxDictionary").add(box);
    					}
     					
    					sample.getGenericFields().add(TridasManipUtil.createGenericField("tellervo.boxID", box.getIdentifier().getValue(), "xs:string"));
    					
    					EntityResource<TridasSample> resource4 = new EntityResource<TridasSample>(sample, element, TridasSample.class);
    					
    					resource4.addResourceEventListener(new ResourceEventListener(){
							@Override
							public void resourceChanged(ResourceEvent re) {
								if(re.getEventType()==ResourceEvent.RESOURCE_QUERY_FAILED) failException = re.getAttachedException();
							}
    					});
    					
    					resource4.query();
    					resource4.queryThread.join();
    					if(failException!=null){
    						String error =  "Error creating sample "+sample.getTitle()+System.lineSeparator();
    						error += failException.getLocalizedMessage()+System.lineSeparator();
    						error += "Skipping record";
    						log.debug(error);
    						importErrorLog += error;
    						continue;
    					}
    				
    					
    					try{
    						sample = resource4.getAssociatedResult();
    					}catch (Exception e)
    					{
							String error =  "Sample "+sample.getTitle()+" not found"+System.lineSeparator();
							error += e.getLocalizedMessage()+System.lineSeparator();
							error += "Skipping record";
							log.debug(error);
							importErrorLog += error;
							continue;    				
						}


    				}
    				
    				successfulCount++;
    				

    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				throw e;
    			}
    			
    			
    		}
    		
            
            return null;

        }
        
        @Override
        protected void process(final List<String> chunks) {
          for (final String string : chunks) {
        	  progress.setStatusMessage(string);
          }
        }
 
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {

        	BufferedWriter writer = null;
        	try
        	{
        	    writer = new BufferedWriter( new FileWriter( "/tmp/import.log"));
        	    writer.write(this.importErrorLog);

        	}
        	catch ( IOException e)
        	{
        	}
        	finally
        	{
        	    try
        	    {
        	        if ( writer != null)
        	        writer.close( );
        	    }
        	    catch ( IOException e)
        	    {
        	    }
        	}
        	
        	d.setVisible(false);
        	
        	try {
                get();
            }catch (InterruptedException e )
        	{
            	Alert.message(App.mainWindow, "Interrupted", "Import interrupted after importing "+successfulCount+ " records.");
            	
            	return;
        	}
        	
        	catch (Exception e) {
				Alert.error("Failed", "Failed after importing "+successfulCount+ " records.\n\n"+FriendlyExceptionTranslator.translate(e).getLocalizedMessage());
				d.setVisible(false);
				return;
            } 
        	
           
        	d.setVisible(false);
    		Alert.message(App.mainWindow, "Finished", "Imported "+successfulCount+" of "+records.size()+ " records.");
    		
           
        }
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		
		
		if ("progress" == evt.getPropertyName()) {
			d.setVisible(true);
            int p = (Integer) evt.getNewValue();
            progress.getProgressBar().setValue(p);
            progress.setEstimate();
            
        } 
		
	}
	
	
	
}
