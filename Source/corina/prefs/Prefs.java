//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.prefs;

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
import java.util.Vector;

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

import corina.gui.Bug;
import corina.ui.I18n;
import corina.util.CorinaLog;
import corina.util.JDisclosureTriangle;
import corina.util.Platform;

/**
 * Storage and access of user preferences.
 *
 * <h2>Left to do</h2>
 * <ul>
 *   <li>switch from System.getProperty() to my own Properties object
 *      (save all, since i'll have that hash table to myself)
 *   <li>add getFont(), setColor(), etc. convenience methods
 *   <li>get rid of JDisclosureTriangle usage (and then delete it)
 *   <li>get rid of "system" preferences (and file Source/prefs.properties) -
 *       these should be inline, in each subsystem, with the prefs call
 *   <li>extract prefs file location to platform?
 *   <li>(goal: 300 lines for everything, maybe 350)
 * </ul>
 *
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 * @version $Id$
 */
public class Prefs {
  public static final String EDIT_FOREGROUND = "corina.edit.foreground";
  public static final String EDIT_BACKGROUND = "corina.edit.background";
  public static final String EDIT_FONT = "corina.edit.font";
  public static final String EDIT_GRIDLINES = "corina.edit.gridlines";
  public static final String GRID_HIGHLIGHT = "corina.grid.highlight";
  public static final String GRID_HIGHLIGHTCOLOR = "corina.grid.hightlightcolor";
  
  private static final CorinaLog log = new CorinaLog("Prefs");
  /*
    OLD:
    ~/xcorina/prefs.properties [win32]
    ~/.corina/prefs.properties [unix]
    ~/Library/Corina/prefs.properties [mac]

    NEW:
    ~/Corina Preferences [win32]
    ~/.corina [unix]
    ~/Library/Preferences/Corina Preferences [mac]
  */

  private final static String FILENAME;
  static {
    // NOTE: Platform should have ensured user.home is legit at this point 
  	String home = System.getProperty("user.home");
  	if (!home.endsWith(File.separator))
  	  home = home + File.separator;
  
  	if (Platform.isWindows)
  	  FILENAME = home + "Corina Preferences";
  	else if (Platform.isMac)
  	  FILENAME = home + "Library/Preferences/Corina Preferences"; // why in prefs?  isn't lib ok?
  	else // plain ol' unix
  	  FILENAME = home + ".corina";
  }
  
  /**
   * Our internal Properties object in which to save preferences
   */
  private static Properties prefs;
  
  /**
   * A copy of the default UIDefaults object available at startup.
   * We cache these defaults to allow the user to "reset" any changes
   * they may have made through the Appearance Panel.
   */
  public static final Hashtable UIDEFAULTS = new Hashtable(); //(Hashtable) UIManager.getDefaults().clone();

  /**
   * Initializes the preferences system.  This should be called upon
   * startup.
   * NOTE: it may be desirable to refactor these "subsystems"
   * into a "module" or "component" interface, so they are all initialized
   * and destroyed in a similar manner at well-defined times.  For now
   * that would introduce too many changes. - aaron
   */
  public static void init() throws IOException {
    //UIDEFAULTS.putAll(UIManager.getDefaults());
    UIDefaults defaults = UIManager.getDefaults();
    // XXX: Even though Hashtable implements Map since
    // Java 1.2, UIDefaults is "special"... the Iterator
    // returned by UIDefaults keySet object does not return any
    // keys, and therefore Enumeration must be used instead - aaron
    Enumeration e = defaults.keys();
    while (e.hasMoreElements()) {
      Object key = e.nextElement();
      //log.debug("Saving UIDefault: " + key);
      UIDEFAULTS.put(key, defaults.get(key));
    }
   
    // proceed with loading the preferences 
    load();
  }

  /**
   * Load system and user properties (preferences).  This runs 2 steps:
   * <ol>
   *   <li>Load system properties, by loading ...
   *   <li>Load user's properties, by loading ...
   * </ol>
   *
	 * Note that this uses a Properties object, gotten from
	 * System.getProperties(), to load the properties into.  This is
	 * because Unix and Win32 behavior differs: on one,
	 * System.setProperties(p) adds the properties in p to the
	 * existing properties, while the other replaces the existing
	 * properties with p.  (I don't remember offhand which is which.)
	 * As usual, the API docs don't really specify.  Grrr...
   */
  public static synchronized void load() throws IOException {
    // get existing properties
    Properties systemprops = System.getProperties();
    log.debug("Loading preferences");

    // a place to record errors that may occur, because we don't
    // want to crap out before we've tried everything.
    String errors="";

    Properties defaults = new Properties(systemprops);
    // load system properties as a resource from this jar
    try {
      ClassLoader cl = Prefs.class.getClassLoader();
      java.io.InputStream is = cl.getResourceAsStream("prefs.properties");
      if (is != null) {
        try {
          defaults.load(is);
        } finally {
          is.close();
        }
      }
      // RENAME this?  ("Default Corina Preferences")
    } catch (IOException ioe) {
        errors += "Error loading Corina's default preferences (bug!).";
    }

    // instantiate our properties using the system properties
    // and corina properties as default values
    prefs = new Properties(defaults);

    // get user properties (with a hack to preserve user.name)
    try {
	    // trying to get rid of this username nonsense - Aaron
      //String n = defaults.getProperty("user.name"); // HACK!!!
      
      prefs.load(new FileInputStream(FILENAME));
      // xxx aaron defaults.setProperty("user.name", n);
    } catch (FileNotFoundException fnfe) {
      // user doesn't have a properties file, so we'll give her
      // one!  the system properties were already loaded, so all
      // i need to do is call save() now.

      // (p->system properties, for save())
      // trying to get rid of dependence on System properties xxx aaron System.setProperties(p);

      try {
    		// this is the guts of save(), but without the nice error handling.
        // XXX: well, PrefsTemplate has a comment saying it is useless and should
        // be deleted ASAP... futhermore it looks like its options and categories
        // arrays are never filled... therefore, hopefully I can conclude this is all
        // unnecessary and will wait for things to break to prove to me otherwise - aaron
    		//Properties pp = getCorinaProperties();
        // Contract of Properties indicates that only the immediate properties, not
        // the parent properties, are written out during 'store'.  Convenient, eh?
    		prefs.store(new FileOutputStream(FILENAME), "Corina user preferences");
      } catch (IOException ioe) {
        errors += "Error copying preferences file to your home directory: " +
                    ioe.getMessage();
      }
    } catch (IOException ioe) {
      errors += "Error loading user preferences file: " + ioe.getMessage();
    }

    // install any UIDefaults preferences the user may have
    loadUIDefaults();
    // set properties
    // xxx no thank you - aaron System.setProperties(p);
       

    // if there was an exception thrown-and-caught, re-throw it now
    if (errors.length() != 0)
        throw new IOException(errors);
  }
  
  /**
   * Loads any saved uidefaults preferences and installs them
   */
  private static synchronized void loadUIDefaults() {
   Iterator it = prefs.entrySet().iterator();
   UIDefaults uidefaults = UIManager.getDefaults();
   log.debug("iterating prefs");
   while (it.hasNext()) {
     Map.Entry entry = (Map.Entry) it.next();
     String prefskey = entry.getKey().toString();
     if (!prefskey.startsWith("uidefaults.") ||
          prefskey.length() <= "uidefaults.".length()) continue;
     String uikey = prefskey.substring("uidefaults.".length());
     Object object = uidefaults.get(uikey);
     log.debug("prefs property " + uikey + " " + object);
     installUIDefault(object.getClass(), prefskey, uikey);
   }
  }

  private static void installUIDefault(Class type, String prefskey, String uikey) {
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
      log.warn("UIDefaults color preference '" + prefskey  + "' was not decodable.");
      return;
    }

    UIDefaults uidefaults = UIManager.getDefaults();
    //if (uidefaults.contains(property)) {
      // NOTE: ok, UIDefaults object is strange.  Not only does
      // it not implement the Map interface correctly, but entries
      // will not "stick".  The entries must be first explicitly
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
   XXX: maybe expose this later if/when we have a component/module/subsystem model 
   public static void destroy() {
    save();
  }*/

  /**
   * Save current properties to the user's system-writable
	 * properties (preferences) file,
	 * <code>FILENAME</code>.
   */
  public static synchronized void save() {
    //log.debug("Saving preferences...");
    CorinaLog.realErr.println("Saving preferences...");
  	// get corina prefs
  	//Properties p = getCorinaProperties();
  
  	// try to save prefs, and on failure present "try again?" dialog.
    for (;;) {
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

  // if true, silently ignore if the prefs can't be saved.
  private static boolean dontWarn = false;

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
  	JDisclosureTriangle v = new JDisclosureTriangle(I18n.getText("click_for_details"),
  							stackTrace, false);
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
    TODO:
    -- set/get any data type
    -- automatically save
    -- (but not right away, which would be slow)
    -- defaults -- required?
    -- convenience functions for non-atomic things like window geometries?
    -- also, lists of data
    -- prefs event
    -- prefs listener
    -- on prefs.set, fire events
    -- get rid of all refreshFromPrefs() methods, HasPreferences interface
    -- (register listener with type, like jrendezvous?)
  */

  public static Properties getPrefs() {
    return prefs;
  }

  // just wrappers, for now
  public static void setPref(String pref, String value) {
  	//System.setProperty(pref, value);
    prefs.setProperty(pref, value);
    save();
    firePrefChanged(pref);
  }
  
  public static String getPref(String pref) { // TODO: require default?
    //return System.getProperty(pref);
    return prefs.getProperty(pref);
  }
  
  public static String getPref(String pref, String deflt) {
    String value = prefs.getProperty(pref);
    if (value == null) value = deflt;
    return value;
  }

  public static Dimension getDimensionPref(String pref, Dimension deflt) {
    String value = prefs.getProperty(pref);
    if (value == null) return deflt;
    StringTokenizer st = new StringTokenizer(value, ",");
    Dimension d = new Dimension(deflt);
    if (st.hasMoreTokens()) {
      String s = st.nextToken();
      try {
        int i = Integer.parseInt(s);
        if (i > 0) d.width = i;
      } catch (NumberFormatException nfe) {
        log.warn("Invalid dimension width: " + s);
      }
    }
    if (st.hasMoreTokens()) {
      String s = st.nextToken();
      try {
        int i = Integer.parseInt(s);
        if (i > 0) d.height = i; 
      } catch (NumberFormatException nfe) {
        log.warn("Invalid dimension height: " + s);
      }
    }
    return d;
  }
  
  public static Color getColorPref(String pref, Color deflt) {
    String value = prefs.getProperty(pref);
    if (value == null) return deflt;
    try {
      return Color.decode(value);
    } catch (NumberFormatException nfe) {
      log.warn("Invalid color for preference '" + pref + "': " + value);
      return deflt;
    }
  }

  public static Font getFontPref(String pref, Font deflt) {
    String value = prefs.getProperty(pref);
    if (value == null) return deflt;
    return Font.decode(value);
  }

  public static void removePref(String pref) {
    prefs.remove(pref);
    save();
    firePrefChanged(pref);
  }

  // event model -- a standard event model, except that it's entirely static
  // (and "this" changed to "Prefs.class")
  private static Vector listeners = new Vector();

  // IDEA: addPrefsListener(l, String pref)?
  public static synchronized void addPrefsListener(PrefsListener l) {
	  if (!listeners.contains(l))
	    listeners.add(l);
  }
  public static synchronized void removePrefsListener(PrefsListener l) {
	  listeners.remove(l);
  }
  
  public static void firePrefChanged(String pref) {
	  // alert all listeners
	  Vector l;
	  synchronized (Prefs.class) {
	    l = (Vector) listeners.clone();
	  }

	  int size = l.size();

	  if (size == 0)
	    return;

	  PrefsEvent e = new PrefsEvent(Prefs.class, pref);

	  for (int i=0; i<size; i++) {
	    PrefsListener listener = (PrefsListener) l.elementAt(i);
	    listener.prefChanged(e);
	  }
  }
}