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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.gui.menus;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import corina.core.App;
import corina.gui.AboutBox;
import corina.ui.Builder;
import corina.ui.CorinaAction;
import corina.ui.I18n;

// TODO: move all menus to corina.gui.menus or even corina.menus (i'm tending towards the latter)
// TODO: error-log should be a singleton-window, and centered
// TODO: system-info should be a singleton-window, and centered
// TODO: perhaps also provide a menuitem which takes you to the corina web page?
// TODO: the error log should be just a text dump window, perhaps
// TODO: need a complaint menu item; perhaps "Submit Complaint..."

/**
   A Help menu.

   <p>Standard menuitems are:</p>

   <ul>
     <li>Corina Help</li>
     <br>
     <li>System Properties...</li>
     <li>Error Log...</li>
     <br>
     <li>About Corina... (except on Mac)</li>
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class HelpMenu extends JMenu {
  public static final CorinaAction ABOUT_ACTION = new CorinaAction("about") {
    public void actionPerformed(ActionEvent ae) {
      AboutBox.getInstance().show();
    }
  };
    // FIXME: do i need(want) to set font here for 1.3?  do i inherit that from my parents?

    /**
       Make a new standard Help menu.
    */
    public HelpMenu() {
	super(I18n.getText("help"));

	addHelpMenu();

        addSeparator();

        addSystemInfoMenu();
        addErrorLogMenu();
        
	if (!App.platform.isMac()) {
	    addSeparator();
	    addAboutMenu();
	}
    }

    /**
       Add the "Corina Help" menuitem.
    */
    protected void addHelpMenu() {
	add(Builder.makeMenuItem("corina_help",
				 "corina.gui.Help.showHelp()"));
    }

    /**
       Add the "System Properties..." menuitem.
    */
    protected void addSystemInfoMenu() {
        add(Builder.makeMenuItem("system_info...",
                                 "corina.util.PropertiesWindow.showPropertiesWindow()"));
    }

    /**
     * Add the "Error Log..." menuitem.
     * FIXME: not really just an "error" log... more like "activity" log.
     */
    protected void addErrorLogMenu() {
      add(Builder.makeMenuItem("error_log...", "corina.gui.ErrorLog.showLogViewer()"));
    }

    /**
       Add the "About Corina..." menuitem.
    */
    protected void addAboutMenu() {
      JMenuItem menuitem = Builder.makeMenuItem("about");
      menuitem.setAction(ABOUT_ACTION);
      add(menuitem);
    }
}
