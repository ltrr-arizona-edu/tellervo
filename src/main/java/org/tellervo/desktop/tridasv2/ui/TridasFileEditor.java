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

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasLocationGeometry;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;


/**
 * @author Peter Brewer
 *
 */
public class TridasFileEditor extends AbstractPropertyEditor {
	
	private TridasFileArrayRenderer label;
	private JButton cancelButton;
	private JButton editButton;
	private ArrayList<TridasFile> fileList;
	
	/**
	 * 
	 */
	public TridasFileEditor() {
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
		((JPanel) editor).add("*", label = new TridasFileArrayRenderer());
		label.setOpaque(false);
		
		editButton = ComponentFactory.Helper.getFactory().createMiniButton();
		editButton.setText("");
		editButton.setIcon(Builder.getIcon("note.png", 16));
		
		((JPanel) editor).add(editButton);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFileList();
			}
		});
		((JPanel) editor).add(cancelButton = ComponentFactory.Helper.getFactory()
				.createMiniButton());
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
		
		
		
		return this.fileList;
	}
	
	@Override
	public void setValue(Object value) {
		
		if(value instanceof TridasFile)
		{
			fileList = new ArrayList<TridasFile>();
			fileList.add((TridasFile) value);
		}
		else
		{
			fileList = (ArrayList<TridasFile>) value;
		}
		
		label.setValue(value);
	}
	
	/**
	 * Remove the current list
	 */
	private void selectNull() {
		ArrayList<TridasFile> oldFileList = (ArrayList<TridasFile>) fileList.clone();
		label.setValue(null);
		fileList = null;
		
		firePropertyChange(oldFileList, fileList);
	}
	
	/**
	 * Pop up a dialog and select a new list
	 */
	@SuppressWarnings("unchecked")
	private void setFileList() {
		ArrayList<TridasFile> oldFileList;
		if(fileList!=null)
		{
			oldFileList = (ArrayList<TridasFile>) fileList.clone();
		}
		else
		{
			oldFileList = null;
		}
		TridasFileListDialog dialog = new TridasFileListDialog(editButton, fileList);
		
		// show the dialog...
		dialog.setVisible(true);
		
		// cancelled...
		if(!dialog.hasResults()) return;
		
		fileList = dialog.getFileList();
		label.setValue(fileList);
		firePropertyChange(oldFileList, fileList);
	}
}
