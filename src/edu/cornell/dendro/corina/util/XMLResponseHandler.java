package edu.cornell.dendro.corina.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLResponseHandler implements ResponseHandler<Document>, EntityResolver {
   /**
     * Returns the response body as an XML document if the response was successful (a
     * 2xx status code). If no response body exists, this returns null. If the
     * response was unsuccessful (>= 300 status code), throws an
     * {@link HttpResponseException}.
     */
    public Document handleResponse(final HttpResponse response)
            throws HttpResponseException, IOException {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        
        HttpEntity entity = response.getEntity();
        return entity == null ? null : toDocument(entity, null);
    }
    
    public Document toDocument(final HttpEntity entity,
			final String defaultCharset) throws IOException, ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		InputStream instream = entity.getContent();
		if (instream == null) {
			return new Document();
		}

		String charset = EntityUtils.getContentCharSet(entity);
		if (charset == null) {
			charset = defaultCharset;
		}
		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}

		/**
		 * Methodology
		 * 1) Save XML to disk temporarily (it can be big, 
		 * 		we might want to look at it as a raw file)
		 * 2) If we can't write to disk:
		 * 		a) Try to parse using schemas
		 * 		b) Give up on failure
		 * 3) Try to parse with schemas
		 * 4) Try to parse without validation, throw error with document
		 * 5) Throw error with the raw document (it's malformed XML)
		 */
		
		File tempFile = null;
		FileWriter fileOut = null;
		boolean usefulFile = false;	// preserve this file outside this local context?

		try {
			// read this into a temporary file if we can
			try {
				tempFile = File.createTempFile("corina", "xmltmp");
				fileOut = new FileWriter(tempFile, false);

			} catch (IOException ioe) {
				// well, that didn't work...
				try {
					// try building it with validation
					SAXBuilder builder = getValidatingBuilder();

					return builder.build(new BufferedReader(
							new InputStreamReader(instream, charset)));

				} catch (JDOMException jdome) {
					// this document must be malformed
					throw new XMLParsingException(jdome);

				}
			}

			// ok, dump the webservice xml to a file
			BufferedReader webIn = new BufferedReader(new InputStreamReader(
					instream, charset));

			char indata[] = new char[1024];
			int inlen;

			// write the file out
			while ((inlen = webIn.read(indata)) >= 0) {
				fileOut.write(indata, 0, inlen);
			}
			
			fileOut.close();
			fileOut = null;

			try {
				// try building it with validation
				SAXBuilder builder = getValidatingBuilder();

				return builder.build(tempFile);

			} catch (JDOMException jdome) {
				try {
					// ok, it didn't validate and/or it's malformed
					// try building it without validation so we can print it out
					Document doc = new SAXBuilder().build(tempFile);

					throw new XMLParsingException(jdome, doc);

				} catch (JDOMException jdome2) {
					// this document must be malformed
					usefulFile = true;
					throw new XMLParsingException(jdome2, tempFile);
				}
			}
		} finally {
			// make sure we closed our file
			if(fileOut != null)
				fileOut.close();
			
			// make sure we delete it, too
			if (!usefulFile)
				tempFile.delete();
			else
				tempFile.deleteOnExit();
		}
	}
    
    private SAXBuilder getValidatingBuilder() {
    	SAXBuilder builder = new SAXBuilder(true);
    	
    	builder.setFeature("http://apache.org/xml/features/validation/schema", true);
        builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", 
        		"http://dendro.cornell.edu/schema/corina/1.0 corina.xsd " + 
        		"http://www.tridas.org/1.1 tridas.xsd " +
        		"http://www.opengis.net/gml gmlsf.xsd " +
        		"http://www.w3.org/1999/xlink xlinks.xsd");
    	builder.setEntityResolver(this);
    	
    	return builder;
    }
    
    public InputSource resolveEntity(String publicID, String systemID)
			throws IOException, SAXException {
    	
		if (systemID != null && systemID.endsWith(".xsd")) {
			String filename = systemID.substring(systemID.lastIndexOf('/') + 1);
			try {
				return new InputSource(findSchema(filename));
			} catch (Exception e) {
				System.out.println("Failed to load local schema: " + filename);
			}
		}
		
		return null; // default behavior: download the schema!
	}
    
    private InputStream findSchema(String filename) {
    	InputStream ret = XMLResponseHandler.class.getClassLoader().getResourceAsStream
    		("edu/cornell/dendro/corina_resources/Schemas/" + filename);
    	
    	if(ret == null)
    		throw new IllegalArgumentException();
    	
    	System.out.println("Loaded local schema: " + filename);
    	
    	return ret;
    }
}
