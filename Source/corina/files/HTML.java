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
import corina.Element;
import corina.Weiserjahre;
import corina.Metadata;
import corina.Metadata.Field;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;

import java.text.DecimalFormat;

/**
   <p>Pretty-print HTML export.  Obviously, there's no way to import
   an HTML file (or reason you'd want to), but this is useful as a
   simple way to print a sample, an easy way to export into a modern
   word processor (like AbiWord or Microsoft Word), and as an easy way
   to put dendro data on the web, should that ever be desireable.</p>

   <p>If anybody ever gets around to adding options to the various
   exporters, there should be options on this class for which sections
   to output (data, metadata, Weiserjahre, etc.), because apparently
   for publication they only want the data/count section.  Also, give
   the option of printing out all metadata fields, or only the
   non-null ones.</p>

   <p>If, in a few years, everybody is using an XML-capable browser
   (like <a href="http://www.mozilla.org">Mozilla</a>), and the
   browser people have decided on a stylesheet system (XSLTT, CSS1,
   CSS2, etc.), this can be replaced completely with XML, which will
   be saveable <i>and</i> loadable, and still browser-viewable with
   stylesheets.  The user can even edit the stylesheet, thus giving
   him more control over the output.  Basically XML is a big win for
   everybody, if the browser writers and users can get their acts
   together.</p>

   <p>This class is not finished, because it's not a high priority at
   this point.  Left to do:</p>

   <ul>

     <li>make one-big-table into many-smaller-tables, so browsers can
     render it quicker</li>

     <li>internationalize it; only the metadata is, so far</li>

     <li>use the real element summaries</li>

     <li>highlight significant Weiserjahre intervals, like crossdating
     grids do</li>

     <li>put the count next to the WJ, and print only a histogram next
     to the data?</li>

     <li>in the metadata, replace newlines with "&lt;br /&gt;" (the
     comments field often uses this) (is there a standard ->HTML
     converter?)</li>

   </ul>

   There's still a lot of refactoring and optimization left to be done
   here.  (I think it would look particularly elegant in Lisp...)

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class HTML extends Filetype {

    /** Return the human-readable name of this file format.
	@return the name of this file format */
    public String toString() {
	return "HTML";
    }

    /** Return a unique character in the name to use as a mnemonic.
	@return character to use as mnemonic */
    public char getMnemonic() {
	return 'L';
    }

    private void printRawData() throws IOException {
	w.write("<table border=\"0\" width=\"100%\">\n");

	w.write("<tr>\n");
	w.write("  <th align=\"left\" width=\"*\">Date</th>");
	for (int i=0; i<10; i++)
	    w.write("<th align=\"right\" width=\"*\">" + i + "</th>");
	w.write("</tr>\n");

	// BUG: this probably breaks at BC/AD boundary

	for (Year y=s.range.getStart(); y.compareTo(s.range.getEnd()) <= 0; y=y.add(+1)) {
	    if (s.range.startOfRow(y))
		w.write("  <tr><th align=\"left\">" + y + "</th>\n  ");

	    if (y.equals(s.range.getStart()))
		for (int i=0; i<s.range.getStart().column(); i++)
		    w.write("<td align=\"right\"></td>");

	    int index = y.diff(s.range.getStart());
	    w.write("<td align=\"right\">" + s.data.get(index) + "</td>");

	    // end-of-line or end-of-sample
	    if (s.range.endOfRow(y))
		w.write("</tr>\n");
	}
	w.write("</table>\n");
    }

    private void printWJ() throws IOException {
	w.write("<table border=\"0\" width=\"60%\">\n");

	w.write("<tr>\n");
	w.write("  <th align=\"left\" width=\"*\">Date</th>");
	for (int i=0; i<10; i++)
	    w.write("<th align=\"right\" width=\"*\">" + i + "</th>");
	w.write("</tr>\n");

	for (Year y=s.range.getStart(); y.compareTo(s.range.getEnd())<=0; y=y.add(+1)) {
	    if (s.range.startOfRow(y))
		w.write("  <tr><th align=\"left\">" + y + "</th>\n  ");

	    if (y.equals(s.range.getStart()))
		for (int i=0; i<s.range.getStart().column(); i++)
		    w.write("<td align=\"right\"></td>");

	    int index = y.diff(s.range.getStart());
	    w.write("<td align=\"right\">" + Weiserjahre.toString(s, index) + "</td>");

	    if (s.range.endOfRow(y))
		w.write("</tr>\n");
	}
	w.write("</table>\n");
    }

    private void printElements() throws IOException {
	// table
	w.write("<table width=\"100%\">\n");

	// a form!  ... trust me ...
	w.write("<form>\n");

	// a row for each element...
	for (int i=0; i<s.elements.size(); i++) {

	    // start row
	    w.write("  <tr>");

	    // load element, and print a summary -- use real Element
	    // summaries here, in case the summaries are already
	    // loaded!
	    Sample sample;
	    try {
	        sample = ((Element) s.elements.get(i)).load();
		w.write("<td><input type=\"checkbox\" checked=\"" + ((Element) s.elements.get(i)).isActive() + "\"/></td>");
		w.write("<td>" + ((sample.meta.get("id") != null) ? sample.meta.get("id") : "") + "</td>");
		w.write("<td>" + sample.meta.get("filename") + "</td>");
		w.write("<td>" + ((sample.meta.get("unmeas_pre") != null) ? sample.meta.get("unmeas_pre") : "") + "</td>");
		w.write("<td>" + sample.range + "</td>");
		w.write("<td>" + ((sample.meta.get("unmeas_post") != null) ? sample.meta.get("unmeas_post") : "") + "</td>");
		w.write("<td>" + ((sample.meta.get("terminal") != null) ? sample.meta.get("terminal") : "") + "</td>");

	    // uh-oh.  i'm going to assume the load() went bad, and just print the filename
	    } catch (IOException ioe) {
		w.write("<td></td>");
		w.write("<td></td>");
		w.write("<td>" + ((Element) s.elements.get(i)).getFilename() + "</td>");
		w.write("<td></td>");
		w.write("<td></td>");
		w.write("<td></td>");
		w.write("<td></td>");
	    }

	    // end row
	    w.write("</tr>\n");
	}

	// close form, table
	w.write("<form>\n");
	w.write("</table>\n");
    }

    private void printSummedData() throws IOException {
	// container table and row
	w.write("<table border=\"0\" width=\"100%\">\n");
	w.write("<tr>\n");

	// left table
	w.write("<td width=\"60%\">\n");
	printRawData();
	w.write("</td>\n");

	// add spacer
	w.write("<td width=\"5%\"></td>\n");

	// right table
	w.write("<td width=\"35%\">\n");
	w.write("<table border=\"0\" width=\"100%\">\n");

	// count
	w.write("<tr>\n");
	w.write("<th align=\"left\" width=\"*\">Date</th>");
	for (int i=0; i<10; i++)
	    w.write("<th align=\"right\" width=\"*\">" + i + "</th>");
	w.write("</tr>\n");

	for (Year y=s.range.getStart(); y.compareTo(s.range.getEnd()) <= 0; y=y.add(+1)) {
	    if (s.range.startOfRow(y))
		w.write("<tr><th align=\"left\">" + y + "</th>\n");

	    if (y.equals(s.range.getStart()))
		for (int i=0; i<s.range.getStart().column(); i++)
		    w.write("<td></td>");

	    int index = y.diff(s.range.getStart());
	    w.write("<td align=\"right\">" + s.count.get(index) + "</td>");

	    if (s.range.endOfRow(y))
		w.write("</tr>\n");
	}
	w.write("</table>\n");

	// end container
	w.write("</td>\n");
	w.write("</tr>\n");
	w.write("</table>\n");
    }

    private void printMeta() throws IOException {
	w.write("<table border=\"0\" width=\"100%\">\n");

	w.write("  <tr>\n");
	w.write("  <th align=\"left\">Key</th><th align=\"left\">Value</th>\n");
	w.write("  </tr>\n");

	// get the fields from the Metadata class -- this ensures
	// they're in order, and i can get the localized strings, and
	// descriptions

	for (int i=0; i<Metadata.fields.length; i++) {
	    Field f = Metadata.fields[i];
	    w.write("  <tr>\n");
	    w.write("    <td align=\"left\">" + f.description + "</td>\n");
	    Object v = s.meta.get(f.variable);
	    w.write("    <td align=\"left\">" + (v==null ? "" : v) + "</td>\n");
	    w.write("  </tr>\n");
	}

	w.write("</table>\n");
    }

    private Sample s;

    private void writeHTML() throws IOException {
	w.write("<html>\n");
	w.write("\n");
	w.write("<head>\n");
	w.write("<title>" + s + "</title>\n");
	w.write("</head>\n");
	w.write("\n");
	w.write("<body bgcolor=\"#ffffff\" link=\"#0000ee\" vlink=\"#551a8b\" alink=\"#0000ee\">\n");
	w.write("<h1>" + s + "</h1>\n");

	// header
	if (!s.isIndexed()) {
	    double radius = ((double) s.computeRadius()) / 1000.;
	    double average = radius / (double) s.data.size();
	    DecimalFormat df = new DecimalFormat("0.000");
	    w.write("Radius: " + df.format(radius) + " cm, Average ring width: " + df.format(average) + " cm\n\n");
	}

	// h2: data (summed, with count, or raw)
	w.write("<h2>Data</h2>\n");
	if (!s.isSummed())
	    printRawData();
	else
	    printSummedData();
	w.write("<p>Total number of rings: " + s.countRings() + "</p>\n");
	if (s.elements != null)
	    w.write("<p>Total number of elements: " + s.elements.size() + "</p>\n");

	// h2: metadata
	{
	    w.write("<h2>Metadata</h2>\n");
	    printMeta();
	}

	// h2: wj (optional)
	if (s.hasWeiserjahre()) {
	    w.write("<h2>Weiserjahre</h2>\n");
	    printWJ();
	}

	// h2: elements (optional)
	if (s.elements != null) {
	    w.write("<h2>Elements</h2>\n");
	    printElements();
	}

	w.write("</body>\n");
	w.write("\n");
	w.write("</html>\n");
    }

    /** Save a sample in HTML format.
	@param filename file to save to
	@param s Sample to save
	@exception IOException if something goes wrong
	@see Sample */
    public void save(Sample sample) throws IOException {
//	w = new BufferedWriter(new FileWriter(filename));
	s = sample;
	writeHTML();
	w.close();
    }

    /** Load a Sample from disk.  This class doesn't implement loading
	(and never will), so just throw an IOException for anybody
	who's crazy enough to try this.
	@param filename the file to load
	@return that file as a Sample
	@exception FileNotFoundException if the file cannot be found on disk
	@exception WrongFiletypeException if the file is obviously not
	this filetype
	@exception IOException if any other I/O exception occurs */
    public Sample load() throws FileNotFoundException,
                                               WrongFiletypeException,
					       IOException {
	throw new IOException("HTML is a write-only format for samples");
    }
}
