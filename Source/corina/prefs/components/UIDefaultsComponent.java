package corina.prefs.components;

import java.awt.Color;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import corina.util.CorinaLog;
import corina.prefs.Prefs;

public class UIDefaultsComponent extends Container implements TableModelListener, ActionListener {
  private static final CorinaLog log = new CorinaLog("UIDefaultsComponent");
  
  private UIDefaultsTableModel model = new UIDefaultsTableModel();
  
  public UIDefaultsComponent() {
    setLayout(new BorderLayout());
    
    Container c = new Container();
    c.setLayout(new FlowLayout());
    
    JButton reset = new JButton("Reset");
    reset.addActionListener(this);
    
    c.add(reset);
    
    model.addTableModelListener(this);
    
    JTable table = new JTable(model);
    model.setComponent(table);
    
    table.setDefaultRenderer(UIDefaultsTableModel.class, new UIDefaultsRenderer(true));
    table.setDefaultEditor(UIDefaultsTableModel.class, new UIDefaultsEditor());
    
    add(c, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
  }
  
  public synchronized void reset() {
    model.reset();
  }
    
  private static final String stringifyFont(Font f) {
    StringBuffer sb = new StringBuffer();
    sb.append(f.getFontName());
    sb.append('-');
    int s = sb.length();
    if ((f.getStyle() & Font.BOLD) != 0) {
      sb.append("BOLD");
    }
    if ((f.getStyle() & Font.ITALIC) != 0) {
      sb.append("ITALIC");
    }
    if (sb.length() > s) {
      sb.append('-');
    }
    sb.append(f.getSize());
    return sb.toString();
  }

  public void tableChanged(TableModelEvent event) {
    if (event.getType() != TableModelEvent.UPDATE) return;

    if (event.getFirstRow() == 0 && event.getLastRow() == Integer.MAX_VALUE) {
      log.debug("table reinitialized");
      return;
      /*Properties p = Prefs.getPrefs();
      
      Iterator it = p.keySet().iterator();
      ArrayList uidefaultslist = new ArrayList(p.size());
      while (it.hasNext()) {
        String key = it.next().toString();
        if (key.startsWith("uidefaults.")) {
          log.debug("found pref to remove " + key);
          uidefaultslist.add(key);
        }           
      }
      
      it = uidefaultslist.iterator();
      while (it.hasNext()) {
        String pref = (String) it.next();
        log.debug("Removing pref " + pref);
        Prefs.removePref(pref);
      }
      
      return;*/
    }
  
    UIDefaultsTableModel model = (UIDefaultsTableModel) event.getSource(); 
    String property = model.getProperty(event.getFirstRow());
  
    log.debug("setting uidefaults." + property);
  
    Object o = model.getValueAt(event.getFirstRow(), event.getColumn());
  
    if (o instanceof Font) {
      log.debug("setting font " + o);
      Prefs.setPref("uidefaults." + property, stringifyFont((Font) o));
    } else if (o instanceof Color) {
      log.debug("setting font " + o);
      Prefs.setPref("uidefaults." + property, "#" + Integer.toHexString(((Color) o).getRGB() & 0x00ffffff));
    } else {
      log.error("unknown new value type (not Color or Font)! " + o);
    }
  }
  
  public void actionPerformed(ActionEvent ae) {
    reset();
  }  
}
