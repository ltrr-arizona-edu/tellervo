package edu.cornell.dendro.corina.editor;

import java.net.URI;
import java.net.URISyntaxException;

import org.mozilla.browser.MozillaPanel;
import org.mozilla.browser.IMozillaWindow.VisibilityMode;
import org.mozilla.interfaces.nsIIOService;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.webdbi.MapLink;

import static org.mozilla.browser.XPCOMUtils.getService;

import org.mozilla.interfaces.nsIIOService;
import org.mozilla.interfaces.nsICookieService;
import org.mozilla.interfaces.nsIURI;

public class EditorMozillaMapPanel extends MozillaPanel {
	public EditorMozillaMapPanel(MapLink link) {
		// hide our toolbar
		super(true, VisibilityMode.FORCED_HIDDEN, VisibilityMode.DEFAULT);

		// set our PHPSESSID cookie
		setCookie(link);
		
		// don't change our title!
		setUpdateTitle(false);

		load(link.getMapLinkURL());
	}
	
	public void setCookie(MapLink link) {
		String cookie = App.prefs.getPref("corina.webservice.cookie");
		
		// no cookie? bail :)
		if(cookie == null)
			return;
		
		URI uri;
		try {
			uri = new URI(link.getMapLinkURL());
		} catch (URISyntaxException use) {
			System.out.println("Error setting cookie! " + use);
			return;
		}

		// https://xxxx.com/blah/something.php -> https://xxxx.com/
		String cookiebase = uri.getScheme() + "://" + uri.getAuthority() + "/";
		
		// call the weird internal mozilla services to set our cookie...
		nsIIOService iosvc = getService("@mozilla.org/network/io-service;1", nsIIOService.class);
		nsIURI nsuri = iosvc.newURI(cookiebase, null, null);
		nsICookieService cookieserv = getService("@mozilla.org/cookieService;1", nsICookieService.class);
		
		// woot!
		cookieserv.setCookieString(nsuri, null, cookie, null);
	}
}
