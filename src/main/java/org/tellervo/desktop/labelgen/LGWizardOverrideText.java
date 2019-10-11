package org.tellervo.desktop.labelgen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class LGWizardOverrideText extends AbstractWizardPanel implements ActionListener{


	private static final long serialVersionUID = 1L;
	private AbstractTellervoLabelStyle style;
	private JTextField txtLine1;
	private JTextField txtLine2;
	private JTextField txtLine3;
	private JTextField txtLine4;
	
	private JLabel lblLine1;
	private JLabel lblLine2;
	private JLabel lblLine3;
	private JLabel lblLine4;
	
	private LGWizardWhatStyle stylePage;

	/**
	 * Create the panel.
	 */
	public LGWizardOverrideText(LGWizardWhatStyle stylePage) {
		super("Step 3 - Would you like to override any of the text on the labels?", 
				"Here you have the option of overriding some of the text on the labels. "+
				"If you leave these blank the default values (typically taken from the "+
				"database) will be used ");
		setLayout(new MigLayout("", "[right][203.00,grow,fill]", "[][][][]"));
		
		this.stylePage = stylePage;
		
		lblLine1 = new JLabel("Line 1:");
		add(lblLine1, "cell 0 0,alignx trailing");
		
		txtLine1 = new JTextField();
		add(txtLine1, "cell 1 0,growx");
		
		lblLine2 = new JLabel("Line 2:");
		add(lblLine2, "cell 0 1,alignx trailing");
		
		txtLine2 = new JTextField();
		add(txtLine2, "cell 1 1,growx");
		
		lblLine3 = new JLabel("Line 3:");
		add(lblLine3, "cell 0 2,alignx trailing");
		
		txtLine3 = new JTextField();
		add(txtLine3, "cell 1 2,growx");
		
		lblLine4 = new JLabel("Line 4:");
		add(lblLine4, "cell 0 3,alignx trailing");
		
		txtLine4 = new JTextField();
		add(txtLine4, "cell 1 3,growx");
			
	}
		
	/**
	 * Function to override if tasks should be performed when page 
	 * is shown
	 */
	@Override
	public void initialViewTasks()
	{
		this.style = this.stylePage.getLabelStyle();
		updateGUI();
	}
	
	private void updateGUI()
	{
		lblLine1.setVisible(style.hasConfigurableLine1());
		txtLine1.setVisible(style.hasConfigurableLine1());
		if(style.hasConfigurableLine1())
		{
			lblLine1.setText(style.getLine1Description());
		}
		
		lblLine2.setVisible(style.hasConfigurableLine2());
		txtLine2.setVisible(style.hasConfigurableLine2());
		if(style.hasConfigurableLine2())
		{
			lblLine2.setText(style.getLine2Description());
		}
		
		lblLine3.setVisible(style.hasConfigurableLine3());
		txtLine3.setVisible(style.hasConfigurableLine3());
		if(style.hasConfigurableLine3())
		{
			lblLine3.setText(style.getLine3Description());
		}
		
		lblLine4.setVisible(style.hasConfigurableLine4());
		txtLine4.setVisible(style.hasConfigurableLine4());
		if(style.hasConfigurableLine4())
		{
			lblLine4.setText(style.getLine4Description());
		}
		
	}
	
	@Override
	public boolean doPageValidation()
	{
		
		
		return true;
	}
	
	public String getLine1TextOverride()
	{
		return txtLine1.getText();
	}
	
	public String getLine2TextOverride()
	{
		return txtLine2.getText();
	}
	
	public String getLine3TextOverride()
	{
		return txtLine3.getText();
	}

	public String getLine4TextOverride()
	{
		return txtLine4.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		
	}
	
	



	
	

}
