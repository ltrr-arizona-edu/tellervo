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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package org.tellervo.desktop.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
   A small help browser.  It's a very simple web browser built using
   JEditorPane for browsing local documentation.

   <h2>Left to do</h2>
   <ul>
      <li>javadoc me
      <li>use special icon for tellervo-help (which?)
      <li>I18n: "Corina Help", "Back", "Forward", error msgs
      <li>what's the initial size/position?
      <li>change Help.java to use this HelpBrowser instead of JavaHelp
      <li>remove all refs to JavaHelp from the build (libs, manifest, makefile)
      <li>fix build to not re-build HTML version of help if docbook unchanged
      <li>make it a singleton window: don't create a new one, but rather
          showHelp() (or showHelp("id"))
      <li>ability to jump to a section by id -- do i need to make my
          own index for this?  (can i jump to an id in the middle of
	  a section in HTML, anyway?  do i care?) -- i'd need to parse
	  the docbook/xml myself to do this correctly, but i can approximate
	  it by grepping for "id=\"%s\"".  i can even 
      <li>extract TOC into its own frame?
      <li>tabs in left frame: TOC, glossary, index, etc.?
      <li>right-click gives popup menu with back/fwd?
      <li>(long-term open-java-help-clone goal: just give it a docbook tree)
      <li>look at moz help for ideas: glossary, index, search, contents in left pane,
          back/forward/home/print? buttons in toolbar, plus content area
      <li>look at GIMP Help Browser: 4 buttons on top: Home, Index, Back, Forward,
          then a pop-up menu (what for?), and the content
      <li>use stylesheet to make it look nicer
      <li>add searching capabilities?
   </ul>
*/
@SuppressWarnings("serial")
public class HelpBrowser extends JFrame {
    
	private final static Logger log = LoggerFactory.getLogger(HelpBrowser.class);
	
	// history
    @SuppressWarnings("unchecked")
	private Stack back=new Stack(), fwd=new Stack();

    // forward/back buttons
    private JButton backButton, fwdButton;

    private static final String HELP_PAGE = "tellervo/manual/index.html";

    private static class JPrettyEditorPane extends JEditorPane {
	@Override
	public void paintComponent(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	    super.paintComponent(g);
	}
    }

    // singleton window
    private static HelpBrowser singleton = null;

    // WRITEME: javadoc this
    // FIXME: are there synchro issues here?
    public static void showHelpBrowser() {
	if (singleton == null) {
	    singleton = new HelpBrowser();
	} else {
	    singleton.toFront();
	    // WRITEME: un-minimize!
	    singleton.requestFocus();
	}
    }
    // WRITEME: showHelpBrowser(String id) (how?)

    public HelpBrowser() {
        super("Corina Help");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	// WRITEME: on close, set singleton = null

        // set up an HTML pane
        final JEditorPane editorPane = new JPrettyEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new LinkListener());
        URL url=null;
        try {
	    editorPane.setPage(getClass().getClassLoader().getResource(HELP_PAGE));
        } catch (Exception e) { // TODO: specify which exceptions!
            log.error("Attempted to read a bad URL: " + url);
        }

	// make it scrollable
	JScrollPane editorScrollPane = new JScrollPane(editorPane);
	editorScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	editorScrollPane.setPreferredSize(new Dimension(600, 500));
	getContentPane().add(editorScrollPane, BorderLayout.CENTER);

	// back button
	backButton = new JButton("Back");
	backButton.addActionListener(new AbstractAction() {
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
		    fwd.push(editorPane.getPage());
		    try {
			editorPane.setPage((URL) back.pop());
		    } catch (IOException ioe) {
			log.error("Attempted to read a bad URL: " + ioe);
		    }
		    if (back.isEmpty())
			backButton.setEnabled(false);
		    fwdButton.setEnabled(true);
		}
	    });
	backButton.setEnabled(false);

	// fwd button
	fwdButton = new JButton("Forward");
	fwdButton.addActionListener(new AbstractAction() {
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
		    back.push(editorPane.getPage());
		    try {
			editorPane.setPage((URL) fwd.pop());
		    } catch (IOException ioe) {
			log.error("Attempted to read a bad URL: " + ioe);
		    }
		    if (fwd.isEmpty())
			fwdButton.setEnabled(false);
		    backButton.setEnabled(true);
		}
	    });
	fwdButton.setEnabled(false);

	// row of buttons
	JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
	flow.add(backButton);
	flow.add(fwdButton);
	getContentPane().add(flow, BorderLayout.NORTH);

	// show it
	pack();
	setVisible(true);
    }

    // listener for hyperlinks
    private class LinkListener implements HyperlinkListener {
	@SuppressWarnings("unchecked")
	public void hyperlinkUpdate(HyperlinkEvent e) {
	    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		JEditorPane pane = (JEditorPane) e.getSource();
		if (e instanceof HTMLFrameHyperlinkEvent) {
		    HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent) e;
		    HTMLDocument doc = (HTMLDocument) pane.getDocument();
		    doc.processHTMLFrameHyperlinkEvent(evt);
		} else {
		    try {
			// history
			back.push(pane.getPage());
			fwd = new Stack();

			// move
			pane.setPage(e.getURL());
			backButton.setEnabled(true);
			fwdButton.setEnabled(false);
		    } catch (Throwable t) {
			t.printStackTrace();
		    }
		}
	    }
	}
    }

    public static void main(String args[]) {
	new HelpBrowser();
    }
}
