package corina.util;

import java.io.StringWriter;

// like stringwriter, but cut out \r's so it's just \n on every platform
public class PureStringWriter extends StringWriter {

    public PureStringWriter(int bufsize) {
        super(bufsize);
    }

    public void write(char[] cbuf, int off, int len) {        
        for (int i=off; i<off+len; i++) {
            char c = cbuf[i];
            if (c != '\r')
                super.write(c);
        }
    }

   /*
    // i could also implement these, if i wanted:
    public void write(int c) {
    }
    public void write(String str) {
    }
    public void write(String str, int off, int len) {
    }
    */
}
