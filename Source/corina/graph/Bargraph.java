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

package corina.graph;

import corina.Year;
import corina.Range;
import corina.Element;
import corina.Sample;
import corina.util.Sort;

import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
   A bargraph.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Bargraph {

    public List bars; // of Element
    public Range range; // total range -- union of all bars[i].range

    // all these are inches, so adjust accordingly
    public double risers = 0.12; // from center up to top of riser
    public double pith = 0.07; // between parallel pith lines
    public double bark = 0.07; // between parallel bark lines
    public double sapwood = 0.03; // from center up to sapwood
    public String title = "click here to type a title";
    public boolean xaxis = true; // draw x-axis?
    public boolean xfixed = true;
    public double xscale = 100.0; // years per inch
    public boolean yfixed = false;
    public double yscale = 0.5; // inches per line

    public String getDocumentTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
	title = newTitle;
    }

    // given: list of Elements
    public Bargraph(List ss) throws IOException {
	// copy list of bars
	bars = ss;

	// make sure they've got details
	for (int i=0; i<bars.size(); i++)
	    ((Element) bars.get(i)).loadMeta();

        // sort into fallback order
        Sort.sort(bars, "range", true);

	// compute range -- union of all bars' ranges, plus some
	Range tmp = ((Element) bars.get(0)).range;
	for (int i=1; i<bars.size(); i++)
	    tmp = tmp.union(((Element) bars.get(i)).range);

	// ick, this isn't terribly pretty...
	Year early = tmp.getStart().add(-tmp.getStart().mod(100));
	Year late = tmp.getEnd().add(100);
	late = late.add(-late.mod(100));
	range = new Range(early, late);
    }

    // An XML parser for Bargraphs.
    private class BargraphHandler extends DefaultHandler {
	private Element currentBar = null;
	private String currentString = null;
	public void startDocument() {
	    // create list for bars
	    bars = new ArrayList();
	}
	public void startElement(String uri, String name, String qName, Attributes atts) {
	    // WRITE ME

	    // starting a new bar
	    if (name.equals("bar")) {
		currentBar = new Element(atts.getValue("filename")); // null if not set: flag error?
		return;
	    }
	}
	public void characters(char ch[], int start, int length) {
	    // store string
	    String currentString = (new String(ch, start, length)).trim();
	}
	public void endElement(String uri, String name, String qName) {
	    if (name.equals("title")) {
		title = currentString;
		currentString = null;
	    }

	    // WRITE ME

	    // ending a bar: add to list
	    if (name.equals("bar") && currentBar!=null) {
		bars.add(currentBar);
		currentBar = null;
		return;
	    }
	}
    }

    /** Load a bargraph, saved in XML format.
	@param filename the target to load
	@exception IOException if an I/O exception occurs while trying to load */
    public void load(String filename) throws IOException {
	try {
	    // make a new XML parser
	    XMLReader xr = XMLReaderFactory.createXMLReader();

	    // ... configure it to use a my SampleHandler ...
	    BargraphHandler loader = new BargraphHandler();
	    xr.setContentHandler(loader);
	    xr.setErrorHandler(loader);

	    // ... and feed it the file
	    FileReader r = new FileReader(filename);
	    xr.parse(new InputSource(r));
	} catch (SAXException se) {
	    throw new IOException("SAX exception: " + se.getMessage());
	}
    }

    // this is a BAD IDEA.  to the user, a-graph-is-a-graph-is-a-graph.  if you want to save
    // a graph, that's fine, but there should be one format for all graphs, not one per type.
    // <graph style="bar"> or <graph style="normal">, for example.
    
    /** Save a bargraph in XML format.
	@param filename the target to save to
	@exception IOException if an I/O exception occurs while trying
	to save */
    public void save(String filename) throws IOException {
	// open for writing
	BufferedWriter w = new BufferedWriter(new FileWriter(filename));

	// XML header
	w.write("<?xml version=\"1.0\"?>");
	w.newLine();

	w.newLine();

	// begin bargraph
	w.write("<bargraph>");
	w.newLine();

	w.newLine();

	/*
	  what needs to be stored?

	  per bargraph:
	  - title
	  - (filename?)
	  - x-scale: fit-to-width, or fixed + scale
	  - y-scale: fit-to-height, or fixed + scale
	  - (all the bars)
	  - author, date?
	  - (later: style (p- or c-style: xml dfn file), legend position)

	  per bar:
	  - filename of original data
	  - title
	  - range (start, end)
	  - sapwood count
	  - "++ information": pith, bark.
	  - (in order)
	*/

	// title
	w.write("  <title>" + title + "</title>");
	w.newLine();

	w.newLine();

	// scale
	w.write("  <scale>");
	w.newLine();

	/*
	  -- horiz=fixed, width=xxx --OR-- horiz=fit
	  -- vert=fixed, height=xxx --OR-- vert=fit
	 */

	if (xfixed)
	    w.write("    <horiz fixed=\"true\""); // x-scale (look ma, no arms!)  (--okay, no more intel jokes)
	w.newLine();

	w.write(""); // y-scale
	w.newLine();

	w.write("  </scale>");
	w.newLine();

	w.newLine();

	// each bar
	for (int i=0; i<bars.size()-1; i++) {
	    // get bar
	    Element b = (Element) bars.get(i);

	    // start bar
	    w.write("  <bar filename=\"" + b.filename + "\">");
	    w.newLine();

	    // title
	    w.write("    <title>" + b.details.get("title") + "</title>");
	    w.newLine();

	    // range
	    w.write("    <range>" + b.range + "</range>");

	    // sapwood
	    w.write("    <sapwood>" + b.numSapwood() + "</sapwood>");
	    w.newLine();

	    // pith
	    if (b.hasPith()) {
		w.write("    <pith />");
		w.newLine();
	    }

	    // bark
	    if (b.hasBark()) {
		w.write("    <bark />");
		w.newLine();
	    }

	    // end bar
	    w.write("  </bar>");
	    w.newLine();
	}

	// end bargraph
	w.write("</bargraph>");
	w.newLine();

	// close
	w.close();
    }
}
