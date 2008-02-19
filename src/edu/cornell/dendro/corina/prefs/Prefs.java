// Copyright (c) 2001 Ken Harris <kbh7@cornell.edu>
// Copyright (c) 2004-2005 Aaron Hamid
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.prefs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import edu.cornell.dendro.corina.core.AbstractSubsystem;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.JDisclosureTriangle;
import edu.cornell.dendro.corina.util.WeakEventListenerList;

/**
 * Storage and access of user preferences.
 * 
 * <h2>Left to do</h2>
 * <ul>
 * <li>add getFont(), setColor(), etc. convenience methods
 * <li>get rid of JDisclosureTriangle usage (and then delete it)
 * <li>get rid of "system" preferences (and file Source/prefs.properties) - these should be inline, in each subsystem, with the prefs call
 * <li>extract prefs file location to platform?
 * <li>(goal: 300 lines for everything, maybe 350)
 * </ul>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at </i> cornell <i style="color: gray">dot </i> edu&gt;
 * @version $Id$
 */
public class Prefs extends AbstractSubsystem {
  public static final String EDIT_FOREGROUND = "corina.edit.foreground";
  public static final String EDIT_BACKGROUND = "corina.edit.background";
  public static final String EDIT_FONT = "corina.edit.font";
  public static final String EDIT_GRIDLINES = "corina.edit.gridlines";
  public static final String GRID_HIGHLIGHT = "corina.grid.highlight";
  public static final String GRID_HIGHLIGHTCOLOR = "corina.grid.hightlightcolor";

  private static final CorinaLog log = new CorinaLog("Prefs");

  /**
   * if true, silently ignore if the prefs can't be saved.
   * protected to avoid synthetic accessor
   */
  protected static boolean dontWarn = false;
  
  /*
   * OLD: ~/xcorina/prefs.properties [win32] ~/.corina/prefs.properties [unix] ~/Library/Corina/prefs.properties [mac]
   * 
   * NEW: ~/Corina Preferences [win32] ~/.corina [unix] ~/Library/Preferences/Corina Preferences [mac]
   */
  private String CORINADIR;
  private String FILENAME;
  private String MACHINEFILENAME;
  /**
   * Our internal Properties object in which to save preferences
   */
  private Properties prefs;

  /**
   * A copy of the default UIDefaults object available at startup. We cache these defaults to allow the user to "reset" any changes they may have made through the Appearance Panel.
   */
  private final Hashtable UIDEFAULTS = new Hashtable(); //(Hashtable) UIManager.getDefaults().clone();

  public String getName() {
    return "Preferences";
  }
  
  // returns the directory for storing Corina-related files...
  public String getCorinaDir() {
	  return CORINADIR;
  }

  /**
   * Initializes the preferences system. This should be called upon startup.
   */
  @Override
public void init() {
    super.init();

    // NOTE: Platform should have ensured user.home is legit at this point
    // [ don't have to worry now that platform is explicitly initialized before
    // prefs, so we don't have these race conditions.
    // delete this comment at some point -aaron ]
    String home = System.getProperty("user.home");
    if (!home.endsWith(File.separator))
      home = home + File.separator;

    if (App.platform.isWindows()) {
    	String basedir = home;
    	
    	// old location for corina preferences exists; migrate it over in the end.    	
    	File oldprefs = new File(home + "Corina Preferences");
    	    	
    	// if the Application Data directory exists, use it as our base.
    	if(new File(home + "Application Data").isDirectory()) 
    		basedir = home + "Application Data" + File.separator;    			
    
    	// ~/Application Data/Corina/
    	// OR ~/Corina/, if no Application Data
    	basedir += "Corina" + File.separator;
    	
    	// if the Corina directory doesn't exist, make it.
    	File corinadir = new File(basedir);
    	if(!corinadir.exists())
    		corinadir.mkdir();
    	
    	CORINADIR = basedir;
    	FILENAME = basedir + "Corina.pref";
    	MACHINEFILENAME = "C:\\Corina System Preferences";

    	// migrate over the old preferences, if they exist.
    	if(oldprefs.exists()) {
    		oldprefs.renameTo(new File(FILENAME));
    	}
    }
    else if (App.platform.isMac()) {
      CORINADIR = home + "Library/Corina/";
      FILENAME = home + "Library/Corina/Preferences"; // why in prefs? isn't lib ok?
      MACHINEFILENAME = "/Library/Preferences/Corina System Preferences";
      
      File basedir = new File(CORINADIR);
      if(!basedir.exists())
    	  basedir.mkdirs();
    }
    else {
      // plain ol' unix
      CORINADIR = home + ".corina";    	
      FILENAME = home + ".corina/.preferences";
      MACHINEFILENAME = "/etc/corina_system_preferences";

      File basedir = new File(CORINADIR);
      if(!basedir.exists())
    	  basedir.mkdirs();
      
    }

    initUIDefaults();

    // proceed with loading the preferences
    try {
      load();
    } catch (IOException ioe) {
      new Bug(ioe);
    }

    setInitialized(true);
  }

  @Override
public void destroy() {
    // don't need to prevent double destroys, just resave
    if (!initialized) log.debug("being destroyed more than once!");
    save();
  }

  @Override
public void finalize() throws Throwable {
    save();
    super.finalize();
  }

  public Hashtable getUIDefaults() {
    return UIDEFAULTS;
  }

  private void initUIDefaults() {
    //UIDEFAULTS.putAll(UIManager.getDefaults());
    UIDefaults defaults = UIManager.getDefaults();
    // XXX: Even though Hashtable implements Map since
    // Java 1.2, UIDefaults is "special"... the Iterator
    // returned by UIDefaults keySet object does not return any
    // keys, and therefore Enumeration must be used instead - aaron
    Enumeration e = defaults.keys();
    
    while (e.hasMoreElements()) {
      Object key = e.nextElement();
      Object value = defaults.get(key);
      
      // for some reason, java 1.6 seems to have some null values.
      if(value == null)
    	  continue;      
      
      //log.debug("Saving UIDefault: " + key);
      UIDEFAULTS.put(key, value);
    }
  }

  /**
   * Load system and user properties (preferences). This runs 3 steps:
   * <ol>
   * <li>Load system properties
   * <li>Load default properties from class loader resource prefs.properties
   * <li>Override with user's properties file (differs by OS; FIXME?)
   * </ol>
   * 
   * TODO: System properties should really override the user properties (as they are more "immediate"), but we'd have to make sure to only save the corina properties back out. I /think/ all corina
   * properties start with 'corina' but I'm not sure yet.
   */
  private synchronized void load() throws IOException {
    // get existing properties
    Properties systemprops = System.getProperties();
    log.debug("Loading preferences");

    // a place to record errors that may occur, because we don't
    // want to crap out before we've tried everything.
    StringBuffer errors = new StringBuffer();

    Properties defaults = new Properties(systemprops);
    // load system properties as a resource from this jar
    ClassLoader cl = Prefs.class.getClassLoader();
    try {
      java.io.InputStream is = cl.getResourceAsStream("edu/cornell/dendro/corina_resources/prefs.properties");
      if (is != null) {
        try {
          defaults.load(is);
        } finally {
          is.close();
        }
      }
      // RENAME this? ("Default Corina Preferences")
    } catch (IOException ioe) {
      errors.append("Error loading Corina's default preferences (bug!).\n");
    }

    // load machine properties
    try {
    	defaults.load(new FileInputStream(MACHINEFILENAME));
    } catch (IOException ioe) {
    	// ignore this; no machine properties is fine.
    }
    
    // instantiate our properties using the system properties
    // and corina properties as default values
    prefs = new Properties(defaults);

    try {
      prefs.load(new FileInputStream(FILENAME));
    } catch (FileNotFoundException fnfe) {
      // user doesn't have a properties file, so we'll give her one!
      try {
        // this is the guts of save(), but without the nice error handling.
        prefs.store(new FileOutputStream(FILENAME), "Corina user preferences");
      } catch (IOException ioe) {
        errors.append("Error copying preferences file to your home directory: " + ioe.getMessage() + "\n");
      }
    } catch (IOException ioe) {
      errors.append("Error loading user preferences file: " + ioe.getMessage() + "\n");
    }

    // install any UIDefaults preferences the user may have
    installUIDefaultsPrefs();

    // if there was an exception thrown-and-caught, re-throw it now
    if (errors.length() != 0)
      throw new IOException(errors.toString());
  }

  /**
   * Loads any saved uidefaults preferences and installs them
   */
  private synchronized void installUIDefaultsPrefs() {
    Iterator it = prefs.entrySet().iterator();
    UIDefaults uidefaults = UIManager.getDefaults();
    log.debug("iterating prefs");
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      String prefskey = entry.getKey().toString();
      if (!prefskey.startsWith("uidefaults.")
          || prefskey.length() <= "uidefaults.".length())
        continue;
      String uikey = prefskey.substring("uidefaults.".length());
      Object object = uidefaults.get(uikey);
      log.debug("prefs property " + uikey + " " + object);
      installUIDefault(object.getClass(), prefskey, uikey);
    }
  }

  private void installUIDefault(Class type, String prefskey, String uikey) {
    Object decoded = null;
    String pref = prefs.getProperty(prefskey);
    if (pref == null) {
      log.warn("Preference '" + prefskey + "' held null value.");
      return;
    }
    if (Color.class.isAssignableFrom(type)) {
      decoded = Color.decode(pref);
    } else if (Font.class.isAssignableFrom(type)) {
      decoded = Font.decode(pref);
    } else {
      log.warn("Unsupported UIDefault preference type: " + type);
      return;
    }

    if (decoded == null) {
      log.warn("UIDefaults color preference '" + prefskey + "' was not decodable.");
      return;
    }

    UIDefaults uidefaults = UIManager.getDefaults();
    //if (uidefaults.contains(property)) {
    // NOTE: ok, UIDefaults object is strange. Not only does
    // it not implement the Map interface correctly, but entries
    // will not "stick". The entries must be first explicitly
    // removed, and then re-added - aaron
    log.debug("Removing UIDefaults key before overwriting: " + uikey);
    uidefaults.remove(uikey);
    //}

    if (Color.class.isAssignableFrom(type)) {
      uidefaults.put(uikey, new ColorUIResource((Color) decoded));
    } else {
      uidefaults.put(uikey, new FontUIResource((Font) decoded));
    }
  }

  /*
   * XXX: maybe expose this later if/when we have a component/module/subsystem model public static void destroy() { save(); }
   */

  /**
   * Save current properties to the user's system-writable properties (preferences) file, <code>FILENAME</code>.
   */
  public synchronized void save() {
    log.debug("Saving preferences...");

    // try to save prefs, and on failure present "try again?" dialog.
    while (true) {
      try {
        // -- p.store() buffers internally, so if i used a buffered stream
        // here it would only hurt performance.
        // -- the second string passed to store() is a comment line which
        // is added to the top of the file.
        prefs.store(new FileOutputStream(FILENAME), "Corina user preferences");
        return;
      } catch (IOException ioe) {
        if (dontWarn)
          return;

        boolean tryAgain = cantSave(ioe);
        if (!tryAgain)
          return;
      }
    }
  }

  // exception |e| thrown while saving; tell user.
  // return value: true => "try again".
  // TODO: use jlinedlabel to be more explicit about the problem
  // TODO: (need left-alignment option on that class, first)
  private static boolean cantSave(Exception e) {
    JPanel message = new JPanel(new BorderLayout(0, 8)); // (hgap,vgap)
    message.add(new JLabel(I18n.getText("prefs_cant_save")), BorderLayout.NORTH);

    // -- dialog with optionpane (warning?)
    JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
    JDialog dialog = optionPane.createDialog(null /* ? */, I18n.getText("prefs_cant_save_title"));

    // -- buttons: cancel, try again.
    optionPane.setOptions(new String[] { I18n.getText("try_again"), I18n.getText("cancel") });

    // -- disclosure triangle with scrollable text area: click for details... (stacktrace)
    JComponent stackTrace = new JScrollPane(new JTextArea(Bug.getStackTrace(e), 10, 60));
    JDisclosureTriangle v = new JDisclosureTriangle(I18n.getText("click_for_details"), stackTrace, false);
    message.add(v, BorderLayout.CENTER);

    // -- checkbox: don't warn me again
    JCheckBox dontWarnCheckbox = new JCheckBox(I18n.getText("dont_warn_again"), false);
    dontWarnCheckbox.addActionListener(new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        dontWarn = !dontWarn;
      }
    });

    // FIXME: consolidate |message| panel construction with Layout methods
    message.add(dontWarnCheckbox, BorderLayout.SOUTH);

    // show dialog
    dialog.pack();
    dialog.setResizable(false);
    dialog.show();

    // return true iff "try again" is clicked
    return optionPane.getValue().equals(I18n.getText("try_again"));
  }

  // --------------------------------------------------
  // new prefs api below here

  /*
   * TODO:
   * -- set/get any data type
   * -- automatically save
   * -- (but not right away, which would be slow)
   * -- defaults
   * -- required?
   * -- convenience functions for non-atomic things like window geometries?
   * -- also, lists of data
   * -- prefs event
   * -- prefs listener
   * -- on prefs.set, fire events
   * -- get rid of all refreshFromPrefs() methods, HasPreferences interface
   * -- (register listener with type, like jrendezvous?)
   */

  public Properties getPrefs() {
    return prefs;
  }

  // just wrappers, for now
  public void setPref(String pref, String value) {
    //System.setProperty(pref, value);
    prefs.setProperty(pref, value);
    save();
    firePrefChanged(pref);
  }

  // TODO: require default?
  public String getPref(String pref) {
    return prefs.getProperty(pref);
  }

  public String getPref(String pref, String deflt) {
    String value = prefs.getProperty(pref);
    if (value == null)
      value = deflt;
    return value;
  }

  public Dimension getDimensionPref(String pref, Dimension deflt) {
    String value = prefs.getProperty(pref);
    if (value == null)
      return deflt;
    StringTokenizer st = new StringTokenizer(value, ",");
    Dimension d = new Dimension(deflt);
    if (st.hasMoreTokens()) {
      String s = st.nextToken();
      try {
        int i = Integer.parseInt(s);
        if (i > 0)
          d.width = i;
      } catch (NumberFormatException nfe) {
        log.warn("Invalid dimension width: " + s);
      }
    }
    if (st.hasMoreTokens()) {
      String s = st.nextToken();
      try {
        int i = Integer.parseInt(s);
        if (i > 0)
          d.height = i;
      } catch (NumberFormatException nfe) {
        log.warn("Invalid dimension height: " + s);
      }
    }
    return d;
  }

  public int getIntPref(String pref, int deflt) {
    String value = prefs.getProperty(pref);
    if (value == null)
      return deflt;
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException nfe) {
      log.warn("Invalid integer for preference '" + pref + "': " + value);
      return deflt;
    }
  }

  public Color getColorPref(String pref, Color deflt) {
    String value = prefs.getProperty(pref);
    if (value == null)
      return deflt;
    try {
      return Color.decode(value);
    } catch (NumberFormatException nfe) {
      log.warn("Invalid color for preference '" + pref + "': " + value);
      return deflt;
    }
  }

  public Font getFontPref(String pref, Font deflt) {
    String value = prefs.getProperty(pref);
    if (value == null)
      return deflt;
    return Font.decode(value);
  }

  public void removePref(String pref) {
    prefs.remove(pref);
    save();
    firePrefChanged(pref);
  }

  private WeakEventListenerList listeners = new WeakEventListenerList();

  // IDEA: addPrefsListener(l, String pref)?
  public void addPrefsListener(PrefsListener l) {
	  listeners.add(PrefsListener.class, l);
  }

  public void removePrefsListener(PrefsListener l) {
    listeners.remove(PrefsListener.class, l);
  }

  public void firePrefChanged(String pref) {
    // alert all listeners
	Object[] l = listeners.getListenerList();
		
    PrefsEvent e = new PrefsEvent(Prefs.class, pref);
    
	for(int i = 0; i < l.length; i += 2) {
		if(l[i] == PrefsListener.class)
			((PrefsListener)l[i+1]).prefChanged(e);
	}
  }
  /*
    PrefsListener[] l;
    synchronized (Prefs.class) {
      l = (PrefsListener[]) listeners.toArray(new PrefsListener[listeners.size()]);
    }

    int size = l.length;

    if (size == 0)
      return;

    PrefsEvent e = new PrefsEvent(Prefs.class, pref);

    for (int i = 0; i < size; i++) {
      l[i].prefChanged(e);
    }
  }
  */
}