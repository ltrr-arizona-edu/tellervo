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

package corina.gui;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;
import java.security.AccessControlContext;
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

import corina.prefs.Prefs;
import corina.util.CorinaLog;
import corina.util.Macintosh;
import corina.util.Netware;
import corina.util.Platform;

/**
 * Bootstrap for Corina. It all starts here...
 * 
 * <h2>Left to do</h2>
 * <ul>
 * <li>extract Bootstrap, which will make testing much easier (Startup =
 * Bootstrap + new XCorina())
 * </ul>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at </i> cornell <i
 *         style="color: gray">dot </i> edu&gt;
 * @version $Id$
 */
public class Startup implements PrivilegedAction {
  private String[] args;

  private Startup(String[] args) {
    this.args = args;
  }

  public Object run() {
    Subject subject = Subject.getSubject(AccessController.getContext());
    if (subject != null) {
      // replace the event queue with one that has the Subject stored
      Toolkit.getDefaultToolkit().getSystemEventQueue().push(new AccessControlContextEventQueue());
    }

    /*
     * Font f = new Font("courier", java.awt.Font.PLAIN, 24);
     * UIManager.put("Menu.font", f); UIManager.put("MenuItem.font", f);
     */
    try {
      // if the user hasn't specified a parser with
      // -Dorg.xml.sax.driver=..., use crimson.
      if (System.getProperty("org.xml.sax.driver") == null)
        System.setProperty("org.xml.sax.driver",
            "org.apache.crimson.parser.XMLReaderImpl");
      // xerces: "org.apache.xerces.parsers.SAXParser"
      // gnu/jaxp: "gnu.xml.aelfred2.SAXDriver"

      // try to get the native L&F
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

      // on a mac, always use the mac menubar -- see TN2031
      // (http://developer.apple.com/technotes/tn/tn2031.html)
      // REFACTOR: move this to Platform?
      if (Platform.isMac) {
        // REFACTOR: make a Platform.JVMVersion field?
        if (System.getProperty("java.version").startsWith("1.4"))
          System.setProperty("apple.laf.useScreenMenuBar", "true");
        else
          System.setProperty("com.apple.macos.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name",
            "Corina");
        // System.setProperty("com.apple.mrj.application.live-resize", "true");

        // also, treat apps as files, not folders (duh -- why's this not
        // default, steve?)
        System.setProperty("com.apple.macos.use-file-dialog-packages", "false"); // for
                                                                                 // AWT
        UIManager.put("JFileChooser.packageIsTraversable", "never"); // for
                                                                     // swing
      }

      // this sets the "about..." name only -- not "hide", "quit", or in the
      // dock.
      // have to use -X args for those, anyway, so this is useless.
      // System.setProperty("com.apple.mrj.application.apple.menu.about.name",
      // "Corina");

      // migrate old prefs (!!!)
      // WAS: Migrate.migrate();

      // load properties -- messagedialog here is UGLY!
      try {
        //Prefs.load();
        Prefs.init();
        //AppContext.init();
      } catch (IOException ioe) {
        JOptionPane.showMessageDialog(null,
            "While trying to load preferences:\n" + ioe.getMessage(),
            "Corina: Error Loading Preferences", JOptionPane.ERROR_MESSAGE);
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

      // can't install a new default exception handler, but i can log them
      //ErrorLog.logErrors();
      CorinaLog.init();
      // (i COULD make this log call bug.bug() ...)

      // set up mac menubar
      Macintosh.configureMenus();

      // let's go...
      XCorina.showCorinaWindow();

    } catch (Exception e) {
      Bug.bug(e);
    }
    return null;
  }

  private static final class PasswordDialogCallbackHandler implements
      CallbackHandler {
    private boolean prompted = false;
    private String user;
    private String pass;

    public void handle(Callback[] callbacks) {
      System.out.println("HANDLE");
      Thread.currentThread().getThreadGroup().list();
      if (prompted) return;

      JTextField nameField = new JTextField();
      JTextField passField = new JTextField();
      int option = JOptionPane.showOptionDialog(null,
                                                new Object[] { "user name", nameField, "password", passField },
                                                "Login",
                                                JOptionPane.OK_CANCEL_OPTION,
                                                JOptionPane.QUESTION_MESSAGE,
                                                null, null, null);
      prompted = true;
      System.out.println("HANDLE2");
      Thread.currentThread().getThreadGroup().list();
      if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION) {
        return;
      }

      for (int i = 0; i < callbacks.length; i++) {
        System.out.println("Callback " + i + ": " + callbacks[i]);
        if (callbacks[i] instanceof NameCallback) {
          ((NameCallback) callbacks[i]).setName(user);
        } else if (callbacks[i] instanceof PasswordCallback) {
          ((PasswordCallback) callbacks[i]).setPassword(pass.toCharArray());
        }
      }
    }
  }

  /**
   * The <code>main()</code> method that sets all of Corina in motion. Loads
   * system and user preferences, and instantiates an XCorina object.
   * @param args
   *          command-line arguments; ignored
   */
  public static void main(String args[]) {
    if (args.length == 0 || !"-a".equals(args[0])) {
      new Startup(args).run();
      return;
    }

    // Obtain a LoginContext, needed for authentication. Tell it
    // to use the LoginModule implementation specified by the
    // entry named "Corina" in the JAAS login configuration
    // file and to also use the specified CallbackHandler.
    LoginContext lc = null;
    try {
      lc = new LoginContext("Corina", new PasswordDialogCallbackHandler());
    } catch (LoginException le) {
      System.err.println("Cannot create LoginContext. " + le.getMessage());
      System.exit(-1);
    } catch (SecurityException se) {
      System.err.println("Cannot create LoginContext. " + se.getMessage());
      System.exit(-1);
    }

    int i;
    for (i = 0; i < 3; i++) {
      try {
        // attempt authentication
        lc.login();

        break;
      } catch (LoginException le) {

        System.err.println("Authentication failed:");
        System.err.println("  " + le.getMessage());
        System.exit(-1);

      }
    }
    // did they fail three times?
    if (i == 3) {
      System.out.println("Sorry");
      System.exit(-1);
    }

    System.out.println("Authentication succeeded!");

    Subject mySubject = lc.getSubject();
    Subject.doAs(mySubject, new Startup(args));
  }
}