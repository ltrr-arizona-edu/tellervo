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
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringWriter;
import java.io.IOException;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import edu.cornell.dendro.corina.Build;

/**
   A window which displays a plain text file.

   <h2>Left to do:</h2>
   <ul>
     <li>Make the interface TextWindow.show(String filename)? -- singleton
     <li>Launch NOTEPAD on Windows, TexTEdit on Mac, then fall back to a Swing window?
     <li>Have text/binary toggle (binary mode = hex dump)
     <li>Have encoding popup (UTF-8, 8859-/n/, native)
     <li>If it looks like binary (how can I tell?), show the binary view
     <li>Java bug?: why doesn't
         <code>s.getVerticalScrollBar().setValue(s.getVerticalScrollBar().getMinimum())</code>
         scroll it to the top?
   </uL>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
@SuppressWarnings("serial")
public class TextWindow extends JFrame {

    /**
       Make a new text window, showing the contents of the specified file.

       @param filename the name of the file to display
       @exception IOException if the file can't be loaded
    */
    public TextWindow(String filename) throws IOException {
	JTextArea a = new JTextArea();
	JScrollPane s = new JScrollPane(a);
	s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	s.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	setContentPane(s);

	String buf = readFile(filename);
	a.setText(buf);
	a.setEditable(false);

	// (taken from ExportDialog.java)
        Font oldFont = a.getFont();
        a.setFont(new Font("monospaced", oldFont.getStyle(), oldFont.getSize()));

	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	setTitle(filename + " - " + Build.VERSION + " " + Build.TIMESTAMP);
	setSize(640, 480);
	setVisible(true);
    }

    // returns the contents of |filename| in a string
    private static String readFile(String filename) throws IOException {
        StringWriter w = new StringWriter();
        BufferedReader r = null;
        
        try {
            r = new BufferedReader(new FileReader(filename));

            String line = r.readLine();
            while (line != null) {
                w.write(line);
                w.write("\n");
                line = r.readLine();
            }
            
        } finally {
            if (r != null)
                r.close();
            w.close(); // this can't ever fail, can it?
        }

	return w.toString();
    }
}
