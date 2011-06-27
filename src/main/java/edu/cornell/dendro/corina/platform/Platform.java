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
package edu.cornell.dendro.corina.platform;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.UIManager;

import edu.cornell.dendro.corina.core.AbstractSubsystem;
import edu.cornell.dendro.corina.logging.CorinaLog;

/**
 * Platform subsystem that takes care of platform-specific things.
 * @author Aaron Hamid
 */
public class Platform extends AbstractSubsystem {
	private static final CorinaLog log = new CorinaLog(Platform.class);

	private boolean isMac;
	private boolean isWindows;
	private boolean isUnix;
	private Desktop desktop;
	
	
	public String getName() {
		return "Platform";
	}

	@Override
	public void init() {
		super.init();
		
		if(Desktop.isDesktopSupported())
		{
			desktop = Desktop.getDesktop();
		}
		else
		{
			System.out.println("This OS is not supported by the Java6 Desktop API!");
		}
		
		//method from TN2042, http://developer.apple.com/technotes/tn/tn2042.html
		isMac = System.getProperty("mrj.version") != null;
		String osname = System.getProperty("os.name");
		isWindows = osname != null && osname.indexOf("Windows") != -1;
		isUnix = !isMac && !isWindows; // assume it's one of mac, win32, unix

		// on a mac, always use the mac menubar -- see TN2031
		// (http://developer.apple.com/technotes/tn/tn2031.html)
		// this MUST be done before setting the look and feel
		if (isMac) {
			// Use the right kind of menu bars
			System.setProperty("apple.laf.useScreenMenuBar", "true");

			// this sets the "about..." name only -- not "hide", "quit", or in the dock.
			// have to use -X args for those, anyway, so this is useless.
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Corina");
			// System.setProperty("com.apple.mrj.application.live-resize", "true");

			// also, treat apps as files, not folders (duh -- why's this not default, steve?)
			System.setProperty("com.apple.macos.use-file-dialog-packages", "false"); // for AWT
			UIManager.put("JFileChooser.packageIsTraversable", "never"); // for swing
		}

		// try to get the native L&F
		String slafclassname = UIManager.getSystemLookAndFeelClassName();

		if (slafclassname != null)
			try {
				if(isUnix)
				{
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

				}
				else
				{
					UIManager.setLookAndFeel(slafclassname);
				}	
				
			} catch (Exception e) {
				log.error("Error setting system look and feel class", e);
			}
		
		// set up mac menubar
		Macintosh.configureMenus();

		setInitialized(true);
	}

	@Override
	public void destroy() {
		super.destroy();
		setInitialized(false);
		// don't need to do anything on destroy
	}

	public static boolean isMac() {
	    String osName = System.getProperty("os.name");
	    return osName.startsWith("Mac OS X");
	}

	public boolean isWindows() {
		return isWindows;
	}

	public boolean isUnix() {
		return isUnix;
	}

	/**
	 * Open a folder in the native file manager
	 * 
	 * @param folder
	 */
	public void open(String folder) {
		File file = new File(folder);
		openFile(file);
	}
	
	/**
	 * Open a file in the default native application
	 * 
	 * @param file
	 */
	public void openFile(File file) {
		
		try {
			desktop.open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Open a URI in the default web browser
	 * 
	 * @param uri
	 */
	public void openURL(URI uri)
	{
        try {
            desktop.browse(uri);
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }

	}

	// get the name of the trash folder
	public String getTrash() {
		if (isWindows)
			return "C:\\recycled\\"; // do they still not have a per-user trash?  just use the trash on C:\.

		if (isMac)
			return System.getProperty("user.home") + "/.Trash/";

		return null; // what to do on unix?
	}

	// mac-only -- windows prepends "* "
	public void setModified(JFrame window, boolean mod) {
		if (isMac)
			window.getRootPane().putClientProperty("windowModified",
					mod ? Boolean.TRUE : Boolean.TRUE);
		// no news is good news

		// REFACTOR: add this to xframe, or whatever my document window is
		// (is that really what i want?)
	}

	// for DnD, the key you press to copy, instead of move.
	public String getCopyModifier() {
		if (isMac)
			return "alt"; // option, really
		else
			return "control";
		// i have no idea what it is on generic unix.  does motif specify?
	}
	


	  
	
	  

	  
}
