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
package org.tellervo.desktop.editor;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

/*
 * this is a dirty little class that pops up a dialog asking what you'd like inside of a sample printed report.
 * then, you can pass the object to a printer which puts values in...
 */

public class SampleBit {
	private boolean sb_title;
	private boolean sb_printinfo;
	private boolean sb_data;
	private boolean sb_meta;
	private boolean sb_weiser;
	private boolean sb_elements;
	private boolean sb_header;
	private boolean sb_doublespace;

	private SampleBit() {		
	}
	
	public boolean wantTitle() { return sb_title; }
	public boolean wantPrintInfo() { return sb_printinfo; }
	public boolean wantSampleHeader() { return sb_header; }
	public boolean wantSampleData() { return sb_data; }
	public boolean wantMetaData() { return sb_meta; }
	public boolean wantWeiserjahre() { return sb_weiser; }
	public boolean wantElements() { return sb_elements; }
	public boolean isDoubleSpaced() { return sb_doublespace; }

	static public SampleBit askBits(JFrame parent) {
		final JDialog dialog;
		Container c;
		JPanel checkboxpanel, buttonpanel;
		
		final class successholder {
			boolean success;			
		}
		
		final successholder success = new successholder();
		success.success = false;
		
		dialog = new JDialog(parent, "Sample report bit chooser", true);
		c = dialog.getContentPane();
		
		c.setLayout(new BorderLayout());
		
		checkboxpanel = new JPanel(new GridBagLayout());
		checkboxpanel.setBorder(BorderFactory.createTitledBorder("Data options"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.gridy = 0;
		
		JCheckBox showTitle = new JCheckBox("Title", true);
		JCheckBox showPrintInfo = new JCheckBox("Printing info", true);
		JCheckBox showSampleHeader = new JCheckBox("Sample header (non-indexed only)", true);
		JCheckBox showSampleData = new JCheckBox("Sample data", true);
		JCheckBox showMeta = new JCheckBox("Metadata", true);
		JCheckBox showWeiser = new JCheckBox("Weiserjahre", true);
		JCheckBox showElements = new JCheckBox("Elements", true);
		JCheckBox doubleSpace = new JCheckBox("Double-space all numerical data", true);
		
		JLabel text = new JLabel("<html>Which parts of the report<br>would you like to include?");
		addComponent(checkboxpanel, text, gbc);
		
		addComponent(checkboxpanel, showTitle, gbc);
		addComponent(checkboxpanel, showPrintInfo, gbc);
		addComponent(checkboxpanel, showSampleHeader, gbc);
		addComponent(checkboxpanel, showSampleData, gbc);
		addComponent(checkboxpanel, showMeta, gbc);
		addComponent(checkboxpanel, showWeiser, gbc);
		addComponent(checkboxpanel, showElements, gbc);
		addComponent(checkboxpanel, doubleSpace, gbc);
		
		c.add(checkboxpanel, BorderLayout.CENTER);
		
		buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
	    String oktext = org.tellervo.desktop.ui.I18n.getText("ok");
	    if (oktext == null) oktext = "Ok";
	    JButton okButton = new JButton(oktext);
	    buttonpanel.add(okButton);
	    okButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	    	  success.success = true;
	    	  dialog.dispose();
	      }
	    });
	    
	    String canceltext = org.tellervo.desktop.ui.I18n.getText("general.cancel");
	    if (canceltext == null) canceltext = "Cancel";
	    JButton cancelButton = new JButton(canceltext);
	    buttonpanel.add(cancelButton);
	    cancelButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	    	  dialog.dispose();
	      }
	    });
	    
		
		c.add(buttonpanel, BorderLayout.SOUTH);
		

		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		
		if(!success.success)
			return null;
	
		SampleBit sb = new SampleBit();
		sb.sb_data = showSampleData.isSelected();
		sb.sb_header = showSampleHeader.isSelected();
		sb.sb_elements = showElements.isSelected();
		sb.sb_meta = showMeta.isSelected();
		sb.sb_printinfo = showPrintInfo.isSelected();
		sb.sb_title = showTitle.isSelected();
		sb.sb_weiser = showWeiser.isSelected();
		sb.sb_doublespace = doubleSpace.isSelected();
		return sb;
	}
	
	static private void addComponent(Container cont, Component comp, GridBagConstraints gbc) {
		gbc.gridy++;
		gbc.gridx = 0;
		
		cont.add(comp, gbc);
	}
}
