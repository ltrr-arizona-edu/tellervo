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

import org.tridas.util.TridasObjectEx;


public class TridasObjectRenderer implements ListCellRenderer {
	public TridasObjectRenderer() {
		panel = new JPanel();
		lblCode = new JLabel("code");		
		lblName = new JLabel("name");

		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		
		panel.add(lblCode);
		panel.add(lblName);	
	}
	
	private JPanel panel;
	private JLabel lblCode;
	private JLabel lblName;
	private int maximumTitleLength = -1;
	
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

		if(value instanceof TridasObjectEx) {
			TridasObjectEx site = (TridasObjectEx) value;
			
			// Full name of site
			String name = site.getTitle();
			
			// Compile code for object, giving parent codes also where applicable
			String code = null;
			TridasObjectEx parent;
			if((parent = site.getParent()) != null) {
				// Site code with parent code
				if(parent.hasLabCode())	{
					code = parent.getLabCode()+">"+site.getLabCode();
				}else{
					code = site.getLabCode();
				}
			}
			else{
				// Code for this site
				code = site.getLabCode();
			}
			
			if(maximumTitleLength > 0 && name.length() > maximumTitleLength)
				name = name.substring(0, maximumTitleLength) + "..."; 
			
			// Set main info in table
			lblCode.setText(code);
			lblName.setText(name);
			Font font = list.getFont();
			lblCode.setFont(font);
			lblName.setFont(font.deriveFont(font.getSize() - 2.0f));			
		
			// Build a more comprehensive tool tip for object
			StringBuffer sb = new StringBuffer();
			sb.append("<html>");
			if(site.hasLabCode()) {
				sb.append("<b>Code:</b> ");
				sb.append(site.getLabCode());
				sb.append("<br>");
			}
			sb.append("<b>Title:</b> ");
			sb.append(site.getTitle());
			sb.append("<br>");
			
			// add parent info, if we can...
			if((parent = site.getParent()) != null) {
				if(parent.hasLabCode()) {
					sb.append("<b>Parent Code:</b> ");
					sb.append(parent.getLabCode());
					sb.append("<br>");
				}
				sb.append("<b>Parent Title:</b> ");
				sb.append(parent.getTitle());
				sb.append("<br>");				
			}

			// add series count if we can...
			Integer seriesCount = site.getSeriesCount();
			if(seriesCount != null) {
				// it's populated, so make it bold
				lblCode.setFont(font.deriveFont(Font.BOLD));
				
				// and then add this info...
				sb.append("<b>Number of series:</b> ");
				sb.append(seriesCount);
				if(site.getChildSeriesCount() != seriesCount) {
					sb.append(" (of ");
					sb.append(site.getChildSeriesCount());
					sb.append(")");
				}
				sb.append("<br>");
			}
			
			panel.setToolTipText(sb.toString());
		} else if(value instanceof String) {
			lblCode.setText((String)value);
			lblName.setText((String)value);
			
			// yellow background
			if(!isSelected)
				c.setBackground(new Color(255, 255, 200));
			
			Font font = list.getFont();
			lblCode.setFont(font.deriveFont(Font.BOLD));
			lblName.setFont(font.deriveFont(font.getSize() - 2.0f));			
		}
		
		return c;
	}
}
