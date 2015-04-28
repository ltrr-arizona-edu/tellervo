package org.tellervo.desktop.gui.menus.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import org.fhaes.fhchart.gui.PlotWindow;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.IOUtils;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.IDendroFile;
import org.tridas.io.TridasIO;
import org.tridas.io.defaults.TridasMetadataFieldSet;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.ImpossibleConversionException;
import org.tridas.io.naming.AbstractNamingConvention;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.util.FilePermissionException;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

public class GraphCreateFileHistoryPlotAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor editor;
	

	
	public GraphCreateFileHistoryPlotAction(AbstractEditor editor) {
        super(I18n.getText("menus.graph.createfhplot"), Builder.getIcon("fhaes.png", 22));
        putValue(SHORT_DESCRIPTION, "Create File History Plot");

        this.editor = editor;
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.graph.createfhplot")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.graph.createfhplot"));
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		DBBrowser browser = new DBBrowser((Frame) null, true, true);
		browser.setVisible(true);

		
		if(browser.getReturnStatus() == DBBrowser.RET_OK) {
			ElementList elements = browser.getSelectedElements();
			
			int groupingType = 0;
					    	    	
	    	// Get defaults for creating project
	    	TridasMetadataFieldSet defaults = new TridasMetadataFieldSet();
			
	    	// Create sample list from elements
			List<Sample> samples = new ArrayList<Sample>();
			for(Element e : elements) {		
				// load it
				Sample s;
				try {
					s = e.load();
					
				} catch (IOException ioe) {
					Alert.error(editor, "Error Loading Sample",
							"Can't open this file: " + ioe.getMessage());
					continue;
				}
				
				samples.add(s);

				OpenRecent.sampleOpened(new SeriesDescriptor(s));
			}
			
			// no samples => don't bother doing anything
			if (samples.isEmpty()) {
				return;
			}
	    	
			// Create a list of projects 
			ArrayList<TridasProject> projList = new ArrayList<TridasProject>();
			ArrayList<LabCode> labCodeList = new ArrayList<LabCode>();
			TridasProject project = defaults.getProjectWithDefaults();
			for (Sample s : samples)
			{
				if(groupingType==1)
				{
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
				
				if(groupingType==1)
				{
					// Add project to list
					projList.add(project);
					LabCode code = s.getMeta(Metadata.LABCODE, LabCode.class);
					labCodeList.add(code);
				}		
			}
			
			if(groupingType==0)
			{
				// Add project to list as there user wants seperate files for each series
				projList.add(project);
				
				if (samples.size()==1)
				{
					LabCode code = samples.get(0).getMeta(Metadata.LABCODE, LabCode.class);
					labCodeList.add(code);
				}
			}
			
			// Loop through projects writing them out as we go
			String messages = "";
			int i=-1;
			for(TridasProject p : projList)
			{
				i++;
				// Create the writer based on the requested format
		    	AbstractDendroCollectionWriter writer = TridasIO.getFileWriter("FHX2");
		    	
		    	// Create and set the naming convention
		    	AbstractNamingConvention nc;
		    	if(labCodeList.size()==projList.size())
		    	{
		    		// We have a Tellervo lab code for each project so use this as the base filename
		    		nc = new NumericalNamingConvention();
		    		((NumericalNamingConvention)nc).setBaseFilename(
		    				LabCodeFormatter.getDefaultFormatter().format(labCodeList.get(i)).toString());
		    	}
		    	else
		    	{
		    		// We don't have labcodes for the project(s) so use the hierarchical naming convention instead
		    		nc = new HierarchicalNamingConvention();
		    	}
		    	writer.setNamingConvention(nc);
		    	
		    	// Get the writer to load the project
				try {
					writer.loadProject(p);
				} catch (ConversionWarningException e1) {
					e1.printStackTrace();
		
				} catch (ImpossibleConversionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					
				
				// Get output folder
				String outputFolder = null;
				File outputFolder2 = null;
				try {
					outputFolder2 = IOUtils.createTempDirectory();
					outputFolder2.deleteOnExit();
					outputFolder = outputFolder2.toString();
					if (!outputFolder.endsWith(File.separator) && !outputFolder.equals("")) {
						outputFolder += File.separator;
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}



				IDendroFile[] files = writer.getFiles();
				for (int j=0; j<files.length; j++) {
					IDendroFile dof = files[j];
					try {
						writer.saveFileToDisk(outputFolder, dof);
					} catch (FilePermissionException e1) {
						messages += "Permission denied\n";
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					
					
					// Add any warnings to our warning message cache
					if(writer.getWarnings().length>0)
					{
						messages += "Warning for file blah\n";
					}

					for(ConversionWarning warning : dof.getDefaults().getWarnings())
					{
						messages += "- "+warning.getMessage()+"\n";
					}
					
					for(ConversionWarning warning : writer.getWarnings())
					{
						messages += "- "+warning.getMessage()+"\n";
					}
				}
				
				final File[] filescreated = outputFolder2.listFiles();
				
				
				
				outputFolder2.deleteOnExit();
				if(filescreated.length>0)
				{
					//logger.debug("Opening plot window with file: "+filescreated[0]);
					PlotWindow plotwindow = new PlotWindow(filescreated[0]);
					
					plotwindow.addWindowListener(new WindowListener(){

						@Override
						public void windowActivated(WindowEvent arg0) {
						}

						@Override
						public void windowClosed(WindowEvent arg0) {
							//log.debug("FHXPlot window closed");
							for(File f : filescreated)
							{
								f.delete();
							}
						}

						@Override
						public void windowClosing(WindowEvent arg0) {								
						}

						@Override
						public void windowDeactivated(WindowEvent arg0) {
						}

						@Override
						public void windowDeiconified(WindowEvent arg0) {
						}

						@Override
						public void windowIconified(WindowEvent arg0) {
						}

						@Override
						public void windowOpened(WindowEvent arg0) {								
						}
						
					});
					
				}
				else
				{
					Alert.error(editor, "FHX Conversion", "Error converting series to FHX format");
					
				}
				
	    	}
		
		}
	}
		
	}
	
