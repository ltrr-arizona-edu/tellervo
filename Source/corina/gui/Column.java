package corina.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Icon;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.util.*;
import java.io.*;

/*
  a single column-browser column:

  [ ==== Folder ==== ]
  | * ABC        > | |
  | * BEF        > | |
  | * CCC        > | |
  | * DDD        > | |
  --------------------

  notes:
  -- folder icon; open-folder icon, when selected.
  -- use a jtable, so i can show the "Folder" header -- is a jlabel good enough?
  -- vertical scrollbar always visible; horizontal never.
  -- each folder is a drag-source.
  -- each folder is a drop-target (?).
  -- a right-arrow in each row, to mean "can descend here". (?)
  -- given: a folder.
  -- show a right arrow to indicate you can descend into it.
  -- type-to-select.

  -- if it gets resized to below threshold, it changes to a popup:

  [ * ABC          > ]
*/

public class Column extends JPanel {

    // icons for "open folder" and "closed folder".  (is there a better way?)
    private static Icon openIcon, closedIcon;
    static {
	DefaultTreeCellRenderer dtcr = new DefaultTreeCellRenderer();
	openIcon = dtcr.getOpenIcon();
	closedIcon = dtcr.getClosedIcon();
    }

    private class MyModel implements ComboBoxModel {
	String children[];
	public MyModel(String folder) {
	    File files[] = new File(folder).listFiles();

	    java.util.List x = new java.util.ArrayList();
	    for (int i=0; i<files.length; i++)
		if (files[i].isDirectory() && !files[i].isHidden())
		    x.add(files[i].getName());

	    Collections.sort(x, new Comparator() {
		    public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;

			return s1.compareToIgnoreCase(s2);
		    }
		});

	    // need to keep track of original file names?

	    children = new String[x.size()];
	    for (int i=0; i<x.size(); i++)
		children[i] = (String) x.get(i);
	}

	// needed by ComboBoxModel and ListModel
	public void addListDataListener(ListDataListener l) {
	    // WRITEME
	}
	public Object getElementAt(int index) {
	    return children[index];
	    // WRITEME
	}
	public int getSize() {
	    return children.length;
	    // WRITEME
	}
	public void removeListDataListener(ListDataListener l) {
	    // WRITEME
	}

	// needed by ComboBoxModel only
	private Object selection=null;
	public Object getSelectedItem() {
	    return selection;
	    // WRITEME
	}
	public void setSelectedItem(Object anItem) {
	    selection = anItem;
	    // WRITEME
	}
    }

    private class MyRenderer extends JLabel implements ListCellRenderer {
	public MyRenderer() {
	    this.setOpaque(true);
	}
	public Component getListCellRendererComponent(JList list,
						      Object value,
						      int index,
						      boolean isSelected,
						      boolean cellHasFocus)
	{
	    setText(value.toString());
	    this.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
	    this.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
	    setIcon(isSelected ? openIcon : closedIcon);
	    return this;
	}
    }
 

    private ComboBoxModel model;
    private ListCellRenderer renderer;
    private boolean useLists=false;

    private static final int THRESHOLD = 50;

    private String folder;

    // only one of these is in use at a time.
    private JList list = null;
    private JComboBox combo = null;

    public Column(String folder) {
	this.folder = folder;

	setLayout(new BorderLayout());
	setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

	model = new MyModel(folder);
	renderer = new MyRenderer();
	list = new JList(model);
	list.setSelectedIndex(0);
	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	list.setCellRenderer(renderer);
	list.addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
		    if (!e.getValueIsAdjusting())
			fireFolderChangedEvent((String) list.getSelectedValue());
		}
	    });
	add(new JScrollPane(list,
			    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

	addComponentListener(new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
		    if (useLists && e.getComponent().getHeight()<THRESHOLD) {
			useLists = false;
			if (list == null) // first time
			    return;
			int x = list.getSelectedIndex();
			remove(0);
			list = null;
			combo = new JComboBox(model);
			combo.setSelectedIndex(x);
			combo.setRenderer(renderer);
			combo.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
				    fireFolderChangedEvent((String) combo.getSelectedItem());
				}
			    });
			add(combo, BorderLayout.NORTH);
		    } else if (!useLists && e.getComponent().getHeight()>THRESHOLD) {
			useLists = true;
			if (combo == null) // first time
			    return;
			int x = combo.getSelectedIndex();
			remove(0);
			combo = null;
			list = new JList(model);
			list.setSelectedIndex(x);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setCellRenderer(renderer);
			list.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
				    if (!e.getValueIsAdjusting())
					fireFolderChangedEvent((String) list.getSelectedValue());
				}
			    });
			add(new JScrollPane(list,
					    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		    } else {
			return;
		    }
		    repaint();
		}
	    });
    }

    public static void main(String args[]) throws Exception {
	// System.setProperty("com.apple.mrj.application.live-resize", "true");

	JFrame f = new JFrame();

	/*
	JLabel lab = new JLabel("Folder");
	lab.setFont(lab.getFont().deriveFont(10f)); // hardcoded size!

	f.getContentPane().setLayout(new BorderLayout());
	f.getContentPane().add(lab, BorderLayout.NORTH);
	f.getContentPane().add(new Column(), BorderLayout.CENTER);
	*/

	JSplitPane s = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);

	final JPanel p = new JPanel(new GridLayout(1, 0));
	p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

	Column c1 = new Column("/Users/");
	c1.addFolderListener(new FolderSelectionListener() {
		public void folderSelected(String f) {
		    p.remove(1);
		    p.remove(2);
		    p.add(new JLabel("1"));
		    p.add(new JLabel("2"));
		    System.out.println("folder " + f + " selected");
		}
	    });

	Column c2 = new Column("/Users/kharris/");
	c2.addFolderListener(new FolderSelectionListener() {
		public void folderSelected(String f) {
		    System.out.println("folder " + f + " selected");
		}
	    });

	Column c3 = new Column("/Users/kharris/Documents/");
	c3.addFolderListener(new FolderSelectionListener() {
		public void folderSelected(String f) {
		    System.out.println("folder " + f + " selected");
		}
	    });

	p.add(c1);
	p.add(c2);
	p.add(c3);

	s.setTopComponent(p);
	s.setBottomComponent(new JLabel("more cool stuff down here"));

	f.getContentPane().add(s, BorderLayout.CENTER);

	f.pack();
	f.setSize(500, 500);
	f.show();
    }

    // event model.  yeuch.
    public static interface FolderSelectionListener {
	public void folderSelected(String newFolder);
    }
    private java.util.Vector listeners = new Vector();
    public void addFolderListener(FolderSelectionListener l) {
	if (!listeners.contains(l))
	    listeners.add(l);
    }
    public void removeFolderListener(FolderSelectionListener l) {
	listeners.add(l);
    }
    private void fireFolderChangedEvent(String newFolder) {
	// alert all listeners
	Vector l;
	synchronized (this) {
	    l = (Vector) listeners.clone();
	}

	int size = l.size();
	if (size == 0)
	    return;
	// prepend absolute path to newFolder?
	for (int i=0; i<size; i++) {
	    FolderSelectionListener listener = (FolderSelectionListener) l.elementAt(i);
	    listener.folderSelected(newFolder);
	}
    }

    /*
      TODO:
      -- list: disallow empty selection
      -- keyb: type to select something
      -- keyb: right-arrow to descend into it (focus next one, scroll)
      -- add "folder" label above each one (10pt?)

      -- make each line a drag-source
      -- make each column(?) a drop-target

      -- add tooltips?
      -- renderer: add non-leaf (arrow) icon?
      -- focus bug: popup selected, resized, press key -- focus is still on popup, not list
      -- race condition: if you shrink it too quickly, it disappears (?)
      -- on resize, make sure list selection remains visible?
      -- if on 1.4, use new methods to get actual system icons for each folder
    */
}
