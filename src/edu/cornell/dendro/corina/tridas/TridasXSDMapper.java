package edu.cornell.dendro.corina.tridas;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;


public class TridasXSDMapper {
	private static final Namespace xns = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema");
	private static Document doc;
	
	private static void setType(String path, boolean mandatory, boolean isList, Class type) {
		System.out.println("> " + path);
	}
	
	private static String getElementName(Element e) {
		return e.getAttributeValue("name");
	}
	
	private static Element findElementNamed(String name) {
		List<Element> el = doc.getRootElement().getChildren("element", xns);
		
		for(Element e : el) {
			if(e.getAttributeValue("name").equals(name))
				return e;
		}
		
		return null;
	}
	
	private static Object[] findBaseType(Element ele, String prefix) {
		String val;
		
		// no name? hope this is just a reference!
		if(getElementName(ele) == null) {
			val = ele.getAttributeValue("ref");
			
			if(val == null)
				throw new IllegalStateException("wtf");

			prefix += "." + val;
			
			return findBaseType(findElementNamed(val), prefix);
		}

		// ok, we found a type
		val = ele.getAttributeValue("type");
		if(val != null) {
			return new Object[] { prefix, val };
		}
		
		val = ele.getAttributeValue("ref");
		if(val != null) {
			// ok, we reference an external type. nice.
			if(val.indexOf(':') != -1) {
				return new Object[] { prefix, val };
			}
		
			// recurse down the reference tree...
			if(prefix == null)
				prefix = getElementName(ele);
			else
				prefix += "." + getElementName(ele);
			
			return findBaseType(findElementNamed(val), prefix);			
		}
		
		// if we get here, we probably have a complex type
		Element e;
		
		e = ele.getChild("complexType", xns);
		if(e != null) {
			return new Object[] { prefix, getElementName(ele) };
		}
		
		e = ele.getChild("simpleContent", xns);
		if(e != null) {
			return new Object[] { prefix, getElementName(ele) };
		}
		
		System.out.println("wtf! " + prefix);
		return null;
	}
	
	private static void goMine(Element root) {
		List<Element> el;

		String path = getElementName(root);
		Element seq = root.getChild("complexType", xns).getChild("sequence", xns);
		
		el = seq.getChildren();
		for(Element e : el) {
			if(e.getName().equals("element")) {
				String name = e.getAttributeValue("ref");
				if(name.equals("title") || name.equals("identifier") || name.equals("linkSeries"))
					continue;
			
				Object[] v = findBaseType(e, path);
				if(v == null)
					continue;
				
				if(ignorePaths.contains(v[0]))
					continue;
				
				String mins = e.getAttributeValue("minOccurs");
				String maxs = e.getAttributeValue("maxOccurs");
				Integer min, max;
				
				try {
					min = (mins == null) ? null : Integer.valueOf(mins);
					if(maxs != null && maxs.equals("unbounded"))
						max = Integer.MAX_VALUE;
					else
						max = (maxs == null) ? null : Integer.valueOf(maxs);
				} catch (NumberFormatException nfe) {
					throw new IllegalStateException("oh noes");
				}
				
				boolean isMandatory = (min == null) ? false : (min > 0);
				boolean isList = (max == null) ? false : (max > 1);
				Class type = typeMappings.containsKey(v[1]) ? typeMappings.get(v[1]) : Object.class;
				
				System.out.println(v[0] + ": " + v[1] + "{" + type.getName()
						+"}" + 
						(isMandatory ? " mandatory" : "") +
						(isList ? " list" : "")
						);
			}
		}
	}
	
	private static void go() {
		goMine(findElementNamed("element"));
		goMine(findElementNamed("sample"));
		goMine(findElementNamed("radius"));
	}
	
	public static void main(String[] args) {
		URL url = TridasXSDMapper.class.getClassLoader().getResource("edu/cornell/dendro/webservice/schemas/tridas.xsd");
		SAXBuilder builder = new SAXBuilder();
		try {
			doc = builder.build(url);
			go();
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	private static HashSet<String> ignorePaths = new HashSet<String>(Arrays.asList(new String[] { 
			"element.sample",
			"sample.radius",
			"radius.measurementSeries"
	}));
	
	private static HashMap<String, Class> typeMappings = new HashMap<String, Class>();
	static {
		typeMappings.put("xs:dateTime", Date.class);
		typeMappings.put("xs:decimal", Double.class);
		typeMappings.put("xs:string", String.class);
		typeMappings.put("xs:boolean", Boolean.class);
	}
	
}
