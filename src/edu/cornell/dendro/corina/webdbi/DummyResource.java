/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.Sample;

/**
 * @author lucasm
 *
 * This calls useless.php on the server :)
 */

public class DummyResource extends ResourceObject<Sample> {
	/**
	 * Constructor for getting information from the server
	 */
	public DummyResource() {
		super("useless", ResourceQueryType.READ);
	}

	/**
	 * Constructor for putting information on the server
	 * @param queryType
	 */
	public DummyResource(ResourceQueryType queryType) {
		super("useless", queryType);
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
		String id;
		
		if(s == null)
			throw new ResourceException("No object tied to Resource!");
		
		switch(queryType) {
		/*
		 * Generates:
		 * <corina>
		 *    <request type="[read|delete]" id="xxx">
		 *    </request>
		 * </corina>
		 */
		case READ:
		case DELETE:
			id = (String) s.getMeta("id");
			if(id == null)
				throw new ResourceException("Reading in a null id?");
			
			requestElement.setAttribute("id", id);
			break;

		/*
		 * Generates:
		 * <corina>
		 *    <request type="update" id="xxx">
		 *    </request>
		 * </corina>
		 */
		case UPDATE: // Update is CREATE with an ID
			id = (String) s.getMeta("id");
			if(id == null)
				throw new ResourceException("Reading in a null id?");
			
			requestElement.setAttribute("id", id);
			// drop through!
		case CREATE: {
			String title = (String) s.getMeta("title");
			
			if(title != null) {
				Element titleElem = new Element("title");
				
				titleElem.setText(title);
				requestElement.addContent(titleElem);
			}
			break;
		}
			
		}
		
		// important :)
		return requestElement;
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
		if(getQueryType() != ResourceQueryType.READ)
			return true;
			
		Sample s = new Sample();
		/*
		 * blah blah blah
		 * put stuff in to sample
		 */
		
		/*
		 * Oh no! Unrecoverable error?
		 * throw new ResourceException("something bad happened, notify queryFailed!");
		 */
		
		/*
		 * oh no! an error that I just don't want to continue with?
		 * return false
		 */

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
