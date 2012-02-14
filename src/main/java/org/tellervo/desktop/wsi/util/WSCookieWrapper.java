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
package org.tellervo.desktop.wsi.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie2;

public class WSCookieWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8158705290755730839L;
	
	//private boolean isClientCookie;

	public WSCookieWrapper(Cookie cookie) {
		name = cookie.getName();
		value = cookie.getValue();
		cookieComment = cookie.getComment();
		cookieDomain = cookie.getDomain();
		cookieExpiryDate = cookie.getExpiryDate();
		cookiePath = cookie.getPath();
		isSecure = cookie.isSecure();
		cookieVersion = cookie.getVersion();
		commentURL = cookie.getCommentURL();
		ports = cookie.getPorts();
		
		/*
		if(cookie instanceof ClientCookie) {
			isClientCookie = true;
			
			// copy over the attributes array (yuck)
			attribs = new HashMap<String, String>();
			for(String attr : ATTR_LIST) {
				String value;
				if((value = ((ClientCookie)cookie).getAttribute(attr)) != null) {
					attribs.put(attr, value);
				}
			}
		}
		*/
		
	}
	
	public Cookie toApacheCookie() {
		BasicClientCookie2 cookie = new BasicClientCookie2(name, value);
		
		cookie.setComment(cookieComment);
		cookie.setDomain(cookieDomain);
		cookie.setExpiryDate(cookieExpiryDate);
		cookie.setPath(cookiePath);
		cookie.setSecure(isSecure);
		cookie.setVersion(cookieVersion);
		cookie.setPorts(ports);
		
		// copy over attributes
		/*
		for(Entry<String, String> entry : attribs.entrySet()) {
			cookie.setAttribute(entry.getKey(), entry.getValue());
		}*/
		
		return cookie;
	}
	
    /** Cookie name */
    private final String name;

    /** Cookie attributes as specified by the origin server */
    private Map<String, String> attribs;
   
    /** Cookie value */
    private String value;

    /** Comment attribute. */
    private String  cookieComment;

    /** Domain attribute. */
    private String  cookieDomain;

    /** Expiration {@link Date}. */
    private Date cookieExpiryDate;

    /** Path attribute. */
    private String cookiePath;

    /** My secure flag. */
    private boolean isSecure;

    /** The version of the cookie specification I was created from. */
    private int cookieVersion;

    private String commentURL;
    private int[] ports;

    
    
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[version: ");
        buffer.append(Integer.toString(this.cookieVersion));
        buffer.append("]");
        buffer.append("[name: ");
        buffer.append(this.name);
        buffer.append("]");
        buffer.append("[value: ");
        buffer.append(this.value);
        buffer.append("]");
        buffer.append("[domain: ");
        buffer.append(this.cookieDomain);
        buffer.append("]");
        buffer.append("[path: ");
        buffer.append(this.cookiePath);
        buffer.append("]");
        buffer.append("[expiry: ");
        buffer.append(this.cookieExpiryDate);
        buffer.append("]");
        /*
		for(Entry<String, String> entry : attribs.entrySet()) {
			buffer.append("[attr:");
			buffer.append(entry.getKey());
			buffer.append("{");
			buffer.append(entry.getValue());
			buffer.append("}]");
		}*/
        return buffer.toString();
    }
    
    // attributes to care about
    /*
    private final static String ATTR_LIST[] = {
    	ClientCookie.VERSION_ATTR, 
    	ClientCookie.PATH_ATTR, 
    	ClientCookie.DOMAIN_ATTR, 
    	ClientCookie.MAX_AGE_ATTR,
    	ClientCookie.SECURE_ATTR,
    	ClientCookie.COMMENT_ATTR,
    	ClientCookie.EXPIRES_ATTR,
    	ClientCookie.PORT_ATTR,
    	ClientCookie.COMMENTURL_ATTR,
    	ClientCookie.DISCARD_ATTR
    };
    */


    private boolean iseq(Object a, Object b) {
    	if(a == null && b != null)
    		return false;
    	
    	if(b == null && a != null)
    		return false;
    	
    	if(a == null && b == null)
    		return true;
    	
    	return a.equals(b);
    }
    
	@Override
	public boolean equals(Object o) {
		if(o instanceof WSCookieWrapper) {
			WSCookieWrapper w = (WSCookieWrapper) o;
			
			return (
					iseq(name, w.name) && iseq(attribs, w.attribs) && iseq(value, w.value) && iseq(cookieComment, w.cookieComment) &&
					iseq(cookieDomain, w.cookieDomain) && iseq(cookiePath, w.cookiePath) && iseq(isSecure, w.isSecure) &&
					iseq(cookieVersion, w.cookieVersion) && iseq(commentURL, w.commentURL) && iseq(ports, w.ports)
				);
		}
		
		if(o instanceof Cookie) {
			Cookie c = (Cookie) o;
			boolean match;
			
			match = iseq(name, c.getName()) && iseq(value, c.getValue()) && iseq(cookieComment, c.getComment()) &&
					iseq(cookieDomain, c.getDomain()) && iseq(cookiePath, c.getPath()) && iseq(isSecure, c.isSecure()) &&
					iseq(cookieVersion, c.getVersion()) && iseq(commentURL, c.getCommentURL()) && iseq(ports, c.getPorts());
			
			if(!match) 
				return false;
			
			/*
			if(c instanceof ClientCookie) {
				ClientCookie cc = (ClientCookie) c;
				
				for(String attr : ATTR_LIST) {
					if(!iseq(attribs.get(attr), cc.getAttribute(attr)))
						return false;
				}
			}
			*/
			
			return true;
		}
		
		return false;
	}
 }
