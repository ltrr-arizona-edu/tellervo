package corina.prefs;

import java.awt.Font;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

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
	    Font font = getFont();
	    setFont(new Font(font.getName(), Font.PLAIN, size));
	    setText((String) value);

	    return this;
	}
    }

    int sizes[] = new int[] {
	8, 9, 10, 11, 12, 14, 16, 18, 20,
    };

    public FontSizePopup(String property, JTable preview) {
	Font oldFont = Font.getFont(property);
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
