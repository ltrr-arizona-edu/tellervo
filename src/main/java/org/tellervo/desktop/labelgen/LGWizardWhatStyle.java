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
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.ComboByIndexWrapper;

import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;

import java.awt.Color;


public class LGWizardWhatStyle extends AbstractWizardPanel implements ActionListener{


	private static final long serialVersionUID = 1L;
	private JComboBox<AbstractTellervoLabelStyle> cboWhat;
	private JTextArea lblDescription;
	private JLabel lblFullPageImage;
	private JLabel lblLabelImage;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public LGWizardWhatStyle() {
		super("Step 1 - What type of labels do you want to print?", 
				"The first step is to define what style of label you would like to print.  Tellervo is preconfigured to print a number of styles of "
				+ "box and sample labels, just pick from the list below");
		setLayout(new MigLayout("", "[][203.00,grow][53.00,grow]", "[][grow,top][grow,top]"));
		
		JLabel lblWhat = new JLabel("Label style:");
		add(lblWhat, "cell 0 0");
		
		cboWhat = new JComboBox<AbstractTellervoLabelStyle>();
		cboWhat.setModel(new DefaultComboBoxModel<AbstractTellervoLabelStyle>(LabelStyleFactory.getAvailableStyles()));
		cboWhat.addActionListener(this);
		cboWhat.setActionCommand("what");
		add(cboWhat, "cell 1 0 2 1,growx");
		
		new ComboByIndexWrapper(cboWhat, PrefKey.LABEL_WIZARD_STYLE, 0, LabelStyleFactory.getAvailableStyles());
		
		
		lblFullPageImage = new JLabel("");
		add(lblFullPageImage, "cell 1 1 1 2,aligny top");
		
		lblLabelImage = new JLabel("");
		add(lblLabelImage, "cell 2 2,aligny top");
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "cell 2 1,grow");
		panel.setLayout(new MigLayout("", "[53.00,grow]", "[grow]"));
		
		lblDescription = new JTextArea("");
		panel.add(lblDescription, "cell 0 0,growx,wmin 10,aligny center");
		lblDescription.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblDescription.setBackground(null);
		lblDescription.setEditable(false);
		lblDescription.setWrapStyleWord(true);
		lblDescription.setLineWrap(true);
		lblDescription.setBorder(new EmptyBorder(5, 5, 5, 5));
		updateGUI();
			
	}
	
	private void updateGUI()
	{
		AbstractTellervoLabelStyle style = getLabelStyle();
		if(style!=null)
		{
			try{
			lblDescription.setText(style.getDescription());
			lblFullPageImage.setIcon(style.getPageImage());
			lblLabelImage.setIcon(style.getLabelImage());
			
			// Enable the picker pages depending on the type of data the style uses
			setPageClassToEnableOrDisable(LGWizardBoxPicker.class, style.getItemType().equals(ItemType.BOX));
			setPageClassToEnableOrDisable(LGWizardSamplePicker.class, style.getItemType().equals(ItemType.SAMPLE));
			} catch ( Exception e)
			{
				e.printStackTrace();
				
			}
			
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
