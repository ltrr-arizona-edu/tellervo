//
// This file is part of Corina.
//
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;

/**
 * About-box for Corina.
 * 
 * <p>
 * This has the unique feature that, because there should only ever be one
 * about-box, creating a second instance simply shows the first one again.
 * (Magic.)
 * </p>
 * 
 * <h2>Left to do</h2>
 * <ul>
 * <li>do version/year/author belong in corina.Version or Build.java?
 * <li>there's refactoring to be done here...
 * </ul>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at </i> cornell <i
 *         style="color: gray">dot </i> edu&gt;
 * @version $Id$
 */
public class AboutBox extends JDialog {
  private static AboutBox instance = null;

  // fonts for various parts -- sizes/styles from aqua/hig
  // use these fonts, or use relative sizes?
  private static final Font nameFont = new Font("sansserif", Font.BOLD, 14);
  private static final Font versionFont = new Font("sansserif", Font.PLAIN, 10);
  private static final Font descriptionFont = new Font("sansserif", Font.PLAIN, 11);
  private static final Font copyrightFont = new Font("sansserif", Font.PLAIN, 10);

  public static synchronized AboutBox getInstance() {
    if (instance == null) {
      instance = new AboutBox();
    } 
    return instance;
  }

  // add the icon and name: centered
  private static void addName(JPanel box) {
    JLabel name = new JLabel("Corina");
    { // (add icon to name)
      Icon icon = Builder.getIcon("treeicon.png");
      if (icon != null) {
        name.setIcon(icon);
      }
      name.setHorizontalTextPosition(SwingConstants.CENTER);
      name.setVerticalTextPosition(SwingConstants.BOTTOM);
      name.setIconTextGap(12);
    }
    name.setHorizontalAlignment(SwingConstants.CENTER);
    name.setFont(nameFont);
    name.setAlignmentX(Component.CENTER_ALIGNMENT);
    box.add(name);
  }

  // add the version number: centered
  private static void addVersion(JPanel box) {
    String text = MessageFormat.format(I18n.getText("version"),
        new Object[] { Build.VERSION });
    JLabel version = new JLabel(text);
    version.setFont(versionFont);
    version.setAlignmentX(Component.CENTER_ALIGNMENT);
    box.add(version);
  }

  // add timestamp
  private static void addTimestamp(JPanel box) {
    String text = MessageFormat.format(I18n.getText("timestamp"),
        new Object[] { Build.TIMESTAMP });
    JLabel timestamp = new JLabel(text);
    timestamp.setFont(versionFont);
    timestamp.setAlignmentX(Component.CENTER_ALIGNMENT);
    box.add(timestamp);
  }

  // add the description: flush left
  private static void addDescription(JPanel box) {
    // get from bundle
    final String description = I18n.getText("description");

    JPanel descriptionBlock = new JPanel();
    descriptionBlock
        .setLayout(new BoxLayout(descriptionBlock, BoxLayout.Y_AXIS));
    box.add(descriptionBlock);
    descriptionBlock.setAlignmentX(Component.CENTER_ALIGNMENT);

    // extract line at a time (would a stringtok on \n\r be simpler?)
    // BETTER: i have a method to do this, somewhere...
    BufferedReader r = new BufferedReader(new StringReader(description));
    String line;
    for (;;) {
      try {
        line = r.readLine();
      } catch (IOException ioe) {
        // can't happen?
        break;
      }
      if (line == null)
        break;

      // add one line
      JLabel descriptionLabel = new JLabel(line);
      descriptionLabel.setFont(descriptionFont);
      descriptionBlock.add(descriptionLabel);
    }
  }

  // add the copyright notice: centered
  private static void addCopyright(JPanel box) {
    // get from bundle
    final String description = I18n.getText("copyright");

    JPanel copyrightBlock = new JPanel();
    copyrightBlock.setLayout(new BoxLayout(copyrightBlock, BoxLayout.Y_AXIS));
    box.add(copyrightBlock);
    copyrightBlock.setAlignmentX(Component.CENTER_ALIGNMENT);

    // extract line at a time
    BufferedReader r = new BufferedReader(new StringReader(description));
    String line;
    for (;;) {
      try {
        line = r.readLine();
      } catch (IOException ioe) {
        // can't happen?
        break;
      }
      if (line == null)
        break;
      String subst = MessageFormat.format(line, new Object[] { Build.YEAR,
          Build.AUTHOR });

      JLabel copyrightLabel = new JLabel(subst);
      copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      copyrightLabel.setFont(copyrightFont);
      copyrightBlock.add(copyrightLabel);
    }
  }

  // add extra space between components
  private static void addSpace(JPanel box, int pixels) {
    box.add(Box.createVerticalStrut(pixels));
  }
  
  // add license text
  private static void addLicense(JPanel box){
   
	final String license = I18n.getText("copyright");
  
	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setLayout(new ScrollPaneLayout());
	
	JPanel licenseBlock = new JPanel();
	licenseBlock.setLayout(new BoxLayout(licenseBlock, BoxLayout.Y_AXIS));
	
	scrollPane.add(licenseBlock);
	box.add(scrollPane);
	licenseBlock.setAlignmentX(Component.CENTER_ALIGNMENT);
				
	JLabel licenseLabel = new JLabel(license);
	licenseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	licenseLabel.setFont(copyrightFont);
	licenseBlock.add(licenseLabel);  
  }

  
  private AboutBox() {
    // no icon?
    setTitle("About Corina");

    // layout: top-to-bottom
    JPanel box = new JPanel();
    box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
    getContentPane().add(box, BorderLayout.CENTER);

    // spacers on the sides
    getContentPane().add(Box.createHorizontalStrut(24), BorderLayout.WEST);
    getContentPane().add(Box.createHorizontalStrut(24), BorderLayout.EAST);

    // put content into box
    addSpace(box, 8);
    addName(box);
    addSpace(box, 8);
    addVersion(box);
    addTimestamp(box);
    addSpace(box, 8);
    addDescription(box);
    addSpace(box, 8);
    addCopyright(box);
    addSpace(box, 8);
    addLicense(box);
    addSpace(box, 20);

    // can't resize -- does this get rid of minimize/maximize buttons?
    setResizable(false);

    addKeyListener(new KeyAdapter() {
      @Override
	public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
          hide();
        }
      }
    });
    // pack
    pack();

    // center it
    Center.center(this);

    // see also PrefsDialog -- PrefsDialog and AboutBox should subclass
    // the same class, maybe gui.SingletonDialog? REFACTOR.
  }
}