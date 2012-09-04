/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.ui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.platform.Platform;

import com.dmurph.mvc.MVCEvent;
import com.lowagie.text.BadElementException;


// TODO: if on 1.4, use setDisplayedMnemonicIndex() so "Save &As..."
// underlines the correct 'A'

public class Builder {
	private final static Logger log = LoggerFactory.getLogger(Builder.class);

	// (don't instantiate me)
	private Builder() {
	}

	public final static String INDENT = "    "; // 4 spaces

	public static JMenu makeMenu(String key) {
		JMenu m = new JMenu();

		setupMnemonics(m, key);

		return m;
	}
	
	public static JMenu makeMenu(String key, String iconfilename) {
		JMenu m = new JMenu();
		m.setIcon(getIcon(iconfilename, ICONS, 22));
		
		setupMnemonics(m, key);

		return m;
	}
	
	/**
	 * Creates a menu item that dispatches an mvc event with the given key what it's pressed
	 * @param key
	 * @param argMVCKey
	 * @param iconfilename
	 * @return
	 */
	public static JMenuItem makeMVCMenuItem(String argTextKey, final String argMVCKey, String argIconFilename) {
		JMenuItem m = new JMenuItem(argTextKey, getIcon(argIconFilename, ICONS, 22));
		setupMnemonics(m, argTextKey);
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				MVCEvent event = new MVCEvent(argMVCKey);
				event.dispatch();
			}
		});
		return m;
	}

	public static JMenuItem makeMenuItem(String key) {
		JMenuItem m = new JMenuItem("");

		m.setText(I18n.getText(key));

		setupMnemonics(m, key);

		return m;
	}

	public static JMenuItem makeMenuItem(String key, boolean enabled) {
		JMenuItem m = makeMenuItem(key);
		setupMnemonics(m, key);
		m.setEnabled(enabled);
		return m;
	}
	
	public static JMenuItem makeMenuItem(String key, boolean enabled, String iconfilename) {
		JMenuItem m = makeMenuItem(key, null, iconfilename);
		setupMnemonics(m, key);
		m.setEnabled(enabled);
		return m;
	}


	public static JMenuItem makeMenuItem(String key, String action) {
		JMenuItem m = makeMenuItem(key);
		setupMnemonics(m, key);
		addAction(m, action);
		return m;
	}
	
	public static JMenuItem makeMenuItem(String key, String action, String iconfilename){					
		JMenuItem m = new JMenuItem(key, getIcon(iconfilename, ICONS, 22));

		setupMnemonics(m, key);
		
		if(action != null)
			addAction(m, action);
		
		return m;	
	}
	
	public static JCheckBoxMenuItem makeCheckBoxMenuItem(String key){
		return makeCheckBoxMenuItem(key, null);
		
	}

	public static JCheckBoxMenuItem makeCheckBoxMenuItem(String key, String iconfilename) {
		JCheckBoxMenuItem m = new JCheckBoxMenuItem("");

		setupMnemonics(m, key);		
		
		if (iconfilename!=null)	
			m.setIcon(getIcon(iconfilename, ICONS, 22));

		return m;
	}

	public static JRadioButtonMenuItem makeRadioButtonMenuItem(String key) {
		JRadioButtonMenuItem m = new JRadioButtonMenuItem("");

		setupMnemonics(m, key);

		return m;
	}

	public static JButton makeButton(String key) {
		JButton b = new JButton();

		setupMnemonics(b, key);

		return b;
	}

	public static JLabel makeLabel(String key) {
		JLabel l = new JLabel();

		l.setText(I18n.getText(key));

		return l;
	}

	public static JRadioButton makeRadioButton(String key) {
		JRadioButton r = new JRadioButton();

		setupMnemonics(r, key);

		return r;
	}
	
	private static void setupMnemonics(AbstractButton b, String key) {
		// set the text
		b.setText(I18n.getText(key));
		
		// set button mnemonic
		// WHY don't we do this on mac?
		if (!Platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null) {
				b.setMnemonic(mnemonic);
				
				// set the displayed mnemonic position
				mnemonic = I18n.getMnemonicPosition(key);
				if(mnemonic != null)
					b.setDisplayedMnemonicIndex(mnemonic);
			}
		}

		// if it's a menu item, set the mnemonic
		if(b instanceof JMenuItem) {
			KeyStroke keystroke = I18n.getKeyStroke(key);
			if (keystroke != null)
				((JMenuItem)b).setAccelerator(keystroke);
		}
	}

	// Simple helper for supplying the application icon for use in title bars
	public static Image getApplicationIcon()
	{
		Image ic = ((ImageIcon) Builder.getIcon("tellervo-application.png", 32)).getImage();
	
		return ic;
	}
	
	/**
	 * Get standard sized icon of specified filename from Icons folder
	 * @param filename
	 * @return ImageIcon
	 * @deprecated old, need to specify size now
	 */
	@Deprecated
	public static Icon getIcon(String filename) {
		return getIcon(filename, ICONS, 22);
	}
	
	/**
	 * Get icon of specified size and filename from Icons folder
	 * @param filename
	 * @param size
	 * @return ImageIcon
	 */
	public static Icon getIcon(String filename, int size) {
		return getIcon(filename, ICONS, size);
	}
		
	/**
	 * 
	 * @param name
	 * @param packagename
	 * @return
	 * @deprecated old, need to specify size now
	 */
	@Deprecated
	public static Icon getIcon(String name, String packagename) {
		return getIcon(name, packagename, 22);
	}
	
	/**
	 * Get an icon of the specified size from the specified package
	 * 
	 * @param name
	 * @param packagename
	 * @param size
	 * @return
	 */
	public static Icon getIcon(String name, String packagename, int size) {
	
		java.net.URL url = cl.getResource(getIconURL(name, packagename, size));
		
		if (url != null)
		{
			return new ImageIcon(url);
		}
		else
		{
			log.warn("Unabled to find icon "+name+".  Replacing with the 'missing icon' icon.");
			return getMissingIcon(size);
		}
		
	}	
	
	public static Icon getImageAsIcon(String name)
	{
		
		StringBuffer urlBuffer = new StringBuffer();	
		urlBuffer.append(RESOURCE_PACKAGE_PREFIX);
		urlBuffer.append(IMAGES);
		urlBuffer.append('/');	
		urlBuffer.append(name);
				
		java.net.URL url = cl.getResource(urlBuffer.toString());
				
		if (url != null)
		{
			return new ImageIcon(url);
		}
		else
		{
			log.warn("Unabled to find file "+name+".");
			return null;
		}
	}
	
	
	public static com.lowagie.text.Image getITextImageIcon(String name){
		
		java.net.URL url = cl.getResource(getIconURL(name, ICONS, 48));
		
		if (url != null){
			try {
				return com.lowagie.text.Image.getInstance(url);
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return getITextImageMissingIcon();
		
	}
	
	public static com.lowagie.text.Image getITextImageMissingIcon(){
		
		java.net.URL url = cl.getResource(getIconURL("missingicon.png", ICONS, 48));
		
		if (url != null){
			try {
				return com.lowagie.text.Image.getInstance(url);
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
		
	}
	

	public static String getIconURL(String name, String packagename, int size){
		StringBuffer urlBuffer = new StringBuffer();
		
		urlBuffer.append(RESOURCE_PACKAGE_PREFIX);
		urlBuffer.append(packagename);
		urlBuffer.append('/');
		urlBuffer.append(size);
		urlBuffer.append('x');
		urlBuffer.append(size);
		urlBuffer.append('/');		
		urlBuffer.append(name);
		
		//log.debug("Icon url: "+urlBuffer.toString());
		return urlBuffer.toString();
		
	}
	
	public static String getBodgeMissingIconURL(int size){
		return getIconURL("missingicon.png", ICONS, size);
	}
	
	public static String getBodgeIconURL(String name, String packagename, int size){
				
		StringBuffer urlBuffer = new StringBuffer();
		
		String returnurl = cl.getResource(urlBuffer.append(getIconURL(name, packagename, size)).toString()).toString();
		
		log.debug("Icon: "+returnurl.substring(5));
		return returnurl.substring(5);
		
	}
	
	
	
	/**
	 * Retrieve the "missing icon" icon
	 * @return
	 */
	public static Icon getMissingIcon(int size) {
		String sizeStr = size + "x" + size;
		String resourceurl = RESOURCE_PACKAGE_PREFIX + ICONS + "/" + sizeStr + "/missingicon.png";
		java.net.URL url = cl.getResource(resourceurl);
		
		if (url != null)
			return new ImageIcon(url);
		else {
			log.error("Error loading icon: "+resourceurl);
			IllegalStateException ise = new IllegalStateException("Can't load missing icon icon!");
			new Bug(ise);
			throw ise;
		}
	}
	
	public static Image getImage(String name) {
		java.net.URL url = cl.getResource(IMAGES_PACKAGE_PREFIX + name);
		if (url != null)
			return new ImageIcon(url).getImage();
		else
			return null;
	}

	public final static String IMAGES = "Images";
	public final static String ICONS = "Icons";
	private final static String RESOURCE_PACKAGE_PREFIX = "";
	
	private final static String IMAGES_PACKAGE_PREFIX = RESOURCE_PACKAGE_PREFIX + IMAGES;
	
	// my classloader, for getting icons as resources.
	private final static ClassLoader cl = org.tellervo.desktop.ui.Builder.class.getClassLoader();


	public static URL getResourceFile(String name)
	{
		java.net.URL url = cl.getResource("Sounds/"+name);
		return url;
		
	}
	
	// ----------------------------------------
	/*
	 REFACTOR!
	 
	 pattern:
	 
	 A.addActionListener(new AbstractAction() {
	 public void actionPerformed(ActionEvent e) {
	 new B();
	 }
	 });
	 
	 or:
	 
	 A.addActionListener(new AbstractAction() {
	 public void actionPerformed(ActionEvent e) {
	 B.c();
	 }
	 });
	 
	 -- of course, what i really want is simpler closures.
	 can i fake that easily?
	 
	 how about another param for Builder?
	 Builder.makeMenuItem("crossdate_kit", "new CrossdateKit();");
	 not perfect, but it would make my job easier in places, i think.
	 (same for buttons?)
	 */

	/**
	 * An easy way to add actions.  The action string is of the form
	 * <code>"new package.SomeClass();"</code> or
	 * <code>"package.SomeClass.staticMethod();</code>.  Be sure to
	 * fully-qualify the class name.
	 * @param button a JButton or JMenuItem
	 * @param action an action string */
	@SuppressWarnings("serial")
	public static void addAction(AbstractButton button, String actionValue) {
		// parse |action|
		final String action = actionValue.trim();
		StringTokenizer tok = new StringTokenizer(action, " ();");
		String arg1 = tok.nextToken();

		// "new package.SomeClass();"
		if (arg1.equals("new")) {
			String arg2 = tok.nextToken();
			try {
				final Class<?> c = Class.forName(arg2);

				button.addActionListener(new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						try {
							c.newInstance();
						} catch (Exception ex) {
							log.error("Builder.addAction(): can't instantiate " + 
									c + "; action was " + action);
						}
					}
				});
			} catch (ClassNotFoundException cnfe) {
				throw new IllegalArgumentException("class '" + arg2
						+ "' not found");
			}
			return;
		}

		// "package.SomeClass.staticMethod();"
		int lastDot = action.lastIndexOf(".");
		if (lastDot == -1)
			throw new IllegalArgumentException(
					"no 'new' or '.' in action string");
		final String className = action.substring(0, lastDot);

		// the method name is everything after the last dot, for as long
		// as java-identifier chars are available.
		String methodName = action.substring(lastDot + 1);
		while (!Character.isJavaIdentifierPart(methodName.charAt(methodName
				.length() - 1)))
			methodName = methodName.substring(0, methodName.length() - 1);
		final String methodNameGlue = methodName;

		try {
			Class<?> c = Class.forName(className);
			final Method m = c.getMethod(methodName, new Class[] {});

			button.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try {
						m.invoke(null, new Object[] {});
						// IllegalAccess, InvocationTarget exceptions
					} catch (Exception ex) {
						ex.printStackTrace();
						throw new IllegalArgumentException(
								"Builder.addAction(): can't invoke "
										+ "requested method (" + methodNameGlue
										+ " in " + className + ")");
					}
				}
			});
		} catch (ClassNotFoundException cnfe) {
			throw new IllegalArgumentException(
					"Builder.addAction(): can't find requested class");
		} catch (NoSuchMethodException nsme) {
			throw new IllegalArgumentException(
					"Builder.addAction(): can't find requested method");
		}
	}
}
