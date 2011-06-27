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
package edu.cornell.dendro.corina.wsi.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

public class WSCookieStore implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8764583327124958950L;
	
	/**
	 * A list of cookies
	 */
	private HashMap<String, WSCookieWrapper> cookies;
	
	/**
	 * Construct a new, empty cookie store
	 */
	public WSCookieStore() {
		cookies = new HashMap<String, WSCookieWrapper>();
	}
	
	/**
	 * Unique hash on name::domain
	 * @param c
	 * @return
	 */
	private String hashCookie(Cookie c) {
		return c.getName() + "::" + c.getDomain().toLowerCase();
	}
	
	/**
	 * Populate this cookie store from an Apache cookie store
	 * @param cs
	 */
	public void fromCookieStore(CookieStore cs) {
		boolean storeChanged = false;
		
		for(Cookie c : cs.getCookies()) {
			String hv = hashCookie(c);
			WSCookieWrapper cw = cookies.get(hv);
			
			// cookie already exists and is unchanged
			if(cw != null && cw.equals(c))
				continue;

			// (re-)wrap a cookie
			cookies.put(hv, new WSCookieWrapper(c));
			storeChanged = true;
			
			System.out.println("New Cookie: " + hv);
		}
		
		if(storeChanged) {
			WSCookieStoreHandler.save(this);
		}
	}	
	
	public CookieStore toCookieStore() {
		CookieStore cs = new BasicCookieStore();
		
		for(Entry<String, WSCookieWrapper> c : cookies.entrySet())
			cs.addCookie(c.getValue().toApacheCookie());
		
		return cs;
	}
	
	/**
	 * Empty the cookie store (i.e. logout)
	 */
	public void emptyCookieJar()
	{
		cookies = new HashMap<String, WSCookieWrapper>();
	}
}
