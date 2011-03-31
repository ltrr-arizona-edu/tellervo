// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.core;

import java.util.List;

import edu.cornell.dendro.corina.core.AppModel.NetworkStatus;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.LoginSplash;
import edu.cornell.dendro.corina.gui.ProgressMeter;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.logging.Logging;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.ModernPreferences;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.tridasv2.TridasObjectList;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.corina.CorinaWsiAccessor;

/**
 * Contextual state of the app; holds references to all "subsystems".
 * Originally was going to be named AppContext, but shortened because it
 * has to be referenced frequently.
 * @author Aaron Hamid
 */
public class App{
  public static Prefs prefs;
  public static Platform platform;
  public static Logging logging;
  public static Dictionary dictionary;
  public static TridasObjectList tridasObjects;
  public static WSISecurityUser currentUser;
  public static Boolean isAdmin;
  public static String domain;
  private static String username;
  private static ModernPreferences prefsDialog;
  public static AppModel appmodel;
  


  
  private final static boolean DEBUGGING = false;

  private static final CorinaLog log = new CorinaLog(App.class);
  private static boolean initialized;
  
  private static ProxyManager proxies; // for handling our proxies

  @SuppressWarnings("unchecked")
public static synchronized void init(ProgressMeter meter, LoginSplash splash) {
    // throwing an error instead of simply ignoring it
    // will point out bad design and/or bugs
	if (initialized) throw new IllegalStateException("AppContext already initialized.");
    log.debug("initializing App");

    appmodel = new AppModel();

    
    
    if (meter != null) {
      meter.setMaximum(9);
      meter.setProgress(2);
    }

    // load properties -- messagedialog here is UGLY!
    if (meter != null) {
      meter.setNote(I18n.getText("login.initPreferences"));
    }
    prefs = new Prefs();
    prefs.init();
    prefsDialog = new ModernPreferences();
    
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
    if (meter != null)
    	meter.setNote(I18n.getText("login.setupProxy"));
    proxies = new ProxyManager();
    if (meter != null) 
    	meter.setProgress(4);
    
    // load our JAXB xml binding context
    if (meter != null)
    	meter.setNote(I18n.getText("login.bindingSchemas"));
    CorinaWsiAccessor.loadCorinaContext();
    if (meter != null)
    	meter.setProgress(5);
        
    
    
    // only do this if we can log in now...
    if (splash != null) {
        if (meter != null) {
        	meter.setNote(I18n.getText("login.loggingIn"));
        }
        
    	//splash.addLoginPanel(); ... in the future...
    	LoginDialog dlg = new LoginDialog();
    	
    	try {
    		// don't log in when in debug mode
    		if(DEBUGGING)
    			throw new UserCancelledException();
    		dlg.doLogin(null, false);
    		username=dlg.getUsername().toString();
    		appmodel.setNetworkStatus(NetworkStatus.ONLINE);  		
    		
       	} catch (UserCancelledException uce) {
    		// we default to not being logged in...
    	}

    	if (meter != null) {
        	meter.setProgress(6);
        }
    }
    
    if (meter != null) {
    	meter.setNote(I18n.getText("login.initDictionary"));
    }
    dictionary = new Dictionary();
    if(splash != null && appmodel.isLoggedIn()) { // we have to be logged in for this...
        if (meter != null) {
        	meter.setNote(I18n.getText("login.updateDictionary"));
        }
        // don't update the dictionary in debug mode
        if(!DEBUGGING)
        	dictionary.query();
    }
    if (meter != null) {
    	meter.setProgress(7);
    }

    // Get the current users details from the dictionary
    isAdmin=false;
    if (appmodel.isLoggedIn()){
    	List<?> dictionary = (List<?>) Dictionary.getDictionary("securityUserDictionary");
		List<WSISecurityUser> users = (List<WSISecurityUser>) ListUtil.subListOfType(dictionary, WSISecurityUser.class);
		
    	for(WSISecurityUser su: users){
    		if(su.getUsername().compareTo(username)==0) 
    		{
    			currentUser = su;
    	    	// Set whether the current user is an administrator or not so that 
    	    	// it is easy to disable options that will result in a 'no permissions' 
    	    	// error
    	    	 try{    				  
    				  for(WSISecurityGroup grp : su.getMemberOf().getSecurityGroups())
    				  {
    					  if(grp.getId().equals("1")) isAdmin=true;
    				  }
    			    } catch (Exception e){  }
    		}
    	}
    }
    
    // Cache the TRiDaS objects 
    if (meter != null) {
    	meter.setNote(I18n.getText("login.initObjectList"));
    }
    tridasObjects = new TridasObjectList();
    if(splash != null && appmodel.isLoggedIn()) { // must be logged in...
    	// don't update the site list in debug mode...
    	if(!DEBUGGING)
    		tridasObjects.query();
        if (meter != null) {
        	meter.setNote(I18n.getText("login.updateObjectList"));
        }
    }
    if (meter != null) {
    	meter.setProgress(8);
    }

    // we're done here!
	if (meter != null)
		meter.setProgress(9);

    initialized = true;   
    
    // Get the domain of the WS used
    String wsurl = App.prefs.getPref("corina.webservice.url", "");
    try {
	
		String path = wsurl.substring(wsurl.indexOf("://")+3);
		path = path.substring(0, path.lastIndexOf("/")+1);
		
		domain = path;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("Error determining domain base from webservice URL");
		e.printStackTrace();
	}
  
  }

  public static boolean isLoggedIn(){
	  return appmodel.isLoggedIn();
  }
  
  public static boolean isInitialized() {
    return initialized;
  }

  public static synchronized void destroy(ProgressMeter meter) {
    // throwing an error instead of simply ignoring it
    // will point out bad design and/or bugs
    if (!initialized) throw new IllegalStateException("AppContext already destroyed.");
  }

	public static void showPreferencesDialog()
	{
		prefsDialog.setVisible(true);
	}
  

}