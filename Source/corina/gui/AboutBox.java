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

package corina.gui;

import corina.Build;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.util.Center;
import corina.util.PropertiesWindow;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

import java.text.MessageFormat;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.AbstractAction;


/**
   About-box for Corina.

   <p>This has the unique feature that, because there should only ever
   be one about-box, creating a second instance simply shows the first
   one again.  (Magic.)</p>

   <h2>Left to do</h2>
   <ul>
      <li>do version/year/author belong in corina.Version or Build.java?
      <li>there's refactoring to be done here...
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class AboutBox extends JDialog {
    // toplevel box to stuff things into
    private JPanel box;

    // don't allow multiple instances
    // -> inherit from one-shot-window class? (preferences has a similar behavior)
    private static AboutBox _me=null;

    // (javadoc me!)

    // use these fonts, or use relative sizes?

    // fonts for various parts -- sizes/styles from aqua/hig
    private Font nameFont = new Font("sansserif", Font.BOLD, 14);
    private Font versionFont = new Font("sansserif", Font.PLAIN, 10);
    private Font descriptionFont = new Font("sansserif", Font.PLAIN, 11);
    private Font copyrightFont = new Font("sansserif", Font.PLAIN, 10);

    // add the icon and name: centered
    private void addName() {
        JLabel name = new JLabel("Corina");
        { // (add icon to name)
	    Icon icon = Builder.getIcon("Tree.png");
            name.setIcon(icon);
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
    private void addVersion() {
	String text = MessageFormat.format(I18n.getText("version"),
					   new Object[] { Build.VERSION });
	JLabel version = new JLabel(text);
	version.setFont(versionFont);
	version.setAlignmentX(Component.CENTER_ALIGNMENT);
	box.add(version);
    }

    // add timestamp
    private void addTimestamp() {
	String text = MessageFormat.format(I18n.getText("timestamp"),
					   new Object[] { Build.TIMESTAMP });
	JLabel timestamp = new JLabel(text);
	timestamp.setFont(versionFont);
	timestamp.setAlignmentX(Component.CENTER_ALIGNMENT);
	box.add(timestamp);
    }

    // add the description: flush left
    private void addDescription() {
        // get from bundle
        final String description = I18n.getText("description");

        JPanel descriptionBlock = new JPanel();
        descriptionBlock.setLayout(new BoxLayout(descriptionBlock, BoxLayout.Y_AXIS));
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
    private void addCopyright() {
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
            String subst = MessageFormat.format(line, new Object[] { Build.YEAR, Build.AUTHOR });

            JLabel copyrightLabel = new JLabel(subst);
            copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            copyrightLabel.setFont(copyrightFont);
            copyrightBlock.add(copyrightLabel);
        }
    }

    // add extra space between components
    private void addSpace(int pixels) {
        box.add(Box.createVerticalStrut(pixels));
    }

    public AboutBox() {
	// on close
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	// there can be only one!
	if (_me == null) {
	    _me = this;
	} else {
	    _me.setVisible(true);
	    _me.toFront();
	    super.dispose();
	    return;
	}

	// no icon?
	setTitle("About Corina");

	// layout: top-to-bottom
	box = new JPanel();
	box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
	getContentPane().add(box, BorderLayout.CENTER);

	// spacers on the sides
	getContentPane().add(Box.createHorizontalStrut(24), BorderLayout.WEST);
	getContentPane().add(Box.createHorizontalStrut(24), BorderLayout.EAST);

	// put content into box
	  addSpace(8);
	addName();
	  addSpace(8);
	addVersion();
	addTimestamp();
	  addSpace(8);
	addDescription();
	  addSpace(8);
	addCopyright();
	  addSpace(20);

	// can't resize -- does this get rid of minimize/maximize buttons?
	setResizable(false);

	// pack
	pack();

	// center it
	Center.center(this);

        // see also PrefsDialog -- PrefsDialog and AboutBox should subclass
        // the same class, maybe gui.SingletonDialog?  REFACTOR.
        
	// show it
	show();
    }

    public void dispose() {
	_me = null;
	super.dispose();
    }
}
