package corina.prefs.panels;

import corina.prefs.Prefs;
import corina.prefs.components.FormattingPrefComponent;
import corina.prefs.components.BoolPrefComponent; // RENAME to: Check[Box?]PrefComponent
import corina.prefs.components.ColorPrefComponent;
import corina.gui.*;
import corina.gui.layouts.DialogLayout;

import java.util.Vector;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CrossdatingPrefsPanel extends JPanel {

    public CrossdatingPrefsPanel() {
        // top: dialog layout
        JPanel top = new JPanel(new DialogLayout());

        // formats for each score
        JComponent t = new FormattingPrefComponent("corina.cross.tscore.format",
                                                    "0.00");
        JComponent r = new FormattingPrefComponent("corina.cross.rvalue.format",
                                                    "0.00");
        JComponent tr = new FormattingPrefComponent("corina.cross.trend.format",
                                                    "0.0%");
        JComponent d = new FormattingPrefComponent("corina.cross.dscore.format",
                                                    "0.00");
        JComponent wj = new FormattingPrefComponent("corina.cross.weiserjahre.format",
                                                    "0.0%");
        top.add(t, "T-score format:");
        top.add(r, "R-value format:");
        top.add(tr, "Trend format:");
        top.add(d, "D-score format:");
        top.add(wj, "Weiserjahre format:");

        // gap
        top.add(new JPanel(), ""); // ???
        
        // min overlap
        JComponent overlap = new OverlapPopup();
        top.add(overlap, "Minimum overlap:");
        
        // bottom: box layout?
        JPanel bottom = new JPanel();
        bottom.setLayout(new DialogLayout()); // new BoxLayout(bottom, BoxLayout.Y_AXIS));
        // WRITEME

        // highlight sig scores -- corina.grid.highlight
        // TODO: rename this pref to corina.cross.highlight!
        BoolPrefComponent hilite = new BoolPrefComponent("Highlight significant scores",
                                                         "corina.grid.highlight");
        
        // highlight color (indented) -- corina.grid.highlightcolor
        // TODO: rename this pref to corina.cross.highlight.color?
        ColorPrefComponent color = new ColorPrefComponent("corina.grid.highlightcolor");

        hilite.controls(color);

        bottom.add(hilite);
        bottom.add(color, "Color:");

        // put it all together
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(top);
        add(bottom);
    }
    
    // a popup, which lets the user set the value of corina.cross.overlap
    // to any of a number of presets.
    private static class OverlapPopup extends JPanel { // !!!
        // entries: any, 5, 10, 25, 50, 100, 250, 500 years.
        // this list must be in order (lowest-to-highest), and no value should
        // be less than 1 (which would make no sense).
        private final static int YEARS[] = new int[] { 1, 5, 10, 25, 50, 100, 250, 500 };

        public OverlapPopup() {
            // make |v| a vector of the possible values, as strings
            Vector v = new Vector();
            for (int i=0; i<YEARS.length; i++)
                v.add(YEARS[i] == 1 ? "any" : (YEARS[i] + " years"));

            // make a popup
            final JComboBox popup = new JComboBox(v);
            add(popup);

            // get initial value -- default is 50
            String init = Prefs.getPref("corina.cross.overlap");
            int value = 50; // default is 50
            if (init != null)
                try {
                    value = Integer.parseInt(init);
                } catch (NumberFormatException nfe) {
                    // ignore -- we'll just use 50, then
                }

            // select value from the list.
            // use >= so if it's something crazy like 51, 50 gets set --
            // remember, the list must be in order.
            for (int i=0; i<YEARS.length; i++)
                if (YEARS[i] >= value)
                    popup.setSelectedIndex(i);

            // add listener
            popup.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    // figure out what got selected
                    int i = popup.getSelectedIndex();
                    int n = YEARS[i];

                    // store it as corina.cross.overlap
                    Prefs.setPref("corina.cross.overlap", String.valueOf(n));
                }
            });
        }
    }

}
