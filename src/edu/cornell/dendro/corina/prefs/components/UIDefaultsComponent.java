package edu.cornell.dendro.corina.prefs.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.logging.CorinaLog;

public class UIDefaultsComponent extends JComponent implements TableModelListener, ActionListener {

  private static final long serialVersionUID = 1L;

  private static final CorinaLog log = new CorinaLog("UIDefaultsComponent");
  
  private UIDefaultsTableModel model = new UIDefaultsTableModel();
  
  public UIDefaultsComponent() {
    setLayout(new BorderLayout());
    
    Container c = new Container();
    GridBagLayout gbl_c = new GridBagLayout();
    gbl_c.columnWidths = new int[]{212, 0};
    gbl_c.rowHeights = new int[]{25, 0};
    gbl_c.columnWeights = new double[]{0.0, Double.MIN_VALUE};
    gbl_c.rowWeights = new double[]{0.0, Double.MIN_VALUE};
    c.setLayout(gbl_c);
    
    model.addTableModelListener(this);
    
    JTable table = new JTable(model);
    model.setComponent(table);
    
    table.setDefaultRenderer(UIDefaultsTableModel.class, new UIDefaultsRenderer(true));
    table.setDefaultEditor(UIDefaultsTableModel.class, new UIDefaultsEditor());
    
    add(c, BorderLayout.NORTH);
    
    JButton reset = new JButton("Reset fonts and colors to default");
    reset.setHorizontalAlignment(SwingConstants.LEFT);
    reset.addActionListener(this);
    
    GridBagConstraints gbc_reset = new GridBagConstraints();
    gbc_reset.anchor = GridBagConstraints.NORTHWEST;
    gbc_reset.gridx = 0;
    gbc_reset.gridy = 0;
    c.add(reset, gbc_reset);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setEnabled(false);
    add(scrollPane, BorderLayout.CENTER);
  }
  
  public synchronized void reset() {
    model.reset();
  }
    
  static final String stringifyFont(Font f) {
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
      App.prefs.setPref("uidefaults." + property, stringifyFont((Font) o));
    } else if (o instanceof Color) {
      log.debug("setting font " + o);
      App.prefs.setPref("uidefaults." + property, "#" + Integer.toHexString(((Color) o).getRGB() & 0x00ffffff));
    } else {
      log.error("unknown new value type (not Color or Font)! " + o);
    }
  }
  
  public void actionPerformed(ActionEvent ae) {
    reset();
  }  
}
