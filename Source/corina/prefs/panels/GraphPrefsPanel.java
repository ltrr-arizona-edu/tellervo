package corina.prefs.panels;

import corina.gui.layouts.DialogLayout;
import corina.gui.Layout;
import corina.prefs.Prefs;
import corina.prefs.components.ColorPrefComponent;
import corina.prefs.components.BoolPrefComponent;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class GraphPrefsPanel extends JPanel {

    public GraphPrefsPanel() {
        setLayout(new DialogLayout());
        
        ColorPrefComponent backColor = new ColorPrefComponent("corina.graph.background");
        ColorPrefComponent axisColor = new ColorPrefComponent("corina.graph.foreground");
        
        BoolPrefComponent drawSapwood = new BoolPrefComponent("Draw sapwood with thicker line", "corina.graph.sapwood");
        BoolPrefComponent drawIndexes = new BoolPrefComponent("Draw indexes with dotted line", "corina.graph.dotindexes");
        BoolPrefComponent drawBaselines = new BoolPrefComponent("Draw baselines", "corina.graph.baselines");
        BoolPrefComponent drawGraphpaper = new BoolPrefComponent("Draw graphpaper", "corina.graph.graphpaper");

        ColorPrefComponent graphpaperColor = new ColorPrefComponent("corina.graph.graphpaper.color");
        
        // WRITEME: pixels-per-year, as empty string -- no, as slider
        // "narrower" = 2, "wider" = 20? 30? 40?
        JComponent horizScale = new HorizontalScaleSlider("corina.graph.pixelsperyear",
                                        "Narrower", "Wider", 2, 20, 10);
        
        // put them all together
        setLayout(new DialogLayout());
        add(backColor, "Background color:");
        add(axisColor, "Axis/cursor color:");
        add(drawSapwood, "");
        add(drawIndexes, "");
        add(drawBaselines, "");
        add(drawGraphpaper, "");
        add(graphpaperColor, "Graphpaper color:");
        add(horizScale, "Horizontal scale:");
    }
    
    // TODO: rename to simply SliderPrefComponent, document, and extract
    private static class HorizontalScaleSlider extends JPanel {
        HorizontalScaleSlider(String preference,
			      String left, String right, int min, int max,
			      int defaultValue) {

            int value = defaultValue;
            try {
                value = Integer.parseInt(Prefs.getPref(preference));
            } catch (NumberFormatException nfe) {
                // it wan't a valid integer: but that's ok,
                // we've already set it to defaultValue
            } catch (NullPointerException npe) {
                // FIXME: make this an if-else, not a catch!
            }

            final JSlider slider = new JSlider(min, max, value);

            // watch for changes.
            final String pref = preference;
            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    // we'll only change things when the user releases
                    // the mouse button, for now.
                    if (!slider.getValueIsAdjusting()) {
                        Prefs.setPref(pref, String.valueOf(slider.getValue()));
                    }
                }
            });

            add(Layout.borderLayout(slider,
                                    new JLabel(left), null, new JLabel(right),
                                    null));
        }
    }
}
