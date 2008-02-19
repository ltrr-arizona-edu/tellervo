/**
 * 
 */
package edu.cornell.dendro.corina.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.formats.Filetype;
import edu.cornell.dendro.corina.formats.PackedFileType;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Overwrite;

/**
 * @author Lucas Madar
 *
 */
public class Exporter {
	private String exportDirectory;
	private boolean rememberExportDirectory;
	
	public Exporter() {
		rememberExportDirectory = true;

		// load the last export directory. If it doesn't exist, make a nice default.
		exportDirectory = App.prefs.getPref("corina.dir.export");
		if(exportDirectory == null)
			exportDirectory = App.prefs.getPref("corina.dir.data");
		if(exportDirectory == null)
			exportDirectory = "";
		
		// now, keep going back until it exists and is a directory.
		File exdf = new File(exportDirectory).getAbsoluteFile();
		
		while(!exdf.isDirectory() && exdf.toString().length() > 0)
			exdf = exdf.getParentFile();
		
		exportDirectory = exdf.getAbsolutePath();
	}

	// set from an outside source? Don't save it.
	public void setExportDirectory(String directory) {
		rememberExportDirectory = false;
		exportDirectory = directory;
	}
	
	/**
	 * Save a single sample.
	 * Pops up a dialog box asking for the file name to save to, exports to the type 
	 * passed in 'format' (ie, corina.Formats.Tucson)
	 * 
	 * @param exportee the sample to export
	 * @return The full path of the exported file name, or null if not saved
	 */
	public String saveSingleSample(Sample exportee, String format) {
		
		// use the default title...
		
		String etext = "";
		if (exportee.getMeta("filename") != null) {
			File oldfile = new File((String) exportee.getMeta("filename"));
			etext = " (" + oldfile.getName() + ")";
		}

		String title = I18n.getText("export") + etext;
		
		return saveSingleSample(exportee, format, title);
	}

	public String saveSingleSample(Sample exportee, String format, String title) {
		String fn = null;
		
		try {
			Filetype f = (Filetype) Class.forName(format).newInstance();
			String suggestedfn = 
				exportDirectory + 
				File.separator + 
				new File((String)exportee.getMeta("filename")).getName() +
				f.getDefaultExtension();
			
			System.out.println(suggestedfn);
			
			fn = FileDialog.showSingle(title, suggestedfn, "export");

			// save the export directory. Remember, fn is the actual filename, so we need to chop off the file name bit.
			if(rememberExportDirectory)
				App.prefs.setPref("corina.dir.export", new File(fn).getParent());

			// check for already-exists
			Overwrite.overwrite(fn);

			// save it
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
				
		return fn;
	}
	
	/**
	 * Save a list of samples in packed format.
	 * Pops up a dialog box asking for the file name to save to, exports to the type chosen in the
	 * visible popup menu.
	 * 
	 * @param exportee the sample to export
	 * @return the full path of the saved packed sample, or null if not saved
	 */
	public String savePackedSample(List slist, String format) {
		return savePackedSample(slist, format, I18n.getText("export"));
	}

	public String savePackedSample(List slist, String format, String title) {
		String fn = null;
		try {
			// ask for filename
			fn = FileDialog.showSingle(title, exportDirectory, "export");
			
			// save the export directory. Remember, fn is the actual filename, so we need to chop off the file name bit.
			if(rememberExportDirectory)
				App.prefs.setPref("corina.dir.export", new File(fn).getParent());
			
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
		
		return fn;
	}
	
	/**
	 * Saves multiple samples.
	 * Pops up a dialog box asking for a folder to save to;
	 * files are dumped in to this folder with a default extension added, ie:
	 * ACM123.PIK becomes ACM123.PIK.TUC
	 * 
	 * @param slist a List of samples
	 * @return a List of saved file names, or null if not saved.
	 */
	public List saveMultiSample(List slist, String format) {
		return saveMultiSample(slist, format, "Choose an Export Folder");
	}
	
	public List saveMultiSample(List slist, String format, String title) {		
		List savedNames = new ArrayList();
		try {
			// get the export format...
			Filetype f = (Filetype) Class.forName(format).newInstance();
			
			JFileChooser chooser = new JFileChooser();
		    chooser.setDialogTitle(title);
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    
		    int rv = chooser.showDialog(null, "OK");
		    if (rv != JFileChooser.APPROVE_OPTION) 
		    	return null;
		    
			// save the export directory. Remember, fn is the actual filename, so we need to chop off the file name bit.
			if(rememberExportDirectory)
				App.prefs.setPref("corina.dir.export", chooser.getSelectedFile().getAbsolutePath());
		    				
			File dir = new File(
					chooser.getSelectedFile().getAbsolutePath() +
					File.separator + "Export" +
					format.substring( format.lastIndexOf('.'), format.length())
					);
			
			if(!((dir.exists() && dir.isDirectory()) || dir.mkdirs())) {
				Alert.error("Couldn't export", "Couldn't create/write to directory " + dir.getName());
				return null;
			}

			// for each sample, make a new filename and export it!
			for (int i = 0; i < slist.size(); i++) {
				Sample s = (Sample) slist.get(i);
				String progress = "Processing "
						+ ((String) s.getMeta("filename")) + " (" + i
						+ "/" + slist.size() + ")";
				//preview.setText(progress);
				
				// so, we have things like "blah.pkw.TUC!"
				// gross, but this is what people wanted.
				String fn = dir.getAbsolutePath() +
					File.separator +
					new File((String)s.getMeta("filename")).getName() +
					f.getDefaultExtension();		
				
				savedNames.add(fn);
				
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
			
		} catch (Exception ex) {
			// problem creating filetype, or npe, or whatever -- bug.
			Bug.bug(ex);
		}
	
		return savedNames;
	}
}
