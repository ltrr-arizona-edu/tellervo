package edu.cornell.dendro.corina.wsi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.VersionInfo;
import org.jdom.Document;
import org.jdom.transform.JDOMResult;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.XMLDebugView;
import edu.cornell.dendro.corina.util.BugReport;
import edu.cornell.dendro.corina.util.PureStringWriter;
import edu.cornell.dendro.corina.util.XMLBody;
import edu.cornell.dendro.corina.util.XMLParsingException;
import edu.cornell.dendro.corina.wsi.util.WSCookieStoreHandler;

/**
 * This class is for accessing XML documents from the web service.
 * 
 * INTYPE: The type we are expecting to receive
 * OUTTYPE: The type we are sending
 */

public class WebJaxbAccessor<INTYPE, OUTTYPE> implements DataAccessor<INTYPE, OUTTYPE> {	
	/** The URL we plan to access */ 
	private URI url;
	
	/** The type of request we plan to use (POST or GET) */
	private RequestMethod requestMethod;
	
	/** The base type we plan to marshall and send to the server */
	private OUTTYPE sendingObject;
	
	/** The class of the sendingObject (OUTTYPE.class) */
	// not necessary!
	//private Class<OUTTYPE> sendingObjectClass;

	/** The class of the requestObject (INTYPE.class) */
	private Class<INTYPE> receivingObjectClass;

	
	/** The noun that we're accessing (measurements, radius, etc). 
	 * Mostly used for debugging at this point.
	 */
	private String noun;
		
	/**
 	 * Makes a class capable of performing operations on a webservice "noun" 
	 * 
	 * Note that this constructor will be useless for queries that do not contain
	 * a POST request.
	 * 
	 * @param noun What we are affecting here. For example, dictionary, sample, etc.
	 * @param receivingObjectClass the class of the object I am hoping to receive
	 */
	public WebJaxbAccessor(String noun, Class<INTYPE> receivingObjectClass) {
		requestMethod = RequestMethod.POST;
		this.noun = noun;
		this.receivingObjectClass = receivingObjectClass;
		
		try {
			String path = App.prefs.getPref("corina.webservice.url", "invalid-url!");
			url = new URI(path.trim());
			
			// If no protocol is provided assume http
			if(url.getScheme()==null)
			{
				url = new URI("http://"+path.trim());
			}
			
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
	public INTYPE query() throws IOException {
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
	
	/**
	 * Gets the schema object that we use for validation
	 * 
	 * @return the schema object, or null for no validation
	 */
	protected Schema getValidationSchema() {
		return null;
	}
	
	/**
	 * Get the JAXB context for this accessor
	 * 
	 * @return the jaxb context
	 * @throws JAXBException
	 */
	protected JAXBContext getJAXBContext() throws JAXBException {
		return JAXBContext.newInstance();
	}

	/**
	 * Get a namespace prefix mapper for this instance
	 * @return
	 */
	protected NamespacePrefixMapper getNamespacePrefixMapper() {
		return null;
	}
	
	/**
	 * Marshall this object to a JDOM Document
	 * 
	 * @param context
	 * @param object
	 * @param prefixMapper an implementation of namespacePrefixMapper
	 * @return
	 * @throws JAXBException
	 */
	protected static Document marshallToDocument(JAXBContext context,
			Object object, NamespacePrefixMapper prefixMapper)
			throws JAXBException {
		JDOMResult result = new JDOMResult();
		Marshaller m = context.createMarshaller();

		// set a namespace prefix mapper
		if (prefixMapper != null)
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefixMapper);

		m.marshal(object, result);

		return result.getDocument();
	}
	
	/**
	 * Set the object to use as an output request
	 * 
	 * @param reqObj
	 */
	public void setRequestObject(OUTTYPE reqObj) {
		this.sendingObject = reqObj;
	}
	
	private INTYPE doRequest() throws IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest req;
		JAXBContext context;
		Document outDocument = null;

		try {
			context = getJAXBContext();
		} catch (JAXBException jaxb) {
			throw new IOException("Unable to acquire JAXB context: " + jaxb.getMessage());
		}
				
		try {
			if(requestMethod == RequestMethod.POST) {
				if(this.sendingObject == null)
					throw new NullPointerException("requestDocument is null yet required for this type of query");

				// Create a new POST request
				HttpPost post = new HttpPost(url);
				// Make it a multipart post
				MultipartEntity postEntity = new MultipartEntity();
				req = post;	
				
				// create an XML document from the given objects
				outDocument = marshallToDocument(context, sendingObject, getNamespacePrefixMapper());
								
				// add it to the http post request
				XMLBody xmlb = new XMLBody(outDocument, "application/corina+xml", null);				
				postEntity.addPart("xmlrequest", xmlb);
				postEntity.addPart("traceback", new StringBody(getStackTrace()));
				post.setEntity(postEntity);
			} else {
				// well, that's nice and easy
				req = new HttpGet(url);
			}

			// debug this transaction...
			TransactionDebug.sent(outDocument, noun);
			
			// load cookies
			client.setCookieStore(WSCookieStoreHandler.getCookieStore().toCookieStore());
			
			req.setHeader("User-Agent", "Corina WSI " + Build.VERSION + 
					" (" + clientModuleVersion + "; ts " + Build.COMPLETE_VERSION_NUMBER +")");
			
			// are we using https? should we allow self-signed certs?
			if(url.getScheme().equals("https"))
				setSelfSignableHTTPSScheme(client);
			
			// create a responsehandler
			JaxbResponseHandler<INTYPE> responseHandler = 
				new JaxbResponseHandler<INTYPE>(context, receivingObjectClass);
			
			// set the schema we validate against
			responseHandler.setValidateSchema(getValidationSchema());
			
			// execute the actual http query
			INTYPE inObject = client.execute(req, responseHandler);		
			
			TransactionDebug.received(inObject, noun, context);
			
			// save our cookies?
			WSCookieStoreHandler.getCookieStore().fromCookieStore(client.getCookieStore());
			
			// ok, now inspect the document we got back
			//CorinaDocumentInspector inspector = new CorinaDocumentInspector(inDocument);

			// Verify our document based on schema validity
			//inspector.validate();
			
			// Verify our document structure, throw any exceptions!
			//inspector.verifyDocument();
			
			return inObject;
		} catch (UnknownHostException e)
		{
			throw new IOException("The URL of the server you have specified is unknown");
		}
		catch (HttpResponseException hre) {
			BugReport bugs = new BugReport(hre);
			
			bugs.addDocument("sent.xml", outDocument);
			
			new Bug(hre, bugs);

			throw new IOException("The server returned a protocol error " + hre.getStatusCode() + 
					": " + hre.getLocalizedMessage());
		} catch (IllegalStateException ex)
		{
			throw new IOException("Webservice URL must be a full URL qualified with a communications protocol.\n" +
				"Corina currently supports http:// and https://.");	
		}
		
		catch (ResponseProcessingException rspe) {
			Throwable cause = rspe.getCause();
			BugReport bugs = new BugReport(cause);
			Document invalidDoc = rspe.getNonvalidatingDocument();
			File invalidFile = rspe.getInvalidFile();

			if(outDocument != null)
				bugs.addDocument("sent.xml", outDocument);
			if(invalidDoc != null)
				bugs.addDocument("recv-nonvalid.xml", invalidDoc);
			if(invalidFile != null)
				bugs.addDocument("recv-malformed.xml", invalidFile);
			
			new Bug(cause, bugs);
			
			XMLDebugView.addDocument(BugReport.getStackTrace(cause), "Parsing Exception", true);
			
			// it's probably an ioexception...
			if(cause instanceof IOException)
				throw (IOException) cause;
			
			throw rspe;
		} catch (XMLParsingException xmlpe) {
			Throwable cause = xmlpe.getCause();
			BugReport bugs = new BugReport(cause);
			Document invalidDoc = xmlpe.getNonvalidatingDocument();
			File invalidFile = xmlpe.getInvalidFile();

			bugs.addDocument("sent.xml", outDocument);
			if(invalidDoc != null)
				bugs.addDocument("recv-nonvalid.xml", invalidDoc);
			if(invalidFile != null)
				bugs.addDocument("recv-malformed.xml", invalidFile);
			
			new Bug(cause, bugs);
			
			XMLDebugView.addDocument(BugReport.getStackTrace(cause), "Parsing Exception", true);
			
			// it's probably an ioexception...
			if(cause instanceof IOException)
				throw (IOException) cause;
			
			throw xmlpe;
		} catch (IOException ioe) {
			throw ioe;
			
		} catch (Exception uhe) {
			BugReport bugs = new BugReport(uhe);
			
			bugs.addDocument("sent.xml", outDocument);
			
			/*
			// MalformedDocs are handled automatically by BugReport class
			if(!(uhe instanceof MalformedDocumentException) && inDocument != null)
				bugs.addDocument("received.xml", inDocument);
			*/
			
			new Bug(uhe, bugs);
			
			throw new IOException("Exception " + uhe.getClass().getName() + ": " + uhe.getLocalizedMessage());
		} finally {
			//?
		}
	}
	
	/** A https scheme that allows for self-signed https certs */
	private static Scheme selfSignableHTTPSScheme = null;
	
	static void setSelfSignableHTTPSScheme(HttpClient client) {
		if(selfSignableHTTPSScheme == null) {
			try {
				// make a new SSL context
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
			
				// make a new socket factory
				SSLSocketFactory socketFactory = new SSLSocketFactory(sc);
			
				// register the scheme with the connection
				selfSignableHTTPSScheme = new Scheme("https", socketFactory, 443);
			} catch (Exception e) {
				// don't do anything; we'll just get errors later.
				return;
			}	
		}
		
		client.getConnectionManager().getSchemeRegistry().register(selfSignableHTTPSScheme);
	}

	// This allows us to ignore the server certificate chain
	private static TrustManager[] trustAllCerts = new TrustManager[] {
		new X509TrustManager() {
        	public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
        	public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
        	public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
        }
	};
	
	// for defining our request type...
	private static enum RequestMethod { GET, POST };

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
}
