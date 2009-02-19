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
    
    public void writeTo(final OutputStream out, int mode) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
       	XMLOutputter outp = new XMLOutputter();
       	outp.setFormat(Format.getCompactFormat());
        	
       	outp.output(in, out);        	
        out.flush();
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
    
}
