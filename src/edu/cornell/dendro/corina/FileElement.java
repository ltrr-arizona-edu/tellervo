package edu.cornell.dendro.corina;

import java.io.File;
import java.io.IOException;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.io.Files;

/**
An Element, basically a reference to a Sample (stored on disk).

<p>Normally used as a member of a SampleSet (though at this time
SampleSet is only an ArrayList that provides text load/save of its
elements).</p>

<p>An Element holds:</p>

<ul>
  <li>An active flag
  <li>A filename
</ul>

<p>The active flag, true by default, indicates whether this Element
is to be used in a particular operation.  For example, Sum will
ignore inactive Elements, so users can quickly and easily try
removing Elements from a Sum to see how the result changes.</p>

<p>The filename is the unique name of this Sample.  It is expected
that at this place in the filesystem, there will be a Sample that
can be loaded by the Sample constructor Sample(String filename).
There will be trouble (i.e., IOExceptions) if this is not the case.
An Element's filename is immutable: once an Element has been
created, it cannot be pointed at any other sample.</p>

<p>(Note: I have considered adding a new field, filetype, so
non-auto-detectable filetypes can be loaded, but so far there has
been no demand for this.)</p>

@version $Id: ObsFileElement.java 1061 2008-04-22 00:04:48Z lucasm $
*/

public class FileElement implements SampleLoader {
	/** The folder, represented by A:B:C separated directory names */
	private String folder;

	/** The actual name of the file */
	private String basename;

	/** The given filename */
	private String filename;
	
	public FileElement(String filename) {
		// if it starts with a ?, this is a relative path, using :'s as separators
		// ie, ?FOREST:ACM:moo123.pik
		if(filename.startsWith("?")) {
			String fn = filename.substring(1);
			
			int pos = fn.lastIndexOf(':');
			
			if(pos >= 0) {
				this.folder = fn.substring(0, pos);
				this.basename = fn.substring(pos + 1, fn.length());
				
				this.filename = App.prefs.getPref("corina.dir.data") + File.separator +
								this.folder.replace(":", File.separator) + File.separator +
								this.basename;
			}
			else {
				this.filename = filename;
				this.basename = new File(filename).getName();
			}
		} else if(filename.startsWith("@")) {
			/* this is from undocumented code in sample.java
			 * I don't know if it means anything, or if it's even used
			 * But, I'm going to wrap this here because I'm forcing everything
			 * to use elements as a go-between for samples for the new databasing
			 * - lucas
			 */
			
			// new @-notation
			// (assumes c.d.r ends with file.sep!)
			this.filename = System.getProperty("corina.dir.data", ".") + filename.substring(1);
			this.basename = new File(this.filename).getName();
		}
		// otherwise, we got passed a whole file name. try and parse it up.
		else {
			String fn = filename;
			
			// if, for some reason, adaptive reading is turned off... 
			// don't bother to parse it!
			if(Boolean.valueOf(App.prefs.getPref("corina.dir.adaptiveread")).booleanValue() == false) {
				this.filename = filename;
				this.basename = new File(filename).getName();
				return;				
			}
			
			// chop off any sort of beginning cruft
			if(fn.startsWith("G:\\DATA\\")) {
				fn = fn.substring(8);
			}
			else if(filename.startsWith(App.prefs.getPref("corina.dir.data") + File.separator)) {
				fn = fn.substring(App.prefs.getPref("corina.dir.data").length() + File.separator.length());
			}
			else {
				// we can't convert this into a special path.
				// leave folder null and bail.
				this.filename = filename;
				this.basename = new File(filename).getName();
				return;
			}
			
			// replace forward slashes with a :, which will be our path separator.
			fn = fn.replace("\\", ":");
			// do the same for some other platform
			fn = fn.replace(File.separator, ":");
			
			int pos = fn.lastIndexOf(':');
			
			if(pos >= 0) {
				this.folder = fn.substring(0, pos);
				this.basename = fn.substring(pos + 1, fn.length());
				
				this.filename = App.prefs.getPref("corina.dir.data", ".") + File.separator +
								this.folder.replace(":", File.separator) + File.separator +
								this.basename;
			}
			else {
				this.filename = filename;
				this.basename = new File(filename).getName();
			}
		}
	}

	/**
	 Load this Element.  Returns this Element in a Sample object.

	 @return the Sample referenced by this Element
	 @exception IOException if an IOException occurred while trying
	 to load it; this can also be the subclasses
	 FileNotFoundException
	 */
	public Sample load() throws IOException {
		Sample s = Files.load(filename);

		s.setLoader(this);
		return s;
	}
	
	public BaseSample loadBasic() throws IOException {
		Sample s = load();
		return new BaseSample(s);
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getBasename() {
		return basename;
	}
	
	/**
	 Return this Element's filename, with
	 <code>corina.dir.data</code> replaced by an "?", if it's in a
	 subfolder of that.  (Otherwise, returns the absolute filename.)

	 @return the filename, with ?'s
	 */
	public String getRelativeFilename() {
		// no basename or folder? return relative path...
		if(basename == null || folder == null)
			return filename;
		
		return "?" + folder + ":" + basename;
	}
	
	public String toString() {
		return getFilename();
	}

	public String getFolder() {
		return folder;
	}
	
	/**
	 * Public interface!!! (As a sampleLoader!)
	 */
	
	public String getName() {
		return filename;
	}
	
	public String getShortName() {
		return basename;
	}
}
