package edu.cornell.dendro.corina.webdbi;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.site.GenericIntermediateObject;
import edu.cornell.dendro.corina.site.Radius;
import edu.cornell.dendro.corina.site.Specimen;
import edu.cornell.dendro.corina.site.Tree;


public class IntermediateResource extends ResourceObject<List<? extends GenericIntermediateObject>> {
	public IntermediateResource(SearchParameters searchParameters) {
		super("intermediate", ResourceQueryType.SEARCH);
		setSearchParameters(searchParameters);
	}

	@Override
	protected Element prepareQuery(ResourceQueryType queryType, Element requestElement) throws ResourceException {
		if(queryType == ResourceQueryType.SEARCH) {
			return requestElement.addContent(getSearchParameters().getXMLElement());
		}
		
		throw new ResourceException("Unsupported query type given to IntermediateResource");
	}

	@Override
	protected boolean processQueryResult(Document doc) throws ResourceException {
		// Extract root and specimen elements from returned XML file
		Element root = doc.getRootElement();

		Element content = root.getChild("content");
		if(content == null)
			throw new MalformedDocumentException("No content element in measurement");

		switch(getQueryType()) {
		case READ:
		case SEARCH: {
			List<GenericIntermediateObject> objList = new ArrayList<GenericIntermediateObject>();
		
			List<Element> base = content.getChildren("tree");
			for(Element e : base) {
				Tree tree = Tree.xmlToTree(e);
			
				if(tree != null)
					objList.add(tree);
			}

			base = content.getChildren("specimen");
			for(Element e : base) {
				Specimen specimen = Specimen.xmlToSpecimen(e);
			
				if(specimen != null)
					objList.add(specimen);
			}

			base = content.getChildren("radius");
			for(Element e : base) {
				Radius radius = Radius.xmlToRadius(e);
			
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
