/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui.dbbrowse;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.tellervo.desktop.gui.widgets.AutoCompletion;

import net.miginfocom.swing.MigLayout;

public class SearchParameterPanel_UI_2 extends JPanel {
	private static final long serialVersionUID = 1L;
	
    protected JComboBox cboSearchField;
    protected JComboBox cboSearchOperator;
    protected JTextField txtSearchText;
    protected JButton btnRemove;
    private JLabel lblParameterOperator;
    
    public SearchParameterPanel_UI_2() {
    	setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
    	setBackground(Color.LIGHT_GRAY);
 
    	initComponents();
    }
    
	public void setParameterOperator(String operator)
	{
		lblParameterOperator.setText(operator);
	}
    
    private void initComponents() {
  
		setLayout(new MigLayout("", "[][31px,grow,fill][][]", "[24px,top][]"));
		
		lblParameterOperator = new JLabel("AND");
		add(lblParameterOperator, "cell 0 0,alignx trailing,aligny center");
		
		cboSearchField = new JComboBox();
		AutoCompletion.enable(cboSearchField);
		add(cboSearchField, "cell 1 0,alignx left,aligny top");
		
		cboSearchOperator = new JComboBox();
		add(cboSearchOperator, "cell 2 0,growx");
		
		btnRemove = new JButton("");
		add(btnRemove, "cell 3 0");
		
		txtSearchText = new JTextField();
		add(txtSearchText, "cell 1 1 2 1,growx");
		txtSearchText.setColumns(10);
		
    }
}
