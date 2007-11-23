package edu.cornell.dendro.corina.webdbi;

import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/*
 * This class verifies Corina XML document structure, 
 * throws exceptions for critical errors, an
 */

public class CorinaDocumentInspector {
	private Document document;
	
	public CorinaDocumentInspector(Document xmlReply) {
		this.document = xmlReply;
	}
	
	public void verifyDocument() throws IOException {
		// debug!
		/*
		try {
			XMLOutputter outp = new XMLOutputter();
			outp.output(document, System.out);
		} catch (Exception e) {}
		*/
		// end debug
		
		Element root = document.getRootElement();
		
		if(root == null) 
			throw new MalformedDocumentException("document has no root element");
		
		if(!root.getName().equalsIgnoreCase("corina"))
			throw new MalformedDocumentException("root element is the wrong type");
		
		Element header;
		if((header = root.getChild("header")) == null)
			throw new MalformedDocumentException("document has no header");
		
		// do we check for content here? I'm going to say no.
		
		Element e;
		if((e = header.getChild("status")) == null) 
			throw new MalformedDocumentException("no status in document header");

		// status is ok? we're fine then.
		if(e.getText().compareToIgnoreCase("ok") == 0)
			return;		
		
		List messages = header.getChildren("message");
		for(int i = 0; i < messages.size(); i++) {
			e = (Element) messages.get(i);
			
			int code;
			String s = e.getAttributeValue("code");
			
			// if there's no code, we just ignore this?
			// yes, and we'll pick it up later.
			if(s == null)
				continue;
			
			code = Integer.parseInt(s);
			s = e.getText();
			
			switch(code) {
			case WebInterfaceException.ERROR_AUTHENTICATION_FAILED:
			case WebInterfaceException.ERROR_PERMISSION_DENIED:
			case WebInterfaceException.ERROR_LOGIN_REQUIRED:
				String nonce = header.getChildText("nonce");				
				throw new WebPermissionsException(code, s, nonce);
			
			default:
				throw new WebInterfaceException(code, s);
				
			}
			
		}
		
		// how'd we get here? it shouldn't happen.
		throw new WebInterfaceException(WebInterfaceException.ERROR_UNKNOWN, "Unspecified generic web interface error");
	}
}
