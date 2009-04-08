package edu.cornell.dendro.corina.webdbi;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.BugReportDialog;
import edu.cornell.dendro.corina.gui.XMLDebugView;
import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.util.BugReport;
import edu.cornell.dendro.corina.util.PureStringWriter;
import edu.cornell.dendro.corina.util.XMLBody;
import edu.cornell.dendro.corina.util.XMLParsingException;
import edu.cornell.dendro.corina.util.XMLResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.VersionInfo;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import java.awt.Frame;
import java.io.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
 * This class is for accessing XML documents from the web service.
 */

public class WebXMLDocumentAccessor {	
	/** The URL we plan to access */ 
	private URI url;
	
	/** The type of request we plan to use (POST or GET) */
	private int requestMethod;
	
	/** The XML document we plan to post to the server */
	private Document requestDocument;
	
	/**
	 * Makes a class capable of performing operations on a webservice "noun" 
	 * This is for original-style requests that solely use HTTP GET
	 * 
	 * For POST queries, use the constructor that only requires a noun
	 * 
	 * @deprecated use POST queries instead
	 * 
	 * @param noun 
	 * What we are affecting here. For example, dictionary, sample, etc.
	 * @param verb
	 * What type of action we're making (create, read, update, delete)
	 */
	@Deprecated
	public WebXMLDocumentAccessor(String noun, String verb) {
		requestMethod = METHOD_GET;
		this.noun = noun;
		
		try {
			String path = App.prefs.getPref("corina.webservice.url", "invalid-url!");
			if(!path.endsWith("/"))
				path += "/";
			path += noun + ".php" + "?mode=" + verb;

			url = new URI(path);			
		} catch (URISyntaxException e) {
			new Bug(e);
		}
	}

	private static String noun;
	
	/**
 	 * Makes a class capable of performing operations on a webservice "noun" 
	 * 
	 * Note that this constructor will be useless for queries that do not contain
	 * a POST request.
	 * 
	 * @param noun What we are affecting here. For example, dictionary, sample, etc.
	 */
	public WebXMLDocumentAccessor(String noun) {
		requestMethod = METHOD_POST;
		this.noun = noun;
		
		try {
			String path = App.prefs.getPref("corina.webservice.url", "invalid-url!");
			/*
			 * Not anymore...
			 * 
			if(!path.endsWith("/"))
				path += "/";
			path += noun + ".php";
			*/
			
			url = new URI(path);
		} catch (URISyntaxException e) {
			new Bug(e);
		}		
	}
		
	/**
	 * The important part of this: query the server. This is a blocking call.
	 * 
	 * @return A DOM document
	 * @throws IOException
	 */
	public Document query() throws IOException {
		return doRequest();
	}
	
	public void execute() throws IOException {
		doRequest();
	}

	/**
	 * Quick and dirty debug: gets a stack trace
	 * @return
	 */
	private String getStackTrace() {
		PureStringWriter sw = new PureStringWriter();
		PrintWriter pw = new PrintWriter(sw);
		new Throwable().printStackTrace(pw);
		return sw.toString();
	}
	
	private Document doRequest() throws IOException {
		//HttpURLConnection http = (HttpURLConnection) url.openConnection();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest req;
		Document inDocument = null;

		lastInDocument = null;
		lastOutDocument = requestDocument;
		
		try {
			if(requestMethod == METHOD_POST) {
				if(this.requestDocument == null)
					throw new NullPointerException("requestDocument is null yet required for this type of query");

				// Create a new POST request
				HttpPost post = new HttpPost(url);
				// Make it a multipart post
				MultipartEntity postEntity = new MultipartEntity();
				req = post;
							
				XMLBody xmlb = new XMLBody(requestDocument, "application/corina+xml", null);				
				postEntity.addPart("xmlrequest", xmlb);
				postEntity.addPart("traceback", new StringBody(getStackTrace()));
				post.setEntity(postEntity);
				
				// debug
				XMLDebugView.addDocument(requestDocument, noun, false);
				/*
				System.out.println("SENDING XML: ");
				XMLOutputter outp = new XMLOutputter();
				outp.setFormat(Format.getPrettyFormat());
				outp.output(requestDocument, System.out);
				*/
			} else {
				// well, that's nice and easy
				req = new HttpGet(url);
			}
			
			client.setCookieStore(WSCookieStoreHandler.getCookieStore().toCookieStore());
			
			req.setHeader("User-Agent", "Corina WSI " + Build.VERSION + 
					" (" + clientModuleVersion + "; ts " + Build.TIMESTAMP +")");

			// are we using https? should we allow self-signed certs?
			if(url.getScheme().equals("https")) {
				try {
					// make a new SSL context
					SSLContext sc = SSLContext.getInstance("SSL");
					sc.init(null, trustAllCerts, new java.security.SecureRandom());
					
					// make a new socket factory
					SSLSocketFactory socketFactory = new SSLSocketFactory(sc);
					
					// register the scheme with the connection
					Scheme scheme = new Scheme("https", socketFactory, 443);
					client.getConnectionManager().getSchemeRegistry().register(scheme);
				} catch (Exception e) {
					// don't do anything; we'll just get errors later.
				}
			}
			
			XMLResponseHandler responseHandler = new XMLResponseHandler();
			inDocument = client.execute(req, responseHandler);
			
			lastInDocument = inDocument;

			XMLDebugView.addDocument(inDocument, noun, true);
			
			// save our cookies?
			WSCookieStoreHandler.getCookieStore().fromCookieStore(client.getCookieStore());
			
			// ok, now inspect the document we got back
			CorinaDocumentInspector inspector = new CorinaDocumentInspector(inDocument);

			// Verify our document based on schema validity
			inspector.validate();
			
			// Verify our document structure, throw any exceptions!
			inspector.verifyDocument();
			
			return inDocument;				

		} catch (HttpResponseException hre) {
			BugReport bugs = new BugReport(hre);
			
			bugs.addDocument("sent.xml", requestDocument);
			
			new Bug(hre, bugs);

			throw new IOException("The server returned a protocol error " + hre.getStatusCode() + 
					": " + hre.getLocalizedMessage());
			
		} catch (XMLParsingException xmlpe) {
			Throwable cause = xmlpe.getCause();
			BugReport bugs = new BugReport(cause);
			Document invalidDoc = xmlpe.getNonvalidatingDocument();
			File invalidFile = xmlpe.getInvalidFile();

			bugs.addDocument("sent.xml", requestDocument);
			if(invalidDoc != null)
				bugs.addDocument("recv-nonvalid.xml", invalidDoc);
			if(invalidFile != null)
				bugs.addDocument("recv-malformed.xml", invalidFile);
			
			new Bug(cause, bugs);
			
			// it's probably an ioexception...
			if(cause instanceof IOException)
				throw (IOException) cause;
			
			throw xmlpe;
		} catch (IOException ioe) {
			throw ioe;
			
		} catch (Exception uhe) {
			BugReport bugs = new BugReport(uhe);
			
			bugs.addDocument("sent.xml", requestDocument);
			
			// MalformedDocs are handled automatically by BugReport class
			if(!(uhe instanceof MalformedDocumentException) && inDocument != null)
				bugs.addDocument("received.xml", inDocument);
			
			new Bug(uhe, bugs);
			
			throw new IOException("Exception " + uhe.getClass().getName() + ": " + uhe.getLocalizedMessage());
		} finally {
			//?
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
	
	/**
	 * Creates an XML document-based request to be sent to the web interface with the basic format:
	 * <corina>
	 *    <request type="verb">
	 *    </request>
	 * </corina>
	 * 
	 * Calling this function a second time will create a new request.
	 * 
	 * @param requestVerb the verb to be used in request: create, read, update, delete, login
	 * @return the Element "request"
	 */
	public Element createRequest(ResourceQueryType queryType) {
		requestMethod = METHOD_POST;
		
		// create the document
		requestDocument = new Document();
		
		// create the root element
		Element corina = new CorinaElement("corina");
		
		requestDocument.setRootElement(corina);
		
		// create the request element
		Element request = new CorinaElement("request");
		request.setAttribute("type", queryType.getVerb());
		corina.addContent(request);
		
		return request;
	}
	
	/**
	 * 
	 * @return the 'request' element in our xml document
	 */
	public Element getRequest() {		
		return requestDocument.getRootElement().getChild("request");
	}

	/**
	 * @return the XML document of our request
	 */
	public Document getRequestDocument() {
		return requestDocument;
	}
	
	// for defining our request type...
	private static final int METHOD_POST = 1;
	private static final int METHOD_GET = 2;

	public static void main(String args[]) {
		App.platform = new Platform();
		App.platform.init();
		App.prefs = new Prefs();
		App.prefs.init();
		
		try{ 
			/*
			Dictionary d = new Dictionary();
			d.loadWait();
			*/
			
			WebXMLDocumentAccessor a = new WebXMLDocumentAccessor("authenticate");
			Element e;
			
			/*
			e = a.createRequest("nonce");
			e.addContent(new Element("authenticate"));
			a.execute();*/
			
			e = a.createRequest(ResourceQueryType.SECURELOGIN);//"securelogin");
			Element auth = new CorinaElement("authenticate");
			e.addContent(auth);			
			auth.setAttribute("username", "kit");
			a.execute();
			
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
	
	static private final String clientModuleVersion;
	
	static {
	    final String[] modules = {
	        "org.apache.http",              // HttpCore (main)
	        "org.apache.http.cookie",       // HttpCookie   
	        "org.apache.http.client",       // HttpClient
	    };
	    
        final VersionInfo[] via = VersionInfo.loadVersionInfo(modules, null);        
        final StringBuffer buf = new StringBuffer();
        
        for(int i = 0; i < via.length; i++) {
        	if(buf.length() != 0)
        		buf.append("; ");
        	
        	buf.append(via[i].getModule() + " " + via[i].getRelease());
        }
        
        clientModuleVersion = buf.toString();
	}
	
	private static Document lastInDocument = null;
	private static Document lastOutDocument = null;
	
	public static void forceGenerateWSBug() {
		Exception e = new Exception("Operator forced bug report");
		BugReport report = new BugReport(e);

		if(lastOutDocument != null)
			report.addDocument("sent.xml", lastOutDocument);
		if(lastInDocument != null)
			report.addDocument("received.xml", lastInDocument);
		
		new BugReportDialog((Frame)null, report);
	}
}
