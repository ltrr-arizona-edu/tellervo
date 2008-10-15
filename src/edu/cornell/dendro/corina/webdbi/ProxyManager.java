package edu.cornell.dendro.corina.webdbi;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.PrefsEvent;
import edu.cornell.dendro.corina.prefs.PrefsListener;

/**
 * This class simply handles our proxy settings
 * 
 * It listens to prefs for any changes, and applies them immediately.
 * 
 * @author lucasm
 */

public class ProxyManager implements PrefsListener {
	private boolean usingProxy;
	
	public ProxyManager() {
		usingProxy = false;
		setupProxy();
		
		App.prefs.addPrefsListener(this);
	}

	public void prefChanged(PrefsEvent e) {
		if(e.getPref().startsWith("corina.proxy."))
			setupProxy();
	}
	
	private void setupProxy() {
		boolean nowUsingProxy = App.prefs.getBooleanPref("corina.proxy.enabled", false);
		boolean useSystemProxies = App.prefs.getBooleanPref("corina.proxies.useSystemDefault", true);
		
		// we were using a proxy, but we're not anymore
		if(usingProxy && !nowUsingProxy) {
			usingProxy = false;
			
			System.out.println("No longer using a proxy server, unsetting system properties");
			
			// remove properties...
			System.clearProperty("http.proxyHost");
			System.clearProperty("http.proxyPort");
			System.clearProperty("https.proxyHost");
			System.clearProperty("https.proxyHost");
			
			if(useSystemProxies) 
				System.setProperty("java.net.useSystemProxies", "true");
			else
				System.setProperty("java.net.useSystemProxies", "false");
		}
		else if(!usingProxy && !nowUsingProxy) {
			if(useSystemProxies)
				System.setProperty("java.net.useSystemProxies", "true");
			else
				System.setProperty("java.net.useSystemProxies", "false");
			return;
		}
		
		usingProxy = nowUsingProxy;
		
		System.out.println("Applying proxy settings");
		
		// no system proxies, we're specifying them!
		System.setProperty("java.net.useSystemProxies", "false");
		
		String host;
		if((host = App.prefs.getPref("corina.proxy.http", null)) != null) {
			System.setProperty("http.proxyHost", host);
			System.setProperty("http.proxyPort", App.prefs.getPref("corina.proxy.http_port", "80"));
		} 
		else {
			System.clearProperty("http.proxyHost");
			System.clearProperty("http.proxyPort");			
		}
		
		if((host = App.prefs.getPref("corina.proxy.https", null)) != null) {
			System.setProperty("https.proxyHost", host);
			System.setProperty("https.proxyPort", App.prefs.getPref("corina.proxy.https_port", "443"));
		} 
		else {
			System.clearProperty("https.proxyHost");
			System.clearProperty("https.proxyPort");			
		}
	}
}
