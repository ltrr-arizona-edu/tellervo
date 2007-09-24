package corina.browser;

import corina.Sample;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JTable;

// TODO: do i need to make Data and Metadata (MetadataTemplate) classes first?
// would the lazy-load capabilities of that make this easier?

public class BrowserComponent extends JTable {

    // WRITEME
    
    public BrowserComponent(List samples) {
        // WRITEME
    }
    
    public static void main(String args[]) {
        List l = new ArrayList();
        for (int i=0; i<args.length; i++) {
            try {
                Sample s = new Sample(args[i]);
                l.add(s);
            } catch (java.io.IOException ioe) { // (including WFTE)
                // ignore
            }
        }

        BrowserComponent bc = new BrowserComponent(l);
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane(bc);
        javax.swing.JFrame f = new javax.swing.JFrame("BrowserComponent test");
        f.getContentPane().add(sp);
        f.pack();
        f.show();
    }
}
