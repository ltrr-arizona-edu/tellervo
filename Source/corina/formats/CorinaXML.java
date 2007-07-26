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

package corina.formats;

import corina.MetadataTemplate;
import corina.Year;
import corina.Range;
import corina.Sample;
import corina.Weiserjahre;
import corina.MetadataTemplate.Field;
import corina.core.App;
import corina.gui.Bug;
import corina.util.StringUtils;
import corina.util.GUIDGenerator;
import corina.ui.I18n;
import corina.util.XMLTag;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.io.StreamTokenizer;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

import java.net.URI;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.*; 
import javax.xml.transform.*; 
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 


/**
 */
public class CorinaXML implements Filetype {

	// Defaults
	public static final String CXML_FIELDSEPARATOR = ",";
	public static final String CXML_WHITESPACE = "\r\n\t ";
	public static final String CXML_SAMPLECOUNTSEPARATOR = "+";

	public String toString() {
		return I18n.getText("format.corina");
	}
	

	public Sample load(BufferedReader r) throws IOException {		
		// new empty sample
		Sample s = new Sample();
		s.meta.clear();

		Document doc;
		
		// create our document builder and our transformer.
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			doc = dbf.newDocumentBuilder().parse(new InputSource(r));
		} catch (ParserConfigurationException e) {
			new Bug(e);
			throw new WrongFiletypeException();
		} catch (SAXException e) {
			// not the write file type (no document root, probably)
			throw new WrongFiletypeException();
		}
		catch (FactoryConfigurationError e) { 
			new Bug(e);
			throw new WrongFiletypeException();			
		}

		//doc
		
		/*
		try {
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty("indent", "yes");
		} catch (TransformerConfigurationException e) {
			new Bug(e);
			throw new WrongFiletypeException();
		}
		*/

		// return
		return s;
	}

	// ****************************************
	
	private void saveReferences(Sample s, Document doc, Node parent) {
		Element data = doc.createElement("references");

		if(s.elements != null) {
			for (int i = 0; i < s.elements.size(); i++) {
				corina.Element el = (corina.Element) s.elements.get(i);
				URI uri = el.getURI();
				Element e = doc.createElement("element");
			
				if(!el.isActive())
					e.setAttribute("active", "false");
				e.setAttribute("path", uri.toString());
				data.appendChild(e);
			}
		}
		
		//TODO: Save image, document, etc references?
		
		parent.appendChild(data);
	}
	
	private String createStringDataset(List l) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < l.size(); i++) {
			if (i != 0)
				sb.append(CXML_FIELDSEPARATOR);
			sb.append(l.get(i).toString());
		}
		
		return sb.toString();
	}
	
	private String createStringWeiserjahreDataset(Sample s) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < s.data.size(); i++) {
			if (i != 0)
				sb.append(CXML_FIELDSEPARATOR);
			sb.append(Weiserjahre.toString(s, i));;
		}
		
		return sb.toString();
	}
	
	private void saveData(Sample s, Document doc, Node parent) {
		Element data = doc.createElement("data");
		Element e;
		Text t;
		
		data.setAttribute("type", "sample");
		data.setAttribute("startYear", s.range.getStart().toString());
		data.setAttribute("count", Integer.toString(s.range.span()));
		
		e = doc.createElement("value");
		t = doc.createTextNode(createStringDataset(s.data));
		e.appendChild(t);
		data.appendChild(e);

		// write out the counts for each data point
		if(s.count != null)
		{
			e = doc.createElement("count");
			t = doc.createTextNode(createStringDataset(s.count));
			e.appendChild(t);
			data.appendChild(e);			
		}

		// write out the counts for each data point
		if(s.hasWeiserjahre())
		{
			e = doc.createElement("weiserjahre");
			t = doc.createTextNode(createStringWeiserjahreDataset(s));
			e.appendChild(t);
			data.appendChild(e);			
		}
		
		parent.appendChild(data);
	}


	private void saveMeta(Sample s, Document doc, Node parent) {
		Element meta = doc.createElement("meta");
		
		Iterator i = MetadataTemplate.getFields();
		while (i.hasNext()) {
		    Field f = (Field) i.next();
		    String fieldName = f.getVariable();
		    Object fieldValue = s.meta.get(fieldName);
		    
		    if(fieldValue != null) {
		    	Element e = doc.createElement(fieldName);
		    	Text t;
		    	
		    	if(f.isMachineValue()) {
		    		e.setAttribute("value", fieldValue.toString());
		    		t = doc.createTextNode(f.getReadableValue(fieldValue.toString()));
		    	} else {
		    		t = doc.createTextNode(fieldValue.toString());
		    	}
		    	e.appendChild(t);
		    	meta.appendChild(e);
		    }
		}
		parent.appendChild(meta);
	}
	
	private void saveContainer(CXMLContainer c, Document doc, Node parent) {
		Sample s = c.sample;
		
		saveMeta(s, doc, parent);
		saveData(s, doc, parent);
		saveReferences(s, doc, parent);
	}
	
	public void saveContainers(List<CXMLContainer> l, Document doc, Node parent) {
		Element contents = doc.createElement("contents");
		
		for(int i = 0; i < l.size(); i++) {
			CXMLContainer c = l.get(i);
			
			Element obj = doc.createElement("object");
			obj.setAttribute("type", "corina-sample");
			obj.setAttribute("guid", c.GUID);
			
			saveContainer(c, doc, obj);

			contents.appendChild(obj);
		}
		
		parent.appendChild(contents);
	}

	public void saveHeader(List<CXMLContainer> l, Document doc, Node parent) throws IOException {
		Element header = doc.createElement("header");
		
		for(int i = 0; i < l.size(); i++) {
			CXMLContainer c = l.get(i);
			
			Element e = doc.createElement("object");
			e.setAttribute("type", "corina-sample");
			e.setAttribute("guid", c.GUID);
			Text t = doc.createTextNode(c.name);
			e.appendChild(t);
			header.appendChild(e);
			
		}
		parent.appendChild(header);
	}
	
	public void save(Sample s, BufferedWriter w) throws IOException {
		ArrayList<Sample> dummy = new ArrayList<Sample>(1);
		
		dummy.add(s);
		save(dummy, w);
	}
	
	public void save(List samples, BufferedWriter w) throws IOException {
		ArrayList<CXMLContainer> containers = new ArrayList();
		Document doc;
		Transformer xformer;
		
		// create our document builder and our transformer.
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			doc = dbf.newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			new Bug(e);
			return;
		}

		try {
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty("indent", "yes");
		} catch (TransformerConfigurationException e) {
			new Bug(e);
			return;
		}

		// Make containers for all of our samples.
		for(int i = 0; i < samples.size(); i++) {
			Sample s = (Sample) samples.get(i);
			CXMLContainer c = new CXMLContainer(s);
			
			containers.add(c);
		}
		
		Element root = doc.createElement("dendrocontainer");
		root.setAttribute("format", "corina-packed-guid");
		doc.appendChild(root);

		saveHeader(containers, doc, root);
		saveContainers(containers, doc, root);
		
		Source src = new DOMSource(doc);
		Result output = new StreamResult(w);
		try {
			xformer.transform(src, output);
		} catch (TransformerException e) {
			new Bug(e);
		}
		
	}

	// default extension -- well, there isn't really one...
	public String getDefaultExtension() {
		return ".cdx";
	}
		
	private class CXMLContainer {
		public CXMLContainer(Sample sample) {
			this.sample = sample;
			
			// If the sample already has a guid, get it. Otherwise, generate one. 
			if(sample.meta.containsKey("guid"))
				this.GUID = sample.meta.get("guid").toString();
			else
				this.GUID = GUIDGenerator.makeGUID();
			
			this.name = StringUtils.escapeForXML(sample.toString());
			
			
		}
		
		public Sample sample;
		public String GUID;	
		public String name;
	}	

}
