package edu.cornell.dendro.corina.gui;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class LoginSplash extends Splash {
	public LoginSplash(String title, ImageIcon image) {
		super(title, image);
	}
	
	public void addLoginPanel() {
		Rectangle parentRect = getContentPane().getBounds();
		
		// magic numbers make it look pretty with our current background image
		// if you change the login image, change these :)
		int xpos = 60;
		int ypos = 150;
		
		LoginPanel loginPanel = new LoginPanel();
		loginPanel.createPanel();
		
		// make it look pretty
		loginPanel.setBorder(BorderFactory.createEtchedBorder());
		loginPanel.setOpaque(true);
		loginPanel.setBackground(new Color(255, 255, 200, 220));
		
		// size it
		loginPanel.setBounds(new Rectangle(xpos, ypos, 
				parentRect.width - (xpos + getContentPane().getInsets().right), 
				parentRect.height - (ypos + 20 + progressPanel.getHeight())));
		
		// and add it!
		getRootPane().getLayeredPane().add(loginPanel, new Integer(JLayeredPane.MODAL_LAYER));
	}
}
