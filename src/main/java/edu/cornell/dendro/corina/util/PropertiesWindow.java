/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.util;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.ui.Builder;

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

@SuppressWarnings("serial")
public class PropertiesWindow extends JDialog {
  private static PropertiesWindow instance;

  @SuppressWarnings("unchecked")
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
	  @SuppressWarnings("unchecked")
	private List props;
    private Properties properties;
    @SuppressWarnings("unchecked")
	private PropertiesTableModel(Properties properties) {
	    // initialize from properties
      this.properties = properties;
      props = new ArrayList();
      init();
	  }
    @SuppressWarnings("unchecked")
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
    pw.setVisible(true);
  }

  private PropertiesTableModel model;
  public PropertiesWindow() {
	  super((JFrame) null, "System Properties");
      setIconImage(Builder.getApplicationIcon());

	  //setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    model = new PropertiesTableModel(System.getProperties());
	  JTable table = new JTable(model);

	  JScrollPane sp = new JScrollPane(table);
    sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	  sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	  JLabel label = new JLabel("Here are your system properties:");
    label.setHorizontalAlignment(SwingConstants.CENTER);
	  label.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));
    
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
