package corina.gui;

import corina.util.PureStringWriter;

import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class Bug {
    /*
      this can be more sophisticated later...
      -- e-mail me
      -- submit a bug report via sf tracker or similar
      -- print out a bug report
      -- print out the loaded data
      -- serialize all loaded samples to disk somewhere
    */

    public static void bug(Throwable t) {
        // get stack trace -- use purestringwriter because otherwise
	// windows would show funny boxes when displaying it (ugh).
        PureStringWriter sw = new PureStringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);

        // show dialog with exception and stack trace
        JOptionPane.showMessageDialog(null,
                                      "You found a bug in Corina!  Tell Ken:\n" +
                                      sw,
                                      "Corina Bug",
                                      JOptionPane.ERROR_MESSAGE);
    }
}
