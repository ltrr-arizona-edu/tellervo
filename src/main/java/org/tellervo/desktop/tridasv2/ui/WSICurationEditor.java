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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.curation.CurationDialog;
import org.tellervo.desktop.curation.CurationEventDialog;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasSample;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;


/**
 * @author Peter Brewer
 *
 */
public class WSICurationEditor extends AbstractPropertyEditor {
	  private final static Logger log = LoggerFactory.getLogger(WSICurationEditor.class);

	private TridasObjectGenericFieldRenderer label;
	private JButton editButton;
	private JButton viewButton;
	private String value;

	
	/**
	 * 
	 */
	public WSICurationEditor(final TridasSample sample) {
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
		((JPanel) editor).add("*", label = new TridasObjectGenericFieldRenderer());
		label.setOpaque(false);
		
		editButton = ComponentFactory.Helper.getFactory().createMiniButton();
		editButton.setText("");
		editButton.setIcon(Builder.getIcon("note.png", 16));
		editButton.setToolTipText("Edit curation status");

		viewButton = ComponentFactory.Helper.getFactory().createMiniButton();
		viewButton.setText("");
		viewButton.setIcon(Builder.getIcon("clock.png", 16));
		viewButton.setToolTipText("View curation history");
		
		((JPanel) editor).add(viewButton);
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CurationDialog dialog = new CurationDialog(sample, viewButton);
				dialog.setVisible(true);
			}
		});
		
		((JPanel) editor).add(editButton);
		WSICurationEditor glue = this;
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CurationEventDialog dialog = new CurationEventDialog(editButton, sample);
				dialog.setUndecorated(true);
				dialog.setVisible(true);
			}
		});
		
		((JPanel) editor).setOpaque(false);
	}
	
	@Override
	public Object getValue() {
		
		
		
		return value;
	}
	
	@Override
	public void setValue(Object value) {
		
		log.debug("setValue in WSICurationEditor passed: "+value);
		
		if(value instanceof String)
		{
			this.value = (String) value;
		}
		else
		{
			
		}
		
		label.setValue(value);
	} 
	
	/**
	 * Remove the current list
	 */
	private void selectNull() {
		/*ArrayList<TridasFile> oldFileList = (ArrayList<TridasFile>) fileList.clone();
		label.setValue(null);
		fileList = null;
		
		firePropertyChange(oldFileList, fileList);*/
	}
	
	/**
	 * Pop up a dialog and select a new list
	 */
	@SuppressWarnings("unchecked")
	private void setFileList() {
		/*ArrayList<TridasFile> oldFileList;
		if(fileList!=null)
		{
			oldFileList = (ArrayList<TridasFile>) fileList.clone();
		}
		else
		{
			oldFileList = null;
		}
		TridasFileListDialog dialog = new TridasFileListDialog(label, fileList);
		
		// show the dialog...
		dialog.setVisible(true);
		
		// cancelled...
		if(!dialog.hasResults()) return;
		
		fileList = dialog.getFileList();
		label.setValue(fileList);
		firePropertyChange(oldFileList, fileList);*/
	}
}
