package edu.cornell.dendro.corina.setupwizard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

public class WizardHardware1 extends AbstractWizardPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JRadioButton radYes; 
	private JRadioButton radNo;
	
	
	public WizardHardware1() {
		super("Setting up measuring platform", 
		"Corina supports a variety of dendro measuring platforms. " +
		" Would you like to configure a platform now?");
		
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
			this.setPageClassToEnableOrDisable(WizardHardware2.class, true);
		}
		else
		{
			this.setPageClassToEnableOrDisable(WizardHardware2.class, false);	
		}
		
	}

}
