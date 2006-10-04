package corina.editor;

import corina.Element;
import corina.Sample;
import corina.io.ExportDialog;
import corina.gui.FileDialog;
import corina.gui.SaveableDocument;
import corina.gui.UserCancelledException;
import corina.gui.menus.FileMenu;
import corina.gui.menus.OpenRecent;
import corina.ui.Alert;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.util.Overwrite;

import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			public void actionPerformed(ActionEvent aev) {
				// REFACTOR: make ExportDialog take just 1 arg (the frame),
				// then make an export() method which calls that,
				// and call makeMenuItem() with 2 args ("c.e.EFM.export()")
				Sample s = ((Editor) f).getSample();
				if (s.isSummed()) {
					String labels[] = { "Sum", "Elements", "Combined" };

					int action = JOptionPane
							.showOptionDialog(
									f,
									"You are exporting a sum.\n"
											+ "Would you like to export the summed values,\n"
											+ "export the sum's elements in a packed file,\n"
											+ "or export them combined in a packed file?",
									I18n.getText("export..."),
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, labels,
									labels[0]);

					Sample base = s;
					List samples = new ArrayList();

					switch (action) {
					case JOptionPane.CLOSED_OPTION:
						return;

					case 0:
						new ExportDialog(s, f);
						break; // this case is normal. whew.

					case 2: // export everything.
						samples.add(s);

					case 1: // export only the elements.
						String errorsamples = "";
						boolean problem = false;

						for (int i = 0; i < s.elements.size(); i++) {
							Element e = (Element) s.elements.get(i);

							if (!e.isActive()) // skip inactive
								continue;

							try {
								Sample stmp = e.load();
								samples.add(stmp);
							} catch (IOException ioe) {
								problem = true;
								if (errorsamples.length() != 0)
									errorsamples += ", ";
								errorsamples += e.getFilename();
							}
						}

						// problem?
						if (problem) {
							Alert.error("Error loading sample(s):",
									errorsamples);
							return;
						}

						// no samples => don't bother doing anything
						if (samples.isEmpty()) {
							return;
						}

						new ExportDialog(samples, f, true);
					}
				} else
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
					if (doc.getFilename() == null) {
						JOptionPane
								.showMessageDialog(
										f,
										"Can't 'rename' an unsaved Sample.\nUse save as instead.",
										"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}

					// be careful.. this may be overkill?
					File oldFile = new File(new String(doc.getFilename()));

					// DESIGN: start out in the same folder as the old filename,
					// if there is one?

					// get new filename
					String filename = FileDialog.showSingle(I18n
							.getText("rename_to...")
							+ " (" + oldFile.getName() + ")");
					File newFile = new File(filename);

					if (newFile.exists()) {
						JOptionPane
								.showMessageDialog(
										f,
										"Can't rename to a file that already exists.\nUse save as instead.",
										"Error renaming...",
										JOptionPane.ERROR_MESSAGE);
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
