package corina.browser;

import corina.util.NaturalSort;
import corina.prefs.Prefs;

import java.io.File;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.tree.*;

import java.awt.event.*;
import javax.swing.event.*;

/*
  ok, it's working.  i want to integrate this into browser.  what to do?

  layout:
  -- move search field to either directly above it, or directly below it (?)
  -- (delete folder popup class)

  events:
  -- (wait ~0.5sec before updating right pane, in case user scrolls again?)

  persistence:
  -- on c'ton, select node corina.browser.folder
  -- on selection, store selected folder in corina.browser.folder
  -- store/restore which nodes are expanded (how?)
  -- store size/position of window, position of split, position of scrollbar

  misc:
  -- allow selecting exactly one item
*/
public class FolderTree extends JPanel {
    // a node of children=files, given a folder.
    // REFACTOR: this is (almost) exactly what a data source should look like...
    private static class FolderNode extends DefaultMutableTreeNode {
	private String folder;
	private String name;
	FolderNode(String folder) {
	    this.folder = folder;
	    this.name = new File(folder).getName();
	}
	private boolean childrenDefined = false;
	public int getChildCount() {
	    if (!childrenDefined) {
		defineChildNodes();
	    }
	    return super.getChildCount();
	}
	public boolean isLeaf() {
	    return false;
	}
	private void defineChildNodes() {
	    childrenDefined = true;
	    File files[] = new File(folder).listFiles();

	    // files can be null here?
	    if (files == null)
		return;

	    // list of folders, as strings -- we'll sort this
	    List buf = new ArrayList();
	    for (int i=0; i<files.length; i++)
		if (files[i].isDirectory() && !files[i].isHidden())
		    buf.add(files[i].getPath());
	    Collections.sort(buf, new NaturalSort.CINaturalComparator());

	    // sort by path ok?  it assumes the path is the same -- i don't know that i can guarantee this.
	    // FIXME: add File objects to buf, sort with a (CI) comparator that checks getName(), then add as buf.get(i).getPath()

	    // add each element of buffer list to this node
	    for (int i=0; i<buf.size(); i++)
		add(new FolderNode((String) buf.get(i)));
	}
	public String toString() {
	    return name;
	}
	public String getFolder() {
	    return folder;
	}
    }

    // WRITEME: a SourceNode, which is a DefaultMutableTreeNode, but
    // wraps a corina.sources Source.

    // WRITEME: a RootNode, which has SourceNodes as children, but
    // isn't itself actually visible.

    private JTree tree;

    private Browser browser;

    public FolderTree(Browser b) {
	this.browser = b;

	tree = new JTree(new FolderNode(Prefs.getPref("corina.dir.data")));

	// select row of tree which corresponds to |corina.browser.folder|
	{
	    // first: crop off corina.dir.data
	    String rootFolder = Prefs.getPref("corina.browser.folder");
	    if (rootFolder == null)
		rootFolder = ".";
	    File root = new File(rootFolder);
	    String browserFolder = Prefs.getPref("corina.browser.folder");
	    if (browserFolder == null)
		browserFolder = rootFolder;
	    File target = new File(browserFolder);

	    while (target!=null && !target.equals(root))
		target = target.getParentFile();

	    // not a child of corina.dir.data?  oh well, just select that, then.
	    if (target == null) {
		tree.setSelectionInterval(0, 0);
		return;
	    }

	    // WRITEME
	    // TRICKY: might need to expand nodes!
	}

	// TODO: if this fails, user.dir? (=cwd)
	tree.addTreeSelectionListener(new TreeSelectionListener() {
		public void valueChanged(TreeSelectionEvent e) {
		    FolderNode n = (FolderNode) e.getPath().getLastPathComponent();
		    String folder = n.getFolder();
		    browser.setFolder(folder);
		    browser.doList(); // careful: this call is SLOW!
		    // (why does it have to be slow?  why can't it start its
		    // own thread, or something, and guarantee a quick return?)
		}
	    });
	setLayout(new BorderLayout());
	add(new JScrollPane(tree), BorderLayout.CENTER);

	tree.addKeyListener(new KeyListener() {
		public void keyPressed(KeyEvent e) {
		    // ignore
		}
		public void keyReleased(KeyEvent e) {
		    // ignore
		}
		public void keyTyped(KeyEvent e) {
		    char c = e.getKeyChar();
		    long t = System.currentTimeMillis();

		    // too long?  start over
		    if (t > lastKeypress + TYPE_SPEED)
			buf = "";

		    // record time
		    lastKeypress = t;

		    // append char
		    buf += c;

		    // WRITEME: jump to |buf| now
		    // how?  start where you are, and go forward until you find something that
		    // starts with this, or you return to the same row again. (loop at end.)

		    // jump to |buf| now
		    int n = tree.getRowCount(); // loop after this many
		    int start = tree.getMinSelectionRow(); // ???
		    int row = start;
		    for (;;) {
			FolderNode node = (FolderNode) tree.getPathForRow(row).getLastPathComponent();
			// (toString() == getName())
			if (node.toString().startsWith(buf)) { // FIXME: case-insensitive
			    tree.scrollRowToVisible(row);
			    tree.setSelectionRow(row);
			    break;
			}

			// next row
			row++;
			row %= n;

			// done?
			if (row == start)
			    break;
		    }
		}
	    });
    }

    private static final int TYPE_SPEED = 1000; // after this many ms, type-buffer is cleared
    private long lastKeypress=-1; // time of last keypress
    private String buf="";
}
