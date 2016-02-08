package org.tellervo.desktop.bulkdataentry.view.odkwizard;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardDialog;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.ui.Builder;

public class ODKImportWizard extends AbstractWizardDialog {
	
	private static final long serialVersionUID = 1L;
	private WizardWelcome page1;
	private WizardGetFolder page2;
	private WizardMediaFiles page3;
	private WizardCreateCSV page4;
	private WizardFinish page5;

	public ODKImportWizard(JFrame parent) {
		
		super("ODK Import Wizard", Builder.getImageAsIcon("odk-sidebar.png"));
		App.init();
		

		this.parent = parent;
		
		// Setup pages
		pages = new ArrayList<AbstractWizardPanel>();
		page1 = new WizardWelcome();
		page2 = new WizardGetFolder();
		page3 = new WizardMediaFiles();
		page4 = new WizardCreateCSV();
		page5 = new WizardFinish();
		pages.add(page1);
		pages.add(page2);
		pages.add(page3);		
		pages.add(page4);
		pages.add(page5);
	
		
		setupGui();
		this.setModal(true);
		this.setVisible(true);
		
	}
	
	/**
	 * Get the location where the ODK instances are stored
	 * 
	 * @return
	 */
	public String getODKInstancesFolder()
	{
		return this.page2.getODKInstancesFolder();
	}
	
	/**
	 * Returns true is we're getting data from the server, or false if ODK files are stored locally
	 * 
	 * @return
	 */
	public boolean isRemoteAccessSelected()
	{
		return this.page2.isRemoteAccessSelected();
	}
	
	/**
	 * Whether a CSV export should be created or not.
	 * 
	 * @return
	 */
	public boolean isCreateCSVFileSelected()
	{
		return page4.isCreateCSVFileSelected();
	}
	
	/**
	 * Whether to include media files in import or not
	 * 
	 * @return
	 */
	public boolean isIncludeMediaFilesSelected()
	{
		return page3.isIncludeMediaFilesSelected();
	}
	
	/**
	 * Whether media files shoudl be renamed or not
	 * 
	 * @return
	 */
	public boolean isRenameMediaFilesSelected()
	{
		return page3.isRenameMediaFilesSelected();
	}
	
	/**
	 * Get the local folder where media files should be copied
	 * 
	 * @return
	 */
	public String getCopyToLocation()
	{
		return page3.getCopyToLocation();
	}
	
	/**
	 * Get the location where media files will be finally located.  Typically an FTP or HTTP location
	 * 
	 * @return
	 */
	public String getFinalLocation()
	{
		return page3.getFinalLocation();
	}
	
	public String getFilenamePrefix()
	{
		return page3.getFilenamePrefix();
	}
	
	/**
	 * Get the filename for the CSV export file to be created 
	 * 
	 * @return
	 */
	public String getCSVFilename()
	{
		return page4.getCSVFilename();
	}
	


}
