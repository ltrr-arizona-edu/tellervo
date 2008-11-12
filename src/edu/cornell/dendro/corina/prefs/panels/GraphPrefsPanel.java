package edu.cornell.dendro.corina.prefs.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.prefs.components.BoolPrefComponent;
import edu.cornell.dendro.corina.prefs.components.ColorPrefComponent;

public class GraphPrefsPanel extends Container {
  public GraphPrefsPanel() {
    setLayout(new BorderLayout());
    
    Container co = new Container();
    co.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridy = 0;
    gbc.insets = new Insets(2, 2, 2, 2);
    
    Component c = new ColorPrefComponent("corina.graph.background");
    JLabel l = new JLabel("Background color:");
    co.add(l, gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    co.add(c, gbc);
    
    
    c = new ColorPrefComponent("corina.graph.foreground");
    l = new JLabel("Axis/cursor color:");
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.weightx = 0;
    co.add(l, gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    co.add(c, gbc);
    
    c = new BoolPrefComponent("Draw sapwood with thicker line", "corina.graph.sapwood");
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.weightx = 1;
    gbc.gridwidth = 2;
    co.add(c, gbc);
          
    c = new BoolPrefComponent("Draw indexes with dotted line", "corina.graph.dotindexes");
    gbc.gridy++;
    co.add(c, gbc);

    
    c = new BoolPrefComponent("Draw baselines", "corina.graph.baselines");
    gbc.gridy++;
    co.add(c, gbc);
    
    c = new BoolPrefComponent("Draw graphpaper", "corina.graph.graphpaper");
    gbc.gridy++;
    co.add(c, gbc);

    c = new ColorPrefComponent("corina.graph.graphpaper.color");
    l = new JLabel("Graphpaper color:");
    gbc.gridy++;
    gbc.gridwidth = 1;
    gbc.weightx = 0;
    co.add(l, gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    co.add(c, gbc);
    
    // WRITEME: pixels-per-year, as empty string -- no, as slider
    // "narrower" = 2, "wider" = 20? 30? 40?
    c = new HorizontalScaleSlider("corina.graph.pixelsperyear",
                                  "Narrower", "Wider", 2, 20, 10);
    l = new JLabel("Horizontal scale:");
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.weightx = 0;
    co.add(l, gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    co.add(c, gbc);

    // WRITEME: pixels-per-year, as empty string -- no, as slider
    // "narrower" = 2, "wider" = 20? 30? 40?
    c = new HorizontalScaleSlider("corina.graph.pixelspertenunit",
                                  "Narrower", "Wider", 2, 20, 10);
    l = new JLabel("Vertical scale:");
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.weightx = 0;
    co.add(l, gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    co.add(c, gbc);
    
    add(co, BorderLayout.NORTH);
  }
    
    // TODO: rename to simply SliderPrefComponent, document, and extract
    private static class HorizontalScaleSlider extends JPanel {
        HorizontalScaleSlider(String preference,
			      String left, String right, int min, int max,
			      int defaultValue) {

            int value = defaultValue;
            try {
                value = Integer.parseInt(App.prefs.getPref(preference));
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
                      App.prefs.setPref(pref, String.valueOf(slider.getValue()));
                    }
                }
            });

            add(Layout.borderLayout(slider,
                                    new JLabel(left), null, new JLabel(right),
                                    null));
        }
    }
}
