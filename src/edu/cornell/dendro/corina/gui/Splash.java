// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
public class Splash implements ProgressMeter.ProgressListener {
  private ImageIcon image;
  private JProgressBar progress = new JProgressBar();
  private JLabel label = new JLabel();
  protected JWindow window = new JWindow();

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
    content.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    content.setLayout(new BorderLayout());
    content.setBackground(Color.white);
    window.setContentPane(content);
    if (title != null) {
      JLabel titlelabel = new JLabel(title);
      titlelabel.setHorizontalAlignment(SwingConstants.CENTER);
      window.getContentPane().add(titlelabel, BorderLayout.NORTH);
    }
    Container progressPanel = new Container();
    progressPanel.setLayout(new GridLayout(2, 1));
    label.setHorizontalAlignment(SwingConstants.CENTER);
    progressPanel.add(label);
    progressPanel.add(progress);
    label.setVisible(false);
    if (image != null) {
      window.getContentPane().add(new JLabel(image), BorderLayout.CENTER);
      window.getContentPane().add(progressPanel, BorderLayout.SOUTH);
    } else {
      window.getContentPane().add(progressPanel, BorderLayout.CENTER);
    }
    window.pack();
    window.addMouseListener(new MouseListener() {
      public void mouseClicked(MouseEvent me) {
        window.dispose();
      }
      public void mousePressed(MouseEvent me) { /* nothing */ }
      public void mouseReleased(MouseEvent me) { /* nothing */ }
      public void mouseEntered(MouseEvent me) { /* nothing */ }
      public void mouseExited(MouseEvent me) { /* nothing */ }
    });
  }

  public void closeProgress(ProgressEvent event) {
    window.dispose();
  }
  public void displayProgress(ProgressEvent event) {
    Center.center(window);
    stateChanged(event);
    window.setVisible(true);
  }
  public void stateChanged(ProgressEvent event) {
    String newnote = event.getNote();
    if (newnote != null) {
      if (!newnote.equals(label.getText())) {
        label.setText(newnote);
      }
      if (!label.isVisible()) label.setVisible(true);
    } else {
      if (label.isVisible()) label.setVisible(false);
    }
    progress.setMinimum(event.getMinimum());
    progress.setMaximum(event.getMaximum());
    progress.setValue(event.getValue());
  }
}