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

package corina.gui;

import corina.ui.Builder;
import corina.util.TextClipboard;
import corina.util.Center;
import corina.util.CorinaLog;


import java.io.PrintStream;
import java.io.StringWriter;
import java.io.OutputStream;

import java.util.Date;
import java.util.Vector;
import java.util.StringTokenizer;

import java.awt.BorderLayout;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.ActionEvent;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
    An error log.

    <h2>Left to do:</h2>
    <ul>
        <li>Put date before each section, not after the first line (check time?)
        <li>I18n: "No Errors", "The following ...", "Error Log"
        <li>Add "Print" button

        <li>Update when new exceptions are thrown?
        <li>Put a little more spacing around the text area
        <li>Figure out how to focus the dialog, when it's re-shown
        <li>Add "Clear" button?

        <li>Consolidate ErrorLog/Bug duality?
    </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class ErrorLog extends JDialog {
  private static final Log commonslog = new CorinaLog("ErrorLog");
  
  /**
      Start logging errors.  This redirects System.err to an internal buffer,
      which can then be displayed to the user in a dialog for copying, printing,
      or so on instead of being printed to the console.  You should call this
      as soon as the application starts, to catch as many error lines as possible.
  */
  public static void logErrors() {
    commonslog.info("logErrors()");
    //w = new StringWriter();
    // use new jEdit-based impl with listmodel
    //System.setErr(new PrintStream(new StringOutputStream()));
    //System.setOut(createPrintStream());
    //System.setErr(createPrintStream());
  }

  /**
      Show the error log.
  */
  public ErrorLog() {
    // only allow one error log to exist at a time
    if (singleton != null && singleton.isVisible()) { // (is this the correct test?)
      singleton.show();
      singleton.toFront();
      // singleton.requestFocus(); // -- doesn't do anything!
      dispose();
      return;
    } else {
      singleton = this;
    }
    
    //CorinaLog.realErr.println("Creating errorlog");
    commonslog.info("creating errorlog");

    /*JTextArea area = new JTextArea(w.toString());
    area.setEditable(false);

    if (area.getText().length() == 0) {
      area.setText("No errors");
      area.setForeground(Color.gray);
    }*/

    //JLabel label = new JLabel("The following errors have occurred:");

    //JScrollPane center = new JScrollPane(area);
    LogViewer center = new LogViewer();
    /*center.setBorder(BorderFactory.createEmptyBorder(0, 8, 16, 0));

    JButton copy = Builder.makeButton("copy");
    copy.addActionListener(new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        TextClipboard.copy(w.toString());
      }
    });*/

    // WRITEME: to add printing, make a Printer that just calls
    // lines.add(line) for each |line| read from w.toString(),
    // and also call the java/awt printing method.

    /*JPanel buttons = Layout.buttonLayout(copy);

    JPanel stuff = Layout.borderLayout(label, null, center, null, buttons);
    */
    center.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

    //setContentPane(stuff);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    getContentPane().add(center, BorderLayout.CENTER);
    setTitle("Log");
    pack();
    setSize(600, 500);
    Center.center(this);
    show();
  }

  // only one error log
  private static JDialog singleton = null;

  // my output-to-string stream: appends the time and date to the first line of each block/exception
  private static class StringOutputStream extends OutputStream {
    /*
      i could implement these, too, if performance was an issue:
    
      public void write(byte b[]);
      public void write(byte b[], int off, int len);
            
            but they're defined in terms of write(int), so i get them for free.
    */
    public void write(int b) {
      // ending a line, and this line didn't start with whitespace
      if (b == '\n') {
        String s = w.toString();
        boolean pass = false;
        // pass A: it's the first line
        if (s.indexOf('\n') == -1)
          pass = true;
        // pass B: after the last \n, there's whitespace
        int newline = s.lastIndexOf('\n');
        if (Character.isWhitespace(s.charAt(newline + 1)))
          pass = true;

        // write date before newline
        if (!pass)
          w.write(" [" + new Date() + "]");
      }

      w.write(b);
    }
  }

  // output stream writes to this, error log reads from it
  private static StringWriter w;
}
