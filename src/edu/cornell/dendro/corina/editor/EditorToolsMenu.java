package edu.cornell.dendro.corina.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;

import edu.cornell.dendro.corina.CorinaPermission;
import edu.cornell.dendro.corina.cross.CrossdateDialog;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
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
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;

// REFACTOR: this class needs refactoring.  there's IOEs and FNFEs in here!

public class EditorToolsMenu extends JMenu implements SampleListener {
	private static final long serialVersionUID = 1L;
	
	private Sample sample;
	private Editor editor;

	public EditorToolsMenu(Sample s, Editor e) {
		super(I18n.getText("menus.tools")); // TODO: mnemonic

		this.sample = s;
		this.editor = e;

		sample.addSampleListener(this);

		// truncate
		JMenuItem truncate = Builder.makeMenuItem("menus.tools.truncate", true, "truncate.png");
		truncate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new TruncateDialog(sample, editor);
			}
		});
		add(truncate);

		// reverse
		JMenuItem reverseMenu = Builder.makeMenuItem("menus.tools.reverse", true, "reverse.png");
		reverseMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// reverse, and add to the undo-stack
				editor.postEdit(Reverse.reverse(sample));
			}
		});
		add(reverseMenu);
		reverseMenu.setEnabled(sample.getSampleType() == SampleType.DIRECT
				&& (!sample.hasMeta(Metadata.CHILD_COUNT) || sample.getMeta(
						Metadata.CHILD_COUNT, Integer.class) == 0));

		// ---
		addSeparator();		
		
		// reconcile
		JMenuItem reconcile = Builder.makeMenuItem("menus.tools.new_reconcile", true, "reconcile.png");
		reconcile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBBrowser browser = new DBBrowser(editor, true, false);

				// select the site we're in
				if(sample.meta().hasSiteCode()) 
					browser.selectSiteByCode(sample.meta().getSiteCode());
				
				browser.setTitle(I18n.getText("question.chooseReference"));
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
						Alert.error(I18n.getText("error.loadingSample"),
								I18n.getText("error.cantOpenFile") +": " + ioe.getMessage());
						return;
					}

					OpenRecent.sampleOpened(new SeriesDescriptor(reference), "reconcile");
					
					// open it for fun times
					new ReconcileWindow(sample, reference);
				}
			}
		});
		
		// now, make this remember the last things we reconciled against!
		JMenu reconcileMenu = Builder.makeMenu("menus.tools.reconcile");
		reconcileMenu.putClientProperty("corina.open_recent_action", new OpenRecent.SampleOpener("reconcile") {
			@Override
			public void performOpen(Sample s) {
				// new reconcile window
				new ReconcileWindow(sample, s);
				// move to top of menu
				OpenRecent.sampleOpened(new SeriesDescriptor(s), getTag());
			}
		});
		reconcileMenu.add(reconcile);
		reconcileMenu.addSeparator();
		OpenRecent.makeOpenRecentMenu("menus.tools.reconcile", reconcileMenu, 2);
		add(reconcileMenu);		
	
		// index
		indexMenu = Builder.makeMenuItem("menus.tools.index", true, "index.png");
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
		
		if(!sample.isSummed())
		{
			// Sample is not a sum so add standard 'sum' button
			JMenuItem sumMenuItem = Builder.makeMenuItem("menus.tools.sum", true, "sum.png");
			sumMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					new SumCreationDialog(editor, ElementList.singletonList(new CachedElement(sample)));
				}
			});
			add(sumMenuItem);
		}
		else
		{
			// Sample is already a sum so offer to modify or create a version
			
			JMenuItem modifySum = Builder.makeMenuItem("menus.tools.modifysumandreplace");
			modifySum.setEnabled(false);
			
			JMenuItem modifySumAsVersion = Builder.makeMenuItem("menus.tools.modifysumasversion");
			modifySumAsVersion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					new SumCreationDialog(editor, sample);
				}
			});
			
			JMenu sumSubMenu = Builder.makeMenu("menus.tools.sum", "sum.png");
			sumSubMenu.add(modifySum);
			sumSubMenu.add(modifySumAsVersion);
			add(sumSubMenu);
		}
		
		

		// redate
		JMenuItem redate = Builder.makeMenuItem("menus.tools.redate", true, "redate.png");
		redate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new RedateDialog(sample, editor).setVisible(true);
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
		
		
		// Crossdating	
		JMenuItem crossAgainst = Builder.makeMenuItem("menus.tools.new_crossdate", true, "crossdate.png");
		crossAgainst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Element secondary = new CachedElement(sample); 
				new CrossdateDialog(editor, ElementList.singletonList(secondary), secondary);
			}
		});
		
		// now, make this remember the last things we reconciled against!
		JMenu crossMenu = Builder.makeMenu("menus.tools.crossdateAgainst");
		crossMenu.putClientProperty("corina.crossdate_open_recent_action", new OpenRecent.SampleOpener("crossdate") {
			@Override
			public void performOpen(Sample s) {
				// new crossdate window
				//new ReconcileWindow(sample, s);
				// move to top of menu
				//OpenRecent.sampleOpened(new SeriesDescriptor(s), getTag());
				Element secondary = new CachedElement(sample); 
				new CrossdateDialog(editor, ElementList.singletonList(secondary), secondary);
				OpenRecent.sampleOpened(new SeriesDescriptor(s), getTag());
			}
		});
		
		ITridasSeries series = s.getSeries();
		if(series instanceof TridasDerivedSeries)
		{
			TridasDerivedSeries ds = (TridasDerivedSeries) series;
			if(ds.getType().getValue().equals("Crossdate"))
			{
				JMenuItem reviewCrossdate = Builder.makeMenuItem("crossdate.reviewCrossdate", true, "crossdate.png");
				final CachedElement element = new CachedElement(s);
				reviewCrossdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						new CrossdateDialog(editor, element);
					}
				});
				add(reviewCrossdate);
			}
			else
			{
				crossMenu.add(crossAgainst);
				crossMenu.addSeparator();
				OpenRecent.makeOpenRecentMenu("menus.tools.crossdate", crossMenu, 10);
				add(crossMenu);	
			}
			
		}
		else
		{
			crossMenu.add(crossAgainst);
			crossMenu.addSeparator();
			OpenRecent.makeOpenRecentMenu("menus.tools.crossdate", crossMenu, 10);
			add(crossMenu);	
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
		/*
		// cross elements: only if elements present, and at least 2 elements
		crossElements.setEnabled(sample.getElements() != null &&
				sample.getElements().size() >= 2);
				*/
	}
}
