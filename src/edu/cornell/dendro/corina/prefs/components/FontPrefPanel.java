package corina.prefs.components;

import java.awt.FlowLayout;
import javax.swing.JComponent;

/**
 * A convenience container that contains both FontPrefComponent components.
 */
public class FontPrefPanel extends JComponent {
  private FontPrefComponent components;

  public FontPrefPanel(String pref) {
    this(new FontPrefComponent(pref));
  }
  public FontPrefPanel(FontPrefComponent components) {
    this.components = components;
    setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
    
    add(components.getLabel());
    add(components.getButton());
  }
  
  public void addNotify() {
    components.setParent(getTopLevelAncestor());
    super.addNotify();
  }
}