package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import edu.cornell.dendro.corina.gui.ProgressMeter.ProgressEvent;

@SuppressWarnings("serial")
public class SplashDialog extends JDialog implements ProgressMeter.ProgressListener {
	private JProgressBar progress = new JProgressBar();
	private JLabel label = new JLabel();
	protected Container progressPanel;

	public SplashDialog() {
		this(null, null);
		setLocationRelativeTo(null);
	}

	public SplashDialog(String title) {
		this(title, null);
		setLocationRelativeTo(null);
	}

	public SplashDialog(BufferedImage img) {
		this(null, img);
		setLocationRelativeTo(null);
	}

	public SplashDialog(String title, BufferedImage img) {
		super((Frame) null, true);
		setUndecorated(true);

		// make the content pane
		ImagePanel content = new ImagePanel(img, ImagePanel.ACTUAL);
		Dimension d = new Dimension(300,400);	
		content.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		content.setLayout(new BorderLayout());
		content.setBackground(Color.white);
		content.setMinimumSize(d);
		content.setMaximumSize(d);
		content.setSize(d);
		
		JLabel test = new JLabel();
		content.add(test, BorderLayout.CENTER);

		/*if (title != null) {
			JLabel titlelabel = new JLabel(title);
			titlelabel.setHorizontalAlignment(SwingConstants.CENTER);
			getContentPane().add(titlelabel, BorderLayout.NORTH);
		}*/
		
		// make the progress pane
		progressPanel = new Container();
		progressPanel.setLayout(new GridLayout(2, 1));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		progressPanel.add(label);
		progressPanel.add(progress);
		label.setVisible(true);
		
		
		
	
		content.add(progressPanel, BorderLayout.CENTER);
			//content.add(progressPanel, BorderLayout.SOUTH);
			
			//content.add(new JLabel("blah"), BorderLayout.NORTH);

		
		setContentPane(content);
		pack();		
		this.setLocationRelativeTo(null);
	}

	public void closeProgress(ProgressEvent event) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				dispose();
			}
		});
	}

	public void displayProgress(final ProgressEvent event) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setLocationRelativeTo(null);
				stateChanged(event);
				toFront();
			}
		});
	}

	public void stateChanged(final ProgressEvent event) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				final String newnote = event.getNote();
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
		});
	}
}