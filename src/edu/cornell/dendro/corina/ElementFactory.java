package edu.cornell.dendro.corina;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;

public class ElementFactory {
	/**
	 * @param filename 
	 * @return
	 */
	public static Element createElement(String filename, Class<? extends Element> clazz) {
		System.out.println("New element, filename: " + filename);
		
		// is this a URL? (e.g. xxxx://xxxxx?)
		if(filename.matches("^\\w+://\\S+$")) {
			URI uri;
			
			try {
				uri = new URI(filename);
			} catch (URISyntaxException use) {
				throw new UnsupportedOperationException(use);
			}
			
			// getScheme() has to return non-null, 
			// since we don't go here if no scheme was specified (see above regex)
			
			// a file?
			if(uri.getScheme().equals("file")) {
				File file = new File(uri);
				
				return spawnElement(clazz, new FileElement(file.getPath()));
			}
			
			throw new UnsupportedOperationException("No support for loading URL schema " + uri.toString());
			// a webpage?
			//if(uri.getScheme().equals("http")) {	
			//}
		}

		// No, just a file then.
		return spawnElement(clazz, new FileElement(filename));
	}
	
	public static Element createElement(String filename) {
		return createElement(filename, Element.class);
	}
	
	private static Element spawnElement(Class<? extends Element> clazz, SampleLoader loader) {
		try {
			Constructor<? extends Element> c = clazz.getConstructor(new Class[] { String.class });
			return c.newInstance(new Object[] { loader });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
