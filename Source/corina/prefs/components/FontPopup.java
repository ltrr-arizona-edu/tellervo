package corina.prefs.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

import corina.core.App;

// UNUSED!
public class FontPopup extends JComboBox {

    static class FontLabelRenderer extends JLabel implements ListCellRenderer {
	public FontLabelRenderer() {
	    setOpaque(true);
	}
	public Component getListCellRendererComponent(JList list, Object value,
						      int index,
						      boolean isSelected, boolean cellHasFocus) {
	    if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    } else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }

	    String font = (String) value;
	    setFont(new Font(font, Font.PLAIN, 12));
	    setText(font);

	    return this;
	}
    }

    public FontPopup(String property, JTable preview) {
	Font oldFont = App.prefs.getFontPref(property, new Font(null));
	String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	// --start hack--
	int n=0;
	for (int i=0; i<fontNames.length; i++)
	    if (fontNames[i].length() > 0)
		n++;
	String realFontNames[] = new String[n];
	int m=0;
	for (int i=0; i<fontNames.length; i++)
	    if (fontNames[i].length() > 0)
		realFontNames[m++] = fontNames[i];
	fontNames = realFontNames;
	// --end hack--
	for (int i=0; i<fontNames.length; i++) {
	    addItem(fontNames[i]);
	    if (fontNames[i].equalsIgnoreCase(oldFont.getName())) {
		setSelectedIndex(i);
		setFont(new Font(fontNames[i], Font.PLAIN, 12));
	    }
	}

	// render each in its own font
	setRenderer(new FontLabelRenderer());

	// (h)ack!
	final JTable glue = preview;

	// the combobox (i.e., the selected value) is in the selected
	// font, as well
	addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    String font = (String) ((JComboBox) e.getSource()).getSelectedItem();
		    /*
		      setFont(new Font(font, Font.PLAIN, 12));
		    */

		    // update preview, too
		    int size = glue.getFont().getSize();
		    glue.setFont(new Font(font, Font.PLAIN, size));
		}
	    });
    }

}
