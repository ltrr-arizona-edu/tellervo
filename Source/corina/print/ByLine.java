package corina.print;

import java.util.Date;
import java.util.ResourceBundle;

import java.text.DateFormat;
import java.text.MessageFormat;

// idea: what about making this simply TextLine.makeByLine()?

public class ByLine extends TextLine {

    public ByLine() {
        super(makeByLine(), 10, true); // testing small-caps
    }

    // (for the printed_by format string)
    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    // print a "printed by ... at ..." line.
    private static String makeByLine() {
        Date date = new Date();
        String dateString = DateFormat.getDateInstance().format(date);
        String timeString = DateFormat.getTimeInstance().format(date);
        String byline = MessageFormat.format(msg.getString("printed_by"),
                                             new Object[] {
                                                 System.getProperty("user.name", "(unknown user)"), // BUG: second arg isn't localized!
                                                 dateString,
                                                 timeString,
                                             });
        return byline;
    }
}
