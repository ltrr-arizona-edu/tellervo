package org.tellervo.desktop.gui;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;



public class QuickLaunchButtonPanel extends JPanel implements ActionListener, MouseListener{

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public QuickLaunchButtonPanel() {
		setLayout(new MigLayout("", "[fill][grow]", "[top][grow]"));
		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBackground(new Color(0,0,0,0));
		panel.setForeground(Color.WHITE);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128), 1, true), "Quick link tasks", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(255, 255, 255)));
		add(panel, "cell 0 0,growx,aligny top");
		panel.setLayout(new MigLayout("", "[fill]", "[top][top][top][]"));
			
		panel.add(getQuickLinkButton(I18n.getText("workspace.createNewSeries"), "newSeries", "filenew.png"), "cell 0 0,growx");
		panel.add(getQuickLinkButton(I18n.getText("workspace.openExistingSeries"), "openSeries", "fileopen.png"), "cell 0 1,growx");
		panel.add(getQuickLinkButton("Browse metadatabase", "metadatabase", "database.png"), "cell 0 2,growx");		
		panel.add(getQuickLinkButton("Browse map", "map", "globe.png"), "cell 0 3,growx");

	}
	
	private JButton getQuickLinkButton(String text, String action, String icon)
	{
		JButton button = new JButton(text);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setBorderPainted(false);
		button.addActionListener(this);
		button.setActionCommand(action);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Dialog", Font.BOLD, 13));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setIcon(Builder.getIcon(icon, 48));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
