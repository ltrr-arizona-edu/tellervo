package edu.cornell.dendro.corina.browser;

import java.net.URLConnection;
import java.net.*;

import java.io.*;
import java.io.IOException;

public class ItrdbURLConnection extends URLConnection {
    public ItrdbURLConnection(URL url) {
        super(url);
    }

    @Override
	public String getContentType() {
        return guessContentTypeFromName(url.getPath());
    }

    @Override
	public synchronized InputStream getInputStream() throws IOException {
        if (!connected)
            connect();

        mungURL();
        
        return realURL.openStream();
    }

    private URL realURL=null;
    private void mungURL() throws MalformedURLException {
        if (realURL == null)
            realURL = new URL("ftp://ftp.ngdc.noaa.gov/paleo/treering/" + url.toString().substring(8));
    }

    @Override
	public synchronized void connect() throws IOException {
        mungURL();

        realURL.openConnection();
        this.connected = true;
    }

    static {
        // what to do with him?
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
            public URLStreamHandler createURLStreamHandler(String protocol) {
                if (protocol.equalsIgnoreCase("itrdb"))
                    return new URLStreamHandler() {
                        @Override
						protected URLConnection openConnection(URL url) throws IOException {
                            return new ItrdbURLConnection(url);
                        }
                    };
                else
                    return null;
            }
        });
    }

    public static void main(String args[]) throws IOException {
        // user doesn't know what he's doing, but that's ok, i'm a weird program
        if (args.length==0) {
            System.out.println("usage: <progname> <uri>");
            System.out.println("  where <uri> looks like:");
            System.out.println("itrdb://chronologies/europe/turk001.crn");
            System.exit(0);
        }

        // let's try to load it
        URL u = new URL(args[0]);
        BufferedReader r = new BufferedReader(new InputStreamReader(u.openStream()));
        for (;;) {
            String l = r.readLine();
            if (l != null)
                System.out.println(l);
            else
                break;
        }
//        Sample s = new Sample(u);
//        System.out.println(s.toString());
    }
}
