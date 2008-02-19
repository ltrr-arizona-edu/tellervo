package edu.cornell.dendro.corina.platform;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UIManager;

import edu.cornell.dendro.corina.core.AbstractSubsystem;
import edu.cornell.dendro.corina.gui.Bug;
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

	public String getName() {
		return "Platform";
	}

	@Override
	public void init() {
		super.init();

		//method from TN2042, http://developer.apple.com/technotes/tn/tn2042.html
		isMac = System.getProperty("mrj.version") != null;
		String osname = System.getProperty("os.name");
		isWindows = osname != null && osname.indexOf("Windows") != -1;
		isUnix = !isMac && !isWindows; // assume it's one of mac, win32, unix

		// try to get the native L&F
		String slafclassname = UIManager.getSystemLookAndFeelClassName();

		/**
		// smooth look and feel, anyone?
		if(isWindows) {
			slafclassname = "smooth.windows.SmoothLookAndFeel";
		}
		 **/

		if (slafclassname != null)
			try {
				UIManager.setLookAndFeel(slafclassname);
			} catch (Exception e) {
				log.error("Error setting system look and feel class", e);
			}

		// this stuff moved from Startup

		// on a mac, always use the mac menubar -- see TN2031
		// (http://developer.apple.com/technotes/tn/tn2031.html)
		// REFACTOR: move this to Platform?
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

		// using windows with netware, netware doesn't tell windows the real
		// username
		// and home directory. here's an ugly workaround to set user.* properties,
		// if they're there. (old way: always call with "java -Duser.home=...",
		// and have the user type in her name -- ugh.) by doing this after the
		// prefs
		// loading, i override anything the user set in the prefs (unless they
		// set it again -- hence it should be removed).
		// try {
		Netware.workaround();
		// } catch (IOException ioe) {
		// Bug.bug(ioe);
		// }

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

	public boolean isMac() {
		return isMac;
	}

	public boolean isWindows() {
		return isWindows;
	}

	public boolean isUnix() {
		return isUnix;
	}

	/**
	 * Open a folder in the system file browser
	 */
	public void open(String folder) {
		String[] command;
		if (isWindows) {
			// if file, "explorer /select,FILENAME"
			// else, "explorer FILENAME"
			boolean isDir = new File(folder).isDirectory();
			command = new String[] { "explorer",
					(isDir ? folder : "/select," + folder) };

			// TODO: this is completely untested!

			// note: in my old SiteInfo.java, i used
			// ("c:\\winnt\\system32\\cmd.exe" "/c" "start" folder)
		} else if (isMac) {
			command = new String[] { "open", "-a",
					"/System/Library/CoreServices/Finder.app/", folder };

			// REFACTOR: make into methods openFolder(folder, file), openFolder(folder)?

			// TODO: should i snarf up stdout/stderr?
			// i think mac is smart enough i don't have to.

			// (don't bother watching return value; it can't fail)

			// note: in my old SiteInfo.java, i used
			// ("/usr/bin/open" folder)
		} else {
			// what to do on unix?  gmc, konqueror, xterm?
			new Bug(new IllegalArgumentException(
					"Platform.open() not implemented on unix yet!"));
			return;
		}

		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException ioe) {
			new Bug(ioe);
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