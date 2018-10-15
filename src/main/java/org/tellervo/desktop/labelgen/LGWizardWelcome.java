package org.tellervo.desktop.labelgen;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;

import javax.swing.JCheckBox;

import java.awt.Color;

import net.miginfocom.swing.MigLayout;


public class LGWizardWelcome extends AbstractWizardPanel {


	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public LGWizardWelcome() {
		super("Label Printing Wizard", 
				"This wizard will take you through the steps to generate labels for boxes or samples from your database.");
		setLayout(new MigLayout("", "[271px,grow,right]", "[grow][23px]"));
		
		JCheckBox chkNoIntro = new JCheckBox("Don't show this introduction again");
		chkNoIntro.setBackground(Color.WHITE);
		add(chkNoIntro, "cell 0 1,alignx right,aligny top");
		
		new CheckBoxWrapper(chkNoIntro, PrefKey.LABEL_WIZARD_HIDE_INTRO, App.prefs.getBooleanPref(PrefKey.LABEL_WIZARD_HIDE_INTRO, false));
		
		


	}

}
