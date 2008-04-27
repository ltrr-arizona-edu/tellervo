package edu.cornell.dendro.corina.gui.menus;

import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.cross.Sequence;
import edu.cornell.dendro.corina.cross.CrossdateWindow;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.ui.Alert;

import java.util.Collections;
import java.util.List;

import javax.swing.JMenu;

// the old "crossdate" menu, for the "xcorina" window.
// lets you pick 1xn, nxn, 1x1, nx1 crosses,
// or the new "crossdate kit" (which will be the only interface, in the future).

public class OldCrossdateMenu extends JMenu {

	public OldCrossdateMenu() {
		super(I18n.getText("crossdate"));

		add(Builder.makeMenuItem("1_by_n", "edu.cornell.dendro.corina.gui.menus.OldCrossdateMenu.cross1byN()"));
		add(Builder.makeMenuItem("n_by_n", "edu.cornell.dendro.corina.gui.menus.OldCrossdateMenu.crossNbyN()"));
		add(Builder.makeMenuItem("1_by_1", "edu.cornell.dendro.corina.gui.menus.OldCrossdateMenu.cross1by1()"));
		add(Builder.makeMenuItem("n_by_1", "edu.cornell.dendro.corina.gui.menus.OldCrossdateMenu.crossNby1()"));
		addSeparator();
		add(Builder.makeMenuItem("crossdate_kit", "new edu.cornell.dendro.corina.cross.CrossdateKit()"));
	}

	// !!!
	// ugly, ugly, ugly
	// - should be in corina.cross?

	public static void cross1byN() {
		try {
			// select fixed file
			Element fixed = new Element(new FileElement(FileDialog.showSingle(I18n.getText("fixed"))));

			// select moving files
			ElementList ss = FileDialog.showMulti(I18n.getText("moving"));

			// the Peter-catcher: if the 'fixed' file is relatively
			// dated, he really wanted an Nx1.  d'oh!  DWIM.
			/*
			if (!fixedSample.isAbsolute())
			new CrossdateWindow(new Sequence(f, ss));
			else
			new CrossdateWindow(new Sequence(ss, f));
			 */

			// new Cross
			new CrossdateWindow(new Sequence(ElementList.singletonList(fixed), ss));
		} catch (UserCancelledException uce) {
			// do nothing
		}
	}

	public static void crossNbyN() {
		try {
			// select files
			ElementList ss = FileDialog.showMulti(I18n.getText("crossdate"));

			// watch out: need at least 2 samples
			if (ss.size() < 2) {
				Alert.error("Not enough samples", "For N-by-N crossdating, you must\n" + "select at least 2 samples");
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
			Element fixed = new Element(new FileElement(FileDialog.showSingle(I18n.getText("fixed"))));

			// select moving file
			Element moving = new Element(new FileElement(FileDialog.showSingle(I18n.getText("moving"))));

			// new Cross
			new CrossdateWindow(new Sequence(ElementList.singletonList(fixed), 
					ElementList.singletonList(moving)));
		} catch (UserCancelledException uce) {
			// do nothing
		}
	}

	public static void crossNby1() {
		try {
			// select fixed files
			ElementList ss = FileDialog.showMulti(I18n.getText("fixed"));

			// select moving file
			Element moving = new Element(new FileElement(FileDialog.showSingle(I18n.getText("moving"))));

			// new Cross
			new CrossdateWindow(new Sequence(ss, ElementList.singletonList(moving)));
		} catch (UserCancelledException uce) {
			// do nothing
		}
	}
}
