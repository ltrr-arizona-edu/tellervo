package edu.cornell.dendro.corina.io;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;

import org.apache.commons.lang.WordUtils;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.IDendroFile;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.IncompleteTridasDataException;
import org.tridas.io.formats.besancon.BesanconToTridasDefaults;
import org.tridas.io.naming.AbstractNamingConvention;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.naming.UUIDNamingConvention;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasValues;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.formats.Filetype;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.Help;
import edu.cornell.dendro.corina.gui.FileDialog.ExtensionFilter;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.tridasv2.LabCode;
import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;

/*
 * ExportUI.java
 *
 * Created on September 7, 2009, 12:27 PM
 */

/**
 *
 * @author  peterbrewer
 */
public class ExportUI extends javax.swing.JPanel{
    	
	private ElementList elements;
	private String exportDirectory;
	private boolean rememberExportDirectory;
	private Boolean exportMulti = true;
	protected DefaultComboBoxModel howModel = new DefaultComboBoxModel();
	protected DefaultComboBoxModel whatModel = new DefaultComboBoxModel();
	protected ArrayListModel<String> formatModel = new ArrayListModel<String>();
	protected ArrayListModel<String> encodingModel = new ArrayListModel<String>();
	protected ArrayListModel<String> groupingModel = new ArrayListModel<String>();
	protected ExportDialog parent;
	final JFileChooser fc = new JFileChooser();
	
    /** Creates new form ExportUI */
    public ExportUI(ExportDialog p, ElementList els) {
    	

    	parent = p;
    	elements = els;
    	
		rememberExportDirectory = true;

		// load the last export directory. If it doesn't exist, make a nice default.
		exportDirectory = App.prefs.getPref("corina.dir.export");
		if(exportDirectory == null)
			exportDirectory = App.prefs.getPref("corina.dir.data");
		if(exportDirectory == null)
			exportDirectory = "";
		
		// now, keep going back until it exists and is a directory.
		File exdf = new File(exportDirectory).getAbsoluteFile();
		
		while(!exdf.isDirectory() && exdf.toString().length() > 0)
			exdf = exdf.getParentFile();
		
		exportDirectory = exdf.getAbsolutePath();
    	
    	
        initComponents();
        setupGui();
        internationalizeComponents();
    }
    
    private void internationalizeComponents()    
    {
    	lblWhat.setText(I18n.getText("export.what")+":");
    	lblGrouping.setText(I18n.getText("export.grouping")+":");
    	lblEncoding.setText(I18n.getText("export.encoding")+":");
    	lblOutput.setText(I18n.getText("export.outputFolder")+":");
    	btnHelp.setText(I18n.getText("menus.help"));
    	btnBrowse.setText(I18n.getText("general.browse"));
    	btnCancel.setText(I18n.getText("general.cancel"));
    	btnOK.setText(I18n.getText("general.ok"));
    }
    
    private void setupGui()
    {    	
        // Set icon        
        lblIcon.setIcon(Builder.getIcon("fileexport.png", 128));
    	
    	// Disable ok button until file/folder has been selected
    	btnOK.setEnabled(false);
        
		Help.assignHelpPageToButton(btnHelp, "File_formats");
    	
    	// Hide how form items
    	//lblHow.setVisible(false);
    	//cboHow.setVisible(false);

		groupingModel.add("Single packed file if possible");
		groupingModel.add("Separate files");
		cboGrouping.setModel(groupingModel);
		cboGrouping.setSelectedIndex(0);
		
      	// setup combo boxes
      	cboWhat.setModel(whatModel);
      	setWhatModel();
      	cboExportFormat.setModel(formatModel);
      	setupFormatList();
      	cboEncoding.setModel(encodingModel);
      	setupEncodingList();
      	cboEncoding.setSelectedIndex(0);
      	
    	// Add action listeners
		btnOK.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				doExport();
			}
		});		
		btnCancel.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// just close
				parent.dispose();
			}
		});

		btnBrowse.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
				// Are we exporting multiple files... 
				if (exportMulti)
				{
					// ... if so choose directory not file
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	
					fc.setDialogTitle(I18n.getText("export.selectAFolder"));
				}
				else
				{
					// ... otherwise set the file filter 
					Filetype ft =  (Filetype) formatModel.getSelectedItem();
					ExtensionFilter filter = new FileDialog.ExtensionFilter(ft.getDefaultExtension().substring(1), ft.toString());
					fc.setFileFilter(filter);
				}
			
				// Open at last used directory
				if(exportDirectory!=null)
				{
					fc.setCurrentDirectory(new File(exportDirectory));					
				}

				// Show dialog
				int returnVal;
				returnVal = fc.showSaveDialog(parent);
		
				// Set file/folder in text box
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            txtOutput.setText(file.getAbsoluteFile().toString());

		        } else {
		            System.out.println("Save command cancelled by user.");
		        }
		        
		        // If file/folder selected enable OK button
				if(txtOutput.getText()==null || txtOutput.getText().equals(""))
				{
					btnOK.setEnabled(false);
				}
				else
				{
					btnOK.setEnabled(true);
				}
		        
			}
		});
		
		txtOutput.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(txtOutput.getText()==null || txtOutput.getText().equals(""))
				{
					btnOK.setEnabled(false);
				}
				else
				{
					btnOK.setEnabled(true);
				}
			}
		});
		
		
		cboExportFormat.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				

			}
		});
        



    }

    /**
     * Set the comboboxmodel for the 'what' combo box.  
     * @param plural if there are more than one series being converted
     */
    public void setWhatModel()
    {
    	if(elements.size()>1)
    	{
        	String whatOptionsPl[] = new String[] {
        		"The selected " + Integer.toString(elements.size()) + " series",
        		//"These series and their associated raw measurement series", 
        		//"These series and all associated series", 
        		};
			int n = whatOptionsPl.length;
			for (int i = 0; i < n; i++) 
			{
				whatModel.addElement(whatOptionsPl[i]);
			}
    	}
    	else
    	{
	    	String whatOptions[] = new String[] {
				I18n.getText("export.justThisSeries"),
				//I18n.getText("export.thisSeriesAndRaws"),
				//I18n.getText("export.thisSeriesAndAll"),
				};
			int n = whatOptions.length;
			for (int i = 0; i < n; i++) 
			{
				whatModel.addElement(whatOptions[i]);
			}  
    	}
	
    }
     
    /**
     * Set up the aspects of the gui that change
     * depending on whether the output is packed 
     * into a single file or not.
     * 
     * @param packed
     */
    public void setGuiForPacked(Boolean packed)
    {
    	if(packed)
    	{
    		lblOutput.setText(I18n.getText("export.outputFile"));
    		txtOutput.setText("");
    	}
    	else
    	{
    		lblOutput.setText(I18n.getText("export.outputFolder"));
    		txtOutput.setText("");
    	}
    }
    
    
    public void setupEncodingList()
    {  	
		// put default one on top, so the user will see
		// "automatic" right underneath
		encodingModel.add(Charset.defaultCharset().displayName());
		for (String s : Charsets.getWritingCharsets()) {
			if (s.equals(Charset.defaultCharset().displayName())) {
				continue;
			}
			encodingModel.add(s);
		}
		
		
    }
    
    /**
     * Set up the combo box containing the output file formats
     */
    public void setupFormatList()
    {
    	String[] outputFormats = TridasIO.getSupportedWritingFormats();
    	ArrayList<String> blah = new ArrayList<String>(Arrays.asList(outputFormats));
    	formatModel.addAll(blah);

    	// Set the previously used format or default to TRiDaS
		if(App.prefs.getPref("corina.export.format")!=null)
		{
			cboExportFormat.setSelectedItem(App.prefs.getPref("corina.export.format"));
		}
		else
		{   	
			cboExportFormat.setSelectedItem("TRiDaS");
		}
		

    }
    

    /**
     * Actually do the export
     */
    private void doExport(){
		
    	// Set the export format for future use
    	App.prefs.setPref("corina.export.format", this.cboExportFormat.getSelectedItem().toString());
    	    	
    	// Get defaults for creating project
    	BesanconToTridasDefaults defaults = new BesanconToTridasDefaults();
		
    	// Create sample list from elements
		List<Sample> samples = new ArrayList<Sample>();
		for(Element e : elements) {		
			// load it
			Sample s;
			try {
				s = e.load();
				
			} catch (IOException ioe) {
				Alert.error("Error Loading Sample",
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
		TridasProject project = defaults.getDefaultTridasProject();
		for (Sample s : samples)
		{
			if(this.cboGrouping.getSelectedIndex()==1)
			{
				// User wants separate files so create a new project for each sample
				project = defaults.getDefaultTridasProject();
			}
			
			TridasObject tobj = s.getMeta(Metadata.OBJECT, TridasObject.class);
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
			
			if(this.cboGrouping.getSelectedIndex()==1)
			{
				// Add project to list
				projList.add(project);
				LabCode code = s.getMeta(Metadata.LABCODE, LabCode.class);
				labCodeList.add(code);
			}		
		}
		if(this.cboGrouping.getSelectedIndex()==0)
		{
			// Add project to list as there is just one project
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
	    	AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(this.cboExportFormat.getSelectedItem().toString());
	    	
	    	// Create and set the naming convention
	    	AbstractNamingConvention nc;
	    	if(labCodeList.size()==projList.size())
	    	{
	    		// We have a Corina lab code for each project so use this as the base filename
	    		nc = new NumericalNamingConvention();
	    		((NumericalNamingConvention)nc).setBaseFilename(
	    				LabCodeFormatter.getSeriesPrefixFormatter().format(labCodeList.get(i)).toString());
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
			} catch (IncompleteTridasDataException e1) {
				e1.printStackTrace();
			} catch (ConversionWarningException e1) {
				e1.printStackTrace();
	
			}
			
			// Add any warnings to our warning message cache
			if(writer.getWarnings().length>0)
			{
				messages += "Warning for file blah\n";
			}
			for(ConversionWarning warning : writer.getWarnings())
			{
				messages += "- "+warning.getMessage()+"\n";
			}
				
			// Get output folder
			String outputFolder = this.txtOutput.getText();			
			if (!outputFolder.endsWith(File.separator) && !outputFolder.equals("")) {
				outputFolder += File.separator;
			}
			
			int currFile = 0;
			Response response= null;
			IDendroFile[] files = writer.getFiles();
			for (int j=0; j<files.length; j++) {
				IDendroFile dof = files[j];
	
				currFile++;
				String filename = writer.getNamingConvention().getFilename(dof);
				
				//model.setSavingFilename(filename + "." + dof.getExtension());
				
				// check to see if it exists:
				File file = new File(outputFolder+File.separator+filename + "." + dof.getExtension());
				/*if(file.exists()){
					if(response == null){
						OverwriteModel om = new OverwriteModel();
						om.setAll(false);
						om.setMessage(I18n.getText("control.convert.overwrite", filename, filename+"(1)"));
						popup[0] = new OverwritePopup(om, ModelLocator.getInstance().getMainWindow());
						// this should hang until the window is closed
						popup[0].setVisible(true);
						popup[0] = null; // so the saving dialog knows it's ok to show itself
						
						response = om.getResponse();
						all = om.isAll();
						if(response == null){
							log.error("response is null");
							j--;
							currFile--;
							continue;
						}
					}
					
					switch(response){
						case IGNORE:
							response = null;
							continue;
						case OVERWRITE:
							p.writer.saveFileToDisk(outputFolder, dof);
							break;
						case RENAME:
							p.writer.getNamingConvention().setFilename(dof, filename+"(1)");
							j--;
							currFile--;
							response = null;
							continue;
					}
					if(!all){
						response = null;
					}
				}*/
				writer.saveFileToDisk(outputFolder, dof);
				//model.setSavingPercent(currFile * 100 / totalFiles);
			}
    	}
	
		

		if(messages.length()>0)
		{
			Alert.message("Warnings", "There were issues with the export:\n"+WordUtils.wrap(messages, 50));
		}
		
		// close dialog
		parent.dispose();


    }
    
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelOptions = new javax.swing.JPanel();
        lblExportFormat = new javax.swing.JLabel();
        txtOutput = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        cboExportFormat = new javax.swing.JComboBox();
        lblOutput = new javax.swing.JLabel();
        cboWhat = new javax.swing.JComboBox();
        lblWhat = new javax.swing.JLabel();
        cboGrouping = new javax.swing.JComboBox();
        lblGrouping = new javax.swing.JLabel();
        panelSpacer = new javax.swing.JPanel();
        lblEncoding = new javax.swing.JLabel();
        cboEncoding = new javax.swing.JComboBox();
        panelBottom = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();
        panelIcon = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();
        panelPadding = new javax.swing.JPanel();
        separator = new javax.swing.JSeparator();

        setMinimumSize(new java.awt.Dimension(400, 200));

        lblExportFormat.setText("Export format:");

        btnBrowse.setText("Browse");

        lblOutput.setText("Output folder:");

        lblWhat.setText("What to export:");

        lblGrouping.setText("Grouping:");

        panelSpacer.setPreferredSize(new java.awt.Dimension(100, 0));

        org.jdesktop.layout.GroupLayout panelSpacerLayout = new org.jdesktop.layout.GroupLayout(panelSpacer);
        panelSpacer.setLayout(panelSpacerLayout);
        panelSpacerLayout.setHorizontalGroup(
            panelSpacerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 318, Short.MAX_VALUE)
        );
        panelSpacerLayout.setVerticalGroup(
            panelSpacerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        lblEncoding.setText("Encoding:");

        cboEncoding.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Automatic", "UTF-8", "UTF-16", "Latin 1", "Mac Roman" }));

        org.jdesktop.layout.GroupLayout panelOptionsLayout = new org.jdesktop.layout.GroupLayout(panelOptions);
        panelOptions.setLayout(panelOptionsLayout);
        panelOptionsLayout.setHorizontalGroup(
            panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelSpacer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .add(panelOptionsLayout.createSequentialGroup()
                        .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(lblOutput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(lblExportFormat, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(lblGrouping, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(lblWhat, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(lblEncoding))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelOptionsLayout.createSequentialGroup()
                                .add(txtOutput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnBrowse))
                            .add(cboExportFormat, 0, 215, Short.MAX_VALUE)
                            .add(cboGrouping, 0, 215, Short.MAX_VALUE)
                            .add(cboWhat, 0, 215, Short.MAX_VALUE)
                            .add(cboEncoding, 0, 215, Short.MAX_VALUE)))))
        );
        panelOptionsLayout.setVerticalGroup(
            panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblWhat)
                    .add(cboWhat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblGrouping)
                    .add(cboGrouping, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblExportFormat)
                    .add(cboExportFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblEncoding)
                    .add(cboEncoding, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblOutput)
                    .add(btnBrowse)
                    .add(txtOutput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelSpacer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOK.setText("OK");

        btnHelp.setText("Help");

        org.jdesktop.layout.GroupLayout panelBottomLayout = new org.jdesktop.layout.GroupLayout(panelBottom);
        panelBottom.setLayout(panelBottomLayout);
        panelBottomLayout.setHorizontalGroup(
            panelBottomLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelBottomLayout.createSequentialGroup()
                .addContainerGap()
                .add(btnHelp)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 240, Short.MAX_VALUE)
                .add(btnCancel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnOK)
                .addContainerGap())
        );
        panelBottomLayout.setVerticalGroup(
            panelBottomLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelBottomLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelBottomLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnOK)
                    .add(btnCancel)
                    .add(btnHelp))
                .addContainerGap())
        );

        lblIcon.setMaximumSize(new java.awt.Dimension(128, 128));
        lblIcon.setMinimumSize(new java.awt.Dimension(128, 128));

        org.jdesktop.layout.GroupLayout panelIconLayout = new org.jdesktop.layout.GroupLayout(panelIcon);
        panelIcon.setLayout(panelIconLayout);
        panelIconLayout.setHorizontalGroup(
            panelIconLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelIconLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelIconLayout.setVerticalGroup(
            panelIconLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelIconLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout panelPaddingLayout = new org.jdesktop.layout.GroupLayout(panelPadding);
        panelPadding.setLayout(panelPaddingLayout);
        panelPaddingLayout.setHorizontalGroup(
            panelPaddingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 482, Short.MAX_VALUE)
        );
        panelPaddingLayout.setVerticalGroup(
            panelPaddingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        separator.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        separator.setMaximumSize(new java.awt.Dimension(32767, 2));
        separator.setMinimumSize(new java.awt.Dimension(0, 2));
        separator.setPreferredSize(new java.awt.Dimension(50, 2));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelBottom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelPadding, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(panelIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(panelOptions, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, separator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(panelIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(panelOptions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 168, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(panelPadding, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(12, 12, 12)
                .add(panelBottom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnBrowse;
    protected javax.swing.JButton btnCancel;
    protected javax.swing.JButton btnHelp;
    protected javax.swing.JButton btnOK;
    protected javax.swing.JComboBox cboEncoding;
    protected javax.swing.JComboBox cboExportFormat;
    protected javax.swing.JComboBox cboGrouping;
    protected javax.swing.JComboBox cboWhat;
    protected javax.swing.JLabel lblEncoding;
    protected javax.swing.JLabel lblExportFormat;
    protected javax.swing.JLabel lblGrouping;
    protected javax.swing.JLabel lblIcon;
    protected javax.swing.JLabel lblOutput;
    protected javax.swing.JLabel lblWhat;
    protected javax.swing.JPanel panelBottom;
    protected javax.swing.JPanel panelIcon;
    protected javax.swing.JPanel panelOptions;
    protected javax.swing.JPanel panelPadding;
    protected javax.swing.JPanel panelSpacer;
    protected javax.swing.JSeparator separator;
    protected javax.swing.JTextField txtOutput;
    // End of variables declaration//GEN-END:variables

    public static class Charsets {
    	public static final String AUTO = "Automatic";
    	public static final String DEFAULT = "System Default"; // djm TODO locale
    	
    	public static final String[] getReadingCharsets() {
    		ArrayList<String> charsets = new ArrayList<String>();
    		charsets.add(AUTO);
    		for (String cs : Charset.availableCharsets().keySet()) {
    			charsets.add(cs);
    		}
    		return charsets.toArray(new String[0]);
    	}
    	
    	public static final String[] getWritingCharsets() {
    		ArrayList<String> charsets = new ArrayList<String>();
    		for (String key : Charset.availableCharsets().keySet()) {
    			Charset cs = Charset.availableCharsets().get(key);
    			if (cs.canEncode()) {
    				charsets.add(key);
    			}
    		}
    		return charsets.toArray(new String[0]);
    	}
    }

    public enum Response {OVERWRITE , IGNORE, RENAME}
}

