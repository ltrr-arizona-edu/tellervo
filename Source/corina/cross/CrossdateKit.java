package corina.cross;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.DefaultListModel;

public class CrossdateKit extends JFrame {
    private JComboBox algPopup;
    private JList fixedList;
    private JList movingList;
    private JButton prevButton;
    private JButton nextButton;
    private JPanel makeSidebar() {
        // create stuff first
        algPopup = new JComboBox();
        algPopup.addItem("T-Score");
        algPopup.addItem("Trend");
        algPopup.addItem("D-Score");
        //        fixedList.addItem("Weiserjahre"); // only if one/both(?) has wj
        // ...
        fixedList = new JList(new DefaultListModel());
        {
            DefaultListModel m = (DefaultListModel) fixedList.getModel();
            for (int i=0; i<9; i++)
                m.add(i, "Zonguldak, Karabuk " + (i+1) + "A");
        }
        // ...
        movingList = new JList(new DefaultListModel());
        {
            DefaultListModel m = (DefaultListModel) movingList.getModel();
            for (int i=0; i<9; i++)
                m.add(i, "Zonguldak, Karabuk " + (i+1) + "A");
        }
        // ...
        prevButton = new JButton("Prev");
        // ...
        nextButton = new JButton("Next");
        // ...
        JPanel buttonSet = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonSet.add(prevButton);
        buttonSet.add(nextButton);

        // now put it into the sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.add(onLeft(new JLabel("Fixed Sample:"), 0));
        sidebar.add(onLeft(new JScrollPane(fixedList), 16));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(onLeft(new JLabel("Moving Sample:"), 0));
        sidebar.add(onLeft(new JScrollPane(movingList), 16));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(onLeft(new JLabel("Algorithm:"), 0));
        sidebar.add(onLeft(algPopup, 16));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(buttonSet);

        return sidebar;
    }
    private JComponent onLeft(JComponent c, int indent) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(Box.createHorizontalStrut(indent));
        p.add(c);
        return p;
    }

    public CrossdateKit() {
        getContentPane().add(makeSidebar(), BorderLayout.WEST);
        // tabs/tables are center, graph/map is north(?)
        pack();
        show();
    }
    
    public static void main(String args[]) {
        new CrossdateKit();
    }

    /*
     general stuff:

     -- prev/next touch alg/fixed/moving
     -- alg/fixed/moving touch table
     -- table touches graph
     -- (alg/fixed/moving touch map)
     -- collapser triangle for graph/map: resizes frame, as well.  (jsplitpane-adjustable, too?  sure.)
     -- load all elements on startup; actually, load them in a background thread so the first one comes up right away.
     
     future:
     -- drag-from and drop-onto fixed/moving lists
     -- when relevant, add/enable weiserjahre algorithm
     -- print?
     -- save?
     */
}
