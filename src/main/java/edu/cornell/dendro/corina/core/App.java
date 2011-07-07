/*******************************************************************************
 * Copyright (C) 2004-2011 Aaron Hamid and Peter Brewer.
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
 *     Aaron Hamid - Initial implementation
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.core;

import java.io.File;
import java.util.List;

import javax.media.opengl.GLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.TridasIO;

import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import edu.cornell.dendro.corina.core.AppModel.NetworkStatus;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.gui.Log4JViewer;
import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.LoginSplash;
import edu.cornell.dendro.corina.gui.ProgressMeter;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.logging.Logging;
import edu.cornell.dendro.corina.nativelib.NativeLibWrapper;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.PreferencesDialog;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.prefs.WizardDialog;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.tridasv2.TridasObjectList;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceEvent;
import edu.cornell.dendro.corina.wsi.ResourceEventListener;
import edu.cornell.dendro.corina.wsi.corina.CorinaWsiAccessor;

/**
 * Contextual state of the app; holds references to all "subsystems".
 * Originally was going to be named AppContext, but shortened because it
 * has to be referenced frequently.
 * @author Aaron Hamid
 */
public class App{
  
  /**
   * Most primitive server version supported by this client should be a three part string
   * e.g 1.1.1
   */
  public static final String earliestServerVersionSupported = "1.0.0";

	
  public static Prefs prefs;
  public static Platform platform;
  public static Logging logging;
  public static Dictionary dictionary;
  public static TridasObjectList tridasObjects;
  public static WSISecurityUser currentUser;
  public static Boolean isAdmin;
  public static String domain;
  private static String username;
  private static PreferencesDialog prefsDialog;
  public static AppModel appmodel;
  private static Log4JViewer logviewer;
  
  
  public static Boolean isFirstRun = false;
  
  private final static boolean DEBUGGING = false;

  //private static final CorinaLog log = new CorinaLog(App.class);
  private final static Logger log = LoggerFactory.getLogger(App.class);
  private static boolean initialized;
  
  private static ProxyManager proxies; // for handling our proxies


public static synchronized void init(ProgressMeter meter, LoginSplash splash) {
    // throwing an error instead of simply ignoring it
    // will point out bad design and/or bugs
	if (initialized) throw new IllegalStateException("AppContext already initialized.");
    log.debug("initializing App");

    appmodel = new AppModel();
    
    logviewer = new Log4JViewer();
    logviewer.setVisible(false);
    
    SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
    
    
    if (meter != null) {
      meter.setMaximum(10);
      meter.setProgress(0);
    }

    if (meter != null) {
    	meter.setNote(I18n.getText("login.initJOGL"));
    	meter.setProgress(1);
    	NativeLibWrapper natives = new NativeLibWrapper();
    	try {
			natives.init();
		} catch (GLException e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
    }
    
    
    
    // load properties -- messagedialog here is UGLY!
    if (meter != null) {
    	meter.setProgress(2);
      meter.setNote(I18n.getText("login.initPreferences"));
    }
    prefs = new Prefs();
    prefs.init();
    prefsDialog = new PreferencesDialog();
    
    if (isFirstRun) {
    	runWizard();
    }
    
    
    // set up our proxies before we try to do anything online
    if (meter != null){
    	meter.setProgress(3);
    	meter.setNote(I18n.getText("login.setupProxy"));
    }
    setProxies(new ProxyManager());
        
    // load our JAXB xml binding context
    if (meter != null)
    	meter.setProgress(4);
    	meter.setNote(I18n.getText("login.bindingSchemas"));
    CorinaWsiAccessor.loadCorinaContext();
       
    // load coordinate reference systems
    if(meter !=null){
    	meter.setProgress(5);
    	meter.setNote(I18n.getText("login.loadingCRS"));
    	TridasIO.initializeCRS();
    }
    
    // only do this if we can log in now...
    if (splash != null) {
        if (meter != null) {
        	meter.setProgress(6);
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

    }
    
    if (meter != null) {
    	meter.setProgress(7);
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
    	meter.setProgress(8);
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
    	meter.setProgress(9);
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
    
    // we're done here!
    if (meter != null) {
    	meter.setProgress(10);
    	meter.setNote("");
    }




    initialized = true;   
    
    // Get the domain of the WS used
    String wsurl = App.prefs.getPref("corina.webservice.url", "");
    try {
	
		String path = wsurl.substring(wsurl.indexOf("://")+3);
		path = path.substring(0, path.lastIndexOf("/")+1);
		
		domain = path;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		log.error("Error determining domain base from webservice URL");
		
	}
  
  }

  public static void setLogViewerVisible(Boolean b)
  {
	  logviewer.setVisible(b);
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
	
	public static void runWizard()
	{
		new WizardDialog(prefsDialog);
	}
  
	
	public static boolean restartApplication()
	{
	    String javaBin = System.getProperty("java.home") + "/bin/java";
	    File jarFile;
	    Sample sample = new Sample();
	    try{
	        jarFile = new File
	        (sample.getClass().getProtectionDomain()
	        .getCodeSource().getLocation().toURI());
	    } catch(Exception e) {
	        return false;
	    }

	    /* is it a jar file? */
	    if ( !jarFile.getName().endsWith(".jar") )
	    return false;   //no, it's a .class probably

	    String  toExec[] = new String[] { javaBin, "-jar", jarFile.getPath() };
	    try{
	        Runtime.getRuntime().exec( toExec );
	    } catch(Exception e) {
	        e.printStackTrace();
	        return false;
	    }

	    System.exit(0);

	    return true;
	}

	public static void setProxies(ProxyManager proxies) {
		App.proxies = proxies;
	}

	public static ProxyManager getProxies() {
		return proxies;
	}


}
