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

import javax.swing.JButton;
import javax.swing.JPanel;

import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasLocationGeometry;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;


/**
 * @author Lucas Madar
 *
 */
public class LocationGeometryEditor extends AbstractPropertyEditor {
	
	private LocationGeometryRenderer label;
	private JButton button;
	private JButton gpsbutton;
	private TridasLocationGeometry geometry;
	
	/**
	 * 
	 */
	public LocationGeometryEditor() {
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
		((JPanel) editor).add("*", label = new LocationGeometryRenderer());
		label.setOpaque(false);
		
		gpsbutton = ComponentFactory.Helper.getFactory().createMiniButton();
		gpsbutton.setText("");
		gpsbutton.setIcon(Builder.getIcon("edit.png", 16));
		
		((JPanel) editor).add(gpsbutton);
		gpsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectGeometry();
			}
		});
		((JPanel) editor).add(button = ComponentFactory.Helper.getFactory()
				.createMiniButton());
		button.setText("");
		button.setIcon(Builder.getIcon("cancel.png", 16));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectNull();
			}
		});
		((JPanel) editor).setOpaque(false);
	}
	
	@Override
	public Object getValue() {
		return geometry;
	}
	
	@Override
	public void setValue(Object value) {
		geometry = (TridasLocationGeometry) value;
		label.setValue(value);
	}
	
	/**
	 * Remove the geometry
	 */
	private void selectNull() {
		TridasLocationGeometry oldGeometry = geometry;
		label.setValue(null);
		geometry = null;
		
		firePropertyChange(oldGeometry, geometry);
	}
	
	/**
	 * Pop up a dialog and select a new geometry
	 */
	private void selectGeometry() {
		TridasLocationGeometry oldGeometry = geometry;
		LocationGeometry dialog = new LocationGeometry();
		
		// show the dialog...
		dialog.showDialog(editor, geometry);
		
		// cancelled...
		if(!dialog.hasResults()) return;
		
		geometry = dialog.getGeometry();
		label.setValue(geometry);
		firePropertyChange(oldGeometry, geometry);
	}
}
