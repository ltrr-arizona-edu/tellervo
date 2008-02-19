package edu.cornell.dendro.corina.util;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Enumeration;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.Component;

// a window which displays all of the system properties.
//
// (should this be a singleton-window, like prefs and about?)
//
// where to put this?
// -- in a "troubleshooting" button in the about-box?
// -- in a hidden button in the about-box?
// -- as Help -> Troubleshooting or Help -> System Information?
//
// TODO:
// -- rename to SystemPropertiesWindow or JavaPropertiesWindow
// -- bug? doesn't seem to update when a property is change and a new PW is created (but it should!)
// - javadoc me
// - make columns not draggable
// - allow copying (right-click?  cmd-C?)
// - i18n?
// - distinguish between or separate standard properties, extra system properties, and corina properties?

public class PropertiesWindow extends JDialog {
  private static PropertiesWindow instance;

  private static class Property implements Comparable {
  	private String key, value;
    private Property(String key, String value) {
	    this.key = key;
	    this.value = escape(value);
	  }
    private static String escape(String s) {
      StringBuffer output = new StringBuffer();
      for (int i=0; i<s.length(); i++) {
    	  if (s.charAt(i) == '\n')
    	    output.append("\\n");
    	  else
    	    output.append(s.charAt(i));
      }
      return output.toString();
    }
    public int compareTo(Object o) {
      Property p2 = (Property) o;
      // (keys are unique, so i don't need to worry about a secondary sort by values)
      return key.compareTo(p2.key);
    }
  }

  private static class PropertiesTableModel extends AbstractTableModel {
	  private List props;
    private Properties properties;
    private PropertiesTableModel(Properties properties) {
	    // initialize from properties
      this.properties = properties;
      props = new ArrayList();
      init();
	  }
    public synchronized void init() {
      props.clear();
      Enumeration e = properties.propertyNames();
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        Property p = new Property(key, System.getProperty(key));
        props.add(p);
      }
      Collections.sort(props);
      fireTableDataChanged();
    }
  	public int getRowCount() {
  	  return props.size();
  	}
  	public int getColumnCount() {
  	  return 2;
  	}
	  public Object getValueAt(int row, int column) {
	    Property p = (Property) props.get(row);
	    return (column==0 ? p.key : p.value);
	  }
	  @Override
	public String getColumnName(int column) {
	    return (column==0 ? "Property" : "Value");
	  }
  }
  
  private synchronized static PropertiesWindow getInstance() {
    if (instance == null) instance = new PropertiesWindow();
    return instance;
  }
  
  public synchronized static void showPropertiesWindow() {
    PropertiesWindow pw = getInstance();
    pw.refresh();
    pw.show();
  }

  private PropertiesTableModel model;
  public PropertiesWindow() {
	  super((JFrame) null, "System Properties");
	  //setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    model = new PropertiesTableModel(System.getProperties());
	  JTable table = new JTable(model);

	  JScrollPane sp = new JScrollPane(table);
    sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	  sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	  JLabel label = new JLabel("Here are your system properties:");
    label.setHorizontalAlignment(SwingConstants.CENTER);
	  label.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));

    Component leftMargin = Box.createHorizontalStrut(14);
    Component rightMargin = Box.createHorizontalStrut(14);
    Component bottomMargin = Box.createVerticalStrut(20);
    
    //setContentPane(Layout.borderLayout(label, leftMargin, sp, rightMargin, bottomMargin));
    
    getContentPane().add(label, BorderLayout.NORTH);
    getContentPane().add(sp, BorderLayout.CENTER);

	  pack(); //setSize(400, 500);
    Center.center(this);
  }
  
  public void refresh() {
    model.init();
  }
}
