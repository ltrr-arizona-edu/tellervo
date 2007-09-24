package corina.gui.menus;

import corina.gui.FileDialog;
import corina.gui.UserCancelledException;
import corina.cross.Sequence;
import corina.cross.CrossdateWindow;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.ui.Alert;

import java.util.Collections;
import java.util.List;

import javax.swing.JMenu;

// the old "crossdate" menu, for the "xcorina" window.
// lets you pick 1xn, nxn, 1x1, nx1 crosses,
// or the new "crossdate kit" (which will be the only interface, in the future).

public class OldCrossdateMenu extends JMenu {

    public OldCrossdateMenu() {
	super(I18n.getText("crossdate"));

        add(Builder.makeMenuItem("1_by_n",
				 "corina.gui.menus.OldCrossdateMenu.cross1byN()"));
        add(Builder.makeMenuItem("n_by_n",
				 "corina.gui.menus.OldCrossdateMenu.crossNbyN()"));
        add(Builder.makeMenuItem("1_by_1",
				 "corina.gui.menus.OldCrossdateMenu.cross1by1()"));
        add(Builder.makeMenuItem("n_by_1",
				 "corina.gui.menus.OldCrossdateMenu.crossNby1()"));
        addSeparator();
        add(Builder.makeMenuItem("crossdate_kit",
				 "new corina.cross.CrossdateKit()"));
    }

    // !!!
    // ugly, ugly, ugly
    // - should be in corina.cross?

    public static void cross1byN() {
	try {
	    // select fixed file
	    String fixed = FileDialog.showSingle(I18n.getText("fixed"));

	    // select moving files
	    List ss = FileDialog.showMulti(I18n.getText("moving"));

            // the Peter-catcher: if the 'fixed' file is relatively
            // dated, he really wanted an Nx1.  d'oh!  DWIM.
            /*
	      if (!fixedSample.isAbsolute())
	      new CrossdateWindow(new Sequence(f, ss));
	      else
	      new CrossdateWindow(new Sequence(ss, f));
	    */

	    // new Cross
	    new CrossdateWindow(new Sequence(Collections.singletonList(fixed), ss));
	} catch (UserCancelledException uce) {
	    // do nothing
	}
    }

    public static void crossNbyN() {
	try {
	    // select files
	    List ss = FileDialog.showMulti(I18n.getText("crossdate"));

	    // watch out: need at least 2 samples
	    if (ss.size() < 2) {
		Alert.error("Not enough samples",
			    "For N-by-N crossdating, you must\n" +
			    "select at least 2 samples");
		return;
	    }

	    // new Cross
	    new CrossdateWindow(new Sequence(ss, ss));
	} catch (UserCancelledException uce) {
	    // do nothing
	}
    }

    public static void cross1by1() {
	try {
	    // select fixed file
	    String fixed = FileDialog.showSingle(I18n.getText("fixed"));

	    // select moving file
	    String moving = FileDialog.showSingle(I18n.getText("moving"));

	    // new Cross
	    new CrossdateWindow(new Sequence(Collections.singletonList(fixed),
					Collections.singletonList(moving)));
	} catch (UserCancelledException uce) {
	    // do nothing
	}
    }

    public static void crossNby1() {
	try {
	    // select fixed files
	    List ss = FileDialog.showMulti(I18n.getText("fixed"));

	    // select moving file
	    String moving = FileDialog.showSingle(I18n.getText("moving"));

	    // new Cross
	    new CrossdateWindow(new Sequence(ss,
					     Collections.singletonList(moving)));
	} catch (UserCancelledException uce) {
	    // do nothing
	}
    }
}
