package corina.browser;

import corina.Sample;
import corina.Metadata;
import corina.Element;
import corina.Range;
import corina.graph.GraphFrame;
import corina.editor.Editor;
import corina.util.Platform;
import corina.prefs.Prefs;
import corina.gui.Bug;

import java.io.File;
import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.AbstractTableModel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import javax.swing.tree.DefaultTreeCellRenderer;

public class Browser extends JFrame {
    /*
     -- refactor!
     
     -- add ITRDB support, now that i can efficiently load URLs.  (need helper classes for ftp dir listing?)
     -- probably should abstract out FileSystem->(LocalFileSystem,RemoteFileSystem)  (later!)

     -- RET on a non-sample file opens it (canopener!)
     -- full drag-n-drop
     -- cmd-up/backspace to go up a folder?
     -- ugh, when folderpopup has focus, up/down cause all hell to break loose!  (well, sort of)  possible to fix?
     -- type at table to jump to file?

     -- i18n. (futured.)  (nearly done, too.)

     -- possible to select multiple files for summing/plotting/grids/bargraphs/etc.?  (what's my interface?  ps7-style popup?)
     -- MENUS! :
     -- menu: File -> { open, plot, index (!), crossdate (?), grid }
     -- menu: Edit -> { Undo, Redo, Cut/Copy/Paste, Select All, Select None }
     -- menu: Help -> Corina Help (/Browser.html)

     -- ps7 context menu: [ open, select all, deselect all, ---, rename, batch rename..., delete, ---, (rotate), ---, reveal location in finder, new folder, ---, (rankings) ]

     -- add "little trash icon" like ps7 has.  (ready, just needs dnd)

     -- whenever "fields" changes, save it as a preference ("corina.browser.fields = name size modified format range length")
     -- set initial value of fields from preference
     -- if no preference, pick something sensible
     -- store in order, including column-reordering mangling
     -- load on open.  when to save?  -- when selecting a menuitem; -- when reordering the columns.

     -- search string "pam am" should match "pam, 7am", but not "pam" alone (!)

     -- highlight search term matches in the table -- this sounds hard

     -- need something along the lines of:
     ---- open in new tab
     ---- clone this view (in another tab, window)
     ---- i dunno, but some way to manage multiple views.
     */

    private FolderPopup folderPopup;

    public void goToParent(int numTimes) {
        File me = new File(folder);
        for (int i=0; i<numTimes; i++)
            me = me.getParentFile();
        folder = me.getPath();
    }

    private JLabel label;
    private void updateSummary() {
        String text = files.size() + " files and folders"; // XXX
        if (!searchField.isEmpty())
            text += ", " + visibleFiles.size() + " shown";
        label.setText(text);
    }

    private SearchField searchField;
    private JTable table;
    private AbstractTableModel model;
    public Browser() {
        this(System.getProperty("corina.dir.data"));
    }
    public Browser(String dir) {
        super(Platform.isMac ? "Browser" : "Corina: Browser"); // or just "Corina", if this is my top-level?
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        setContentPane(p);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // set folder
        folder = dir;

// TEMPORARILY REMOVED!  if there's only one tab, it's not worth it.
//        JTabbedPane t = new JTabbedPane();
//        p.add(t);

        // label
        label = new JLabel("", JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        thermo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        {
            // main panel...
            JPanel main = new JPanel(new BorderLayout());

            // north is another panel
            JPanel north = new JPanel();
            north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));
            main.add(north, BorderLayout.NORTH);

            // "folder"
            JLabel folderLabel = new JLabel("Folder:");
            if (!Platform.isMac)
                folderLabel.setDisplayedMnemonic('F');
            folderPopup = new FolderPopup(folder, this);
            folderLabel.setLabelFor(folderPopup);
            north.add(Box.createHorizontalStrut(14));
            north.add(folderLabel);
            north.add(Box.createHorizontalStrut(10));
            north.add(folderPopup);
            
            // |<-- space -->|
            north.add(Box.createHorizontalGlue());
            north.add(Box.createVerticalStrut(50)); // temp hack...

            // load default fields from prefs
            loadFields();
             
            // center is the table (but searchfield needs this, so put it here)
            model = new BrowserTableModel();
            table = new JTable(model) {
                public boolean isManagingFocus() {
                    return false; // don't let the table see tab/ctrl-tab -- DOESN'T WORK ON WIN32?
                }
            };
            table.setShowGrid(false);
            table.setShowVerticalLines(true);
            table.setGridColor(Color.lightGray);
            addIconsForFirstColumn();
            // for the other columns, too
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        // get existing label
                        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        // every-other-line colors -- OAOO me
                        c.setOpaque(true);
                        if (!isSelected)
                            c.setBackground(row%2==0 ? oddRowColor : Color.white);

                        // return it
                        return c;
                    }
                });

            // "search for"
            JLabel searchLabel = new JLabel("Search for:");
            if (!Platform.isMac)
                searchLabel.setDisplayedMnemonic('S');
            searchField = new SearchField(this, table);
            searchLabel.setLabelFor(searchField);
            north.add(searchLabel);
            north.add(Box.createHorizontalStrut(10));
            north.add(searchField);
            north.add(Box.createHorizontalStrut(14));
            
            // table: return opens a file/folder
            table.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER)
                        e.consume();
                }
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER)
                        e.consume();
                }
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        e.consume(); // don't go to next row of table
                        openCurrentFile();
                    }
                }
            });

            // table: double-click opens, too.
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        openCurrentFile();
                    }
                }
            });

            // table: click-to-sort
            table.getTableHeader().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() != 1) // should double-click count as 2 clicks?  users might find that natural.
                        return;
                    sortBy(table.getColumnModel().getColumnIndexAtX(e.getX()));
                }
            });

            // table: initial view
            doList();
            main.add(new JScrollPane(table), BorderLayout.CENTER);

            // south is a summary label, and maybe other stuff
            JPanel south = new JPanel(new BorderLayout());
            south.add(label, BorderLayout.EAST); // was: .CENTER
            south.add(thermo, BorderLayout.WEST);
            main.add(south, BorderLayout.SOUTH);

// TEMPORARILY REMOVED!  see above.
//            t.addTab("Data", main);
            p.add(main);
        }

//        { // placeholder
//            t.addTab("ITRDB", new JLabel("Coming soon...", JLabel.CENTER));
//        }

        makeMenus();

        // try putting keyboard-sorting on a bunch of components -- BUG: still doesn't seem to work on win32, but why?
        addKeyListener(new KeyboardSorter());
        folderPopup.addKeyListener(new KeyboardSorter());
        searchField.addKeyListener(new KeyboardSorter());

        // if user re-orders columns, save new prefs.
        table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) { }
            public void columnMarginChanged(ChangeEvent e) { }
            public void columnMoved(TableColumnModelEvent e) {
                saveFields();
            }
            public void columnRemoved(TableColumnModelEvent e) { }
            public void columnSelectionChanged(ListSelectionEvent e) { }
        });
        
        pack();
        show();
        searchField.requestFocus();
    }

    private void addIconsForFirstColumn() {
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                // get existing label
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // every-other-line colors
                c.setOpaque(true);
                if (!isSelected)
                    c.setBackground(row%2==0 ? oddRowColor : Color.white);

                // get icon, text from row
                Row r = (Row) visibleFiles.get(row);
                c.setIcon(r.getIcon());
                c.setText(r.getName());

                // done
                return c;
            }
        });
    }

    // watch for cmd-/n/
    private class KeyboardSorter extends KeyAdapter {
        public void keyTyped(KeyEvent e) {
            boolean cmdPressed = ((Platform.isMac && e.isMetaDown()) || (Platform.isWindows && e.isControlDown()));
            if (cmdPressed && Character.isDigit(e.getKeyChar())) {
                // 0 means 10, because it's the 10th key on the board; otherwise 1=0, 2=1, ...
                int column;
                if (e.getKeyChar() == '0')
                    column = 9;
                else
                    column = Character.getNumericValue(e.getKeyChar()) - 1;

                // make sure there's enough columns, and call sortBy()
                if (column < fields.size())
                    sortBy(column);
            }
        }
    }

    private final static String INDENT = "    "; // 4 spaces

    private void makeMenus() {
        JMenuBar mb = new JMenuBar();

        // FILE
        JMenu file = new JMenu("File");

        // open
        JMenuItem graph = new JMenuItem("Graph");
        graph.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // get selected samples
                int selected[] = table.getSelectedRows();
                int n = selected.length;
                List elements = new ArrayList(n);
                for (int i=0; i<n; i++) {
                    elements.add(new Element(((Row) visibleFiles.get(selected[i])).getPath()));
                }
                // BUG: not all rows will be loadable ... only get samples
                // BUG: what if no files are selected?  (add listener so if none are, this isn't even enabled?)
                // TODO: add cmd-G shortcut for graph
                
                // graph
                new GraphFrame(elements);
            }
        });
        file.add(graph);

        // VIEW
        JMenu display = new JMenu("View");

        // -- file metadata -- USE RESOURCEBUNDLE TO GET THE NAMES!
        display.add(new FieldCheckBoxMenuItem("name", msg.getString("browser_name"), model));
        display.add(new FieldCheckBoxMenuItem("kind", msg.getString("browser_kind"), model));
        display.add(new FieldCheckBoxMenuItem("size", msg.getString("browser_size"), model));
        display.add(new FieldCheckBoxMenuItem("modified", msg.getString("browser_modified"), model));

        // this one isn't even normally used, but it's there.
        display.add(new FieldCheckBoxMenuItem("filetype", "Filetype", model));

        display.addSeparator();

        // -- range metadata -- USE RESOURCEBUNDLE!
        display.add(new FieldCheckBoxMenuItem("range", msg.getString("browser_range"), model));
        display.add(new FieldCheckBoxMenuItem("start", INDENT + msg.getString("browser_start"), model));
        display.add(new FieldCheckBoxMenuItem("end", INDENT + msg.getString("browser_end"), model));
        display.add(new FieldCheckBoxMenuItem("length", INDENT + msg.getString("browser_length"), model));

        display.addSeparator();

        // -- sample metadata
        for (int i=0; i<Metadata.fields.length; i++)
            display.add(new FieldCheckBoxMenuItem(Metadata.fields[i].variable, Metadata.fields[i].description, model));

        mb.add(file);
        mb.add(display);
        setJMenuBar(mb); // use xmenubar for a real file menu, etc.?
    }

    private class FieldCheckBoxMenuItem extends JCheckBoxMenuItem {
        private String field, label;
        FieldCheckBoxMenuItem(String variable, String label, AbstractTableModel tableModel) {
            super(label);
            this.field = variable;
            this.label = label;
            setSelected(fields.indexOf(field) != -1);
            final AbstractTableModel glue = tableModel;
            addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (isSelected()) {
                        // selected => add to end of list
                        fields.add(field);
                    } else {
                        // unselected => remove from list
                        fields.remove(fields.indexOf(field));
                    }

                    // either way, the table changed
                    glue.fireTableStructureChanged();

                    // that kills the icons in col0, so reset those
                    addIconsForFirstColumn();

                    // re-search, since the last search may very well no longer be valid
                    doSearch();
                    
                    // finally, save these fields in the prefs
                    saveFields();
                }
            });
        }
    }

    private JProgressBar thermo = new JProgressBar();

    // (actually a method of the table)
    private void selectFirstRow() {
        if (table.getRowCount() >= 1)
            table.setRowSelectionInterval(0, 0);
    }

    private void openCurrentFile() {
        int i = table.getSelectedRow();
        if (i == -1) return; // no file selected
        Row r = (Row) visibleFiles.get(i); // XXX
        // if fn is a folder, enter that folder
        if (r.isDirectory()) {
            // set folder and re-list table
            folder = r.getPath();
            doList();

            // add an element to the folderPopup
            folderPopup.descendInto(new File(folder));
            
            // focus back on the search field?
            searchField.requestFocus();
            
            // stop.  (not pretty, i know.)
            return;
        }

        // nope, it's a file, so try to open that.  BETTER: use the canopener so you can load grids, too.
        try {
            new Editor(new Sample(r.getPath()));
            // FIXME: this editor is just a JFrame, so add a close listener to update this row when it closes,
            // iff the modified date changes.
            // (or even: as long as it's open, add a thread to watch it by statting the file every (10?) sec.)
        } catch (IOException ioe) {
            System.out.println("ugh, i/o error: " + ioe);
        }
    }

    private String folder=System.getProperty("corina.dir.data"); // redundant?

    private static final Map fileMetadata = new Hashtable();
    static {
        // you know, this seems downright silly...
        fileMetadata.put("name", "browser_name");
        fileMetadata.put("size", "browser_size");
        fileMetadata.put("kind", "browser_kind");
        fileMetadata.put("modified", "browser_modified");

        fileMetadata.put("range", "browser_range");
        fileMetadata.put("start", "browser_start");
        fileMetadata.put("end", "browser_end");
        fileMetadata.put("length", "browser_length");
    }

    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");
    
    // this'll get a lot bigger, and eventually go in its own file
    class BrowserTableModel extends AbstractTableModel {
        public int getRowCount() {
            return visibleFiles.size();
        }
        public int getColumnCount() {
            return fields.size();
        }
        public String getColumnName(int column) {
            String field = (String) fields.get(column);

            // file metadata
            if (fileMetadata.containsKey(field))
                return msg.getString((String) fileMetadata.get(field));

            // standard metadata label
            for (int i=0; i<Metadata.fields.length; i++)
                if (Metadata.fields[i].variable.equals(field))
                    return Metadata.fields[i].description;

            // "filetype", which is (sort of) a hack.  (well, it sure is now!)
            if (fields.get(column).equals("filetype"))
                return "Filetype";

            // unknown -- should never happen
            return "???";
        }
        public Object getValueAt(int row, int column) {
            Row r = (Row) visibleFiles.get(row);
            return r.getField((String) fields.get(column));
        }
    }

    // fields to view
    private List fields = new ArrayList();
    public List getVisibleFields() {
        return fields;
    }
    
    // files to view
    List files=new ArrayList(); // of Element
    List visibleFiles= new ArrayList(); // of Element

    public void doList() {
        // reset search field (good idea?)
        searchField.reset();

        // clear old list -- WHY can't i just make a new one?
        while (files.size() > 0)
            files.remove(0);

        // dump all files
        File all[] = new File(folder).listFiles();
        for (int i=0; i<all.length; i++)
            if (!all[i].isHidden())
                files.add(new Row(all[i], this));

        // sort using whatever the current sort is
        doSort();

        // update data
        // -- BUG: what happens when loading a file causes it to appear in the search field?
        if (timer != null)
            timer.stop(); // stop it?
        timer = new Timer(200 /* 100ms = 0.1s */, new AbstractAction() {
            // testing seems to indicate <100ms is faster than it can load them, and >200ms is wasting noticable time.
            // 200 is a good compromise between "reasonably fast" and "reasonably responsive".
            // of course, that's on my laptop disk.  i'll have to test it on RAID over 100bt sometime.

            /*
             a better sort of timer usage would be:
             -- load 'em all as fast as you can
             -- fire tablemodel events every /n/ ms.
             can this architecture support that?  sure:
             -- start a new thread to just load samples into |files|
             -- start a timer to just call model.fire
             how does |visibleFiles| get updated from |files|?
             -- make Row mutable
             */
            public void actionPerformed(ActionEvent e) {
                if (next < files.size()) {
                    Row r = (Row) files.get(next);
                    while (r.isDirectory()) {
                        next++;
                        if (next == files.size()) {
                            stop(e);
                            return;
                        }
                        r = (Row) files.get(next); // OAOO me?  this is a weird loop.
                    }

                    // load the metadata for this file.
                    r.load();

                    // update search, too.
                    doSearch(); // i only need to check one row, so there must be a much more efficient way to do this

                    int i[] = table.getSelectedRows(); // i'd like to re-select these, but how?
                    model.fireTableDataChanged(); // row only would be much more efficient, if i knew which row
                    next++;
                    thermo.setValue(next);
                } else {
                    stop(e); // i'd like to be OAOO'd, too
                }
            }
            private void stop(ActionEvent e) {
                // stop timer
                ((Timer) e.getSource()).stop();
                thermo.setValue(0);
            }
        });
        next=0;
        thermo.setValue(0);
        thermo.setMaximum(files.size());
        timer.start();

        // update search
        doSearch();
    }

    // the name of the field to sort by
    private String sortField = "name";
    private boolean reverse = false;

    public void sortBy(int viewColumn) {
        int modelColumn = table.convertColumnIndexToModel(viewColumn);
        // make sure it's a valid column
        String newSort = (String) fields.get(modelColumn);
        if (sortField.equals(newSort)) {
            reverse = !reverse;
            // wait ... in this case, i don't need to doSort() again; reverse() should be good enough, right?
            // UNLESS: this breaks stability (does it?)
            // let's try it:
            Collections.reverse(visibleFiles);
            model.fireTableDataChanged();
        } else {
            sortField = newSort;
            reverse = false;
            doSort();
        }
    }

    // run the sort based on |sortField| and |reverse|, updating the table when done
    private void doSort() {
        Collections.sort(files, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object f1 = ((Row) o1).getField(sortField);
                Object f2 = ((Row) o2).getField(sortField);

                // if one is null, dump it at the end(?)
                if (f1==null && f2==null)
                    return 0;
                if (f1 == null)
                    return +1;
                if (f2 == null)
                    return -1;

                // if they're strings, ignore case when sorting, because users will.
                if (f1 instanceof String && f2 instanceof String) {
                    if (!reverse)
                        return ((String) f1).compareToIgnoreCase((String) f2);
                    else
                        return ((String) f2).compareToIgnoreCase((String) f1);
                }

                // sort numbers backwards, high-to-low.  (this is why FileLength extends Number)
                if (f1 instanceof Number && f2 instanceof Number) {
                    if (!reverse)
                        return ((Comparable) f2).compareTo(f1);
                    else
                        return ((Comparable) f1).compareTo(f2);
                }

                // dates should go backwards (newest-to-oldest) -- this is why RelativeDate should extend Date
                if (f1 instanceof RelativeDate && f2 instanceof RelativeDate) {
                    if (!reverse)
                        return ((Comparable) f2).compareTo(f1);
                    else
                        return ((Comparable) f1).compareTo(f2);
                }

                // ranges should go backwards, because that's fallback-order
                if (f1 instanceof Range && f2 instanceof Range) {
                    if (!reverse)
                        return ((Comparable) f2).compareTo(f1);
                    else
                        return ((Comparable) f1).compareTo(f2);
                }
                
                // whatever's left, just compare them
                if (!reverse)
                    return ((Comparable) f1).compareTo(f2);
                else
                    return ((Comparable) f2).compareTo(f1);
            }
        });
        doSearch();
        // Q: would it be better to run the sort on just the visible elements?
        // maybe: sort(visible), <update>, sort(all)
        // it might look more responsive that way.
        // the downside: if the user types something, you'll need all sorted, anyway.
        // what you're saving here is having to run doSearch() when sorting, which could be significant.
        model.fireTableDataChanged();
    }

    // the metadata-loader-updater
    Timer timer=null;
    private int next=0; // next row to update

    // searching: update |visibleFiles| from |files|, using |searchField|
    public void doSearch() {
        // clear old list
        while (visibleFiles.size() > 0)
            visibleFiles.remove(0);

        // create search target: fake case insensitivity by doing everything in lower-case
        String words[] = searchField.getTextAsWords();

        // loop through files, adding to visibleFiles if it should be visible
        for (int i=0; i<files.size(); i++) {
            Row r = (Row) files.get(i);
            if (r.matches(words))
                visibleFiles.add(r);
            continue;
        }

        // update the table and summary line, and select the first one
        model.fireTableDataChanged();
        updateSummary();
        selectFirstRow();
    }

    // returns true iff |test| matches (case-insensitiviley) all of |terms[]|;
    // assumes all of |terms[]| are lower-case.
    static boolean matchesAny(String test, String terms[]) {
        // no terms to match?  then it can't not-match.
        if (terms.length == 0)
            return true;

        test = test.toLowerCase();
        boolean match = true;
        for (int i=0; i<terms.length; i++) {
            if (test.indexOf(terms[i]) == -1) {
                match = false;
                break;
            }
        }
        return match;
    }

    // odd-row color
    private static Color oddRowColor = new Color(232, 245, 255); // iTunes is more like 230,243,255

    // save fields as a pref
    private synchronized void saveFields() {
        // generate the string
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<fields.size(); i++) {
            int j = table.convertColumnIndexToModel(i); // view -> model mapping
            buf.append((String) fields.get(j));
            if (i < fields.size()-1)
                buf.append(" ");
        }

        // save to prefs
        System.setProperty("corina.browser.fields", buf.toString()); // OAOO the property?
        try {
            Prefs.save();
        } catch (IOException ioe) {
            Bug.bug(ioe); // i dunno what happened...
        }
    }

    // load fields from prefs (or use "name,size,range,format" as a reasonable default)
    private void loadFields() {
        String pref = System.getProperty("corina.browser.fields", "name size modified range format");
        StringTokenizer tok = new StringTokenizer(pref, ", ");
        int n = tok.countTokens();
        fields = new ArrayList(n);
        for (int i=0; i<n; i++)
            fields.add(tok.nextToken());
    }
}
