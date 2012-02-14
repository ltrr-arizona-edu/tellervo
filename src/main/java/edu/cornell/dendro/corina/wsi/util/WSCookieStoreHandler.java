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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cornell.dendro.corina.core.App;

public class WSCookieStoreHandler {
	private final static Logger log = LoggerFactory.getLogger(WSCookieStoreHandler.class);
	private static WSCookieStore cookieStore = null;
	
	/**
	 * Get the singleton instance of this cookie store
	 * @return
	 */
	public static WSCookieStore getCookieStore() {
		if(cookieStore == null)
			cookieStore = load();
		
		return cookieStore;
	}
	
	/**
	 * Load the serialized object
	 * @return
	 */
	private static WSCookieStore load() {
		try {
			FileInputStream fis = new FileInputStream(getStorePath());
			ObjectInputStream in = new ObjectInputStream(fis);
			
			WSCookieStore cs = (WSCookieStore) in.readObject();
			
			log.debug("Sucessfully loaded cookie store: " + getStorePath());

			in.close();
			return cs;
			
		} catch(IOException ioe) {
			log.error("Failed to load cookie store: " + getStorePath());
			ioe.printStackTrace();
		} catch(ClassNotFoundException cnfe) {
			// err??
			// ignore, I suppose
		}
		return new WSCookieStore();
	}
	
	/**
	 * Save the cookie store
	 * Not intended to be called by any future code
	 * @param cs
	 */
	public static void save(WSCookieStore cs) {
		try {
			FileOutputStream fos = new FileOutputStream(getStorePath());
			ObjectOutputStream out = new ObjectOutputStream(fos);
			
			out.writeObject(cs);
			out.close();
			
		} catch (IOException ex) {
			log.error("Failed to save cookie store: " + getStorePath());
			ex.printStackTrace();
		}
	}
	
	private static String getStorePath() {
		return App.prefs.getCorinaDir() + "WebCookieStore";
	}
}
