package edu.cornell.dendro.corina.util;

/*
 * $HeadURL: https://svn.apache.org/repos/asf/httpcomponents/httpclient/trunk/module-httpmime/src/main/java/org/apache/http/entity/mime/content/StringBody.java $
 * $Revision: 709485 $
 * $Date: 2008-10-31 18:16:08 +0100 (Fri, 31 Oct 2008) $
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;

public class UTF8StringBody extends AbstractContentBody {

    private final byte[] content;
    private final String filename;
    
    public UTF8StringBody(
            final String text, 
            final String mimeType, 
            final String filename) throws UnsupportedEncodingException {
        super(mimeType);
        if (text == null) {
            throw new IllegalArgumentException("Text may not be null");
        }
        this.content = text.getBytes("utf-8");
        this.filename = filename;
    }
    
    public UTF8StringBody(final String text, final String filename) throws UnsupportedEncodingException {
        this(text, "text/plain", filename);
    }
    
    public UTF8StringBody(final String text) throws UnsupportedEncodingException {
        this(text, "text/plain", null);
    }
    
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    public void writeTo(final OutputStream out, int mode) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream in = new ByteArrayInputStream(this.content);
        byte[] tmp = new byte[4096];
        int l;
        while ((l = in.read(tmp)) != -1) {
            out.write(tmp, 0, l);
        }
        out.flush();
    }

    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public String getCharset() {
        return "utf-8";
    }

    public long getContentLength() {
        return this.content.length;
    }
    
    public String getFilename() {
        return filename;
    }

	@Override
	public void writeTo(OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
