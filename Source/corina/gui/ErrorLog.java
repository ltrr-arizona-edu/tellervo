package corina.gui;

import java.io.*;

import javax.swing.*;
import javax.swing.tree.*;

// this means i don't strictly NEED to catch/bug.bug everywhere, if i don't
// think anything will go wrong.  if something does, i can tell the user to
// check the error log.
//
// -- no, that's bad.  there should be one bug interface.  if bug.bug is
// called, it should still print to stderr, which goes in the error log.
// -- even better, make errorlog responsible for showing bug.bug?  then
// i can flag everything.

/*
  TODO:
  -- UPDATE VIEW when new exceptions are thrown?  scroll to bottom?  update about-box?
  -- make this a SINGLETON DIALOG
  -- center the dialog?
  -- convenient way to COPY/PRINT/EMAIL an uncaught exception (save, too?)
  ---- how about "Copy", "Print", "Email" buttons on the bottom?
  -- change icons from folder/file to triangle/dot?
  -- (save error log between sessions?)
  ---- (and add "clear" button?)
*/

public class ErrorLog extends JDialog {

    // call this method once to start logging errors
    public static void logErrors() {
	w = new StringWriter();
	System.setErr(new PrintStream(new StringOutputStream()));
    }

    // show the error log
    public ErrorLog() {
	DefaultMutableTreeNode root = new DefaultMutableTreeNode();
	BufferedReader r = new BufferedReader(new StringReader(w.toString()));
	DefaultMutableTreeNode lastNode = null;
	for (;;) {
	    String line = null;
	    try {
		line = r.readLine();
	    } catch (IOException ioe) {
		// it's a string reader, this can't happen
	    }
	    if (line == null)
		break;
	    if (line.startsWith("Exception"))
		continue;
	    boolean leaf = Character.isWhitespace(line.charAt(0));
	    DefaultMutableTreeNode node = new DefaultMutableTreeNode(line, !leaf);

	    if (leaf) {
		lastNode.add(node);
	    } else {
		root.add(node);
		lastNode = node;
	    }
	}

	JTree tree = new JTree(root);

	// hide the root
	tree.setRootVisible(false);

	// expand the last one
	tree.expandRow(tree.getRowCount()-1);

	JScrollPane scrollPane = new JScrollPane(tree);

	JLabel label = new JLabel("The following errors have occurred:");

	JPanel stuff = Layout.borderLayout(label,
					   null, scrollPane, null,
					   null);
	stuff.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 20));

	setContentPane(stuff);
	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	setTitle("Error Log");
	pack();
	setSize(600, 500);
	show();
    }

    // my output-to-string stream
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
		    w.write(" [" + new java.util.Date() + "]");
	    }

	    w.write(b);
	}
    }

    // output stream writes to this, error log reads from it
    private static StringWriter w;
}
