package corina.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import corina.util.Platform;

/**
 * A base Corina action which sets Mnemonic and Accelerator based on I18N settings.
 * @author Aaron Hamid
 */
public abstract class CorinaAction extends AbstractAction {
  public CorinaAction(String key) {
    super();
    putValue(NAME, I18n.getText(key));
    if (!Platform.isMac) {
      Character mnemonic = I18n.getMnemonic(key);
      if (mnemonic != null)
        putValue(MNEMONIC_KEY, new Integer(mnemonic.charValue()));
    }
    String keystroke = I18n.getKeyStroke(key);
    if (keystroke != null)
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(keystroke));
  }
  public void perform(Object source) {
    ActionEvent ae = new ActionEvent(source == null ? this : source,
                                     ActionEvent.ACTION_PERFORMED,
                                     (String) getValue(Action.ACTION_COMMAND_KEY));
    actionPerformed(ae);
  }
}