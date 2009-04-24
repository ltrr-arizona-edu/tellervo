package edu.cornell.dendro.corina.wsi;

import java.awt.Frame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jdom.Document;

import edu.cornell.dendro.corina.gui.BugReportDialog;
import edu.cornell.dendro.corina.gui.XMLDebugView;
import edu.cornell.dendro.corina.util.BugReport;

public class TransactionDebug {
	private static boolean isDebuggingEnabled = true;
	
	public static void sent(Document doc, String noun) {
		XMLDebugView.addDocument(doc, noun, false);
		
		lastOutDocument = doc;
		lastInDocument = null;		
	}
	
	public static void sent(Object out, String noun, JAXBContext context) {
		if(!isDebuggingEnabled)
			return;

		// Marshall it to an xml document
		try {
			Document doc = WebJaxbAccessor.marshallToDocument(context, out);
			
			sent(doc, noun);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
}
	
	public static void received(Object in, String noun, JAXBContext context) {		
		if(!isDebuggingEnabled)
			return;
		
		// Marshall it to an xml document
		Document doc;
		try {
			doc = WebJaxbAccessor.marshallToDocument(context, in);
			XMLDebugView.addDocument(doc, noun, true);
			
			lastInDocument = doc;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private static Document lastInDocument = null;
	private static Document lastOutDocument = null;
	
	public static void forceGenerateWSBug() {
		Exception e = new Exception("Operator forced bug report");
		BugReport report = new BugReport(e);

		if(lastOutDocument != null)
			report.addDocument("sent.xml", lastOutDocument);
		if(lastInDocument != null)
			report.addDocument("received.xml", lastInDocument);
		
		new BugReportDialog((Frame)null, report);
	}	

}
