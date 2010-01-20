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
public class TucsonSimple implements Filetype {

	@Override
	public String toString() {
		return I18n.getText("format.tucsonsimple");
	}

	public String getDefaultExtension() {
		return ".RWL"; // .TUC? .MST?
	}

	public Sample load(BufferedReader r) throws IOException {
		// we don't do any loading; Tucson handles that for us.
		throw new WrongFiletypeException();
	}


	// ------------------------ start saver -------------------------

	// 6-digit code
	private String make6digitCode(Sample s) {
		String code;

		if (s.hasMeta("id"))
			code = s.getMeta("id").toString();
		else
			code = "000000";

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

		// dirty kludge hack: It's simple. we don't want any of the
		// advanced features of tucson.
		boolean isSummed = false;
		boolean isProcessed = false;

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
		// data only!
		saveData(s, w);
	}

	public Boolean isPackedFileCapable() {
		return false;
	}

	public String getDeficiencyDescription() {
		return this.toString() + " has almost no metadata capabilities. " +
		"It does not handle the BC/AD boundary correctly so " +
		"datasets that span this period are offset by one year.  " +
		"This format is also unable to represent data prior to 1000BC.";
	}

	public Boolean isLossless() {
		return false;
	}
}
