package corina.cross;

import corina.Element;
import corina.util.JLine;
import corina.util.UserFriendlyFile;
import corina.util.OKCancel;
import corina.gui.ButtonLayout;
import corina.gui.FileDialog;
import corina.gui.UserCancelledException;

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
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

/*
 LEFT TO DO:
 -- layout of bottom buttons is ugly
 -- layout of labels/lists is ugly
 
 -- doesn't handle drags 100%
 -- fix drags: drag=move, mac+option / win32+control = copy
 -- fix drags: if dragging onto same list, re-order
 -- fix drags: don't appear to drag whole list (aqua problem only?)
 -- fix drags: allow dragging multiple items

 -- show icons in list? -- (need custom renderer)
 
 -- don't allow copy from empty-to-full, or (better) add undo, or make it much more obvious which way a copy goes, or something... (?)
*/

public class CrossdateKit extends JDialog {

    // a label, a (scrollable) list of samples, and add/remove buttons.
    // supports keys (delete, cmd-A, ...), and drag-n-drop (opt-drag=copy).
    private static class SampleList extends JPanel {
        JButton add, remove;
        JList list;
        DefaultListModel model;
        SampleList(String title) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            // label
            {
                JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                flow.add(new JLabel(title));
                add(flow);
            }
            
            // list
            model = new DefaultListModel();
            list = new JList(model);
            add(new JScrollPane(list));
            
            // add
            add = new JButton("Add...");
            add.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        List chosen = FileDialog.showMulti("Add");
                        for (int i=0; i<chosen.size(); i++)
                            model.addElement(new UserFriendlyFile(((Element) chosen.get(i)).getFilename()));
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
                    // i don't check .getValueIsAdjusting(), but i'm not entirely sure why not...
                    remove.setEnabled(!list.isSelectionEmpty());
                }
            });

            // buttons, on the bottom
            JPanel buttons = new JPanel(new ButtonLayout());
            buttons.add(add);
            buttons.add(remove);
            add(Box.createVerticalStrut(8));
            add(buttons);

            // delete => removeSelectedRows()
            list.addKeyListener(new KeyAdapter() {
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
                }
                public void dragOver(DropTargetDragEvent event) { } // ignore
                public void dragExit(DropTargetEvent event) { } // ignore
                public void dropActionChanged(DropTargetDragEvent event) { } // ignore
                public void drop(DropTargetDropEvent event) {
                    try {
                        Transferable transferable = event.getTransferable();

                        // we accept only filelists
                        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            event.acceptDrop(DnDConstants.ACTION_COPY);
                            Object o = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                            List l = (List) o; // a List of Files

                            // sort files here by name, or things just plain look weird
                            Collections.sort(l);

                            // add each one in turn
                            for (int i=0; i<l.size(); i++) {
                                String pathname = ((File) l.get(i)).getPath();
                                model.addElement(new UserFriendlyFile(pathname));
                            }

                            event.getDropTargetContext().dropComplete(true);
                        } else {
                            event.rejectDrop();
                        }
                    } catch (IOException ioe) {
                        event.rejectDrop(); // handle error?
                    } catch (UnsupportedFlavorException ufe) {
                        event.rejectDrop(); // handle error?
                    }
                }
            });
            
/*
 // handle drags
            drag = new DragSource();
            drag.createDefaultDragGestureRecognizer(list,
                                                    DnDConstants.ACTION_MOVE, // ?
                                                    new DragGestureListener() {
                                                        public void dragGestureRecognized(DragGestureEvent e) {
                                                            Transferable t = new Transferable() {
                                                                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                                                                    if (!flavor.equals(DataFlavor.javaFileListFlavor))
                                                                        throw new UnsupportedFlavorException(flavor);

                                                                    // drag should carry all selected files, as Files
                                                                    List files = new ArrayList();
                                                                    int selection[] = list.getSelectedIndices();
                                                                    Object o[] = model.toArray();
                                                                    for (int i=0; i<selection.length; i++)
                                                                        files.add(o[selection[i]]); // they're already (UserFriendly)Files
                                                                    return files;
                                                                }
                                                                public DataFlavor[] getTransferDataFlavors() {
                                                                    return new DataFlavor[] { DataFlavor.javaFileListFlavor };
                                                                }
                                                                public boolean isDataFlavorSupported(DataFlavor flavor) {
                                                                    return flavor.equals(DataFlavor.javaFileListFlavor);
                                                                }
                                                            };
                                                            drag.startDrag(e, DragSource.DefaultMoveDrop, t, new DragSourceListener() {
                                                                // do i want any of these?
                                                                public void dragDropEnd(DragSourceDropEvent event) { }
                                                                public void dragEnter(DragSourceDragEvent event) { }
                                                                public void dragExit(DragSourceEvent event) { }
                                                                public void dragOver(DragSourceDragEvent event) { }
                                                                public void dropActionChanged( DragSourceDragEvent event) { }
                                                            });
                                                        }
                                                    });
*/
        }
//        private DragSource drag;
        void removeSelectedRows() {
            int selection[] = list.getSelectedIndices();
            // do backwards, so earlier indices don't change under me
            for (int i=selection.length-1; i>=0; i--)
                model.removeElementAt(selection[i]);
            //            remove.setEnabled(false); // hmm... (???)
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
        // QUESTION: should this skip dupes?
        void addFiles(List files) {
            for (int i=0; i<files.size(); i++)
                model.addElement(files.get(i));
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
        setTitle("Crossdate");

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
                new CrossFrame(seq);
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
                // i don't check .getValueIsAdjusting(), but i'm not entirely sure why not...
                JList list = (JList) e.getSource();
                boolean selectionEmpty = list.isSelectionEmpty();
                copy1.setEnabled(!selectionEmpty);
                move1.setEnabled(!selectionEmpty);
            }
        });
        moving.getJList().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                // i don't check .getValueIsAdjusting(), but i'm not entirely sure why not...
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
            private void change() {
                boolean fixedEmpty = (fixed.getJList().getModel().getSize() == 0);
                boolean movingEmpty = (moving.getJList().getModel().getSize() == 0);
                run.setEnabled(!fixedEmpty && !movingEmpty);
            }
        };
        fixed.getJList().getModel().addListDataListener(listener);
        moving.getJList().getModel().addListDataListener(listener);
        run.setEnabled(false);

        JPanel leftRight = new JPanel();
        leftRight.setLayout(new BoxLayout(leftRight, BoxLayout.X_AXIS)); // was: GridLayout(1,2)
        leftRight.add(fixed);
        leftRight.add(Box.createHorizontalStrut(8));
        leftRight.add(center);
        leftRight.add(Box.createHorizontalStrut(8));
        leftRight.add(moving);

        JPanel buttons = new JPanel(new ButtonLayout());
        JButton help = new JButton("Help");
        help.setEnabled(false); // implement me ... later
        buttons.add(help);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(run);

        JPanel chunk = new JPanel();
        chunk.setLayout(new BoxLayout(chunk, BoxLayout.Y_AXIS));
        {
            JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            flow.add(new JLabel("Run a new sequences of crossdates with the following samples:"));
            chunk.add(flow);
            // add smaller detailed text here?
        }
        chunk.add(leftRight);
        chunk.add(Box.createVerticalStrut(14));
        chunk.add(new JLine());
        chunk.add(Box.createVerticalStrut(14));
	chunk.add(makeCheckboxes());
        chunk.add(Box.createVerticalStrut(14));
        chunk.add(new JLine());
        chunk.add(Box.createVerticalStrut(14));
        chunk.add(buttons);

        JPanel stuff = new JPanel();
        stuff.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        stuff.add(chunk);
        setContentPane(stuff);
        
        OKCancel.addKeyboardDefaults(this, run);

        setResizable(false);
    }

    private JPanel makeCheckboxes() {
	JPanel p2 = new JPanel(new GridLayout(1, 0));
	JCheckBox t = new JCheckBox("T-Score", tscore);
	JCheckBox tr = new JCheckBox("Trend", trend);
	JCheckBox d = new JCheckBox("D-Score", dscore);
	JCheckBox w = new JCheckBox("Weiserjahre", weiserjahre);
	p2.add(t);
	p2.add(tr);
	p2.add(d);
	p2.add(w);

	// don't really need these, do i?  just, instead of reading flags, read the state of the checkboxes.
	// well, actually, i might as well.  i need to set run based on the checkboxes, anyway.
	t.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    tscore = !tscore;
		    run.setEnabled(tscore || trend || dscore || weiserjahre); } } );
	tr.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    trend = !trend;
		    run.setEnabled(tscore || trend || dscore || weiserjahre); } } );
	d.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    dscore = !dscore;
		    run.setEnabled(tscore || trend || dscore || weiserjahre); } } );
	w.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    weiserjahre = !weiserjahre;
		    run.setEnabled(tscore || trend || dscore || weiserjahre); } } );

	JPanel pad = new JPanel(new FlowLayout(FlowLayout.LEFT));
	pad.add(new JLabel("Select algorithms to use:"));

	JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(pad);
	p.add(Box.createVerticalStrut(8));
	p.add(p2);

	return p;
    }

    // default is (tscore,trend,dscore)
    private boolean tscore=true, trend=true, dscore=true, weiserjahre=false;

    // this seems clunky.
    private String[] getAlgorithms() {
	int n = 0;
	if (tscore) n++;
	if (trend) n++;
	if (dscore) n++;
	if (weiserjahre) n++;
	String r[] = new String[n];
	int i=0;
	if (tscore) r[i++] = "corina.cross.TScore";
	if (trend) r[i++] = "corina.cross.Trend";
	if (dscore) r[i++] = "corina.cross.DScore";
	if (weiserjahre) r[i++] = "corina.cross.Weiserjahre";
	return r;
    }

    // (this, too.)
    private void setAlgorithms(String a[]) {
	tscore = trend = dscore = weiserjahre = false;
	for (int i=0; i<a.length; i++) {
	    if (a[i].equals("corina.cross.TScore"))
		tscore = true;
	    else if (a[i].equals("corina.cross.Trend"))
		trend = true;
	    else if (a[i].equals("corina.cross.DScore"))
		dscore = true;
	    else if (a[i].equals("corina.cross.Weiserjahre"))
		weiserjahre = true;
	}
    }

    public CrossdateKit() {
        setup();
        pack();
        show();
    }
    public CrossdateKit(Sequence seq) { // from an already-running sequence

	setAlgorithms(seq.getAlgorithms()); // needs to run before makeCheckboxes(), which setup() calls

        setup();

        // use seq.getAllFixed(), seq.getAllMoving()
        // -- yup, (setq fixed-files (mapcar 'make-user-friendly-file (seq-get-all-fixed seq))), etc.
        // (i can't do this in-place, because i'm given the actual list, not a copy)
        List fixedFiles = new ArrayList(), fixedNames = seq.getAllFixed();
        for (int i=0; i<fixedNames.size(); i++)
            fixedFiles.add(new UserFriendlyFile((String) fixedNames.get(i)));
        fixed.setFiles(fixedFiles);
        List movingFiles = new ArrayList(), movingNames = seq.getAllMoving();
        for (int i=0; i<movingNames.size(); i++)
            movingFiles.add(new UserFriendlyFile((String) movingNames.get(i)));
        moving.setFiles(movingFiles);

        // run is enabled by default here, because we know there's a sequence
        run.setEnabled(true);

        pack();
        show();
    }

    public static void main(String args[]) {
        new CrossdateKit();
    }
}
