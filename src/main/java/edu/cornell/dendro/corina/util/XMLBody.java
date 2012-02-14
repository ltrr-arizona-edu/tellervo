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
package edu.cornell.dendro.corina.util;

/**
 * XML body content for MIME multipart POST
 */

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLBody extends AbstractContentBody {

    private final Document in;
    private final String filename;
    private boolean pretty = false;
    
    public XMLBody(final Document in, final String mimeType, final String filename) {
        super(mimeType);
        if (in == null) {
            throw new IllegalArgumentException("XML Document may not be null");
        }
        this.in = in;
        this.filename = filename;
    }
    
    public XMLBody(final Document in, final String filename) {
        this(in, "application/xml", filename);
    }
    
    /**
     * Format this XML as pretty or not
     * Returns self, so you can do:
     *    new XMLBody(in).setPretty(true)...
     * @param pretty
     * @return
     */
    public XMLBody setPretty(boolean pretty) {
    	this.pretty = pretty;
    	
    	return this;
    }
    


    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public String getCharset() {
        return null;
    }

    public long getContentLength() {
        return -1;
    }
    
    public String getFilename() {
        return this.filename;
    }

	@Override
	public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
       	XMLOutputter outp = new XMLOutputter();
       	outp.setFormat(pretty ? Format.getPrettyFormat() : Format.getCompactFormat());
        	
       	outp.output(in, out);        	
        out.flush();
		
	}
    
}
