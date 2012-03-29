/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
// Copyright (c) 2001 Ken Harris <kbh7@cornell.edu>
// Copyright (c) 2004-2005 Aaron Hamid
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package org.tellervo.desktop.prefs;

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
import java.util.ArrayList;
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

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.AbstractSubsystem;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.schema.WSIWmsServer;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.BugReport;
import org.tellervo.desktop.util.JDisclosureTriangle;
import org.tellervo.desktop.util.WeakEventListenerList;


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
	// sample editor

	private final static Logger log = LoggerFactory.getLogger(Prefs.class);

	
	/**
	 * Enum for all the keys identifying preference types in Tellervo
	 * 
	 * @author pwb48
	 *
	 */
	public enum PrefKey{
		
		EDIT_FOREGROUND("tellervo.edit.foreground"),
		EDIT_BACKGROUND("tellervo.edit.background"),
		EDIT_FONT("tellervo.edit.font"),
		EDIT_GRIDLINES("tellervo.edit.gridlines"),
		GRAPH_BACKGROUND("tellervo.graph.background"),
		GRAPH_GRIDLINES("tellervo.graph.graphpaper"),
		GRAPH_GRIDLINES_COLOR("tellervo.graph.graphpaper.color"),
		GRAPH_AXISCURSORCOLOR("tellervo.graph.foreground"),
		GRID_HIGHLIGHT("tellervo.grid.highlight"),
		GRID_HIGHLIGHTCOLOR("tellervo.grid.hightlightcolor"),
		DISPLAY_UNITS("tellervo.displayunits"),
		BARCODES_DISABLED("tellervo.barcodes.disable"),
		MEASUREMENT_VARIABLE("tellervo.measurement.variable"),
		
		/** Boolean indicating whether OpenGL failed on last attempt*/
		OPENGL_FAILED("opengl.failed"),
		OPENGL_LIBRARY_PRESENT("opengl.librarypresent"),
		SERIAL_DEVICE("tellervo.serial.measuring.device"),
		SERIAL_PORT("tellervo.serialsampleio.port"),
		SERIAL_DATABITS("tellervo.port.databits"),
		SERIAL_BAUD("tellervo.port.baudrate"),
		SERIAL_FLOWCONTROL("tellervo.port.flowcontrol"),
		SERIAL_PARITY("tellervo.port.parity"),
		SERIAL_LINEFEED("tellervo.port.linefeed"),
		SERIAL_STOPBITS("tellervo.port.stopbits"),
		SERIAL_MEASURE_CUMULATIVELY("tellervo.serial.measurecumulatively"),
		SERIAL_MEASURE_IN_REVERSE("tellervo.serial.measurereverse"),
		SERIAL_MULTIPLIER("tellervo.serial.multiplier"),
		SERIAL_LIBRARY_PRESENT("tellervo.serial.librarypresent"),
		
		STATS_FORMAT_TSCORE("tellervo.cross.tscore.format"),
		STATS_FORMAT_RVALUE("tellervo.cross.rvalue.format"),
		STATS_FORMAT_TREND("tellervo.cross.trend.format"),
		STATS_FORMAT_DSCORE("tellervo.cross.dscore.format"),
		STATS_FORMAT_WEISERJAHRE("tellervo.cross.weiserjahre.format"),
		STATS_OVERLAP_REQUIRED("tellervo.cross.overlap"),
		STATS_OVERLAP_REQUIRED_DSCORE("tellervo.cross.d-overlap"),
		STATS_MODELINE("tellervo.modeline.statistic"),
		
		PERSONAL_DETAILS_EMAIL("tellervo.bugreport.fromemail"),
		PERSONAL_DETAILS_USERNAME("tellervo.login.username"),
		PERSONAL_DETAILS_PASSWORD("tellervo.login.password"),
		REMEMBER_USERNAME("tellervo.login.remember_username"),
		REMEMBER_PASSWORD("tellervo.login.remember_password"),
		AUTO_LOGIN("tellervo.login.auto_login"),
		
		INDEX_POLY_DEGREES("tellervo.index.polydegs"),
		INDEX_LOWPASS("tellervo.index.lowpass"),
		INDEX_CUBIC_FACTOR("tellervo.index.cubicfactor"),
		
		EXPORT_FORMAT("tellervo.export.format"),
		IMPORT_FORMAT("tellervo.import.format"),
		
		/** URL for the Tellervo webservice */
		WEBSERVICE_URL("tellervo.webservice.url"),
		WEBSERVICE_DISABLED("tellervo.webservice.disable"),
		PROXY_TYPE("tellervo.proxy.type"),
		PROXY_PORT_HTTP("tellervo.proxy.http_port"),
		PROXY_PORT_HTTPS("tellervo.proxy.https_port"),
		PROXY_HTTPS("tellervo.proxy.https"),
		PROXY_HTTP("tellervo.proxy.http"),
		
		WMS_PERSONAL_SERVERS("tellervo.wms.personalservers"),
		
		GRAPH_DEFAULT_AGENT("tellervo.graph.defaultagent"),
				
		FOLDER_DATA("tellervo.dir.data"),
		FOLDER_LAST_READ("tellervo.dir.lastimport"),
		FOLDER_LAST_SAVE("tellervo.dir.lastsave"),
		FOLDER_LAST_GPS("tellervo.dir.lastgps");
		
		
		
		private String key;
		
		private PrefKey(String key)
		{
			this.key = key;
		}
		
		public String getValue()
		{
			return key;
		}
	}
	
	
	/**
	 * if true, silently ignore if the prefs can't be saved. protected to avoid
	 * synthetic accessor
	 */
	protected static boolean dontWarn = false;
	private String prefsFolder;
	private String userPrefsFilename;

	/**
	 * Our internal Properties object in which to save preferences
	 */
	private Properties prefs;

	/**
	 * A copy of the default UIDefaults object available at startup. We cache
	 * these defaults to allow the user to "reset" any changes they may have
	 * made through the Appearance Panel.
	 */
	private final Hashtable<Object, Object> UIDEFAULTS = new Hashtable<Object, Object>(); // (Hashtable)
															// UIManager.getDefaults().clone();

	public String getName() {
		return "Preferences";
	}

	// returns the directory for storing Tellervo-related files...
	public String getTellervoDir() {
		return prefsFolder;
	}

	/**
	 * Migrate old Corina preferences to Tellervo
	 */
	private Boolean migrateCorinaToTellervo()
	{
		String home = System.getProperty("user.home");

		String corinaUserPrefsFilename;
		String tellervoUserPrefsFilename;
		
		if (!home.endsWith(File.separator))
			home = home + File.separator;

		if (App.platform.isWindows()) 
		{			
			corinaUserPrefsFilename = home + "Application Data" + File.separator + "Corina" + File.separator + "Corina.pref";
			tellervoUserPrefsFilename  = home + "Application Data" + File.separator + "Tellervo" + File.separator + "Tellervo.pref";
		} 
		else if (Platform.isMac()) 
		{
			corinaUserPrefsFilename = home + "Library/Corina/Preferences"; 
			tellervoUserPrefsFilename  = home + "Library/Tellervo/Preferences";
		} 
		else 
		{
			corinaUserPrefsFilename = home + ".corina/.preferences";
			tellervoUserPrefsFilename  = home + ".tellervo" + File.separator + ".preferences";
		}
		
		
		// Do the copy if old exists and new doesn't
		File oldprefs = new File(corinaUserPrefsFilename);
		File newprefs = new File(tellervoUserPrefsFilename);
		if(oldprefs.exists() && !newprefs.exists())
		{
			try {
				FileUtils.copyFile(oldprefs, newprefs);
				String oldprefcontents = FileUtils.readFileToString(oldprefs, "UTF-8");
				String newprefcontents = oldprefcontents.replaceAll("corina.", "tellervo.");
				FileUtils.writeStringToFile(newprefs, newprefcontents, "UTF-8");
				initPrefFolderLocations();
				load();
			} catch (IOException e) {
				log.error("Failed to migrate Corina preferences to Tellervo");
			}
		}
		else
		{
			return false;
		}

		// Help out Cornelians by updating their WS URL
		if(App.prefs.getPref(PrefKey.WEBSERVICE_URL, null).equals("https://dendro.cornell.edu/webservice.php"))
		{
			App.prefs.setPref(PrefKey.WEBSERVICE_URL, "https://dendro.cornell.edu/tellervo/");
			log.info("Altered Webservice URL to new Tellervo URL");
		}
		
		
		return true;
	}
	
	/**
	 * Set the preferences folder and filename depending on OS
	 */
	private void initPrefFolderLocations()
	{
		// Get home folder
		String home = System.getProperty("user.home");
		if (!home.endsWith(File.separator))
			home = home + File.separator;


		if (App.platform.isWindows()) {
			String basedir = home;

			// if the Application Data directory exists, use it as our base.
			if (new File(home + "Application Data").isDirectory())
				basedir = home + "Application Data" + File.separator;

			// ~/Application Data/Tellervo/
			// OR ~/Tellervo/, if no Application Data
			basedir += "Tellervo" + File.separator;

			// if the Tellervo directory doesn't exist, make it.
			File tellervodir = new File(basedir);
			if (!tellervodir.exists())
				tellervodir.mkdir();

			prefsFolder = basedir;
			userPrefsFilename = basedir + "Tellervo.pref";

		} 
		else if (Platform.isMac()) 
		{
			prefsFolder = home + "Library/Tellervo/";
			userPrefsFilename = home + "Library/Tellervo/Preferences"; 
			File basedir = new File(prefsFolder);
			if (!basedir.exists())
				basedir.mkdirs();
		} 
		else 
		{
			// good ol' Linux
			prefsFolder = home + ".tellervo/";
			userPrefsFilename = home + ".tellervo/.preferences";

			File basedir = new File(prefsFolder);
			if (!basedir.exists())
				basedir.mkdirs();
		}
	}
	
	/**
	 * Initializes the preferences system. This should be called upon startup.
	 */
	@Override
	public void init() {
		super.init();

		// Migrate Corina prefs first if necessary
		Boolean migrated = migrateCorinaToTellervo();
		if(migrated) 
		{
			setInitialized(true);
			return;
		}
		
		// Initialise the preferences file and folder 
		initPrefFolderLocations();

		// Initialise the default UI settings
		initUIDefaults();

		// Proceed with loading the preferences
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
		if (!initialized)
			log.debug("being destroyed more than once!");
		save();
	}

	@Override
	public void finalize() throws Throwable {
		save();
		super.finalize();
	}

	public Hashtable<Object, Object> getUIDefaults() {
		return UIDEFAULTS;
	}

	private void initUIDefaults() {
		// UIDEFAULTS.putAll(UIManager.getDefaults());
		UIDefaults defaults = UIManager.getDefaults();
		// XXX: Even though Hashtable implements Map since
		// Java 1.2, UIDefaults is "special"... the Iterator
		// returned by UIDefaults keySet object does not return any
		// keys, and therefore Enumeration must be used instead - aaron
		Enumeration<?> e = defaults.keys();

		while (e.hasMoreElements()) {
			Object key = e.nextElement();
			Object value = defaults.get(key);

			// for some reason, java 1.6 seems to have some null values.
			if (value == null)
				continue;

			// log.debug("Saving UIDefault: " + key);
			UIDEFAULTS.put(key, value);
		}
	}

	/**
	 * Load system and user properties (preferences). This runs 3 steps:
	 * <ol>
	 * <li>Load system properties
	 * <li>Load default properties from class loader resource prefs.properties
	 * <li>Override with user's properties file 
	 * </ol>
	 * 
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
			java.io.InputStream is = cl.getResourceAsStream("prefs.properties");
			if (is != null) {
				try {
					defaults.load(is);
				} finally {
					is.close();
				}
			}
		} catch (IOException ioe) {
			errors.append("Error loading Tellervo's default preferences.\n");
		}

		// instantiate our properties using the system properties
		// and tellervo properties as default values
		prefs = new Properties(defaults);

		try {
			prefs.load(new FileInputStream(userPrefsFilename));	
		} catch (FileNotFoundException fnfe) {
			// user doesn't have a properties file, so we'll give her one!
			try {
				// this is the guts of save(), but without the nice error
				// handling.
				prefs.store(new FileOutputStream(userPrefsFilename),
						"Tellervo user preferences");
				App.isFirstRun = true;
			} catch (IOException ioe) {
				errors.append("Error copying preferences file to your home directory: "
							+ ioe.getMessage() + "\n");
			}
		} catch (IOException ioe) {
			errors.append("Error loading user preferences file: "
					+ ioe.getMessage() + "\n");
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
		Iterator<?> it = prefs.entrySet().iterator();
		UIDefaults uidefaults = UIManager.getDefaults();
		log.debug("iterating prefs");
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
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

	private void installUIDefault(Class<? extends Object> type, String prefskey, String uikey) {
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
			log.warn("UIDefaults color preference '" + prefskey
					+ "' was not decodable.");
			return;
		}

		UIDefaults uidefaults = UIManager.getDefaults();
		// if (uidefaults.contains(property)) {
		// NOTE: ok, UIDefaults object is strange. Not only does
		// it not implement the Map interface correctly, but entries
		// will not "stick". The entries must be first explicitly
		// removed, and then re-added - aaron
		log.debug("Removing UIDefaults key before overwriting: " + uikey);
		uidefaults.remove(uikey);
		// }

		if (Color.class.isAssignableFrom(type)) {
			uidefaults.put(uikey, new ColorUIResource((Color) decoded));
		} else {
			uidefaults.put(uikey, new FontUIResource((Font) decoded));
		}
	}

	/*
	 * XXX: maybe expose this later if/when we have a component/module/subsystem
	 * model public static void destroy() { save(); }
	 */

	/**
	 * Save current properties to the user's system-writable properties
	 * (preferences) file, <code>FILENAME</code>.
	 */
	public synchronized void save() {
		log.debug("Saving preferences...");

		// try to save prefs, and on failure present "try again?" dialog.
		while (true) {
			try {
				// -- p.store() buffers internally, so if i used a buffered
				// stream
				// here it would only hurt performance.
				// -- the second string passed to store() is a comment line
				// which
				// is added to the top of the file.
				prefs.store(new FileOutputStream(userPrefsFilename),
						"Tellervo user preferences");
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
		message.add(new JLabel(I18n.getText("error.prefs_cant_save")),
				BorderLayout.NORTH);

		// -- dialog with optionpane (warning?)
		JOptionPane optionPane = new JOptionPane(message,
				JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog(null /* ? */, I18n
				.getText("error.prefs_cant_save_title"));

		// -- buttons: cancel, try again.
		optionPane.setOptions(new String[] { I18n.getText("question.try_again"),
				I18n.getText("general.cancel") });

		// -- disclosure triangle with scrollable text area: click for
		// details... (stacktrace)
		JComponent stackTrace = new JScrollPane(new JTextArea(BugReport
				.getStackTrace(e), 10, 60));
		JDisclosureTriangle v = new JDisclosureTriangle(I18n
				.getText("bug.click_for_details"), stackTrace, false);
		message.add(v, BorderLayout.CENTER);

		// -- checkbox: don't warn me again
		JCheckBox dontWarnCheckbox = new JCheckBox(I18n
				.getText("bug.dont_warn_again"), false);
		dontWarnCheckbox.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				dontWarn = !dontWarn;
			}
		});

		// FIXME: consolidate |message| panel construction with Layout methods
		message.add(dontWarnCheckbox, BorderLayout.SOUTH);

		// show dialog
		dialog.pack();
		dialog.setResizable(false);
		dialog.setVisible(true);

		// return true if "try again" is clicked
		return optionPane.getValue().equals(I18n.getText("try_again"));
	}

	// --------------------------------------------------
	// new prefs api below here

	/*
	 * TODO: -- set/get any data type -- automatically save -- (but not right
	 * away, which would be slow) -- defaults -- required? -- convenience
	 * functions for non-atomic things like window geometries? -- also, lists of
	 * data -- prefs event -- prefs listener -- on prefs.set, fire events -- get
	 * rid of all refreshFromPrefs() methods, HasPreferences interface --
	 * (register listener with type, like jrendezvous?)
	 */

	public Properties getPrefs() {
		return prefs;
	}


	
	/**
	 * Set the value of a preference
	 * 
	 * @param key
	 * @param value
	 */
	public void setPref(PrefKey key, String value)
	{
		String pref = key.getValue();
		
		// support removing via set(null)
		if(value == null)
		{
			prefs.remove(pref);
		}
		// Trim spaces from webservice URL
		else if(pref.equals("tellervo.webservice.url"))
		{
			prefs.setProperty(pref, value.trim());
		}
		else
		{
			prefs.setProperty(pref, value);
		}

		save();
		firePrefChanged(pref);
	}
	
	/**
	 * Set the value of a preference to the specified boolean
	 * @param key
	 * @param value
	 */
	public void setBooleanPref(PrefKey key, boolean value) {
		setPref(key, Boolean.toString(value));
	}
	
	
	public void removeWSIWmsServerFromArray(PrefKey key, WSIWmsServer server)
	{
		ArrayList<WSIWmsServer> servers = getWSIWmsServerArrayPref(key);
		
		servers.remove(server);
		
		setWSIWmsServerArrayPref(key, servers);
	
	}
	
	public void addWSIWmsServerToArrayPref(PrefKey key, WSIWmsServer server)
	{
		ArrayList<WSIWmsServer> servers = getWSIWmsServerArrayPref(key);
		
		if(servers == null) servers = new ArrayList<WSIWmsServer>();
		
		servers.add(server);
		
		setWSIWmsServerArrayPref(key, servers);
	}
	
	public void setWSIWmsServerArrayPref(PrefKey key, ArrayList<WSIWmsServer> arr)
	{
		String str = "";
		
		for(WSIWmsServer server : arr)
		{
			str += server.getName()+"="+server.getUrl()+":::";
		}
		
		setPref(key, str);

	}
	
	public ArrayList<WSIWmsServer> getWSIWmsServerArrayPref(PrefKey key)
	{
		ArrayList<WSIWmsServer> servers = new ArrayList<WSIWmsServer>();
		String str = getPref(key, "");
		
		String[] pairs = str.split(":::");
		
		for(String pair : pairs)
		{
			String[] pair2 = pair.split("=");
			
			if(pair2.length!=2) continue;
			
			WSIWmsServer server = new WSIWmsServer();
			server.setName(pair2[0]);
			server.setUrl(pair2[1]);
			servers.add(server);
			
		}
		
		return servers;
		
		
	}
	
	
	/**
	 * Set the value of a preference to the specified color
	 * @param pref
	 * @param value
	 */
	public void setColorPref(PrefKey pref, Color value) {
		String encoded = "#" + Integer.toHexString(value.getRGB() & 0x00ffffff);
		setPref(pref, encoded);
	}
	
	/**
	 * Set the value of a preference to the specified int
	 * @param pref
	 * @param value
	 */
	public void setIntPref(PrefKey pref, int value) {
		setPref(pref, Integer.toString(value));
	}
	
	/**
	 * Set the value of a preference to the specified double
	 * @param pref
	 * @param value
	 */
	public void setDoublePref(PrefKey pref, Double value) {
		setPref(pref, Double.toString(value));
	}
	
	/**
	 * Set the value of a preference to the specified font
	 * @param pref
	 * @param value
	 */
	public void setFontPref(PrefKey pref, Font value) {
		setPref(pref, stringifyFont(value));
	}
	
	/**
	 * Set the value of a preference to the specified dimension
	 * @param pref
	 * @param value
	 */
	public void setDimensionPref(PrefKey pref, Dimension value) {
		setPref(pref, value.width + "," + value.height);
	}


	
	/**
	 * Method for getting the string value of a preference
	 * 
	 * @param key
	 * @param deflt
	 * @return
	 */
	public String getPref(PrefKey key, String deflt){
		String value = prefs.getProperty(key.getValue());
		if (value == null)
			value = deflt;
		
		return value;	
	}
	
	public Double getDoublePref(PrefKey key, Double deflt){
		
		String value = prefs.getProperty(key.getValue());
		if (value == null)
			return deflt;
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException nfe) {
			log.warn("Invalid double for preference '" + key.getValue() + "': " + value);
			return deflt;
		}
		
	}
	
	/**
	 * Get a color for the specified preference key
	 * 
	 * @param key
	 * @param deflt
	 * @return
	 */
	public Color getColorPref(PrefKey key, Color deflt) {
		String value = prefs.getProperty(key.getValue());
		if (value == null)
			return deflt;
		try {
			return Color.decode(value);
		} catch (NumberFormatException nfe) {
			log.warn("Invalid color for preference '" + key.getValue() + "': " + value);
			return deflt;
		}
	}
	
	/**
	 * Get an enum for the specified preference key
	 * 
	 * @param <T>
	 * @param pref
	 * @param deflt
	 * @param enumType
	 * @return
	 */
	public <T extends Enum<T>> T getEnumPref(PrefKey pref, T deflt, Class<T> enumType) {
		String val = getPref(pref, null);
		if(val == null)
			return deflt;
		
		try {
			return Enum.valueOf(enumType, val);
		} catch (Exception e) {
			return deflt;
		}
	}
	
	
	/**
	 * Method for getting the boolean value of a preference
	 * @param key
	 * @param deflt
	 * @return
	 */
	public Boolean getBooleanPref(PrefKey key, Boolean deflt)
	{
		String value = prefs.getProperty(key.getValue());
		if(value == null)
			return deflt;
		return Boolean.parseBoolean(value);
	}
	
	/**
	 * Get the specified preference as an int
	 * @param key
	 * @param deflt
	 * @return
	 */
	public int getIntPref(PrefKey key, int deflt) {
		String value = prefs.getProperty(key.getValue());
		if (value == null)
			return deflt;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			log.warn("Invalid integer for preference '" + key.getValue() + "': " + value);
			return deflt;
		}
	}
	
	
	/**
	 * Get the requested preference as a font
	 * 
	 * @param key
	 * @param deflt
	 * @return
	 */
	public Font getFontPref(PrefKey key, Font deflt) {
		String value = prefs.getProperty(key.getValue());
		if (value == null)
			return deflt;
		return Font.decode(value);
	}
	
	/**
	 * Get the requested preference as a Dimension
	 * 
	 * @param key
	 * @param deflt
	 * @return
	 */
	public Dimension getDimensionPref(PrefKey key, Dimension deflt) {
		String value = prefs.getProperty(key.getValue());
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
	
	
	

	/**
	 * Remove a preference
	 * 
	 * @param key
	 */
	public void removePref(PrefKey key)
	{
		prefs.remove(key.getValue());
		save();
		firePrefChanged(key.getValue());
	}
	
	/**
	 * Remove a preference
	 * 
	 * @param pref
	 */
	@Deprecated
	public void removePref(String pref) {
		prefs.remove(pref);
		save();
		firePrefChanged(pref);
	}

	private WeakEventListenerList listeners = new WeakEventListenerList();

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

		for (int i = 0; i < l.length; i += 2) {
			if (l[i] == PrefsListener.class)
				((PrefsListener) l[i + 1]).prefChanged(e);
		}
	}
	
	/**
	 * Code a font into a string
	 * 
	 * @param f
	 * @return
	 */
	public static final String stringifyFont(Font f) {
		StringBuffer sb = new StringBuffer();

		sb.append(f.getFontName());
		sb.append('-');
		
		int s = sb.length();
		if ((f.getStyle() & Font.BOLD) != 0) {
			sb.append("BOLD");
		}
		if ((f.getStyle() & Font.ITALIC) != 0) {
			sb.append("ITALIC");
		}
		if (sb.length() > s) {
			sb.append('-');
		}
		
		sb.append(f.getSize());
		
		return sb.toString();
	}

	

	
	
	
	/**
	 *  DEPRECATED METHODS
	 */
	
	/**
	 * Use setPref(PrefKey, ...) instead
	 * @param pref
	 * @param value
	 */
	@Deprecated
	public void setBooleanPref(String pref, boolean value) {
		setPref(pref, Boolean.toString(value));
	}
	
	/**
	 * Set the value of a preference to the specified double
	 * @param pref
	 * @param value
	 */
	@Deprecated
	public void setDoublePref(String pref, Double value) {
		setPref(pref, Double.toString(value));
	}
	
	
	
	/**
	 * Use getPref(PrefKey, ...) instead
	 * 
	 * @param pref
	 * @param deflt
	 * @return
	 */
	@Deprecated
	public Double getDoublePref(String pref, Double deflt) {
		String value = prefs.getProperty(pref);
		if(value == null)
			return deflt;
		return Double.parseDouble(value);
	}
	
	/**
	 * Use getPref(PrefKey, ...) instead
	 * 
	 * @param pref
	 * @param deflt
	 * @return
	 */
	@Deprecated
	public boolean getBooleanPref(String pref, boolean deflt) {
		String value = prefs.getProperty(pref);
		if(value == null)
			return deflt;
		return Boolean.parseBoolean(value);
	}
	
	/**
	 * Use setPref(PrefKey, ...) instead
	 * @param pref
	 * @param value
	 */
	@Deprecated
	public void setDimensionPref(String pref, Dimension value) {
		setPref(pref, value.width + "," + value.height);
	}

	/**
	 * Use getPref(PrefKey, ...) instead
	 * 
	 * @param pref
	 * @param deflt
	 * @return
	 */
	@Deprecated
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
	
	/**
	 * Use setPref(PrefKey, ...) instead
	 * @param pref
	 * @param value
	 */
	@Deprecated
	public void setIntPref(String pref, int value) {
		setPref(pref, Integer.toString(value));
	}

	/**
	 * Use getPref(PrefKey, ...) instead
	 * 
	 * @param pref
	 * @param deflt
	 * @return
	 */
	@Deprecated
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
	
	/**
	 * Use setPref(PrefKey, ...) instead
	 * @param pref
	 * @param value
	 */
	@Deprecated
	public void setColorPref(String pref, Color value) {
		String encoded = "#" + Integer.toHexString(value.getRGB() & 0x00ffffff);
		setPref(pref, encoded);
	}

	/**
	 * Use getPref(PrefKey, ...) instead
	 * 
	 * @param pref
	 * @param deflt
	 * @return
	 */
	@Deprecated
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
	
	/**
	 * Use setPref(PrefKey, ...) instead
	 * @param pref
	 * @param value
	 */
	@Deprecated
	public void setFontPref(String pref, Font value) {
		setPref(pref, stringifyFont(value));
	}

	/**
	 * Use getPref(PrefKey, ...) instead
	 * 
	 * @param pref
	 * @param deflt
	 * @return
	 */
	@Deprecated
	public Font getFontPref(String pref, Font deflt) {
		String value = prefs.getProperty(pref);
		if (value == null)
			return deflt;
		return Font.decode(value);
	}
	
	/**
	 * Use getPref(PrefKey, ...) instead
	 * @param pref
	 * @return
	 */
	@Deprecated
	public String getPref(String pref) {
		return prefs.getProperty(pref);
	}
	
	/**
	 * Get a preference result as an enum
	 * 
	 * @param pref
	 * @param deflt
	 * @param enumType
	 * @return
	 */
	@Deprecated
	public <T extends Enum<T>> T getEnumPref(String pref, T deflt, Class<T> enumType) {
		String val = getPref(pref, null);
		if(val == null)
			return deflt;
		
		try {
			return Enum.valueOf(enumType, val);
		} catch (Exception e) {
			return deflt;
		}
	}
	
	/**
	 * Use getPref(PrefKey, String) instead
	 * 
	 * @param pref
	 * @param deflt
	 * @return
	 */
	@Deprecated
	public String getPref(String pref, String deflt) {
		String value = prefs.getProperty(pref);
		if (value == null)
			value = deflt;
				
		return value;
	}
	
	@Deprecated
	public void setPref(String pref, String value) {
		
		// support removing via set(null)
		if(value == null)
		{
			prefs.remove(pref);
		}
		// Trim spaces from webservice URL
		else if(pref.equals("tellervo.webservice.url"))
		{
			prefs.setProperty(pref, value.trim());
		}
		else
		{
			prefs.setProperty(pref, value);
		}

		save();
		firePrefChanged(pref);
	}
}
