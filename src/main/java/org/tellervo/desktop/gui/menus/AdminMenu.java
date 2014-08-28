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

package org.tellervo.desktop.gui.menus;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.admin.view.PermissionByEntityDialog;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.core.AppModel;
import org.tellervo.desktop.curation.DatabaseSummaryDialog;
import org.tellervo.desktop.curation.LoanDialog;
import org.tellervo.desktop.curation.SampleStatusDialog;
import org.tellervo.desktop.gis.GISFrame;
import org.tellervo.desktop.gui.AboutBox;
import org.tellervo.desktop.gui.dbbrowse.MetadataBrowser;
import org.tellervo.desktop.gui.menus.actions.MetadatabaseBrowserAction;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.odk.ODKFormDesignPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.ui.TellervoAction;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;


@SuppressWarnings("serial")
public class AdminMenu extends JMenu {
	
	
	
  public static final TellervoAction ABOUT_ACTION = new TellervoAction("menus.about") {
    public void actionPerformed(ActionEvent ae) {
      AboutBox.getInstance().setVisible(true);
   
    }
  };
/** Make a new Admin menu. */
  public AdminMenu(JFrame frame) {
      super(I18n.getText("menus.admin"));
      
      init();
      linkModel();  
  }
  
  protected void linkModel()
  {
	  App.appmodel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if(argEvt.getPropertyName().equals(AppModel.NETWORK_STATUS)){
					setEnabled(App.isLoggedIn());
				}	
			}
		});
	  
	  setEnabled(App.isLoggedIn());
	  
	  if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
	  {
		  this.setVisible(false);
	  }
	  else
	  {
		  this.setVisible(true);
	  }
  }
  
  
  protected void init() {

	  addUserGroupMenu();
	  addPermissionsMenu();
	  addPasswordMenu();
      	addSeparator();
	  addReportsMenu();
	  addLabelMenu();
	  	addSeparator();
	  addCurationMenu();
	  
  }
  
	protected void addPasswordMenu()
  	{
  	  JMenuItem changepwd = Builder.makeMenuItem("menus.admin.changepwd",
              "org.tellervo.desktop.admin.SetPasswordUI.loadDialog()", "password.png");
   	  add(changepwd); 
   	  
   	  
   	  if(App.prefs.getBooleanPref(PrefKey.REMEMBER_PASSWORD, false))
   	  {
   		  JMenuItem forgetpwd = Builder.makeMenuItem("menus.admin.forgetpwd",
   				  "org.tellervo.desktop.gui.menus.AdminMenu.forgetPassword()", "forgetpassword.png");
   		  add(forgetpwd);
   	  }
   	  
  	}
  	
    /**
     * Remove preferences for remembering password and autologging in
     */
    public static void forgetPassword()
    {
		App.prefs.removePref(PrefKey.REMEMBER_PASSWORD);
		App.prefs.removePref(PrefKey.PERSONAL_DETAILS_PASSWORD);
		App.prefs.removePref(PrefKey.AUTO_LOGIN);
		// TODO Would be good to disable or remove the button after this
    }
    
  
    /**
       Add the "Labels" menuitem.
    */
    protected void addLabelMenu() {
    	
    	JMenu labelmenu = Builder.makeMenu("menus.admin.labels", "label.png");

    	
    	JMenuItem boxlabel = Builder.makeMenuItem("menus.admin.boxlabels",
                "org.tellervo.desktop.util.labels.ui.LabelPrintingDialog.boxLabelDialog()", "box.png");
        labelmenu.add(boxlabel);
        
    	JMenuItem basicBoxlabel = Builder.makeMenuItem("Basic box labels",
                "org.tellervo.desktop.util.labels.ui.LabelPrintingDialog.boxBasicLabelDialog()", "box.png");
        labelmenu.add(basicBoxlabel);
    	
        JMenuItem samplelabel = Builder.makeMenuItem("menus.admin.samplelabels",
                "org.tellervo.desktop.util.labels.ui.LabelPrintingDialog.sampleLabelDialog()", "sample.png");
        labelmenu.add(samplelabel);   
        add(labelmenu);
    }
    
    /**
    Add the "Reports" menuitem.
    */
	 protected void addReportsMenu() {
	 	
	 	JMenu reportmenu = Builder.makeMenu("menus.admin.reports", "prosheet.png");
	 	
	 	
	    JMenuItem prosheet = Builder.makeMenuItem("menus.admin.prosheet",
	            "org.tellervo.desktop.util.labels.ui.LabelPrintingDialog.proSheetPrintingDialog()", "prosheet.png");
	    prosheet.setEnabled(false);
	    reportmenu.add(prosheet); 
	 	add(reportmenu);
	 }
 
	 /**
	 Add the "User and groups" menuitem.
	*/
	protected void addUserGroupMenu() {
		
	  	JMenuItem usergroup = Builder.makeMenuItem("menus.admin.usersandgroups",
	            "org.tellervo.desktop.admin.view.UserGroupAdminView.main()", "edit_group.png");
	
	  	
		// Enable if user is an admin
	  	Boolean adm = App.isAdmin;
		usergroup.setEnabled(adm);
		
		add(usergroup);
	}

	protected void addPermissionsMenu(){

	  	JMenuItem permissions = new JMenuItem("Edit/view permissions");
	  	permissions.setIcon(Builder.getIcon("trafficlight.png", 22));
	  	
	  	permissions.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ITridas returned = TridasEntityPickerDialog.pickEntity(null, "Pick Sample", TridasSample.class, EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT);
				
				if(returned==null) return;
				
				PermissionByEntityDialog.showDialog(returned);
				
			}
	  		
	  	});
	

		// Enable if user is an admin
	  	Boolean adm = App.isAdmin;
	  	permissions.setEnabled(adm);
		
		add(permissions);
		
		
		

		
	}
	
	 /**
	 Add the "Curation" menuitem.
	*/
	protected void addCurationMenu() {
		
	 	JMenu curationmenu = Builder.makeMenu("menus.admin.curation", "curation.png");
	 	
	 	
	    JMenuItem findsample = Builder.makeMenuItem("menus.admin.findsample",
	            "org.tellervo.desktop.admin.SampleCuration.showDialog()", "findsample.png");
	    curationmenu.add(findsample); 
	    
	    
	    JMenuItem sampleStatusMenuItem = new JMenuItem("Sample status");
	    sampleStatusMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
			    new SampleStatusDialog();
				
			}
	    
	    });
	    sampleStatusMenuItem.setIcon(Builder.getIcon("samplestatus.png", 22));
	    curationmenu.add(sampleStatusMenuItem);
	    
	    curationmenu.addSeparator();
	    
	    JMenuItem boxdetails = Builder.makeMenuItem("menus.admin.boxdetails",
	            "org.tellervo.desktop.admin.BoxCuration.showDialog()", "box.png");
	    curationmenu.add(boxdetails); 
	    
	    /*JMenuItem checkoutbox = Builder.makeMenuItem("menus.admin.checkoutbox",
	            "org.tellervo.desktop.admin.BoxCuration.checkoutBox()", "checkout.png");
	    curationmenu.add(checkoutbox); 
	    
	    JMenuItem checkinbox = Builder.makeMenuItem("menus.admin.checkinbox",
	            "org.tellervo.desktop.admin.BoxCuration.checkinBox()", "checkin.png");
	    curationmenu.add(checkinbox); 
	    
	    JMenuItem inventory = Builder.makeMenuItem("menus.admin.inventory",
	            "org.tellervo.desktop.util.labels.ui.LabelPrintingDialog.proSheetPrintingDialog()");
	    inventory.setEnabled(false);
	    curationmenu.add(inventory); */
	    
	    curationmenu.addSeparator();
	    
	    JMenuItem loanDialog = new JMenuItem("Browse loans...");
	    final Component glue = this;
	    loanDialog.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				LoanDialog dialog = new LoanDialog(glue);
				dialog.setVisible(true);
			}
	    	
	    });
	    curationmenu.add(loanDialog);
	    
	    JMenuItem newLoan = new JMenuItem("New loan...");
	    newLoan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LoanDialog.showNewLoanDialog(glue);
			}
	    });
	    curationmenu.add(newLoan);
	    
	    


	    JMenuItem statsMenuItem = new JMenuItem("Database statistics");
	    statsMenuItem.setIcon(Builder.getIcon("statistics.png", 22));
	    statsMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
			    DatabaseSummaryDialog dialog = new DatabaseSummaryDialog();
			    dialog.setVisible(true);
				
			}
	    
	    });
	    add(statsMenuItem);
	    
	 	add(curationmenu);
	 	addSeparator();
	 	
	 	JMenuItem buildODKForm = new JMenuItem("Design ODK form");
	 	buildODKForm.setIcon(Builder.getIcon("odk.png", 22));
	 	buildODKForm.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ODKFormDesignPanel panel = new ODKFormDesignPanel();
				JDialog dialog = new JDialog();
				dialog.setLayout(new BorderLayout());
				dialog.add(panel, BorderLayout.CENTER);
				dialog.pack();
				dialog.setVisible(true);
				
			}
	 		
	 	});
	 	add(buildODKForm);
	 	
	 	Action metadbAction = new MetadatabaseBrowserAction();
	 	JMenuItem metadb = new JMenuItem(metadbAction);
	 	add(metadb);

	 	
	 	JMenuItem showMap = Builder.makeMenuItem("general.sitemap", "org.tellervo.desktop.gui.menus.AdminMenu.showMap()", "globe.png");
	 	add(showMap);
	 	// Disable if OpenGL support is screen
	 	showMap.setEnabled(!App.prefs.getBooleanPref(PrefKey.OPENGL_FAILED, false));	 	

	}
    
	public static void metadataBrowser(){
		MetadataBrowser dialog = new MetadataBrowser(App.mainWindow, false);
		dialog.setVisible(true);
	}
	
	public static void showMap(){
			
		GISFrame map = new GISFrame(false);
		map.setVisible(true);

    
		
	}
}
