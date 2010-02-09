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

package edu.cornell.dendro.corina.formats;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

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

 <p>There are two variants of Tucson files: raw and processed.
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

 DETERMINING IF A FILE IS INDEXED: 800-rule (obsolete?)

 <h3>Reference</h3>

 <p>See NOAA's
 <a href="http://www.ngdc.noaa.gov/paleo/treeinfo.html">Tree Ring Data
 Description</a>.  It's the only actual description of the Tucson format
 I've ever seen, but it doesn't document very much.</p>

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$
 */
public class Tucson implements Filetype {

	@Override
	public String toString() {
		return I18n.getText("format.tucson") + " (*"+ getDefaultExtension()+")";
	}

	public String getDefaultExtension() {
		return ".rwl"; 
	}

	// load 0-3 lines of header, if available, and fill meta appropriately
	private void loadHeader(BufferedReader r, Sample s) throws IOException {
		// if first 2 lines start with the same 6 chars, no header
		boolean readFirstLine = true; // read a header line?  usually...
		{
			r.mark(85 + 80 + 20); // 80+ABSOL + 80 + 20=buffer
			String l1 = r.readLine();
			String l2 = r.readLine();
			r.reset();
			if (l1 == null || l2 == null || l1.length() < 6 || l2.length() < 6)
				throw new WrongFiletypeException();
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
			r.mark(81 * 3); // 3 full lines
			String l1 = r.readLine();
			String l2 = r.readLine();
			String l3 = r.readLine();
			if (l1.substring(0, 6).equals(l2.substring(0, 6))
					&& l2.substring(0, 6).equals(l3.substring(0, 6))
					&& (l1.substring(6).trim().charAt(0) == '1')
					&& (l2.substring(6).trim().charAt(0) == '2')
					&& (l3.substring(6).trim().charAt(0) == '3')) {

				// was: substring(9,61) -- is there anything beyond the title i care about?
				if (l1.length() >= 9) {
					String title = l1.substring(9).trim();

					// read 4-letter species code
					if (l1.length() >= 65) { // also make sure it's in " ABCD " form?
						String species = l1.substring(61, 65);
						s.setMeta("species", species);
						title = l1.substring(9, 61).trim();
					}

					s.setMeta("title", title);
				}

				// ignore country?  (it's l2:9..22)

				// old way to do species: doesn't work?
				//		if (l2.length() >= 40)
				//		    setMeta("species", l2.substring(22, 40).trim());

				// altitude -- just dump in comments for now
				// lat/long -- just dump in comments for now
				if (l2.length() >= 57) {
					String alt = l2.substring(40, 45).trim();
					String latlong = l2.substring(47, 57).trim(); // decode me!
					s.setMeta("comments", "Altitude = " + alt + ", Lat/Long = "
							+ latlong);
				}

				// start/end -- ignore, they're in the data, too

				// author, and use 6-digit code
				if (l3.length() >= 10)
					s.setMeta("author", l3.substring(9).trim());
				// don't load id here: it's per-sample, not per-file

				// other stuff: if it's in the ITRDB, it's probably
				// Absolute, and Reconciled
				s.setMeta("dating", "A");
				s.setMeta("reconciled", "Y");
			} else {
				r.reset();
			}
		}

		// first line -- used by corina-tucson files only?
		else {
			String firstLine = r.readLine();
			if (firstLine.length() > 80) { // title + RELAT/ABSOL
				s.setMeta("title", firstLine.substring(0, 80).trim());
				s.setMeta("dating", firstLine.substring(80, 81));
			} else { // just title
				s.setMeta("title", firstLine.trim());
			}
		}
	}

	public Sample load(BufferedReader r) throws IOException {
		// new sample
		Sample s = new Sample();
		
		// gah! don't do this!
		//s.meta.clear();
		
		s.setMeta("dating", "R");

		// !!! load any header lines; below here is data only
		loadHeader(r, s);

		// just load the data for one sample
		loadOneSample(r, s); // OOPS: this returns NULL to mean no-data-present

		// that was a SUPER-SUBTLE bug!  ouch.

		// no data => not a tucson file at all
		// (why doesn't loadOne just throw wfte then?  good question...)
		if (s == null)
			throw new WrongFiletypeException();

		// return
		return s;
	}

	private void loadOneSample(BufferedReader r, Sample s) throws IOException {
		// don't know start year, yet
		Year start = null;

		boolean firstLine = true; // true for the first iteration only
		boolean isProcessed = false;
		boolean isIndexed = false;
		boolean isSummed = false;

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
				s.setMeta("id", line.substring(0, 6).trim());

				// set format -- bug: doesn't this get overridden by guessIndexed() every time?
				if (isSummed)
					s.setMeta("format", "S");
				else if (isIndexed)
					s.setMeta("format", "I");
				else
					s.setMeta("format", "R");

				// make a count List, if it's summed
				if (isSummed)
					s.setCount(new ArrayList());

				// don't come back here
				firstLine = false;
			}

			// the last line may optionally (my favorite word!) have
			// some stats.  the spec (hah!) is at
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

			/*
			 why this is dumb:
			 -- little gain: for long samples, saves a few ms of computation
			 -- (no gain: but if you do it lazily, or in a different thread, not even that)
			 -- loss: adds a restriction: max 999 years
			 -- only possible gain: if you wanted to list these stats for many samples quickly
			 -- (but then: a real database, or even a simple index file, would be better)
			 see Summary.java for my take on this.
			 */

			// switch on format
			if (!isProcessed) { // raw

				try {
					if (start == null)
						start = new Year(line.substring(8, 12));
				} catch (NumberFormatException nfe) {
					throw new WrongFiletypeException();
					// DESIGN: should this be IOE? [see below, too]
				}

				for (int j = 12; j + 6 <= line.length(); j += 6) {
					String stringlet = line.substring(j, j + 6).trim();
					if (stringlet.length() == 0)
						break;
					int value;
					try {
						value = Integer.parseInt(stringlet);
					} catch (NumberFormatException nfe) {
						throw new WrongFiletypeException();
						// DESIGN: should this be IOE?  probably a
						// corrupted file, no?
					}
					if (value != 999)
						s.getData().add(new Integer(value));
					else
						break loop; // sometimes there's "999 0 0 0", so explicitly stop
				}

			} else { // processed

				try {
					if (start == null)
						start = new Year(line.substring(6, 10));
				} catch (NumberFormatException nfe) {
					throw new WrongFiletypeException();
					// DESIGN: should this be IOE?
				}

				for (int j = 10; j < line.length(); j += 7) { // or: j+4<=line.length()
					// figure out data value
					int lastSpace = line.substring(j, j + 4).lastIndexOf(' ');
					String stringlet = line.substring(j + lastSpace + 1, j + 4);
					int value;
					try {
						value = Integer.parseInt(stringlet);
					} catch (NumberFormatException nfe) {
						throw new WrongFiletypeException();
					}
					if (value != 9990) {
						s.getData().add(new Integer(value));

						// count
						if (isSummed) {
							lastSpace = line.substring(j + 4, j + 7)
									.lastIndexOf(' ');
							stringlet = line.substring(j + 4 + lastSpace + 1,
									j + 7);
							s.getCount().add(new Integer(stringlet));
						}
					} else if (!s.getData().isEmpty()) {
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

		// no data read?  return a null, i suppose
		if (s.getData().isEmpty()) {
			s = null;
			// this is the other end of the super-subtle bug.  load()
			// was expecting a wfte!
			return;
		}

		// the 800-rule
		s.guessIndexed();

		// set range
		s.setRange(new Range(start, s.getData().size()));
	}

	// -------------------- end composite loader --------------------

	// ------------------------ start saver -------------------------

	private void saveFirstLine(Sample s, BufferedWriter w) throws IOException {
		StringBuffer line1 = new StringBuffer((String) s.getMeta("title"));
		line1.ensureCapacity(86);

		if (line1.length() > 80) {
			line1 = new StringBuffer(line1.substring(0, 80));
		} else {
			int oldEnd = line1.length();
			line1.setLength(80);
			for (int i = oldEnd; i < 80; i++)
				line1.setCharAt(i, ' ');
		}

		// relat or absol
		line1.append(s.isAbsolutelyDated() ? "ABSOL" : "RELAT");

		// write
		w.write(line1.toString());
		w.newLine();
	}

	// 6-digit code
	private String make6digitCode(Sample s) {
		String code;

		if (s.getIdentifier().getValue()!=null){
			code = s.getIdentifier().getValue().toString();
			code = code.substring(code.length()-6);
		}
		else{
			code = "000000";
		}

		// ensure exactly 6 chars
		if (code.length() > 6)
			code = code.substring(0, 6);
		else
			while (code.length() < 6)
				code += " ";

		return code;
	}

	// write out 6-digit code, then year.
	private void saveRowHeader(BufferedWriter w, Sample s, String code,
			int yearWidth, Year y) throws IOException {
		String prefix; // don't print the decade for the first one
		if (y.compareTo(s.getRange().getStart()) < 0)
			prefix = s.getRange().getStart().toString();
		else
			prefix = y.toString();
		while (prefix.length() < yearWidth)
			prefix = " " + prefix;
		w.write(code + prefix);
	}

	protected void saveData(Sample s, BufferedWriter w) throws IOException {
		// punchcard formats like tucson generally can't handle BC (we
		// might not save it with the range it's date to, since tucson
		// can't handle BC years -- so this will often (but not
		// always) be set to s.range.)
		Range r = computeRange(s);

		// start and end years
		Year start = r.getStart();
		Year end = r.getEnd();

		// 6-digit code which starts each line
		String code = make6digitCode(s);

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
				saveRowHeader(w, s, code, (isProcessed ? 4 : 6), y);

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
				w.write(StringUtils.leftPad(s.getData().get(y.diff(start))
						.toString(), (isProcessed ? 4 : 6)));

				// count: "%3d" (right-align)
				if (isSummed)
					w.write(StringUtils.leftPad(s.getCount().get(y.diff(start))
							.toString(), 3));
				else if (isProcessed) // which is really isIndexed
					w.write("   ");
			}

			// processed files end only after 9cols+eoln
			if (isProcessed && y.compareTo(end) > 0 && y.column() == 9)
				break;

			// eoln
			if (y.column() == 9)
				w.newLine();

			// inc
			y = y.add(+1);
		}

		w.newLine();
	}

	// make sure this sample's range doesn't go BC.
	// simply flagging an error is dumb. why yell at the user for having data
	// that exceeds the capabilities of this format?
	// better:
	// -- if start>=1, use range as-is
	// -- if start+8000>=1, use range+8000 (some labs do this, right?)
	// -- if even that doesn't work, just start at 1001
	private Range computeRange(Sample s) {
		Year start = s.getRange().getStart();

		// if it's AD-only, we're fine
		if (start.compareTo(new Year(1)) >= 0)
			return s.getRange();

		// if adding 8000 makes it AD, do that
		if (start.add(8000).compareTo(new Year(1)) >= 0)
			return s.getRange().redateBy(8000);

		// ouch.  just start it at 1001.
		return s.getRange().redateStartTo(new Year(1001));
	}

	public void save(Sample s, BufferedWriter w) throws IOException {
		// first line, then data
		saveFirstLine(s, w);
		saveData(s, w);
	}

	public Boolean isPackedFileCapable() {
		return false;
	}

	public String getDeficiencyDescription() {
		return this.toString() + " file format has almost no metadata capabilities. " +
				"It does not handle the BC/AD boundary correctly so " +
				"datasets that span this period are offset by one year.  " +
				"This format is also unable to represent data prior to 1000BC.";
	}

	public Boolean isLossless() {
		return false;
	}
}
