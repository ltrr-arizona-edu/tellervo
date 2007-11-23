/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

import java.security.MessageDigest;
import java.util.Random;

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
	
	public Authenticate(String username, String password, String nonce) {
		super("authenticate", new ResourceQueryType(ResourceQueryType.SECURELOGIN));
		
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

	protected Element prepareQuery(Element requestElement) {
		Element auth = new Element("authenticate");
		
		auth.setAttribute("username", username);
		auth.setAttribute("hash", hash);
		auth.setAttribute("nonce", cliNonce);
		
		requestElement.addContent(auth);
		return requestElement;
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
