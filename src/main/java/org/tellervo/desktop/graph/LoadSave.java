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

package org.tellervo.desktop.graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.io.WrongFiletypeException;
import org.tellervo.desktop.sample.ElementFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


// methods for loading and saving a bunch of Graphs

public class LoadSave {

    // save the list of graphs under a given name
    @SuppressWarnings("unchecked")
	public static void save(String filename, List graphs) throws IOException {
        // open for writing
        BufferedWriter w = new BufferedWriter(new FileWriter(filename));

        try {
          // XML header
          w.write("<?xml version=\"1.0\"?>");
          w.newLine();
  
          w.newLine();
  
          // begin graph
          w.write("<graphs>");
          w.newLine();
  
          w.newLine();
  
          // each graph
          for (int i=0; i<graphs.size(); i++) {
              w.write("  " + ((Graph) graphs.get(i)).toXML());
              w.newLine();
          }
  
          w.newLine();
  
          // end graph
          w.write("</graphs>");
          w.newLine();
        } finally {
          try {
            // close
            w.close();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }

    }

    // try to load a plot from disk
    // (synch because temp samples would get overwritten)
    @SuppressWarnings("unchecked")
	public static synchronized List load(String filename) throws IOException {
        // this is load(String) from Grid.java
        // REFACTOR: i should be able to load an xml file in one line.  dunno why
        // sax doesn't let me do that, but i should write a wrapper for this crap.
        // something like: try { my_blob = XML.load("filename", SomeHandler.class); } catch (SE) { ... }
        try {
            // make a new XML parser
            XMLReader xr = XMLReaderFactory.createXMLReader();

            // ... configure it to use a my SampleHandler ...
            GraphHandler loader = new GraphHandler();
            xr.setContentHandler(loader);
            xr.setErrorHandler(loader);

            // ... and feed it the file
            BufferedReader r = new BufferedReader(new FileReader(filename));
            xr.parse(new InputSource(r));
        } catch (Exception e) { // (SAXException se) {
                                // if (se.getMessage().equals("Not a graph!"))
            throw new WrongFiletypeException();
            // else
            // throw new IOException("SAX exception: " + se.getMessage());
        }

        return samples;
        }

    // temp storage
    @SuppressWarnings("unchecked")
	private static List samples;

    // sax2 handler for load()
    private static class GraphHandler extends DefaultHandler {
	// state
	float scale;
	int xoffset, yoffset;
	String filename;
	@SuppressWarnings("unchecked")
	@Override
	public void startDocument() {
	    // start a fresh list
	    samples = new ArrayList();
	}
	@Override
	public void startElement(String uri, String name, String qName, Attributes atts)
	    throws SAXException {
	    // "graphs" is toplevel; ignore it
	    if (name.equals("graphs"))
		return;

	    // "graph" is a graph -- get scale/x/y
	    if (name.equals("graph")) {
		scale = Float.parseFloat(atts.getValue("scale"));
		xoffset = Integer.parseInt(atts.getValue("xoffset"));
		yoffset = Integer.parseInt(atts.getValue("yoffset"));
	    }
	}
	@Override
	public void characters(char ch[], int start, int length) {
	    filename = new String(ch, start, length).trim(); // stringify
	}
	@SuppressWarnings("unchecked")
	@Override
	public void endElement(String uri, String name, String qName) {
	    // it's a <graph/>, right?
	    if (!name.equals("graph"))
		return;

	    try {
		// construct a Graph
		Graph g = new Graph(ElementFactory.createElement(filename).load());
		g.scale = scale;
		g.xoffset = xoffset;
		g.yoffset = yoffset;

		// add to the list
		samples.add(g);
	    } catch (IOException ioe) {
		// can't load?  ignore for now.  FIXME: report error
	    }
	}
    }
}
