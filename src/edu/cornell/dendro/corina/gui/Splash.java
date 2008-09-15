// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import edu.cornell.dendro.corina.gui.ProgressMeter.ProgressEvent;
import edu.cornell.dendro.corina.util.Center;

/**
 * @author Aaron
 */
public class Splash extends JFrame implements ProgressMeter.ProgressListener {
	private ImageIcon image;
	private JProgressBar progress = new JProgressBar();
	private JLabel label = new JLabel();
	protected Container progressPanel;

	public Splash() {
		this(null, null);
	}

	public Splash(String title) {
		this(title, null);
	}

	public Splash(ImageIcon image) {
		this(null, image);
	}

	public Splash(String title, ImageIcon image) {
		JPanel content = new JPanel();
		
		setUndecorated(true);

		// make the content pane
		content.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		content.setLayout(new BorderLayout());
		content.setBackground(Color.white);
		setContentPane(content);
		
		if (title != null) {
			JLabel titlelabel = new JLabel(title);
			titlelabel.setHorizontalAlignment(SwingConstants.CENTER);
			getContentPane().add(titlelabel, BorderLayout.NORTH);
		}
		
		progressPanel = new Container();
		progressPanel.setLayout(new GridLayout(2, 1));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		progressPanel.add(label);
		progressPanel.add(progress);
		label.setVisible(false);
		
		if (image != null) {
			getContentPane().add(new JLabel(image), BorderLayout.CENTER);
			getContentPane().add(progressPanel, BorderLayout.SOUTH);
		} else {
			getContentPane().add(progressPanel, BorderLayout.CENTER);
		}
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
	}

	public void closeProgress(ProgressEvent event) {
		dispose();
	}

	public void displayProgress(ProgressEvent event) {
		Center.center(this);
		stateChanged(event);
		setVisible(true);
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