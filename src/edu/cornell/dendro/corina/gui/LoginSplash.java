package edu.cornell.dendro.corina.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import edu.cornell.dendro.corina.ui.Builder;

@SuppressWarnings("serial")
public class LoginSplash extends Splash {
	public LoginSplash(String title, BufferedImage img) {
		super(title, img);
        setIconImage(Builder.getApplicationIcon());
        
	}
	
	public void addLoginPanel() {

		LoginPanel loginPanel = new LoginPanel();
		loginPanel.createPanel();
		
		// make it look pretty
		loginPanel.setBorder(BorderFactory.createEtchedBorder());
		loginPanel.setOpaque(true);
		loginPanel.setBackground(new Color(255, 255, 200, 220));
		
		// and add it!
		getRootPane().getLayeredPane().add(loginPanel, new Integer(JLayeredPane.MODAL_LAYER));
	}
}
