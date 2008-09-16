package edu.cornell.dendro.corina.editor;

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

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.CorinaPermission;
import edu.cornell.dendro.corina.cross.CrossdateWindow;
import edu.cornell.dendro.corina.cross.Sequence;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.index.IndexDialog;
import edu.cornell.dendro.corina.manip.Reconcile;
import edu.cornell.dendro.corina.manip.ReconcileDialog;
import edu.cornell.dendro.corina.manip.RedateDialog;
import edu.cornell.dendro.corina.manip.Reverse;
import edu.cornell.dendro.corina.manip.SumCreationDialog;
import edu.cornell.dendro.corina.manip.TruncateDialog;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementFactory;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

// REFACTOR: this class needs refactoring.  there's IOEs and FNFEs in here!

public class EditorToolsMenu extends JMenu implements SampleListener {

	private Sample sample;
	private Editor editor;

	public EditorToolsMenu(Sample s, Editor e) {
		super(I18n.getText("tools")); // TODO: mnemonic

		this.sample = s;
		this.editor = e;

		sample.addSampleListener(this);

		// redate
		JMenuItem redate = Builder.makeMenuItem("redate...", true, "redate.png");
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
		indexMenu = Builder.makeMenuItem("index...", true, "index.png");
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

		// sum
		sumMenu = Builder.makeMenuItem("sum...", true, "sum.png");
		sumMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				new SumCreationDialog(editor, ElementList.singletonList(new CachedElement(sample)));
			}
		});
		sumMenu.setEnabled(!sample.isSummed());
		add(sumMenu);

		// truncate
		JMenuItem truncate = Builder.makeMenuItem("truncate...");
		truncate.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				new TruncateDialog(sample, editor);
			}
		});
		add(truncate);

		// reverse
		JMenuItem reverseMenu = Builder.makeMenuItem("reverse", true, "reverse.png");
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
		JMenuItem crossAgainst = Builder.makeMenuItem("cross_against...", true, "crossdate.png");
		crossAgainst.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				try {
					// select moving files
					ElementList ss = FileDialog.showMulti("Crossdate " +
							"\"" + sample + "\"" +
					" against:");

					// (note also the Peter-catcher: see XMenubar.java)

					// hack for bug 228: filename may be null, and sequence
					// uses it to hash, so let's make up a fake
					// filename that can't be a real filename.
					//String filename = (String) sample.getMeta("filename");
					//if (filename == null)
					//    filename = "\u011e"; // this can't begin a word!

					Sequence seq = new Sequence(ElementList.singletonList(new CachedElement(sample)), ss);
					new CrossdateWindow(seq);
				} catch (UserCancelledException uce) {
					// do nothing
				}
			}
		});
		add(crossAgainst);

		// cross all
		crossElements = Builder.makeMenuItem("cross_elements", true, "crossdate.png");
		crossElements.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				// n-by-n cross
				Sequence seq = new Sequence(sample.getElements(),
						sample.getElements());
				new CrossdateWindow(seq);
			}
		});
		add(crossElements);

		// reconcile
		// but don't put this here if disablereconcile is on
		// this is an awful hack; we're sorry.
		if (!Boolean.valueOf(
				App.prefs.getPref("corina.editor.disablereconcile")).booleanValue()) {
			JMenuItem reconcile = Builder.makeMenuItem("reconcile", true, "reconcile.png");
			reconcile.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// REFACTOR: this method isn't very pretty. there's probably
					// an elegant refactoring to be done, but i'm not seeing
					// it right now...

					// EXTRACT: why isn't this part of reconciledialog?

					// check for filename
					String filename = (String) sample.getMeta("filename");

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
						new ReconcileDialog(sample, ElementFactory.createElement(target).load());
					} catch (IOException ioe) {
						// BUG: why does reconciledialog throw ioe's?
						new Bug(ioe);
					} catch (UserCancelledException uce) {
						// do nothing
					} catch (Exception ex) {
						new Bug(ex);
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

	private JMenuItem indexMenu, sumMenu, crossElements;

	//
	// listener
	//
	public void sampleRedated(SampleEvent e) { }
	public void sampleDataChanged(SampleEvent e) { }
	public void sampleMetadataChanged(SampleEvent e) {
		// index menu: only if not indexed
		indexMenu.setEnabled(!sample.isIndexed());
		sumMenu.setEnabled(!sample.isSummed());
	}
	public void sampleElementsChanged(SampleEvent e) {
		// cross elements: only if elements present, and at least 2 elements
		crossElements.setEnabled(sample.getElements() != null &&
				sample.getElements().size() >= 2);
	}
}
