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
package org.tellervo.desktop.prefs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.panels.AbstractPreferencesPanel;
import org.tellervo.desktop.prefs.panels.AppearancePrefsPanel;
import org.tellervo.desktop.prefs.panels.HardwarePrefsPanel;
import org.tellervo.desktop.prefs.panels.MappingPrefsPanel;
import org.tellervo.desktop.prefs.panels.NetworkPrefsPanel;
import org.tellervo.desktop.prefs.panels.GeneralPrefsPanel;
import org.tellervo.desktop.prefs.panels.StatsPrefsPanel;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.WSIServerDetails;
import org.tellervo.desktop.wsi.WSIServerDetails.WSIServerStatus;

import net.miginfocom.swing.MigLayout;

import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;


public class PreferencesDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButtonBar buttonBar;
	private JPanel toolbarPanel;
	private JLayeredPane mainPanel;
	private JLabel lblTitle;
	private JLabel lblSubtitle;
	private ButtonGroup pageButtons = new ButtonGroup();
	private JButton btnResetAllPreferences;
	private ArrayList<AbstractPreferencesPanel> pageList = new ArrayList<AbstractPreferencesPanel>();
	
	
	
	
	/**
	 * Create the dialog.
	 */
	public PreferencesDialog() {
	
		// Define the pages of the preferences panels
		registerPreferencesPage(new NetworkPrefsPanel(this));
		registerPreferencesPage(new HardwarePrefsPanel(this));
		registerPreferencesPage(new GeneralPrefsPanel(this));
		registerPreferencesPage(new AppearancePrefsPanel(this));
		registerPreferencesPage(new MappingPrefsPanel(this));
		registerPreferencesPage(new StatsPrefsPanel(this));


		
		setIconImage(Builder.getApplicationIcon());
		setTitle("Preferences");
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			toolbarPanel = new JPanel();
			toolbarPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			toolbarPanel.setBackground(Color.WHITE);
			contentPanel.add(toolbarPanel, BorderLayout.WEST);
		}
		{

			
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
		    JScrollPane scrollPane = new JScrollPane(panel,   
		    		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		    
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(10, 10));
			{
				mainPanel = new JLayeredPane();
				mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				panel.add(mainPanel);
			}

		}
		{
			JPanel titlePanel = new JPanel();
			titlePanel.setBackground(Color.WHITE);
			contentPanel.add(titlePanel, BorderLayout.NORTH);
			titlePanel.setLayout(new MigLayout("", "[65px,grow][]", "[15px][]"));
			{
				lblTitle = new JLabel("Corina Preferences");
				lblTitle.setFont(new Font("Dialog", Font.BOLD, 14));
				titlePanel.add(lblTitle, "cell 0 0,alignx left,aligny top");
			}
			{
				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				titlePanel.add(panel, "cell 1 0 1 2,grow");
				{
					JLabel lblIcon = new JLabel();
					lblIcon.setIcon(Builder.getIcon("advancedsettings.png", 48));
					panel.add(lblIcon);
				}
			}
			{
				lblSubtitle = new JLabel("Select the preferences category to edit");
				lblSubtitle.setFont(new Font("Dialog", Font.PLAIN, 10));
				titlePanel.add(lblSubtitle, "cell 0 1,aligny top");
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new MigLayout("", "[][54px,grow][81px]", "[3:3:3][25px]"));
			{
				JSeparator separator = new JSeparator();
				buttonPane.add(separator, "cell 0 0 3 1");
			}
			{
				btnResetAllPreferences = new JButton("Reset all to default");
				
				
				
				btnResetAllPreferences.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						int response = JOptionPane.showConfirmDialog(contentPanel, "Resetting your preferences will mean Tellervo acts as a fresh install.\n\nAre you sure you want to continue?");
						
						if(response == JOptionPane.OK_OPTION)
						{
							Boolean success = App.prefs.resetPreferences();
							
							if(success)
							{
								int n = JOptionPane.showConfirmDialog(
										App.mainWindow,
									    "You will need to restart Tellervo for the changes to take effect.\n" +
									    "Would you like to restart now?  Any unsaved changes will be lost.",
									    "Restart required",
									    JOptionPane.YES_NO_CANCEL_OPTION);

								if(n == JOptionPane.YES_OPTION)
								{
									try {
										App.restartApplication();
									} catch (Exception e1) {
										Alert.message("Manual restart required", 
										"Unable to restart Tellervo automatically.  Please restart manually!");
									}
								}
								else if (n == JOptionPane.CANCEL_OPTION)
								{
									return;
								}	
							}
							else
							{
								Alert.error("Error", "Unable to reset preferences. Please contact the developers.");
							}
						}
						
					}
					
					
				});
				
				buttonPane.add(btnResetAllPreferences, "cell 0 1");
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						
						NetworkPrefsPanel networkPrefsPanel = (NetworkPrefsPanel) getPreferencesPage(NetworkPrefsPanel.class);
						if(networkPrefsPanel==null) return;						
						
						if(networkPrefsPanel.hasWSURLChanged())
						{
							// First test to see if the WS connection is working
							Boolean wsok = networkPrefsPanel.testWSConnection(false);
							if(wsok.equals(false)) return;							

							int n = JOptionPane.showConfirmDialog(
								    App.mainWindow,
								    "You will need to restart Tellervo for web service changes to take effect.\n" +
								    "Would you like to restart now?  Any unsaved changes will be lost.",
								    "Restart required",
								    JOptionPane.YES_NO_CANCEL_OPTION);

							if(n == JOptionPane.YES_OPTION)
							{
								try {
									App.restartApplication();
								} catch (Exception e1) {
									Alert.message("Manual restart required", 
									"Unable to restart Tellervo automatically.  Please restart manually!");
								}
							}
							else if (n == JOptionPane.CANCEL_OPTION)
							{
								return;
							}	
						}
						
						AppearancePrefsPanel appearancePrefsPanel = (AppearancePrefsPanel) getPreferencesPage(AppearancePrefsPanel.class);
						if(appearancePrefsPanel==null) return;						
						
						if(appearancePrefsPanel.hasLocaleChanged())
						{
							int n = JOptionPane.showConfirmDialog(
									App.mainWindow,
								    "You will need to restart Tellervo for the new language to take effect.\n" +
								    "Would you like to restart now?  Any unsaved changes will be lost.",
								    "Restart required",
								    JOptionPane.YES_NO_CANCEL_OPTION);

							if(n == JOptionPane.YES_OPTION)
							{
								try {
									App.restartApplication();
								} catch (Exception e1) {
									Alert.message("Manual restart required", 
									"Unable to restart Tellervo automatically.  Please restart manually!");
								}
							}
							else if (n == JOptionPane.CANCEL_OPTION)
							{
								return;
							}	
						}

						// Hide the dialog (don't close it)
						setVisible(false);						
					}
					
				});
				buttonPane.add(okButton, "cell 2 1,growx,aligny top");
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setupPages();
		pack();

		setLocationRelativeTo(null); 
		this.setMinimumSize(new Dimension(510, 460));
		
	}
	
	private void hideAllPages()
	{
		for(AbstractPreferencesPanel page : pageList)
		{
			page.setVisible(false);
		}
	}
	
	private void registerPreferencesPage(AbstractPreferencesPanel page)
	{
		pageList.add((AbstractPreferencesPanel) page);
	}
	
	

	
	private void setupPages()
	{
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		buttonBar = new JButtonBar();
		buttonBar.setOrientation(JButtonBar.VERTICAL);
		
		buttonBar.setUI(new BlueishButtonBarUI());
		buttonBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		
		for(final AbstractPreferencesPanel page: pageList)
		{
			mainPanel.add(page);
			JToggleButton button = page.getTabButton();
			button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					lblTitle.setText(page.getPageTitle());
					lblSubtitle.setText(page.getSubTitle());
					hideAllPages();
					page.setVisible(true);
					page.refresh();
				}
			});
			
			pageButtons.add(button);
			buttonBar.add(button);
		}	
		
		// Add button bar to toolbar panel
		toolbarPanel.add(buttonBar);
		
		// Click the first tab
		pageList.get(0).getTabButton().doClick();
	}
	

	/**
	 * Get the prefs panel by class
	 * 
	 * @param clazz
	 * @return
	 */
	private AbstractPreferencesPanel getPreferencesPage(Class <? extends AbstractPreferencesPanel> clazz)
	{
		if(pageList.size()==0) return null;
		
		for(AbstractPreferencesPanel page : pageList)
		{
			if(page.getClass().equals(clazz))
			{
				page.refresh();
				return page;
			}
		}

		return null;
		
	}
	
	/**
	 * Refresh all preferences panels
	 */
	public void refreshPages()
	{
		for(AbstractPreferencesPanel page : pageList)
		{
			page.refresh();
		}
	}
	
	void showHardPrefsPanel()
	{
		hideAllPages();
		for(final AbstractPreferencesPanel page: pageList)
		{
			if (page instanceof HardwarePrefsPanel)
			{
				page.setVisible(true);
			}
		}
	}


	
}
	
