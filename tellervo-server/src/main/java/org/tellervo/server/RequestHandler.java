package org.tellervo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestStatus;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIContent;
import org.tellervo.schema.WSIHeader;
import org.tellervo.schema.WSIHelp;
import org.tellervo.schema.WSIMessage;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.xml.sax.SAXException;

/**
 * RequestHandler does all the standard validating, marshalling and unmarshalling.  It also holds the request and response variables
 * 
 * @author pbrewer
 *
 */
public class RequestHandler {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	public static final boolean DO_XML_VALIDATION = true;
	
	private HttpServletRequest originalFullRequest;
	private HttpServletResponse response;
	private WSIRequest request;
	private String requestAsString;
	private TellervoRequestStatus currentStatus = TellervoRequestStatus.OK;
	private WSIHeader header = new WSIHeader();
	private WSIContent content = new WSIContent();
	private ArrayList<WSIMessage> messages = new ArrayList<WSIMessage>();
	public TimeKeeper timeKeeper;
	public Auth auth;

	public RequestHandler(HttpServletRequest request, HttpServletResponse response) {
		
		// Start the TimeKeeper for logging execution time
		timeKeeper = new TimeKeeper();
		timeKeeper.log("Starting");
		
		this.originalFullRequest = request;
		this.response = response;

		// Set basic header info
		XMLGregorianCalendar requestDate = getXMLGregorianCalendarNow();
		header.setRequestDate(requestDate);
		header.setClientVersion(originalFullRequest.getHeader("User-Agent"));
		header.setRequestUrl(originalFullRequest.getServletPath());
		header.setWebserviceVersion("1.1.4-SNAPSHOT");
		response.setContentType("text/xml");
		timeKeeper.log("Set basic response headers");

		parseRequest();
		timeKeeper.log("Marshalled XML using JAXB");
		
		// Set request format to standard if not specified by user
		if(getRequest()!=null)
		{
			if(!getRequest().isSetFormat())
			{
				getRequest().setFormat(TellervoRequestFormat.STANDARD);
			}
		}
		
		// Set up Auth to check for existing session etc
		auth = new Auth(this);
		header.setSecurityUser(auth.getCurrentSecurityUser());
		
	}

	
	/**
	 * Extract the XML request from the HTTP POST sent by the client
	 */
	private void parseRequest() {
		
		String xmlrequest = null;
		
		// Grab request content depending on the way the client has sent it
		if(originalFullRequest.getContentType().startsWith("multipart/form-data"))
		{
			// Tellervo uses this method
			//log.debug("Request is using multipart/form-data");

			Collection<Part> postParts = null;
			try {
				postParts = originalFullRequest.getParts();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
				addMessage(
						TellervoRequestStatus.ERROR,
						667,
						"Internal server error extracting your XML request");
				return;
				
			} catch (IOException e1) {
				e1.printStackTrace();
				addMessage(
						TellervoRequestStatus.ERROR,
						667,
						"Internal server error extracting your XML request");
				return;
			} catch (ServletException e1) {
				e1.printStackTrace();
				addMessage(
						TellervoRequestStatus.ERROR,
						667,
						"Internal server error extracting your XML request");
				return;
			}
			for (Part part : postParts) {
				if (part.getName().equals("xmlrequest")) {
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(part.getInputStream(),
										"UTF-8"));
						StringBuilder value = new StringBuilder();
						char[] buffer = new char[10240];
	
						for (int length = 0; (length = reader.read(buffer)) > 0;) {
							value.append(buffer, 0, length);
						}
	
						xmlrequest = value.toString();
	
					} catch (IOException e) {
						e.printStackTrace();
						addMessage(
								TellervoRequestStatus.ERROR,
								667,
								"Internal server error extracting your XML request");
						return;
					}
				}
			}
		}
		else if (originalFullRequest.getContentType().startsWith("application/x-www-form-urlencoded"))
		{
			// This is the standard HTML form method
			//log.debug("Request is using standard application/x-www-form-urlencoded");
			xmlrequest = originalFullRequest.getParameter("xmlrequest");
		}
		else
		{
			addMessage(
					TellervoRequestStatus.ERROR,
					905,
					"The XML request was sent with an unknown HTTP content type.  Must be one of 'application/x-www-form-urlencoded' or 'multipart/form-data'");
			return;
		}
		

		// Complain if no request has been found
		if (xmlrequest == null) {
			addMessage(
					TellervoRequestStatus.ERROR,
					905,
					"The client you are using has not sent a valid HTTP POST parameter called xmlrequest");
			return;
		}
		else
		{
			requestAsString = xmlrequest.trim();
		}
				
		timeKeeper.log("Extracted user request from POST");

		
		// Validate the XML sent by the client if configured
		if(DO_XML_VALIDATION)
		{
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
	
			try {
				URL file = new URL(
						"https://tellervo.ltrr.arizona.edu/navajocfi/schemas/tellervo.xsd");
	
				// File file = new
				// File(getServletContext().getRealPath("/schemas/tellervo.xsd"));
	
				Schema schema = null;
				// Next try to load the schema to validate
				schema = factory.newSchema(file);
	
				// Do the validation
				Validator validator = schema.newValidator();
				StreamSource source = new StreamSource(new StringReader(requestAsString));
				validator.validate(source);
	
			} catch (SAXException ex) {
				addMessage(TellervoRequestStatus.WARNING, 667,
						"Tellervo schema is invalid! Unable to validate your request.");
	
			} catch (IOException e) {
				addMessage(TellervoRequestStatus.WARNING, 667,
						"Unable to load Tellervo schema to validate your request.");
			}
			timeKeeper.log("Validated XML request");
		}

		// Marshall the XML into Java classes using JAXB
		StringReader reader = new StringReader(requestAsString);
		try {
			timeKeeper.log("Instantiate JAXB classes");

			XMLStreamReader xmler = Main.xmlInputFactory.createXMLStreamReader(reader);
			timeKeeper.log("Instantiated XMLStreamReader");

			Unmarshaller u = Main.jaxbContext.createUnmarshaller();
			timeKeeper.log("Instantiated Unmarshaller");

			Object root = u.unmarshal(xmler);
			timeKeeper.log("Do unmarshalling");

			if (root instanceof WSIRootElement) {
				WSIRootElement rootElement = (WSIRootElement) root;

				if (rootElement.isSetRequest()) {
					timeKeeper.log("Extract request element");
					request = rootElement.getRequest();
					timeKeeper.log("Finished extracing request element");

					return;
				} else {
					addMessage(TellervoRequestStatus.ERROR, 905,
							"Your XML does not include a request section");
					request = null;
					return;
				}
			}
			else {
				addMessage(TellervoRequestStatus.ERROR, 905,
						"Your request does not validate against the Tellervo schema.");
				request = null;
				return;
			}

		} catch (JAXBException e2) {
			addMessage(TellervoRequestStatus.ERROR, 905,
					"Your request does not validate against the Tellervo schema.");
			request = null;
			return;
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get the original HttpServletRequest sent to the server.  Not to be confused with getRequest()
	 * 
	 * @return
	 */
	public HttpServletRequest getOriginalFullRequest() {
		return originalFullRequest;
	}

	/**
	 * Get the original HttpServletResponse
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Get the Tellervo xmlrequest sent by the client.  Not to be confused with getOriginalFullRequest()
	 * 
	 * @return
	 */
	public WSIRequest getRequest() {
		return request;
	}

	/**
	 * Get the current status of this request
	 * 
	 * @return
	 */
	public TellervoRequestStatus getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * Get the WSIHeader portion for this request
	 * 
	 * @return
	 */
	public WSIHeader getHeader() {
		return header;
	}

	/**
	 * Get the WSIContent portion for this request
	 * 
	 * @return
	 */
	public WSIContent getContent() {
		return content;
	}

	/**
	 * Get an array of messages for this request
	 * 
	 * @return
	 */
	public ArrayList<WSIMessage> getMessages() {
		return messages;
	}



	/**
	 * Package up the response and send back to the user. This will include any
	 * errors/warnings/notices etc.
	 */
	public void sendResponse() {
		sendResponse(null);
	}

	/**
	 * Package up the response and send back to the user. This will include any
	 * errors/warnings/notices etc. If help is included then it will be shown,
	 * otherwise any WSIContent will be sent instead
	 */
	public void sendResponse(WSIHelp help) {
		header.setStatus(currentStatus);

		WSIHeader.QueryTime qt = new WSIHeader.QueryTime();
		qt.setValue(timeKeeper.getTimeSinceStart());
		qt.setUnit("ms");
		header.setQueryTime(qt);

		if (request != null && request.isSetType()) {
			header.setRequestType(request.getType());
		} else {
			header.setRequestType(TellervoRequestType.HELP);
		}

		if (messages.size() > 0) {
			header.setMessages(messages);
		} else {
			header.setMessages(null);
		}

		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			Marshaller m = Main.jaxbContext.createMarshaller();
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					new TellervoNamespacePrefixMapper());
			/*
			 * if (schema != null) { m.setSchema(schema); }
			 */

			WSIRootElement root = new WSIRootElement();
			root.setHeader(header);

			if (help == null) {
				root.setContent(content);
			} else {
				root.setHelp(help);
			}

			m.marshal(root, out);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setContent(WSIContent content) {
		this.content = content;
	}

	/**
	 * Get the XML request sent by the client as a string rather than a WSIRequest 
	 * 
	 * @see getRequest() 
	 * @return
	 */
	public String getRequestAsString()
	{
		return requestAsString;
	}
	
	/**
	 * Add a notice, warning or error message to the response header.  This function 
	 * ensures the current status is set to the most severe state encountered
	 * 
	 * @param status
	 * @param code
	 * @param message
	 */
	public void addMessage(TellervoRequestStatus status, Integer code,
			String message) {
		if (currentStatus.equals(TellervoRequestStatus.OK)) {
			currentStatus = status;
		} else if (currentStatus.equals(TellervoRequestStatus.NOTICE)) {
			if (currentStatus != TellervoRequestStatus.OK) {
				currentStatus = status;
			}
		} else if (currentStatus.equals(TellervoRequestStatus.WARNING)) {
			if (currentStatus != TellervoRequestStatus.OK
					&& currentStatus != TellervoRequestStatus.NOTICE) {
				currentStatus = status;
			}
		} else if (currentStatus.equals(TellervoRequestStatus.ERROR)) {

		}

		WSIMessage ms = new WSIMessage();
		ms.setCode(code);
		ArrayList<Object> messlist = new ArrayList<Object>();
		messlist.add(message);
		ms.setContent(messlist);

		messages.add(ms);

	}

	/**
	 * Get an XMLGregorianCalendar for the current date and time
	 * 
	 * @return
	 */
	private static XMLGregorianCalendar getXMLGregorianCalendarNow(){
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		XMLGregorianCalendar now = Main.datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		return now;
	}

}
