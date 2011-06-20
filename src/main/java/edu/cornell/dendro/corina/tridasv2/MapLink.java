/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.net.URI;
import java.net.URISyntaxException;

import org.tridas.interfaces.ITridasGeneric;
import org.tridas.schema.TridasGenericField;

/**
 * Stupid wrapper class for Map Link
 * 
 * Why do we need this? Might be more convenient if we ever have different types of maps?
 * 
 * @author Lucas Madar
 */
public class MapLink {
	private URI mapURI;
	
	public MapLink(ITridasGeneric generic) {
		TridasGenericField field = GenericFieldUtils.findField(generic, "corina.mapLink");

		// no URI? no link!
		if(field == null) {
			mapURI = null;
			return;
		}

		// get the URI
		try {
			mapURI = new URI(field.getValue());
		} catch (URISyntaxException e) {
			System.err.println("Invalid map URI: " + field.getValue());
			mapURI = null;
		}
	}
	
	public URI getURI() {
		return mapURI;
	}
	
	public boolean hasLink() {
		return mapURI != null;
	}
}
