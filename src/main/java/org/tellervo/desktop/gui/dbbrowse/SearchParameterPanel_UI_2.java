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
