package edu.cornell.dendro.corina.cross;

import edu.cornell.dendro.corina.Element;
import edu.cornell.dendro.corina.util.UserFriendlyFile;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.Tree;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.Help;

import java.io.File;
import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.ScrollPaneConstants;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Component;

/**
   A dialog view of a Sequence.  On the top, it shows the fixed and
   moving samples, in two lists.  Users can move or copy samples
   between the lists, and add and remove them, simply by dragging.  On
   the bottom, checkboxes let you pick which crossdates to run on the
   samples (e.g., T-score or trend).  A "Run" button will run the
   requested crossdates, i.e., in a CrossdateWindow.

   @see edu.cornell.dendro.corina.cross.Sequence
   @see edu.cornell.dendro.corina.cross.CrossdateWindow

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/

/*
 TODO:
 -- should DEFAULT_CROSSDATES, ALL_CROSSDATES live in here?  this is the
    only place they're used.  no, but they shouldn't be the huge
    vulnerabilities that they are.  better interface: static String[]
    getAllCrossdates()/getDefaultCrossdates().
 -- "help" button should go to a more appropriate place in the manual
 -- refactor
 -- clean up .* imports
 -- layout of bottom buttons is ugly
 -- layout of labels/lists is ugly
 -- (when D&D is done, only the "swap" button will really be needed.)
 -- resizeable would be nice...
 -- checkboxes need more spacing on the left side

 -- doesn't handle drags 100%:
 -- fix drags: drop says "rejected", but it still appears -- what's up?
 -- fix drags: draw component(s) correctly
 -- fix drags: drag=move, mac+option / win32+control = copy
 -- (fix drags: if dragging onto same list, re-order? -- at the very least, do nothing)

 -- drag-over border might be 1px too short in both directions when THICKNESS is odd

 -- show icons in list? -- (need custom renderer) -- sure.
 -- show titles, not filenames -- filenames will soon be obsolete, and aren't available for all sources, anyway.
 -- if a folder is dropped, show it as a folder icon, with "(3 items)" appended to the title
 ---- when "run" is clicked, convert all folders to all of their child files.
 ---- (oops: then you don't get back the folder if you press cmd-E later.  hmm...)
 
 -- add undo: watch for accel-Z (no need for menuitem).
*/

public class CrossdateKit extends JDialog {

    // a label, a (scrollable) list of samples, and add/remove buttons.
    // supports keys (delete, cmd-A, ...), and drag-n-drop (opt-drag=copy).
    private static class SampleList extends JPanel {
	// REFACTOR: since this is 50% the size of the CrossdateKit class itself,
	// why not EXTRACT CLASS?
	// REFACTOR: add anything more, and this starts to look like the browser.
	// can i combine them?  crossdatekit = samplelist backed by array,
	// browser = samplelist backed my folder, etc.
        JButton add, remove;
        JList list;
        DefaultListModel model;
	private boolean hilite=false; // ="something is being dragged over me"
        SampleList(String title) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            // label
	    add(Layout.flowLayoutL(title));

            // list
            model = new DefaultListModel();
            list = new JList(model);
	    JScrollPane sp = new JScrollPane(list,
					     ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) {
		    @Override
			public void paint(Graphics g) {
			super.paint(g); // component, border, children
			if (hilite) {
			    final int THICKNESS = 3; // extract me?  (Platform?  UniversalConsts?)
			    Color border = list.getSelectionBackground();
			    g.setColor(border);
			    Graphics2D g2 = (Graphics2D) g;
			    g2.setStroke(new BasicStroke(THICKNESS));
			    Insets in = this.getInsets();
			    int scrollbarWidth = getVerticalScrollBar().getWidth();
			    g2.drawRect(in.left+THICKNESS/2,
					   in.top+THICKNESS/2,
					this.getWidth()-in.right-in.left-scrollbarWidth-THICKNESS,
					   this.getHeight()-in.bottom-in.top-THICKNESS);
			}
		    }
		};
            add(sp);

	    // TESTING: a cell renderer, so bogus files appear in red
	    list.setCellRenderer(new DefaultListCellRenderer() {
		    // BETTER?: put an alert icon (yellow yield sign?  red stop sign?)
		    // on the right edge of the cell?
		    // TODO: good; also put this icon to the right of "fixed/moving"
		    // text above the list, so if one that's scrolled off is bogus,
		    // they can still see it.
		    @Override
			public Component getListCellRendererComponent(JList list,
								  Object value,
								  int index,
								  boolean isSelected,
								  boolean cellHasFocus) {
			JLabel normal = (JLabel) super.getListCellRendererComponent(list,
										    value,
										    index,
										    isSelected,
										    cellHasFocus);

			// TESTING: every other one
			// if (index % 2 == 0)
			// normal.setForeground(Color.red);

			// TESTING: draw warning icon
			/*
			normal = new JLabel() {
				public void paintComponent(Graphics g) {
				    normal.paintComponent(g);
				    g.drawImage
				}
			    };
			*/

			return normal;
		    }
		});

            // add
            add = new JButton("Add...");
            add.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        List chosen = FileDialog.showMulti("Add");
                        for (int i=0; i<chosen.size(); i++) {
			    Element el = (Element) chosen.get(i);
                            model.addElement(new UserFriendlyFile(el.getFilename()));
			}
                    } catch (UserCancelledException uce) {
                        // do nothing
                    }
                }
            });

            // remove
            remove = new JButton("Remove");
            remove.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    removeSelectedRows();
                }
            });
            remove.setEnabled(false); // by default, no entries, hence, no selection

            // dim-undim "remove" as necessary
            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    remove.setEnabled(!list.isSelectionEmpty());
                }
            });

            // buttons, on the bottom
            add(Box.createVerticalStrut(8));
	    add(Layout.buttonLayout(add, remove));

            // delete => removeSelectedRows()
            list.addKeyListener(new KeyAdapter() {
                @Override
				public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                        removeSelectedRows();
                }
            });

            // handle drops
            // REFACTOR: STOLEN FROM DROPPLOTTER.JAVA
            new DropTarget(list, new DropTargetListener() {
                public void dragEnter(DropTargetDragEvent event) {
                    event.acceptDrag(DnDConstants.ACTION_COPY); // accept
		    // WRITEME: accept MOVES, too?
                }
                public void dragOver(DropTargetDragEvent event) {
		    // turn on hilite
		    hilite = true;
		    repaint(); // should repaint() calls really be something like revalidate()?
		}
                public void dragExit(DropTargetEvent event) {
		    // turn off hilite
		    hilite = false;
		    repaint();
		}
                public void dropActionChanged(DropTargetDragEvent event) {
		    // ignore
		    // WRITEME: don't ignore!
		}
                public void drop(DropTargetDropEvent event) {
                    try {
                        Transferable transferable = event.getTransferable();

                        // we accept only filelists
                        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            event.acceptDrop(DnDConstants.ACTION_COPY);
                            Object o = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                            List l = (List) o; // a List of Files

			    // make a copy.  i don't know that i'm not
			    // getting an immutable List here -- yep,
			    // they exist -- and i'll want to sort it.
			    int n = l.size();
			    List l2 = new ArrayList(n);
			    for (int i=0; i<n; i++)
				l2.add(l.get(i));

                            // sort files here by name, or things just plain look weird.
			    Collections.sort(l2);

                            // add each one in turn
                            for (int i=0; i<n; i++) {
                                String pathname = ((File) l2.get(i)).getPath();
                                model.addElement(new UserFriendlyFile(pathname));
				// REFACTOR: use addFiles(), so we skip dupes (or no?)
                            }

                            event.getDropTargetContext().dropComplete(true);
			    // System.out.println("drop is complete");
                        } else {
			    System.out.println("rejecting drop: bad type");
                            event.rejectDrop();
                        }
                    } catch (IOException ioe) {
			System.out.println("rejecting drop: ioe");
                        event.rejectDrop(); // handle error? -- tell user what went wrong?
                    } catch (UnsupportedFlavorException ufe) {
			System.out.println("rejecting drop: ufe");
                        event.rejectDrop(); // handle error? -- can this even happen?
                    }
                }
            });
            
	    // handle drags
	    DragGestureListener dragger = new DragGestureListener() {
		    public void dragGestureRecognized(DragGestureEvent event) {
			int i = list.getSelectedIndex();
			Object v = list.getSelectedValue();

			// nothing there
			if (i == -1)
			    return;

			// use real image for this drag
			Component c = list.getCellRenderer().getListCellRendererComponent(list, v, i, false, true);
			Dimension d = c.getPreferredSize();
			int w = d.width, h = d.height;
			w = list.getWidth(); // BUT: we don't want preferred-width, we want width-as-drawn

			BufferedImage img = new BufferedImage(w, h,
							      BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics2D g2 = (Graphics2D) img.getGraphics();
			// g2.setColor(Color.red);
			// g2.fillRect(0, 0, w, h);

			// draw real component here
			c.setBounds(0, 0, w, h);
			c.paint(g2);

			// WRITEME: what about multiple selections?  need to maybe draw several lines to my |img|...

			// draw black border -- do i want this?
			// g2.setColor(Color.black);
			// g2.drawRect(0, 0, c.getWidth()-1, c.getHeight()-1);

			// a transferable: use transferablefilelist, which takes any number of files.
			Transferable t=null; // (placate compiler)
			{
			    List l = new ArrayList();
			    Object o[] = model.toArray();
			    int selection[] = list.getSelectedIndices();
			    for (int j=0; j<selection.length; j++)
				l.add(o[selection[j]]);
			    t = new Tree.TransferableFileList(l);
			}

			// point that this
			Point pt = new Point(-event.getDragOrigin().x, -(event.getDragOrigin().y % h));

			event.startDrag(DragSource.DefaultMoveDrop, // was: DefaultCopyDrop
					img, pt,
					t,
					new Tree.EmptyDragSourceListener());
		    }
		};

	    drag = new DragSource();
	    drag.createDefaultDragGestureRecognizer(list, // component
						    DnDConstants.ACTION_MOVE, // always move?  was: ACTION_COPY
						    dragger); // dragger

	    // only big things left:
	    // 1: make the default action move, with option- forcing a copy
	    // 2: figure out why it thinks it's failing (even though it's not) [DONE?]
	    // 3: get image right when multiple files are selected

	    /*
	      new DragSourceListener() {
	      // do i want any of these?
	      public void dragDropEnd(DragSourceDropEvent event) { }
	      public void dragEnter(DragSourceDragEvent event) { }
	      public void dragExit(DragSourceEvent event) { }
	      public void dragOver(DragSourceDragEvent event) { }
	      public void dropActionChanged(DragSourceDragEvent event) { }
	      }
	    */
        }
	private DragSource drag;
        void removeSelectedRows() {
            int selection[] = list.getSelectedIndices();
            // do backwards, so earlier indices don't change under me
            for (int i=selection.length-1; i>=0; i--)
                model.removeElementAt(selection[i]);
        }
        List getSelectedFiles() {
            List files = new ArrayList();
            Object o[] = model.toArray();
            int selection[] = list.getSelectedIndices();
            for (int i=0; i<selection.length; i++)
                files.add(o[selection[i]]);
            return files; // refactor?
        }
        List getFiles() {
            List files = new ArrayList();
            int n = model.size();
            Object o[] = model.toArray();
            for (int i=0; i<n; i++)
                files.add(o[i]);
            return files;
        }
        void setFiles(List files) {
            // remove all
            model.removeAllElements();

            // now add these
            addFiles(files);
        }
        void addFiles(List files) {
            for (int i=0; i<files.size(); i++)
		if (!model.contains(files.get(i))) // skip dupes (do i really want this?)
		    model.addElement(files.get(i));
	    // REFACTOR: also in drop(dtde) files get added, but addFiles() isn't used, so we don't get the dupe-skipping code.  oops.
        }
        JList getJList() { // so you can add your own listeners n'stuff
            return list;
        }
    }

    // ============================================================

    private SampleList fixed, moving;
    private JButton move1, move2, copy1, copy2;
    private JButton run;
    
    private void setup() {
        setTitle("Crossdate Sequence");

        fixed = new SampleList("Fixed samples:");
        moving = new SampleList("Moving samples:");

        copy1 = new JButton("Copy >");
        copy1.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moving.addFiles(fixed.getSelectedFiles());
            }
        });

        copy2 = new JButton("< Copy");
        copy2.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                fixed.addFiles(moving.getSelectedFiles());
            }
        });

        move1 = new JButton("Move >");
        move1.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moving.addFiles(fixed.getSelectedFiles());
                fixed.removeSelectedRows();
            }
        });

        move2 = new JButton("< Move");
        move2.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                fixed.addFiles(moving.getSelectedFiles());
                moving.removeSelectedRows();
            }
        });

        JButton swap = new JButton("< Swap >"); // need to say "swap _all_"?
        swap.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                List temp = fixed.getFiles();
                fixed.setFiles(moving.getFiles());
                moving.setFiles(temp);
            }
        });

        run = new JButton("Run");
        run.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // close window
                dispose();

                // convert lists of (UserFriendly)Files to lists of filenames
                // yeah, just (setq fixed-list (mapcar 'namestring (list-get-files fixed))), etc.
                List fixedList = fixed.getFiles();
                List movingList = moving.getFiles();
                for (int i=0; i<fixedList.size(); i++)
                    fixedList.set(i, ((File) fixedList.get(i)).getPath());
                for (int i=0; i<movingList.size(); i++)
                    movingList.set(i, ((File) movingList.get(i)).getPath());

                // run crosses
		Sequence seq = new Sequence(fixedList, movingList);
		seq.setAlgorithms(getAlgorithms());
                new CrossdateWindow(seq);
            }
        });

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(copy1);
        center.add(Box.createVerticalStrut(4));
        center.add(copy2);
        center.add(Box.createVerticalStrut(18));
        center.add(move1);
        center.add(Box.createVerticalStrut(4));
        center.add(move2);
        center.add(Box.createVerticalStrut(18));
        center.add(swap);

        // by default, nothing's selected, so dim them all
        copy1.setEnabled(false);
        copy2.setEnabled(false);
        move1.setEnabled(false);
        move2.setEnabled(false);

        // handle dimming/undimming for copy/move buttons (this is slightly awkward ... ?)
        fixed.getJList().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                boolean selectionEmpty = list.isSelectionEmpty();
                copy1.setEnabled(!selectionEmpty);
                move1.setEnabled(!selectionEmpty);
            }
        });
        moving.getJList().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                boolean selectionEmpty = list.isSelectionEmpty();
                copy2.setEnabled(!selectionEmpty);
                move2.setEnabled(!selectionEmpty);
            }
        });

        // handle dimming/undimming for run-button
        ListDataListener listener = new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                change();
            }
            public void intervalAdded(ListDataEvent e) {
                change();
            }
            public void intervalRemoved(ListDataEvent e) {
                change();
            }
	    private void change() { // does this really need to be its own method now?
		updateRun();
            }
        };
        fixed.getJList().getModel().addListDataListener(listener);
        moving.getJList().getModel().addListDataListener(listener);
        run.setEnabled(false); // by default, they're both empty, so it's not enabled -- right?

        JPanel leftRight = new JPanel();
        leftRight.setLayout(new BoxLayout(leftRight, BoxLayout.X_AXIS)); // was: GridLayout(1,2)
        leftRight.add(fixed);
        leftRight.add(Box.createHorizontalStrut(8));
        leftRight.add(center);
        leftRight.add(Box.createHorizontalStrut(8));
        leftRight.add(moving);

        JButton help = new JButton("Help");
	Help.addToButton(help, "crossdating");
	JPanel buttons = Layout.buttonLayout(help, null, run);

        JPanel chunk = new JPanel();
        chunk.setLayout(new BoxLayout(chunk, BoxLayout.Y_AXIS));
        {
            // add smaller detailed text here?
	    // chunk.add(Layout.flowLayoutL("Run a new sequences of crossdates with the following samples:"));
        }
        chunk.add(leftRight);
        chunk.add(Box.createVerticalStrut(14));
	chunk.add(makeCheckboxes());
        chunk.add(Box.createVerticalStrut(14));
        chunk.add(buttons);

        JPanel stuff = new JPanel();
        stuff.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 14, 20, 20, 20));
        stuff.add(chunk);
        setContentPane(stuff);
        
        OKCancel.addKeyboardDefaults(run);

	// resizable would be nice, but my layout managers
	// murder it when i resize it.
	// (perhaps in a future version.)
	setResizable(false);
    }

    // update "run" button, either enabled or no
    private void updateRun() {
	// make sure at least one of crossEnabled[i] is true
	boolean checkboxesOk = crossEnabled[0];
	for (int i=1; i<crossEnabled.length; i++)
	    checkboxesOk |= crossEnabled[i];

	// make sure both fixed and moving lists have at least 1 element
	boolean fixedEmpty = (fixed.getJList().getModel().getSize() == 0);
	boolean movingEmpty = (moving.getJList().getModel().getSize() == 0);
	boolean listsOk = (!fixedEmpty && !movingEmpty);

	// use that
	run.setEnabled(listsOk && checkboxesOk);
    }

    // get the name of a crossdate -- HACK!
    // can't use Cross.getName(), because static methods don't inherit, and
    // can't use Cross.makeCross(), because that assumes non-null samples
    private String getName(String className) {
	try {
	    Cross c = (Cross) Class.forName(className).newInstance();
	    return c.getName();
	} catch (Exception e) {
	    new Bug(e);
	    return className;
	}
    }

    // make checkboxes, using ALL_CROSSDATES
    private JPanel makeCheckboxes() {
	JPanel p2 = new JPanel(new GridLayout(1, 0));

	final int n = Cross.ALL_CROSSDATES.length;
	final JCheckBox checkboxes[] = new JCheckBox[n];
	for (int i=0; i<n; i++) {
	    String name = getName(Cross.ALL_CROSSDATES[i]);
	    checkboxes[i] = new JCheckBox(name, crossEnabled[i]);
	    p2.add(checkboxes[i]);
	}

	// one action for all checkboxes
	AbstractAction a = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // reverse this crossEnabled[i]
		    JCheckBox s = (JCheckBox) e.getSource();
		    for (int i=0; i<n; i++)
			if (s == checkboxes[i])
			    crossEnabled[i] = !crossEnabled[i];

		    // update "run" button
		    updateRun();
		}
	    };
	for (int i=0; i<n; i++)
	    checkboxes[i].addActionListener(a);

	// don't really need these, do i?  just, instead of reading flags, read the state of the checkboxes.
	// well, actually, i might as well.  i need to set run based on the checkboxes, anyway.

	JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(Layout.flowLayoutL("Select algorithms to use:"));
	p.add(Box.createVerticalStrut(8));
	p.add(p2);

	return p;
    }

    // is this crossdate enabled?
    private boolean crossEnabled[] = new boolean[Cross.ALL_CROSSDATES.length];

    // this seems clunky.
    private String[] getAlgorithms() {
	// count the number of enabled ones
	int n = 0;
	for (int i=0; i<crossEnabled.length; i++)
	    if (crossEnabled[i])
		n++;

	// put them in a new array
	String r[] = new String[n];
	int nextSlot = 0;
	for (int i=0; i<crossEnabled.length; i++)
	    if (crossEnabled[i])
		r[nextSlot++] = Cross.ALL_CROSSDATES[i];
	return r;
    }

    // (this, too.)
    private void setAlgorithms(String a[]) {
	// set them all to false
	for (int i=0; i<crossEnabled.length; i++)
	    crossEnabled[i] = false;

	// for each one, set the corresponding crossEnabled[] to true
	for (int i=0; i<a.length; i++) {
	    for (int j=0; j<Cross.ALL_CROSSDATES.length; j++)
		if (a[i].equals(Cross.ALL_CROSSDATES[j]))
		    crossEnabled[j] = true;
	}
    }

    // init enabled[], using Cross.DEFAULT_CROSSDATES
    private void initEnabled() {
	setAlgorithms(Cross.DEFAULT_CROSSDATES); // ack,  this doesn't need to be its own method now
    }

    /**
       Make a new crossdating kit, with no samples, and the default
       algorithms checked.

       @see edu.cornell.dendro.corina.cross.Cross#DEFAULT_CROSSDATES
    */
    public CrossdateKit() {
	initEnabled();
        setup();
        pack();
        show();
    }

    /**
       Make a new crossdating kit, with the samples and algorithms
       from the given sequence.

       @param sequence a sequence containing the fixed and moving
       samples and algorithms to use
    */
    public CrossdateKit(Sequence sequence) {
	initEnabled();

	// this needs to run before makeCheckboxes(), which setup() calls
	setAlgorithms(sequence.getAlgorithms());

        setup();

        // use seq.getAllFixed(), seq.getAllMoving()
        // -- yup, (setq fixed-files (mapcar 'make-user-friendly-file (seq-get-all-fixed seq))), etc.
        // (i can't do this in-place, because i'm given the actual list, not a copy)
	// REFACTOR!
        List fixedFiles = new ArrayList(), fixedNames = sequence.getAllFixed();
        for (int i=0; i<fixedNames.size(); i++)
            fixedFiles.add(new UserFriendlyFile((String) fixedNames.get(i)));
        fixed.setFiles(fixedFiles);
        List movingFiles = new ArrayList(), movingNames = sequence.getAllMoving();
        for (int i=0; i<movingNames.size(); i++)
            movingFiles.add(new UserFriendlyFile((String) movingNames.get(i)));
        moving.setFiles(movingFiles);

        // run may or not be enabled here (a sequence might have an empty list),
	// so we'll have to figure it out.
	updateRun();

        pack();
        show();
    }
}
