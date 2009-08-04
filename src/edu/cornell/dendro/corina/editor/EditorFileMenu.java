package edu.cornell.dendro.corina.editor;

import edu.cornell.dendro.corina.io.Exporter;
import edu.cornell.dendro.corina.io.ExportDialog;
import edu.cornell.dendro.corina.io.NativeSpawn;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.PrintableDocument;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.print.SeriesReport;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;

import javax.swing.JMenuItem;
import javax.swing.AbstractAction;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private Sample sample;
		
	public EditorFileMenu(Editor e, Sample s){
		super(e);
		this.sample = s;
		
	}
	
	@Override
	public void addIOMenus(){
	
		add(Builder.makeMenuItem("dbimport...", "edu.cornell.dendro.corina.gui.menus.FileMenu.importdb()", "fileimport.png"));
		addExportMenu();
	}
	
	@Override
	public void addPrintMenu() {
		// Add report printing entry
		JMenuItem reportPrint = Builder.makeMenuItem("print", true, "printer.png");
		reportPrint.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				SeriesReport.printReport(sample.getSeries().getIdentifier().getValue().toString());
			}
		});
		add(reportPrint);

		// Add preview printing entry
		JMenuItem reportPreview = Builder.makeMenuItem("printpreview", true);
		reportPreview.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				SeriesReport.viewReport(sample.getSeries().getIdentifier().getValue().toString());
			}
		});
		add(reportPreview);		

	}
	
	@Override
	public void addCloseSaveMenus() {
		super.addCloseSaveMenus();
		
		//addExportMenu();
		
		if(Boolean.parseBoolean(App.prefs.getPref("corina.corem.enable"))) {
			addCoremMenu();
		}
			
		// add "Rename to..." menuitem
		JMenuItem rename_to = Builder.makeMenuItem("rename_to...");
		rename_to.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {
					// get doc
					SaveableDocument doc = (SaveableDocument) f;
					if (doc.getFilename() == null) {
						JOptionPane.showMessageDialog(
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
						JOptionPane.showMessageDialog(
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
		rename_to.setVisible(false);
	}

	private void addCoremMenu() {
		JMenuItem corem = new JMenuItem("Export to COREM...");
		
		corem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent aev) {
				
				// create an exporter object
				Exporter exporter = new Exporter();
				
				// for cofecha, remember the last directory we used corem in.
				String lastCofechaDir = App.prefs.getPref("corina.corem.lastdir");
				if(lastCofechaDir != null)
					exporter.setExportDirectory(lastCofechaDir);

				Sample s = ((Editor) f).getSample();
				File savedFile = null;
				if (s.isSummed()) {
					String labels[] = { "Sum", "Elements", "Combined" }, fileRet;

					int action = JOptionPane
							.showOptionDialog(
									f,
									"You are exporting a sum.\n"
											+ "Would you like to export the summed values,\n"
											+ "export the sum's elements in a packed file,\n"
											+ "or export them combined in a packed file?",
									"Export to COFECHA...",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, labels,
									labels[0]);

					Sample base = s;
					List<Sample> samples = new ArrayList<Sample>();

					switch (action) {
					case JOptionPane.CLOSED_OPTION:
						return;

					case 0:
						fileRet = exporter.saveSingleSample(s, "edu.cornell.dendro.corina.formats.Tucson", "Export as... [For COFECHA]");
						if(fileRet != null)
							savedFile = new File(fileRet);
						break; // this case is normal. whew.

					case 2: // export everything.
						samples.add(s);

					case 1: // export only the elements.
						String errorsamples = "";
						boolean problem = false;

						ElementList elements = s.getElements();
						for (int i = 0; i < elements.size(); i++) {
							Element e = s.getElements().get(i);

							if (elements.isActive(e)) // skip inactive
								continue;

							try {
								Sample stmp = e.load();
								samples.add(stmp);
							} catch (IOException ioe) {
								problem = true;
								if (errorsamples.length() != 0)
									errorsamples += ", ";
								errorsamples += e.toString();
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
						
						fileRet = exporter.savePackedSample(samples, "edu.cornell.dendro.corina.formats.PackedTucson", "Export as... [For COFECHA]");
						if(fileRet != null)
							savedFile = new File(fileRet);
					}
				} else {
					String fileRet = exporter.saveSingleSample(s, "edu.cornell.dendro.corina.formats.Tucson", "Export as... [For COFECHA]");
					if(fileRet != null)
						savedFile = new File(fileRet);
				}
				
				// ok, we saved a file...
				if(savedFile != null) {
					
					// Where is corem? Remember this!
					String coremPath = App.prefs.getPref("corina.corem.dir", "corem.exe");
					// Save our directory for convenience
					App.prefs.setPref("corina.corem.lastdir", savedFile.getParent());
					
					if(!new File(coremPath).exists()) {
						Alert.error("Couldn't launch COREM", "COREM executable does not exist.\nMake sure you've set it up correctly in the preferences!");
						return;
					}
					
					final String command = '"' + coremPath + '"';
					final String workingPath = savedFile.getParent();
					final String inFilename = savedFile.getAbsolutePath();
					final String title = s.toString();
					
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								NativeSpawn.spawnCofecha(command, workingPath, inFilename, title);
							} catch (Exception ioe) {
								Alert.error("Couldn't launch COFECHA", ioe.toString());
							}
						}
					});
				}
			}
		});
		add(corem);
	}

	private void addExportMenu() {
		// add "Export..." menuitem
		JMenuItem export = Builder.makeMenuItem("export...", true, "fileexport.png");
		export.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				Sample s = ((Editor) f).getSample();
				if (s.isSummed()) {
					String labels[] = { "Sum", "Elements", "Combined" };

					int action = JOptionPane.showOptionDialog(
									f,
									"You are exporting a sum.\n"
											+ "Would you like to export the summed values,\n"
											+ "export the sum's elements in a packed file,\n"
											+ "or export them combined in a packed file?",
									I18n.getText("export..."),
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									labels, labels[0]);

					Sample base = s;
					List<Sample> samples = new ArrayList<Sample>();

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

						ElementList elements = s.getElements();
						for (int i = 0; i < elements.size(); i++) {
							Element e = elements.get(i);

							if (!elements.isActive(e)) // skip inactive
								continue;

							try {
								Sample stmp = e.load();
								samples.add(stmp);
							} catch (IOException ioe) {
								problem = true;
								if (errorsamples.length() != 0)
									errorsamples += ", ";
								errorsamples += e.toString();
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
	}
}
