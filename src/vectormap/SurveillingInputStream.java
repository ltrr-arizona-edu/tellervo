//Copyright (c) 2005 Aaron Hamid.  All rights reserved.
//See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An inputstream which writes any read bytes to a specified outputstream.
 * This can be used to avoid a tedious one-time block copy of some other stream.  
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class SurveillingInputStream extends FilterInputStream {
  private OutputStream os;
  public SurveillingInputStream(InputStream in, OutputStream os) {
    super(in);
    this.os = os;
  }
  public int read() throws IOException {
    int i = super.read();
    if (i != -1) {
      os.write(i); 
    }
    return i;
  }
  public int read(byte[] b, int off, int len) throws IOException {
    int r = super.read(b, off, len);
    if (r > 0) {
      os.write(b, off, r);
    }
    return r;
  }
  
  public void close() throws IOException {
    super.close();
    os.close();
  }  
}