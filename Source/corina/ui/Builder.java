package corina.ui;

import corina.util.Platform;

import java.util.ResourceBundle;

import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

// TODO: if on 1.4, use setDisplayedMnemonicIndex() so "Save &As..." underlines the correct A

public class Builder {

    public static JMenu makeMenu(String key) {
	JMenu m = new JMenu();

	// TODO: set font only on java<1.4?
	if (!Platform.isMac && System.getProperty("corina.menubar.font")!=null)
	    m.setFont(Font.getFont("corina.menubar.font"));

	m.setText(I18n.getText(key));

	if (!Platform.isMac) {
	    Character mnemonic = I18n.getMnemonic(key);
	    if (mnemonic != null)
		m.setMnemonic(mnemonic.charValue());
	}

	return m;
    }

    public static JMenuItem makeMenuItem(String key) {
	JMenuItem m = new JMenuItem("");

	// TODO: set font only on java<1.4?
	if (!Platform.isMac && System.getProperty("corina.menubar.font")!=null)
	    m.setFont(Font.getFont("corina.menubar.font"));

	m.setText(I18n.getText(key));

	if (!Platform.isMac) {
	    Character mnemonic = I18n.getMnemonic(key);
	    if (mnemonic != null)
		m.setMnemonic(mnemonic.charValue());
	}

	String keystroke = I18n.getKeyStroke(key);
	if (keystroke != null)
	    m.setAccelerator(KeyStroke.getKeyStroke(keystroke));

	return m;
    }

    public static JButton makeButton(String key) {
	JButton b = new JButton();

	b.setText(I18n.getText(key));

	if (!Platform.isMac) {
	    Character mnemonic = I18n.getMnemonic(key);
	    if (mnemonic != null)
		b.setMnemonic(mnemonic.charValue());
	}

	return b;
    }

    public static JRadioButton makeRadioButton(String key) {
	JRadioButton r = new JRadioButton();

	r.setText(I18n.getText(key));

	if (!Platform.isMac) {
	    Character mnemonic = I18n.getMnemonic(key);
	    if (mnemonic != null)
		r.setMnemonic(mnemonic.charValue());
	}

	return r;
    }
}
