/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.wsi.tellervo;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class TellervoSchema {
	private final static Logger log = LoggerFactory.getLogger(TellervoSchema.class);

	
	private TellervoSchema() {
		// not instantiable
	}

	/**
	 * Returns a schema object representing all valid tellervo schemas
	 * @return
	 */
	public static Schema getCorinaSchema() {
		// be sure we load the schema in a threadsafe manner
		synchronized(schemaLoaded) {
			if(!schemaLoaded) {
				tellervoSchema = loadCorinaSchema();
				schemaLoaded = true;
			}
		}
		
		return tellervoSchema;
	}
	
	/** Is the schema loaded? */
	private static Boolean schemaLoaded = false;
	/** Our schema object */
	private static Schema tellervoSchema;
	
	/**
	 * Loads the actual schema
	 * @return
	 */
	private static Schema loadCorinaSchema() {
		//SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

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
    	InputStream ret = TellervoSchema.class.getClassLoader().getResourceAsStream("schemas/"+filename);
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
    	{ "http://www.tellervo.org/schema/1.0", "tellervo.xsd", "c"},

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

