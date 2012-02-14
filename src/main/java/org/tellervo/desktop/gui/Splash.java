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
// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package org.tellervo.desktop.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.tellervo.desktop.gui.ProgressMeter.ProgressEvent;
import org.tellervo.desktop.util.Center;


/**
 * @author Aaron
 */
@SuppressWarnings("serial")
public class Splash extends JDialog implements ProgressMeter.ProgressListener {
	private JProgressBar progress = new JProgressBar();
	private JLabel label = new JLabel();
	protected Container progressPanel;

	public Splash() {
		this(null, null);
	}

	public Splash(String title) {
		this(title, null);
	}

	public Splash(BufferedImage img) {
		this(null, img);
	}

	public Splash(String title, BufferedImage img) {
		
		setUndecorated(true);

		// make the content pane
		ImagePanel content = new ImagePanel(img, ImagePanel.ACTUAL);

		//content.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		//content.setLayout(new BorderLayout());
		//content.setBackground(Color.white);
	
		setContentPane(content);
				
		progressPanel = new Container();
		progressPanel.setLayout(new GridLayout(2, 1));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.WHITE);
		progressPanel.add(label);	
		progressPanel.add(progress);
		label.setVisible(false);
		
		
		
		getContentPane().add(progressPanel, BorderLayout.SOUTH);	
		pack();
				
		/* really? this behavior sucks! 
		window.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent me) {
				window.dispose();
			}

			public void mousePressed(MouseEvent me) { 
			}

			public void mouseReleased(MouseEvent me) {
			}

			public void mouseEntered(MouseEvent me) { 
			}

			public void mouseExited(MouseEvent me) { 
			}
		});
		*/
		
		setLocationRelativeTo(null);
	}

	public void closeProgress(ProgressEvent event) {
		dispose();
	}

	public void displayProgress(ProgressEvent event) {
		Center.center(this);
		stateChanged(event);
		setVisible(true);
		toFront();
	}

	public void stateChanged(ProgressEvent event) {
		String newnote = event.getNote();
		if (newnote != null) {
			if (!newnote.equals(label.getText())) {
				label.setText(newnote);
			}
			if (!label.isVisible())
				label.setVisible(true);
		} else {
			if (label.isVisible())
				label.setVisible(false);
		}
		progress.setMinimum(event.getMinimum());
		progress.setMaximum(event.getMaximum());
		progress.setValue(event.getValue());
	}
}
