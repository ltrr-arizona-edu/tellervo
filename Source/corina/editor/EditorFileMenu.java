package corina.editor;

import corina.Sample;
import corina.io.ExportDialog;
import corina.gui.menus.FileMenu;
import corina.ui.Builder;

import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

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
    }
}
