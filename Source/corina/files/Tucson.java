//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.files;

import corina.Year;
import corina.Range;
import corina.Sample;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
   The Tucson tree ring laboratory's file format.  This has been the
   canonical file format since the punchcard days.  Unfortunately,
   since it's just the punchcard format in a text file, it has
   numerous shortcomings:

   <ul>

     <li>minimal metadata support (only 80-character ASCII title,
     6-digit ID, and relative/absolute flag)

     <li>relative/absolute flag starts in column 80 of the title,
     making it inconvenient to edit with most text editors

     <li>no way to store BC(E) samples; some labs add 8000 to the
     absolute year as a hack to get around this, but unfortunately
     they've run into a Y10K problem as of last year (it can't store
     5-digit years, either)

     <li>no way to store auxiliary numerical data (like Weiserjahre,
     or earlywood/latewood)

     <li>no way to distinguish with certainty if a summed file is
     indexed (as far as I can tell, but see the 800 Rule, below)

     <li>no MIME type (even an application/x- one), standard file
     extension, or telltale opening signature

   </ul>

   <p>What it does do well is store raw data, readable by every dendro
   program ever written (yet another win for open standards).
   Unfortunately, they never bothered to make an improved format after
   they stopped using punchcards, so every other program also has a
   (better) native format.  Thus we have a dozen completely
   incompatible file formats today.</p>

   <h3>File Format</h3>

   <p>There are two types of "Tucson" files, "raw" and "processed".
   Processed files hold data that is indexed, summed, or both.</p>

   <p>There appear to be subtle variations on each type, like whether
   to fill out the last decade with extra 0's after the
   999-terminator, that different programs have introduced over the
   years.  I think this class can read any type of Tucson file
   correctly.</p>

   <h3>Raw Samples</h3>

   <pre>
   xxxxxx  yyyy
   </pre>

   <table border="0">
   <tr><th>letter</th><th>description</th></tr>
     <tr><td>x</td><td>id number (6 digits)</td>
     <tr><td>y</td><td>decade starting year (2 spaces + 4 digits)</td>
   </table>

   // WRITE ME

   <h3>Processed Samples</h3>

   // WRITE ME

   DETERMINING IF A FILE IS INDEXED: 800-rule (obsolete)

   <h3>Reference</h3>

   See NOAA's <a
   href="http://www.ngdc.noaa.gov/paleo/treeinfo.html">Tree Ring Data
   Description</a>.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Tucson extends Filetype {
    
    /** Return the human-readable name of this file format.
	@return the name of this file format */
    public String toString() {
	return "Tucson";
    }

    /** Return a unique character in the name to use as a mnemonic.
	@return character to use as mnemonic */
    public char getMnemonic() {
	return 'T';
    }

    // load 0-3 lines of header, if available, and fill meta
    // appropriately
    private void loadHeader(BufferedReader r, Map meta) throws IOException {
	// if first 2 lines start with the same 6 chars, no header
	boolean readFirstLine=true; // read a header line?  usually...
	{
	    r.mark(85+80+20); // 80+ABSOL + 80 + 20=buffer
	    String l1 = r.readLine();
	    String l2 = r.readLine();
	    r.reset();
	    if (l1.substring(0, 6).equals(l2.substring(0, 6)))
		readFirstLine = false;
	}

	// ITRDB tucson files have usually 3 header lines: "xxxxxx 1
	// Site", "xxxxxx 2 Country Species Altitude Long Lat Start
	// End", "xxxxxx 3 Author".  I *assume* no tucson file has
	// just 2, or more than 3, lines.  other programs apparently
	// can't agree where to put the " 1 " ("  1", for some files),
	// so I grab 3 chars and parseInt() it, to be sure.
	if (!readFirstLine) {
	    r.mark(81*3); // 3 full lines
	    String l1 = r.readLine();
	    String l2 = r.readLine();
	    String l3 = r.readLine();
	    if (l1.substring(0, 6).equals(l2.substring(0, 6)) &&
		l2.substring(0, 6).equals(l3.substring(0, 6)) &&
		(l1.substring(6).trim().charAt(0) == '1') &&
		(l2.substring(6).trim().charAt(0) == '2') &&
		(l3.substring(6).trim().charAt(0) == '3')) {

                // was: substring(9,61) -- is there anything beyond the title i care about?
                if (l1.length() >= 9) {
                    String title = l1.substring(9).trim();

                    // read 4-letter species code
                    if (l1.length()>=65) { // also make sure it's in " ABCD " form?
                        String species = l1.substring(61, 65);
                        meta.put("species", species);
                        title = l1.substring(9, 61).trim();
                    }

                    meta.put("title", title);
                }

		// ignore country?  (it's l2:9..22)

                // old way to do species: doesn't work?
                //		if (l2.length() >= 40)
                //		    meta.put("species", l2.substring(22, 40).trim());

		// altitude -- just dump in comments for now
		// lat/long -- just dump in comments for now
		if (l2.length() >= 57) {
		    String alt = l2.substring(40, 45).trim();
		    String latlong = l2.substring(47, 57).trim(); // decode me!
		    meta.put("comments", "Altitude = " + alt + ", Lat/Long = " + latlong);
		}

		// start/end -- ignore, they're in the data, too

		// author, and use 6-digit code
		if (l3.length() >= 10)
		    meta.put("author", l3.substring(9).trim());
		// don't load id here: it's per-sample, not per-file

		// other stuff: if it's in the ITRDB, it's probably
		// Absolute, and Reconciled
		meta.put("dating", "A");
		meta.put("reconciled", "Y");
	    } else {
		r.reset();
	    }
	}

	// first line -- used by corina-tucson files only?
	if (readFirstLine) {
	    String firstLine = r.readLine();
	    if (firstLine.length() > 80) { // title + RELAT/ABSOL
		meta.put("title", firstLine.substring(0, 80).trim());
		meta.put("dating", firstLine.substring(80, 81));
	    } else { // just title
		meta.put("title", firstLine.trim());
	    }
	}
    }

    /** Load a sample in Tucson format.
	@param filename file to load from
	@return Sample loaded from the file
	@exception FileNotFoundException if the file cannot be found
	@exception WrongFiletypeException if it's obviously not a Tucson file
	@exception IOException if something goes wrong */
    public Sample load() throws IOException {
	// set up a reader
//	r = new BufferedReader(new FileReader(filename));

	// new sample, with given filename
	s = new Sample();
	s.meta.clear();
//	s.meta.put("filename", filename);
	s.meta.put("dating", "R");

	// !!! load any header lines; below here is data only
	loadHeader(r, s.meta);

	// just load the data for one sample
	loadOneSample();

	// close file
	r.close();

	// return
	return s;
    }

    private Sample s;
    private HashMap meta = new HashMap(); // ugly, but needed

    public void open(String filename) throws IOException, FileNotFoundException {
	r = new BufferedReader(new FileReader(filename));
	loadHeader(r, meta);
	meta.put("filename", filename);
	meta.put("dating", "R");
    }
    public void close() throws IOException {
	r.close();
    }
    public Sample loadNext() throws WrongFiletypeException, IOException {
	// create a new sample to return
	s = new Sample();
	s.meta = (Map) meta.clone();

	// using w, into s
	loadOneSample();

	// return it (s may be null here)
	return s;
    }

    private void loadOneSample() throws IOException {
	// don't know start year, yet
	Year start = null;

	boolean firstLine=true; // true for the first iteration only
	boolean isProcessed=false;
	boolean isIndexed=false;
	boolean isSummed=false;

	loop: // loop through data lines
	while (true) {
	    // read a line; halt on EOF
	    String line = r.readLine();
	    if (line == null)
		break;

	    // is a statistics line?  stop.
	    if (line.indexOf('.') != -1)
		break;

	    // from the first line of data, we determine the format
	    if (firstLine) {
		// error checking
		if (line.length() < 18)
		    throw new WrongFiletypeException(); // size matters

		// processed data?
		if (line.trim().length() % 6 != 0) // was: (line.charAt(6) != ' ')
		    isProcessed = true;

		// summed? / indexed?
		if (isProcessed && line.charAt(16) != ' ')
		    isSummed = true;
		else
		    isIndexed = true; // right?

		// get id
		s.meta.put("id", line.substring(0, 6).trim());

                // set format -- bug: doesn't this get overridden by guessIndexed() every time?
		if (isSummed)
		    s.meta.put("format", "S");
		else if (isIndexed)
		    s.meta.put("format", "I");
		else
		    s.meta.put("format", "R");

		// make a count List, if it's summed
		if (isSummed)
		    s.count = new ArrayList();

		// don't come back here
		firstLine = false;
	    }

	    // the last line may optionally (my favorite word!) have
	    // some stats.  the spec is at
	    // ftp://ftp.ngdc.noaa.gov/paleo/treering/treeinfo.txt
	    // this is after a 999/9990 value, so I don't have to
	    // explicitly check for it (but if I did, I'd check for a
	    // '.' on the line).  anyway, if this is ever really
	    // needed, here's what the data is, but I'm just going to
	    // ignore it for now.

	    // -- cols 0-5: site id
	    // -- cols 7-9: number of years (max 999 ... augh!)
	    // -- cols 12-15: first order autocorrelation (what's that?)
	    // -- cols 18-21: standard deviation
	    // -- cols 24-27: mean sensitivity (we compute this ourselves)
	    // -- cols 28-34: mean index value (what's that?)
	    // -- cols 36-43: sum of indices (huh?)
	    // -- cols 45-52: sum of squares of indices (aiee!)
	    // -- cols 61-62: max# of series (*thump*)

	    // switch on format
	    if (!isProcessed) { // raw

		if (start == null)
		    start = new Year(line.substring(8, 12));

		for (int j=12; j+6<=line.length(); j+=6) {
		    String stringlet = line.substring(j, j+6).trim();
		    if (stringlet.length() == 0)
			break;
		    int value = Integer.parseInt(stringlet);
		    if (value != 999)
			s.data.add(new Integer(value));
		    else
			break loop; // sometimes there's "999 0 0 0", so explicitly stop
		}

	    } else { // processed

		if (start == null)
		    start = new Year(line.substring(6, 10));

		for (int j=10; j<line.length(); j+=7) { // or: j+4<=line.length()
		    // figure out data value
		    int lastSpace = line.substring(j, j+4).lastIndexOf(' ');
		    String stringlet = line.substring(j+lastSpace+1, j+4);
		    int value = Integer.parseInt(stringlet);
		    if (value != 9990) {
			s.data.add(new Integer(value));

			// count
			if (isSummed) {
			    lastSpace = line.substring(j+4, j+7).lastIndexOf(' ');
			    stringlet = line.substring(j+4+lastSpace+1, j+7);
			    s.count.add(new Integer(stringlet));
			}
		    } else if (!s.data.isEmpty()) {
			// last line might be len=80, padded with spaces.  so 9990 + (already have data) => break.
			break loop;
		    }
		}
	    }
	}

	// RIGHT HERE i've finished reading the line that ends this
	// sample.  if i wanted to try reading another sample here,
	// i'd have to: (1) have kept meta-without-id in a safe place,
	// (2) make a new sample, clone meta-without-id, and add the
	// next id (line.substring[0..5]) to it, but generally just
	// (3) run the loop ("loop:" label) again.  piece o' cake.  be
	// sure to move 800-test (obsolete) and range computations
	// into the per-sample loop, and close it when it's all
	// finished (EOF).

	// no data read?  return null, i suppose
	if (s.data.isEmpty()) {
	    s = null;
	    return;
	}

	// the 800-rule
	s.guessIndexed();

	// set range
	s.range = new Range(start, s.data.size());
    }

    // -------------------- end composite loader --------------------

    // ---
    // ------------------------------ start saver ------------------------------
    // ---

    private void saveFirstLine() throws IOException {
	StringBuffer line1 = new StringBuffer((String) s.meta.get("title"));
	line1.ensureCapacity(86);

	if (line1.length() > 80) {
	    line1 = new StringBuffer(line1.substring(0, 80));
	} else {
	    int oldEnd = line1.length();
	    line1.setLength(80);
	    for (int i=oldEnd; i<80; i++)
		line1.setCharAt(i, ' ');
	}

	// relat or absol
	line1.append(s.isAbsolute() ? "ABSOL" : "RELAT");

	// write
	w.write(line1.toString());
	w.newLine();
    }

    // 6-digit code
    private String code; // not pretty here, but leftPad() needs this... [it doesn't, whassup?]
    private void make6digitCode() {
	if (s.meta.get("id") != null)
	    code = s.meta.get("id").toString();
	else
	    code = "000000";

	// ensure exactly 6 chars
	if (code.length() > 6)
	    code = code.substring(0, 6);
	else while (code.length() < 6)
	    code += " ";
    }

    // write out 6-digit code, then year.
    private void saveRowHeader(int yearWidth, Year y) throws IOException {
	String prefix; // don't print the decade for the first one
	if (y.compareTo(s.range.getStart()) < 0)
	    prefix = s.range.getStart().toString();
	else
	    prefix = y.toString();
	while (prefix.length() < yearWidth)
	    prefix = " " + prefix;
	w.write(code + prefix);
    }

    private void saveData() throws IOException {
	// start and end years
	Year start = s.range.getStart();
	Year end = s.range.getEnd();

	// 6-digit code which starts each line
	make6digitCode();

	// if it's summed, we print spaces instead of [1]'s later
	boolean isSummed = s.isSummed();
	boolean isProcessed = s.isIndexed() || s.isSummed();

	// start year; processed files always start on the decade
	Year y = start;
	if (isProcessed)
	    y = y.add(-start.column());

	for (;;) {
	    // row header
	    if (y.column() == 0 || (y.equals(start) && !isProcessed))
		saveRowHeader((isProcessed ? 4 : 6), y);

	    // out of range (fixme: is isProcessed really needed here?)
	    if (y.compareTo(end) > 0 || (isProcessed && y.compareTo(start) < 0)) {
		if (!isProcessed) {
		    // "   999", and STOP
		    w.write("   999");
		    break;
		} else {
		    // "9990   " or "9990  0"
		    w.write(isSummed ? "9990  0" : "9990   ");
		}
	    } else { // in range
		// data: "%4d" / %6d
		w.write(leftPad(s.data.get(y.diff(start)).toString(),
				(isProcessed ? 4 : 6)));

		// count: "%3d" (right-align)
		if (isSummed)
		    w.write(leftPad(s.count.get(y.diff(start)).toString(), 3));
		else if (isProcessed) // which is really isIndexed
		    w.write("   ");
	    }

	    // eoln
	    if (y.column() == 9)
		w.newLine();

	    // processed files end only after 9cols+eoln
	    if (isProcessed && y.compareTo(end)>0 && y.column()==9)
		break;

	    // inc
	    y = y.add(+1);
	}

	w.newLine(); // fixme: is this possible one too many eolns?
    }

    // make sure this sample's range doesn't go BC
    private void verifyADonly() throws IOException {
	if (s.range.getStart().compareTo(new Year(1)) < 0)
	    throw new IOException("Can't save a file in Tucson format if it starts\n" +
				  "before year 1.  (Try redating it first.)");
    }

    /** Save a sample in Tucson format.
	@param filename file to save to
	@param sample Sample to save
	@exception IOException if something goes wrong */
    public void save(Sample sample) throws IOException {
	// copy sample ref
	s = sample;

	// punchcard formats like tucson generally can't handle BC
	verifyADonly();

	// open file
//        w = new BufferedWriter(new FileWriter(filename));

	// first line, then data
	saveFirstLine();
	saveData();

	// close file
	w.close();
    }

    // save a 3-line header.  itrdb uses these.  |id| is usually just 3
    // chars ("PFU ").
    private void save3LineHeader(String id) throws IOException {
	// ensure exactly 6 chars
	if (id.length() > 6)
	    id = id.substring(0, 6);
	else while (id.length() < 6)
	    id += " ";

	// write out lines -- mostly blank, fill in rest later?
	w.write(id + " 1 Untitled");
	w.newLine();
	w.write(id + " 2 ");
	w.newLine();
	w.write(id + " 3 ");
	w.newLine();
    }

    // ========================================
    // for debug only, right now.
    public static void main(String args[]) throws IOException {
	// what it does: if it's a dir, packs, else, unpacks.
	String filename = args[0];
	File f = new File(filename);

	if (f.isDirectory()) {
	    System.out.println("packing dir " + filename);
	    pack(filename);
	    // would need to delete each file, then the dir -- ?
	} else {
	    System.out.println("unpacking file " + filename);
	    unpack(filename);
	    // new File(filename).delete();
	}
    }
    // ========================================

    /*
      notes:
      - packed tucson files can't be read normally.  well, not exactly.  it's possibly destructive.
      - subclass tucson with PackedTucson, which has pack()/unpack() methods like above
      - normal load() just throws an i/o exception that says "this tucson is packed; use unpack..."
      - problem: tucson can load packed tucson files; solution: try packedtucson first
    */

    // given the name |dirname| of a folder of samples:
    // -- create a new packedtucson file called dirname.tuc
    // -- load each sample from dirname and add it to dirname.tuc
    public static void pack(String dirname) throws IOException {
	// get list of files
	File dir = new File(dirname);
	if (!dir.isDirectory())
	    throw new IOException("Not a directory!");
	File files[] = dir.listFiles();

	// pick a filename
	String filename = dir.getPath() + ".tuc";

	// set up saver
	Tucson t = new Tucson();
	t.w = new BufferedWriter(new FileWriter(filename));
	t.save3LineHeader("000   ");
	// fixme: id should be the minimal set of common chars to each id (like "167   ").  need a buffer for that.

	// foreach: load, save
	int n = files.length;
	for (int i=0; i<n; i++) {
	    try {
		t.s = new Sample(files[i].getPath());
	    } catch (WrongFiletypeException wfte) {
		// not a dendro file => ignore
	    }
	    t.saveData();
	}

	// close
	t.w.close();
    }

    // given the name of a packed file:
    // -- create a directory with the same name, but no extension (ioe if it can't)
    // -- load each sample from the packed tucson file
    // -- save each one into the directory
    // -- for safety, this does NOT delete the original file; if it succeeds, the front-end can easily do that
    public static void unpack(String filename) throws IOException {
	// open
	Tucson t = new Tucson();
	t.open(filename);

	// make the dir
	int dot = filename.lastIndexOf(".");
	// fixme: if dot==-1, i *should* do a little "mv x x.tuc" here and carry on
	if (dot == -1)
	    throw new IOException("oops #1...");
	String dirname = filename.substring(0, dot);
	File dir = new File(dirname);
	if (dir.exists()) // what if it already exists?  you call this error handling?
	    throw new IOException("oops #2...");
	dir.mkdir();

	// save each one
	for (;;) {
	    Sample s = t.loadNext();
	    if (s == null)
		break;

	    // construct new filename -- (is ".raw" ok?)
	    String base = dir.getPath() + File.separator + "Sample " + s.meta.get("id");
	    String out = base + ".raw";
	    if (new File(out).exists()) {
		int n = 2;
		do {
		    out = base + "-" + n + ".raw";
		    n++;
		} while (new File(out).exists());
	    }

	    s.save(out);
	}
    }
}
