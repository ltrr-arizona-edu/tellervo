package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchParameterPanel_UI_2 extends JPanel {
	private static final long serialVersionUID = 1L;
	
    protected JComboBox cboSearchField;
    protected JComboBox cboSearchOperator;
    protected JTextField txtSearchText;
    protected JButton btnRemove;
    
    public SearchParameterPanel_UI_2() {
    	initComponents();
    }
    
    private void initComponents() {
    	cboSearchField = new JComboBox();
    	
    	// some dummy field to make it default width
    	cboSearchField.setPrototypeDisplayValue("Series datingErrorPositive");
    	
    	cboSearchOperator = new JComboBox();
    	txtSearchText = new JTextField();
    
    	btnRemove = new JButton();
    	
    	//
    	// [search param] | [operator] | [remove] 
    	// ----------------------------------------
    	// [ search value............]
    	//
    	
    	setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	
    	c.anchor = GridBagConstraints.LINE_START;
    	c.gridx = 0;
    	c.gridy = 0;
    	c.weightx = 0.7;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	add(cboSearchField, c);
    	
    	c.weightx = 0.3;
    	c.gridx++;
    	add(cboSearchOperator, c);
    	
    	c.gridy++;
    	c.gridx = 0;
    	c.gridwidth = 2;
    	add(txtSearchText, c);
    	
    	c.anchor = GridBagConstraints.FIRST_LINE_END;
    	c.fill = GridBagConstraints.NONE;
    	c.weightx = 0;
    	c.gridx = 2;
    	c.gridy = 0;
    	add(btnRemove, c);
    }
}
