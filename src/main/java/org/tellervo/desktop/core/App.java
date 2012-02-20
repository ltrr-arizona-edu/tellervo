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
package org.tellervo.desktop.core;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.AppModel.NetworkStatus;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.Log4JViewer;
import org.tellervo.desktop.gui.LoginDialog;
import org.tellervo.desktop.gui.LoginSplash;
import org.tellervo.desktop.gui.ProgressMeter;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.nativelib.NativeLibWrapper;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.prefs.PreferencesDialog;
import org.tellervo.desktop.prefs.Prefs;
import org.tellervo.desktop.prefs.WizardDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.schema.WSIConfiguration;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.schema.WSISecurityUser;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.tridasv2.TridasObjectList;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.tellervo.TellervoWsiAccessor;
import org.tridas.io.TridasIO;

import com.dmurph.mvc.MVC;

import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;
import gov.nasa.worldwind.util.Logging;

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
  public static final String earliestServerVersionSupported = "1.0.5";

  public static final String SUN_JAVA_COMMAND = "sun.java.command";
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



public static synchronized void init() {
	
	App.platform = new Platform();
    App.platform.init();
    prefs = new Prefs();
    prefs.init();
	
    log.debug("initializing App");
    
    appmodel = new AppModel();
    
    logviewer = new Log4JViewer();
    logviewer.setVisible(false);
    
    SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
    
}


public static synchronized void init(ProgressMeter meter, LoginSplash splash) 
{
    App.init();
    
    // throwing an error instead of simply ignoring it
    // will point out bad design and/or bugs
	if (initialized) throw new IllegalStateException("AppContext already initialized.");
	
    if (meter != null) {
      meter.setMaximum(10);
      meter.setProgress(0);
    }

    if (meter != null) {
    	meter.setNote(I18n.getText("login.initJOGL"));
    	meter.setProgress(1);
    	NativeLibWrapper natives = new NativeLibWrapper();
    	try {
			//natives.init();
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

    prefsDialog = new PreferencesDialog();
    
    // If it's the first run then hide splash and show setup wizard
    if (isFirstRun) {
    	if(splash!=null)
    	{
    		splash.setVisible(false);
    	}
    	SetupWizard.launchWizard();
    }
    
    if(splash!=null)
    {
    	splash.setVisible(true);
    }
    
    // set up our proxies before we try to do anything online
    if (meter != null){
    	meter.setProgress(3);
    	meter.setNote(I18n.getText("login.setupProxy"));
    }
    setProxies(new ProxyManager());
        
    // load our JAXB xml binding context
    if (meter != null)
    {
    	meter.setProgress(4);
    	meter.setNote(I18n.getText("login.bindingSchemas"));
    	TellervoWsiAccessor.loadTellervoContext();
    }
       
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
        
        if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
        {
        	// Webservice is disabled so don't bother trying to log in
        }
        else
        {
	        // If we are not automatically logging in hide the splash
	        // because we are about to display a login dialog
	        if(!App.prefs.getBooleanPref(PrefKey.AUTO_LOGIN, false))
	        {
	        	splash.setVisible(false);
	        }
	        	
	        
	    	//splash.addLoginPanel(); ... in the future...
	    	LoginDialog dlg = new LoginDialog();
	    	
	    	try {
	    		// don't log in when in debug mode
	    		if(DEBUGGING)
	    			throw new UserCancelledException();
	    		dlg.doLogin(null, false);
	    		
	    		appmodel.setNetworkStatus(NetworkStatus.ONLINE);  		
	    		
	       	} catch (UserCancelledException uce) {
	    		// we default to not being logged in...
	    	}
	       	
			// Grab username either from prefs or directly from dialog
			if(App.prefs.getPref(PrefKey.PERSONAL_DETAILS_USERNAME, null)!=null)
			{
				username= App.prefs.getPref(PrefKey.PERSONAL_DETAILS_USERNAME, null);
			}
			else
			{
				username=dlg.getUsername().toString();
			}
        }

    }
    
    if(splash!=null)
    {
    	splash.setVisible(true);
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
    	
    	if(currentUser==null){
    		log.error("Current user is null");
    		
    		
    	}
    	else
    	{
    		try{    				  
				  for(String grpId : currentUser.getMemberOves())
				  {
					  if(grpId.equals("1")) isAdmin=true;
				  }
			    } catch (Exception e){  }
    	}
    	
    	
    	
    	
    	
    	/*List<?> dictionary = (List<?>) Dictionary.getDictionary("securityUserDictionary");
		List<WSISecurityUser> users = (List<WSISecurityUser>) ListUtil.subListOfType(dictionary, WSISecurityUser.class);
		
    	for(WSISecurityUser su: users){
    		try{
	    		if(su.getUsername().compareTo(username)==0) 
	    		{
	    			currentUser = su;
	    	    	// Set whether the current user is an administrator or not so that 
	    	    	// it is easy to disable options that will result in a 'no permissions' 
	    	    	// error
	    	    	 try{    				  
	    				  for(String grpId : su.getMemberOves())
	    				  {
	    					  if(grpId.equals("1")) isAdmin=true;
	    				  }
	    			    } catch (Exception e){  }
	    		}
    		} catch (NullPointerException e)
    		{
    			if(username==null) log.warn("username is null");
    			if(su==null) log.warn("su is null");
    			if(su.getUsername()==null) log.warn("su.getusername is null");
    		}
    	}*/
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
    String wsurl = App.prefs.getPref(PrefKey.WEBSERVICE_URL, "");
    try {
	
		String path = wsurl.substring(wsurl.indexOf("://")+3);
		path = path.substring(0, path.lastIndexOf("/")+1);
		
		domain = path;
	} catch (Exception e) {

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

    /**
     * Show the preferences dialog 
     */
	public static void showPreferencesDialog()
	{
		prefsDialog.setVisible(true);
	}
	
	/**
	 * Refresh the pages of the preferences dialog
	 */
	public static void refreshPreferencesDialog()
	{
		prefsDialog.refreshPages();
	}
	
	/**
	 * Run the setup wizard interface
	 */
	public static void runWizard()
	{
		new WizardDialog(prefsDialog);
	}
  	
	/**
	 * Restart Corina programmatically
	 * 
	 * @throws Exception
	 */
	public static void restartApplication() throws Exception
	{
	       try {
	            // java binary
	            String java = System.getProperty("java.home") + "/bin/java";
	            
	            // Add quotes if necessary
	            if(java.contains(" "))
	            {
	            	java = "\""+java+"\"";
	            }
	                    
	            // vm arguments
	            List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
	            StringBuffer vmArgsOneLine = new StringBuffer();
	            for (String arg : vmArguments) {
	                // if it's the agent argument : we ignore it otherwise the
	                // address of the old application and the new one will be in conflict
	                if (!arg.contains("-agentlib")) {
	                    vmArgsOneLine.append(arg);
	                    vmArgsOneLine.append(" ");
	                }
	            }
	            // init the command to execute, add the vm args
	            final StringBuffer cmd = new StringBuffer(java + " " + vmArgsOneLine);

	            // program main and program arguments
	            String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
	            String pathFile = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	            if (pathFile != null && pathFile.endsWith(".exe")) { // EXE wrapper
	                cmd.append("-jar " + new File(pathFile));
	            } else if (mainCommand != null && !mainCommand[0].isEmpty()) { // Hotspot VM implementation
	                // program main is a jar
	                if (mainCommand[0].endsWith(".jar")) {
	                    // if it's a jar, add -jar mainJar
	                    cmd.append("-jar " + new File(mainCommand[0]).getPath());
	                } else {
	                    // else it's a .class, add the classpath and mainClass
	                    cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
	                }
	                // finally add program arguments
	                for (int i = 1; i < mainCommand.length; i++) {
	                    cmd.append(" ");
	                    cmd.append(mainCommand[i]);
	                }
	            } else { // Non Hotspot VM implementation
	                cmd.append("-jar " + new File(pathFile));
	            }

	            // execute the command in a shutdown hook, to be sure that all the
	            // resources have been disposed before restarting the application
	            Runtime.getRuntime().addShutdownHook(new Thread() {

	                @Override
	                public void run() {
	                    try {
	                    	System.out.println("Run command: " +cmd.toString());
	                        Runtime.getRuntime().exec(cmd.toString());
	                    }
	                    catch (IOException e) {
	                    	log.error("Unable to restart application");
	                    	e.printStackTrace();
	                    } 
	                }
	            });
	           
	            System.exit(0);
	        }
	        catch (Exception e) {
	            // something went wrong
	            throw new Exception("Error while trying to restart the application", e);
	        }
			
	}

	public static void setProxies(ProxyManager proxies) {
		App.proxies = proxies;
	}

	public static ProxyManager getProxies() {
		return proxies;
	}

	public static String getLabAcronym(){
		
		try{
			ArrayList<WSIConfiguration> configDic = (ArrayList<WSIConfiguration>) App.dictionary.getDictionaryAsArrayList("configurationDictionary");
					
			for(WSIConfiguration conf : configDic )
			{
				if(conf.getKey().equals("lab.acronym")) return conf.getValue();
			}
		} catch (Exception e){ }
		
		log.error("Unable to determine lab acronym from dictionary");
		return "";
		
	}
	
	public static String getLabName(){
		
		try{
			ArrayList<WSIConfiguration> configDic = (ArrayList<WSIConfiguration>) App.dictionary.getDictionaryAsArrayList("configurationDictionary");
					
			for(WSIConfiguration conf : configDic )
			{
				if(conf.getKey().equals("lab.name")) return conf.getValue();
			}
		} catch (Exception e){ }
		
		log.error("Unable to determine lab name from dictionary");
		return "";
		
	}
	
	
	public static String getLabCodePrefix(){
		
		String acronym = App.getLabAcronym();
		
		if(acronym!=null)
		{
			if(acronym.length()>0) 
			{
				return acronym+"-";
			}
		}
		
		return "";
		
	}
}
