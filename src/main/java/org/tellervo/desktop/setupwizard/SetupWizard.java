package org.tellervo.desktop.setupwizard;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardDialog;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.ui.Builder;

public class SetupWizard extends AbstractWizardDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;

	public SetupWizard(JFrame parent, ArrayList<AbstractWizardPanel> pages) {
		
		super(parent,pages, "Setup Wizard", Builder.getImageAsIcon("sidebar.png"));
			
	}
	
	public static void launchWizard()
	{
		App.init();

		ArrayList<AbstractWizardPanel> pages = new ArrayList<AbstractWizardPanel>();
		pages.add(new WizardWelcome());
		pages.add(new WizardModeChooser());
		pages.add(new WizardProxy());
		pages.add(new WizardServer());
		pages.add(new WizardHardwareAsk());
		pages.add(new WizardHardwareDo());
		pages.add(new WizardHardwareTest());
		pages.add(new WizardFinish());
		
		JDialog dialog = new SetupWizard(null, pages);
		
		dialog.setVisible(true);
		
	}
	
	public static void launchFirstRunWizard()
	{
		App.init();

		ArrayList<AbstractWizardPanel> pages = new ArrayList<AbstractWizardPanel>();
		pages.add(new WizardFirstRunWelcome());
		pages.add(new WizardProxy());
		pages.add(new WizardServer());
		pages.add(new WizardHardwareAsk());
		pages.add(new WizardHardwareDo());
		pages.add(new WizardFinish());
		
		JDialog dialog = new SetupWizard(null, pages);
		
		dialog.setVisible(true);
	}
	


}
