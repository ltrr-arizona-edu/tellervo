package edu.cornell.dendro.corina.sample;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class is intended to be used only for corinaweb:// urls
 * Use at your own risk with other things :)
 * 
 * @author lucasm
 *
 */

public class CorinaWebElement implements SampleLoader {
	private URI uri;
	
	public CorinaWebElement(String strUri) throws URISyntaxException {
		this.uri = new URI(strUri);
	}

	public String getName() {
		return uri.toString();
	}

	public String getShortName() {
		return uri.toString();
	}

	public Sample load() throws IOException {
		return null;
	}

	public BaseSample loadBasic() throws IOException {
		return null;
	}

}
