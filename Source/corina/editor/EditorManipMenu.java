package corina.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import corina.core.App;
import corina.CorinaPermission;
import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.cross.CrossdateWindow;
import corina.cross.Sequence;
import corina.gui.Bug;
import corina.gui.FileDialog;
import corina.gui.UserCancelledException;
import corina.index.IndexDialog;
import corina.manip.Reconcile;
import corina.manip.ReconcileDialog;
import corina.manip.RedateDialog;
import corina.manip.Reverse;
import corina.manip.TruncateDialog;
import corina.ui.Builder;
import corina.ui.I18n;

// REFACTOR: this class needs refactoring.  there's IOEs and FNFEs in here!

public class EditorManipMenu extends JMenu implements SampleListener {

    private Sample sample;
    private Editor editor;

    public EditorManipMenu(Sample s, Editor e) {
	super(I18n.getText("manip")); // TODO: mnemonic

	this.sample = s;
	this.editor = e;

	sample.addSampleListener(this);

	// redate
	JMenuItem redate = Builder.makeMenuItem("redate...");
  redate.addActionListener(new AbstractAction() {
    public void actionPerformed(ActionEvent ae) {
        new RedateDialog(sample, editor);
    }
  });
  if (System.getSecurityManager() != null) {
    try {
      AccessController.checkPermission(new CorinaPermission("redate"));
    } catch (AccessControlException ace) {
      ace.printStackTrace();
      redate.setEnabled(false);
      redate.setBackground(Color.red.darker().darker());
    }
  }
	add(redate);

	// index
	indexMenu = Builder.makeMenuItem("index...");
	indexMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // PERF: for big samples, it can take a couple
		    // seconds for the dialog to appear.  not enough
		    // for a progressbar, but enough that i should use
		    // the "wait" cursor on the editor window.
		    new IndexDialog(sample, editor);
		}
	    });
	indexMenu.setEnabled(!sample.isIndexed());
	add(indexMenu);

	// truncate
	JMenuItem truncate = Builder.makeMenuItem("truncate...");
	truncate.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    new TruncateDialog(sample, editor);
		}
	    });
	add(truncate);

	// reverse
	JMenuItem reverseMenu = Builder.makeMenuItem("reverse");
	reverseMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // reverse, and add to the undo-stack
		    editor.postEdit(Reverse.reverse(sample));
		}
	    });
	add(reverseMenu);

	// ---
	addSeparator();

	// cross against
	// HACK: just disable this if the sample isn't saved?
	JMenuItem crossAgainst = Builder.makeMenuItem("cross_against...");
	crossAgainst.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    try {
			// select moving files
			List ss = FileDialog.showMulti("Crossdate " +
						       "\"" + sample + "\"" +
						       " against:");

			// (note also the Peter-catcher: see XMenubar.java)

			// hack for bug 228: filename may be null, and sequence
			// uses it to hash, so let's make up a fake
			// filename that can't be a real filename.
			String filename = (String) sample.meta.get("filename");
			if (filename == null)
			    filename = "\u011e"; // this can't begin a word!

			Sequence seq = new Sequence(Collections.singletonList(filename), ss);
			new CrossdateWindow(seq);
		    } catch (UserCancelledException uce) {
			// do nothing
		    }
		}
	    });
	add(crossAgainst);

	// cross all
	crossElements = Builder.makeMenuItem("cross_elements");
	crossElements.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // n-by-n cross
		    Sequence seq = new Sequence(sample.elements,
						sample.elements);
		    new CrossdateWindow(seq);
		}
	    });
	add(crossElements);

	// reconcile
	// but don't put this here if disablereconcile is on
	// this is an awful hack; we're sorry.
	if (!Boolean.valueOf(
				App.prefs.getPref("corina.editor.disablereconcile")).booleanValue()) {
			JMenuItem reconcile = Builder.makeMenuItem("reconcile");
			reconcile.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// REFACTOR: this method isn't very pretty. there's probably
					// an elegant refactoring to be done, but i'm not seeing
					// it right now...

					// EXTRACT: why isn't this part of reconciledialog?

					// check for filename
					String filename = (String) sample.meta.get("filename");

					// (do we need to ask the user?)
					boolean askUser = false;

					// if null, need to ask user.
					if (filename == null) {
						askUser = true;
					}

					// try to guess
					String target = null;
					try {
						if (!askUser)
							target = Reconcile.guessOtherReading(filename);
					} catch (FileNotFoundException fnfe) {
						askUser = true;
					}

					// ask user here -- REDUNDANT!
					try {
						if (askUser)
							target = FileDialog.showSingle(I18n
									.getText("other_reading"));

						// reconcile this and target
						new ReconcileDialog(sample, new Sample(target));
					} catch (IOException ioe) {
						// BUG: why does reconciledialog throw ioe's?
						Bug.bug(ioe);
					} catch (UserCancelledException uce) {
						// do nothing
					} catch (Exception ex) {
						Bug.bug(ex);
					}
					/*
					 * here's how the reconcile UI should work: -- auto-tile
					 * +------A Reading------+ +------C Reading------+ | | | | | | | | | | | | | | | | | | | | | | | | | | | |
					 * +---------------------+ +---------------------+
					 * +--------------Reconcile Dialog---------------+ | | | |
					 * +---------------------------------------------+ --
					 * selecting an error row in the reconcile dialog selects
					 * that year in A and C
					 */
				}
			});
			add(reconcile);
		}

	// hit them so they enable/disable themselves properly
	sampleMetadataChanged(null);
	sampleElementsChanged(null);
    }

    private JMenuItem indexMenu, crossElements;

    //
    // listener
    //
    public void sampleRedated(SampleEvent e) { }
    public void sampleDataChanged(SampleEvent e) { }
    public void sampleMetadataChanged(SampleEvent e) {
	// index menu: only if not indexed
	indexMenu.setEnabled(!sample.isIndexed());
    }
    public void sampleElementsChanged(SampleEvent e) {
	// cross elements: only if elements present, and at least 2 elements
	crossElements.setEnabled(sample.elements != null &&
				 sample.elements.size() >= 2);
    }
}
