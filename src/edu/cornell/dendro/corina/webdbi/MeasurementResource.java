/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import org.jdom.*;

import java.io.IOException;
import java.util.*;

import edu.cornell.dendro.corina.formats.CorinaXML;
import edu.cornell.dendro.corina.index.Index;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;

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
		ResourceIdentifier ri = this.getIdentifier();
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
		case DELETE:
			if(ri == null)
				throw new ResourceException("No identifier specified for read/delete");
			
			requestElement.addContent(ri.asRequestXMLElement());
			
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
		
		switch(sType) {
		case DIRECT: {
			ResourceIdentifier ri = (ResourceIdentifier) s.getMeta("::dbparent");
			Element parentElement = (Element) ri.asRequestXMLElement();
		
			parentElement.addContent(new CorinaXML().saveToElement(s));
			requestElement.addContent(parentElement);
			
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
		
		default:
			throw new ResourceException("Create for SampleType of " + s.getSampleType() + " not supported");
		
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
		// Extract root and specimen elements from returned XML file
		Element root = doc.getRootElement();
		
		switch(getQueryType()) {
		case READ:
		case UPDATE:
		case CREATE:
			return loadSample(root);
			
		case DELETE:
			return true;
			
		default:
			throw new ResourceException("I don't handle type " + getQueryType() + " yet :(");
		}
	}
	
	// do the actual loading of the sample. hmm...
	private boolean loadSample(Element root) throws ResourceException {
		// Create new sample to hold data
		Sample s = new Sample();
		
		Element content = root.getChild("content");
		if(content == null)
			throw new MalformedDocumentException("No content element in measurement");
		
		Element measurementElement = content.getChild("measurement");
		if(measurementElement == null)
			throw new MalformedDocumentException("No measurement element in measurement");
		
		CorinaXML loader = new CorinaXML();
		
		try {
			loader.loadMeasurement(s, measurementElement);
		} catch (IOException ioe) {
			throw new MalformedDocumentException("Poorly formed measurement: " + ioe);
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