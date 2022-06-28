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
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.tridas.schema.TridasProject;
import org.tridas.util.TridasObjectEx;


public class TridasProjectRenderer implements ListCellRenderer {
	public TridasProjectRenderer() {
		panel = new JPanel();
		lblTitle = new JLabel("code");		
		lblPI = new JLabel("name");

		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		
		panel.add(lblTitle);
		panel.add(lblPI);	
	}
	
	private Boolean hideTitles = false;
	private JPanel panel;
	private JLabel lblTitle;
	private JLabel lblPI;
	private int maximumTitleLength = -1;
	
	public void setHideTitles(Boolean b)
	{
		hideTitles = b;
	}
	
	public void setMaximumTitleLength(int maximumTitleLength) {
		this.maximumTitleLength = maximumTitleLength;
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		JPanel c = panel;
		
		if(isSelected)
			c.setBackground(list.getSelectionBackground());
		else
			c.setBackground(index % 2 == 0 ? ElementListCellRenderer.BROWSER_ODD_ROW_COLOR
					: Color.white);	

		if(value instanceof TridasProject) {
			TridasProject project = (TridasProject) value;
			
			// Full name of project
			String name = project.getTitle();
			String pi = project.getInvestigator();
						
			if(maximumTitleLength > 0 && name.length() > maximumTitleLength)
				name = name.substring(0, maximumTitleLength) + "..."; 
			
			// Set main info in table
			lblTitle.setText(name);
			lblPI.setText(pi);
			Font font = list.getFont();
			lblTitle.setFont(font);
			lblPI.setFont(font.deriveFont(font.getSize() - 2.0f));			
		
			
			
		} else if(value instanceof String) {
			lblTitle.setText((String)value);
			lblPI.setText("");
			
			// yellow background
			if(!isSelected)
				c.setBackground(new Color(255, 255, 200));
			
			Font font = list.getFont();
			lblTitle.setFont(font.deriveFont(Font.BOLD));
			lblPI.setFont(font.deriveFont(font.getSize() - 2.0f));			
		}
		
		
		if(hideTitles)
		{
			lblPI.setVisible(false);
		}
		
		return c;
	}
}
