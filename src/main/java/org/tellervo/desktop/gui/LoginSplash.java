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
package org.tellervo.desktop.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import org.tellervo.desktop.ui.Builder;


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
