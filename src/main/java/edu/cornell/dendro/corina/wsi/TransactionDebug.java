/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.wsi;

import java.awt.Frame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.BugReportDialog;
import edu.cornell.dendro.corina.gui.XMLDebugView;
import edu.cornell.dendro.corina.util.BugReport;
import edu.cornell.dendro.corina.util.XMLDebug;
import edu.cornell.dendro.corina.wsi.corina.CorinaNamespacePrefixMapper;

public class TransactionDebug {
	private final static Logger log = LoggerFactory.getLogger(TransactionDebug.class);
	private static boolean isDebuggingEnabled = true;
	private static boolean isExtremelyVerboseDebuggingEnabled = false;
	
	public static void sent(Document doc, String noun) {
		XMLDebugView.addDocument(doc, noun, false);
	
		if(isExtremelyVerboseDebuggingEnabled) {
			log.trace("---SENT---");
			XMLDebug.dumpDocument(doc);
		}
		
		lastOutDocument = doc;
		lastInDocument = null;		
	}
	
	public static void sent(Object out, String noun, JAXBContext context) {
		if(!isDebuggingEnabled)
			return;

		// Marshall it to an xml document
		try {
			Document doc = WebJaxbAccessor.marshallToDocument(context, out, 
					new CorinaNamespacePrefixMapper());
			
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
			doc = WebJaxbAccessor.marshallToDocument(context, in,
					new CorinaNamespacePrefixMapper());
			XMLDebugView.addDocument(doc, noun, true);

			if(isExtremelyVerboseDebuggingEnabled) {
				log.trace("---RECV---");
				XMLDebug.dumpDocument(doc);
			}
			
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
