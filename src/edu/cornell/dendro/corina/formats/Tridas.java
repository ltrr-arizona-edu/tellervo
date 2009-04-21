package edu.cornell.dendro.corina.formats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.tridas.TridasEntityMap;
import edu.cornell.dendro.corina.tridas.TridasIdentifier;
import edu.cornell.dendro.corina.tridas.TridasObject;
import edu.cornell.dendro.corina.tridas.TridasSeries;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.XMLDebug;
import edu.cornell.dendro.corina.webdbi.CorinaXML;

public class Tridas implements Filetype {
	/**
	 * The following are all tridas fields. For mappings, see Metadata.java.
	 */
	public static String TITLE = "title";
	public static String IDENTIFIER = "identifier";
	public static String ANALYST = "analyst";
	public static String DENDROCHRONOLOGIST = "dendrochronologist";
	public static String MEASURING_METHOD = "measuringMethod";
	public static String COMMENTS = "comments";
	public static String INTERPRETATION = "interpretation";
	public static String FIRST_YEAR = "firstYear";
	public static String CREATED_TS = "createdTimestamp";
	public static String MODIFIED_TS = "lastModifiedTimestamp";
	
	public void loadMetadataElement(BaseSample s, Element e, String prefix) {
		List<Element> children = e.getChildren();
		String name;
		
		if(prefix == null)
			name = e.getName();
		else
			name = prefix + "." + e.getName();
		
		// special case: generic fields
		// these map to "!name"
		if(name.equals("genericField")) {
			name = "!" + e.getAttributeValue("name");
		}
		
		// Here, perform any special case attribute tests
		
		// if we have children, time to recurse
		if(children.size() > 0) {			
			for(Element en : children) 
				loadMetadataElement(s, en, name);
		
			// We assume no mixed text/element mess. Please.
			return;
		}
		
		Metadata.Mapping value = Metadata.mapTridas(e, name);
		if(value == null) {
			System.out.println("Unhandled: " + name);
			return;
		}
		
		s.setMeta(value.metaTag, value.value);
	}
	
	private void loadFromObjectTree(Element root, List<BaseSample> samples, List<TridasObject> objects) {
		List<Element> elements = (List<Element>) root.getChildren("element", CorinaXML.TRIDAS_NS);
		List<Element> deeperObjects = (List<Element>) root.getChildren("object", CorinaXML.TRIDAS_NS);
		TridasIdentifier identifier = new TridasIdentifier(root.getChild("identifier", CorinaXML.TRIDAS_NS), "object");
		TridasObject thisObject = (TridasObject) TridasEntityMap.find(identifier);
		
		if(thisObject == null) {
			thisObject = new TridasObject(root);
			System.out.println("Object '" + thisObject.toFullString() + "' did not exist in cache!");
		}
		
		// add myself to the tail of the objects list
		objects.add(thisObject);
		
		// MUST process elements first!
		// We do a breadth-first traversal here because 'objects' list represents objects at this depth;
		// it's not so easy to go back.
		
		// ok, now recursively process any child objects
		for(Element childObject : deeperObjects) 
			loadFromObjectTree(childObject, samples, objects);
	}
	
	public BaseSample[] loadFromTree(Element treeRoot) throws IOException {
		List<BaseSample> samples = new ArrayList<BaseSample>();
		List<Element> treeChildren = (List<Element>) treeRoot.getChildren();

		// loop through every element in 'treeroot,' 
		// loading samples in order of appearance.
		
		for(Element child : treeChildren) {
			
			// First, check for sanity
			if(!child.getNamespace().equals(CorinaXML.TRIDAS_NS))
				throw new IOException("Not a valid tridas document");

			String childName = child.getName();
			if(childName.equals("measurementSeries") || childName.equals("derivedSeries")) {
				// whew, this is the easy, fun way!
				TridasSeries series = new TridasSeries(child);
				Sample s = new Sample();

				series.mapOntoSample(s);
				samples.add(s);
			}
			else if(childName.equals("object")) {
				// urgh, load an entire tree
				// pass an empty list: the object tree hasn't been loaded yet,
				// and loadFromObjectTree is recursive
				loadFromObjectTree(child, samples, new ArrayList<TridasObject>());
			}
			else 
				System.out.println("Unknown type '" + childName + "' in tridas tree ignored");
		}
		
		return (BaseSample[]) samples.toArray();
	}
	
	public void loadBasicSeries(BaseSample s, Element root) throws IOException {

		
		String rootName = root.getName();
		if(rootName.equals("measurementSeries") || rootName.equals("derivedSeries")) {
			// whew, this is the easy, fun way!
			TridasSeries series = new TridasSeries(root);
			
			series.mapOntoSample(s);
		}
		else if(rootName.equals("object")) {
			// urgh, load an entire tree
		}
		else {
			throw new IOException("Unknown tridas data format (not summary OR comprehensive)");
		}
		/*
		String tmpId = root.getAttributeValue("id");
		String tmpName = root.getName() + ((tmpId != null) ? ("." + tmpId) : "");
		
		s.setMeta(Metadata.TITLE, tmpName);
		
		// No filename?
		if(!s.hasMeta(Metadata.FILENAME))
			s.setMeta(Metadata.FILENAME, tmpName);
		
		// load tridas identifier
		Element tmp = root.getChild("identifier", CorinaXML.TRIDAS_NS);
		s.setMeta(Metadata.TRIDAS_IDENTIFIER, new TridasIdentifier(tmp, root.getName()));
		
		boolean haveLabCode = false;
		for(Element e : (List<Element>) root.getChildren()) {
			if(!e.getNamespace().equals(CorinaXML.TRIDAS_NS)) {
				// not a tridas object.
				// Should this happen? Maybe in the future?
				continue;
			}
			
			loadMetadataElement(s, e, null);			
		}
		
		if(s.getMeta(Metadata.TITLE).equals(tmpName))
			s.setMeta(Metadata.TITLE, s.getMeta(Metadata.NAME));
			*/
	}
	
	public void loadSeries(Sample s, Element root) throws IOException {
		loadBasicSeries(s, root);
	}

	public Sample load(BufferedReader r) throws IOException,
			WrongFiletypeException {
		// quickly check: am I an XML document?
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
		
		if(root.getName().equals("measurementSeries") || root.getName().equals("derivedSeries")) {
			loadSeries(s, root);
		}
		else
			throw new WrongFiletypeException();

		return s;
	}

	public void save(Sample s, BufferedWriter w) throws IOException {
		
		
	}

	public String getDefaultExtension() {
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
}
