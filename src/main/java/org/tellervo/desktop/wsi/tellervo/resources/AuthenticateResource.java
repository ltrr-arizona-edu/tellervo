/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.wsi.tellervo.resources;

import java.security.MessageDigest;
import java.util.Random;

import org.tellervo.desktop.gui.Bug;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIAuthenticate;
import org.tellervo.schema.WSINonce;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.schema.WSISecurityUser;
import org.tellervo.desktop.util.StringUtils;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoResource;


/**
 * @author Lucas Madar
 *
 */
public class AuthenticateResource extends TellervoResource {
	private String username;
	private String cliNonce;
	private String hash;
	private String srvNonce;
	private String seq;
	private WSISecurityUser user;
	
	/** 
	 * Constructor that provides an authenticate resource intended for actually logging in
	 * 
	 * @param username
	 * @param password
	 * @param nonce
	 * @param seq
	 */
	public AuthenticateResource(String username, String password, String nonce, String seq) {
		super("authenticate", TellervoRequestType.SECURELOGIN, BadCredentialsBehavior.JUST_FAIL);

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
		super("authenticate", TellervoRequestType.NONCE, BadCredentialsBehavior.JUST_FAIL);

		
		
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
	public static String md5(String in) {
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
	 * @see org.tellervo.desktop.wsi.tellervo.CorinaResource#populateRequest(org.tellervo.desktop.schema.WSIRequest)
	 */
	@Override
	protected void populateRequest(WSIRequest request) {
		if(getQueryType() == TellervoRequestType.SECURELOGIN) {
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
	 * @see org.tellervo.desktop.wsi.Resource#processQueryResult(java.lang.Object)
	 */
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		if(getQueryType() == TellervoRequestType.NONCE) {
			WSINonce nonce = object.getHeader().getNonce();
			
			// nonce has to exist...
			if(nonce == null || !nonce.isSetValue())
				return false;
			
			this.seq = nonce.getSeq();
			this.srvNonce = nonce.getValue();			
		}
		
		// if we got here, things are all good -> go!
		
		try{
			user = (WSISecurityUser) object.getContent().getSqlsAndObjectsAndElements().get(0);
		} catch (Exception e){}
		
		return true;
	}
	
	
	public WSISecurityUser getAuthenticatedUser()
	{
		return user;
	}

}
