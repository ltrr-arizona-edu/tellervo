package edu.cornell.dendro.corina.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.cornell.dendro.corina.CorinaPermission;
import edu.cornell.dendro.corina.cross.Crossdater;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.gui.menus.OpenRecent;
import edu.cornell.dendro.corina.index.IndexDialog;
import edu.cornell.dendro.corina.manip.ReconcileWindow;
import edu.cornell.dendro.corina.manip.RedateDialog;
import edu.cornell.dendro.corina.manip.Reverse;
import edu.cornell.dendro.corina.manip.SumCreationDialog;
import edu.cornell.dendro.corina.manip.TruncateDialog;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;

// REFACTOR: this class needs refactoring.  there's IOEs and FNFEs in here!

public class EditorToolsMenu extends JMenu implements SampleListener {
	private static final long serialVersionUID = 1L;
	
	private Sample sample;
	private Editor editor;

	public EditorToolsMenu(Sample s, Editor e) {
		super(I18n.getText("tools")); // TODO: mnemonic

		this.sample = s;
		this.editor = e;

		sample.addSampleListener(this);

		// truncate
		JMenuItem truncate = Builder.makeMenuItem("truncate...", true, "truncate.png");
		truncate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new TruncateDialog(sample, editor);
			}
		});
		add(truncate);
		truncate.setEnabled(sample.getSampleType() == SampleType.DIRECT);

		// reverse
		JMenuItem reverseMenu = Builder.makeMenuItem("reverse", true, "reverse.png");
		reverseMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// reverse, and add to the undo-stack
				editor.postEdit(Reverse.reverse(sample));
			}
		});
		add(reverseMenu);
		truncate.setEnabled(sample.getSampleType() == SampleType.DIRECT);

		// ---
		addSeparator();		
		
		// reconcile
		JMenuItem reconcile = Builder.makeMenuItem("new_reconcile", true, "reconcile.png");
		reconcile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBBrowser browser = new DBBrowser(editor, true, false);

				// select the site we're in
				if(sample.meta().hasSiteCode()) 
					browser.selectSiteByCode(sample.meta().getSiteCode());
				
				browser.setTitle("Choose a reference sample");
				browser.setVisible(true);
				
				if(browser.getReturnStatus() == DBBrowser.RET_OK) {
					ElementList toOpen = browser.getSelectedElements();

					if(toOpen.size() < 1)
						return;

					// load it
					Sample reference;
					try {
						reference = toOpen.get(0).load();
					} catch (IOException ioe) {
						Alert.error("Error Loading Sample",
								"Can't open this file: " + ioe.getMessage());
						return;
					}

					OpenRecent.sampleOpened(reference.getLoader(), "reconcile");
					
					// open it for fun times
					Center.center(new ReconcileWindow(sample, reference), editor);
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
		
		// now, make this remember the last things we reconciled against!
		JMenu reconcileMenu = Builder.makeMenu("reconcile");
		reconcileMenu.putClientProperty("corina.open_recent_action", new OpenRecent.SampleOpener("reconcile") {
			@Override
			public void performOpen(Sample s) {
				// new reconcile window
				Center.center(new ReconcileWindow(sample, s), editor);
				// move to top of menu
				OpenRecent.sampleOpened(s.getLoader(), getTag());
			}
		});
		reconcileMenu.add(reconcile);
		reconcileMenu.addSeparator();
		OpenRecent.makeOpenRecentMenu("reconcile", reconcileMenu, 2);
		add(reconcileMenu);		
	
		// index
		indexMenu = Builder.makeMenuItem("index...", true, "index.png");
		indexMenu.addActionListener(new ActionListener() {
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
		sumMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new SumCreationDialog(editor, ElementList.singletonList(new CachedElement(sample)));
			}
		});
		sumMenu.setEnabled(!sample.isSummed());
		add(sumMenu);

		// redate
		JMenuItem redate = Builder.makeMenuItem("redate...", true, "redate.png");
		redate.addActionListener(new ActionListener() {
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
		redate.setEnabled(true);
		redate.setVisible(true);		


		// cross against
		// HACK: just disable this if the sample isn't saved?
		JMenuItem crossAgainst = Builder.makeMenuItem("cross_against...", true, "crossdate.png");
		crossAgainst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Element secondary = new CachedElement(sample); 
				new Crossdater(editor, ElementList.singletonList(secondary), secondary);
			}
		});
		add(crossAgainst);

		// cross all
		/*
		crossElements = Builder.makeMenuItem("cross_elements", true, "crossdate.png");
		crossElements.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// n-by-n cross
				Sequence seq = new Sequence(sample.getElements(),
						sample.getElements());
				new CrossdateWindow(seq);
			}
		});
		add(crossElements);
		crossElements.setEnabled(false);
		crossElements.setVisible(false);
		*/



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
		/*
		// cross elements: only if elements present, and at least 2 elements
		crossElements.setEnabled(sample.getElements() != null &&
				sample.getElements().size() >= 2);
				*/
	}
}
