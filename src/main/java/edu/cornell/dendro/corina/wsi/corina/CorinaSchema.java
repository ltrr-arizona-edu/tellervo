package edu.cornell.dendro.corina.wsi.corina;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class CorinaSchema {
	private final static Logger log = LoggerFactory.getLogger(CorinaSchema.class);

	
	private CorinaSchema() {
		// not instantiable
	}

	/**
	 * Returns a schema object representing all valid corina schemas
	 * @return
	 */
	public static Schema getCorinaSchema() {
		// be sure we load the schema in a threadsafe manner
		synchronized(schemaLoaded) {
			if(!schemaLoaded) {
				corinaSchema = loadCorinaSchema();
				schemaLoaded = true;
			}
		}
		
		return corinaSchema;
	}
	
	/** Is the schema loaded? */
	private static Boolean schemaLoaded = false;
	/** Our schema object */
	private static Schema corinaSchema;
	
	/**
	 * Loads the actual schema
	 * @return
	 */
	private static Schema loadCorinaSchema() {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			return factory.newSchema(getSchemaSources());
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get an array of schema sources
	 * @return
	 */
	private static Source[] getSchemaSources() {
		ArrayList<StreamSource> schemas = new ArrayList<StreamSource>();
		Source[] ret;
		
		for(String[] source : VALIDATE_SCHEMAS) {
			InputStream is = findSchema(source[1]);
			
			// couldn't load that schema :(
			if(is == null)
				continue;
			
			schemas.add(new StreamSource(is));
		}
		
		ret = new Source[schemas.size()];
		ret = schemas.toArray(ret);
		
		return ret;
	}
	
	/**
	 * Returns an InputStream pointing to this schema file
	 * @param filename
	 * @return
	 */
    private static InputStream findSchema(String filename) {
    	InputStream ret = CorinaSchema.class.getClassLoader().getResourceAsStream("schemas/"+filename);
    	//InputStream ret = CorinaSchema.class.getClassLoader().getResourceAsStream("edu/cornell/dendro/webservice/schemas/" + filename);
    	
    	if(ret == null)
    	{
    		log.error("Failed to load local schema: " + filename);
    	}
    	else
    	{
    		log.debug("Successfully loaded schema: " + filename);
    	}
    	
    	
    	return ret;
    }
    
    /**
     * A list of schema namespaces and their associated namespace files
     */
    
    private final static String VALIDATE_SCHEMAS[][] = {
    	// Order is important!
    	// namespace, filename, prefix
    	{ "http://www.w3.org/1999/xlink", "xlinks.xsd", "xlink" },
    	{ "http://www.opengis.net/gml", "gmlsf.xsd", "gml" },
    	{ "http://www.tridas.org/1.2.2", "tridas.xsd", "tridas" },
    	{ "http://dendro.cornell.edu/schema/corina/1.0", "corina.xsd", "c"},
    };
    
    /**
     * Acquires a list of schemas
     * 0: Schema namespace
     * 1: Schema filename
     * 2: Desired schema prefix
     * @return
     */
    public static String[][] getSchemas() {
    	return VALIDATE_SCHEMAS;
    }
}
