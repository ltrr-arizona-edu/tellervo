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
import org.tellervo.desktop.curation.CurationDialog;
import org.tellervo.desktop.curation.CurationEventDialog;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.schema.CurationStatus;
import org.tridas.schema.TridasGenericField;
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

	private TridasGenericFieldRenderer label;
	private JButton editButton;
	private JButton viewButton;
	private String value;
	private TridasSample sample;

	
	/**
	 * 
	 */
	public WSICurationEditor(final TridasSample sample) {
		this.sample = sample;
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
		((JPanel) editor).add("*", label = new TridasGenericFieldRenderer());
		label.setOpaque(false);
		
		editButton = ComponentFactory.Helper.getFactory().createMiniButton();
		editButton.setText("");
		editButton.setIcon(Builder.getIcon("edit.png", 16));
		editButton.setToolTipText("Edit curation status");

		viewButton = ComponentFactory.Helper.getFactory().createMiniButton();
		viewButton.setText("");
		viewButton.setIcon(Builder.getIcon("clock.png", 16));
		viewButton.setToolTipText("View curation history");
		
		((JPanel) editor).add(viewButton);
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CurationDialog dialog = new CurationDialog(sample, editor);
				dialog.setVisible(true);
				
				if(dialog.wasChanged())
				{
					String newLabel = dialog.getCurrentCurationStatus();
					setValue(newLabel);
				}
				else
				{
					return;
				}
				
				
			}
		});
		
		((JPanel) editor).add(editButton);

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CurationEventDialog dialog = new CurationEventDialog(editor, sample);
				dialog.setUndecorated(true);
				
				dialog.setVisible(true);
				
				if(dialog.wasChanged())
				{
					String newLabel = dialog.getCurationStatus().value();
					setValue(newLabel);
				}
				else
				{
					return;
				}
			}
		});
		
		((JPanel) editor).setOpaque(false);
	}

	
	@Override
	public void setValue(Object value) {
		
		log.debug("setValue in WSICurationEditor passed: "+value);
		TridasGenericFieldRenderer oldLabel = label;

		if(value ==null)
		{
			log.debug("Value is null");

			label.setValue(null);
			
		
		}
		if(value instanceof String)
		{
			log.debug("Value is a string");
			this.value = (String) value;
			
			TridasGenericField gf = new TridasGenericField();
			gf.setName("tellervo.curationStatus");
			gf.setValue((String) value);
			ArrayList<TridasGenericField> gfs = new ArrayList<TridasGenericField>();
			gfs.add(gf);
			label.setValue(gfs);
			
			firePropertyChange(oldLabel, label);
			
		}
		else if (value instanceof CurationStatus)
		{
			log.debug("Value is a CurationStatus");
			this.value = ((CurationStatus) value).value();
			
			TridasGenericField gf = new TridasGenericField();
			gf.setName("tellervo.curationStatus");
			gf.setValue(this.value);
			ArrayList<TridasGenericField> gfs = new ArrayList<TridasGenericField>();
			gfs.add(gf);
			label.setValue(gfs);
			
			firePropertyChange(oldLabel, label);
		}
		else 
		{
			log.debug("Value is "+value.getClass());
		}
		

	} 
	
	/**
	 * Remove the current list
	 */
	private void selectNull() {
		String oldValue = value;
		label.setValue(null);
		value = null;
		
		firePropertyChange(oldValue, value);
	}
	

}
