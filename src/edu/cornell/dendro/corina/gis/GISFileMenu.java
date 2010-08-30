package edu.cornell.dendro.corina.gis;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.ui.Builder;
import gov.nasa.worldwind.examples.util.ScreenShotAction;

public class GISFileMenu extends FileMenu {

	private static final long serialVersionUID = 4583709816910084036L;
	protected GISPanel wwMapPanel;
	
	public GISFileMenu(JFrame f, GISPanel wwMapPanel) {
		super(f);
		this.wwMapPanel = wwMapPanel;
	}
	
	@Override
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
	
	@Override
	public void addExportMenus(){
		
        JMenuItem snapItem = Builder.makeMenuItem("menus.file.exportmapimage", "edu.cornell.dendro.corina.gis.GISFileMenu.exportMapImage()", "fileexport.png");
        add(snapItem);
		
	}
	
	@Override
	public void addIOMenus(){
		
		JMenuItem importmenu = Builder.makeMenuItem("menus.file.import", "edu.cornell.dendro.corina.gui.menus.FileMenu.importdbwithbarcode()", "fileimport.png");
		importmenu.setEnabled(false);
		add(importmenu);
	}
	
	public void exportMapImage()
	{
		ScreenShotAction screenshot = new ScreenShotAction(this.wwMapPanel.getWwd());
	}

}
