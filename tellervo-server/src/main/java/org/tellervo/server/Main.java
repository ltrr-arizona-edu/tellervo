package org.tellervo.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.stream.XMLInputFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestStatus;
import org.tellervo.schema.TellervoRequestType;


/**
 * Main Tellervo Server end point
 */
@MultipartConfig(location = "/tmp", maxFileSize = 10485760L) // 10MB. 
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	// Static factories
	public static DatatypeFactory datatypeFactory;
	public static XMLInputFactory xmlInputFactory;
	public static JAXBContext jaxbContext;
	public static String dbconnstring = "";
	public static String dbuser = "";
	public static String dbpwd = "";
	public static String labName = "";
	public static String labAcronym = "";
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Main() {
		super();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			log.debug("Failed to load postgres driver");
		}
		
	    InitialContext initialcontext;
		try {
			initialcontext = new InitialContext();
		    String dbCredentialsFileLoc = (String) initialcontext.lookup("java:comp/env/DatabaseCredentials");
		    @SuppressWarnings("unchecked")
			List<String> configLines = FileUtils.readLines(new File(dbCredentialsFileLoc));

		    if(configLines.size()<3)
		    {
		    	log.error("DB credentials file is broken");
		    	return;
		    }
		    
		    dbconnstring = configLines.get(0);
		    dbuser = configLines.get(1);
		    dbpwd = configLines.get(2);
		    
		    labName = (String) initialcontext.lookup("java:comp/env/LabName");
		    labAcronym = (String) initialcontext.lookup("java:comp/env/LabAcronym");

		    
		} catch (NamingException e) {
	    	log.error("DB credentials file is broken");
	    	return;
		} catch (IOException e) {
	    	log.error("DB credentials file is broken");
	    	return;
		}  

	}

	
	public static Connection getDatabaseConnection()
	{
		try {
			return DriverManager.getConnection(dbconnstring, dbuser, dbpwd);
		} catch (SQLException e) {
			log.error("Error connecting to database");
		}
		
		return null;
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
			"<?xml-stylesheet type=\"text/css\" href=\"css/tellervo.css\"?><?xml-stylesheet type=\"text/css\" href=\"css/docbook/driver.css\"?><tellervo>\n"+
			"<header>\n"+
			"<webserviceVersion>1.1.3-SNAPSHOT</webserviceVersion>\n"+
			"<clientVersion>Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:32.0) Gecko/20100101 Firefox/32.0</clientVersion>\n"+
			"<requestDate>2014-09-10T15:29:08-07:00</requestDate>\n"+
			"<queryTime unit=\"seconds\">0</queryTime>\n"+
			"<requestUrl>/navajocfi/</requestUrl>\n"+
			"<requestType>read</requestType>\n"+
			"<status>OK</status>\n"+
			"</header>\n"+
			"<help>\n"+
			"<chapter>Welcome to the LTRR, Navajo CFI Tellervo Webservice</chapter>\n"+
			"<para>The webservice appears to be configured correctly and is ready to be used.</para><para>To access the webservice you should use the Tellervo desktop application, point in at this URL and log in with the username and password provided to you by the LTRR, Navajo CFI systems administrator.</para>\n"+
			"<para>If you have not yet installed Tellervo desktop you can download the application from <ulink url=\"http://www.tellervo.org/download\">http://www.tellervo.org/download</ulink></para>\n"+
			"</help>\n"+
			"</tellervo>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		RequestHandler handler = new RequestHandler(request, response);
		
		// Check that the user has included a request!
		if(handler.getRequest()==null)
		{
			if(handler.getCurrentStatus().equals(TellervoRequestStatus.ERROR))
			{
				handler.sendResponse();
				return;
			}
		}
		

				
		// If user is not logged in and is not attempting some sort of authentication, force them to log in
		if(!handler.auth.getIsLoggedIn())
		{
			if((!handler.getRequest().getType().equals(TellervoRequestType.PLAINLOGIN)) && 
				(!handler.getRequest().getType().equals(TellervoRequestType.SECURELOGIN)) &&
				(!handler.getRequest().getType().equals(TellervoRequestType.NONCE)) &&
				(!handler.getRequest().getType().equals(TellervoRequestType.LOGOUT))
			   )
		  {	  
			log.debug("User is not logged in and they aren't making any authentication requests.  Force them to login first");
			log.debug("Request type is: "+handler.getRequest().getType());
			handler.auth.requestUserLogin();
			return;
		  }
		}
		
		
		if(handler.getRequest().getType().equals(TellervoRequestType.PLAINLOGIN))
		{
			handler.auth.plainLogin();
			return;
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.NONCE))
		{
			handler.timeKeeper.log("Starting to process nonce request");

			handler.auth.returnNonce();
			return;
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.SECURELOGIN))
		{
			handler.timeKeeper.log("Starting to process secure login request");

			handler.auth.secureLogin();
			return;
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.LOGOUT))
		{
			handler.auth.logout();
			return;
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.READ))
		{
			if(handler.getRequest().isSetDictionaries())
			{
				Dictionaries.returnDictionaries(handler);
				return;
			}
			
			handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");
			handler.sendResponse();
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.CREATE))
		{
			//TODO
			handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");
			handler.sendResponse();
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.UPDATE))
		{
			//TODO
			handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");
			handler.sendResponse();
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.DELETE))
		{
			//TODO
			handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");
			handler.sendResponse();
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.ASSIGN))
		{
			//TODO
			handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");
			handler.sendResponse();
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.HELP))
		{
			//TODO
			handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");
			handler.sendResponse();
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.MERGE))
		{
			//TODO
			handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");
			handler.sendResponse();
		}
		else if(handler.getRequest().getType().equals(TellervoRequestType.SEARCH))
		{
			//TODO
			handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");
			handler.sendResponse();
		}

		
		handler.addMessage(TellervoRequestStatus.ERROR,667, "Unknown request type");
		handler.sendResponse();

	}
	

	// Create static instances of factories that are time consuming to instantiate. 
	// This dramatically improves performance
	static{
		try {
			jaxbContext = JAXBContext.newInstance("org.tellervo.schema");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static{
	    try {
	        datatypeFactory = DatatypeFactory.newInstance();
	    } catch (DatatypeConfigurationException e) {
	        throw new RuntimeException("Init Error!", e);
	    }
	}
	static{
		xmlInputFactory = XMLInputFactory.newInstance();
	}
}
