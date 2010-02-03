/**
 * 
 */
package edu.cornell.dendro.corina.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import java.nio.charset.Charset;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.formats.Filetype;
import edu.cornell.dendro.corina.formats.PackedFileType;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Overwrite;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;

/**
 * @author Lucas Madar
 *
 */
public class Exporter {
	private String exportDirectory;
	private boolean rememberExportDirectory;
	private EncodingType encodingType;
	

	public enum EncodingType {
		UTF8,
		UTF16,
		ISO8859_1,
		MacRoman;
	}
	
	public Exporter() {
		// Default encoding
		encodingType = EncodingType.UTF8;
		
		rememberExportDirectory = true;

		// load the last export directory. If it doesn't exist, make a nice default.
		exportDirectory = App.prefs.getPref("corina.dir.export", null);
		if(exportDirectory == null)
			exportDirectory = App.prefs.getPref("corina.dir.data", null);
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
	
	public void setEncodingType(EncodingType type)
	{
		encodingType = type;
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
		String title = I18n.getText("export") + " " + exportee.getDisplayTitle();
		
		return saveSingleSample(exportee, format, title);
	}

	public void saveSingleSample2(Sample exportee, String format, String fn)
	{		
		
		Filetype f;
		try {
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn), encodingType.toString())); 
			f = (Filetype) Class.forName(format).newInstance();
			f.save(exportee, w);
			w.close();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
	
	public String saveSingleSample(Sample exportee, String format, String title) {
		String fn = null;
		
		try {
			Filetype f = (Filetype) Class.forName(format).newInstance();
			String suggestedfn = 
				exportDirectory + 
				File.separator + 
				exportee.getDisplayTitle().toString() +
				f.getDefaultExtension();
			
			System.out.println(suggestedfn);
			
			fn = FileDialog.showSingle(I18n.getText("export"), title, suggestedfn, "export");

			// save the export directory. Remember, fn is the actual filename, so we need to chop off the file name bit.
			if(rememberExportDirectory)
				App.prefs.setPref("corina.dir.export", new File(fn).getParent());

			// check for already-exists
			Overwrite.overwrite(fn);

			// save it
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fn), encodingType.toString())); 
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
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fn), encodingType.toString())); 
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
	
	public Boolean savePackedSample2(List<Sample> samples, Filetype ft, File fn)
	{
		
		BufferedWriter w = null;
		try {
			w = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fn), encodingType.toString())); 

			((PackedFileType)ft).saveSamples(samples, w);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				w.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return false;
			}
		}
		
		return true;
		
	}
	
	
	public Boolean savePackedSample2(ElementList elements, Filetype ft, File fn)
	{
		
		List<Sample> samples = new ArrayList<Sample>();

		for(Element e : elements) {
			// load it
			Sample s;
			try {
				s = e.load();
				
			} catch (IOException ioe) {
				Alert.error("Error Loading Sample",
						"Can't open this file: " + ioe.getMessage());
				continue;
			}
			samples.add(s);

			OpenRecent.sampleOpened(new SeriesDescriptor(s));
		}
		
		// no samples => don't bother doing anything
		if (samples.isEmpty()) {
			return null;
		}
		
		
		BufferedWriter w = null;
		try {
			w = new BufferedWriter(new FileWriter(fn));
			((PackedFileType)ft).saveSamples(samples, w);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				w.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return false;
			}
		}
		
		return true;
		
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
	
	public Boolean saveMultiSample2(List<Sample> samples, Filetype ft, File folder)
	{
		try {
						
			if(!((folder.exists() && folder.isDirectory()) || folder.mkdirs())) {
				Alert.error("Couldn't export", "Couldn't create/write to directory " + folder.getName());
				return false;
			}
			
			// for each sample, make a new filename and export it!
			for (int i = 0; i < samples.size(); i++) {
				Sample s = (Sample) samples.get(i);
				String progress = "Processing "
						+ ((String) s.getDisplayTitle() + " (" + i
						+ "/" + samples.size() + ")");
				
				// so, we have things like "blah.pkw.TUC!"
				// gross, but this is what people wanted.
				String fn = folder.getAbsolutePath() +
					File.separator +
					new File((String)s.getDisplayTitle() +
					ft.getDefaultExtension());		
								
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fn), encodingType.toString())); 
				try {
					ft.save(s, w);
				} finally {
					try {
						w.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
						return false;
					}
				}					
				System.out.println("Exported " + fn);
			}			
			
		} catch (Exception ex) {
			// problem creating filetype, or npe, or whatever -- bug.
			Bug.bug(ex);
		}
		return true;
	}
	
	public Boolean saveMultiSample2(ElementList elements, Filetype ft, File folder)
	{
		try {
						
			if(!((folder.exists() && folder.isDirectory()) || folder.mkdirs())) {
				Alert.error("Couldn't export", "Couldn't create/write to directory " + folder.getName());
				return false;
			}
			
			int i=0;
			for(Element e : elements) {
				i++;
				// load it
				Sample s;
				try {
					s = e.load();
					
				} catch (IOException ioe) {
					Alert.error("Error Loading Sample",
							"Can't open this file: " + ioe.getMessage());
					continue;
				}
				
				String progress = "Processing "
					+ ((String) s.getDisplayTitle() + " (" + i
					+ "/" + elements.size() + ")");
				
				// so, we have things like "blah.pkw.TUC!"
				// gross, but this is what people wanted.
				String fn = folder.getAbsolutePath() +
					File.separator +
					new File((String)s.getDisplayTitle() +
					ft.getDefaultExtension());					
				
				OpenRecent.sampleOpened(new SeriesDescriptor(s));
				
				BufferedWriter w =   new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fn), encodingType.toString())); 
				try {
					ft.save(s, w);
				} finally {
					try {
						w.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
						return false;
					}
				}					
				System.out.println("Exported " + fn);
				
			}		
			
		} catch (Exception ex) {
			// problem creating filetype, or npe, or whatever -- bug.
			Bug.bug(ex);
		}
		return true;
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
				
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fn), encodingType.toString())); 
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
