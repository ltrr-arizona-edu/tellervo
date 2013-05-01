package org.tellervo.desktop.gui;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.gui.menus.AdminMenu;
import org.tellervo.desktop.gui.menus.FileMenu;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;



public class QuickLaunchButtonPanel extends JPanel implements ActionListener{

	/**
	 * Create the panel.
	 */
	public QuickLaunchButtonPanel() {
		setLayout(new MigLayout("", "[fill][grow]", "[][grow]"));
		setOpaque(false);
		setBackground(new Color(0,0,0,0));
	
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBackground(new Color(0,0,0,0));
		panel.setForeground(Color.WHITE);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128), 1, true), "Quick link tasks", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(255, 255, 255)));
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[fill]", "[][][][]"));
		
		
		JButton btnNewButton = new JButton(I18n.getText("workspace.createNewSeries"));
		btnNewButton.addActionListener(this);
		btnNewButton.setActionCommand("newSeries");
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 13));
		panel.add(btnNewButton, "cell 0 0,growx");
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton.setIcon(Builder.getIcon("filenew.png", 48));
		btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JButton btnOpenSeries = new JButton(I18n.getText("workspace.openExistingSeries"));
		btnOpenSeries.setForeground(Color.WHITE);
		btnOpenSeries.addActionListener(this);
		btnOpenSeries.setActionCommand("openSeries");
		btnOpenSeries.setContentAreaFilled(false);
		btnOpenSeries.setFont(new Font("Dialog", Font.BOLD, 13));
		panel.add(btnOpenSeries, "cell 0 1,growx");
		btnOpenSeries.setHorizontalAlignment(SwingConstants.LEFT);
		btnOpenSeries.setIcon(Builder.getIcon("fileopen.png", 48));
		btnOpenSeries.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JButton btnMetadatabase = new JButton("Browse metadatabase");
		btnMetadatabase.setContentAreaFilled(false);
		btnMetadatabase.addActionListener(this);
		btnMetadatabase.setActionCommand("metadatabase");
		btnMetadatabase.setForeground(Color.WHITE);
		btnMetadatabase.setFont(new Font("Dialog", Font.BOLD, 13));
		panel.add(btnMetadatabase, "cell 0 2");
		btnMetadatabase.setHorizontalAlignment(SwingConstants.LEFT);
		btnMetadatabase.setIcon(Builder.getIcon("database.png", 48));
		btnMetadatabase.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JButton button = new JButton("Browse map");
		button.setIcon(Builder.getIcon("globe.png", 48));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Dialog", Font.BOLD, 13));
		button.setContentAreaFilled(false);
		button.setActionCommand("map");
		panel.add(button, "cell 0 3,growx");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("newSeries"))
		{
			EditorFactory.newSeries(this);
		}
		else if(evt.getActionCommand().equals("openSeries"))
		{
			FileMenu.opendb();
		}
		else if(evt.getActionCommand().equals("importSeries"))
		{
			FileMenu.importdbwithbarcode();
		}
		else if(evt.getActionCommand().equals("metadatabase"))
		{
			AdminMenu.metadataBrowser();
		}
		else if(evt.getActionCommand().equals("map"))
		{
			AdminMenu.showMap();
		}
	}

}
