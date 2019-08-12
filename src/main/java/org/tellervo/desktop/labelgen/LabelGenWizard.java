package org.tellervo.desktop.labelgen;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardDialog;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.labelgen.AbstractTellervoLabelStyle.ItemType;
import org.tellervo.desktop.labelgen.LGWizardBoxPicker.BoxSortType;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.schema.WSIBox;

public class LabelGenWizard extends AbstractWizardDialog {

	private static final long serialVersionUID = 1L;
	private LGWizardWelcome page1;
	private LGWizardWhatStyle page2;
	private LGWizardBoxPicker page3;
	private LGWizardSamplePicker page3b;
	private LGWizardSummary finalpage;
	private boolean cancelled = false;
	
	
	public LabelGenWizard(JFrame parent) {
		super("ODK Import Wizard", Builder.getImageAsIcon("sidebar-label.png"));
		App.init();
		

		this.parent = parent;
		
		// Setup pages
		pages = new ArrayList<AbstractWizardPanel>();
		page1 = new LGWizardWelcome();
		page2 = new LGWizardWhatStyle();
		page3 = new LGWizardBoxPicker();
		page3b = new LGWizardSamplePicker();
		finalpage = new LGWizardSummary();
		
		if(App.prefs.getBooleanPref(PrefKey.LABEL_WIZARD_HIDE_INTRO, false)==false)
		{
			// Only show intro if not asked to hide
			pages.add(page1);	
		}
		
		pages.add(page2);
		pages.add(page3);
		pages.add(page3b);
		pages.add(finalpage);
		
		setupGui();
		this.setModal(true);
		this.setVisible(true);
		
	}

	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("next")) {
			showNextPage();
		} else if (evt.getActionCommand().equals("previous")) {
			showPreviousPage();
		} else if (evt.getActionCommand().equals("close")) {
			this.cancelled = true;		
			this.setVisible(false);
		}

	}
	
	/**
	 * Whether the user cancelled the wizard or not
	 * 
	 * 
	 * @return
	 */
	public boolean wasCancelled()
	{
		return this.cancelled;
	}
	
	/**
	 * Open a temp PDF file of the specified label style with the provided items
	 *  
	 */
	@SuppressWarnings("unchecked")
	public void openLabelsPDF()
	{
		AbstractTellervoLabelStyle style = page2.getLabelStyle();
		
		if(style==null) return;
		
		ItemType itemType = style.getItemType();
		ArrayList boxes;
		ArrayList samples;
		
		File outputFile;
		try {
			outputFile = File.createTempFile("boxlabel", ".pdf");
			outputFile.deleteOnExit();
			FileOutputStream output = new FileOutputStream(outputFile);
			
			if(itemType.equals(ItemType.BOX))
			{
				boxes = page3.getBoxes();
				
				if(page3.getSortType().equals(BoxSortType.NO_SORT))
				{
					
				}
				else if(page3.getSortType().equals(BoxSortType.TITLE))
				{
					Collections.sort(boxes, new Comparator<WSIBox>() {

						@Override
						public int compare(WSIBox o1, WSIBox o2) {
							return o1.getTitle().compareTo(o2.getTitle());
						}
					});
				}
				else if(page3.getSortType().equals(BoxSortType.LOCATION))
				{
					Collections.sort(boxes, new Comparator<WSIBox>() {

						@Override
						public int compare(WSIBox o1, WSIBox o2) {
							
							String cl1 = o1.getCurationLocation();
							String cl2 = o2.getCurationLocation();
							
							if(cl1==null ) cl1 = "";
							if(cl2==null) cl2 = "";

							// If the location is the same then sort by title
							if(cl1.compareTo(cl2)==0)
							{
								return o1.getTitle().compareTo(o2.getTitle());
							}
							
							return cl1.compareTo(cl2);
							
						}
					});
				}
				else if(page3.getSortType().equals(BoxSortType.CREATED_TIMESTAMP))
				{
					Collections.sort(boxes, new Comparator<WSIBox>() {

						@Override
						public int compare(WSIBox o1, WSIBox o2) {
							return o1.getCreatedTimestamp().getValue().compare(o2.getCreatedTimestamp().getValue());

						}
					});
				}
				else if(page3.getSortType().equals(BoxSortType.LAST_MODIFIED_TIMESTAMP))
				{
					Collections.sort(boxes, new Comparator<WSIBox>() {

						@Override
						public int compare(WSIBox o1, WSIBox o2) {
							return o1.getLastModifiedTimestamp().getValue().compare(o2.getLastModifiedTimestamp().getValue());

						}
					});
				}
				
				style.outputPDFToStream(output, boxes);
			}
			
			if(itemType.equals(ItemType.SAMPLE))
			{
				samples = page3b.getSamples();
				style.outputPDFToStream(output, samples);
			}
		
			Desktop.getDesktop().open(outputFile);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
