package corina.gui;

import javax.swing.JOptionPane;

public class Bug {

    /*
      this can be more sophisticated later...

      -- e-mail me

      -- submit a bug report via bugzilla or similar

      -- print out a bug report

      -- print out the loaded data

      -- serialize all loaded samples to disk somewhere
    */

    public static void bug(Exception e) {
	JOptionPane.showMessageDialog(null,
				      "You found a bug in Corina!  Tell Ken:\n" +
				      e.getMessage(),
				      "Corina Bug",
				      JOptionPane.ERROR_MESSAGE);
    }

}
