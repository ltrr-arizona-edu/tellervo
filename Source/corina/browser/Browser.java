package corina.browser;

import corina.Sample;
import corina.editor.Editor;
import corina.util.Platform;

import java.io.File;
import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.tree.DefaultTreeCellRenderer;

public class Browser extends JFrame {
    /*
     -- refactor!  this is too big for one source file now.
     -- add ITRDB support, now that i can efficiently load URLs.  (need helper classes for ftp dir listing?)
     -- probably should abstract out FileSystem->(LocalFileSystem,RemoteFileSystem)
     
     -- put folders first?  no, better: its type is "Folder", so only when sort=filetype
     -- by default, select the first one
     -- RET on a non-sample file opens it (canopener!)
     -- in background thread, load/cache metadata, updating view as you go
     -- if a file is not a sample, dim it to 50%
     -- use tree-doc icon for samples
     -- full drag-n-drop
     -- cmd-up/backspace to go up a folder?
     -- ugh, when folderpopup has focus, up/down cause all hell to break loose!  (well, sort of)  possible to fix?
     -- type at table to jump to file
     
     -- possible to select multiple files for summing/plotting/grids/bargraphs/etc.?  (what's my interface?)

     -- add generous spacing around components, especially folder/search components

     -- tab-order is folder, search, files, sort-by, show-headers.

     -- i18n (futured)

     -- MENUS! :
     -- menu: Sort -> by { name, kind, size, date-mod, <all metadata fields> }
     -- menu: Show -> { <everything in Sort menu, with name=dimmed,checked, > }

     -- add full support for file-metadata and sample-metadata columns to table.
     -- search ALL VISIBLE COLUMNS, not just filename

     -- ps7 context menu: [ open, select all, deselect all, ---, rename, batch rename..., delete, ---, (rotate), ---, reveal location in finder, new folder, ---, (rankings) ]
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
        String text = files.size() + " files and folders";
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
        
        JTabbedPane t = new JTabbedPane();
        p.add(t);

        // label
        label = new JLabel("", JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
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

            // center is the table (but searchfield needs this, so put it here)
            model = new BrowserTableModel();
            table = new JTable(model) {
                public boolean isManagingFocus() {
                    return false; // don't let the table see tab/ctrl-tab -- DOESN'T WORK ON WIN32?
                }
            };
            table.setShowGrid(false);
            table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    // CATCHER FOR THREAD PROTOTYPE
                    if (!(visibleFiles.get(row) instanceof File)) {
                        c.setIcon(leafIcon);
                        c.setText(visibleFiles.get(row).toString());
                        return c;
                    }
                    File f = (File) visibleFiles.get(row); // is this just another way to say f=value?
                    c.setIcon(f.isDirectory() ? closedIcon : leafIcon); // leafIcon isn't exactly correct on win32
                    c.setText(f.getName()); // show just the name, not the whole path
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
            
            // table: initial view
            doList();
            main.add(new JScrollPane(table), BorderLayout.CENTER);

            // south is a summary label
            main.add(label, BorderLayout.SOUTH);

            t.addTab("Local", main);
        }

        { // placeholder
            t.addTab("ITRDB", new JLabel("Coming soon...", JLabel.CENTER));
        }
        
        pack();
        show();
        searchField.requestFocus();
    }

    // (actually a method of the table)
    private void selectFirstRow() {
        if (table.getRowCount() >= 1)
            table.setRowSelectionInterval(0, 0);
    }

    private void openCurrentFile() {
        int i = table.getSelectedRow();
        File f = (File) visibleFiles.get(i);
        
        // if fn is a folder, enter that folder
        if (f.isDirectory()) {
            // set folder and re-list table
            folder = f.getPath();
            doList();

            // add an element to the folderPopup
            folderPopup.descendInto(f);

            // focus back on the search field?
            searchField.requestFocus();

            // stop.  (not pretty, i know.)
            return;
        }

        // nope, it's a file, so try to open that.  BETTER: use the canopener so you can load grids, too.
        try {
            new Editor(new Sample(f.getPath()));
        } catch (IOException ioe) {
            System.out.println("ugh, i/o error: " + ioe);
        }
    }

    private String folder=System.getProperty("corina.dir.data");

    // this'll get a lot bigger, and eventually go in its own file
    class BrowserTableModel extends AbstractTableModel {
        public int getRowCount() {
            return visibleFiles.size();
        }
        public int getColumnCount() {
            return 1;
        }
        public String getColumnName(int column) {
            return "Name";
        }
        public Object getValueAt(int row, int column) {
            return visibleFiles.get(row);
        }
    }

    // files to view
    List files=new ArrayList(); // of Element
    List visibleFiles= new ArrayList(); // of Element

    public void doList() {
        // reset search field (good idea?)
        searchField.reset();

        // clear old list
        while (files.size() > 0)
            files.remove(0);

        // dump all files
        File all[] = new File(folder).listFiles();
        for (int i=0; i<all.length; i++)
            if (!all[i].isHidden())
                files.add(all[i]);

        // just sort them by name (alphabetical, case-insensitive) for now.
        Collections.sort(files, new Comparator() {
            public int compare(Object o1, Object o2) {
                File f1 = (File) o1;
                File f2 = (File) o2;
                return f1.getName().compareToIgnoreCase(f2.getName());
            }
        });

        // update search
        doSearch();
    }

    // searching: update |visibleFiles| from |files|, using |searchField|
    public void doSearch() {
        // clear old list
        while (visibleFiles.size() > 0)
            visibleFiles.remove(0);

        // create search target: fake case insensitivity by doing everything in lower-case
        String words[] = searchField.getTextAsWords();

        // loop through files, adding to visibleFiles if it should be visible
        for (int i=0; i<files.size(); i++) {
            // TEMPORARY CATCH FOR THREAD PROTOTYPE
            if (!(files.get(i) instanceof File)) { visibleFiles.add(files.get(i)); continue; }
            
            File f = (File) files.get(i);

            // look for a match
            String check = f.getName().toLowerCase();
            boolean match = true; // bad!  use a named break?
            for (int j=0; j<words.length; j++) {
                if (check.indexOf(words[j]) == -1) {
                    match = false;
                    break;
                }
            }
            if (match)
                visibleFiles.add(f);
        }

        // update the table and summary line, and select the first one
        model.fireTableDataChanged();
        updateSummary();
        selectFirstRow();
    }

    // icon singletons
    private static Icon leafIcon = new DefaultTreeCellRenderer().getLeafIcon();
    private static Icon closedIcon = new DefaultTreeCellRenderer().getClosedIcon();
}
