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
import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.Element;
import edu.cornell.dendro.corina.Weiserjahre;
import edu.cornell.dendro.corina.metadata.*;
import edu.cornell.dendro.corina.ui.I18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.List;
import java.util.Iterator;

import java.text.DecimalFormat;

/**
   Pretty-print HTML export.

   <p>Obviously, there's no way to import an HTML file (or reason
   you'd want to), but this is useful as a simple way to print a
   sample, an easy way to export into a modern word processor (like
   AbiWord or Microsoft Word), and as an easy way to put dendro data
   on the web, should that ever be desireable.</p>

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

   <p>(This class is unfinished, because it's not a high priority at
   this point.)</p>

   <h2>Left to do</h2>

   <ul>

     <li>make one-big-table into many-smaller-tables, so old browsers
     can render it more quickly (10 rows per table?)</li>

     <li>internationalize the rest of it; 7 keys left to
     extract/translate: Key/Value(for meta), Radius/Average ring
     width:, cm, Total number of rings, Total number of elements. --
     can i use capitalization on total_radius reliably?</li>

     <li>print a header for the elements table?

     <li>use the real element summaries, to save having to load them
     from disk again</li>

     <li>highlight significant Weiserjahre intervals, like crossdating
     grids do</li>

     <li>put the count next to the WJ, and print only a histogram next
     to the data?</li>

     <li>in the metadata, replace newlines (and angle brackets, and
     ampersands) with "&lt;br /&gt;" (the comments field often uses
     this) (is there a standard ->HTML converter?  maybe not, but i
     can write a StringUtils.escapeForHTML() method.)</li>

     <li>bug: it doesn't export the BC/AD boundary correctly, I
     believe

     <li>bug: what if the title of a sample is "<font color=\"red\">"?
     do i need to sanitize all user-entered text here?

     <li>bug: if the writer failed (which will probably never happen,
     but technically could) in printElements() (and possibly
     elsewhere) bad things could happen</li>
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class HTML implements Filetype {

    public String toString() {
	return I18n.getText("format.html");
    }

    public String getDefaultExtension() {
	return ".html";
    }

    private void printRawData(Sample s, BufferedWriter w) throws IOException {
	w.write("<table border=\"0\" width=\"100%\">");
	w.newLine();

	w.write("<tr>");
	w.newLine();
	w.write("  <th align=\"left\" width=\"*\">" +
		I18n.getText("year") + "</th>");
	for (int i=0; i<10; i++)
	    w.write("<th align=\"right\" width=\"*\">" + i + "</th>");
	w.write("</tr>");
	w.newLine();

	// BUG: this probably breaks at BC/AD boundary

	Range r = s.range;
	for (Year y=r.getStart(); y.compareTo(r.getEnd())<=0; y=y.add(+1)) {
	    if (s.range.startOfRow(y)) {
		w.write("  <tr><th align=\"left\">" + y + "</th>");
		w.newLine();
		w.write("  ");
	    }

	    if (y.equals(s.range.getStart()))
		for (int i=0; i<s.range.getStart().column(); i++)
		    w.write("<td align=\"right\"></td>");

	    int index = y.diff(s.range.getStart());
	    w.write("<td align=\"right\">" + s.data.get(index) + "</td>");

	    // end-of-line or end-of-sample
	    if (s.range.endOfRow(y)) {
		w.write("</tr>");
		w.newLine();
	    }
	}
	w.write("</table>");
	w.newLine();
    }

    private void printWJ(Sample s, BufferedWriter w) throws IOException {
	w.write("<table border=\"0\" width=\"60%\">");
	w.newLine();

	w.write("<tr>");
	w.newLine();
	w.write("  <th align=\"left\" width=\"*\">" +
		I18n.getText("year") + "</th>");
	for (int i=0; i<10; i++)
	    w.write("<th align=\"right\" width=\"*\">" + i + "</th>");
	w.write("</tr>");
	w.newLine();

	Range r = s.range;
	for (Year y=r.getStart(); y.compareTo(r.getEnd())<=0; y=y.add(+1)) {
	    if (r.startOfRow(y)) {
		w.write("  <tr><th align=\"left\">" + y + "</th>");
		w.newLine();
		w.write("  ");
	    }

	    if (y.equals(r.getStart()))
		for (int i=0; i<r.getStart().column(); i++)
		    w.write("<td align=\"right\"></td>");

	    int index = y.diff(r.getStart());
	    w.write("<td align=\"right\">" +
		    Weiserjahre.toString(s, index) +
		    "</td>");

	    if (r.endOfRow(y)) {
		w.write("</tr>");
		w.newLine();
	    }
	}
	w.write("</table>");
	w.newLine();
    }

    // return the metadata field |key| from |s|, as a string;
    // if that metadata field is empty or null, return "".
    private String emptyIfNull(Sample s, String key) {
	Object value = s.meta.get(key);
	return (value == null ? "" : value.toString());
    }

    private void printElements(Sample s, BufferedWriter w) throws IOException {
	// table
	w.write("<table width=\"100%\">");
	w.newLine();

	// a form!  ... trust me ...
	w.write("<form>");
	w.newLine();

	// a row for each element...
	for (int i=0; i<s.elements.size(); i++) {

	    // start row
	    w.write("  <tr>");

	    // load element, and print a summary
	    Element e = (Element) s.elements.get(i);

	    // TODO: use real Element summaries here, in case the
	    // summaries are already loaded!

	    try {
		Sample sample = e.load();
		w.write("<td><input type=\"checkbox\" checked=\"" +
			            e.isActive() + "\"/></td>");
		w.write("<td>" + emptyIfNull(sample, "id") + "</td>");
		w.write("<td>" + sample.meta.get("filename") + "</td>");
		w.write("<td>" + emptyIfNull(sample, "unmeas_pre") + "</td>");
		w.write("<td>" + sample.range + "</td>");
		w.write("<td>" + emptyIfNull(sample, "unmeas_post") + "</td>");
		w.write("<td>" + emptyIfNull(sample, "terminal") + "</td>");
	    } catch (IOException ioe) {
		// uh-oh.  i'm going to ASSUME the load() went bad,
		// and just print the filename.
		// (BUG: what if one of the write() calls threw the ioe?)
		w.write("<td></td>");
		w.write("<td></td>");
		w.write("<td>" + e.getFilename() + "</td>");
		w.write("<td></td>");
		w.write("<td></td>");
		w.write("<td></td>");
		w.write("<td></td>");
	    }

	    // end row
	    w.write("</tr>");
	    w.newLine();
	}

	// close form, table
	w.write("<form>");
	w.newLine();
	w.write("</table>");
	w.newLine();
    }

    private void printSummedData(Sample s, BufferedWriter w)
	throws IOException {

	// container table and row
	w.write("<table border=\"0\" width=\"100%\">");
	w.newLine();
	w.write("<tr>");
	w.newLine();

	// left table
	w.write("<td width=\"60%\">");
	w.newLine();
	printRawData(s, w);
	w.write("</td>");
	w.newLine();

	// add spacer
	w.write("<td width=\"5%\"></td>");
	w.newLine();

	// right table
	w.write("<td width=\"35%\">");
	w.newLine();
	w.write("<table border=\"0\" width=\"100%\">");
	w.newLine();

	// count
	w.write("<tr>");
	w.newLine();
	w.write("<th align=\"left\" width=\"*\">" +
		I18n.getText("year") + "</th>");
	for (int i=0; i<10; i++)
	    w.write("<th align=\"right\" width=\"*\">" + i + "</th>");
	w.write("</tr>");
	w.newLine();

	Range r = s.range;
	for (Year y=r.getStart(); y.compareTo(r.getEnd())<=0; y=y.add(+1)) {
	    if (s.range.startOfRow(y)) {
		w.write("<tr><th align=\"left\">" + y + "</th>");
		w.newLine();
	    }

	    if (y.equals(r.getStart()))
		for (int i=0; i<r.getStart().column(); i++)
		    w.write("<td></td>");

	    int index = y.diff(r.getStart());
	    w.write("<td align=\"right\">" + s.count.get(index) + "</td>");

	    if (r.endOfRow(y)) {
		w.write("</tr>");
		w.newLine();
	    }
	}
	w.write("</table>");
	w.newLine();

	// end container
	w.write("</td>");
	w.newLine();
	w.write("</tr>");
	w.newLine();
	w.write("</table>");
	w.newLine();
    }

    private void printMeta(Sample s, BufferedWriter w) throws IOException {
	w.write("<table border=\"0\" width=\"100%\">");
	w.newLine();

	w.write("  <tr>");
	w.newLine();
	w.write("  <th align=\"left\">Key</th><th align=\"left\">Value</th>");
	w.newLine();
	w.write("  </tr>");
	w.newLine();

	// get the fields from the MetadataTemplate class -- this
	// ensures they're in order, and i can get the localized
	// strings, and descriptions
	Iterator i = MetadataTemplate.getFields();
	while (i.hasNext()) {
	    MetadataField f = (MetadataField) i.next();
	    w.write("  <tr>");
	    w.newLine();
	    w.write("    <td align=\"left\">" + f.getFieldDescription() + "</td>");
	    w.newLine();
	    Object v = s.meta.get(f.getVariable());
	    w.write("    <td align=\"left\">" + (v==null ? "" : v) + "</td>");
	    w.newLine();
	    w.write("  </tr>");
	    w.newLine();
	}

	w.write("</table>");
	w.newLine();
    }

    public void save(Sample s, BufferedWriter w) throws IOException {
	w.write("<html>");
	w.newLine();
	w.newLine();
	w.write("<head>");
	w.newLine();
	w.write("<title>" + s + "</title>");
	w.newLine();
	w.write("</head>");
	w.newLine();
	w.newLine();
	w.write("<body bgcolor=\"#ffffff\" link=\"#0000ee\" " +
		        "vlink=\"#551a8b\" alink=\"#0000ee\">");
	w.newLine();
	w.write("<h1>" + s + "</h1>");
	w.newLine();

	// header
	if (!s.isIndexed()) {
	    float radius = ((float) s.computeRadius()) / 1000f;
	    float average = radius / (float) s.data.size();
	    DecimalFormat df = new DecimalFormat("0.000");
	    w.write("Radius: " + df.format(radius) + " cm, " +
		    "Average ring width: " + df.format(average) + " cm");
	    w.newLine();
	    w.newLine();
	}

	// h2: data (summed, with count, or raw)
	w.write("<h2>" + I18n.getText("tab_data") + "</h2>");
	w.newLine();
	if (!s.isSummed())
	    printRawData(s, w);
	else
	    printSummedData(s, w);
	w.write("<p>Total number of rings: " + s.countRings() + "</p>");
	w.newLine();
	if (s.elements != null) {
	    int n = s.elements.size();
	    w.write("<p>Total number of elements: " + n + "</p>");
	    w.newLine();
	}

	// h2: metadata
	{
	    w.write("<h2>" + I18n.getText("tab_metadata") + "</h2>");
	    w.newLine();
	    printMeta(s, w);
	}

	// h2: wj (optional)
	if (s.hasWeiserjahre()) {
	    w.write("<h2>" + I18n.getText("tab_weiserjahre") + "</h2>");
	    w.newLine();
	    printWJ(s, w);
	}

	// h2: elements (optional)
	if (s.elements != null) {
	    w.write("<h2>" + I18n.getText("tab_elements") + "</h2>");
	    w.newLine();
	    printElements(s, w);
	}

	w.write("</body>");
	w.newLine();
	w.newLine();
	w.write("</html>");
	w.newLine();
    }

    /**
       Load a Sample from disk.  This class doesn't implement loading
       (and never will), so just throw an IOException.  (This should
       never be called.)
    */
    public Sample load(BufferedReader r) throws WrongFiletypeException,
						IOException {
	throw new IOException("HTML is a write-only format for samples");
    }
}
