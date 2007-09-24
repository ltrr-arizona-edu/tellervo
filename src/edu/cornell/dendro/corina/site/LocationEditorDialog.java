package edu.cornell.dendro.corina.site;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.*;

import java.awt.*;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;

import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.site.*;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.OKCancel;

public class LocationEditorDialog extends JDialog {	
	private JTextField latdeg;
	private JTextField latmin;
	private JComboBox lathemi;
	private JTextField longdeg;
	private JTextField longmin;
	private JComboBox longhemi;	
	
	private final static String[] N_S = {"N", "S"};
	private final static String[] E_W = {"E", "W"};

	public LocationEditorDialog(Site site, Dialog window) {
		super(window, site.getName(), true);
		
		init(site, window);
	}
	
	public LocationEditorDialog(Site site) {
		super((Frame) null, site.getName(), true);
		
		init(site, null);
	}
	
	private void init(Site site, Window parent) {
		String location;
		if(site.getLocation() == null)
			location = new String("0,0,N,0,0,E");
		else
			location = site.getLocation().getEasyString();
		
		StringTokenizer tok = new StringTokenizer(location, ",");
		
		latdeg = new JTextField(tok.nextToken());
		latmin = new JTextField(tok.nextToken());
		lathemi = new JComboBox(N_S);
		int latidx = tok.nextToken().charAt(0) == 'S' ? 1 : 0;		
		lathemi.setSelectedIndex(latidx);
		
		longdeg = new JTextField(tok.nextToken());
		longmin = new JTextField(tok.nextToken());
		longhemi = new JComboBox(E_W);
		int longidx = tok.nextToken().charAt(0) == 'S' ? 1 : 0;
		longhemi.setSelectedIndex(longidx);
		
		JPanel rcontent = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
	    gbc.anchor = GridBagConstraints.EAST;
	    gbc.fill = GridBagConstraints.BOTH;
	    gbc.insets = new Insets(2, 2, 5, 5);
	    gbc.gridy = 0;

	    JLabel label;
	    gbc.gridx = 0;

	    // top row skips column 0
	    gbc.gridx++;
	    label = new JLabel("Degrees");
	    rcontent.add(label, gbc);
	    gbc.gridx++;
	    label = new JLabel("Minutes");
	    rcontent.add(label, gbc);
	    gbc.gridx++;
	    label = new JLabel("Hemisphere");
	    rcontent.add(label, gbc);
	    
	    gbc.gridx = 0;
	    gbc.gridy++;
	    label = new JLabel("Latitude");
	    rcontent.add(label, gbc);
	    gbc.gridx++;
	    rcontent.add(latdeg, gbc);
	    gbc.gridx++;
	    rcontent.add(latmin, gbc);
	    gbc.gridx++;
	    rcontent.add(lathemi, gbc);

	    gbc.gridx = 0;
	    gbc.gridy++;
	    label = new JLabel("Longiitude");
	    rcontent.add(label, gbc);
	    gbc.gridx++;
	    rcontent.add(longdeg, gbc);
	    gbc.gridx++;
	    rcontent.add(longmin, gbc);
	    gbc.gridx++;
	    rcontent.add(longhemi, gbc);
		
	    /*
		JPanel line_1 = Layout.flowLayoutL(
				labelOnTopNotKey(" ", new JLabel("Latitude")), 
				strutH(12), 
				labelOnTopNotKey("Degrees", latdeg), 
				strutH(12), 
				labelOnTopNotKey("Minutes", latmin),
				strutH(12), 
				labelOnTopNotKey("Hemisphere", lathemi)
				);
		JPanel line_2 = Layout.flowLayoutL(
				labelOnTopNotKey(" ", new JLabel("Longitude")), 
				strutH(12), 
				labelOnTopNotKey("Degrees", longdeg), 
				strutH(12), 
				labelOnTopNotKey("Minutes", longmin),
				strutH(12), 
				labelOnTopNotKey("Hemisphere", longhemi)
				);
		*/
	    
		JButton cancel = Builder.makeButton("cancel");
		final JButton ok = Builder.makeButton("ok");
		final JDialog dlg = this;
		final Site _site = site;
		AbstractAction buttonAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// if 'ok' clicked, writeback
				boolean kill = true;
				if (e.getSource() == ok) {
					String value = 
						latdeg.getText() + Location.DEGREE_SIGN + latmin.getText() + "'" + lathemi.getSelectedItem().toString() +
						" " +
						longdeg.getText() + Location.DEGREE_SIGN + longmin.getText() + "'" + longhemi.getSelectedItem().toString();
					
					try {
						Location loc = new Location(value);
						_site.setLocation(loc);
					}
					catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(dlg, "Invalid location!");
						kill = false;
					}
				}

				// close this dialog if we're told to kill
				if (kill)
					dispose();
			}
		};
		cancel.addActionListener(buttonAction);
		ok.addActionListener(buttonAction);

		JPanel buttons = Layout.buttonLayout(null, null, cancel, ok);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		//JPanel content = Layout.boxLayoutY(line_1, line_2);
		// everything together
		JPanel everything = Layout.borderLayout(null, null, rcontent, null,
				buttons);
		everything.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setContentPane(everything);

		setResizable(false);
		OKCancel.addKeyboardDefaults(ok);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		pack();
		if (parent != null)
			Center.center(this, parent);
		else
			Center.center(this); // (on screen)
		setVisible(true);
		
	}
	
	private static JComponent labelOnTopNotKey(String key, JComponent component) {
		String text = key;

		return Layout.borderLayout(new JLabel(text), null, component, null,
				null);
	}
	private static Component strutH(int width) {
		return Box.createHorizontalStrut(width);
	}
	
}
