package corina.browser;

import corina.Previewable;
import corina.Sample;
import corina.Element;
import corina.cross.Grid;
import corina.files.WrongFiletypeException;
import corina.editor.Editor;
import corina.gui.ElementsPanel;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.event.*;
import javax.swing.tree.*;

/*
  TODO:
  -- use new preview model; wait, no preview model at all?
  -- don't say "Unreadable file" -- dim files, but that requires extensions -- anything else...
  -- show only dirs on left, add elementspanel on right
  -- add summary label ("19 samples, 24 KB")
  -- add search box
  -- add realtime searching/limiting capabilities
  -- multiple top-level directories, user-specifyable
  -- virtual fs (ftp, etc.)
  -- drag-n-drop
  -- "wait..." while opening
*/

public class OldBrowser extends JFrame implements TreeExpansionListener,
					       TreeSelectionListener,
					       MouseListener,
					       KeyListener,
Comparator,
                                               FileFilter {

    JSplitPane splitPane;

    // filefilter -- ignore dotfiles
    public boolean accept(File pathname) {
	return !pathname.getName().startsWith(".");
    }

    // sort files by name, ignoring case
    public int compare(Object o1, Object o2) {
        String f1 = ((File) o1).getPath();
        String f2 = ((File) o2).getPath();
        return f1.compareToIgnoreCase(f2);
    }

    // treeexpansionlistener
    public void treeCollapsed(TreeExpansionEvent e) {
	// delete all children of this node
	DefaultMutableTreeNode parent = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

	// get count, and store a list -- i can't remove things in the
	// middle of an enumeration (arg!)
	int n = parent.getChildCount();
	DefaultMutableTreeNode children[] = new DefaultMutableTreeNode[n];
	int count=0;
	Enumeration enum = parent.children();
	while (enum.hasMoreElements())
	    children[count++] = (DefaultMutableTreeNode) enum.nextElement();

	// now i can remove them
	for (int i=0; i<n; i++)
	    ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(children[i]);
    }
    public void treeExpanded(TreeExpansionEvent e) {
	Object p[] = e.getPath().getPath();

	String path = parent;
	for (int i=0; i<p.length; i++)
	    path += File.separatorChar + p[i].toString();

	File newDir = new File(path);
	File children[] = newDir.listFiles(this);

        // sort by name
        Arrays.sort(children, this);

	DefaultMutableTreeNode parent = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

	// go backwards, so i'm always inserting at 0 (wimp)
	// for (int i=children.length-1; i>=0; i--) {
	for (int i=0; i<children.length; i++) {
	    final boolean isDir = children[i].isDirectory();
	    DefaultMutableTreeNode t = new DefaultMutableTreeNode(children[i].getName()) {
		    public boolean isLeaf() {
			return !isDir;
		    }
		};

	    // ((DefaultTreeModel) tree.getModel()).insertNodeInto(t, parent, 0);
	    ((DefaultTreeModel) tree.getModel()).insertNodeInto(t, parent, i);
	}

	// -- child is "please wait..."
	// -- read directory (fsv?)
	// -- children are all files
    }

    // treeselectionlistener
    public void valueChanged(TreeSelectionEvent e) {
	String filename = getSelectedFilename();

	// put directory/filename in title?
	if (filename == null)
	    setTitle("Corina");
	else {
	    Object p[] = tree.getSelectionPath().getPath();
	    String path = p[0].toString();
	    for (int i=1; i<p.length; i++)
		path += File.separatorChar + p[i].toString();
	    setTitle("Corina: " + path);
	}

	// nothing selected?
	if (filename == null) {
	    // what to do?
	    return;
	}

	// not a dir?
	File file = new File(filename);
	if (!file.isDirectory()) {
	    // what to do?
	    return;
	}

        // set preview: it's an elementspanel
        List el = new ArrayList();
        File[] children = file.listFiles(); // fixme: no dotfiles!
        for (int i=0; i<children.length; i++) {
            if (children[i].isDirectory())
                continue;
            if (children[i].getName().startsWith("."))
                continue;
            el.add(new Element(children[i].getPath(), false)); // false?
        }
        ElementsPanel ep = new ElementsPanel(el);
        ep.setView(ElementsPanel.VIEW_STANDARD);
        splitPane.setRightComponent(ep);
    }

    // mouselistener
    public void mouseClicked(MouseEvent e) {
	if (e.getClickCount() == 2) {
	    openSelectedFile();
	} else if (e.isPopupTrigger()) {
	    System.out.println("context..."); // doesn't work -- why not?
	}
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }

    // keylistener
    private String typed="";
    private long lastKey=-1;
    private static int DT = 1000;
    public void keyPressed(KeyEvent e) {
	// -- ENTER / control-O => open
	if (e.getKeyCode() == KeyEvent.VK_ENTER ||
	    (e.getKeyCode()==KeyEvent.VK_O && e.isControlDown())) {
	    openSelectedFile();
	    typed = "";
	    lastKey = -1;
	}

	// -- ESCAPE?  kill.
	if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	    typed = "";
	    lastKey = -1;
	}

	// -- letters => jump to file/folder
	char c = e.getKeyChar();
	if (Character.isLetter(c) || Character.isDigit(c) || c=='.' || c==' ') {
	    // time's up!
	    if (lastKey + DT < System.currentTimeMillis())
		typed = "";

	    // record this time
	    lastKey = System.currentTimeMillis();

	    // append typed
	    typed += Character.toUpperCase(c);

	    // select node
	    int r = tree.getMaxSelectionRow(); // selected row
	    if (r == -1)
		r = 0;

	    // this row still good?  stay here.
	    {
		TreePath p = tree.getPathForRow(r);
		if (p.getLastPathComponent().toString().toUpperCase().indexOf(typed) == 0)
		    return;
	    }

	    // ok, let's go hunting for a good row.
	    int startRow = r; // if we get back here, we've failed
	    for (;;) {
		TreePath p = tree.getPathForRow(r);
		if (p.getLastPathComponent().toString().toUpperCase().indexOf(typed) == 0) {
		    // select it, and scroll to it
		    tree.setSelectionPath(p);
		    tree.scrollRowToVisible(tree.getMaxSelectionRow());
		    return;
		}

		// next one
		r++;
		r %= tree.getRowCount();

		if (r == startRow)
		    return;
	    }
	}
    }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }

    private String getSelectedFilename() {
        try {
            Object p[] = tree.getSelectionPath().getPath();
            String path = parent;
            for (int i=0; i<p.length; i++)
                path += File.separatorChar + p[i].toString();
            return path;
        } catch (NullPointerException npe) {
            // deselected something, so give 'em the toplevel one.
            //            return _fn;
            return null;
        }
    }
    private void openSelectedFile() {
        try {
            new Editor(new Sample(getSelectedFilename()));
        } catch (IOException ioe) {
            // tell user what's wrong
        }
    }

    private String _fn;

    private JTree tree;
    private JPanel preview;

    private String parent;

    public OldBrowser(String fn) {
	setTitle("Corina");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	_fn = fn;

	File dir = new File(fn);
	final File files[] = dir.listFiles(this);

	parent = dir.getParent();

        // sort by name
        Arrays.sort(files, this);

	// tree
	DefaultMutableTreeNode top = new DefaultMutableTreeNode(dir.getName());
	for (int i=0; i<files.length; i++) {
	    final boolean isDir = files[i].isDirectory();
	    DefaultMutableTreeNode t = new DefaultMutableTreeNode(files[i].getName()) {
		    public boolean isLeaf() {
			return !isDir;
		    }
		};
	    top.add(t);
	}
	tree = new JTree(top);
	tree.addTreeExpansionListener(this);
	tree.addTreeSelectionListener(this);
	tree.addMouseListener(this);
	tree.addKeyListener(this);
	tree.putClientProperty("JTree.lineStyle", "Angled"); // is there a better way to do this now?

	// preview
	preview = new JPanel();

        // tabs: (local) data, and itrdb
        JTabbedPane tabs = new JTabbedPane();
        tabs.add(new JScrollPane(tree), "Data");
        tabs.add(new javax.swing.JButton("oink"), "ITRDB");

        // split pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setLeftComponent(tabs);
        splitPane.setRightComponent(preview);

        getContentPane().add(splitPane, BorderLayout.CENTER);
        pack();
        setSize(new Dimension(320, 480));
        show();
        tree.requestFocus(); // does this do anything?
    }

    public static void main(String args[]) {
	new OldBrowser(args.length>=1 ? args[0] : System.getProperty("user.home"));
    }

    /*
      do immediately:
      -- combine checkbox and filename/title (whichever is first?  doesn't much matter, title will be gone soon)
      -- remove add/remove buttons from elementspanel
      -- clean up bargraphs
      -- clean up (normal) graphing code -- is StandardPlot really needed?
    */
}
