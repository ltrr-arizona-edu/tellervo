package edu.cornell.dendro.corina.editor;

import static org.mozilla.browser.XPCOMUtils.getService;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.mozilla.browser.MozillaPanel;
import org.mozilla.interfaces.nsICookieService;
import org.mozilla.interfaces.nsIIOService;
import org.mozilla.interfaces.nsIURI;

import edu.cornell.dendro.corina.tridasv2.MapLink;
import edu.cornell.dendro.corina.wsi.util.WSCookieStoreHandler;

public class EditorMozillaMapPanel extends MozillaPanel {
	private static final long serialVersionUID = 1L;

	public EditorMozillaMapPanel(MapLink link) {
		// hide our toolbar
		super(VisibilityMode.FORCED_HIDDEN, VisibilityMode.DEFAULT);

		// set our PHPSESSID cookie
		setCookies();
		
		// don't change our title!
		setUpdateTitle(false);

		// load the link
		load(link.getURI().toString());
	}
	
	/**
	 * Populate the mozilla browser with cookies from our webservice interface
	 */
	private void setCookies() {
		CookieStore cookieStore = WSCookieStoreHandler.getCookieStore().toCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
					
		// cookie service used for adding cookies (backdoor interface)
		nsICookieService cookieService;
		nsIIOService ioService;
		
		try {
			cookieService = getService("@mozilla.org/cookieService;1", nsICookieService.class);
			ioService = getService("@mozilla.org/network/io-service;1", nsIIOService.class);
		} 
		catch (Exception e) {
			System.err.println("Couldn't create mozilla object: " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		}

		// it's null??
		if(cookieService == null || ioService == null) {
			System.err.println("Couldn't create cookie manager/io service (it's just null...)");
			return;
		}
		
		for(Cookie cookie : cookies) {
			StringBuffer path = new StringBuffer();
			StringBuffer value = new StringBuffer();
			
			// end up with http[s]://domain/path
			path.append(cookie.isSecure() ? "https" : "http");
			path.append("://");
			path.append(cookie.getDomain());
			path.append(cookie.getPath());
			
			// end up with NAME=VALUE
			value.append(cookie.getName());
			value.append('=');
			value.append(cookie.getValue());
			
			// create an URI based on the given information
			// then, set a cookie...
			try {
				nsIURI uri = ioService.newURI(path.toString(), null, null);
				cookieService.setCookieString(uri, null, value.toString(), null);
			} catch (Exception e) {
				System.err.println("Couldn't create cookie: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
}
