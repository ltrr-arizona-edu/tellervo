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
package org.tellervo.desktop.tridasv2.ui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tellervo.desktop.ui.Builder;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.itextpdf.text.Font;


public class TridasDefaultPropertyEditor extends AbstractPropertyEditor {

	private JLabel label;
	private JButton button;
	private Object value;

	public TridasDefaultPropertyEditor() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setOpaque(false);
		
		label = new JLabel("");
		label.setForeground(Color.GRAY.brighter());
		label.setFont(label.getFont().deriveFont(Font.ITALIC));
		label.setOpaque(false);
		panel.add(label);

		// set class editor
		editor = panel;

		button = new JButton();
		button.setIcon(Builder.getIcon("cancel.png", 16));
		button.setMargin(new Insets(0,5,0,5));
		
		panel.add(Box.createHorizontalGlue());
		panel.add(button);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectNull();
			}
		});
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		label.setText(value == null ? "" : " present");
		button.setVisible(value == null ? false : true);
	}

	protected void selectNull() {
		Object oldValue = value;
		label.setText("");
		value = null;
		firePropertyChange(oldValue, null);
	}
}

