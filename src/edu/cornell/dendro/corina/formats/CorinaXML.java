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

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.User;
import edu.cornell.dendro.corina.sample.*;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.webdbi.ResourceIdentifier;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jdom.*;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


/**
 */
public class CorinaXML implements Filetype {

	@Override
	public String toString() {
		return I18n.getText("format.corinaxml");
	}

	/**
	 * Quickly check to see if it's an XML document
	 * @param r
	 * @throws IOException
	 */
	private void quickVerify(BufferedReader r) throws IOException {
		r.mark(4096);

		String firstLine = r.readLine();
		if(firstLine == null || !firstLine.startsWith("<?xml"))
			throw new WrongFiletypeException();
		
		r.reset();
	}
	
	private void loadReferences(Sample s, List<Element> references) {
		for(Element ref : references) {
			if(ref.getName().equals("measurement")) {
				BaseSample bs = new BaseSample();
				String attr;
								
				// id? is this useful at all?
				attr = ref.getAttributeValue("id");
				if(attr != null)
					bs.setMeta("::dbid", attr);

				// Determine how we link to this element
				ResourceIdentifier rid = null;
				List<Element> links = ref.getChildren("link");
				rid = ResourceIdentifier.fromLinks(links);
				
				// no resource identifier?
				if(rid == null) {
					System.out.println("Resource identifier missing from measurement " +  
							ref.getAttributeValue("id"));
					continue;
				}
				
				bs.setMeta("filename", ":/\\:WEB:/\\:" + rid.toString());
				bs.setMeta("title", rid.toString());
				
				// create an element!
				Element innerMeta;
				if((innerMeta = ref.getChild("metadata")) != null) {
					loadMetadata(bs, innerMeta);
					
					// create an elements list
					if(s.getElements() == null)
						s.setElements(new ElementList());
					
					edu.cornell.dendro.corina.sample.Element cachedElement;
					
					// set the loader to a link to our element
					bs.setLoader(new CorinaWebElement(rid));
					
					// use that to make a cached element...
					cachedElement = new CachedElement(bs);
					
					// and add it to the elements list of our parent
					s.getElements().add(cachedElement);
				}
			} else {
				System.out.println("Reference type " + ref.getName() + " ignored.");
			}
		}
	}
	
	private void setUserFor(String metaKey, Element e, BaseSample s) {
		String ids = e.getAttributeValue("id");
		if(ids == null)
			return;
		
		// traverse the userlist in an order N fashion, finding the user by id
		List<User> users = (List<User>) App.dictionary.getDictionary("Users");
		for(User u : users) {
			if(u.getInternalRepresentation().equals(ids)) {
				s.setMeta(metaKey, u);
				return;
			}
		}
		
		System.out.println("Measurement contains unknown author " + ids + ":" + e.toString());
	}
	
	private void loadMetadata(BaseSample s, Element meta) {
		List<Element> elements = (List<Element>) meta.getChildren();

		for(Element e : elements) {
			String key = e.getName();
			String value = e.getValue();
			
			if(key.equals("name"))
				s.setMeta("title", value);
			else if(key.equals("owner"))
				setUserFor("owner", e, s);
			else if(key.equals("measuredBy"))
				setUserFor("author", e, s);
			else if(key.equals("description"))
				s.setMeta("comments", value);
			else if(key.equals("dating")) {
				String startYear = e.getAttributeValue("startYear");
				String type = e.getAttributeValue("type");
				String count = e.getAttributeValue("count");
				String positiveError = e.getAttributeValue("positiveError");
				String negativeError = e.getAttributeValue("negativeError");
				
				if(startYear == null || type == null || count == null) {
					System.out.println("bad dating tag");
					continue;
				}
				
				// first, do the strings
				if(type.equalsIgnoreCase("absolute"))
					s.setMeta("dating", "A");
				else if(type.equalsIgnoreCase("absolute with uncertainty"))
					s.setMeta("dating", "AU");
				else if(type.equalsIgnoreCase("relative"))
					s.setMeta("dating", "R");
				else {
					System.out.println("Unknown dating type: " + value);
					continue;
				}
				
				// now, do integer values
				try {
					// start with range/count
					int intYear = Integer.parseInt(startYear);
					int intCount = Integer.parseInt(count);					
					s.setRange(new Range(new Year(intYear), intCount));
					
					// then pos/neg error
					int intval;
					if(positiveError != null) {
						intval = Integer.parseInt(positiveError);
						s.setMeta("datingErrorPositive", intval);
					}
					if(negativeError != null) {
						intval = Integer.parseInt(negativeError);
						s.setMeta("datingErrorNegative", intval);
					}
					
				} catch (NumberFormatException nfe) {
					System.out.println("bad dating tag integer values");					
					continue;
				}
			}
			else if(key.equals("references")) {
				List<Element> references = e.getChildren();
				
				// sorry, no nesting
				if(s instanceof Sample)
					loadReferences((Sample) s, references);
			}
			else if(value != null){
				// store this anyway?
				s.setMeta("__:" + key, value);
				System.out.println("Unknown Metadata: " + key + " -> " + value);
			}
		}
	}

	private void loadAnnualReadings(Sample s, Element readingSet) throws IOException {
		List<Object> dataset = new ArrayList<Object>();
		List<Integer> countset = new ArrayList<Integer>();
		List<Integer> wjinc = null, wjdec = null;
		Integer lastYear = null;
		Year firstYear = null;
		boolean haveWeiserjahre = false;
		
		List<Element> readings = readingSet.getChildren();
		for(Element r : readings) {
			String yearStr = r.getAttributeValue("year");
			String countStr = r.getAttributeValue("count");
			String valStr = r.getAttributeValue("value");
			String wjStr = r.getAttributeValue("weiserjahre");
			Integer year, count, val;

			if(yearStr == null || countStr == null || valStr == null)
				throw new IOException("Missing values in readings!");
			
			try {
				year = Integer.parseInt(yearStr, 10);
				count = Integer.parseInt(countStr, 10);
				val = Integer.parseInt(valStr, 10);
			} catch (NumberFormatException nfe) {
				throw new IOException("Invalid values in readings!");
			}
			
			if(lastYear == null) {
				firstYear = new Year(year);
				lastYear = year;
			} else if(year - lastYear > 1) {
				throw new IOException("Readings are not contiguous!");
			}
			
			lastYear = year;
			dataset.add(val);
			countset.add(count);
			
			if(wjStr != null && !haveWeiserjahre) {
				// initialize
				wjinc = new ArrayList<Integer>();
				wjdec = new ArrayList<Integer>();
				
				// wj always starts with 0/0
				wjinc.add(new Integer(0));
				wjdec.add(new Integer(0));
				haveWeiserjahre = true;
			}
			
			if(haveWeiserjahre && wjStr == null)
				throw new IOException("WJ is not contiguous!");
			
			if(haveWeiserjahre) {
				int slashIndex = wjStr.indexOf("/");
				
				if(slashIndex < 0)
					throw new IOException("WJ is malformed");
	
				wjinc.add(new Integer(wjStr.substring(0, slashIndex)));
				wjdec.add(new Integer(wjStr.substring(slashIndex + 1, wjStr.length())));
			}
		}
		
		// copy our data into our sample
		s.setData(dataset);
		s.setCount(countset);
		s.setRange(new Range(firstYear, dataset.size()));
		
		if(haveWeiserjahre) {
			s.setWJIncr(wjinc);
			s.setWJDecr(wjdec);
		}
	}
	
	public void loadBasicMeasurement(BaseSample s, Element root) throws IOException {
		String attr;

		// store our id!
		attr = root.getAttributeValue("id");
		if(attr != null)
			s.setMeta("::dbid", attr);

		ResourceIdentifier myid = ResourceIdentifier.fromRootElement(root);
		if(myid != null)
			s.setMeta("::dbrid", myid);
		
		s.setMeta("filename", "invalid filename");
		s.setMeta("title", "measurement " + attr);
		
		// load metadata
		Element metadata = root.getChild("metadata");		
		if(metadata == null)
			throw new WrongFiletypeException("No metadata in measurement!");
		loadMetadata(s, metadata);		
	}
	
	public void loadMeasurement(Sample s, Element root) throws IOException {
		// first, load our base stuff...
		loadBasicMeasurement(s, root);
		
		// load readings...
		List<Element> readings = root.getChildren("readings");
		for(Element rs : readings) {
			String rtype = rs.getAttributeValue("type");
			String unitsStr = rs.getAttributeValue("units");
			Integer units;
			
			if(rtype == null) {
				System.out.println("Readings without type; ignoring.");
				continue;
			}
			
			try {
				units = Integer.parseInt(unitsStr);
			} catch (NumberFormatException nfe) {
				units = -6; // magic number? why not.
			}
			
			if(rtype.equals("annual")) {
				loadAnnualReadings(s, rs);
				
				// if no elements and no weiserjahre, 
				// get rid of count; it's 1 for everything.
				if(!s.hasWeiserjahre() && s.getElements() == null)
					s.setCount(null);
			}
			else
				System.out.println("Unknown reading type: " + rtype);
		}
	}
	
	
	public Sample load(BufferedReader r) throws IOException {		
		// first, quickly verify it without the overhead of an xml parser
		quickVerify(r);

		Sample s = new Sample();
		Document doc;
		
		try {
			doc = new SAXBuilder().build(r);
		} catch (JDOMException jdome) {
			System.out.println("JDOME: " + jdome);
			throw new WrongFiletypeException();
		}
		
		Element root = doc.getRootElement();
		
		// no root element??
		if(root == null)
			throw new WrongFiletypeException();
		
		if(root.getName().equals("measurement")) {
			loadMeasurement(s, root);
		}

		return s;
	}

	private Element saveMetadata(Sample s) {
		Element meta = new Element("metadata");

		// note title -> name
		if(s.hasMeta("title")) 
			meta.addContent(new Element("name").setText((String) s.getMeta("title")));

		// comments -> description
		if(s.hasMeta("comments")) 
			meta.addContent(new Element("description").setText((String) s.getMeta("comments")));

		// dating is kind of complex
		Element dating = new Element("dating");
		if(s.hasMeta("dating")) {
			String datingVal = (String) s.getMeta("dating");
			String datingType;
			
			if(datingVal.equals("A"))
				datingType = "Absolute";
			else if(datingVal.equals("R"))
				datingType = "Relative";
			else
				datingType = "Unknown";
			
			dating.setAttribute("type", datingType);
		}
		
		// don't forget range!
		Range range = s.getRange();
		if(range != null)
			dating.setAttribute("startYear", range.getStart().toString())
			.setAttribute("count", String.valueOf(range.span()));
		
		if(s.hasMeta("datingErrorPositive"))
			dating.setAttribute("positiveError", s.getMeta("datingErrorPositive").toString());
		if(s.hasMeta("datingErrorNegative"))
			dating.setAttribute("negativeError", s.getMeta("datingErrorNegative").toString());
		
		meta.addContent(dating);
		
		// these need IDs...
		if(s.hasMeta("owner")) {
			Object o = s.getMeta("owner");
			if(o instanceof User)
				meta.addContent(new Element("owner")
					.setText(o.toString())
					.setAttribute("id", ((User)o).getInternalRepresentation()));
		}
		if(s.hasMeta("author")) {
			Object o = s.getMeta("author");
			if(o instanceof User)
				meta.addContent(new Element("measuredBy")
					.setText(o.toString())
					.setAttribute("id", ((User)o).getInternalRepresentation()));
		}

		return meta;
	}
	
	public Element saveReadings(Sample s) {
		Element readings = new Element("readings");
		
		// maybe one day we want to have other values here?
		// or check some metadata?
		readings.setAttribute("type", "annual");
		readings.setAttribute("units", "-6");
		
		// get the data
		List<Object> data = s.getData();
		Year startYear = s.getRange().getStart();
		
		// add the data
		for(int i = 0; i < data.size(); i++) {
			Number n = (Number) data.get(i);
			Element reading = new Element("reading");
			
			reading.setAttribute("year", startYear.add(i).toString());
			reading.setAttribute("value", String.valueOf(n));
			readings.addContent(reading);
		}
		
		return readings;
	}
	
	private void saveXMLtoWriter(Element root, BufferedWriter w) throws IOException {
		Document doc = new Document();
		XMLOutputter outputter;			
		Format format = Format.getPrettyFormat();

		doc.setRootElement(root);

		format.setEncoding("UTF-8");
		outputter = new XMLOutputter(format);
		outputter.output(doc, w);
		
		// maybe we want to use this elsewhere?
		doc.detachRootElement();
	}
	
	public Element saveToElement(Sample s) {
		Element root = new Element("measurement");
		
		// do we have an ID stored?
		if(s.hasMeta("::dbid")) 
			root.setAttribute("id", (String) s.getMeta("::dbid"));
		
		// get metadata
		Element meta = saveMetadata(s);
		root.addContent(meta);
		
		Element readings = saveReadings(s);
		root.addContent(readings);

		return root;
	}
	
	public void save(Sample s, BufferedWriter w) throws IOException {
		Element root = saveToElement(s);
		
		saveXMLtoWriter(root, w);
	}
	
	public void save(List<Sample> samples, BufferedWriter w) throws IOException {
	}

	// default extension -- well, there isn't really one...
	public String getDefaultExtension() {
		return ".cdx";
	}
		
}
