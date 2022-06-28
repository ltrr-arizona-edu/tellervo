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
package org.tellervo.desktop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLResponseHandler implements ResponseHandler<Document> {
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
		 * 		a) Try to parse
		 * 		b) Give up on failure
		 * 3) Try to parse
		 * 4) Throw error with the raw document (it's malformed XML)
		 */
		
		File tempFile = null;
		OutputStreamWriter fileOut = null;
		boolean usefulFile = false;	// preserve this file outside this local context?

		try {
			// read this into a temporary file if we can
			try {
				tempFile = File.createTempFile("tellervo", ".xml");
				fileOut = new OutputStreamWriter(new FileOutputStream(tempFile, false), charset);

			} catch (IOException ioe) {
				// well, that didn't work...
				try {
					// try building it in memory, then
					SAXBuilder builder = new SAXBuilder();

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
				SAXBuilder builder = new SAXBuilder();

				return builder.build(tempFile);

			} catch (JDOMException jdome) {
				// this document must be malformed
				usefulFile = true;
				throw new XMLParsingException(jdome, tempFile);
			} 
		} catch (IOException ioe) {
			// well, something there failed and it was lower level than just bad XML...
			if(tempFile != null) {
				usefulFile = true;
				throw new XMLParsingException(ioe, tempFile);
			}
			
			throw new XMLParsingException(ioe);
			
		} finally {
			// make sure we closed our file
			if(fileOut != null)
				fileOut.close();
			
			// make sure we delete it, too
			if(tempFile != null)
			{
				if (!usefulFile)
					tempFile.delete();
				else
					tempFile.deleteOnExit();
			}
		}
	}
      
}
