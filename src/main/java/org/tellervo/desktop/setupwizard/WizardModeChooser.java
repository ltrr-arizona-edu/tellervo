package org.tellervo.desktop.setupwizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import java.awt.Font;

public class WizardModeChooser extends AbstractWizardPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JRadioButton radTellervoStandard; 
	private JRadioButton radTellervoLite;
	private JLabel lblTellervoLiteIcon;
	private JLabel lblrequiresTellervoServer;
	private JLabel lblbasicMeasuringOnly;

	public WizardModeChooser(){
		super("Tellervo or Tellervo-lite", 
				"Tellervo can be run in standard or 'lite' mode.  Standard mode requires access to a Tellervo server to"
						+ " store and manage data.  Standard mode is required for all the advanced features of Tellervo including "
						+ "3D mapping, barcoding, databasing and data manipulation.  Tellervo-lite, however, provides the basic "
						+ "functionality necessary to measure samples and save data to legacy data files such as Tucson RWL. Tellervo-lite "
						+ "does not require the separate Tellervo server.\n\n"
						+ "Please choose what mode you would like to run Tellervo in:");

		setLayout(new MigLayout("", "[fill][382.00]", "[][][][12.00][][][grow]"));


		ButtonGroup grp = new ButtonGroup();


		radTellervoStandard = new JRadioButton("");
		radTellervoStandard.addActionListener(this);

		radTellervoStandard.setSelected(true);


		add(radTellervoStandard, "cell 0 1,alignx left,aligny center");
		grp.add(radTellervoStandard);

		radTellervoLite = new JRadioButton("");
		radTellervoLite.addActionListener(this);
		
		JLabel lblTellervoIcon = new JLabel("");
		lblTellervoIcon.setIcon(Builder.getIcon("tellervo.png", 32));
		add(lblTellervoIcon, "cell 1 1");
		
		lblrequiresTellervoServer = new JLabel("(requires access to a Tellervo server)");
		lblrequiresTellervoServer.setFont(new Font("Dialog", Font.PLAIN, 10));
		add(lblrequiresTellervoServer, "cell 1 2");
		add(radTellervoLite, "cell 0 4,aligny center");
		grp.add(radTellervoLite);
		
		lblTellervoLiteIcon = new JLabel("");
		lblTellervoLiteIcon.setIcon(Builder.getIcon("tellervolite.png", 32));
		add(lblTellervoLiteIcon, "cell 1 4");
		
		lblbasicMeasuringOnly = new JLabel("(basic measuring only, no advanced features such as analysis, mapping, databasing etc)");
		lblbasicMeasuringOnly.setFont(new Font("Dialog", Font.PLAIN, 10));
		add(lblbasicMeasuringOnly, "cell 1 5");

		radTellervoLite.setSelected(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false));

	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(radTellervoStandard.isSelected())
		{
			App.prefs.setBooleanPref(PrefKey.WEBSERVICE_DISABLED, false);
			this.setPageClassToEnableOrDisable(WizardProxy.class, true);
			this.setPageClassToEnableOrDisable(WizardServer.class, true);
		}
		else
		{
			App.prefs.setBooleanPref(PrefKey.WEBSERVICE_DISABLED, true);
			this.setPageClassToEnableOrDisable(WizardProxy.class, false);
			this.setPageClassToEnableOrDisable(WizardServer.class, false);	
		}
	}
}





