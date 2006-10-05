/**
 * 
 */
package corina.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import corina.Sample;
import corina.formats.Filetype;
import corina.formats.PackedFileType;
import corina.gui.Bug;
import corina.gui.FileDialog;
import corina.gui.UserCancelledException;
import corina.ui.Alert;
import corina.ui.I18n;
import corina.util.Overwrite;

/**
 * @author Lucas Madar
 *
 */
public class Export {
	/**
	 * Save a single sample.
	 * Pops up a dialog box asking for the file name to save to, exports to the type 
	 * passed in 'format' (ie, corina.Formats.Tucson)
	 * 
	 * @param exportee the sample to export
	 */
	public static void saveSingleSample(Sample exportee, String format) {
		
		// use the default title...
		
		String etext = "";
		if (exportee.meta.get("filename") != null) {
			File oldfile = new File((String) exportee.meta
					.get("filename"));
			etext = " (" + oldfile.getName() + ")";
		}

		String title = I18n.getText("export") + etext;
		
		saveSingleSample(exportee, format, title);
	}

	public static void saveSingleSample(Sample exportee, String format, String title) {
		try {
			String fn = FileDialog.showSingle(title);

			// check for already-exists
			Overwrite.overwrite(fn);

			// save it
			Filetype f = (Filetype) Class.forName(format).newInstance();
			BufferedWriter w = new BufferedWriter(new FileWriter(fn));
			try {
				f.save(exportee, w);
			} finally {
				try {
					w.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		} catch (UserCancelledException uce) {
			// do nothing
		} catch (IOException ioe) {
			// problem saving, tell user
			// WAS: passed |me| as owner of dialog; do i lose something here?
			// WAS: WARNING_MESSAGE -- Alert uses ERROR_MESSAGE, which i think is at least as good
			Alert.error(I18n.getText("export_error_title"), I18n
					.getText("xport_error")
					+ ioe);
		} catch (Exception ex) {
			// problem creating filetype, or npe, or whatever -- bug.
			Bug.bug(ex);
		}		
	}
	
	/**
	 * Save a list of samples in packed format.
	 * Pops up a dialog box asking for the file name to save to, exports to the type chosen in the
	 * visible popup menu.
	 * 
	 * @param exportee the sample to export
	 */
	public static void savePackedSample(List slist, String format) {
		savePackedSample(slist, format, I18n.getText("export"));
	}

	public static void savePackedSample(List slist, String format, String title) {
		try {
			// ask for filename
			String fn = FileDialog.showSingle(title);

			// check for already-exists
			Overwrite.overwrite(fn);

			// save it
			Filetype f = (Filetype) Class.forName(format).newInstance();
			BufferedWriter w = new BufferedWriter(new FileWriter(fn));
			try {
				((PackedFileType)f).saveSamples(slist, w);
			} finally {
				try {
					w.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		} catch (UserCancelledException uce) {
			// do nothing
		} catch (IOException ioe) {
			// problem saving, tell user
			// WAS: passed |me| as owner of dialog; do i lose something here?
			// WAS: WARNING_MESSAGE -- Alert uses ERROR_MESSAGE, which i think is at least as good
			Alert.error(I18n.getText("export_error_title"), I18n
					.getText("xport_error")
					+ ioe);
		} catch (Exception ex) {
			// problem creating filetype, or npe, or whatever -- bug.
			Bug.bug(ex);
		}		
	}
	
	/**
	 * Saves multiple samples.
	 * Pops up a dialog box asking for a folder to save to;
	 * files are dumped in to this folder with a default extension added, ie:
	 * ACM123.PIK becomes ACM123.PIK.TUC
	 * 
	 * @param slist a List of samples
	 */
	public static void saveMultiSample(List slist, String format) {
		saveMultiSample(slist, format, "Choose an Export Folder");
	}
	
	public static void saveMultiSample(List slist, String format, String title) {		
		try {
			// get the export format...
			Filetype f = (Filetype) Class.forName(format).newInstance();
			
			JFileChooser chooser = new JFileChooser();
		    chooser.setDialogTitle(title);
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    
		    int rv = chooser.showDialog(null, "OK");
		    if (rv != JFileChooser.APPROVE_OPTION) 
		    	return;
		    				
			File dir = new File(
					chooser.getSelectedFile().getAbsolutePath() +
					File.separator + "Export" +
					format.substring( format.lastIndexOf('.'), format.length())
					);
			
			if(!((dir.exists() && dir.isDirectory()) || dir.mkdir())) {
				Alert.error("Couldn't export", "Couldn't create/write to directory " + dir.getName());
				return;
			}

			// for each sample, make a new filename and export it!
			for (int i = 0; i < slist.size(); i++) {
				Sample s = (Sample) slist.get(i);
				String progress = "Processing "
						+ ((String) s.meta.get("filename")) + " (" + i
						+ "/" + slist.size() + ")";
				//preview.setText(progress);
				
				// so, we have things like "blah.pkw.TUC!"
				// gross, but this is what people wanted.
				String fn = dir.getAbsolutePath() +
					File.separator +
					new File((String)s.meta.get("filename")).getName() +
					f.getDefaultExtension();		
				
				BufferedWriter w = new BufferedWriter(new FileWriter(fn));
				try {
					f.save(s, w);
				} finally {
					try {
						w.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}					
				System.out.println("Exported " + fn);
			}
			
			Alert.message(I18n.getText("bulkexport..."), "Exporting comple.");
		} catch (Exception ex) {
			// problem creating filetype, or npe, or whatever -- bug.
			Bug.bug(ex);
		}
		
	}


}
