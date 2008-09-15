/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import java.security.MessageDigest;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.util.StringUtils;

/**
 * @author Lucas Madar
 *
 */
public class Authenticate extends Resource {
	private String username;
	private String cliNonce;
	private String hash;
	private String srvNonce;
	
	public Authenticate(String username, String password, String nonce) {
		super("authenticate", ResourceQueryType.SECURELOGIN);
		
		// fail differently :)
		setResourceQueryExceptionBehavior(JUST_FAIL);
		
		// generate a random client nonce
		byte[] randomBytes = new byte[10];
		Random random = new Random();
		random.nextBytes(randomBytes);
		
		this.cliNonce = StringUtils.bytesToHex(randomBytes);
		
		// generate our fun hash thing.
		String md5password = md5(password);
		String prehash = username + ":" + md5password + ":" + nonce + ":" + cliNonce;
		
		this.hash = md5(prehash);
		this.username = username;
	}

	public Authenticate() {
		super("authenticate", ResourceQueryType.NONCE);
		
		// fail differently :)
		setResourceQueryExceptionBehavior(JUST_FAIL);		
	}
	
	public String getServerNonce() {
		return srvNonce;
	}

	@Override
	protected Element prepareQuery(ResourceQueryType queryType, Element requestElement) {
		Element auth = new Element("authenticate");
		
		// only for secure login
		if(queryType == ResourceQueryType.SECURELOGIN) {
			auth.setAttribute("username", username);
			auth.setAttribute("hash", hash);
			auth.setAttribute("nonce", cliNonce);
		}
		
		requestElement.addContent(auth);
		return requestElement;
	}
	
	@Override
	protected boolean processQueryResult(Document doc) {
		
		// get our nonce nicely
		if(getQueryType() == ResourceQueryType.NONCE) {
			Element content = doc.getRootElement().getChild("content");
			if(content == null)
				return false;
			
			Element nonce = content.getChild("nonce");
			if(nonce == null)
				return false;
			
			srvNonce = nonce.getText();
			if(srvNonce == null)
				return false;
		}
		
		// We don't do anything with this data (we know it just says succeeded)
		return true;
	}
	
	private String md5(String in) {
		String value;
		
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			digest.update(in.getBytes());
			
			return StringUtils.bytesToHex(digest.digest());
		} catch (Exception e) {
			new Bug(e);
			return "<error>";
		}
		
	}
}
