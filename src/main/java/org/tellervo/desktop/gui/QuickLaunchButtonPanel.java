package org.tellervo.desktop.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.gui.menus.AdminMenu;
import org.tellervo.desktop.gui.menus.actions.FileOpenAction;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;



public class QuickLaunchButtonPanel extends JPanel implements ActionListener, MouseListener{

	private static final long serialVersionUID = 1L;

	private JButton btnNewSeries;
	private JButton btnOpenSeries;
	private JButton btnMetadb;
	private JButton btnMap;
	private JFrame parent;
	
	/**
	 * Create the panel.
	 */
	public QuickLaunchButtonPanel(JFrame parent) {
		this.parent = parent;
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
		panel.setLayout(new MigLayout("hidemode 3", "[fill]", "[top][top][top][]"));
			
		btnNewSeries = getQuickLinkButton(I18n.getText("workspace.createNewSeries"), "newSeries", "filenew.png");
		btnOpenSeries = getQuickLinkButton(I18n.getText("workspace.openExistingSeries"), "openSeries", "fileopen.png");
		btnMetadb = getQuickLinkButton("Browse metadatabase", "metadatabase", "database.png");
		btnMap = getQuickLinkButton("Browse map", "map", "globe.png");
		
		panel.add(btnNewSeries,  "cell 0 0,growx");
		panel.add(btnOpenSeries, "cell 0 1,growx");
		panel.add(btnMetadb, "cell 0 2,growx");		
		panel.add(btnMap, "cell 0 3,growx");
		
		
		if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
		{
			setBounds(10, 10, 470, 230);
			btnMetadb.setVisible(false);
			btnMap.setVisible(false);

		}
		else
		{
			setBounds(10, 10, 470, 330);
		}
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
			EditorFactory.newSeries(parent);
		}
		else if(evt.getActionCommand().equals("openSeries"))
		{
			if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
			{
				FileOpenAction.openLegacyFile(parent);
			}
			else
			{
				FileOpenAction.opendb(false);
			}
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
