package edu.cornell.dendro.corina.formats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.tridas.TridasIdentifier;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.webdbi.CorinaXML;

public class Tridas implements Filetype {
	
	public void loadBasicSeries(Sample s, Element root) throws IOException {
		String tmpId = root.getAttributeValue("id");
		String tmpName = root.getName() + ((tmpId == null) ? ("." + tmpId) : "");
		
		s.setMeta(Metadata.TITLE, tmpName);
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
			
			String key = e.getName();
			String value = e.getText();
			
			if(key.equals("title")) {
				s.setMeta(Metadata.NAME, value);
				
				if(!haveLabCode)
					s.setMeta(Metadata.TITLE, value);
			}
		}
		
	}
	
	public void loadSeries(Sample s, Element root) throws IOException {
		loadBasicSeries(s, root);
	}

	@Override
	public Sample load(BufferedReader r) throws IOException,
			WrongFiletypeException {
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

	@Override
	public void save(Sample s, BufferedWriter w) throws IOException {
		
		
	}

	@Override
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
