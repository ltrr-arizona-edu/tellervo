package corina.gui;

import corina.util.NoEmptySelection;
import corina.util.OKCancel;
import corina.util.UpdateFolder;
import corina.util.PopupListener;
import corina.db.DBBrowser;
import corina.db.Convert;
import corina.ui.Builder;
import corina.ui.Alert;
import corina.gui.Layout;
import corina.gui.layouts.DialogLayout;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.*;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.awt.image.BufferedImage;

import java.sql.*;

// purpose: show a list of all the available sources, to be used with
// the corina sample browser.

// possible sources include:
// -- local folders
// -- remote (ftp) folders
// -- smart lists

// REFACTOR: move this to corina.browser.*
// WRITEME: a SourceType interface, which has:
// -- icon
// -- name
// -- info dialog?
// -- "do this on drag"
// -- "do this on drop"
// -- (see what else i need as i go)
// -- a complete filesystem-like interface:
// ---- list samples (in some context)
// ---- load a sample
// ---- save a sample
// ---- find samples that match... (yay JDBC!)

/*
  drag-n-drop:
  -- dragging a folder source is a "copy folder" operation, e.g., drag it to a disk
  -- dragging a different source (e.g., an rdbms) copies files
  -- "copy" always really means "update" -- use the oscarizer algorithm to make it run fast
  -- dropping on a folder source copies files to that folder
  -- dropping on a database source copies samples to that database
  -- drag-n-drop between file sources will run fastest if it just copies files (no sample loading)
  -- drag-n-drop between databases -- is there a fast way to copy across SQL?
  -- what about dropping on the blank area below the sources?  can that create a new source?
*/

public class SourcesTable extends JTable {
    public static interface Source {
	Icon getIcon();
	String getName(); // name of ... what?  doesn't user get to name it?
	void setName(String name);
	String getTip();

	boolean hasBrowser();
	Component makeBrowser();

	// do whatever you want for "show info" on this source
	// BETTER: return component with "okClicked" method
	// then i can show it in a dialog,
	// and also show it in a dialog for "new component", and if cancel, do nothing
	void showInfo();

	// what to drag
	Transferable getDragTransferable();

	// OTHER METHODS:
	// -- Element[] getSamples() (?) -- but not for Library/ITRDB
	// -- get info for top?
	// ---- getFolders()?  getSubfolders()?
	// -- getSample(Element)?  no, ELement.load() does that -- need Element.load() for DB Element, etc., then!
	// ---- (have element.filename="jdbc:???/dendro/sid=???", then?  that's fairly simple.)
	// -- getSamples(Search)?
    }
    private static class DatabaseSource implements Source {
	private String name;
	private String uri;
	private String username, password;
	public DatabaseSource(String name, String uri, String username, String password) {
	    this.name = name;
	    this.uri = uri;
	    this.username = username;
	    this.password = password;
	}
	public Icon getIcon() {
	    return Builder.getIcon("Database-32.png"); // See also Relations.*
	    // IDEAS for database icon: strong, robust, fast, powerful ... relations?
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getTip() {
	    return uri;
	}
	public boolean hasBrowser() {
	    return true;
	}
	private Connection connect() throws Exception { // WAS: SQLException
	    // open a connection
	    Class.forName("org.postgresql.Driver"); // DESIGN: get the JDBC driver from the URI?
	    Class.forName("org.hsqldb.jdbcDriver"); // DESIGN: get the JDBC driver from the URI?
	    Connection con;
	    try {
		// PERF: to connect to a local HSQLDB database takes around 9 sec (100 samples, 900KB)
		// (if it grows linearly and these are representative samples, that's a 400MB database!)
		con = DriverManager.getConnection(uri, username, password);
		return con;
	    } catch (Exception se) { // WAS: SQLException se
		// DESIGN: if ClassNotFound, error message should say that the driver couldn't be loaded.
		// (but thiss will do for now)

		// eep, can't connect!  let's not lose our heads, though...

		// get the first line of the error message
		String message = se.getMessage();
		if (message.indexOf('\n') != -1)
		    message = message.substring(0, message.indexOf('\n'));

		// (why extract just the first line?  because if it's really a network
		// exception, getConnection() has to wrap it in an SQL exception, and
		// it includes the entire stack trace in its message, and users won't
		// care about that.)

		// prepend "can't connect..." string
		//		    message = "Corina can't connect to the database.\n" +
		//			"The first line of the error message is:\n" +
		//			message;

		JLabel message3 = new JLabel("Corina can't connect to the database.");
		message3.setFont(message3.getFont().deriveFont(Font.BOLD));
		Object message2 = new Object[] {
		    message3,
		    Box.createVerticalStrut(6),
		    "The first line of the error message is:",
		    message,
		};

		// show it as an error message
		Alert.error("Can't Connect to Database", message2.toString());

		// FIXME: this dialog is resizable!

		// examples of exceptions i've gotten:
		// -- "FATAL 1:  No pg_hba.conf entry for host 128.253.105.30, user kharris, database dendro" (which means i didn't have permissions)
		// -- "The connection attempt failed because Exception: java.net.UnknownHostException: picea.arts.cornell.edu" (followed by a stack trace) (which means i wasn't connected to the network)

		// well, gotta return some sort of component, still -- throw it again.
		throw se;
	    }
	}
	public Component makeBrowser() {
	    try {
		try {
		    Connection con = connect();

		    // create a db browser
		    return new DBBrowser(con);
		} catch (Exception se) {
		    return new JLabel("couldn't connect"); // what to return?
		}
	    } catch (RuntimeException re) {
		// REMOVE ME later
		System.out.println("ex -- " + re);
		re.printStackTrace();
		return null;
	    }
	}
	public void showInfo() {
	    System.out.println("WRITEME: show info for database source");
	}
	public Transferable getDragTransferable() {
	    return null; // WRITEME
	}
	public void drop(File f) { // if you get something dropped
	    System.out.println("database: adding " + f.getPath() + " to " + uri);

	    try {
		// open connection
		Class.forName("org.postgresql.Driver");
		Class.forName("org.hsqldb.jdbcDriver");
		Connection connection = DriverManager.getConnection(uri, username, password);;

		// dump into database
		Convert.dump(f, connection);

		// close connection
		connection.close();
	    } catch (Exception e) {
		Bug.bug(e);
	    }
	}
    }
    /* was: private */ static class FolderSource implements Source {
	private String name;
	private String folder;
	public FolderSource(String name, String folder) {
	    this.name = name;
	    this.folder = folder;
	}
	public Icon getIcon() {
	    return Builder.getIcon("Library-32.png");
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getTip() {
	    return folder;
	}
	public boolean hasBrowser() {
	    return true;
	}
	public Component makeBrowser() {
	    ColumnBrowser cb = new ColumnBrowser(folder);
	    // JScrollPane sp = new JScrollPane(cb);
	    // sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    // sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	    // sp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	    // return sp;
	    return cb;
	}
	public void showInfo() {
	    // REFACTOR: move the dialog up, since all sources will have it?
	    final JDialog d = new JDialog();
	    d.setTitle("Source Information");

	    JPanel p = new JPanel(new BorderLayout());
	    d.setContentPane(p);

	    // DESIGN: "get info" should always show the icon, with the name.
	    // DESIGN: allow users to use their own icons, by selecting the icon in get-info
	    // and pasting an image (sounds hard ... ?)

	    // REFACTOR: make this a util class?
	    final JTextField folderField = new JTextField(folder, 20);
	    JButton change = new JButton("Change...");
	    change.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser(System.getProperty("corina.dir.data"));
			fc.setDialogTitle("Select Source Folder");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int x = fc.showDialog(null /* ??? */, "Select");
			if (x == JFileChooser.APPROVE_OPTION)
			    folderField.setText(fc.getSelectedFile().getPath());
			// ROBUSTNESS: what about ERROR_OPTION?
		    }
		});
	    // (make text field uneditable?  on mac only?)

	    JPanel folderWidget = Layout.flowLayoutL(folderField,
						      Box.createHorizontalStrut(4),
						      change);
	    folderWidget.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // doesn't work?...hmm...

	    final JTextField nameField = new JTextField(name, 40);

	    JPanel center = new JPanel(new DialogLayout());
	    center.add(nameField, "Name:");
	    center.add(folderWidget, "Folder:");
	    p.add(center, BorderLayout.CENTER);

	    JButton cancel = Builder.makeButton("cancel");
	    cancel.addActionListener(new AbstractAction() { // REFACTOR: why doesn't okcancel do this for me?
		    public void actionPerformed(ActionEvent e){
			d.dispose();
		    }
		});
	    JButton ok = Builder.makeButton("ok");
	    ok.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e){
			name = nameField.getText();
			folder = folderField.getText();
			// TODO: store in prefs, and save, now?
			d.dispose();
		    }
		});

	    JPanel bottom = Layout.buttonLayout(cancel, ok);
	    bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    p.add(bottom, BorderLayout.SOUTH);

	    OKCancel.addKeyboardDefaults(ok);

	    d.pack();
	    d.setResizable(false);

	    // select "library" name by default -- DOESN'T WORK! (why not?)
	    nameField.selectAll();
	    nameField.requestFocus();

	    d.show();
	}
	public Transferable getDragTransferable() {
	    return new Tree.TransferableFile(folder);
	}
	public void drop(File f) { // if you get something dropped
	    // BUG: what if the fs is case-insensitive, and these don't equal?
	    if (f.getPath().equals(folder)) {
		System.out.println("SAME FOLDER: doing nothing");
		return;
	    }

	    System.out.println("folder: updating " + folder + " with " + f.getPath() + "...");
	    UpdateFolder u = new UpdateFolder(f, new File(folder));
	    u.run();
	    System.out.println("...done!");
	    // TODO: run in thread; show progress / estimated time remaining?
	}
    }
    private static class NetworkSource implements Source {
	// IDEA: if user/pass given, keep summary file in same folder, else store locally.
	private String name;
	private String url;
	public NetworkSource(String name, String url) {
	    this.name = name;
	    this.url = url;
	}
	public Icon getIcon() {
	    return Builder.getIcon("ITRDB-32.png");
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getTip() {
	    return url;
	}
	public boolean hasBrowser() {
	    return true;
	}
	public Component makeBrowser() {
	    return null; // WRITEME!
	}
	public void showInfo() {
	    System.out.println("WRITEME: show info for network source");
	}
	public Transferable getDragTransferable() {
	    return null; // WRITEME
	}
    }
    private static class FavoritesSource implements Source {
	private String name;
	public FavoritesSource(String name) {
	    this.name = name;
	}
	public Icon getIcon() {
	    return Builder.getIcon("List.png");
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getTip() {
	    return null;
	}
	public boolean hasBrowser() {
	    return false;
	}
	public Component makeBrowser() {
	    throw new IllegalArgumentException(); // good exception?
	}
	public void showInfo() {
	    System.out.println("WRITEME: show info for favorites source");
	}
	public Transferable getDragTransferable() {
	    return null; // WRITEME
	}
    }
    private static class SmartListSource implements Source {
	private String name;
	public SmartListSource(String name) {
	    this.name = name;
	}
	public Icon getIcon() {
	    return Builder.getIcon("SmartList.png");
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getTip() {
	    return "Length>100, Sapwood=Yes, and Reconciled=Yes";
	}
	public boolean hasBrowser() {
	    return false;
	}
	public Component makeBrowser() {
	    throw new IllegalArgumentException(); // good exception?
	}
	public void showInfo() {
	    System.out.println("WRITEME: show info for smartlist source");
	}
	public Transferable getDragTransferable() {
	    return null; // WRITEME
	}
    }

    // this should be user-modifyable
    private List sources = new ArrayList();
    {
	sources.add(new FolderSource("Library (lab)", "/Users/kharris/Documents/Corina/corina/Demo Data"));
	sources.add(new FolderSource("Library (laptop)", "/Users/kharris/Documents/Corina/corina/Demo Data Copy"));
	sources.add(new FolderSource("Library (data)", "/Users/kharris/Documents/Corina/corina/DATA"));
	sources.add(new DatabaseSource("Database (server)", "jdbc:postgresql://picea.arts.cornell.edu/dendro",
				       "kharris", "merhaba"));
	sources.add(new DatabaseSource("Database (local)", "jdbc:hsqldb:/Users/kharris/dendro",
				       "sa", ""));
	/*
	sources.add(new NetworkSource("ITRDB", "ftp://ftp.ngdc.noaa.gov/paleo/treering/"));
	sources.add(new FavoritesSource("Favorites"));
	sources.add(new SmartListSource("Modified today"));
	*/
    };

    // remove the selected source.
    public void remove() {
	int row = getSelectedRow();
	sources.remove(getSource());
	((AbstractTableModel) getModel()).fireTableRowsDeleted(row, row);
	// BUG: what if it's too high?
	// BUG: what if there are no sources left?
	setRowSelectionInterval(row, row);
    }

    // add a new source
    public void add(Source s) {
	sources.add(s);

	int row = sources.size() - 1;
	setRowSelectionInterval(row, row);
	((AbstractTableModel) getModel()).fireTableRowsInserted(row, row);
    }

    // NEED: a source factory, which can take a bunch of strings in a prefs file
    // and generate the list of sources.  (later!)

    private class SourcesTableModel extends AbstractTableModel {
	public int getRowCount() {
	    return sources.size();
	}
	public int getColumnCount() {
	    return 1;
	}
	public Object getValueAt(int row, int column) {
	    return (Source) sources.get(row);
	}
	public void setValueAt(Object value, int row, int col) {
	    Source s = (Source) sources.get(row);
	    s.setName((String) value);
	}
	public String getColumnName(int column) {
	    return "Source";
	}
    }

    // return the currently-selected row.
    public Source getSource() {
	int r = getSelectedRow();
	if (r == -1 || r >= sources.size())
	    return null;
	else
	    return (Source) sources.get(r);
    }

    private DragSource drag;
    private DropTarget drop;
    private int hilited=-1;

    public SourcesTable() {
	super();
	setModel(new SourcesTableModel());
	setShowGrid(false);
	for (int i=0; i<sources.size(); i++) { // is this kosher?
	    setRowHeight(i, ((Source) sources.get(i)).getIcon().getIconHeight() + 4);
	}
	setMinimumSize(new Dimension(100, 0)); // 100px?
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // allow at most 1
	NoEmptySelection.noEmptySelection(this); // allow exactly 1
	setRowSelectionInterval(0, 0); // select first entry (BETTER: store as pref?)
	getTableHeader().setReorderingAllowed(false); // don't allow reordering
	getColumn(getColumnName(0)).setResizable(false); // don't allow resizing

	// display as [icon name], with tooltip.
	setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
		public Component getTableCellRendererComponent(JTable table, Object value,
							       boolean isSelected, boolean hasFocus,
							       int row, int column) {
		    Component c = super.getTableCellRendererComponent(table, value,
								      isSelected, hasFocus,
								      row, column);

		    Source s = (Source) value;
		    JLabel l = (JLabel) c;

		    l.setIcon(s.getIcon());
		    l.setText(s.getName());
		    l.setToolTipText(s.getTip());

		    // do this for a drag-under effect:
		    Color border;
		    if (row == hilited) {
			border = table.getSelectionBackground();
		    } else {
			border = new Color(0, 0, 0, 0); // (completely transparent)
		    }
		    l.setBorder(BorderFactory.createLineBorder(border, 3));

		    return l;
		}
	    });

	final JTable glue = this;

		    // drag-n-drop! -- can i not just add the gesture listener to the renderer,
		    // like i can with the tooltip?  i guess not...
		    DragGestureListener dragger = new DragGestureListener() {
			    public void dragGestureRecognized(DragGestureEvent event) {

				Point p = event.getDragOrigin();
				int i = glue.rowAtPoint(p);
				Source s = (Source) sources.get(i);

				// what if it can't be dragged? -- DEBUGGING/TESTING ONLY!
				Transferable t = s.getDragTransferable();
				if (t == null)
				    return;

				// make an Image that looks like what we're dragging: ask the renderer
				Component c = glue.getCellRenderer(i, 0).getTableCellRendererComponent(glue, s, false, true, i, 0);
				Dimension d = c.getPreferredSize();
				int w = d.width, h = d.height;
				w = glue.getWidth(); // BUT: we don't want preferred-width, we want width-as-drawn

				// create a place to draw
				BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
				Graphics2D g2 = (Graphics2D) img.getGraphics();

				// fill with background (probably white)
				g2.setColor(glue.getBackground());
				g2.fillRect(0, 0, w, h);

				// paint on the renderer
				c.setBounds(0, 0, w, h);
				c.paint(g2);

				// draw a nice black border
				g2.setColor(Color.black);
				g2.drawRect(0, 0, w-1, h-1);

				// point that was dragged
				Point pt = new Point(-p.x, -h/2); // DESIGN: y-value here is incorrect: need to measure table rows

				// start the drag
				event.startDrag(DragSource.DefaultCopyDrop,
						img, pt,
						t,
						new Tree.EmptyDragSourceListener());
			    }
			};

		    drag = new DragSource();
		    drag.createDefaultDragGestureRecognizer(this, // component
							    DnDConstants.ACTION_COPY, // always copy
							    dragger); // dragger

		    DropTargetListener dropper = new DropTargetListener() {
			    public void dragEnter(DropTargetDragEvent event) {
				event.acceptDrag(DnDConstants.ACTION_COPY);
			    }
			    public void dragOver(DropTargetDragEvent event) {
				event.acceptDrag(DnDConstants.ACTION_COPY);
				int row = rowAtPoint(event.getLocation());
				if (row == -1) { // not on a row
				    System.out.println("drag over not-a-row");
				    ; // WRITEME: draw bar, to indicate add-as-source, if it's a folder?
				} else {
				    hilited = row; // setRowSelectionInterval(row, row);
				    repaint();
				}
			    }
			    public void dragExit(DropTargetEvent event) {
				// do nothing
				hilited = -1;
				repaint();
			    }
			    public void dropActionChanged(DropTargetDragEvent event) {
				// do nothing
				event.acceptDrag(DnDConstants.ACTION_COPY);
			    }
			    public void drop(DropTargetDropEvent event) {
				event.acceptDrop(DnDConstants.ACTION_COPY);
				try {
				    Transferable t = event.getTransferable();

				    // we accept only filelists
				    if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					event.acceptDrop(DnDConstants.ACTION_COPY);
					Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
					List l = (List) o; // a List of Files

					// what's the source?
					int row = rowAtPoint(event.getLocation());
					Source s = (Source) sources.get(row);

					// load each one in turn
					for (int i=0; i<l.size(); i++) {
					    String pathname = ((File) l.get(i)).getPath();
					    //					    try {
					    if (s instanceof DatabaseSource)
						((DatabaseSource) s).drop(new File(pathname));
					    else if (s instanceof FolderSource)
						((FolderSource) s).drop(new File(pathname));
					    else
						System.out.println(pathname + " was dropped on " + s);
						// WRITEME: do stuff here
						//					    } catch (IOException ioe) {
						//						System.out.println("error on " + pathname + "!"); // NEED BETTER ERROR HANDLING!
						//					    }
					}
					repaint();
					// event.getDropTargetContext().dropComplete(true);
					event.dropComplete(true);
				    } else {
					event.rejectDrop();
				    }
				} catch (IOException ioe) {
				    event.rejectDrop(); // handle error?
				} catch (UnsupportedFlavorException ufe) {
				    event.rejectDrop(); // handle error?
				}
			    }
			};

		    drop = new DropTarget(this, // component
					  dropper); // dropper

	final JPopupMenu popup = new JPopupMenu();

	// TODO: this should be "Find... [accel F]", same as elsewhere.
	// TODO: the top component should be a popup that lists the sources (with their icons).
	// TODO: the default choice should be the selected one (main window) or my container (editor).
	JMenuItem find = Builder.makeMenuItem("find...");
	find.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // get selected source
		    Source s = getSource();

		    // search it
		    // WRITEME
		}
	    });
	popup.add(find);

	JMenuItem info = Builder.makeMenuItem("get_info...");
	info.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // get selected source
		    Source s = getSource();

		    // show info for it
		    s.showInfo();
		}
	    });
	popup.add(info);

	addMouseListener(new PopupListener() {
		public void showPopup(MouseEvent e) {
		    // figure out what row was clicked, and select it
		    int row = rowAtPoint(e.getPoint());
		    if (row == -1) // not on a row
			return;
		    setRowSelectionInterval(row, row);

		    // show the popup here
		    popup.show(e.getComponent(), e.getX(), e.getY());
		}
	    });

	// TODO: show stuff when it's selected (how?)

	// TODO: double-click to rename in-place?
    }

    /*
      IF YOU WANT to be able to rename sources in-place, you must:
      -- have isCellEditable() return true
      -- get the background color to come out right (editable cells have funky background colors)
      -- write an editor that consists of icon + jtextfield
      -- disable DND during editing, because otherwise the user can't select text with the mouse
      -- be sure to select all text when editing starts
      -- be sure to give the right text to edit -- it uses toString() by default
      all in all, an awful lot of work for a relatively small gain.  save it for a rainy day.
    */
}
