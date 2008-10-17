package edu.cornell.dendro.corina.core;

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
	private String lastProxyType;
	
	public ProxyManager() {
		setupProxy();
		
		App.prefs.addPrefsListener(this);
	}

	public void prefChanged(PrefsEvent e) {
		if(e.getPref().startsWith("corina.proxy."))
			setupProxy();
	}
	
	private void setupProxy() {
		String proxyType = App.prefs.getPref("corina.proxy.type", "default");

		// no changes here? ignore it and move on
		// but not for manual - we might have changed some settings
		if(proxyType.equals(lastProxyType) && !"manual".equals(proxyType))
			return;

		// be safe and copy the string
		lastProxyType = new String(proxyType);
		
		System.out.println("PROXY: Proxy type: " + proxyType);
		
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
		if((host = App.prefs.getPref("corina.proxy.http", null)) != null) {
			System.setProperty("http.proxyHost", host);
			System.setProperty("http.proxyPort", App.prefs.getPref("corina.proxy.http_port", "80"));

			System.out.println("PROXY: http: " + System.getProperty("http.proxyHost") + 
					":" + System.getProperty("http.proxyPort"));
		} 
		else {
			System.clearProperty("http.proxyHost");
			System.clearProperty("http.proxyPort");			
		}
		
		if((host = App.prefs.getPref("corina.proxy.https", null)) != null) {
			System.setProperty("https.proxyHost", host);
			System.setProperty("https.proxyPort", App.prefs.getPref("corina.proxy.https_port", "443"));

			System.out.println("PROXY: https: " + System.getProperty("https.proxyHost") + 
					":" + System.getProperty("https.proxyPort"));
		} 
		else {
			System.clearProperty("https.proxyHost");
			System.clearProperty("https.proxyPort");			
		}
	}
}
