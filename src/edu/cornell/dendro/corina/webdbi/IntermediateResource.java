package edu.cornell.dendro.corina.webdbi;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.tridas.Subsite;
import edu.cornell.dendro.corina.tridas.TridasElement;
import edu.cornell.dendro.corina.tridas.TridasEntityBase;
import edu.cornell.dendro.corina.tridas.TridasObject;
import edu.cornell.dendro.corina.tridas.TridasRadius;
import edu.cornell.dendro.corina.tridas.TridasSample;


public class IntermediateResource extends ResourceObject<List<? extends TridasEntityBase>> {
	/**
	 * Performs a search based on search parameters, returns a list of one type of object (specified by search!)
	 * 
	 * @param searchParameters
	 */
	public IntermediateResource(SearchParameters searchParameters) {
		super("intermediate", ResourceQueryType.SEARCH);
		setSearchParameters(searchParameters);
	}
	
	/**
	 * Performs a CREATE query
	 * parent is the parent object (e.g., if we're creating a subsite, a site is a parent object)
	 * child is the new object to be created
	 * 
	 * note that 'parent' is ignored for creating sites (it should be null)
	 * 
	 * @param parent
	 * @param child
	 * @param queryType
	 */
	public IntermediateResource(TridasEntityBase parent, 
			TridasEntityBase child)
	{
		super("intermediate", child.isNew() ? ResourceQueryType.CREATE : ResourceQueryType.UPDATE);

		// well that's a mouthful
		ArrayList<TridasEntityBase> kludgelist = 
			new ArrayList<TridasEntityBase>();
		
		// add our list!
		kludgelist.add(parent);
		kludgelist.add(child);
		setObject(kludgelist);
	}

	@Override
	protected Element prepareQuery(ResourceQueryType queryType, Element requestElement) throws ResourceException {
		if(queryType == ResourceQueryType.SEARCH) {
			return requestElement.addContent(getSearchParameters().getXMLElement());
		}
		else if(queryType == ResourceQueryType.CREATE) {
			TridasEntityBase parent = getObject().get(0);
			TridasEntityBase child = getObject().get(1);
			
			// child must be new, otherwise use an update!
			if(!child.isNew())
				throw new ResourceException("CREATE called for resource that already has an ID");
			
			// child must have a non-new parent (except for sites)
			if(!(child instanceof TridasObject) && (parent == null || parent.isNew()))
				throw new ResourceException("CREATE called with an invalid parent");
			
			Element parentElement;
			if(child instanceof TridasObject)
				parentElement = requestElement;
			else 
				// make a copy, otherwise we'll break it
				parentElement = (Element) parent.getResourceIdentifier().asRequestXMLElement();
			
			// get our XML...
			parentElement.addContent(child.toXML());
			
			// add it, if it's a new thingy
			if(!(child instanceof TridasObject))
				requestElement.addContent(parentElement);
			
			return requestElement;
		}
		else if(queryType == ResourceQueryType.UPDATE) {
			TridasEntityBase parent = getObject().get(0);
			TridasEntityBase child = getObject().get(1);
			
			// child must be old, otherwise use a create
			if(child.isNew())
				throw new ResourceException("UPDATE called for resource that doesn't have an ID");
			
			// child must have a non-new parent (except for sites)
			if(parent != null)
				throw new ResourceException("UPDATE called with a parent");
			
			requestElement.addContent(child.toXML());
			
			return requestElement;
		}
				
		throw new ResourceException("Unsupported query type given to IntermediateResource");
	}

	@Override
	protected boolean processQueryResult(Document doc) throws ResourceException {
		// Extract root and specimen elements from returned XML file
		Element root = doc.getRootElement();

		Element content = root.getChild("content");
		if(content == null)
			throw new MalformedDocumentException(doc, "No content element in measurement");

		switch(getQueryType()) {
		case READ:
		case CREATE:
		case UPDATE:
		case SEARCH: {
			List<TridasEntityBase> objList = new ArrayList<TridasEntityBase>();
			List<Element> base;
		
			base = content.getChildren("site");
			for(Element e : base) {
				TridasObject site = TridasObject.xmlToSite(e);
			
				if(site != null)
					objList.add(site);
			}

			base = content.getChildren("subSite");
			for(Element e : base) {
				Subsite subsite = Subsite.xmlToSubsite(e);
			
				if(subsite != null)
					objList.add(subsite);
			}

			base = content.getChildren("tree");
			for(Element e : base) {
				TridasElement tree = TridasElement.xmlToTree(e);
			
				if(tree != null)
					objList.add(tree);
			}

			base = content.getChildren("specimen");
			for(Element e : base) {
				TridasSample specimen = TridasSample.xmlToSpecimen(e);
			
				if(specimen != null)
					objList.add(specimen);
			}

			base = content.getChildren("radius");
			for(Element e : base) {
				TridasRadius radius = TridasRadius.xmlToRadius(e);
			
				if(radius != null)
					objList.add(radius);
			}

			setObject(objList);
			break;
		}
		
		default:
			break;
		}
		return true;
	}

}
