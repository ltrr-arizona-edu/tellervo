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

package org.tellervo.desktop.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.AboutBox;
import org.tellervo.desktop.gui.Help;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.TellervoAction;
import org.tellervo.desktop.ui.I18n;

import com.dmurph.mvc.MVC;


/**
   A Help menu.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class HelpMenu extends JMenu {

	private static final long serialVersionUID = -1495245171423158200L;
	private JMenuItem debugMenu;
	
	/**
	 * Create a help menu
	 */
    public HelpMenu() {
	super(I18n.getText("menus.help"));

	    addHelpMenu();
	    addSetupWizardMenu();
        addSeparator();
        addErrorLogMenu();
        addSeparator();
        addSystemInfoMenu();
		if (!Platform.isMac()) {
		    addSeparator();
		    addAboutMenu();
		}
		
    }

    

    
    private void addSetupWizardMenu() {
    	JMenuItem helpWizard = Builder.makeMenuItem("menus.help.setupWizard", true, "wizard.png");

    	helpWizard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				SetupWizard.launchWizard();
				
			}
		});
		
		add(helpWizard);
		
	}




	/**
       Add the "Corina Help" menuitem.
    */
    protected void addHelpMenu() {

		JMenuItem helpwiki = Builder.makeMenuItem("menus.help.contents", true, "help.png");

		helpwiki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Help.showHelpIndex();
				
			}
		});
		
		add(helpwiki);
    }
    

    /**
       Add the "System Properties..." menuitem.
    */
    protected void addSystemInfoMenu() {
        add(Builder.makeMenuItem("menus.help.system_info",
                                 "org.tellervo.desktop.util.PropertiesWindow.showPropertiesWindow()", "system.png"));
    }

    /**
     * Add the "Error Log..." menuitem.
     * FIXME: not really just an "error" log... more like "activity" log.
     */
    protected void addErrorLogMenu() {
        //add(Builder.makeMenuItem("menus.help.error_log", "org.tellervo.desktop.gui.ErrorLog.showLogViewer()", "log.png"));
        JMenuItem logviewer = new JMenuItem(I18n.getText("menus.help.error_log"));
        logviewer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				App.setLogViewerVisible(true);
				
			}
        	
        });
        logviewer.setIcon(Builder.getIcon("bug.png", 22));
        add(logviewer);
    	
    	//add(Builder.makeMenuItem("menus.help.error_log", "org.tellervo.desktop.gui.Log4JViewer.showLogViewer()", "log.png"));
        add(Builder.makeMenuItem("menus.help.error_ws", "org.tellervo.desktop.wsi.TransactionDebug.forceGenerateWSBug()", "bugreport.png"));

        debugMenu = Builder.makeMenuItem("menus.help.xml_debug", "org.tellervo.desktop.gui.XMLDebugView.showDialog()", "networksettings.png");
        add(debugMenu);
        

        add(Builder.makeMenuItem("menus.help.showmvcmonitor", "com.dmurph.mvc.MVC.showEventMonitor()"));
        
        //add(Builder.makeMenuItem("debug_instantiator", "org.tellervo.desktop.gui.DebugInstantiator.showMe()"));
        //add(Builder.makeMenuItem("debug_instantiator", "org.tellervo.desktop.gui.newui.NewJFrame1.main()"));
    }

    /**
       Add the "About Corina..." menuitem.
    */
    protected void addAboutMenu() {
      JMenuItem menuitem = Builder.makeMenuItem("menus.about");
      menuitem.setAction(ABOUT_ACTION);
      menuitem.setIcon(Builder.getIcon("info.png", 22));
      add(menuitem);
    }
    
	public static final TellervoAction ABOUT_ACTION = new TellervoAction("menus.about") {
		private static final long serialVersionUID = -6930373548175605620L;
		public void actionPerformed(ActionEvent ae) {
	      AboutBox.getInstance().setVisible(true);
	    }
	  };
}
