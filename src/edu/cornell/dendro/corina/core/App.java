// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.core;

import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.LoginSplash;
import edu.cornell.dendro.corina.gui.ProgressMeter;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.logging.Logging;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.site.SiteList;
import edu.cornell.dendro.corina.webdbi.Authenticate;
import edu.cornell.dendro.corina.webdbi.ProxyManager;
import edu.cornell.dendro.corina.dictionary.Dictionary;

/**
 * Contextual state of the app; holds references to all "subsystems".
 * Originally was going to be named AppContext, but shortened because it
 * has to be referenced frequently.
 * @author Aaron Hamid
 */
public class App {
  public static Prefs prefs;
  public static Platform platform;
  public static Logging logging;
  public static Dictionary dictionary;
  public static SiteList sites;
  
  private final static boolean DEBUGGING = false;

  private static final CorinaLog log = new CorinaLog(App.class);
  private static boolean initialized;
  
  private static ProxyManager proxies; // for handling our proxies

  public static synchronized void init(ProgressMeter meter, LoginSplash splash) {
    // throwing an error instead of simply ignoring it
    // will point out bad design and/or bugs
    if (initialized) throw new IllegalStateException("AppContext already initialized.");

    log.debug("initializing App");
    
    if (meter != null) {
      meter.setMaximum(7);
      meter.setProgress(2);
    }

    // <init prefs>
    //prefs = new Prefs();
    //prefs.init();

    // if the user hasn't specified a parser with
    // -Dorg.xml.sax.driver=..., use crimson.
    if (System.getProperty("org.xml.sax.driver") == null)
      System.setProperty("org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl");
    // xerces: "org.apache.xerces.parsers.SAXParser"
    // gnu/jaxp: "gnu.xml.aelfred2.SAXDriver"

    // migrate old prefs (!!!)
    // WAS: Migrate.migrate();

    // load properties -- messagedialog here is UGLY!
    if (meter != null) {
      meter.setNote("Initializing Preferences...");
    }
    prefs = new Prefs();
    prefs.init();
    if (meter != null) {
      meter.setProgress(3);
    }
    /*try {
      //Prefs.init();
    } catch (IOException ioe) {
      JOptionPane.showMessageDialog(null,
          "While trying to load preferences:\n" + ioe.getMessage(),
          "Corina: Error Loading Preferences", JOptionPane.ERROR_MESSAGE);
    }*/
    
    // set up our proxies before we try to do anything online
    proxies = new ProxyManager();
    
    boolean isLoggedIn = false;
    
    // only do this if we can log in now...
    if (splash != null) {
        if (meter != null) {
        	meter.setNote("Logging in...");
        }
        
    	//splash.addLoginPanel(); ... in the future...
    	LoginDialog dlg = new LoginDialog();
    	
    	try {
    		// don't log in when in debug mode
    		if(DEBUGGING)
    			throw new UserCancelledException();
    		dlg.doLogin(null, false);
    		isLoggedIn = true;
       	} catch (UserCancelledException uce) {
    		// we default to not being logged in...
    	}

    	if (meter != null) {
        	meter.setProgress(4);
        }
    }
    
    if (meter != null) {
    	meter.setNote("Initializing Dictionary...");
    }
    dictionary = new Dictionary();
    if(splash != null && isLoggedIn) { // we have to be logged in for this...
        if (meter != null) {
        	meter.setNote("Updating Dictionary...");
        }
        // don't update the dictionary in debug mode
        if(!DEBUGGING)
        	dictionary.query();
    }
    if (meter != null) {
    	meter.setProgress(5);
    }

    if (meter != null) {
    	meter.setNote("Initializing Site List...");
    }
    sites = new SiteList();
    if(splash != null && isLoggedIn) { // must be logged in...
    	// don't update the site list in debug mode...
    	if(!DEBUGGING)
    		sites.query();
        if (meter != null) {
        	meter.setNote("Updating Site List...");
        }
    }
    if (meter != null) {
    	meter.setProgress(6);
    }

    // we're done here!
	if (meter != null)
		meter.setProgress(7);

    initialized = true;   
  }

  public static boolean isInitialized() {
    return initialized;
  }

  public static synchronized void destroy(ProgressMeter meter) {
    // throwing an error instead of simply ignoring it
    // will point out bad design and/or bugs
    if (!initialized) throw new IllegalStateException("AppContext already destroyed.");
  }
}