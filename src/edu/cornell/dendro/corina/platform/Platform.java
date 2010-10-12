package edu.cornell.dendro.corina.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

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
			File file = new File(folder);
			openLinuxFile(file, null);
			return;
		}

		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException ioe) {
			new Bug(ioe);
		}
	}
	
	public void openFile(File file) {
		if(isWindows)
			Windows.openFile(file);
		else if(isMac)
			Macintosh.openFile(file);
		else{
			
	    	String filetype = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
    		openLinuxFile(file, filetype);
	    	return;
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
	
	  /**
	   * Open a file on a Linux machine 
	   * 
	   * @param file
	   * @param filetype
	   */
	  public void openLinuxFile(File file, String filetype)
	  {
		  if(!isUnix) return;
		  
		  String app = getLinuxApplicationFilename(filetype);
		  if(app==null || app.equals(""))
		  {
			  return;
		  }
		  
	      String[] command = new String[] { app, file.getAbsolutePath() };
	            
		  try {
		    Runtime.getRuntime().exec(command);
		  } catch (IOException ioe) {
		    new Bug(ioe);
		  }
	  }
	  
	  /**
	   * Check whether a file exists
	   * 
	   * @param filename
	   * @return
	   */
	  private Boolean checkFileExists(String filename)
	  {
		  File f = new File(filename);
		  return f.exists();
	  }
	  
	  /**
	   * Locate an application on a Linux machine by file type. This
	   * is a bit of a kluding function that hunts for popular programs
	   * based on file extensions.  This would be better done by reading
	   * the .desktop files.
	   *   
	   * Supported types includes pdf, doc, txt, htm, html.
	   * 
	   * The funcion will return the filename for a file manager
	   * application if an unsupported file type is provided.  
	   * 
	   * Function assumes executables are location in /usr/bin.
	   * 
	   * @param filetype
	   * @return
	   */
	  private String getLinuxApplicationFilename(String filetype)
	  {
		  if(!isUnix) return null;
		  
		  if(filetype.toLowerCase().equals("pdf"))
		  {
			  String[] appNames = {"acroread", "okular", "evince", "gv", "kpdf", "gpdf", "xpdf"};
			  for(String app : appNames)
			  {
				  if(checkFileExists("/usr/bin/"+app))
				  {
					  return "/usr/bin/"+app;
				  }
			  }
			  new Bug(new IOException("Unable to find a PDF viewing application"));
		  }
		  else if ( (filetype.toLowerCase().equals("doc")) || 
				    (filetype.toLowerCase().equals("txt")) 
				  )
		  {
			  String[] appNames = {"oowriter", "ooffice", "abiword", "kword"};
			  for(String app : appNames)
			  {
				  if(checkFileExists("/usr/bin/"+app))
				  {
					  return "/usr/bin/"+app;
				  }
			  }
			  new Bug(new IOException("Unable to find an office application"));
		  }
		  else if ( (filetype.toLowerCase().equals("htm")) || 
				    (filetype.toLowerCase().equals("html")) 
				  )
		  {
			  String[] appNames = {"firefox", "mozilla-firefox", "rekonq", "konqueror", 
					  "iceweasel", "opera", "seamonkey", "chromium", "galeon", "epiphany", "amaya"};
			  for(String app : appNames)
			  {
				  if(checkFileExists("/usr/bin/"+app))
				  {
					  return "/usr/bin/"+app;
				  }
			  }
			  new Bug(new IOException("Unable to find a web browser"));
		  }
		  else 
		  {
			  // Null filetype means folder
			  String[] appNames = {"dolphin", "nautilus", "thunar", "xfe", "krusader"};
			  for(String app : appNames)
			  {
				  if(checkFileExists("/usr/bin/"+app))
				  {
					  return "/usr/bin/"+app;
				  }
			  }
			  new Bug(new IOException("Unable to find a file manager application"));
		  }
		  	
		  		  
		 
		  return null;
	  }
	  
	  
	  /**
	   * Get the .desktop file for the document filetype specified.
	   * Until .desktop files are a little more uniform this isn't a practical
	   * way of doing things.  Use getLinuxApplicationFilename() instead.
	   * 
	   * @param filetype
	   * @return
	   */
	  private String getLinuxPreferredApplication(String filetype)
	  {	  
	/*	    if(!isUnix) return null;
		  
			Scanner scanner = null;
			try {
				scanner = new Scanner(new FileInputStream("/usr/share/applications/defaults.list"));
			} catch (FileNotFoundException e) {
		    	new Bug(new IOException("Unabled to determine the preferred application for the filetype: "+filetype));
		    	return null;
			}
			try {
			  while (scanner.hasNextLine())
			  {
				  
				  String line = scanner.nextLine();
				  if(line.startsWith("application/"+filetype))
				  {
					  return "/usr/share/applications/"+line.substring(line.indexOf("=")+1);
				  }
			  }
			}
			finally{
				scanner.close();
				
			}
	    	new Bug(new IOException("Unabled to determine the preferred application for the filetype: "+filetype));
	    	
	    	*/
	    	return null;
	  }
	  
}