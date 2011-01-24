package edu.cornell.dendro.corina.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gis.GISPanel;
import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.io.ExportDialog;
import edu.cornell.dendro.corina.io.control.OpenExportEvent;
import edu.cornell.dendro.corina.print.SeriesReport;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import gov.nasa.worldwind.examples.util.ScreenShotAction;

// a FileMenu with "Export..." for samples.
// TODO: this doesn't need to be public.
public class EditorFileMenu extends FileMenu {

	// DESIGN: should this really be its own class?
	// DESIGN: should ExportDialog really be in corina.io (and not .editor)?

	// TODO: add "print sections..." menuitem?
	// -- if so, it goes in addPrintingMenus() between
	// addPageSetupMenu() and addPrintMenu().
	// old comments:
	// TODO: this shows a sections-chooser,
	// (TODO: combine with page-chooser in corina.cross!)
	// THEN print whatever sections you like.

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
		JMenuItem export = Builder.makeMenuItem("menus.file.export", true, "fileexport.png");
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				OpenExportEvent event = new OpenExportEvent(sample);
				event.dispatch();
			}
		});
		add(export);
		
		
		
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
