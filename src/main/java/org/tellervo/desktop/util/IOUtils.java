package org.tellervo.desktop.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Various IO convenience routines
 *
 * @author Charles Johnson
 * @version 1.0
 */
public class IOUtils {
    // Various options for closing streams

    public static final int CLOSE_NEITHER  = 1;
    public static final int CLOSE_INPUT = 2;
    public static final int CLOSE_BOTH = 3;
    public static final int CLOSE_OUTPUT = 4;

    /**
     * Copy stderr and stdout of a Process to the console
     *
     * @param p The Process' streams we want to print
     */
    public static void outputProcessStreams(final Process p) {
        Thread runStdout = new Thread() {
            public void run() {
                IOUtils.copyStream(p.getInputStream(), System.out);
            }
        };

        runStdout.start();

        Thread runStderr = new Thread() {
            public void run() {
                IOUtils.copyStream(p.getErrorStream(), System.err);
            }
        };

        runStderr.start();
    }

    /**
     * Copy a stream
     *
     * @param in The source stream
     * @param out The target stream
     */
    public static void copyStream(InputStream in, OutputStream out) {
        final int DEFAULT_READ_BUFFER_SIZE = 1 << 10 << 3; // 8KiB

        try {
            byte[] buffer = new byte[DEFAULT_READ_BUFFER_SIZE];
            int bytesRead = -1;

            while ((bytesRead = in.read(buffer)) > -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                /* ignore */
            }

            try {
                out.close();
            } catch (IOException e) {
                /* ignore */
            }
        }
    }

    /**
     *
     * @param in The source stream
     * @param out The target stream
     * @param closeOptions Which stream(s) do we want to close? A bit mask
     */
    public static void copyStream(InputStream in, OutputStream out,
            int closeOptions) {
        final int DEFAULT_READ_BUFFER_SIZE = 1 << 10 << 3; // 8KiB

        try {
            byte[] buffer = new byte[DEFAULT_READ_BUFFER_SIZE];
            int bytesRead = -1;

            while ((bytesRead = in.read(buffer)) > -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ignore anything else if CLOSE_NEITHER is set
            if ((closeOptions & IOUtils.CLOSE_NEITHER) < 1) {
                try {
                    if (  ((closeOptions & IOUtils.CLOSE_INPUT) > 0) || 
                    	  ((closeOptions & IOUtils.CLOSE_BOTH) > 0)
                       ) {
                        in.close();
                    }
                } catch (IOException e) {
                    /* ignore */
                }

                try {
                    if ( ((closeOptions & IOUtils.CLOSE_OUTPUT) > 0) ||
                         ((closeOptions & IOUtils.CLOSE_BOTH) > 0)
                      ){
                        out.close();
                    }
                } catch (IOException e) {
                    /* ignore */
                }
            }
        }
    }
    
    public static File createTempDirectory()
    	    throws IOException
    	{
    	    final File temp;

    	    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

    	    if(!(temp.delete()))
    	    {
    	        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
    	    }

    	    if(!(temp.mkdir()))
    	    {
    	        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
    	    }

    	    return (temp);
    	}
}