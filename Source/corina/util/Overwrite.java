package corina.util;

import javax.swing.JOptionPane;

// move this to FileDialog?
public class Overwrite {

    // need i18n
    public static boolean overwrite(String filename) {
        int x = JOptionPane.showOptionDialog(null,
                                             "A file called \"" + filename + "\"\n" +
                                             "already exists; overwrite it with this data?",
                                             "Already Exists",
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE,
                                             null, // icon
                                             new Object[] {"Overwrite", "Cancel" }, // good, explicit commands
                                             null); // default
        return (x == 0); // overwrite => true
    }
    
}
