/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.security.MessageDigest;
import java.util.Random;

import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIAuthenticate;
import edu.cornell.dendro.corina.schema.WSINonce;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaResource;

/**
 * @author Lucas Madar
 *
 */
public class AuthenticateResource extends CorinaResource {
	private String username;
	private String cliNonce;
	private String hash;
	private String srvNonce;
	private String seq;
	
	/** 
	 * Constructor that provides an authenticate resource intended for actually logging in
	 * 
	 * @param username
	 * @param password
	 * @param nonce
	 * @param seq
	 */
	public AuthenticateResource(String username, String password, String nonce, String seq) {
		super("authenticate", CorinaRequestType.SECURELOGIN, BadCredentialsBehavior.JUST_FAIL);
			
		// generate a random client nonce
		byte[] randomBytes = new byte[10];
		Random random = new Random();
		random.nextBytes(randomBytes);
		
		this.srvNonce = nonce;
		this.seq = seq;
		this.cliNonce = StringUtils.bytesToHex(randomBytes);
		
		// generate our fun hash thing.
		String md5password = md5(password);
		String prehash = username + ":" + md5password + ":" + nonce + ":" + cliNonce;
		
		this.hash = md5(prehash);
		this.username = username;
	}

	/**
	 * Constructor that provides an authenticate resource intended for retrieving a nonce
	 */
	public AuthenticateResource() {
		super("authenticate", CorinaRequestType.NONCE, BadCredentialsBehavior.JUST_FAIL);
	}

	/**
	 * Retrieve the server's nonce
	 * @return
	 */
	public String getServerNonce() {
		return srvNonce;
	}
	
	/**
	 * Retrieve the server's nonce sequence
	 * @return
	 */
	public String getServerNonceSeq() {
		return seq;
	}
	
	/**
	 * Compute an md5 hash of this string
	 * 
	 * @param in
	 * @return an md5 hash, in string format
	 */
	private String md5(String in) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			digest.update(in.getBytes());
			
			return StringUtils.bytesToHex(digest.digest());
		} catch (Exception e) {
			new Bug(e);
			return "<error>";
		}	
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.corina.CorinaResource#populateRequest(edu.cornell.dendro.corina.schema.WSIRequest)
	 */
	@Override
	protected void populateRequest(WSIRequest request) {
		if(getQueryType() == CorinaRequestType.SECURELOGIN) {
			WSIAuthenticate auth = new WSIAuthenticate();
			
			auth.setUsername(this.username);
			auth.setHash(this.hash);
			auth.setCnonce(this.cliNonce);
			auth.setSnonce(this.srvNonce);
			auth.setSeq(this.seq);
			
			request.setAuthenticate(auth);
		}
		
		return;
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.Resource#processQueryResult(java.lang.Object)
	 */
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		if(getQueryType() == CorinaRequestType.NONCE) {
			WSINonce nonce = object.getHeader().getNonce();
			
			// nonce has to exist...
			if(nonce == null || !nonce.isSetValue())
				return false;
			
			this.seq = nonce.getSeq();
			this.srvNonce = nonce.getValue();			
		}
		
		// if we got here, things are all good -> go!
		return true;
	}

}
