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

import corina.gui.XMenubar.XMenuItem;

import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
   A shell for (Python) scripting.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
 */

public class Shell extends JFrame {
    private JTextArea output;
    private JScrollPane scroller;
    private JTextField input;
    private PythonInterpreter p;

    /** Creates a new Shell window. */
    public Shell() {
	// create a new interpreter, and plug its output into the output widget
	p = new PythonInterpreter();
	OutputStream o = new OutputStream() {
		public void write(int b) throws IOException {
		    output.append(new String(new char[] { (char) b } ));
		    JScrollBar s = scroller.getVerticalScrollBar(); // scroll
		    s.setValue(s.getMaximum());
		}
	    };
	p.setOut(o);
	p.setErr(o);
	p.exec("from java.lang import *");
	p.exec("from corina import *");

	// create an input line
	input = new JTextField();
	input.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    String text = input.getText();
		    input.setText("");
		    output.append(">>> " + text + "\n");

		    p.exec(text); // exec

		    JScrollBar b = scroller.getVerticalScrollBar(); // scroll
		    b.setValue(b.getMaximum());
		}
	    });
	getContentPane().add(input, BorderLayout.SOUTH);

	// create a new scrolled output area
	output = new JTextArea();
	output.setEditable(false);
	scroller = new JScrollPane(output);
	scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	getContentPane().add(scroller, BorderLayout.CENTER);

	// set size, title, focus, and show
	setTitle("Shell");
	setSize(640, 480);
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	show();
	input.requestFocus();
    }

    /** Creates an array of menu items, one per file in the "Scripts"
        directory.  The text of the menuitem is the name of the file,
        with a <code>.py</code> extension removed, if present.
        Selecting the menuitem runs the script in that file.
        Filenames starting with a "." or ending with anything other
        than ".py" are ignored.  (This assumes the working directory
        is the Corina/Program/ directory, right?)
	@return an array of <code>JMenuItem</code>s */
    public static JMenuItem[] loadScripts() {
	String cwd = System.getProperty("user.dir");
	File scriptDir = new File(cwd + File.separator + "Scripts");
	if (!scriptDir.exists() || !scriptDir.isDirectory())
	    return new XMenubar.XMenuItem[0];
	String scripts[] = scriptDir.list();
	int n = scripts.length;
	for (int i=0; i<n; i++)
	    if (scripts[i].startsWith(".") || !scripts[i].toLowerCase().endsWith(".py"))
		n--;
	JMenuItem menus[] = new JMenuItem[n];
	int x = 0;
	for (int i=0; i<scripts.length; i++) {
	    if (scripts[i].startsWith(".") || !scripts[i].toLowerCase().endsWith(".py"))
		continue;

	    String name = scripts[i];
	    if (name.toLowerCase().endsWith(".py"))
		name = name.substring(0, name.length() - 3);

	    menus[x] = new XMenubar.XMenuItem(name);
	    final String glue = (new File(cwd + File.separator + "Scripts" + File.separator + scripts[i])).getPath();
	    menus[x].addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			PythonInterpreter interp = new PythonInterpreter();

			try {
			    interp.execfile(glue);
			} catch (Exception ex) {
			    JOptionPane.showMessageDialog(null,
							  "There was a problem running this script:\n" +
							    ex.getClass() + "\n" +
							    ex.getMessage(),
							  "Error Running Script",
							  JOptionPane.ERROR_MESSAGE);
			}
		    }
		});
	    x++;
	}
	return menus;
    }

    /*
    public static JMenuItem makeRunScriptMenuItem() {
    // this is really easy, just something along the lines of:

	    // get script
        try {
            String script = FileDialog.showSingle("Run");
        } catch (UserCancelledException uce) {
            return;
        }

        // run it
        PythonInterpreter interp = new PythonInterpreter();
        interp.execfile(script);

    // but it'd never get used, so ...
    }

    public static JMenuItem makeShellMenuItem() {
	// WRITE ME
    }
    */
}
