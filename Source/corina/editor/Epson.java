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

package corina.editor;

import corina.Year;
import corina.Sample;
import corina.Element;
import corina.Weiserjahre;

import java.text.DecimalFormat;
import java.text.MessageFormat;

import java.util.Date;
import java.util.StringTokenizer;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
   Backwards-compatibility to allow text-only printing to Epson (and
   compatible) dot-matrix printers.<p>

   If <i>either</i> the Windows Epson driver was better, or Java's
   printing API allowed printing raw text files directly, this
   wouldn't be necessary, but they aren't.<p>

   Editor calls Epson.print() to print.  Epson.print() actually uses
   Epson like a files.Filetype to save the sample to $TMP, and call a
   user-provided shell command to send that file directly to a print
   queue.  Ugh.  Unfortunately it's the simplest way to send raw text
   to a dot-matrix printer.

   I SHOULD use standard printing, and have them set up their systems
   to use the dot matrix printer.  But that might not be as pretty
   (legible) for normal text.

   I should look into 1.4's printing service, too.  That might provide
   a way to send raw text directly to a printer.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Epson {

    private BufferedWriter w;
    private Sample s;

    // FIXME: can i use w.newLine() to mean \n\r, and/or does this
    // really give a newline on the epson?  if yes to either, then use
    // it, otherwise write a "private void newline() { ... }".

    // sequence that tells an Epson to "print small text".
    private void small() throws IOException {
	w.write(15);
	w.write(0);
    }

    // sequence that tells an Epson to "print large text".
    private void large() throws IOException {
	w.write(18);
	w.write(0);
    }

    private String getUserName() {
        return System.getProperty("user.name", "(unknown user)");
    }
    
    // "unknown user", "radius", etc. need i18n
    private void printHeader(Sample s) throws IOException {
        w.write(s.meta.get("title") + "\r\n");
        w.write("Printed by: " + getUserName() + "\r\n");
        w.write(new Date() + "\r\n");
        w.write("\r\n");
        double radius = s.computeRadius() / 1000.;
        double average = radius / s.data.size();
        DecimalFormat df = new DecimalFormat("0.000");
        if (!s.isIndexed())
            w.write("Radius: " + df.format(radius) + " cm, " +
                    "Average ring width: " + df.format(average) + " cm\r\n\r\n");
    }

    private void printSummedData(Sample s) throws IOException {
	w.write("\r\n\r\n");
	small();
	Year decade=null;
	for (Year y=s.range.getStart(); y.compareTo(s.range.getEnd())<=0; y=y.add(+1)) {
	    // start of row?  print year
	    if (s.range.startOfRow(y)) {
		decade = y; // store this year!
		StringBuffer buf = new StringBuffer(y.toString());
		while (buf.length() < 5)
		    buf.insert(0, ' ');
		w.write(buf.toString());
	    }

	    // start of sample?  tab over to first column
	    if (y.equals(s.range.getStart()))
		for (int i=0; i<s.range.getStart().column(); i++)
		    w.write("      ");

	    // print data
	    int index = y.diff(s.range.getStart());
	    StringBuffer buf = new StringBuffer(s.data.get(index).toString());
	    while (buf.length() < 6)
		buf.insert(0, ' ');
	    w.write(buf.toString());

	    // end of row?  print count (entire line)
	    if (s.range.endOfRow(y)) {
		w.write("      "); // extra horizontal spacing

		if (y.compareTo(s.range.getEnd()) == 0) // last one, need to indent more
		    for (int i=s.range.getEnd().column()+1; i<10; i++)
			w.write("      ");

		for (int i=0; i<decade.column(); i++)
		    w.write("     ");

		for (Year yy=decade; yy.compareTo(y)<=0; yy=yy.add(+1)) {
		    buf = new StringBuffer(s.count.get(yy.diff(s.range.getStart())).toString());
		    while (buf.length() < 5)
			buf.insert(0, ' ');
		    w.write(buf.toString());
		}

		w.write("\r\n");
	    }
	}
	large();
	w.write("\r\n");

	// random data that Maryanne likes
	w.write("Number of samples in data set: " + s.elements.size() + "\r\n");
	w.write("Number of rings in data set:   " + s.countRings() + "\r\n");
	w.write("Length of data set:            " + s.range.span() + " years.\r\n");
	w.write("\r\n");
    }

    private void printRawData(Sample s) throws IOException {
	// print "1 2 3 ..." header line
	w.write("     ");
	for (int i=0; i<10; i++)
	    w.write("      " + i);
	w.write("\r\n\r\n\r\n\r\n");

	for (Year y=s.range.getStart(); y.compareTo(s.range.getEnd()) <= 0; y=y.add(+1)) {
	    // start of row?  print year
	    if (s.range.startOfRow(y)) {
		StringBuffer buf = new StringBuffer(y.toString());
		while (buf.length() < 5)
		    buf.insert(0, ' ');
		w.write(buf.toString());
	    }

	    // start of sample?  tab over to first column
	    if (y.equals(s.range.getStart()))
		for (int i=0; i<s.range.getStart().column(); i++)
		    w.write("       ");

	    // print data
	    int index = y.diff(s.range.getStart());
	    StringBuffer buf = new StringBuffer(s.data.get(index).toString());
	    while (buf.length() < 7)
		buf.insert(0, ' ');
	    w.write(buf.toString());

	    // end of row?  bunch of newlines
	    if (s.range.endOfRow(y))
		w.write("\r\n\r\n\r\n");
	}
    }

    private void printMetadata(Sample s) throws IOException {
	if (s.meta.containsKey("id"))
	    w.write("ID Number " + s.meta.get("id") + "\r\n");
	if (s.meta.containsKey("title"))
	    w.write("Title of sample: " + s.meta.get("title") + "\r\n");
	w.write(s.isAbsolute() ? "Absolutely dated\r\n" : "Relatively dated\r\n");
	if (s.meta.containsKey("unmeas_pre"))
	    w.write(s.meta.get("unmeas_pre") + " unmeasured rings at beginning of sample.\r\n");
	if (s.meta.containsKey("unmeas_post"))
	    w.write(s.meta.get("unmeas_post") + " unmeasured rings at end of sample.\r\n");
	if (s.meta.containsKey("filename"))
	    w.write("File saved as " + s.meta.get("filename") + "\r\n");

        // - comments -- loop
        if (s.meta.containsKey("comments")) {
            int start = 0;
            String comments = (String) s.meta.get("comments");
            StringTokenizer tok = new StringTokenizer(comments, "\n");
            int n = tok.countTokens();
            for (int i=0; i<n; i++)
                w.write("Comments: " + tok.nextToken());
        }

        if (s.meta.containsKey("type"))
            w.write("Type of sample " + s.meta.get("type") + "\r\n");
        if (s.meta.containsKey("species"))
            w.write("Species: " + s.meta.get("species") + "\r\n");
        if (s.meta.containsKey("format")) { // use a switch?
            if (s.meta.get("format").equals("R"))
                w.write("Raw format\r\n");
            else if (s.isIndexed())
                w.write("Indexed format\r\n");
            else
                w.write("Unknown format\r\n");
        }
        if (s.meta.containsKey("sapwood"))
            w.write(s.meta.get("sapwood") + " sapwood rings.\r\n");
	if (s.meta.containsKey("pith")) {
	    String p = (String) s.meta.get("pith");
	    if (p.equals("P"))
		w.write("Pith present and datable\r\n");
	    else if (p.equals("*"))
		w.write("Pith present but undatable\r\n");
	    else if (p.equals("N")) // uppercase only?
		w.write("No pith present\r\n");
	    else
		w.write("Unknown pith\r\n");
	}
	if (s.meta.containsKey("terminal"))
	    w.write("Last ring measured " + s.meta.get("terminal") + "\r\n");
	if (s.meta.containsKey("continuous")) {
	    String c = (String) s.meta.get("continuous");
	    if (c.equals("C")) // uppercase only?
		w.write("Last ring measured is continuous\r\n");
	    else if (c.equals("R")) // uppercase only?
		w.write("Last ring measured is partially continuous\r\n");
	}
	if (s.meta.containsKey("quality"))
	    w.write("The quality of the sample is " + s.meta.get("quality") + "\r\n");
    }

    private void printElements(Sample s) throws IOException {
	w.write("\r\n");

	w.write("Elements:\r\n");
	small();

	for (int i=0; i<s.elements.size(); i++) {
	    Element e = (Element) s.elements.get(i);

	    w.write(e.active ? "* " : "  ");

	    try {
	        Sample sample = e.load();

		// crop the filename to 40 chars
		StringBuffer filename = new StringBuffer((String) sample.meta.get("filename"));
		filename.setLength(40);
		
		// grab summary info
		Object meta[] = new Object[] {
		    sample.meta.get("id"), // was: .toString() (?)
		    filename,
		    sample.meta.get("pith"),
		    sample.meta.get("unmeas_pre"),
		    sample.range,
		    sample.meta.get("unmeas_post"),
		    sample.meta.get("terminal"),
		};
		for (int j=0; j<meta.length; j++)
		    if (meta[j] == null)
			meta[j] = "";
		w.write(MessageFormat.format("{0} " + // id number // was: {0,number,#}
					     "{1} " + // filename
					     "{2} " + // pith
					     "{3} " + // unmeas_pre
					     "({4}) " + // range
					     "{5} " + // unmeas_post
					     "{6}\r\n", // terminal
					     meta));
	    } catch (Exception ioe) { // was: IOException
		w.write(e.filename + "\r\n"); // this is "old-style"
	    }

	}

	large();
	w.write("\r\n");
    }

    private void printWeiserjahre(Sample s) throws IOException {
	w.write("\r\n");
	w.write("Weiserjahre data\r\n");
	small();

	// <0, because wj is smaller than data -- uh, it's not now.  fixme?
	for (Year y=s.range.getStart(); y.compareTo(s.range.getEnd())<0; y=y.add(+1)) {
	    if (s.range.startOfRow(y)) {
		String ys = y.toString();
		while (ys.length() < 5)
		    ys = " " + ys;
		w.write(ys);
	    }

	    int i = y.diff(s.range.getStart());

	    w.write(Weiserjahre.toStringFixed(s, i, 9)); // "####/####" => 9

	    if (s.range.endOfRow(y))
		w.write("\r\n");
	}

	large();
	w.write("\r\n");

	// 3 more lines: wj summary
	int sig = s.countSignificantIntervals();
	w.write("Number of intervals with >3 samples: " + s.count3SampleIntervals() + "\r\n");
	w.write("Number of significant intervals: " + sig + "\r\n");
	double pct = (double) sig / (double) s.incr.size();
	w.write("Percent significant intervals: " + (new DecimalFormat("0.0%")).format(pct) + "\r\n");
    }

    /** Export the given sample to a (temporary) file for sending to
	an Epson queue.
	@param filename the tempfile to save to
	@param s the sample to export
	@exception IOException if an I/O error occurs */
    private void save(String filename, Sample s) throws IOException {
	w = new BufferedWriter(new FileWriter(filename));

	printHeader(s);

	if (s.isSummed())
	    printSummedData(s);
	else
	    printRawData(s);

	printMetadata(s);

	if (s.hasWeiserjahre())
	    printWeiserjahre(s);

	if (s.elements != null)
	    printElements(s);

	w.close();
    }

    public static void print(Sample s) throws IOException {
	// save to temp file
	File temp = File.createTempFile("corina-", ".tmp");
	temp.deleteOnExit(); // does this really work?
	(new Epson()).save(temp.getPath(), s);

	// construct printer command
	String command = MessageFormat.format(
			    System.getProperty("corina.print.command"),
			    new Object[] { temp.getPath() });

	// run print command
	Runtime.getRuntime().exec(command);
    }
}
