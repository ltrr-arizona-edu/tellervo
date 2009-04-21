/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import org.jdom.*;

import java.io.IOException;
import java.util.*;

import edu.cornell.dendro.corina.formats.CorinaXML;
import edu.cornell.dendro.corina.formats.Tridas;
import edu.cornell.dendro.corina.index.Index;
import edu.cornell.dendro.corina.sample.CorinaWebElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridas.Subsite;
import edu.cornell.dendro.corina.tridas.TridasElement;
import edu.cornell.dendro.corina.tridas.TridasIdentifier;
import edu.cornell.dendro.corina.tridas.TridasObject;
import edu.cornell.dendro.corina.tridas.TridasRadius;
import edu.cornell.dendro.corina.tridas.TridasSample;

/**
 * @author lucasm
 *
 * The web interface for loading Samples... woot
 */

public class MeasurementResource extends ResourceObject<Sample> {
	/**
	 * Constructor for getting information from the server
	 */
	public MeasurementResource() {
		super("measurements", ResourceQueryType.READ);
	}

	/**
	 * Constructor for putting information on the server
	 * @param queryType
	 */
	public MeasurementResource(ResourceQueryType queryType) {
		super("measurements", queryType);
	}

	/**
	 * Prepare the query document, which is in the format below.
	 * The element passed is "request." 
	 * This is returned so the method can be chained.
	 * 
	 * <corina>
	 *    <request type="action">
	 *    </request>
	 * </corina>
	 * 
	 * @param requestElement 
	 */
	@Override
	protected Element prepareQuery(ResourceQueryType queryType, Element requestElement) throws ResourceException {
		Sample s = this.getObject();
		TridasIdentifier ri = this.getIdentifier();
		String id;
				
		switch(queryType) {
		/*
		 * Generates:
		 * <corina>
		 *    <request type="[read|delete]">
		 *       <measurement id="123" />
		 *    </request>
		 * </corina>
		 */
		case READ:
			// if we're reading, ask for a standard, but not too comprehensive format
			requestElement.setAttribute("format", "standard");
			
		case DELETE:
			if(ri == null)
				throw new ResourceException("No identifier specified for read/delete");
			
			requestElement.addContent(ri.toCorinaEntityTag());
			
			break;

		/*
		 * Update case just dumps the measurement out 
		 * We presume that 
		 */
		case UPDATE: // Update is CREATE with an ID
			if(s == null)
				throw new ResourceException("Can't update without a sample");
			
			id = (String) s.getMeta("::dbid");
			if(id == null)
				throw new ResourceException("Update sample has a null id?");

			// we always want to get a comprehensive format back.
			requestElement.setAttribute("format", "comprehensive");
			
			requestElement.addContent(new CorinaXML().saveToElement(s));
			break;
			
		case CREATE: {
			prepareCreateQuery(s, requestElement);
			break;
		}
			
		}
		
		// important :)
		return requestElement;
	}
	
	/**
	 * A separate routine to create new samples, since this logic is complex and convoluted...
	 * @param s
	 * @param requestElement
	 */
	private void prepareCreateQuery(Sample s, Element requestElement) throws ResourceException {
		SampleType sType = (SampleType) s.getMeta("::saveoperation");
		
		// no sample type specified, so assume direct
		if(sType == null)
			sType = SampleType.DIRECT;
		
		// we always want to get a comprehensive format back.
		requestElement.setAttribute("format", "comprehensive");
		
		switch(sType) {
		case DIRECT: {
			ResourceIdentifier ri = (ResourceIdentifier) s.getMeta("::dbparent");
			Element parentElement = (Element) ri.asRequestXMLElement();
		
			parentElement.addContent(new CorinaXML().saveToElement(s));
			requestElement.addContent(parentElement);
			
			return;
		}
		
		case SUM: {
			/*
			 * Create this: 
			 * <measurement> 
			 * 	<metadata> 
			 * 		<name>Sum of something</name> 
			 * 		<operation>sum</operation
			 * 	</metadata> 
			 * 	<references> 
			 * 		<measurement id="1" /> 
			 * 		<measurement id="..." /> 
			 * 	</references>
			 * </measurement>
			 * 
			 */
			Element measurementElement = new Element("measurement");
			Element metadataElement = new Element("metadata");
			Element referencesElement = new Element("references");

		
			metadataElement.addContent(new Element("name").setText((String) s.getMeta("name")));
			metadataElement.addContent(new Element("operation").setText("sum"));
			
			// now, add each child element in the list...
			for(edu.cornell.dendro.corina.sample.Element child : s.getElements()) {
				if(child.getLoader() instanceof CorinaWebElement) {
					CorinaWebElement cwe = (CorinaWebElement) child.getLoader();
					
					referencesElement.addContent(cwe.getResourceIdentifier().asRequestXMLElement());
				}
				else
					throw new ResourceException("Non-corina element in sum -- cannot deal");
			}

			measurementElement.addContent(metadataElement);
			measurementElement.addContent(referencesElement);
			requestElement.addContent(measurementElement);
			return;
		}
		
		case INDEX: {
			ResourceIdentifier ri = (ResourceIdentifier) s.getMeta("::dbparent");
			Index index = (Index) s.getMeta("::indexclass");
			
			if(ri == null)
				throw new ResourceException("Cannot index a Sample that hasn't been saved!");

			/*
			 * Create this: <measurement> <metadata> <name>Index of
			 * something</name> <operation parameter="linear">index</operation
			 * </metadata> <references> <measurement id="1" /> </references>
			 * </measurement>
			 * 
			 */
			
			Element measurementElement = new Element("measurement");
			Element metadataElement = new Element("metadata");
			Element operationElement = new Element("operation");
			Element referencesElement = new Element("references");

			// <operation parameter="xxxx">index</operation>
			operationElement.setAttribute("parameter", index.getIndexFunction().getDatabaseRepresentation());
			operationElement.setText("index");
			
			metadataElement.addContent(operationElement);
			// add <name>xxx</name>
			metadataElement.addContent(new Element("name").setText((String) s.getMeta("name")));
			referencesElement.addContent(ri.asRequestXMLElement());
			
			measurementElement.addContent(metadataElement);
			measurementElement.addContent(referencesElement);
			requestElement.addContent(measurementElement);
			return;
		}

		case CROSSDATE: {
			ResourceIdentifier rip = (ResourceIdentifier) s.getMeta("::dbparent");
			ResourceIdentifier rim = (ResourceIdentifier) s.getMeta("::crossdatemaster");
			
			if(rip == null || rim == null)
				throw new ResourceException("Cannot crossdate -- internal error!");

			/*
			 * Create this: 
			 * <measurement> 
			 *  <metadata> 
			 *   <name>My redated measurement</name> 
			 *   <operation>crossdate</operation>
			 *   <crossdate>
			 *     <basedOn><measurement id="1"/></basedOn>
			 *     <startYear>1950</startYear> <certaintyLevel>5</certaintyLevel>
			 *     <justification>T-score 6.2</justification> 
			 *   </crossdate>
			 *  </metadata>
			 *  <references>
			 *   <measurement id="1" /> 
			 *  </references>
			 * </measurement>
			 */
			
			Element measurementElement = new Element("measurement");
			Element metadataElement = new Element("metadata");
			Element operationElement = new Element("operation");
			Element referencesElement = new Element("references");
			Element crossdateElement = new Element("crossdate");

			operationElement.setText("crossdate");
			
			// crossdating tag...
			crossdateElement.addContent(new Element("basedOn").
					addContent(rim.asRequestXMLElement()));
			crossdateElement.addContent(new Element("startYear").
					setText(s.getRange().getStart().toString()));
			crossdateElement.addContent(new Element("justification").
					setText(s.getMeta("::crossdatejustification").toString()));
			crossdateElement.addContent(new Element("certaintyLevel").
					setText(s.getMeta("::crossdatecertainty").toString()));
			
			metadataElement.addContent(new Element("name").setText((String) s.getMeta("name")));
			metadataElement.addContent(operationElement);
			metadataElement.addContent(crossdateElement);
			
			referencesElement.addContent(rip.asRequestXMLElement());
			
			measurementElement.addContent(metadataElement);
			measurementElement.addContent(referencesElement);
			requestElement.addContent(measurementElement);
			return;
		}

		
		default:
			throw new ResourceException("Create for SampleType of " + sType + " not supported");
		
		}
	}
	
	/**
	 * In this function, parse the document and populate any internal variables.
	 * 
	 * WARNING: This function *must* be threadsafe. 
	 * This means it must not change any class variables without synchronizing against them!
	 * 
	 * Any UI responses should use a ResourceListener. NO DIALOGS HERE. :)
	 * 
	 * If this function returns false, queryFailed is not called
	 * If it throws an exception, queryFailed IS called
	 * 
	 * @param doc The XML JDOM document obtained by the query function
	 * @return true on success, false on failure
	 * @throws ResourceException if error parsing
	 */
	@Override
	protected boolean processQueryResult(Document doc) throws ResourceException {
		
		switch(getQueryType()) {
		case READ:
		case UPDATE:
		case CREATE:
			return loadSample(doc);
			
		case DELETE:
			return true;
			
		default:
			throw new ResourceException("I don't handle type " + getQueryType() + " yet :(");
		}
	}
	
	// do the actual loading of the sample. hmm...
	private boolean loadSample(Document doc) throws ResourceException {
		// Extract root and specimen elements from returned XML file
		Element root = doc.getRootElement();
		
		// Create new sample to hold data
		Sample s = new Sample();
		
		Element content = root.getChild("content", edu.cornell.dendro.corina.webdbi.CorinaXML.CORINA_NS);
		if(content == null)
			throw new MalformedDocumentException(doc, "No content element in measurement");

		/*
		// try the easy way first
		Element measurementElement = content.getChild("measurement");
		if(measurementElement == null) {
			// ok, maybe we have a 'comprehensive' format for a raw sample?
			Element siteElement = content.getChild("site");

			if(siteElement == null)
				throw new MalformedDocumentException(doc, "Measurement format invalid or unknown (not comprehensive or standard)");
			
			// kludgy way of doing this, maybe, but I think it's cleaner than a million if(x==null) statements
			try {
				Element subSiteElement = siteElement.getChild("subSite");
				Element treeElement = subSiteElement.getChild("tree");
				Element specimenElement = treeElement.getChild("specimen");
				Element radiusElement = specimenElement.getChild("radius");
				measurementElement = radiusElement.getChild("measurement");
				
				// kludge to catch a null measurementElement :)
				measurementElement.getChild("metadata");
				
				s.setMeta("::radius", TridasRadius.xmlToRadius(radiusElement));
				s.setMeta("::specimen", TridasSample.xmlToSpecimen(specimenElement));
				s.setMeta("::tree", TridasElement.xmlToTree(treeElement));
				s.setMeta("::subsite", Subsite.xmlToSubsite(subSiteElement));
				s.setMeta("::site", TridasObject.xmlToSite(siteElement));
			} catch (NullPointerException npe) {
				throw new MalformedDocumentException(doc, "Invalid comprehensive measurement format");				
			}
		}
		*/
		
		Tridas loader = new Tridas();
		
		try {
			loader.loadSeries(s, content);
		} catch (IOException ioe) {
			throw new MalformedDocumentException(doc, "Poorly formed measurement: " + ioe);
		}
		
		// Synchronise object
		setObject(s);
		return true;
	}

	/**
	 * Note this is called in the context of the parsing thread
	 * NO dialogs or anything here, just internal stuff. UI responses should
	 * use a ResourceListener
	 */
	@Override
	protected void queryFailed(Exception e) {
		super.queryFailed(e);
	}
}