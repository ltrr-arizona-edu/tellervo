package org.tellervo.desktop.gui.dbbrowse;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

public class SearchParameterPanel_UI_3 extends JPanel {

	private static final long serialVersionUID = 1L;
	protected JComboBox cboSearchField;
	protected JComboBox cboSearchOperator;
    protected JTextField txtSearchText;
    protected JButton btnRemove;
    private JLabel lblParameterOperator;
    

	/**
	 * Create the panel.
	 */
	public SearchParameterPanel_UI_3() {

		initGui();
		
		
	}
	
	public void setParameterOperator(String operator)
	{
		lblParameterOperator.setText(operator);
	}
	
	
	private void initGui()
	{
		setLayout(new MigLayout("", "[][31px,grow,fill][][]", "[24px,grow,top][]"));
		
		lblParameterOperator = new JLabel("AND");
		add(lblParameterOperator, "cell 0 0,alignx trailing,aligny center");
		
		cboSearchField = new JComboBox();
		add(cboSearchField, "cell 1 0,alignx left,aligny top");
		
		cboSearchOperator = new JComboBox();
		add(cboSearchOperator, "cell 2 0,growx");
		
		btnRemove = new JButton("X");
		add(btnRemove, "cell 3 0");
		
		txtSearchText = new JTextField();
		add(txtSearchText, "cell 1 1 2 1,growx");
		txtSearchText.setColumns(10);
	}

}
