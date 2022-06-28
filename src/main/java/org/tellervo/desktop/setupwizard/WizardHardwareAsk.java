package org.tellervo.desktop.setupwizard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;

import net.miginfocom.swing.MigLayout;

public class WizardHardwareAsk extends AbstractWizardPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JRadioButton radYes; 
	private JRadioButton radNo;
	
	
	public WizardHardwareAsk() {
		super("Setting up measuring platform", 
		"Tellervo supports a variety of dendro measuring platforms. If you have a platform already attached " +
		"to your computer you can go ahead and configure it now.  Otherwise, you should skip this step and set" +
		"up your platform on another ocassion.  Serial cables should only be plugged in to your computer when " +
		"the computer is turned off. \n\n" +
		"Would you like to configure a platform now?");
		
		setLayout(new MigLayout("", "[450px,fill]", "[][][][grow]"));
		
		
		ButtonGroup grp = new ButtonGroup();

		
		radYes = new JRadioButton("Yes, I'd like to configure a measuring platform.");
		radYes.addActionListener(this);
		
		radYes.setSelected(true);
		
		
		add(radYes, "cell 0 1,alignx left,aligny top");
		grp.add(radYes);
		
		radNo = new JRadioButton("No, I'd like to skip this step.");
		radNo.addActionListener(this);
		add(radNo, "cell 0 2");
		grp.add(radNo);
		


	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(radYes.isSelected())
		{
			this.setPageClassToEnableOrDisable(WizardHardwareDo.class, true);
			this.setPageClassToEnableOrDisable(WizardHardwareTest.class, true);
		}
		else
		{
			this.setPageClassToEnableOrDisable(WizardHardwareDo.class, false);
			this.setPageClassToEnableOrDisable(WizardHardwareTest.class, false);	
		}
		
	}

}
