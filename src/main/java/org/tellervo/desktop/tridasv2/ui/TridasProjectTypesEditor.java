/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.tridasv2.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.ControlledVoc;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;


/**
 * @author Peter Brewer
 *
 */
public class TridasProjectTypesEditor extends AbstractPropertyEditor {
	
	private TridasListRenderer label;
	private JButton cancelButton;
	private JButton editButton;
	private ArrayList<ControlledVoc> projectTypesList;
	
	/**
	 * 
	 */
	public TridasProjectTypesEditor() {
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
		((JPanel) editor).add("*", label = new TridasListRenderer());
		label.setOpaque(false);
		
		editButton = ComponentFactory.Helper.getFactory().createMiniButton();
		editButton.setText("");
		editButton.setIcon(Builder.getIcon("edit.png", 16));
		
		((JPanel) editor).add(editButton);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setList();
			}
		});
		((JPanel) editor).add(cancelButton = ComponentFactory.Helper.getFactory().createMiniButton());
		cancelButton.setText("");
		cancelButton.setIcon(Builder.getIcon("cancel.png", 16));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectNull();
			}
		});
		((JPanel) editor).setOpaque(false);
	}
	
	@Override
	public Object getValue() {

		return projectTypesList;
	}
	
	@Override
	public void setValue(Object value) {
		
		if(value instanceof ControlledVoc)
		{
			projectTypesList = new ArrayList<ControlledVoc>();
			projectTypesList.add((ControlledVoc) value);
		}
		else
		{
			projectTypesList = (ArrayList<ControlledVoc>) value;
		}
		
		label.setValue(value);
	}
	
	/**
	 * Remove the current list
	 */
	private void selectNull() {
		try{
			ArrayList<ControlledVoc> oldList = (ArrayList<ControlledVoc>) projectTypesList.clone();
			
			label.setValue(null);
			projectTypesList = null;
			
			firePropertyChange(oldList, projectTypesList);
		} catch (NullPointerException e)
		{
			
		}
	}
	
	/**
	 * Pop up a dialog and select a new list
	 */
	@SuppressWarnings("unchecked")
	private void setList() {
		ArrayList<ControlledVoc> oldList;
		if(projectTypesList!=null)
		{
			oldList = (ArrayList<ControlledVoc>) projectTypesList.clone();
		}
		else
		{
			oldList = null;
		}
		TridasProjectTypesDialog dialog = new TridasProjectTypesDialog(label, oldList);
		
		// show the dialog...
		dialog.setVisible(true);
		
		// cancelled...
		if(!dialog.hasResults()) return;
		
		ArrayList<ControlledVoc> newList = dialog.getList();
		
		
		projectTypesList = newList;
		firePropertyChange(oldList, projectTypesList);
		label.setValue(projectTypesList);
		

	}
}
