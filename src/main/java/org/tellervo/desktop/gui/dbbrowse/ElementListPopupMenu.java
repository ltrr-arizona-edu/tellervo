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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.tellervo.desktop.cross.CrossdateDialog;
import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.graph.GraphWindow;
import org.tellervo.desktop.gui.hierarchy.WSITagNameDialog;
import org.tellervo.desktop.gui.hierarchy.WSITagUnassignDialog;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;


public class ElementListPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	public ElementListPopupMenu(final Element element, final ElementListManager browser) {
		JMenuItem item;
		

		
		item = Builder.makeMenuItem("menus.file.open", true, "fileopen.png");
		add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Sample s = element.load();
					new Editor(s);
				} catch (IOException e1) {
					Alert.errorLoading(element.getShortName(), e1);
				} 
			}
		});
		
		item = Builder.makeMenuItem("graph", true, "graph.png");
		add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Sample s = element.load();
					new GraphWindow(s);
				} catch (IOException e1) {
					Alert.errorLoading(element.getShortName(), e1);
				} 
			}
		});

		BaseSample bs;
		try {
			bs = element.load();
		} catch (IOException ioe) {
			// shouldn't happen?
			return;
		}
		
		final ITridasSeries series = bs.getSeries();
		if( series instanceof TridasDerivedSeries)
		{
			final TridasDerivedSeries ds = (TridasDerivedSeries) bs.getSeries();
			if(ds.getType().getValue().equalsIgnoreCase("Crossdate"))
			{
				item = Builder.makeMenuItem("crossdate.reviewCrossdate", true, "crossdate.png");
				add(item);
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {			
					
						new CrossdateDialog(new JFrame(), element);

					}
				});
			}
		}
		
		this.addSeparator();
		
		// Tag series 
		item = new JMenuItem("Tag this series");
		item.setIcon(Builder.getIcon("tag.png", 16));
		
		item.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				WSITagNameDialog.addTagToSeries(null, series);
				
			}
			
			
		});
		add(item);	
		
		// Remove tag 
		item = new JMenuItem("Remove tag from series");
		item.setIcon(Builder.getIcon("deletetag.png", 16));
		
		item.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				WSITagUnassignDialog.showDialog(null, series);
				
			}
			
			
		});
		add(item);		
		
		// direct number of children
		Integer directChildCount = bs.getMeta(Metadata.CHILD_COUNT, Integer.class);
		boolean canDelete = (element.isDeletable() && (directChildCount == null || directChildCount == 0));
				
		// Delete 
		addSeparator();
		item = Builder.makeMenuItem("general.delete", canDelete, "delete.png");
		add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				
					Object[] options = {"OK",
					                    "Cancel"};
					int ret = JOptionPane.showOptionDialog(getParent(), "Are you sure you want to delete this series?", "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						
					if(ret == JOptionPane.YES_OPTION)
					{
						element.delete();
						browser.deleteElement(element);
					}
					
				} catch (IOException ioe) {
					Alert.error("Cannot delete", "Unable to delete: " + ioe.getMessage());
				}
			}
		});
		

	}
}
