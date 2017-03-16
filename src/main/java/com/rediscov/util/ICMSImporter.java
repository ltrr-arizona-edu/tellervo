package com.rediscov.util;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.WordUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.DictionaryUtil;
import org.tellervo.desktop.wsi.Resource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIRootElement;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.rediscov.schema.RediscoveryExport;

public class ICMSImporter {

	List<RediscoveryExport> records;
	protected final static Logger log = LoggerFactory.getLogger(ICMSImporter.class);

	HashMap<String, TridasProject> projectHash = new HashMap<String, TridasProject>();
	HashMap<String, UUID> objectHash = new HashMap<String, UUID>();
	HashMap<String, UUID> elementHash = new HashMap<String, UUID>();
	HashMap<String, UUID> sampleHash = new HashMap<String, UUID>();
	ControlledVoc defaultObjectType;
	ControlledVoc defaultElementType;
	ControlledVoc defaultTaxon;

	private String filename;
	
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
	
		records = RediscoveryExportEx.getICMSRecordsFromXMLFile(filename, true);
		
		
		int i = 0;
		for(RediscoveryExport record : records)
		{
			i++;
			RediscoveryExportEx rec = (RediscoveryExportEx) record;
			
			if(!rec.isImportable())
			{
				log.debug("Ignoring record '"+rec.getCatalogCode()+"' because it is not importable.");
				continue;
			}
			else
			{
				log.debug("Importing record '"+rec.getCatalogCode()+"'");
			
			}
			
			
			try {
				TridasObjectEx park = Dictionary.getTridasObjectByCode(rec.getObjectCode());
				if(park==null)
				{
					// Object not known so create
					park = new TridasObjectEx();
					park.setTitle(rec.getObjectCode());
					TridasUtils.setObjectCode(park, rec.getObjectCode());
					park.setType(this.defaultObjectType);
					
					
					
					EntityResource<TridasObjectEx> resource = new EntityResource<TridasObjectEx>(park, TellervoRequestType.CREATE, TridasObjectEx.class);
										
					// set up a dialog...
					TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, i, records.size());
					
					resource.query();
					dialog.setVisible(true);
					
					if(!dialog.isSuccessful()) {
						JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), "Failed at "+rec.getCatalogCode()+"\n"+ I18n.getText("error.savingChanges") + "\r\n" +
								I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
								I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
						break;
					}
					park = resource.getAssociatedResult();
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
										
					// set up a dialog...
					TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, i, records.size());
					
					resource.query();
					dialog.setVisible(true);
					
					if(!dialog.isSuccessful()) {
						JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), "Failed at "+rec.getCatalogCode()+"\n"+ I18n.getText("error.savingChanges") + "\r\n" +
								I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
								I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
						break;
					}
					site = resource.getAssociatedResult();
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
				if(taxon==null) taxon = this.defaultTaxon;
				element.setTaxon(taxon);
				
				TridasLocation loc = rec.getTridasLocation();
				if(loc!=null)
				{
					element.setLocation(loc);
				}
				
				
				EntityResource<TridasElement> resource = new EntityResource<TridasElement>(element, site, TridasElement.class);
				TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, i, records.size());
				resource.query();
				dialog.setVisible(true);
				if(!dialog.isSuccessful()) {
					JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), "Failed at "+rec.getCatalogCode()+"\n"+ I18n.getText("error.savingChanges") + "\r\n" +
							I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
							I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
					break;
				}
				element = resource.getAssociatedResult();
				
				
				
				//***********************************
				// SAMPLE
				//***********************************
							
				int title=0;
				ArrayList<ControlledVoc> types = rec.getSampleTypes();
				for(ControlledVoc sampletype : types )
				{ 
					TridasSample sample = new TridasSample();
					sample.setTitle(numberToAlphaSequence(title));
					sample.setType(sampletype);

					
					
					
					EntityResource<TridasSample> resource2 = new EntityResource<TridasSample>(sample, element, TridasSample.class);
					TellervoResourceAccessDialog dialog2 = new TellervoResourceAccessDialog(App.mainWindow, resource2, i, records.size());
					resource2.query();
					dialog2.setVisible(true);
					if(!dialog2.isSuccessful()) {
						JOptionPane.showMessageDialog(BulkImportModel.getInstance().getMainView(), "Failed at "+rec.getCatalogCode()+"\n"+ I18n.getText("error.savingChanges") + "\r\n" +
								I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
								I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
						break;
					}
					sample = resource2.getAssociatedResult();
					

				}
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
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
	
	
	
}
