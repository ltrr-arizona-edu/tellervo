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
//
// This file is part of Corina.
// 
// Tellervo is free software; you can redistribute it and/or modify
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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package org.tellervo.desktop.ui;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.util.StringUtils;


/**
   Provide localization strings.

   <p>Java's ResourceBundles are intended to do this, but they don't
   provide the level of support that most other libraries do.</p>

   <p>For example, suppose you have a "Copy" menuitem.
<pre>
   JMenuItem copy = new JMenuItem("Copy");
</pre>

   Of course, you should internationalize this.  But you want to
   change the text, the mnemonic, and the keyboard accelerator.  If
   you only had ResourceBundle to work with, you might end up with 3
   lines of translation for every word in your program.  Plus, you'd
   have to know the name of the I18n file you used, each time you
   wanted to use it.  Most other libraries (like Powerplant on Mac and
   Win95 resources) let you put them all in one line, which makes the
   translator's job much easier.  That's what this class does.</p>

   <p>Now, all you have to say is:
<pre>
   copy = &amp;Copy [accel C]
</pre>

   <p>There are several things going on here:</p>

   <ul>

     <li>"copy" is the <i>key</i>.  When you want to refer to this
     entry, you'll ask for it by this name.  The user never sees this.

     <li>"Copy" (without the &amp;) is the <i>text</i> of this key.
     This is what users will see in menuitems, buttons, and so forth.

     <li>"C" (the thing right after the &amp;) is the <i>mnemonic</i>.
     On most platforms (all except Mac), this letter is underlined,
     and users can jump to it by pressing this letter, or Alt and this
     letter (depending on platform and context).

     <li>"accel C" is the <i>keystroke</i>.  The user can press this
     at any time to invoke this command.  You can use the modifiers
     "shift", "control", "alt", and "meta", or the special modifier
     "accel".  "accel" is automatically turned into "control" on PCs
     (Windows and Linux), and "meta" (Java's term for "command") on
     Macs.  You should normally use the generic "accel" modifier.  You
     can also combine modifiers, like "shift accel S" (the standard
     keystroke for "Save As...").  The class javax.swing.KeyStroke
     takes these strings as input: you can say
<pre>
   KeyStroke.getKeyStroke(I18n.getKeyStroke("copy"))
</pre>
     to get a Swing KeyStroke object.

   </ul>

   <p>This is convenient, but it gets even better: normally, you don't
   even have to mess with keystrokes and mnemonics.  You can simply
   use the Builder factory to do the dirty work for you:

<pre>
   JMenuItem copy = Builder.makeMenuItem("copy");
</pre>

   Of course, if you just want the text (for making a printout, for
   example), you should still use I18n.getText().</p>

   <p>Not all keys must have a keystroke, or a mnemonic.  Note that
   those methods can return nulls.</p>

   <p>These methods get their values from the resource bundle called
   "TextBundle".  That is, the file is called
   "TextBundle.properties", or some variant, like
   "TextBundle_de_DE.properties".</p>

   <h2>Left to do</h2>
   <ul>
     <li>The way getMnemonic() is set up, "Save &as..." will
         still show it as "S&ave as...".  Bad, but not fatal.

     <li>Add way to escape [/]/&amp; in text; also, add markers so
         "this text is mnemonic/text/keystroke" and "this text is
	 just a string" are separate(?)

     <li>in getKeyStroke(), why not use
         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() instead of
	 (?:) for command/control choice?
   </ul>

   @see java.util.ResourceBundle

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class I18n {
	
	private final static Logger log = LoggerFactory.getLogger(I18n.class);

	private I18n() {
		// don't instantiate me
	}

	/**
	 * Get the text for this key. The text has no special control characters in
	 * it, and can be presented to the user.
	 * 
	 * <p>
	 * For example, if the localization file has the line
	 * <code>copy = &amp;Copy [accel C]</code>, the string "Copy" is returned.
	 * </p>
	 * 
	 * @param key
	 *            the key to look up in the localization file
	 * @return the text
	 */
	public static String getText(String key) {
		
		String value;
		try{
			value = msg.getString(key);
		} catch (MissingResourceException e)
		{
			value = key;
		}
		
		StringBuffer buf = new StringBuffer();

		int n = value.length();
		boolean ignore = false;
		for (int i = 0; i < n; i++) {
			char c = value.charAt(i);
			switch (c) {
			case '&':
				continue;
			case '[':
				ignore = true;
				break;
			case ']':
				ignore = false;
				break;
			default:
				if (!ignore)
					buf.append(c);
			}
		}

		return buf.toString().trim();
	}

	/**
	 * Get the keystroke string for this key. This string can be passed directly
	 * to the Keystroke.getKeyStroke() method.
	 * 
	 * <p>
	 * For example, if the localization file has the line
	 * <code>copy = &amp;Copy [accel C]</code>, the string "control C" is
	 * returned (or on the Mac, "meta C").
	 * </p>
	 * 
	 * <p>
	 * If the string has no [keystroke] listed, null is returned.
	 * </p>
	 * 
	 * @param key
	 *            the key to look up in the localization file
	 * @return the keystroke
	 */
	public static KeyStroke getKeyStroke(String key) {
		String value;
		try {
			value = msg.getString(key);
		} catch (MissingResourceException e) {
			return null;
		}

		int left = value.indexOf('[');
		int right = value.indexOf(']');

		if (left == -1 || right == -1)
			return null;

		String stroke = value.substring(left + 1, right).trim();

		// accel = command (in java-ese: "meta") on mac, control on pc
		String accel = (Platform.isMac() ? "meta" : "control");
		stroke = StringUtils.substitute(stroke, "accel", accel);

		return KeyStroke.getKeyStroke(stroke);
	}

	/**
	 * Get the mnemonic character this key.
	 * 
	 * <p>
	 * For example, if the localization file has the line
	 * <code>copy = &amp;Copy [accel C]</code>, the character "C" is returned.
	 * </p>
	 * 
	 * <p>
	 * If the string has no &amp;mnemonic listed, null is returned.
	 * </p>
	 * 
	 * @param key
	 *            the key to look up in the localization file
	 * @return the integer representing the mnemonic character
	 */
	public static Integer getMnemonic(String key) {
		
		String value = null;
		try{
			value = msg.getString(key);
		} catch (MissingResourceException e)
		{
			return null;
		}

		int amp = value.indexOf('&');

		if (amp == -1 || amp == value.length() - 1)
			return null;

		return new Integer(Character.toUpperCase(value.charAt(amp + 1)));
	}
	
	/**
	 * Get the position of the mnemonic character in the string
	 * Used for setDisplayedMnemonicIndex
	 * @param key
	 * @return an Integer, or null
	 */
	public static Integer getMnemonicPosition(String key) {
		
		String value = null;
		try{
			value = msg.getString(key);
		} catch (MissingResourceException e)
		{
			return null;
		}

		int amp = value.indexOf('&');

		if (amp == -1 || amp == value.length() - 1)
			return null;
		
		return amp;
	}


	// the resource bundle to use
	private final static ResourceBundle msg;


	static {
		ResourceBundle bundle;
		try {
			
			// Grab overriding language prefs if available
			String country = App.prefs.getPref(PrefKey.LOCALE_COUNTRY_CODE, "xxx");
			String language = App.prefs.getPref(PrefKey.LOCALE_LANGUAGE_CODE, "xxx");
			
			if(country.equals("xxx") || language.equals("xxx"))
			{
				// No prefs specified so just go with the default from the system
				bundle = ResourceBundle.getBundle("Translations/TextBundle");
			}
			else
			{
				// Prefs specified so use these instead
				bundle = ResourceBundle.getBundle("Translations/TextBundle", new Locale(language, country));
			}
			
		} catch (MissingResourceException mre) {
			try {
				bundle = ResourceBundle.getBundle("Translations/TextBundle");
			} catch (MissingResourceException mre2) {
				log.error("Could not find locale file.");
				mre2.printStackTrace();
				bundle = new ResourceBundle() {
					
					@Override
					protected Object handleGetObject(String key) {
						return key;
					}
					
					@Override
					public Enumeration<String> getKeys() {
						return EMPTY_ENUMERATION;
					}
					
					private final Enumeration<String> EMPTY_ENUMERATION = new Enumeration<String>() {
						public boolean hasMoreElements() {
							return false;
						}
						
						public String nextElement() {
							throw new NoSuchElementException();
						}
					};
				};
			}
		}
		msg = bundle;
	}
	
	/**
	 * Created by Daniel, easier way of using an arbitrary number of replacing
	 * strings.
	 * 
	 * @param argKey
	 * @param argReplacing
	 * @return
	 */
	public static String getText(String argKey, String... argReplacing) {
		String text = getText(argKey);
		
		for (int i = 0; i < argReplacing.length; i++) {
			text = text.replace("{" + i + "}", argReplacing[i]);
		}
		
		return text;
	}
	
	/**
	 * Get the TellervoLocale using the country and language codes.  If Tellervo 
	 * doesn't support the requested country/language, then the default Tellervo
	 * locale is returned.
	 * 
	 * @param country
	 * @param language
	 * @return
	 */
	public static TellervoLocale getTellervoLocale(String country, String language)
	{
		for(TellervoLocale loc : TellervoLocale.values())
		{
			if(loc.getCountryCode().equals(country) && loc.getLanguageCode().equals(language))
			{
				return loc;
			}
		}
		return getDefaultTellervoLocale(true);
	}
	
	
	/**
	 * Get the default locale for the system.  If the systems locale is not supported
	 * 'fallback' determines if Tellervo's default locale (US English) should be 
	 * returned.
	 * 
	 * @param fallback
	 * @return
	 */
	public static TellervoLocale getDefaultTellervoLocale(Boolean fallback)
	{
		Locale defloc = Locale.getDefault();
		String country = defloc.getCountry();
		String language = defloc.getLanguage();

		for(TellervoLocale loc : TellervoLocale.values())
		{
			if(loc.getCountryCode().equals(country) && loc.getLanguageCode().equals(language))
			{
				return loc;
			}
		}
		
		// Still no luck
		// In fallback is true then send US English		
		if(fallback)
		{
			// Preferred locale not found or not supported so use US English instead
			return TellervoLocale.ENGLISH_US;
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * Get the TelleroLocale from the preferences, or if none is specified
	 * get the default.
	 * 
	 * @param fallback - if true then return default Locale if all else fails
	 * @return
	 */
	public static TellervoLocale getPreferredTellervoLocale(Boolean fallback)
	{

		// First try to get a locale from the preferences values
		String country = App.prefs.getPref(PrefKey.LOCALE_COUNTRY_CODE, "xxx");
		String language = App.prefs.getPref(PrefKey.LOCALE_LANGUAGE_CODE, "xxx");
		for(TellervoLocale loc : TellervoLocale.values())
		{
			if(loc.getCountryCode().equals(country) && loc.getLanguageCode().equals(language))
			{
				return loc;
			}
		}
		
		// Couldn't find locale from prefs, so try system default instead
		return getDefaultTellervoLocale(fallback);
	}
		
	
	/**
	 * Locale enum for supported country/languages 
	 * 
	 * @author pwb48
	 *
	 */
	public enum TellervoLocale{
		GERMAN        ("Deutsch",      "de", "DE" ), 
		ENGLISH_PROPER("English (UK)", "en", "GB" ),
		ENGLISH_US    ("English (US)", "en", "US"),
		FRENCH        ("Français",     "fr", "FR"),
		DUTCH         ("Nederlands",   "nl", "NL"),
		POLISH        ("Polski",       "pl", "PL"),
		TURKISH       ("Türk",         "tr", "TR");
		
		
		private String country;
		private String language;
		private String name;

		
		private TellervoLocale(String name, String language, String country)
		{
			this.name = name;
			this.country  = country;
			this.language = language;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String toString()
		{
			return getName();
		}
		
		public String getCountryCode()
		{
			return country;
		
		}
		
		public String getLanguageCode()
		{
			return language;
		}
		
		public Locale getLocale()
		{
			Locale locale = new Locale(language, country);
			return locale;	
		}
		
		public Icon getFlag()
		{
			return Builder.getIcon(country+".png", 16);
		}
		

		
	};
}
