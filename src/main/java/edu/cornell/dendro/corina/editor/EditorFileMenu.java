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
package edu.cornell.dendro.corina.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import edu.cornell.dendro.corina.gis.GISPanel;
import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.io.control.OpenExportEvent;
import edu.cornell.dendro.corina.print.SeriesReport;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import gov.nasa.worldwind.examples.util.ScreenShotAction;

// a FileMenu with "Export..." for samples.
// TODO: this doesn't need to be public.
public class EditorFileMenu extends FileMenu {

	private static final long serialVersionUID = 1L;
	private Sample sample;
		
	public EditorFileMenu(Editor e, Sample s){
		super(e);
		this.sample = s;
		
	}
	
	@Override
	public void addPrintMenu() {
		// Add report printing entry
		JMenuItem reportPrint = Builder.makeMenuItem("menus.file.print", true, "printer.png");
		reportPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SeriesReport.printReport(sample);
			}
		});
		add(reportPrint);

		// Add preview printing entry
		JMenuItem reportPreview = Builder.makeMenuItem("menus.file.printpreview", true);
		reportPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				SeriesReport.viewReport(sample);
			}
		});
		add(reportPreview);		

	}


	public void addExportMenus() {
		// add "Export..." menuitem
		fileexport = Builder.makeMenuItem("menus.file.export", true, "fileexport.png");
		fileexport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				OpenExportEvent event = new OpenExportEvent(sample);
				event.dispatch();
			}
		});
		add(fileexport);
		
		
		
		GISPanel panel = ((Editor)this.f).wwMapPanel;
		
        JMenuItem exportmenu = new JMenuItem(I18n.getText("menus.file.exportmapimage"));
        exportmenu.setIcon(Builder.getIcon("captureimage.png", 22));
        if(panel!=null)
        {
        	exportmenu.addActionListener(new ScreenShotAction(panel.getWwd()));
        	exportmenu.setEnabled(true);
        }
        else
        {
        	exportmenu.setEnabled(false);
        }
        
        add(exportmenu);
		
		
	}
}
