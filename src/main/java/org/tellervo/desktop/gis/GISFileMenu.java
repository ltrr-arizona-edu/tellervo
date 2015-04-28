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
package org.tellervo.desktop.gis;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class GISFileMenu extends JMenu {

	private static final long serialVersionUID = 4583709816910084036L;
	private GISFrame f;

	
	public GISFileMenu(GISFrame f) {
		this.f=f;
	}
	
	public void addPrintMenu() {
		// Add report printing entry
		JMenuItem reportPrint = Builder.makeMenuItem("menus.file.print", true, "printer.png");
		reportPrint.setEnabled(false);
		add(reportPrint);

		// Add preview printing entry
		JMenuItem reportPreview = Builder.makeMenuItem("menus.file.printpreview", true);
		reportPreview.setEnabled(false);
		add(reportPreview);		

	}
	

	public void addIOMenus(){
		
		JMenuItem importmenu = Builder.makeMenuItem("menus.file.import", "org.tellervo.desktop.gui.menus.FileMenu.importdbwithbarcode()", "fileimport.png");
		importmenu.setEnabled(false);
		add(importmenu);
		
		GISPanel panel = ((GISFrame)this.f).wwMapPanel;
		
        JMenuItem exportmenu = new JMenuItem(I18n.getText("menus.file.exportmapimage"));
        //exportmenu.addActionListener(new FileExportMapAction(panel));
        //add(exportmenu);
	}
	
	


}
