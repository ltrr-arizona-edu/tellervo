package corina.gui;

import corina.util.JDisclosureTriangle;
import corina.util.PureStringWriter;

import java.io.PrintWriter;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComponent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class Bug {
    /*
      this can be more sophisticated later...
      -- email me
      -- submit a bug report via sf tracker or similar
      -- print out a bug report
      -- print out the loaded data
      -- serialize all loaded samples to disk somewhere

      short term stuff to do:
      -- show in a textarea, with scrollbars.  then it's at least possible to
      print, email, etc., and it won't go off the screen
      -- make (be) a custom dialog with buttons "print"(?), "email"(?), "oh darn"(?)
    */

    // get stack trace -- use purestringwriter because otherwise
    // windows would show funny boxes when displaying it (ugh).
    // (i think 1.4 has a method for this.)
    public static String getStackTrace(Throwable t) {
        PureStringWriter sw = new PureStringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
	return sw.toString();
    }

    public static void bug(Throwable t) {
	JPanel message = new JPanel(new BorderLayout());

	JComponent stackTrace = new JScrollPane(new JTextArea(getStackTrace(t), 10, 60));

	JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JLabel ack = new JLabel("You found a bug in Corina!");
	flow.add(ack);

	message.add(flow, BorderLayout.NORTH);
	// add padding here?

        // show dialog with exception and stack trace
	JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
	optionPane.setOptions(new String[] { "Bummer" });

	JDialog dialog = optionPane.createDialog(null /* !!! */, "Corina Bug");

	JDisclosureTriangle v = new JDisclosureTriangle("Click for details", stackTrace, dialog, false);
	message.add(v, BorderLayout.CENTER);

	dialog.pack();
	dialog.setResizable(false);
	dialog.show();

	// TODO: add "copy to clipboard" button(?)
	// TODO: make "bummer" label default
	// TODO: center dialog on screen
    }

    public static void main(String args[]) {
	try {
	    if (args.length == 0)
		main(new String[] { "0" });
	    if (Integer.parseInt(args[0]) == 20)
		throw new IllegalArgumentException("bad stuff happening");
	    else
		main(new String[] { String.valueOf(Integer.parseInt(args[0]) + 1) } );
	} catch (Exception e) {
	    Bug.bug(e);
	}
    }
}
