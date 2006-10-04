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

package corina.prefs.panels;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;

import java.io.File;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileFilter;

import corina.core.App;
import corina.io.SerialSampleIO;
import corina.prefs.components.BoolPrefComponent;
import corina.prefs.components.FontPrefComponent;
import corina.util.DocumentListener2;

// TODO: javadoc

// TODO: center this stuff

// TODO: i18n

/*
 [ ] override menubar font
 font: Lucida Grande 11
 (Change...)
 
 [ ] Override user name
 name: [             ]
 
 
 use which file chooser:
 (*) swing (slower)
 ( ) awt (faster, but no preview)
 */

public class AdvancedPrefsPanel extends JPanel {
	private FontPrefComponent fontprefcomponent = new FontPrefComponent(
			"corina.menubar.font");

	public AdvancedPrefsPanel() {
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS) {
			public void moo() {
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(6, 6, 6, 6);
		gbc.gridy = gbc.gridx = 0;
		gbc.weightx = 1.0;

		// BEGIN OVERRIDES
		{
			JPanel box = new JPanel(new GridBagLayout());
			box.setBorder(BorderFactory
							.createTitledBorder("Setting Overrides"));

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.5;

			// menubar font override
			final JCheckBox menubarCheckBox = new JCheckBox(
					"Override menubar font");
			menubarCheckBox.setAlignmentX(LEFT_ALIGNMENT);
			box.add(menubarCheckBox, gbc);

			// TODO: make checkbox a bool-pref
			// TODO: make checkbox control font-pref-component (dim)
			// TODO: c.gui.menubar.font isn't the right pref!
			// TODO: figure out how to make the checkbox un-set the font pref
			// (idea: PrefComponent interface, getPref(), checkbox calls
			// getPref() on controlled components, sets them to null on dim)

			gbc.weightx = 0.60;
			gbc.gridx++;
			box.add(fontprefcomponent.getLabel(), gbc);

			gbc.gridx++;
			box.add(fontprefcomponent.getButton(), gbc);

			fontprefcomponent.getLabel().setEnabled(
					menubarCheckBox.isSelected());
			fontprefcomponent.getButton().setEnabled(
					menubarCheckBox.isSelected());

			menubarCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					fontprefcomponent.getLabel().setEnabled(
							menubarCheckBox.isSelected());
					fontprefcomponent.getButton().setEnabled(
							menubarCheckBox.isSelected());
				}
			});

			// user name override
			// TODO: fix layout
			// TODO: change these to better names (usernameCheckbox,
			// usernameField)
			final JCheckBox username = new JCheckBox("Override user name");
			final JTextField usernameField = new JTextField(20);
			final JLabel usernameLabel = new JLabel("Name:");
			usernameLabel.setLabelFor(usernameField);

			// set initial state from prefs
			if (App.prefs.getPref("corina.user.name") != null) {
				username.setSelected(true);
				usernameField.setText(App.prefs.getPref("corina.user.name"));
			} else {
				username.setSelected(false);
				usernameField.setEnabled(false);
				usernameLabel.setEnabled(false);
				usernameField.setText(System.getProperty("user.name"));
			}

			/*
			 * why use corina.user.name instead of just setting user.name
			 * directly? because if the user unchecks the checkbox, we need to
			 * be able to get the original value of user.name back, and once you
			 * set the system property user.name to something else, the original
			 * value is gone.
			 */

			username.setAlignmentX(LEFT_ALIGNMENT);
			username.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// enable/disable text field
					usernameField.setEnabled(username.isSelected());
					usernameLabel.setEnabled(username.isSelected());

					// name to use: either the default, or the user's version
					String name;
					if (username.isSelected())
						name = usernameField.getText();
					else
						name = System.getProperty("user.name");

					// set pref
					App.prefs.setPref("corina.user.name", name);
				}
			});

			usernameField.getDocument().addDocumentListener(
					new DocumentListener2() {
						public void update(DocumentEvent e) {
							App.prefs.setPref("corina.user.name", usernameField
									.getText());
						}
					});

			gbc.gridy++;
			gbc.gridx = 0;

			box.add(username, gbc);

			gbc.gridx++;

			box.add(usernameLabel, gbc);

			gbc.gridx++;

			box.add(usernameField, gbc);

			content.add(box);

		} // END OVERRIDES

		// serial IO capability...?
		if (SerialSampleIO.hasSerialCapability()) {
			JPanel box = new JPanel(new GridBagLayout());
			box.setBorder(BorderFactory
					.createTitledBorder("Measuring device configuration"));

			gbc.gridy = gbc.gridx = 0;
			gbc.anchor = gbc.EAST;

			boolean addedPort = false;
			JLabel label = new JLabel("Sample Recorder Data Port");
			// first, enumerate all the ports.
			Vector comportlist = SerialSampleIO.enumeratePorts();

			// do we have a COM port selected that's not in the list? (ugh!)
			String curport = App.prefs.getPref("corina.serialsampleio.port");
			if (curport != null && !comportlist.contains(curport)) {
				comportlist.add(curport);
				addedPort = true;
			} else if (curport == null) {
				curport = "<choose a serial port>";
				comportlist.add(curport);
			}

			// make the combobox, and select the current port...
			final JComboBox comports = new JComboBox(comportlist);
			if (curport != null)
				comports.setSelectedItem(curport);

			comports.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					App.prefs.setPref("corina.serialsampleio.port",
							(String) comports.getSelectedItem());
				}
			});

			label.setLabelFor(comports);
			box.add(label, gbc);

			gbc.anchor = gbc.WEST;
			gbc.gridx++;

			box.add(comports, gbc);

			// add a warning message if we have a strange com port...
			if (addedPort) {
				gbc.gridy++;
				gbc.gridx = 1;
				gbc.gridwidth = gbc.REMAINDER;
				gbc.fill = gbc.VERTICAL;

				label = new JLabel(
						"<html>The communications port '"
								+ curport
								+ "' was not found.<br>It may be in use;<br>"
								+ "you may experience problems if you attempt to use it.");
				box.add(label, gbc);

				gbc.fill = gbc.NONE;
				gbc.gridwidth = 1;
			}
			content.add(box);
		}
		
		gbc.weightx = 1;
		
		// COFECHA integration
		{
			JPanel box = new JPanel(new GridBagLayout());
			box.setBorder(BorderFactory.createTitledBorder("COFECHA Integration"));
			
			gbc.gridx = gbc.gridy = 0;
			gbc.anchor = gbc.WEST;
			
			Component c = new BoolPrefComponent(
					"Enable COFECHA Integration [requires Corina restart]",
					"corina.cofecha.enable");
			box.add(c, gbc);
			
			gbc.gridy++;
			
			Container co = new Container();
			co.setLayout(new FlowLayout(FlowLayout.LEFT));
			
		    JLabel l = new JLabel("Path to COFECHA executable:");
		    String oldFolder = App.prefs.getPref("corina.cofecha.dir", "");
		    
		    if(oldFolder.length() == 0)
		    	cofechaPath = new JTextField("");
		    else
		    	cofechaPath = new JTextField(new File(oldFolder).getAbsolutePath());
		    
		    cofechaPath.setEditable(false);
		    cofechaPath.setColumns(30);		    
		    JButton change = new JButton("Change...");
		    l.setLabelFor(change);
		    
		    change.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent ae) {
		    		chooseCofechaExecutable();
		    	}
		    });
		    
		    co.add(l); co.add(cofechaPath); co.add(change);
		    box.add(co, gbc);
		    
		    content.add(box);
		}

		{
			JPanel box = new JPanel(new GridBagLayout());
			box.setBorder(BorderFactory.createTitledBorder("Miscellaneous settings"));

			gbc.gridx = gbc.gridy = 0;
			gbc.weightx = 1;
			
			Component c = new BoolPrefComponent(
					"BETA: Smoothed feel on Windows [requires Corina restart]",
					"corina.windows.smooth");
			box.add(c, gbc);

			gbc.gridy++;
			c = new BoolPrefComponent(
					"<html>Adaptive reading for elements (G:\\DATA removal)<br>"
							+ "<font size=-2>Attempts to interpret absolute paths when reading from sums.",
					"corina.dir.adaptiveread");
			box.add(c, gbc);

			gbc.gridy++;
			c = new BoolPrefComponent(
					"<html>Save relative paths to elements<br>"
							+ "<font size=-2>Sums produced with this on will not be compatible with older versions of Corina.",
					"corina.dir.relativepaths");
			box.add(c, gbc);
			
			content.add(box);
		}

		// file chooser
		/*
		 * This appears to be absolutely useless. Commented out for now? - lucas
		 * ButtonGroup group = new ButtonGroup(); JRadioButton swing = new
		 * JRadioButton("Swing (slower)"); JRadioButton awt = new JRadioButton("AWT
		 * (faster, but no preview)"); group.add(swing); group.add(awt);
		 * 
		 * JLabel l = new JLabel("Use which file chooser:");
		 * l.setAlignmentX(LEFT_ALIGNMENT);
		 * 
		 * co.add(l, gbc);
		 * 
		 * gbc.gridx++;
		 * 
		 * co.add(swing, gbc);
		 * 
		 * gbc.gridx++; gbc.weightx = 1;
		 * 
		 * co.add(awt, gbc);
		 *  // TODO: add spacer below bottom, just in case?
		 * 
		 */
		add(content);
	}
	
	public void addNotify() {
		fontprefcomponent.setParent(getTopLevelAncestor());
		super.addNotify();
	}
	
	private JTextField cofechaPath;
	
	private void chooseCofechaExecutable() {
	    Container parent = getTopLevelAncestor();
	    JFileChooser chooser = new JFileChooser();
	    FileFilter filter = new FileFilter() {
	    	public boolean accept(File f) {
	            return f.getName().toLowerCase().equals("cofecha.exe") || f.isDirectory();
	        }
	        
	        public String getDescription() {
	            return "COFECHA Program files";
	        }
	    };
	    
	    chooser.setDialogTitle("Choose COFECHA executable (COFECHA.EXE)");
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    chooser.setCurrentDirectory(new File(cofechaPath.getText()));
	    chooser.setFileFilter(filter);
	    
	    int rv = chooser.showDialog(parent, "OK");
	    if(rv != JFileChooser.APPROVE_OPTION)
	    	return;
	    
	    String cofecha = chooser.getSelectedFile().getPath();
	    cofechaPath.setText(cofecha);
	    App.prefs.setPref("corina.dir.cofecha", cofecha);
	}
}
