package corina.ui;

import corina.gui.XMenubar;
import corina.util.Platform;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

// TODO: if on 1.4, use setDisplayedMnemonicIndex() so "Save &As..." underlines the correct A

public class Builder {

    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    // ----------------------------------------
    // extract parts of a string
    // input: "&Copy [control C]"
    // text: "Copy"
    // keystroke: "control C" (may be null)
    // mnemonic: 'C' (may be null)
    public static String getText(String key) {
	StringBuffer buf = new StringBuffer();

	int n = key.length();
	boolean ignore = false;
	for (int i=0; i<n; i++) {
	    char c = key.charAt(i);
	    switch (c) {
	    case '&': continue;
	    case '[': ignore = true; break;
	    case ']': ignore = false; break;
	    default:
		if (!ignore)
		    buf.append(c);
	    }
	}

	return buf.toString().trim();
    }
    protected static String getKeyStroke(String key) {
	int left = key.indexOf('[');
	int right = key.indexOf(']');

	if (left==-1 || right==-1)
	    return null;

	String sub = key.substring(left+1, right).trim();
	return sub;
    }
    protected static Character getMnemonic(String key) {
	int amp = key.indexOf('&');

	if (amp==-1 || amp==key.length()-1)
	    return null;

	return new Character(Character.toUpperCase(key.charAt(amp+1)));
    }
    // ----------------------------------------

    public static JMenu makeMenu(String key) {
	JMenu m = new JMenu();

	if (!Platform.isMac && System.getProperty("corina.menubar.font")!=null) // TODO: set font only on java<1.4?
	    m.setFont(Font.getFont("corina.menubar.font"));

	String t = msg.getString(key);

	m.setText(getText(t));

	if (!Platform.isMac) {
	    Character mnemonic = getMnemonic(t);
	    if (mnemonic != null)
		m.setMnemonic(mnemonic.charValue());
	}

	return m;
    }

    public static JMenuItem makeMenuItem(String key) {
	JMenuItem m = new XMenubar.XMenuItem("");

	if (!Platform.isMac && System.getProperty("corina.menubar.font")!=null) // TODO: set font only on java<1.4?
	    m.setFont(Font.getFont("corina.menubar.font"));

	String t = msg.getString(key);

	m.setText(getText(t));

	if (!Platform.isMac) {
	    Character mnemonic = getMnemonic(t);
	    if (mnemonic != null)
		m.setMnemonic(mnemonic.charValue());
	}

	String keystroke = getKeyStroke(t);
	if (keystroke != null)
	    m.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize(keystroke)));
	    // REFACTOR: macize() is in XMenubar?  move that method!

	return m;
    }

    public static JButton makeButton(String key) {
	JButton b = new JButton();

	String t = msg.getString(key);

	b.setText(getText(t));

	if (!Platform.isMac) {
	    Character mnemonic = getMnemonic(t);
	    if (mnemonic != null)
		b.setMnemonic(mnemonic.charValue());
	}

	return b;
    }

    public static JRadioButton makeRadioButton(String key) {
	JRadioButton r = new JRadioButton();

	String t = msg.getString(key);

	r.setText(getText(t));

	if (!Platform.isMac) {
	    Character mnemonic = getMnemonic(t);
	    if (mnemonic != null)
		r.setMnemonic(mnemonic.charValue());
	}

	return r;
    }
}
