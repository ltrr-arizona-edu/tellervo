package corina.prefs.panels;

import corina.prefs.Prefs;
import corina.prefs.components.FormattingPrefComponent;
import corina.prefs.components.BoolPrefComponent; // RENAME to: Check[Box?]PrefComponent
import corina.prefs.components.ColorPrefComponent;
import corina.gui.*;
import corina.gui.layouts.DialogLayout;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Vector;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboBoxEditor;

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
        
        SpinnerComboBox scb = new SpinnerComboBox(new Integer[] { new Integer(0),
                                                                  new Integer(1),
                                                                  new Integer(2) });
        DecimalFormat df0 = new DecimalFormat("#,##0");
        DecimalFormat df1 = new DecimalFormat("#,##0 years");
        System.out.println("isParseIntegerOnly: " + df0.isParseIntegerOnly() + " " + df0.getMinimumFractionDigits());
        System.out.println("isParseIntegerOnly: " + df1.isParseIntegerOnly() + " " + df1.getMinimumFractionDigits());  
        scb.setFormats(df0, df1);
        
        top.add(scb, "test");
      
        DecimalFormat df2 = new DecimalFormat("#,##0.0##");
        DecimalFormat df3 = new DecimalFormat("#,##0.0## years");
      System.out.println("isParseIntegerOnly: " + df2.isParseIntegerOnly() + " " + df2.getMinimumFractionDigits());
              System.out.println("isParseIntegerOnly: " + df3.isParseIntegerOnly() + " " + df3.getMinimumFractionDigits());  
        
        SpinnerComboBox scb2 = new SpinnerComboBox(new Double[] { new Double(0),
                                                                        new Double(1),
                                                                        new Double(2) });
        scb2.setFormats(df2, df3);
       
        top.add(scb2, "test2");  
        
        
        // put it all together
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(top);
        add(bottom);
    }
    
    private static class SpinnerComboBoxEditor extends BasicComboBoxEditor {
      private JSpinner spinner = new JSpinner(/*new SpinnerNumberModel(0.0d, 0.0d, Double.MAX_VALUE, 0.01d)*/);
      
      public SpinnerComboBoxEditor() {
        spinner.setBorder(BorderFactory.createEmptyBorder());
        /*System.out.println(((JSpinner.NumberEditor) spinner.getEditor()).getFormat().toPattern());
        System.out.println(((JSpinner.NumberEditor) spinner.getEditor()).getTextField().getFormatter());
        //spinner.setEditor(new JSpinner.NumberEditor(spinner, "#,##0.0##"));
        System.out.println(((JSpinner.NumberEditor) spinner.getEditor()).getFormat().toPattern());
        System.out.println(((JSpinner.NumberEditor) spinner.getEditor()).getTextField().getFormatter());
        //((javax.swing.text.NumberFormatter) ((JSpinner.NumberEditor) spinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(true);
        */
      }
      
      public JSpinner getSpinner() {
        return spinner;
      }
      
      public Component getEditorComponent() {
        /*System.out.println(spinner.getEditor());
        System.out.println(((JSpinner.NumberEditor) spinner.getEditor()).getFormat().toPattern());
        System.out.println(spinner.getModel());*/
        return spinner; 
      }

      public Object getItem() {
        System.out.println("Getting spinner value: " + spinner.getValue());
        return spinner.getValue();
      }
      public void setItem(Object anObject) {
        System.out.println(anObject.getClass());
        if (anObject instanceof String) {
          String theObject = (String) anObject;
          if ("any".equals(theObject)) anObject = new Integer(1);
          else if (theObject.endsWith(" years")) {
            anObject = new Integer(theObject.substring(0, theObject.length() - " years".length()));
          }
        }
        spinner.setValue(anObject);
        //System.out.println("Setting spinner value and preference: " + anObject);
        // store it as corina.cross.overlap
        //Prefs.setPref("corina.cross.overlap", String.valueOf(anObject)); 
      }      
    }
    
    /*private static class FocusableListCellRenderer implements FocusListener, ListCellRenderer {
      private ListCellRenderer internal;
      private JComboBox cb;
      private ArrayList listeners = new ArrayList(4);
      
      public FocusableListCellRenderer(JComboBox cb, ListCellRenderer internal) {
        this.internal = internal;
        this.cb = cb;
      }
      
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        Component c = internal.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        c.removeFocusListener(this);
        c.addFocusListener(this);
        /*if (isSelected) {
          cb.setEditable(true);
        } else {
          cb.setEditable(false);
        }*//*
        return c;
      }
      
      public void addFocusListener(FocusListener l) {
        listeners.add(l);  
      }
      
      public void removeFocusListener(FocusListener l) {
        listeners.remove(l);  
      }
      
      public void focusGained(FocusEvent e) {
        for (int i = 0; i < listeners.size(); i++) {
          ((FocusListener) listeners.get(i)).focusGained(e);
        }
        System.out.println(e);
      }
      public void focusLost(FocusEvent e) {
        for (int i = 0; i < listeners.size(); i++) {
          ((FocusListener) listeners.get(i)).focusLost(e);
        }
        System.out.println(e);  
      }           
    }*/
    
    // a popup, which lets the user set the value of corina.cross.overlap
    // to any of a number of presets.
    private static class OverlapPopup extends JPanel { // !!!
        // entries: any, 5, 10, 25, 50, 100, 250, 500 years.
        // this list must be in order (lowest-to-highest), and no value should
        // be less than 1 (which would make no sense).
        private final static int YEARS[] = new int[] { 1, 5, 10, 15, 20, 25, 50, 100, 250, 500 };

        public OverlapPopup() {
            // make |v| a vector of the possible values, as strings
            Vector v = new Vector();
            String[] choices = new String[YEARS.length];
            for (int i = 0; i < YEARS.length; i++) {
              choices[i] = YEARS[i] == 1 ? "any" : YEARS[i] + " years";
            }

            // make a popup
            final JComboBox popup = new JComboBox(choices);
            Component[] children = popup.getComponents();
            for (int i = 0; i < children.length; i++) {
              System.out.println(children[i]);
            }
                        
            SpinnerComboBoxEditor scbe = new SpinnerComboBoxEditor();
            final JSpinner spinner = scbe.getSpinner();
            /*scbe.getEditorComponent().addFocusListener(new FocusListener() {
              public void focusGained(FocusEvent fe) {
                System.out.println("Editorcomponent " + fe);
              }
              public void focusLost(FocusEvent fe) {
                System.out.println("Editorcomponent " + fe);
                popup.setEditable(false);
                popup.repaint();
              }
            });*/
            spinner.addChangeListener(new ChangeListener() {
              public void stateChanged(ChangeEvent e) {
                System.err.println("spinner stateChanged: " + spinner.getValue() + " " + spinner.getValue().getClass());
                Prefs.setPref("corina.cross.overlap", spinner.getValue().toString());

                popup.setSelectedItem(spinner.getValue());
                System.out.println("Selected item after setting: " + popup.getSelectedItem());
              }
            });
            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().addFocusListener(new FocusListener() {
              public void focusGained(FocusEvent fe) {
                System.out.println("textfield fained focus " + fe);
              }
              public void focusLost(FocusEvent fe) {
                System.out.println("textfield lost focus " + fe);
                SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                    System.out.println("Setting editable to false.");
                    popup.setEditable(false);
                    popup.repaint();
                  }
                });
              }
            });

            popup.setEditor(scbe);
            popup.addFocusListener(new FocusAdapter() {
              public void focusLost(FocusEvent fe) {
                System.out.println("Combobox lost focus");
                if (!SwingUtilities.isDescendingFrom(fe.getOppositeComponent(), popup)) {
                  System.out.println("Combobox lost focus to non-child component");
                  //popup.setEditable(false);
                  //popup.repaint();
                }
              }
              
              public void focusGained(FocusEvent fe) {
                System.out.println("Combobox gained focus");
              }
            });
            
            popup.addMouseListener(new MouseAdapter() {
              public void mouseClicked(MouseEvent me) {
                System.out.println("mouse clicked " + me.getSource() + ", setting editable to true");
                SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                    popup.setEditable(true);
                    popup.repaint();
                  }
                });
              }
            });

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
            boolean found = false;
            for (int i=0; i<YEARS.length; i++)
                if (YEARS[i] == value) {
                    found = true;
                    popup.setSelectedIndex(i);
                }
            if (!found) {
              System.out.println("Setting custom value: " + new Integer(value));
              popup.setEditable(true);
              popup.setSelectedItem(new Integer(value));
              popup.setEditable(false);
            }
            
          //popup.setEditable(true);
          add(popup);

          // add listener
          popup.addActionListener(new AbstractAction() {
              public void actionPerformed(ActionEvent e) {
                  // figure out what got selected
                  int i = popup.getSelectedIndex();
                  System.out.println("Selected index: " + i);
                  if (i > -1) {
                    int n = YEARS[i];
                    System.out.println("Selected value: " + n);
                    // store it as corina.cross.overlap
                    Prefs.setPref("corina.cross.overlap", String.valueOf(n));
                    System.out.println("popup is editable: " + popup.isEditable());
                  }

                  popup.setEditable(true);
              }
          });
        }
    }

}
