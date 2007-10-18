package edu.cornell.dendro.corina.webdbi;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.platform.Platform;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.XMLOutputter;

import java.net.*;
import javax.net.ssl.*;
import java.io.*;

/*
 * This class is for accessing XML documents from the web service.
 */

public class WebXMLDocumentAccessor {
	
	private URL url;
	/**
	 * Makes a class capable of performing operations on a webservice "noun" 
	 * 
	 * @param noun 
	 * What we are affecting here. For example, dictionary, sample, etc.
	 * @param verb
	 * What type of action we're making (create, read, update, delete)
	 */
	public WebXMLDocumentAccessor(String noun, String verb) {
		try {
			String path = App.prefs.getPref("corina.webservice.url");
			if(!path.endsWith("/"))
				path += "/";
			path += noun + ".php" + "?mode=" + verb;
			
			url = new URL(path);
			
			System.out.println("Access URL: " + url);
		} catch (MalformedURLException e) {
			new Bug(e);
		}
	}
		
	public Document query() throws IOException {
		return doRequest();
	}
	
	public void execute() throws IOException {
		doRequest();
	}
	
	private Document doRequest() throws IOException {
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		
		// Set any header fields
		http.setRequestProperty("User-Agent", "Corina WSI " + Build.VERSION);
		
		// if we're using an https connection, we're going to have to be careful
		if(http instanceof HttpsURLConnection) {
			HttpsURLConnection https = (HttpsURLConnection) http;
			
			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				https.setSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
				// don't do anything; we'll just ger errors later.
			}
		}

		int responseCode = http.getResponseCode();
		if(responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException("Unexpected response code " + responseCode + " while accessing " + url.toExternalForm());
		}
		
		InputStream in = http.getInputStream();
		
		try {
			SAXBuilder builder = new SAXBuilder();
			return builder.build(in);
		} catch (JDOMException jdoe) {
			throw new IOException("Could not parse document: JDOM error: " + jdoe);
		}
	}

	// This allows us to ignore the server certificate chain
	private static TrustManager[] trustAllCerts = new TrustManager[] {
		new X509TrustManager() {
        	public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
        	public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
        	public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
        }
	};

	public static void main(String args[]) {
		App.platform = new Platform();
		App.platform.init();
		App.prefs = new Prefs();
		App.prefs.init();
		
		try{ 
			Dictionary d = new Dictionary();
			d.loadWait();
			/*
			WebXMLDocumentAccessor a = new WebXMLDocumentAccessor("dictionaries", "read");
			Document doc = a.query();

			System.out.println("Result: ");
			XMLOutputter out = new XMLOutputter();
			out.output(doc, System.out);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
