//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.cross;

import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteNotFoundException;
import corina.map.MapFrame;
import corina.gui.XFrame;
import corina.gui.menus.FileMenu;
import corina.gui.menus.EditMenu;
import corina.gui.menus.WindowMenu;
import corina.gui.menus.HelpMenu;
import corina.gui.PrintableDocument;
// import corina.gui.Tree; -- REMOVE?
import corina.gui.Layout;
import corina.util.Platform;
import corina.util.Sort;
import corina.util.Center;
import corina.util.OKCancel;
import corina.util.JLinedLabel;
import corina.prefs.Prefs;
import corina.prefs.PrefsListener;
import corina.prefs.PrefsEvent;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.ui.Alert;

import java.io.IOException;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.print.PageFormat;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.BorderFactory;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
    A window which displays a crossdate.  Displays all scores and
    significant scores, and lets the user step forward/backward through
    the Sequence, graphing any desired cross.

    <p>The crossdate window operates on two levels.  First, it provides
    3 different views for crossdating Sequences: normal view, table
    view, and grid view.  Second, for the normal view, it has 3 tabs
    for different views of the current Crossdate from that Sequence:
    significant scores, all scores, and a histogram.</p>

    <h2>Left to do</h2>
    <ul>
<pre>
    -- get rid of refreshFromPrefs(), HasPreferences
    -- implement prefsListener - what's the jframe equiv of addNotify()/removeNotify()?
        -- note, also, i might not even need this!
    -- only run new crossdate once, if needed.
        -- each view: if corina.cross.overlap!=cross.overlap, re-run.
            -- need: if cross is running, wait for it
            -- need: a "min overlap" field in crossdate

 -- View->range menuitems aren't working
 -- the JMenuBar should be CrossdateMenuBar extends CorinaMenuBar, so i get file/window/help for free
 -- dim lower part of view menu (ranges) if top part is not "crossdate"
 
  -- error handling.
  -- extract methods viewAsCrossdate(), viewAsTable(), viewAsGrid().  (then i can extract menus)
  -- use new CorinaMenuBar abstraction
  -- need jumpToCrossdate(f,m) method for views
  -- if crossdating a 25-year sample against a 35-year sample with
     minimum_overlap = 50, instead of the normal tabs, put up a label
     explaining why it can't compute any scores for this crossdate,
     and provide an easy way out (if len1>5&&len2>5, add button
     "compute anyway, with minimum overlap = popup([5,10,25,...])",
     else say "this sample is only 5 years long, there's no way you'll
     get anything useful out of that.")
  -- for short samples, don't spawn a new thread.  assume toggling
     button-enabled and thread spawning take zero time (the latter is
     around 1ms), and that we want to keep response time under 50ms on
     a 500mhz computer.  figure out how long the samples can be for that.
  -- ...it depends on the i/o speed, naturally.  load all samples on c'ton
     so it doesn't.
  -- if you can't run the WJ cross because fixed/moving are backwards,
     put up a label saying why it can't run the crossdate, and how to
     change it (cmd-E, swap).
  -- (crossdate kit:) mark files with WJ in lists, so this is easier.
  -- do ScoreRenderer here and index/DecimalRenderer do just the same thing?
     no: DR centers around ".", SR hilites sig scores.  can the latter
     extend the former, though?
  -- extract CrossdateView (=tabs)
  -- extract menus?
  -- start renaming things to be nicer: "cross" => "crossdate", etc.
  -- get rid of GridFrame (is there anything in there i want?)
</pre>
    </ul>

    <p>Future: after extraction, the Crossdate views will only exist as
    part of CrossdateView(?), so I won't even need to talk about them
    here.</p>

    @see corina.cross.TableView
    @see corina.cross.GridView
    @see corina.cross.SignificantScoresView
    @see corina.cross.AllScoresView
    @see corina.cross.HistogramView

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class CrossdateWindow extends XFrame
                          implements PrintableDocument,
                                     PrefsListener {
    // FIXME: XFrame?

    // gui components
    private JTabbedPane tabPane;
    private JButton graphButton, mapButton;
    private JButton prevButton, nextButton;

    // data i'd displaying
    private Sequence sequence;
    private Cross crossdate=null;

    // titles on top
    private JLabel fixedTitle;
    private JLabel movingTitle;
    // private Tree fixedTree;
    // private Tree movingTree;

    // convert fixedFloats/movingFloats to a string, for storing in the prefs
    private String encode() {
        if (fixedFloats && !movingFloats)
            return "fixed";
        else if (!fixedFloats && movingFloats)
            return "moving";
        else
            return "both";
    }
    // set fixedFloats/movingFloats based on a string, which was stored in the prefs
    private void decode(String pref) {
        if (pref.equals("fixed")) {
            fixedFloats = true;
            movingFloats = false;
        } else if (pref.equals("both")) {
            fixedFloats = true;
            movingFloats = true;
        } else { // "moving"
            fixedFloats = false;
            movingFloats = true;
        }
    }

    private JPanel defaultView; // MOVE ME: put all views together in this file

    // gui setup
    private void initGui() {
        // cards!
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards);

        // default view
        defaultView = new JPanel(new BorderLayout());
        cards.add(defaultView, "default");
        
        // bottom panel -------------------------------------------------

        // prev
        prevButton = Builder.makeButton("prev");
        if (!Platform.isMac)
	    prevButton.setIcon(Builder.getIcon("Back.png"));
	// or even: "corina.cross.Back" (generate from SVG as needed)

        // next
        nextButton = Builder.makeButton("next");
	if (!Platform.isMac)
	    nextButton.setIcon(Builder.getIcon("Next.png"));

        // actionlistener for prev/next
        Action prevNext = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // disable buttons during processing
                disableButtons();

                try {
                    if (e.getSource() == prevButton)
                        sequence.prevPairing();
		    else // nextButton
                        sequence.nextPairing();
                    crossdate = sequence.makeCross();
                } catch (IOException ioe) {
                    System.out.println("ioe! -- " + ioe);
                    // DEAL WITH ME INTELLIGENTLY!
                }

                // run it in its own thread, and then do the gui stuff
		new RunThread().start();
            }
        };
        prevButton.addActionListener(prevNext);
        nextButton.addActionListener(prevNext);

        // graph
        graphButton = Builder.makeButton("plot");
        graphButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
		// FUTURE: get component on tab, cast to CrossdateView,
		// call (CrossdateView).graphSelectedCrossdate().
		// ALSO: when the tab changes, ask the view if it can
		// graph.  no, better: give it the button, so it can
		// enable/disable as it likes.

		// HACK: "graph" button for sigs, all
		Component panel = tabPane.getSelectedComponent();
		if (panel instanceof SignificantScoresView) {
		    ((SignificantScoresView) panel).graphSelectedCrossdate();
		    return;
		} else if (panel instanceof AllScoresView) {
		    ((AllScoresView) panel).graphSelectedCrossdate();
		    return;
		}

		// HistogramView: can't happen
            }
        });

        // map
        mapButton = Builder.makeButton("map");
        mapButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    // figure out the locations
                    Site s1 = SiteDB.getSiteDB().getSite(crossdate.getFixed());
                    Site s2 = SiteDB.getSiteDB().getSite(crossdate.getMoving());

                    // draw the map
                    new MapFrame(s1, s2);
                } catch (SiteNotFoundException e) {
		    // (can't happen)
                    throw new IllegalArgumentException();
                }
            }
        });

        // put buttons in panel (with border)
	JPanel b = Layout.buttonLayout(graphButton, mapButton,
				       null,
				       prevButton, nextButton);
	b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	defaultView.add(b, BorderLayout.SOUTH);

        // center panel ---------------------------------------------------
        tabPane = new JTabbedPane();
        defaultView.add(tabPane, BorderLayout.CENTER);

        // (dim "graph" button for histogram tab)
        tabPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
		// (see also enableButtons())

		Component component = tabPane.getSelectedComponent();
		boolean isHistogram = (component instanceof HistogramView);
		graphButton.setEnabled(!isHistogram);
            }
        });

        // top panel ------------------------------------------------------
        /*
	  Fixed: [@] [Zonguldak, Karabuk 3A]
	  Moving: [@] [Zonguldak, Karabuk 4A]
        */

        // fixedTree = new Tree(crossdate.fixed);
        fixedTitle = new JLabel(crossdate.getFixed().toString());

	JPanel f = Layout.flowLayoutL(new JLabel(I18n.getText("fixed") + ":"),
				      fixedTitle);

        // movingTree = new Tree(crossdate.moving);
        movingTitle = new JLabel(crossdate.getMoving().toString());

	JPanel m = Layout.flowLayoutL(new JLabel(I18n.getText("moving") + ":"),
				      movingTitle);

	JPanel top = Layout.boxLayoutY(f, m);
        // defaultView.add(top, BorderLayout.NORTH);

	JButton edit = new JButton("Edit...");
	edit.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // (taken from cmd-E code, below)
		    dispose();
		    new CrossdateKit(sequence);
		    // TODO: i should also pass to the CDK c'tor:
		    // -- my size/position on screen
		    // -- which crossdate i'm looking at
		}
	    });
	JPanel center = Layout.borderLayout(edit,
					    null, null, null,
					    null);
	JPanel full = Layout.borderLayout(null,
					  top, null, center, // ugh, lousy names
					  null);
	defaultView.add(full, BorderLayout.NORTH);
    }

    private class RunThread extends Thread {
	public void run() {
	    try {
		if (bad) {
		    EventQueue.invokeLater(new Runnable() {
			    public void run() {
				// remove last component -- not terribly reliable ...
				defaultView.remove(defaultView.getComponentCount()-1);
				bad = false;
				defaultView.add(tabPane, BorderLayout.CENTER);

				// repaint();
			    }
			});
		}

		// run the cross.  this can throw IllegalArgumentException,
		// if it's not a valid cross.
		// for 2 2000-year-long samples, can take 1000-2000 ms (!).
		crossdate.run();

		// update tables
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    // show tables
			    // -- FIXME: this special case should be handled inside updateTables()
			    if (tabPane.getTabCount() == 0) {
				initTables(); // takes 50-100 ms
			    } else {
				updateTables(); // after the first time, takes <10 ms
			    }
			}
		    });

	    } catch (IllegalArgumentException iae) {
		// crossdate couldn't run --> exception says why
		// JLabel label = new JLabel(iae.getMessage(), JLabel.CENTER);
		final String message = iae.getMessage();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    JLinedLabel label = new JLinedLabel(message);
			    defaultView.remove(tabPane);
			    defaultView.add(label, BorderLayout.CENTER);
			    bad=true;
			    // repaint();
			}
		    });

		// DESIGN: if overlap too short, offer to "Set minimum overlap to <x> for this crossdate only."?
		// DESIGN: if can't run WJ cross, and swapping them would help,
		// say so, and say how to do it (cmd-E, swap) -- also offer "swap" button?

		// DESIGN: if you're looking at A-vs-B-with-X, and hit cmd-E, and return, return as close
		// as possible -- A-vs-B-with-Y, or B-vs-A-with-X, or something like that.  not always 1-1-1.

		// BUG: if the overlap is too short, and it's changed in the prefs, it should update the view properly
	    } finally {
		// these better run no matter what
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    // update titles
			    fixedTitle.setText(crossdate.getFixed().toString());
			    movingTitle.setText(crossdate.getMoving().toString());
			    // fixedTree.setSample(crossdate.getFixed());
			    // movingTree.setSample(crossdate.getMoving());

			    // enable buttons (very important!)
			    enableButtons();

			    // window title
			    setTitle(crossdate.toString());

			    // repaint(); // needed?
			}
		    });
	    }
	}
    }

    private boolean bad=false; // FIXME: what's this?

    private void updateTables() {
	// sort
        if (sort != null) // (null=user hasn't sorted yet -- FIXME?)
            Sort.sort(crossdate.highScores, sort, !sort.equals("number"));
	// sort by last sort, again -- BUG: doesn't store |reverse| flag

	// TESTING! -- problem: this doesn't update the table header (why not?)
	sigs.setCrossdate(crossdate);
	all.setCrossdate(crossdate);
	histo.setCrossdate(crossdate);
    }

    // initialize both tables
    private void initTables() {
	// stuff things into tabs
	try {
	    all = new AllScoresView(crossdate);
	    sigs = new SignificantScoresView(crossdate);
	    histo = new HistogramView(crossdate);
	    // BUG: what if one of these c'tors aborts?  i should still show
	    // the other 2, right?  (along with a "this is a bug" message.)
	} catch (Exception e) {
	    // NOTE: histogram is dying right now; you can remove this later.
	    new corina.gui.Bug(e);
	}

	tabPane.addTab(I18n.getText("sig_scores"), sigs);
	tabPane.addTab(I18n.getText("all_scores"), all);
	tabPane.addTab(I18n.getText("score_distro"), histo);
    }

    private AllScoresView all;
    private SignificantScoresView sigs;
    private HistogramView histo;

    private String sort=null;

    // do both fixed and moving sites have a site in the sitedb,
    // with a non-null location?
    // BUG: theoretically, i should have a listener to watch this,
    // and update mapButton if anything changes
    private boolean mapAvailable() {
	// REFACTOR: should it be sample.getSite(), or Site.getSite(sample)?
        try {
            Site s1 = SiteDB.getSiteDB().getSite(crossdate.getFixed());
            Site s2 = SiteDB.getSiteDB().getSite(crossdate.getMoving());
            return (s1.getLocation()!=null && s2.getLocation()!=null);
        } catch (SiteNotFoundException snfe) {
            return false;
        }
    }

    private void enableButtons() {
        prevButton.setEnabled(!(sequence.isFirst()));
        nextButton.setEnabled(!(sequence.isLast()));
        graphButton.setEnabled(!(tabPane.getSelectedComponent() instanceof HistogramView));
        mapButton.setEnabled(mapAvailable());
    }
    private void disableButtons() {
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        graphButton.setEnabled(false);
        mapButton.setEnabled(false);
    }

    /** Create a crossdate for a given Sequence.
	@param sequence the Sequence to display */
    public CrossdateWindow(Sequence sequence) {
        // REFACTOR: makeCross(), disableButtons(), and start()/start()
	// exist in two places.

        // copy data
        this.sequence = sequence;
        try {
            crossdate = sequence.makeCross();
        } catch (IOException ioe) {
            // XXX: DEAL WITH ME!
        }

        // init gui
        initGui();

        // menubar
        {
            JMenuBar menubar = new JMenuBar();

            menubar.add(new FileMenu(this));
            menubar.add(new CrossdateEditMenu());
            menubar.add(new CrossdateViewMenu());
            if (Platform.isMac)
                menubar.add(new WindowMenu(this));
            menubar.add(new HelpMenu());
            
            setJMenuBar(menubar);

            // REFACTOR: use new CorinaMenuBar abstraction
        }

        // don't let user click any buttons until the first cross is done
        disableButtons();

        // run it in its own thread, and then do the gui stuff.
	// (note: if you extend this class, weird things can happen
	// because the thread will get started before anything in
	// the subclass constructor gets run.)
	new RunThread().start();
        
        // show
        pack();
        setSize(new Dimension(640, 480));
        show();
    }

    // what i'm actually displaying
    private CardLayout cardLayout;
    private JPanel cards;

    // null=not made yet
    private JComponent tableView = null;
    private JComponent gridView = null;

    private int view = 0; // 0=default, 1=table, 2=grid

    // our Edit menu has the additional menu item "Edit this Crossdate" (or
    // something like that) which brings up a CrossdateKit.
    private class CrossdateEditMenu extends EditMenu {
        protected void init() {
            // BETTER: addTop(), addBottom()?

            addUndoRedo();
            addSeparator();
            
            addClipboard();
            addSeparator();

            addSelectAll();
            
            addSeparator();
            addKit();

            addPreferences();
        }

        // nobody ever sees this.  should i get rid of it?  (keyboard accel is pretty handy.)
        private void addKit() {
            JMenuItem kit = Builder.makeMenuItem("edit_crossdate");
            kit.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new CrossdateKit(sequence);
                }
            });

            // BUG: after cmd-E, put window back where it was (location-wise)
            // BUG: after cmd-E, put it back in the same view (e.g., table-view)

            add(kit);
        }
    }
    
    private class CrossdateViewMenu extends JMenu {
        CrossdateViewMenu() {
            super(I18n.getText("view"));

            JMenuItem asCross = Builder.makeRadioButtonMenuItem("cross_as_cross");
            asCross.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    // nothing need be done, right?
                    cardLayout.show(cards, "default");
                    setTitle(crossdate.toString());
                    view = 0;
                }
            });

            JMenuItem asTable = Builder.makeRadioButtonMenuItem("cross_as_table");
            asTable.setEnabled(true); // FIXME: enable for 1-by-n crossdates only?
            asTable.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // make table, if null
                        if (tableView == null) {
                            tableView = new TableView(sequence);

                            // add card
                            cards.add(tableView, "table");
                        }

                        // show card
                        cardLayout.show(cards, "table");
                        setTitle(tableView.toString());
                        view = 1;

                    } catch (IOException ioe) {
                        // MOVE ME UP: tableView() shouldn't throw anything
                        System.out.println("ack -- " + ioe);
                    }
                }
            });

            JMenuItem asGrid = Builder.makeRadioButtonMenuItem("cross_as_grid");
            asGrid.setEnabled(true); // FIXME: enabled for n-by-n crossdates only?
            asGrid.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    // make grid, if null
                    if (gridView == null) {
                        // make gridview
                        gridView = new GridView(sequence);

                        // add card
                        cards.add(gridView, "grid");
                    }

                    // show card
                    cardLayout.show(cards, "grid");
                    setTitle(gridView.toString());
                    view = 2;
                }
            });

            // menuitems (final for inner class use)
            final JMenuItem moving = Builder.makeRadioButtonMenuItem("second_moving");
            final JMenuItem fixed = Builder.makeRadioButtonMenuItem("first_moving");
            final JMenuItem both = Builder.makeRadioButtonMenuItem("both_moving");

            // read old value from prefs, set window accordingly
            if (Prefs.getPref("corina.cross.dating") != null) {
                decode(Prefs.getPref("corina.cross.dating"));
                if (!fixedFloats && movingFloats)
                    moving.setSelected(true);
                else if (fixedFloats && !movingFloats)
                    fixed.setSelected(true);
                else
                    both.setSelected(true);
            } else {
                moving.setSelected(true);
            }

            // an action to call when one of the 3 menuitems is selected
            Action a = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    // based on menuitem, set flags
                    Object source = e.getSource();
                    if (source == moving) {
                        fixedFloats = false;
                        movingFloats = true;
                    } else if (source == fixed) {
                        fixedFloats = true;
                        movingFloats = false;
                    } else { // both
                        fixedFloats = true;
                        movingFloats = true;
                    }

                    // update view
                    repaint();

                    // set pref
                  Prefs.setPref("corina.cross.dating", encode());
                }
            };
            moving.addActionListener(a);
            fixed.addActionListener(a);
            both.addActionListener(a);

            { // views radio button group -- this always starts as-cross
                ButtonGroup views = new ButtonGroup();
                views.add(asCross);
                views.add(asTable);
                views.add(asGrid);
                asCross.setSelected(true);
            }

            { // ranges radio button group
                ButtonGroup ranges = new ButtonGroup();
                ranges.add(fixed);
                ranges.add(moving);
                ranges.add(both);
            }

            add(asCross);
            add(asTable);
            add(asGrid);
            addSeparator();
            add(moving);
            add(fixed);
            add(both);
        }
    }

    private boolean fixedFloats = false, movingFloats = true;

    /*
      if i implement the "BETTER" idea below (changes
      crossdate.range/span and views), i won't need to have a prefs
      listener here at all.  this window frame, after all, doesn't use
      any prefs -- only the views do.  i'll only need to make sure i
      add/remove prefs listeners to them on add/remove notify (but
      that, too, doesn't get done here).
    */
    // DESIGN: does this need to re-run the crossdate?
    // well, if the overlap changes, i'll need more/less data...
    // so, yeah.  ouch.
    // PERF: slow, especially if long sample and min_overlap didn't change
    // -- it's only needed if min_overlap changed, right?  that's ok.
    public void prefChanged(PrefsEvent e) {
	if (e.getPref().equals("corina.cross.overlap")) {
	    new RunThread().start(); // ugly, but necessary (i think)
	    // BETTER: why not always compute the full overlap, but
	    // only display the relevant parts?  then changing the overlap
	    // wouldn't require recomputing anything.
	    // (downside: for long samples with short overlap, it
	    // would seem to take longer -- but not much, i think)
	} else {
	    // WRITEME! -- what else can change?
	    new RunThread().start();
	}
    }

    //
    // printing
    //

    public Object getPrinter(PageFormat pf) {
	// TODO: instead of switching on some variable,
	// make all views implement PrintableDoc themselves,
	// and just ask the current view to print itself.
	switch (view) {
	case 0:
	    boolean sections[] = askSections();
	    CrossdatePrinter printer = new CrossdatePrinter(crossdate,
							    sections[0],
							    sections[1],
							    sections[2]);
	    return printer;
	case 1:
	    return ((TableView) tableView).print();
	case 2:
	    return ((GridView) gridView).print(pf);
	default:
	    throw new IllegalArgumentException("eek!");
	}
    }

    public String getPrintTitle() {
	// title of window -- which may be something like "Crossdating Table"
        return getTitle();
    }

    // ask what to print; return the sections as boolean[3],
    // representing print-sigs, print-all, and print-histo.
    private boolean[] askSections() {
        final JDialog d = new JDialog(this, "Print Views", true);

        int tab = tabPane.getSelectedIndex();
        JCheckBox sigsCheckbox = new JCheckBox("Significant Scores");
        JCheckBox allCheckbox = new JCheckBox("All Scores");
        JCheckBox histoCheckbox = new JCheckBox("Histogram");
	sigsCheckbox.setSelected(tab == 0);
	allCheckbox.setSelected(tab == 1);
	histoCheckbox.setSelected(tab == 2);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(10));
        center.add(sigsCheckbox);
        center.add(Box.createVerticalStrut(6));
        center.add(allCheckbox);
        center.add(Box.createVerticalStrut(6));
        center.add(histoCheckbox);
        center.add(Box.createVerticalStrut(12));
	// with an empty[4,0,6,0] border and vgap=6, this is just Layout.boxLayoutY(sigs,all,histo)
	// (but i can't set vgap on that, yet -- ack.)

	// want to force the window's width somehow: add some space here
        JButton cancel = Builder.makeButton("cancel");
        JButton ok = Builder.makeButton("ok");
	Component spacer = Box.createHorizontalStrut(24);
	JPanel buttons = Layout.buttonLayout(spacer, cancel, ok);

	// add some more space here
	Component spacer2 = Box.createHorizontalStrut(20);
	JPanel p = Layout.borderLayout(new JLabel("Print which views?"),
				       spacer2, center, null,
				       buttons);
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        d.setContentPane(p);

//        boolean reallyPrint=false;
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
        ok.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
//                reallyPrint = true;
            }
        });

        OKCancel.addKeyboardDefaults(ok);
        d.setResizable(false);
        d.pack();
	Center.center(d, this);
        d.show();

	// BUG: if user cancels (!reallyPrint), throw UCE here!

	boolean sections[] = new boolean[] {
	    sigsCheckbox.isSelected(),
	    allCheckbox.isSelected(),
	    histoCheckbox.isSelected(),
	};
	return sections;
    }
}
