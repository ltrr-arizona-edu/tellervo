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
import corina.Metadata;
import corina.Weiserjahre;

import java.util.List;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
   <p>TRML, my experimental Tree-Ring Markup Language.</p>

   It's long, it's ugly, it's probably fairly buggy, it has really
   lousy error handling, but it seems to work for the one file I've
   tested it on.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class TRML extends Filetype {
    /** Return the human-readable name of this file format.
	@return the name of this file format */
    public String toString() {
	return "TRML";
    }

    /** Load a sample in TRML format.
	@param filename file to load from
	@return Sample loaded from the file
	@exception FileNotFoundException if the file cannot be found
	@exception WrongFiletypeException if the file is obviously not
	this filetype
	@exception IOException if something goes wrong
	@see Sample */
    public Sample load() throws IOException {
	// make sure it's an xml file: first lines starts with "+"
	r.mark(50); // ASSUMES: the first line is <=50 chars
	String line = r.readLine();
	if (line==null || !line.startsWith("<?xml"))
	    throw new WrongFiletypeException(); // no header found
	r.reset();

	try {
	    // make a new XML parser
	    XMLReader xr = XMLReaderFactory.createXMLReader();

	    // ... configure it to use a my SampleHandler ...
	    TRMLHandler loader = new TRMLHandler();
	    xr.setContentHandler(loader);
	    xr.setErrorHandler(loader);

	    // ... and feed it the file
	    xr.parse(new InputSource(r));
	    return loader.getSample();
	} catch (SAXException se) {
	    System.out.println("got sax exception -- " + se);
	    se.printStackTrace();
	    throw new WrongFiletypeException();
	}
    }

    /** A SAX2 handler for loading saved TRML files. */
    private static class TRMLHandler extends DefaultHandler {
	private boolean readAnything = false;
	private Sample s = new Sample();
	public Sample getSample() {
	    return s;
	}

        public void startElement(String uri, String name,
				 String qName, Attributes atts) throws SAXException {
            // something has been read!  make sure it's a trml file
            if (!readAnything) {
                if (name.equals("treerings")) {
                    readAnything = true;
                    return;
                }

                // else
                throw new SAXException("Not a TRML file!"); // can't i do better?  wfte?
	    }

	    // if inactive, set flag
	    Object a = atts.getValue("active");
	    if (a != null)
		active = a.equals("true");

	    // data type
	    if (name.equals("data"))
		type = atts.getValue("type");
	    // TODO: use units, too?
        }
	private boolean active = true;
	private String type;
	private StringBuffer data = new StringBuffer();
	public void characters(char ch[], int start, int length) {
            data.append(new String(ch, start, length));
	}
	private Year start=null, end=null;
	private Range range=null;
        public void endElement(String uri, String name, String qName) {
	    // range
	    if (name.equals("start"))
		start = new Year(data.toString());
	    else if (name.equals("end"))
		end = new Year(data.toString());
	    if (start!=null && end!=null && range==null) {
		range = new Range(start, end);
		s.range = range;
	    }
	    if (name.equals("start") || name.equals("end")) {
		data = new StringBuffer();
		return;
	    }

	    // other metadata field
	    for (int i=0; i<Metadata.fields.length; i++) {
		Metadata.Field f = Metadata.fields[i];
		if (f.variable.equals(name)) {
		    s.meta.put(name, data.toString().trim());
		    data = new StringBuffer();
		    return;
		}
	    }

	    // count/incr/decr field
	    if (name.equals("v")) {
		// if list doesn't exist, create
		if (type.equals("width") && s.data==null) {
		    s.data = new ArrayList();
		}
		if (type.equals("count") && s.count==null) {
		    s.count = new ArrayList();
		}
		if (type.equals("incr") && s.incr==null) {
		    s.incr = new ArrayList();
		}
		if (type.equals("decr") && s.decr==null) {
		    s.decr = new ArrayList();
		}

		// parse value
		int x = Integer.parseInt(data.toString().trim());

		// add this value to list
		if (type.equals("width"))
		    s.data.add(new Integer(x));
		if (type.equals("count"))
		    s.count.add(new Integer(x));
		if (type.equals("incr"))
		    s.incr.add(new Integer(x));
		if (type.equals("decr"))
		    s.decr.add(new Integer(x));

		data = new StringBuffer();
		return;
	    }

	    // elements field
	    if (name.equals("element")) {
		if (s.elements == null)
		    s.elements = new ArrayList();
		Element e = new Element(data.toString(), active);
		s.elements.add(e);
		active = true;
		data = new StringBuffer();
	    }
        }
    }

    /** Save a Sample to disk.
	@param s the sample to save
	@exception IOException if an I/O exception occurs */
    public void save(Sample sample) throws IOException {
	w.write("<?xml version=\"1.0\"?>");
	w.newLine();

	// big fat disclaimer
	w.newLine();
	w.write("<!-- This file was created by Corina (http://corina.sf.net/).   -->");
	w.newLine();
	w.write("<!-- This is an experimental format, TRML, that is not intended -->");
	w.newLine();
	w.write("<!-- for actual use.  No other programs can read this format,   -->");
	w.newLine();
	w.write("<!-- and it is subject to change at any time.                   -->");
	w.newLine();
	w.write("<!-- You have been warned.                                      -->");
	w.newLine();
	w.write("<!-- Cheers, Ken (kbh7@cornell.edu)                             -->");
	w.newLine();
	w.newLine();

	w.write("<treerings>");
	w.newLine();

	w.write("   <metadata>");
	w.newLine();

	// range
	w.write("      <start>" + sample.range.getStart() + "</start>");
	w.newLine();
	w.write("      <end>" + sample.range.getEnd() + "</end>");
	w.newLine();

	// other fields
	for (int i=0; i<Metadata.fields.length; i++) {
	    Metadata.Field f = Metadata.fields[i];
	    Object x = sample.meta.get(f.variable);
	    if (x != null) {
		w.write("      <" + f.variable + ">" + x + "</" + f.variable + ">");
		w.newLine();
	    }
	}

	// TODO: if you want precision, make a new metadata field for it.
	// e.g., <precision>0.01</precision>

	w.write("   </metadata>");
	w.newLine();

	// data
	w.newLine();
	w.write("   <data type=\"width\" units=\"0.01mm\">");
	w.newLine();
	saveData(sample.data);
	w.write("   </data>");
	w.newLine();
	// TODO: add per-year comments?

	// count
	if (sample.count != null) {
	    w.newLine();
	    w.write("   <data type=\"count\" units=\"number\">");
	    w.newLine();
	    saveData(sample.count);
	    w.write("   </data>");
	    w.newLine();
	}

	// weiserjahre
	if (sample.incr != null) {
	    w.newLine();
	    w.write("   <data type=\"incr\" units=\"number\">");
	    w.newLine();
	    saveData(sample.incr);
	    w.write("   </data>");
	    w.newLine();

	    w.newLine();
	    w.write("   <data type=\"decr\" units=\"number\">");
	    w.newLine();
	    saveData(sample.decr);
	    w.write("   </data>");
	    w.newLine();
	}

	// elements
	if (sample.elements != null) {
	    w.newLine();

	    w.write("   <elements>");
	    w.newLine();

	    for (int i=0; i<sample.elements.size(); i++) {
		Element e = (Element) sample.elements.get(i);
		w.write("      <element" +
			(e.isActive() ? "" : " active=\"false\"") + ">" +
			e + "</element>");
		w.newLine();
	    }

	    w.write("   </elements>");
	    w.newLine();
	}

	w.write("</treerings>");
	w.newLine();

	// close
	w.close();
    }

    private static final int COLUMNS = 5;
    private void saveData(List data) throws IOException {
	for (int i=0; i<data.size(); i++) {
	    if (i % COLUMNS == 0)
		w.write("      ");
	    w.write("<v>" + data.get(i) + "</v>");
	    if (i % COLUMNS == COLUMNS-1 || i == data.size()-1)
		w.newLine();
	    else
		w.write(" ");
	}
    }
}
