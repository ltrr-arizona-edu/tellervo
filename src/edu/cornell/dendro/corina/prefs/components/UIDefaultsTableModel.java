package edu.cornell.dendro.corina.prefs.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.logging.CorinaLog;

public class UIDefaultsTableModel extends AbstractTableModel {
  private static final CorinaLog log = new CorinaLog(UIDefaultsTableModel.class);
  
  private Object[][] data;
  private Component component;

  public void setComponent(Component comp) {
    component = comp;
  }

  public UIDefaultsTableModel() {
    this(UIManager.getDefaults());
  }

  public UIDefaultsTableModel(Hashtable defaults) {
    init(defaults);
  }
  
  public void init(Hashtable defaults) {
    ArrayList list = new ArrayList(defaults.size());
    Enumeration e = defaults.keys();
    while (e.hasMoreElements()) {
      Object key = e.nextElement();
      Object value = defaults.get(key);
      
      /*if (key.toString().toLowerCase().startsWith("menu")) {
        log.debug("uidefault " + key.getClass() + " '" + key + "' " + value);
      }*/    
      
      if (value == null) continue;
      
      if (!(value instanceof javax.swing.plaf.ColorUIResource) &&
          !(value instanceof javax.swing.plaf.FontUIResource)) continue;
      
      StringBuffer name = new StringBuffer(key.toString());
      boolean lastWasSpace = false; 
      for (int i = 0; i < name.length(); i++) {
        char c = name.charAt(i);
        if (c == '.') {
          name.setCharAt(i, ' ');
          lastWasSpace = false;
        } else if (Character.isWhitespace(c)) {
          lastWasSpace = true; 
        } else if (lastWasSpace) {
          name.setCharAt(i, Character.toUpperCase(c));
        }
      }
      
      list.add(new Object[] { key, name.toString(), value });
    }

    data = (Object[][]) list.toArray(new Object[list.size()][2]);

    Arrays.sort(data, new Comparator() {
      public int compare(Object o1, Object o2) {
        return ((String) ((Object[]) o1)[1]).compareTo((String) ((Object[]) o2)[1]);
      }
    });
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    log.trace("isCellEditable " + columnIndex + " " + (columnIndex == 1));
    return columnIndex == 1;
  }
  
  public void setValueAt(Object value, int row, int col) {
    Object original = data[row][col + 1];
    log.trace("value: " + value);
    log.trace("original: " + value);
    if (original instanceof ColorUIResource) {
      data[row][col + 1] = new ColorUIResource((Color) value);
      UIManager.getDefaults().put(data[row][0], data[row][2]);
    } else {
      data[row][col + 1] = new FontUIResource((Font) value);
      UIManager.getDefaults().put(data[row][0], data[row][2]);
    }    
    
    fireTableCellUpdated(row, col);
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Frame[] frames = Frame.getFrames();
        if (frames == null) return;
        for (int i = 0; i < frames.length; i++) {
          SwingUtilities.updateComponentTreeUI(frames[i]);
        }        
      }
    });
  }
  
  public synchronized void reset() {
    UIDefaults uidefaults = UIManager.getDefaults();
    
    CorinaLog.realErr.println("Clearing UIDefaults preferences");
    uidefaults.clear();
    log.debug("UIDEFAULTS size: " + App.prefs.getUIDefaults().size());
    uidefaults.putAll(App.prefs.getUIDefaults());
        
    /*Enumeration e = Prefs.UIDEFAULTS.keys();
    while (e.hasMoreElements()) {
      Object key = e.nextElement();
      CorinaLog.realErr.println("putting " + key);
      uidefaults.put(key, Prefs.UIDEFAULTS.get(key));    
    }*/
    Set keyset = App.prefs.getPrefs().keySet();
    Iterator it = keyset.iterator();
    // must do this in two stages to avoid concurrent modification errors
    ArrayList keylist = new ArrayList(keyset.size());
    while (it.hasNext()) {
      String key = it.next().toString();
      if (key.startsWith("uidefaults.")) {
        CorinaLog.realErr.println("Found UIDefaults preference: " + key);
        keylist.add(key);
      }
    }
    
    // now remove the prefs
    it = keylist.iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      CorinaLog.realErr.println("Removing UIDefaults preference: " + key);
      App.prefs.removePref(key);
    }
    
    App.prefs.getPrefs().list(System.out);
        
    init(uidefaults);
    
    fireTableDataChanged();
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Frame[] frames = Frame.getFrames();
        if (frames == null) return;
        for (int i = 0; i < frames.length; i++) {
          SwingUtilities.updateComponentTreeUI(frames[i]);
        }        
      }
    });
  }

  public String getColumnName(int columnIndex) {
    if (columnIndex == 0)
      return "Property";
    else
      return "Value";
  }

  public Class getColumnClass(int c) {
    log.trace("getColumnClass: " + c);
    if (c == 0) return String.class;
    else return UIDefaultsTableModel.class;
  }

  public int getRowCount() {
    return data.length;
  }
  
  public int getColumnCount() {
    return 2;
  }
  
  public Object getValueAt(int row, int column) {
    return data[row][column + 1];
  }
  
  public String getProperty(int row) {
    return data[row][0].toString();
  }
}
