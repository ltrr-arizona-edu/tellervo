package org.tellervo.desktop.labelgen;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardDialog;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.labelgen.AbstractTellervoLabelStyle.ItemType;
import org.tellervo.desktop.ui.Builder;

public class LabelGenWizard extends AbstractWizardDialog {

	private static final long serialVersionUID = 1L;
	private LGWizardWelcome page1;
	private LGWizardWhatStyle page2;
	private LGWizardBoxPicker page3;
	private LGWizardSamplePicker page3b;
	private LGWizardSummary finalpage;
	private boolean cancelled = false;
	
	
	public LabelGenWizard(JFrame parent) {
		super("ODK Import Wizard", Builder.getImageAsIcon("sidebar.png"));
		App.init();
		

		this.parent = parent;
		
		// Setup pages
		pages = new ArrayList<AbstractWizardPanel>();
		page1 = new LGWizardWelcome();
		page2 = new LGWizardWhatStyle();
		page3 = new LGWizardBoxPicker();
		page3b = new LGWizardSamplePicker();
		finalpage = new LGWizardSummary();
		
		pages.add(page1);
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
			FileOutputStream output = new FileOutputStream(outputFile);
			
			if(itemType.equals(ItemType.BOX))
			{
				boxes = page3.getBoxes();
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
