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

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import java.util.Stack;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

// web browser in under 120 lines.  whee.

public class HelpBrowser extends JFrame implements HyperlinkListener {

    // history
    private Stack back=new Stack(), fwd=new Stack();

    // forward/back
    private JButton backButton, fwdButton;

    public HelpBrowser() {
        super("Corina Manual");

        // set up an HTML pane
        final JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(this);
        URL url=null;
        try {
            editorPane.setPage(getClass().getClassLoader().getResource("Contents.html"));
        } catch (Exception e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }

	// make it scrollable
	JScrollPane editorScrollPane = new JScrollPane(editorPane);
	editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	editorScrollPane.setPreferredSize(new Dimension(600, 500));
	getContentPane().add(editorScrollPane, BorderLayout.CENTER);

	// back button
	backButton = new JButton("Back");
	backButton.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    fwd.push(editorPane.getPage());
		    try {
			editorPane.setPage((URL) back.pop());
		    } catch (IOException ioe) {
			System.err.println("Attempted to read a bad URL: " + ioe);
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
		public void actionPerformed(ActionEvent e) {
		    back.push(editorPane.getPage());
		    try {
			editorPane.setPage((URL) fwd.pop());
		    } catch (IOException ioe) {
			System.err.println("Attempted to read a bad URL: " + ioe);
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
	show();
    }

    // listener for hyperlinks
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
