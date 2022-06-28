/*******************************************************************************
 * Copyright (C) 2003 Ken Harris
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
package org.tellervo.desktop.platform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.gui.menus.HelpMenu;
import org.tellervo.desktop.ui.TellervoAction;


// TODO: refactor.  lots of duplicate code in here.
// TODO: javadoc me.

// TODO: implement com.apple.MRJOpenApplicationHandler ("open application" apple events)
// TODO: implement com.apple.MRJOpenDocumentHandler (double-clicking a document or dragging to icon)

// class to deal with macintosh-specific things, like
// -- adding "about"/"prefs"/"quit" menuitems
// (using apple classes, but with code that compiles on any system.)

// other notes:
// -- "return null" in invocation handlers.  i have to return something, but the docs don't say what.

public class OSX {

	/**
	   Uses the other methods of this class to set up the About,
	   Preferences, and Quit menu items.

	   <p>"About" calls "new AboutBox()".  Preferences calls
	   "PrefsDialog.showPreferences()".  Quit calls "TellervoMainWindow.quit();
	   System.exit(0);".</p>

	   <p>If this system is not a Mac, does nothing.</p>
	 */
	public static void configureMenus() {
		if (Platform.isMac()) {
			// register "about" menuitem
			OSX.registerAboutHandler(HelpMenu.ABOUT_ACTION);/*new Runnable() {
			                public void run() {
			                    AboutBox.getInstance().show();
			                }
			            });*/

			// and "preferences"
			OSX.registerPrefsHandler(new Runnable() {
				public void run() {
					App.showPreferencesDialog();
				}
			});

			// and "quit"
			OSX.registerQuitHandler(new Runnable() {
				public void run() {
					//TellervoMainWindow.quit();
					//TODO!!!
					System.exit(0);
				}
			});
		}
	}

	// assumes:
	// -- this is actually a mac we're running on
	// -- about.run() throws no exceptions
	// what i do: basically the same as:
	/*
	    import com.apple.mrj.*;
	    ...
	        MRJApplicationUtils.registerAboutHandler(new MRJAboutHandler() {
	            public void handleAbout() {
	                about.run();
	            }
	        });
	 */

	@SuppressWarnings("unchecked")
	public static void registerAboutHandler(final TellervoAction about) {
		try {
			InvocationHandler handler = new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) {
					if (method.getName().equals("handleAbout"))
						about.perform(null);
					return null;
				}
			};

			Class appUtils = Class.forName("com.apple.mrj.MRJApplicationUtils");
			Class paramTypes[] = new Class[] { Class.forName("com.apple.mrj.MRJAboutHandler") };
			Object aboutHandler = Proxy.newProxyInstance(getClassLoader(), paramTypes, handler);
			Method register = appUtils.getMethod("registerAboutHandler", paramTypes);
			register.invoke(appUtils.newInstance(), new Object[] { aboutHandler });
		} catch (Exception e) {
			// can't happen <=> bug!
			new BugDialog(e);
		}
	}

	// --------------------------------------------------

	// same thing, but for quit:
	/*
	    import com.apple.mrj.*;
	    ...
	    MRJApplicationUtils.registerQuitHandler(new MRJQuitHandler() {
	            public void handleQuit() {
	                (new Thread() { // needs to run in its own thread, for reasons i don't entirely understand.
	                    public void run() {
	                        try {
	                            XCorina.quit();
	                            System.exit(0);
	                        } catch (IllegalStateException ise) {
			        // don't do anything
	                        }
	                    }
	                }).start();
	            }
	        });
	 */

	// -- why does it have to be run in its own thread?
	// -- what's with the ISEx?  is that how i cancel?  if yes, say so.
	@SuppressWarnings("unchecked")
	public static void registerQuitHandler(Runnable quit) {
		final Runnable glue = quit;

		try {
			InvocationHandler handler = new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) {
					if (method.getName().equals("handleQuit")) {
						// needs to run in its own thread, for reasons i don't entirely understand.
						(new Thread() {
							@Override
							public void run() {
								try {
									glue.run();
								} catch (IllegalStateException ise) {
									// don't quit -- i guess this doesn't need to be rethrown,
									// though again, no idea why.
								}
							}
						}).start();
					}
					return null;
				}
			};

			Class appUtils = Class.forName("com.apple.mrj.MRJApplicationUtils");
			Class paramTypes[] = new Class[] { Class.forName("com.apple.mrj.MRJQuitHandler") };
			Object quitHandler = Proxy.newProxyInstance(getClassLoader(), paramTypes, handler);
			Method register = appUtils.getMethod("registerQuitHandler", paramTypes);
			register.invoke(appUtils.newInstance(), new Object[] { quitHandler });
		} catch (Exception e) {
			// can't happen <=> bug!
			new BugDialog(e);
		}
	}

	// --------------------------------------------------
	// finally, for prefs:
	/*
	    import com.apple.mrj.*;
	    ...
	         MRJApplicationUtils.registerPrefsHandler(new MRJPrefsHandler() {
	            public void handlePrefs() {
	                prefs.run();
	            }
	 */

	@SuppressWarnings("unchecked")
	public static void registerPrefsHandler(Runnable prefs) {
		final Runnable glue = prefs;

		try {
			InvocationHandler handler = new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) {
					if (method.getName().equals("handlePrefs"))
						glue.run();
					return null;
				}
			};

			Class appUtils = Class.forName("com.apple.mrj.MRJApplicationUtils");
			Class paramTypes[] = new Class[] { Class.forName("com.apple.mrj.MRJPrefsHandler") };
			Object prefsHandler = Proxy.newProxyInstance(getClassLoader(), paramTypes, handler);
			Method register = appUtils.getMethod("registerPrefsHandler", paramTypes);
			register.invoke(appUtils.newInstance(), new Object[] { prefsHandler });
		} catch (Exception e) {
			// can't happen <=> bug!
			new BugDialog(e);
		}
	}
	
	// --------------------------------------------------
	// common code:
	private static ClassLoader getClassLoader() {
		return org.tellervo.desktop.platform.OSX.class.getClassLoader();
	}
}
