package edu.cornell.dendro.corina.webdbi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.SAXOutputter;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import edu.cornell.dendro.corina.util.XMLParsingException;

/*
 * This class verifies Corina XML document structure, 
 * throws exceptions for critical errors, an
 */

public class CorinaDocumentInspector {
	private Document document;
	
	public CorinaDocumentInspector(Document xmlReply) {
		this.document = xmlReply;		
	}
	
	public void verifyDocument() throws ResourceException {
		// debug!
		/*
		try {
			XMLOutputter outp = new XMLOutputter();
			outp.setFormat(Format.getPrettyFormat());
			outp.output(document, System.out);
		} catch (Exception e) {}
		*/
		// end debug
		
		Element root = document.getRootElement();
		
		if(root == null) 
			throw new MalformedDocumentException(document, "document has no root element");
		
		if(!root.getName().equalsIgnoreCase("corina"))
			throw new MalformedDocumentException(document, "root element is the wrong type");
		
		Element header;
		if((header = root.getChild("header", CorinaXML.CORINA_NS)) == null)
			throw new MalformedDocumentException(document, "document has no header");
		
		// do we check for content here? I'm going to say no.
		
		Element e;
		if((e = header.getChild("status", CorinaXML.CORINA_NS)) == null) 
			throw new MalformedDocumentException(document, "no status in document header");

		// status is ok? we're fine then.
		if(e.getText().compareToIgnoreCase("ok") == 0)
			return;		
		
		List<Element> messages = header.getChildren("message", CorinaXML.CORINA_NS);
		for(int i = 0; i < messages.size(); i++) {
			e = messages.get(i);
			
			int code;
			String s = e.getAttributeValue("code");
			
			// if there's no code, we just ignore this?
			// yes, and we'll pick it up later.
			if(s == null)
				continue;

			try {
				code = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				throw new ResourceException("The server experienced an internal error '" + s + 
						"': " + e.getTextNormalize());
			}
			
			s = e.getText();
			
			switch(code) {
			case WebInterfaceException.ERROR_AUTHENTICATION_FAILED:
			case WebInterfaceException.ERROR_PERMISSION_DENIED:
			case WebInterfaceException.ERROR_LOGIN_REQUIRED: {
				Element nonceElement = header.getChild("nonce", CorinaXML.CORINA_NS);
				
				// no nonce? wtf?
				if(nonceElement == null)
					throw new WebPermissionsException(code, s);
					
				throw new WebPermissionsException(code, s, nonceElement.getText(), 
						nonceElement.getAttributeValue("seq"));
			}
			
			default:
				throw new WebInterfaceException(code, s);
				
			}
			
		}
		
		// how'd we get here? it shouldn't happen.
		throw new WebInterfaceException(WebInterfaceException.ERROR_UNKNOWN, "Unspecified generic web interface error");
	}
	
	/**
	 * Validate the document against any schemas
	 */
	public void validate() throws XMLParsingException {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(getSchemaSources());
			ValidatorHandler validator = schema.newValidatorHandler();
			
			SAXOutputter saxo = new SAXOutputter(validator);
			
			saxo.output(document);
			
		} catch (SAXException saxe) {
			throw new XMLParsingException(saxe, document);
			
		} catch (JDOMException jdome) {
			throw new XMLParsingException(jdome, document);
			
		}
	}

	/**
	 * Get an array of schema sources
	 * @return
	 */
	private Source[] getSchemaSources() {
		ArrayList<StreamSource> schemas = new ArrayList<StreamSource>();
		Source[] ret;
		
		for(String[] source : VALIDATE_SCHEMAS) {
			InputStream is = findSchema(source[1]);
			
			// couldn't load that schema :(
			if(is == null)
				continue;
			
			schemas.add(new StreamSource(is));
		}
		
		ret = new Source[schemas.size()];
		ret = schemas.toArray(ret);
		
		return ret;
	}
	
	/**
	 * Returns an InputStream pointing to this schema file
	 * @param filename
	 * @return
	 */
    private InputStream findSchema(String filename) {
    	InputStream ret = CorinaDocumentInspector.class.getClassLoader().getResourceAsStream
    		("edu/cornell/dendro/webservice/schemas/" + filename);
    	
    	if(ret == null)
    		System.out.println("FAILED to load local schema: " + filename);
    	else
    		System.out.println("Loaded local schema: " + filename);
    	
    	return ret;
    }

    /**
     * A list of schema namespaces and their associated namespace files
     */
    
    private final static String VALIDATE_SCHEMAS[][] = {
    	// Order is important!
    	{ "http://www.w3.org/1999/xlink", "xlinks.xsd" },
    	{ "http://www.opengis.net/gml", "gmlsf.xsd" },
    	{ "http://www.tridas.org/1.1", "tridas.xsd" },
    	{ "http://dendro.cornell.edu/schema/corina/1.0", "corina.xsd" },
    };
    
}
