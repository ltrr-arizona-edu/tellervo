/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

package org.tellervo.desktop.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.gis2.OpenGLTestCapabilities;
import org.tellervo.desktop.model.TellervoModelLocator;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.ui.Builder;


/**
 * Bootstrap for Tellervo. It all starts here...
 * 
 * <h2>Left to do</h2>
 * <ul>
 * <li>extract Bootstrap, which will make testing much easier (Startup =
 * Bootstrap + new TellervoMainWindow())
 * </ul>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at </i> cornell <i
 *         style="color: gray">dot </i> edu&gt;
 * @version $Id$
 */
public class Startup  {
	private final static Logger log = LoggerFactory.getLogger(Startup.class);
	
	private Startup(String[] args) {
	}

	public Object run() {
		Subject subject = Subject.getSubject(AccessController.getContext());
		if (subject != null) {
			// replace the event queue with one that has the Subject stored
			Toolkit.getDefaultToolkit().getSystemEventQueue().push(
					new AccessControlContextEventQueue());
		}

		/*
		 * Font f = new Font("courier", java.awt.Font.PLAIN, 24);
		 * UIManager.put("Menu.font", f); UIManager.put("MenuItem.font", f);
		 */
		try {		    
		    // now, init the platform. This sets look/feel and Mac toolbars and such.
		    App.platform = new Platform();
		    App.platform.init();
			
			ClassLoader cl = this.getClass().getClassLoader();
			URL url = cl.getResource("Images/splashscreen.png");
			BufferedImage img = null;
		
			if (url != null) {
				img = javax.imageio.ImageIO.read(url);
			}
			Splash splash = new Splash("Tellervo", img);
			ProgressMeter pm = new ProgressMeter();
			
			Dimension d = new Dimension(img.getWidth(), img.getHeight());	
			splash.setMinimumSize(d);
			splash.setMaximumSize(d);
			splash.setSize(d);	
			
			
			pm.addProgressListener(splash);
			pm.setMillisToDecideToPopup(0);
			pm.setMillisToPopup(0);
			
			App.init(pm, splash);
			// monitor.close();
			// let's go...

			
			// Open the editor
			if(App.isTellervoLiteMode())
			{
				// Lite mode
				LiteEditor editor = LiteEditor.getNewInstance();
				App.mainWindow = editor;
				editor.setVisible(true);
			}
			else
			{
				// Full mode - but first check OpenGL capabilities.
				if(OpenGLTestCapabilities.isOpenGLCapable())
				{
					log.debug("Computer is OpenGL capable");
				}
				else
				{
					log.warn("Computer is not OpenGL capable. Mapping will be disabled");
					log.warn("The following debug report may be helpful...");
					log.warn(OpenGLTestCapabilities.getOpenGLProblems());
				}
				
				// Full Tellervo uses editor as main window
				FullEditor editor = FullEditor.getInstance();
				App.mainWindow = editor;
				editor.setVisible(true);

			}
			
		} catch (Throwable t) {
			new Bug(t);
		}
		return null;
	}

	/**
	 * The <code>main()</code> method that sets all of Tellervo in motion. Loads
	 * system and user preferences, and instantiates an TellervoMainWindow object.
	 * 
	 * @param args
	 *            command-line arguments; ignored
	 */
	public static void main(String args[]) {
		// initialize controllers
		TellervoModelLocator.getInstance();

		if (args.length == 0 || !"-a".equals(args[0])) {
			new Startup(args).run();
			return;
		}
		
		UIManager.put("wizard.sidebar.image", Builder.getImage("background3.png"));


	}
}
