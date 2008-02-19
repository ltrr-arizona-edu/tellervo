package edu.cornell.dendro.corina.prefs.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

import edu.cornell.dendro.corina.core.App;

public class FontSizePopup extends JComboBox {

    static class SizeLabelRenderer extends JLabel implements ListCellRenderer {
	public SizeLabelRenderer() {
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

	    int size = Integer.parseInt((String) value);
	    setFont(getFont().deriveFont(Font.PLAIN, size));
	    setText((String) value);

	    return this;
	}
    }

    int sizes[] = new int[] {
	8, 9, 10, 11, 12, 14, 16, 18, 20,
    };

    public FontSizePopup(String property, JTable preview) {
	Font oldFont = App.prefs.getFontPref(property, new Font(null));
	for (int i=0; i<sizes.length; i++) {
	    addItem(String.valueOf(sizes[i]));
	    if (oldFont.getSize() == sizes[i])
		setSelectedIndex(i);
	}

	// render in its size: how well does this work?  pretty darned
	// well.
	setRenderer(new SizeLabelRenderer());

	// ugh.
	final JTable glue = preview;

	// the combobox (i.e., the selected value) is in the selected
	// size, as well (this works less well)
	addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int size = Integer.parseInt((String) ((JComboBox) e.getSource()).getSelectedItem());
		    Font font = getFont();
		    /*
		      setFont(new Font(font.getName(), Font.PLAIN, size));
		    */

		    // set both the table font size, and its row
		    // height (which is why preview isn't the generic
		    // jcomponent)
		    glue.setFont(new Font(font.getName(), Font.PLAIN, size));
		    glue.setRowHeight(size + 4);
		}
	    });
    }
}
