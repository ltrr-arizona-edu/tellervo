/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * This class URLencodes an output stream
 * 
 * @author Lucas Madar
 *
 */
public class URLEncodingOutputStream extends FilterOutputStream {
	public URLEncodingOutputStream(OutputStream out) {
		super(out);
	}

	@Override
	public void write(int b) throws IOException {
		byte[] bytes = new byte[] { (byte) b };
		
		write(bytes);
	}
	
	@Override
	public void write(byte[] bytes) throws IOException {
		String output = URLEncoder.encode(new String(bytes), "UTF-8");
		
		out.write(output.getBytes());		
	}

}
