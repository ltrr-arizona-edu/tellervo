package edu.cornell.dendro.corina.util;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLDebug {
	
	public static void dumpDocument(Document d) {
		XMLOutputter xmlo = new XMLOutputter();
		xmlo.setFormat(Format.getPrettyFormat());
		
		try {
			xmlo.output(d, System.out);
		} catch(Exception e) {}
	}

	public static void dumpElement(Element d) {
		XMLOutputter xmlo = new XMLOutputter();
		xmlo.setFormat(Format.getPrettyFormat());
		
		try {
			xmlo.output(d, System.out);
		} catch(Exception e) {}
	}
}
