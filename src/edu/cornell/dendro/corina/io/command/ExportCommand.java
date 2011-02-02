/**
 * Created at Jan 19, 2011, 7:11:14 PM
 */
package edu.cornell.dendro.corina.io.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.defaults.TridasMetadataFieldSet;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.INamingConvention;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.tridasv2.LabCode;
import edu.cornell.dendro.corina.ui.Alert;


import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.io.control.ConvertEvent;
import edu.cornell.dendro.corina.io.control.ExportEvent;
import edu.cornell.dendro.corina.io.model.ConvertModel;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.Element;


import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;

import edu.cornell.dendro.corina.io.Metadata;

/**
 * @author Daniel
 *
 */
public class ExportCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(ExportCommand.class);
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		ExportEvent event = (ExportEvent) argEvent;
		
		// Set the export format for future use
    	App.prefs.setPref("corina.export.format", event.format);
    	    	
    	// Get defaults for creating project
    	TridasMetadataFieldSet defaults = new TridasMetadataFieldSet();
		
    	// Create sample list from elements
		List<Sample> samples = new ArrayList<Sample>();
		for(Element e : event.model.getElements()) {		
			// load it
			Sample s;
			try {
				s = e.load();
				
			} catch (IOException ioe) {
				Alert.error("Error Loading Sample", "Can't open this file: " + ioe.getMessage());
				continue;
			}
			
			samples.add(s);

			OpenRecent.sampleOpened(new SeriesDescriptor(s));
		}
		
		// no samples => don't bother doing anything
		if (samples.isEmpty()) {
			log.error("No samples to export");
			return;
		}
    	
		// Create a list of projects 
		ArrayList<TridasProject> projList = new ArrayList<TridasProject>();
		ArrayList<LabCode> labCodeList = new ArrayList<LabCode>();
		TridasProject project = null;
		for (Sample s : samples){
			if(!event.grouped || project == null){
				// User wants separate files so create a new project for each sample
				project = defaults.getProjectWithDefaults();
			}
			
			TridasObject tobj = (TridasObject) s.getMeta(Metadata.OBJECT, TridasObject.class);
			TridasElement telem = s.getMeta(Metadata.ELEMENT, TridasElement.class);
			TridasSample tsamp = s.getMeta(Metadata.SAMPLE, TridasSample.class);
			TridasRadius trad = s.getMeta(Metadata.RADIUS, TridasRadius.class);
			ITridasSeries tseries = s.getSeries();
			
			if(tseries instanceof TridasMeasurementSeries)
			{
				// Set all the standard mSeries entities
				trad.getMeasurementSeries().add((TridasMeasurementSeries) tseries);
				tsamp.getRadiuses().add(trad);
				telem.getSamples().add(tsamp);
				tobj.getElements().add(telem);
				project.getObjects().add(tobj);
			}
			else
			{
				// Derived series so no other entities
				project.getDerivedSeries().add((TridasDerivedSeries) tseries);
			}
			
			if(!event.grouped){
				// Add project to list
				projList.add(project);
				LabCode code = s.getMeta(Metadata.LABCODE, LabCode.class);
				labCodeList.add(code);
			}		
		}
		
		if(event.grouped) {
			// Add project to list as there user wants one file for each series
			projList.add(project);
			
			if (samples.size() == 1){
				LabCode code = samples.get(0).getMeta(Metadata.LABCODE, LabCode.class);
				labCodeList.add(code);
			}
		}
		
		INamingConvention naming = event.naming;
		if(labCodeList.size() != projList.size()){
			log.warn("Lab code list isn't the same size as project list");
			Alert.message("Whoops", "Lab codes are not supported in the input set, using hierarchical naming convention instead.");
			naming = new HierarchicalNamingConvention();
		}
		
		ConvertModel cmodel = new ConvertModel(event.model.getNodes());
		cmodel.setTridasProjects(projList.toArray(new TridasProject[0]));
		cmodel.setLabCodes(labCodeList.toArray(new LabCode[0]));
		
		ConvertEvent cevent = new ConvertEvent(event.format, naming, cmodel, event.model.getExportView());
		cevent.dispatch();
	}
	
}
