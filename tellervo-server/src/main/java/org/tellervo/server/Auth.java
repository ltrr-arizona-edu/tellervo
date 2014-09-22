package org.tellervo.server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.wsi.tellervo.resources.AuthenticateResource;
import org.tellervo.schema.TellervoRequestStatus;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSINonce;
import org.tellervo.schema.WSISecurityUser;

public class Auth {

	static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs
	private static final Logger log = LoggerFactory.getLogger(Auth.class);

	private Integer cookieLifetimeSecs = 30 * 60;

	private String securityUserID = null;
	private String firstName = null;
	private String lastName = null;
	private String userName = null;
	private Boolean isLoggedIn = false;
	private Boolean isAdmin = false;
	private RequestHandler handler;

	public Auth(RequestHandler handler) {
		this.handler = handler;

		logRequest();
		
		HttpSession session = handler.getOriginalFullRequest()
				.getSession(false);

		if (session == null) {
			log.debug("This is a new session.  User will need to log in if they aren't already trying...");
			return;
		}

		log.debug("Session ID = " + session.getId());
		log.debug("Session was created = " + session.getCreationTime());

		if(session.getAttribute("securityuserid")!=null) this.setSecurityUserID(session.getAttribute("securityuserid").toString());
		if(session.getAttribute("firstname")!=null) this.setFirstName(session.getAttribute("firstname").toString());
		if(session.getAttribute("lastname")!=null) this.setLastName(session.getAttribute("lastname").toString());
		if(session.getAttribute("username")!=null) this.setUserName(session.getAttribute("username").toString());

		if (securityUserID != null && firstName != null && lastName != null
				&& userName != null) {
			setIsLoggedIn(true);
			setIsUserAdmin();
		} else {
			setIsLoggedIn(false);
			isAdmin = false;
		}
	}

	/**
	 * Get the currently logged in user.  If no user is logged in, then return null;
	 * @return
	 */
	public WSISecurityUser getCurrentSecurityUser()
	{
		if(isLoggedIn)
		{
			WSISecurityUser user = new WSISecurityUser();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUsername(userName);
			user.setId(securityUserID);
			return user;
		}
		
		return null;
	}
	
	/**
	 * Determine whether this user is an administrator
	 */
	private void setIsUserAdmin() {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Main.getDatabaseConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM cpgdb.isadmin('"+this.getSecurityUserID()+"'::uuid) WHERE isadmin=true");

			rs.next();
			
			if (rs.getBoolean("isadmin")==false)
			{
				//log.debug("User is not an admin");
			}
			else
			{
				//log.debug("User is an admin");
				this.isAdmin = true;
			}

		} catch (SQLException ex) {
			log.error(ex.getMessage());
			handler.addMessage(TellervoRequestStatus.ERROR, 701,
					"Error connecting to database");

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				log.error(ex.getMessage());
				handler.addMessage(TellervoRequestStatus.ERROR, 701,
						"Error connecting to database");
			}
		}
	}

	/**
	 * Log in using
	 */
	public void secureLogin(){
		
		log.debug("Doing secure login");

		if (!handler.getRequest().isSetAuthenticate()) {
			logIP();
			handler.addMessage(TellervoRequestStatus.ERROR, 905,
					"You asked to log in but didn't provide any authentication credentials");
			handler.sendResponse();
			return;
		}
		
		// Extract credentials provided by user
		String tusr = null;
		String hash = null;
		String snonce = null;
		String cnonce = null;
		String seq = null;
		Integer seqint = null;
		String dbpwdhash = null;
		try {
			tusr = StringEscapeUtils.escapeSql(handler.getRequest().getAuthenticate().getUsername());
			hash = StringEscapeUtils.escapeSql(handler.getRequest().getAuthenticate().getHash());
			snonce  = StringEscapeUtils.escapeSql(handler.getRequest().getAuthenticate().getSnonce());
			cnonce = StringEscapeUtils.escapeSql(handler.getRequest().getAuthenticate().getCnonce());
			seq = StringEscapeUtils.escapeSql(handler.getRequest().getAuthenticate().getSeq());
			
		} catch (Exception e) {
			logIP();

			handler.addMessage(TellervoRequestStatus.ERROR, 905,
					"You requested to log in but didn't provide all the necessary parameters for a secure login");
			handler.sendResponse();
			return;
		}
		try{
			seqint = Integer.parseInt(seq);
		} catch (NumberFormatException e)
		{
			logIP();

			handler.addMessage(TellervoRequestStatus.ERROR, 111,
					"Invalid sequence number supplied while doing secure authentication");
			handler.sendResponse();
			return;
		}
		
		// Check user has supplied all necessary parameters
		if(tusr==null || hash==null || snonce==null || cnonce==null || seqint==null)
		{
			logIP();
			handler.addMessage(TellervoRequestStatus.ERROR, 902,
					"One or more parameters are missing from the secure login request");
			handler.sendResponse();
			return;
		}
		
		
		// Check the server nonce the client is using has not expired
		if((!snonce.equals(getNonce(seqint, true).getValue())) && 
				(snonce.equals(getNonce(seqint, false).getValue())))
		{
			logIP();
			handler.addMessage(TellervoRequestStatus.ERROR, 105,
					"The server nonce you supplied is invalid");
			handler.sendResponse();
			return;
		}
		
		
	    String sql = "SELECT * FROM tblsecurityuser WHERE username='"+tusr+"' AND isactive=true";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;


		try {
			handler.timeKeeper.log("Opening DB connection");

			con = Main.getDatabaseConnection();
			st = con.createStatement();
			
			handler.timeKeeper.log("Sending query to DB");

			rs = st.executeQuery(sql);

			handler.timeKeeper.log("Received response from DB");

			if (rs.next()) {
				dbpwdhash = rs.getString("password");
			}
			
			if(dbpwdhash==null)
			{
				logIP();
				this.setIsLoggedIn(false);
				handler.addMessage(TellervoRequestStatus.ERROR, 106,
						"The username '"+tusr+"' is not known");
				handler.sendResponse();
				return;
			}
			
			// Compute hashes from what we know and compare to what user has sent
			String serverHashCurrent = AuthenticateResource.getHash(tusr + ":" + dbpwdhash + ":" + getNonce(seqint, true).getValue() + ":" + cnonce);
			String serverHashLast = AuthenticateResource.getHash(tusr + ":" + dbpwdhash + ":" + getNonce(seqint, false).getValue() + ":" + cnonce);
			if(hash.equals(serverHashCurrent) || hash.equals(serverHashLast))
			{
				this.setIsLoggedIn(true);
				this.setSecurityUserID(rs.getString("securityuserid"));
				this.setFirstName(rs.getString("firstname"));
				this.setLastName(rs.getString("lastname"));
				this.setUserName(rs.getString("username"));
				HttpSession session = handler.getOriginalFullRequest()
						.getSession();
				session.setMaxInactiveInterval(this.cookieLifetimeSecs);
				session.setAttribute("securityuserid", this.getSecurityUserID());
				session.setAttribute("firstname", this.getFirstName());
				session.setAttribute("lastname", this.getLastName());
				session.setAttribute("username", this.getUserName());

				handler.getHeader().setSecurityUser(this.getCurrentSecurityUser());
				handler.timeKeeper.log("Sending response to user");

				handler.sendResponse();
				handler.timeKeeper.log("Response sent");
				logIP();
				return;

			} else {
				logIP();
				this.setIsLoggedIn(false);
				handler.addMessage(TellervoRequestStatus.ERROR, 101,
						"Authentication failed using secure login method");
				handler.sendResponse();
				return;

			}

		} catch (SQLException ex) {
			logIP();
			log.error(ex.getMessage());
			handler.addMessage(TellervoRequestStatus.ERROR, 701,
					"Error connecting to database");
			handler.sendResponse();
			return;

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logIP();
				log.error(ex.getMessage());
				handler.addMessage(TellervoRequestStatus.ERROR, 701,
						"Error connecting to database");
				handler.sendResponse();
				return;
			}
		}

	}
	

	
	/**
	 * Log in to the web service using the less secure plain login method.
	 * Should really be using secureLogin() instead
	 */
	public void plainLogin() {

		log.debug("Doing plain login");

		if (!handler.getRequest().isSetAuthenticate()) {
			logIP();
			handler.addMessage(TellervoRequestStatus.ERROR, 905,
					"You requested to log in but didn't provide any authentication credentials");
			handler.sendResponse();
			return;
		}

		String tusr = null;
		String tpwd = null;
		String dbpwd = null;
		try {
			tusr = handler.getRequest().getAuthenticate().getUsername();
			tpwd = handler.getRequest().getAuthenticate().getPassword();
		} catch (Exception e) {
			logIP();
			handler.addMessage(TellervoRequestStatus.ERROR, 905,
					"You requested to log in but didn't provide username and/or password");
			handler.sendResponse();
			return;
		}

		if (tusr == null || tusr.length() == 0) {
			logIP();
			handler.addMessage(TellervoRequestStatus.ERROR, 905,
					"You requested to log in but didn't provide username");
			handler.sendResponse();
			return;
		}
		if (tpwd == null || tpwd.length() == 0) {
			logIP();
			handler.addMessage(TellervoRequestStatus.ERROR, 905,
					"You requested to log in but didn't provide password");
			handler.sendResponse();
			return;
		}

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Main.getDatabaseConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM tblsecurityuser WHERE username='"
					+ StringEscapeUtils.escapeSql(tusr) + "' AND isactive=true");

			if (rs.next()) {
				dbpwd = rs.getString("password");
			}

			if (md5Java(tpwd).equals(dbpwd)) {
				this.setIsLoggedIn(true);
				this.setSecurityUserID(rs.getString("securityuserid"));
				this.setFirstName(rs.getString("firstname"));
				this.setLastName(rs.getString("lastname"));
				this.setUserName(rs.getString("username"));
				HttpSession session = handler.getOriginalFullRequest()
						.getSession();
				session.setMaxInactiveInterval(this.cookieLifetimeSecs);
				session.setAttribute("securityuserid", this.getSecurityUserID());
				session.setAttribute("firstname", this.getFirstName());
				session.setAttribute("lastname", this.getLastName());
				session.setAttribute("username", this.getUserName());
				
				logIP();
				handler.getHeader().setSecurityUser(this.getCurrentSecurityUser());
				handler.sendResponse();
				return;

			} else {
				logIP();
				this.setIsLoggedIn(false);
				handler.addMessage(TellervoRequestStatus.ERROR, 101,
						"Authentication failed using plain login method");
				handler.sendResponse();
				return;

			}

		} catch (SQLException ex) {
			logIP();
			log.error(ex.getMessage());
			handler.addMessage(TellervoRequestStatus.ERROR, 701,
					"Error connecting to database");
			handler.sendResponse();
			return;

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logIP();
				log.error(ex.getMessage());
				handler.addMessage(TellervoRequestStatus.ERROR, 701,
						"Error connecting to database");
				handler.sendResponse();
				return;
			}
		}

	}

	/**
	 * Log out from the web service by expiring all cookies
	 */
	public void logout() {

		setIsLoggedIn(false);

		HttpSession session = handler.getOriginalFullRequest()
				.getSession(false);

		if (session != null) {
			session.invalidate();
		}

		handler.sendResponse();
	}

	/**
	 * Force the user to authenticate
	 */
	public void requestUserLogin() {
		handler.addMessage(TellervoRequestStatus.ERROR, 102, "You must login");
		returnNonce();
	}

	public void returnNonce() {
		handler.getHeader().setNonce(getNonce(getSequence(), true));
		handler.sendResponse();
	}

	/**
	 * Get a sequence number
	 * 
	 * @return
	 */
	private int getSequence() {
		SecureRandom random = new SecureRandom();
		return Math.abs(random.nextInt());
	}

	/**
	 * Get a nonce using the specified sequence. If useCurrentTime is true, then
	 * the nonce is based on the current date/time, otherwise it uses the
	 * date/time from 1 minute ago
	 * 
	 * @param sequence
	 * @param useCurrentTime
	 * @return
	 */
	private WSINonce getNonce(Integer sequence, boolean useCurrentTime) {
		WSINonce nonce = new WSINonce();
		nonce.setSeq(sequence.toString());
		SimpleDateFormat dt = new SimpleDateFormat("EEEE MMMM d H:mm z yyyy");

		if (useCurrentTime) {
			String message = dt.format(new Date()) + ":" + sequence;
			nonce.setValue(md5Java(message));
		} else {
			long timeNow = new Date().getTime();
			Date oneminago = new Date(timeNow - ONE_MINUTE_IN_MILLIS);
			String message = dt.format(oneminago) + ":" + sequence;
			nonce.setValue(md5Java(message));
		}

		return nonce;
	}

	public static String md5Java(String message) {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));

			// converting byte array to Hexadecimal String
			StringBuilder sb = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				sb.append(String.format("%02x", b & 0xff));
			}

			digest = sb.toString();

		} catch (UnsupportedEncodingException ex) {
		} catch (NoSuchAlgorithmException ex) {
		}
		return digest;
	}

	public String getSecurityUserID() {
		return securityUserID;
	}

	private void setSecurityUserID(String securityUserID) {
		this.securityUserID = securityUserID;
	}

	public String getFirstName() {
		return firstName;
	}

	private void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	private void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	private void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	/**
	 * Log the IP address for security auditing purposes
	 */
	private void logIP() {

		Connection con = null;
		Statement st1 = null;

		String delSQL = "DELETE FROM tbliptracking WHERE ipaddr='"
				+ StringEscapeUtils.escapeSql(handler.getOriginalFullRequest()
						.getRemoteAddr()) + "'";
		String insSQL = "INSERT INTO tbliptracking (ipaddr, securityuserid) VALUES ('"
				+ StringEscapeUtils.escapeSql(handler.getOriginalFullRequest()
						.getRemoteAddr()) + "', '"+this.getSecurityUserID()+"')";

		//log.debug("DelSQL : " + delSQL);
		//log.debug("InsSQL : " + insSQL);

		try {
			con = Main.getDatabaseConnection();
			con.setAutoCommit(false);
			st1 = con.createStatement();
			st1.executeUpdate(delSQL);
			st1.executeUpdate(insSQL);
			con.commit();

		} catch (SQLException ex) {
			ex.printStackTrace();
			if (con != null) {
				try {
					log.debug("Transaction is being rolled back");
					con.rollback();
					return;
				} catch (SQLException excep) {
					log.error("Error rolling back transaction");
					return;
				}
			}
		} finally {
			try {

				if (st1 != null) {
					st1.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				log.error(ex.getMessage());
				handler.addMessage(TellervoRequestStatus.ERROR, 701,
						"Error connecting to database");
			}
		}

	}
	
	/**
	 * Log this request to the database
	 */
	private void logRequest() {
		
		Connection con = null;
		Statement st1 = null;

		String insSQL = "INSERT INTO tblrequestlog (request, "
				+ "ipaddr, "
				+ "client, "
				+ "securityuserid) VALUES ('";
			
		try{
			if(handler.getRequest().getType().equals(TellervoRequestType.PLAINLOGIN))
			{
				insSQL += StringEscapeUtils.escapeSql("[plain text login not logged]")+"', '";
			}
			else
			{
				insSQL += StringEscapeUtils.escapeSql(handler.getRequestAsString())+"', '";
			}	
		} catch (Exception e)
		{
			insSQL += StringEscapeUtils.escapeSql(handler.getRequestAsString())+"', '";
		}
			
			
		insSQL += StringEscapeUtils.escapeSql(handler.getOriginalFullRequest().getRemoteAddr()) + "', '"
				+ StringEscapeUtils.escapeSql(handler.getOriginalFullRequest().getHeader("User-Agent"))+ "', ";
		
		if(this.getSecurityUserID()==null)
		{
			insSQL+= "null)";
		}
		else
		{
			insSQL+="'"+this.getSecurityUserID()+"')";
		}

		log.debug("InsSQL : " + insSQL);

		try {
			con = Main.getDatabaseConnection();
			con.setAutoCommit(false);
			st1 = con.createStatement();
			st1.executeUpdate(insSQL);
			con.commit();

		} catch (SQLException ex) {
			ex.printStackTrace();
			if (con != null) {
				try {
					log.debug("Transaction is being rolled back");
					con.rollback();
					return;
				} catch (SQLException excep) {
					log.error("Error rolling back transaction");
					return;
				}
			}
		} finally {
			try {

				if (st1 != null) {
					st1.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				log.error(ex.getMessage());
				handler.addMessage(TellervoRequestStatus.ERROR, 701,
						"Error connecting to database");
			}
		}
		
		
	}

}
