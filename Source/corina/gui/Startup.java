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

import java.awt.Toolkit;
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

import corina.core.App;
import corina.core.ProgressListener;

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
      // TODO: implement progress listeners and splash screen for real
      ProgressListener dummy = new ProgressListener() {
        public void setLimit(int i) { /* do nothing */ }
        public void setValue(int i) { /* do nothing */ }
        public void setNote(String string) { /* do nothing */ }
      };
      App.init(dummy, dummy);

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
    Subject.doAsPrivileged(mySubject, new Startup(args), null);
    //Subject.doAs(mySubject, new Startup(args));
  }
}