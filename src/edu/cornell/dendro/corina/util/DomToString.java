package edu.cornell.dendro.corina.util;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Utility class takes a DOM element or document and turns it into a string
 * 
 * @author Lucas Madar
 *
 */

public class DomToString {
	private final Element domElement;
	private final boolean showXmlDeclaration;
	private final boolean pretty;

	public DomToString(Element domElement, boolean pretty) {
		this.domElement = domElement;
		this.showXmlDeclaration = false;
		this.pretty = pretty;
	}

	public DomToString(Document domDocument, boolean pretty) {
		this.domElement = domDocument.getDocumentElement();
		this.showXmlDeclaration = true;
		this.pretty = pretty;
	}
	
	/**
	 * Output the XML document to a string
	 */
	public String toString() {
		try {
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(domElement);
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			
			xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, showXmlDeclaration ? "no" : "yes");
			xformer.setOutputProperty(OutputKeys.INDENT, pretty ? "yes" : "no");
			
			xformer.transform(source, result);
			
			return sw.toString();
		} catch (Exception e) {
			return "<error: " + e.getMessage() + " >";
		}
	}
}
