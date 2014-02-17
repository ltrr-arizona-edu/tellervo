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
package org.tellervo.desktop.gui.menus;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel;
import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.manip.RedateDialog;
import org.tellervo.desktop.manip.Reverse;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class EditorLiteToolsMenu extends JMenu implements SampleListener {
	private static final long serialVersionUID = 1L;
	
	private Sample sample;
	private Editor editor;
	
	private JMenuItem reverseMenu = new JMenuItem();
	private JMenuItem redate = new JMenuItem();
	
	public EditorLiteToolsMenu(Editor e, Sample s) {
		super(I18n.getText("menus.tools")); 

		this.sample = s;
		this.editor = e;

		sample.addSampleListener(this);

		// reverse
		reverseMenu = Builder.makeMenuItem("menus.tools.reverse", true, "reverse.png");
		reverseMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// reverse, and add to the undo-stack
				editor.postEdit(Reverse.reverse(sample));
			}
		});
		add(reverseMenu);
		reverseMenu.setEnabled(sample.getSampleType() == SampleType.DIRECT
				&& (!sample.hasMeta(Metadata.CHILD_COUNT) || sample.getMeta(
						Metadata.CHILD_COUNT, Integer.class) == 0));
		// redate
		redate = Builder.makeMenuItem("menus.tools.redate", true, "redate.png");
		redate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new RedateDialog(sample, editor).setVisible(true);
			}
		});

		add(redate);
		redate.setEnabled(true);
		redate.setVisible(true);		
		
		
		
		linkModel();
		
		
	}


	public void sampleRedated(SampleEvent e) { }
	public void sampleDataChanged(SampleEvent e) { }
	public void sampleElementsChanged(SampleEvent e) { }
	public void sampleDisplayUnitsChanged(SampleEvent e) {	}
	public void sampleMetadataChanged(SampleEvent e) {
		setMenusForNetworkStatus();
	}

	
	protected void linkModel()
	{
		  App.appmodel.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent argEvt) {
					if(argEvt.getPropertyName().equals(AppModel.NETWORK_STATUS)){
						setMenusForNetworkStatus();
					}	
				}
			});
		  
		  setMenusForNetworkStatus();
	}
	  
	protected void setMenusForNetworkStatus()
	{

	}


	@Override
	public void measurementVariableChanged(SampleEvent e) {
				
	}


	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		
	}


}


