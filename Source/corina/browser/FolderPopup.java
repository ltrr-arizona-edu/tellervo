package corina.browser;

import java.io.File;

import java.util.Vector;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.AbstractAction;

public class FolderPopup extends JComboBox {
    // make a new folderpopup
    public FolderPopup(String folder, Browser browser) {
        // make a vector of the names of this folder, and all its parents up to corina.dir.data
        Vector items = new Vector();
        File root = new File(System.getProperty("corina.dir.data"));
        for (File f=new File(folder); !f.equals(root); f=f.getParentFile())
            items.add(f.getName());
        items.add(root.getName());
        
        // ... and use it as my data model
        setModel(new DefaultComboBoxModel(items));

        // custom renderer: add open-folder icon
        setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setIcon(openIcon); // UGH!  why am i setting this every time?  just set it once, on construction!
                return c;
            }});

        // watch for selection
        final Browser glue = browser;
        addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // java bug workaround: if descending, don't do anything here.
                if (descending) return;
                
                // selected itself?  don't do anything.
                int selected = getSelectedIndex();
                if (selected == 0)
                    return;

                if (selected >= getItemCount()) {
                    System.out.println("working around bug in java");
                    return;
                }

                // delete everything before this
                for (int i=0; i<selected; i++)
                    removeItemAt(0);

                // figure out what i should be looking at
                glue.goToParent(selected);

                // re-list table
                glue.doList();
            }
        });

        // ignore keypresses -- does this work?  do i want this?
        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                e.consume();
            }
            public void keyReleased(KeyEvent e) {
                e.consume();
            }
            public void keyTyped(KeyEvent e) {
                e.consume();
            }
        });
    }

    private boolean descending = false;
    public void descendInto(File f) {
        descending = true; // don't handle actionevents while doing the insertItemAt()
        insertItemAt(f.getName(), 0);
        setSelectedIndex(0);
        descending = false;
    }
    private Browser browser;

    // icon singletons
    private static Icon openIcon = new DefaultTreeCellRenderer().getOpenIcon();
}
