/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.prefs.PrefsEvent;
import org.tellervo.desktop.prefs.PrefsListener;
import org.tellervo.desktop.prefs.Prefs.PrefKey;


/**
 * This class simply handles our proxy settings
 * 
 * It listens to prefs for any changes, and applies them immediately.
 * 
 * @author lucasm
 */

public class ProxyManager implements PrefsListener {
	private String lastProxyType;
	private final static Logger log = LoggerFactory.getLogger(ProxyManager.class);
	
	public ProxyManager() {
		setupProxy();
		
		App.prefs.addPrefsListener(this);
	}

	public void prefChanged(PrefsEvent e) {
		if(e.getPref().startsWith("tellervo.proxy."))
			setupProxy();
	}
	
	private void setupProxy() {
		String proxyType = App.prefs.getPref(PrefKey.PROXY_TYPE, "default");

		// no changes here? ignore it and move on
		// but not for manual - we might have changed some settings
		if(proxyType.equals(lastProxyType) && !"manual".equals(proxyType))
			return;

		// be safe and copy the string
		lastProxyType = new String(proxyType);
		
		log.info("Proxy type: {}", proxyType);
		
		// make sure we remove any properties we don't want
		if("default".equals(proxyType) || "direct".equals(proxyType)) {
			// remove properties...
			System.clearProperty("http.proxyHost");
			System.clearProperty("http.proxyPort");
			System.clearProperty("https.proxyHost");
			System.clearProperty("https.proxyHost");
		}
		
		if("default".equals(proxyType)) {
			System.setProperty("java.net.useSystemProxies", "true");
			return;
		}
		
		// ok then, no defaults
		System.setProperty("java.net.useSystemProxies", "false");
		
		// drop out here with nothing set if we're on "direct" mode
		if("direct".equals(proxyType))
			return;
				
		String host;
		if((host = App.prefs.getPref(PrefKey.PROXY_HTTP, (String)null)) != null) {
			System.setProperty("http.proxyHost", host);
			System.setProperty("http.proxyPort", App.prefs.getPref(PrefKey.PROXY_PORT_HTTP, "80"));

			log.info("PROXY: http: " + System.getProperty("http.proxyHost") + 
					":" + System.getProperty("http.proxyPort"));
		} 
		else {
			System.clearProperty("http.proxyHost");
			System.clearProperty("http.proxyPort");			
		}
		
		if((host = App.prefs.getPref(PrefKey.PROXY_HTTPS, (String)null)) != null) {
			System.setProperty("https.proxyHost", host);
			System.setProperty("https.proxyPort", App.prefs.getPref(PrefKey.PROXY_HTTPS, "443"));

			log.info("PROXY: https: " + System.getProperty("https.proxyHost") + 
					":" + System.getProperty("https.proxyPort"));
		} 
		else {
			System.clearProperty("https.proxyHost");
			System.clearProperty("https.proxyPort");			
		}
	}
}
