/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.io;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.sample.Sample;

// wrapper for tellervo.formats: load() and save() a file, given only its filename
public class Files {
	
	private final static Logger log = LoggerFactory.getLogger(Files.class);

	
	// don't instantiate me
	private Files() {
	}

	// load a file
	public static Sample load(String filename) throws IOException {
		// don't try a bunch of formats that we know a priori will all
		// fail: try some quick tests first
		File f = new File(filename);
		if (!f.exists())
			throw new FileNotFoundException("File doesn't exist.");
		if (f.isDirectory())
			return null;
		if (!f.isFile())
			throw new IOException("Not a file.");
		if (!f.canRead())
			throw new IOException("No read access allowed.");

		// i can ignore a lot of common filetypes without even
		// looking -- see SKIP_EXTENSIONS
		for (int i = 0; i < SKIP_EXTENSIONS.length; i++)
			if (filename.toUpperCase().endsWith(SKIP_EXTENSIONS[i]))
				throw new WrongFiletypeException();

		// make a buffered reader for it
		BufferedReader br = new BufferedReader(new FileReader(filename));

		try {
			// try each loader in turn
			for (int i = 0; i < LOADERS.length; i++) {
				try {
					// use factory to make a Filetype from the class name
					Filetype format = makeFileFormat(LOADERS[i]);

					// if somebody closed the stream, any call to read(),
					// ready(), mark(), or reset()
					// will throw an IOE. so i'll try a ready() call to see if
					// it's been closed.
					// (any SAX-based reader will close the stream after trying
					// to load it,
					// whether it succeeded or not -- but note that a file
					// format can check the first
					// line for "<?xml", and fail immediately if it doesn't look
					// like an XML file,
					// so this doesn't even happen whenever an XML-based format
					// fails.)
					// this way i'll only have to re-open the stream if needed;
					// not the most efficient way, sure, but not bad, in the
					// most common case.
					try {
						br.ready();
					} catch (IOException ioe) {
						// somebody closed my stream, re-open it
						try {
							br.close();
						} catch (IOException ioe2) {
							ioe2.printStackTrace();
						}
						br = new BufferedReader(new FileReader(filename));
					}

					// TODO: if the mark can't be reset, for some reason, the
					// stream will
					// need to be re-opened, also. (but it would be better to
					// simply
					// snarf it up into memory, and parse it there.)

					// try loading
					br.mark(80 * 5); // is 5 lines enough?
					Sample s = format.load(br);
					br.close();

					String filetype = format.toString();
					s.setMeta("filetype", filetype); // (used only for preview)
					s.setMeta("filename", filename);

					// if we made it this far without throwing a
					// WrongFiletypeException or IOException (or any other
					// Exception), it must have loaded correctly.
					return s;
				} catch (IllegalArgumentException iae) {
					// can't create loader -- ??? (bug, probably)
					log.error(iae.getLocalizedMessage());
				} catch (WrongFiletypeException wfe) {
					continue;
				} catch (IOException ioe) {
					String l = LOADERS[i];
					l = l.substring(l.lastIndexOf('.') + 1);
					throw new IOException(l + ": " + ioe.getMessage());
				} catch (Exception e) {
					// load() failed -- unknown reason -- log me? (once,
					// some loaders threw crazy things like
					// NullPointerExceptions, so this is a catch-all for
					// those. it should be unnecessary now, but it can't
					// hurt to have too much error-checking.)

					// use Bug to report it if this ever happens!
					continue;
				} finally {
					try {
						br.reset();
					} catch (IOException ioe) {
						// it's ok, stream is closed, but we have the sample.
					}
				}
			}
		} finally {
			try {
				br.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		// WRITEME
		throw new WrongFiletypeException();
	}

	// make a file format loader, given its name (e.g., "TwoColumn")
	private static Filetype makeFileFormat(String name)
			throws IllegalArgumentException {
		try {
			Constructor<?> cons = Class.forName(name).getConstructor(
					new Class[] {});
			return (Filetype) cons.newInstance(new Object[] {});
		} catch (Exception e) { // class not found, no such method,
								// instantiation exceptions
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Default Filetype class to use for saving files. Value is
	 * "tellervo.formats.Corina".
	 * 
	 * @see org.tellervo.desktop.formats.Corina
	 */
	private final static String DEFAULT_SAVER = "org.tellervo.desktop.formats.Corina";

	// FIXME: make pref: "default save format: tellervo, tucson, ..., whatever it
	// was before.

	// save a file
	public static void save(Sample s, String filename) throws IOException {
		Filetype format = makeFileFormat(DEFAULT_SAVER);
		BufferedWriter w = new BufferedWriter(new FileWriter(filename));
		try {
			format.save(s, w);
		} finally {
			try {
				w.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * Filename extensions of files to ignore out of principle: .XLS, .DOC,
	 * .JPG, .GIF, and .TIF are a good start. They should be all upper case.
	 */
	private final static String SKIP_EXTENSIONS[] = { ".XLS", ".SLK", ".DOC",
			".PPT", ".RTF", ".EXE",
			".ZIP", // auto-(de)-compression? no, not enough benefit.
			".JPG", ".JPEG", ".GIF", ".TIF", ".TIFF", ".14I", ".14S", ".14D",
			".14P", ".14L", // Oxcal, my arch-nemesis!
			".MPJ", ".MTW", // dunno what these are, but they're not mine
			".GRF", // "GS Draw File: Copyright Golden Software Inc. 1991-1994"
					// (a GRaF)
			".JAR", ".CLASS", ".JAVA", "SITE.LOC", "SITE.XML", // (old tellervo
																// data files --
																// remove these
																// later)
			".Corina_Cache",

	// TODO: should i skip backup~ files here, too? (they're not actually
	// invalid, just "hidden")
	};

	private final static String LOADERS[] = {
			"org.tellervo.desktop.formats.Corina",
			"org.tellervo.desktop.formats.TRML",
			"org.tellervo.desktop.formats.TSAPMatrix",
			"org.tellervo.desktop.formats.Hohenheim",
			"org.tellervo.desktop.formats.Heidelberg",
			"org.tellervo.desktop.formats.Tucson",
			"org.tellervo.desktop.formats.TwoColumn", // <-- should always
															// be last
	};
}
