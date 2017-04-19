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
/**
 * 
 */
package org.tellervo.desktop.tridasv2.doc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * Generates properties file for TRiDaS documentation.  This is used 
 * 
 * @author lucasm
 * 
 */
public class DocBundleGenerator {
	public static void main(String args[]) {

		InputStream is = DocBundleGenerator.class.getClassLoader().getResourceAsStream("schemas/tridas.xsd");
		Document doc;
		SAXBuilder builder = new SAXBuilder();
		try {
			doc = builder.build(is);
		} catch (JDOMException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		List<?> docs;
		try {
			docs = XPath.selectNodes(doc.getRootElement(), "//xs:documentation");
		} catch (JDOMException e) {
			e.printStackTrace();
			return;
		}
		
		writers = new HashMap<String,PrintWriter>();
		
		for(Object o : docs) {
			if(!(o instanceof Element))
				continue;
			
			Element e = (Element) o;
						
			String tree = buildTreeFor(e);
			String lang = e.getAttributeValue("lang", Namespace.XML_NAMESPACE);
			
			lang = (lang == null) ? "en" : lang.toLowerCase();
			
			try {
				PrintWriter w = getWriterForLang(lang);
				
				w.println(tree + " = " + e.getTextNormalize());
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return;
			}
		}
		writeHardcodedDocumentation();
		
		
		closeWriters();
		System.out.println("XML Documentation bundle created successfully!");
	}
	
	
	private static void writeHardcodedDocumentation()
	{
		try {
			PrintWriter w = getWriterForLang("en");
			
			w.println("externalId = Identifier for this sample in an external database or repository such as Park Service or Archaeological Service databases");
			w.println("curationStatus = Status of this sample in the collection.  For instance whether this sample is archived, on loan or currently used in active research");
			w.println("sampleStatus = Stage of preparation this sample is currently at");
			w.println("files = References to files, webpages or URNs containing associated information");
			w.println("objectCode = Short code used to identify the site.  Traditionally this is three characters long, but there is no reason it can't be longer");
			w.println("presence = Pick list of options to indicate whether this feature is present or not");
			w.println("certainty = Information regarding how precise this date is");
			w.println("suffix = Whether the year is AD, BC or BP");
			w.println("vegetationType = Only applicable when object is associated with live trees.  Records the type of vegetation at a site");
			w.println("sample.userDefinedField.icms.catalogcode = Catalog code used by the Re:discovery ICMS software to identify a sample.");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static Map<String, PrintWriter> writers;
	
	private static PrintWriter getWriterForLang(String lang) throws IOException {	
		PrintWriter writer = writers.get(lang);
		
		if(writer != null)
			return writer;
		
		String fn = "src/main/java/org/tellervo/desktop/tridasv2/doc/" +  
			"DocsBundle" + (lang.equals("en") ? "" : ("_" + lang)) + ".properties";
		writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fn), "UTF-8"));

		writers.put(lang, writer);
		
		return writer;
	}
	
	private static void closeWriters() {
		for(Writer w : writers.values()) {
			try {
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		writers.clear();
	}
	
	public static String buildTreeFor(Element e) {
		List<String> hier = new ArrayList<String>();
		
		while((e = e.getParentElement()) != null) {
			if(e.getAttribute("name") != null && 
					(e.getName().equals("simpleType") || e.getName().equals("complexType")))
				hier.add(e.getAttributeValue("name"));
			else if((e.getName().equals("element") || e.getName().equals("group"))
					&& e.getAttribute("name") != null) 
				hier.add(e.getAttributeValue("name"));
			else if(e.getName().equals("attribute") && e.getAttribute("name") != null) 
				hier.add(e.getAttributeValue("name"));
		
		}
		
		Collections.reverse(hier);
		
		StringBuffer sb = new StringBuffer();
		for(String s : hier) {
			if(sb.length() > 0)
				sb.append('.');
			sb.append(s);
		}
		
		return sb.toString();
	}
}
