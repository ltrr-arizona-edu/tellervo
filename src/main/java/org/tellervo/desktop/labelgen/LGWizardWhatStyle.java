package org.tellervo.desktop.labelgen;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.labelgen.AbstractTellervoLabelStyle.ItemType;


public class LGWizardWhatStyle extends AbstractWizardPanel implements ActionListener{


	private static final long serialVersionUID = 1L;
	private JComboBox<AbstractTellervoLabelStyle> cboWhat;
	private JLabel lblDescription;

	/**
	 * Create the panel.
	 */
	public LGWizardWhatStyle() {
		super("Step 1 - What type of labels do you want to print?", 
				"The first step is to define what style of label you would like to print.  Tellervo is preconfigured to print a number of styles of "
				+ "box and sample labels, just pick from the list below");
		setLayout(new MigLayout("", "[][grow]", "[][53.00,grow,top][grow]"));
		
		JLabel lblWhat = new JLabel("Label style:");
		add(lblWhat, "cell 0 0");
		
		cboWhat = new JComboBox<AbstractTellervoLabelStyle>();
		cboWhat.setModel(new DefaultComboBoxModel<AbstractTellervoLabelStyle>(LabelStyleFactory.getAvailableStyles()));
		cboWhat.addActionListener(this);
		cboWhat.setActionCommand("what");
		add(cboWhat, "cell 1 0,growx");
		
		lblDescription = new JLabel("");
		lblDescription.setFont(new Font("Dialog", Font.ITALIC, 11));
		add(lblDescription, "cell 1 1");
		updateGUI();
			
	}
	
	private void updateGUI()
	{
		AbstractTellervoLabelStyle style = getLabelStyle();
		if(style!=null)
		{
			lblDescription.setText(style.getDescription());
			
			// Enable the picker pages depending on the type of data the style uses
			setPageClassToEnableOrDisable(LGWizardBoxPicker.class, style.getItemType().equals(ItemType.BOX));
			setPageClassToEnableOrDisable(LGWizardSamplePicker.class, style.getItemType().equals(ItemType.SAMPLE));
			
		}
		else
		{
			lblDescription.setText("");
		}
	}

	/**
	 * Gets the label style chosen by the user
	 * 
	 * @return
	 */
	public AbstractTellervoLabelStyle getLabelStyle()
	{
		return (AbstractTellervoLabelStyle) cboWhat.getSelectedItem();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("what"))
		{
			updateGUI();
		}
		
	}
	
	



	
	

}
