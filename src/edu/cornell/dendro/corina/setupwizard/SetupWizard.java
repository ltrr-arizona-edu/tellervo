package edu.cornell.dendro.corina.setupwizard;

import java.awt.Container;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.core.App;

public class SetupWizard {

	public static void main(String[] args)
	{
		SetupWizard.showSetupWizard();
		
	}

	public static void showSetupWizard()
	{
		
        Class[] clazz = new Class[] {
                SetupIntroduction.class,
                SetupWebservice.class,
                };
            WizardResultProducer finishIt = new ComputeResult();
            Rectangle size = new Rectangle(500,300);
            Object result = WizardDisplayer.showWizard (
                WizardPage.createWizard(clazz, finishIt), size);
		
	}
	
	private static class ComputeResult implements
	WizardResultProducer {
	
	@Override
	public boolean cancel(Map map) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public Object finish(Map wizardData) throws WizardException {
	    
		for (Iterator i = wizardData.keySet().iterator(); i.hasNext();)
	    {
	        Object key = i.next();
	        Object val = wizardData.get(key);
	        System.out.println(key+":"+val);
	    }
	    
	    if(wizardData.containsKey("webserviceURL"))
	    {
	    	App.prefs.setPref("corina.webservice.url",wizardData.get("webserviceURL").toString());
	    }
	    
		return wizardData;
	}

}

}
