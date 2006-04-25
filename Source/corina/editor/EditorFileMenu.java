package corina.editor;

import corina.Sample;
import corina.io.ExportDialog;
import corina.gui.FileDialog;
import corina.gui.SaveableDocument;
import corina.gui.UserCancelledException;
import corina.gui.menus.FileMenu;
import corina.gui.menus.OpenRecent;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.util.Overwrite;

import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JOptionPane;

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

    public EditorFileMenu(Editor e) {
	super(e);
    }

    public void addCloseSaveMenus() {
	super.addCloseSaveMenus();

	// add "Export..." menuitem
	JMenuItem export = Builder.makeMenuItem("export...");
	export.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // REFACTOR: make ExportDialog take just 1 arg (the frame),
		    // then make an export() method which calls that,
		    // and call makeMenuItem() with 2 args ("c.e.EFM.export()")
		    Sample s = ((Editor) f).getSample();
		    new ExportDialog(s, f);
		}
	    });
	add(export);
    
	// add "Rename to..." menuitem
	JMenuItem rename_to = Builder.makeMenuItem("rename_to...");
	rename_to.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			try {
			    // get doc
			    SaveableDocument doc = (SaveableDocument) f;
			    if(doc.getFilename() == null) {
			    	JOptionPane.showMessageDialog(f, "Can't 'rename' an unsaved Sample.\nUse save as instead.", 
			    			"Error", JOptionPane.ERROR_MESSAGE);
			    	return;
			    }
			    
			    // be careful.. this may be overkill?
			    File oldFile = new File(new String(doc.getFilename()));
				
			    // DESIGN: start out in the same folder as the old filename,
			    // if there is one?

			    // get new filename
			    String filename = FileDialog.showSingle(I18n.getText("rename_to...") +
			    		" (" + oldFile.getName() + ")");
			    File newFile = new File(filename);
			    
			    if(newFile.exists()) {
			    	JOptionPane.showMessageDialog(f, "Can't rename to a file that already exists.\nUse save as instead.",
			    			"Error renaming...", JOptionPane.ERROR_MESSAGE);
			    	return;
			    }
			    
			    doc.setFilename(filename);
			    OpenRecent.fileOpened(doc.getFilename());			    
			    oldFile.renameTo(newFile);
			} catch (UserCancelledException uce) {
			    // do nothing
			}			
		}
	    });
	add(rename_to);
    }
}
