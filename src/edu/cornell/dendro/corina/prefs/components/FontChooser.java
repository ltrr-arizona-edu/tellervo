//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.prefs.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.util.Center;

/*
  TODO:
  -- sort fonts by name
  -- show fonts in that font
  -- scroll list to old selection

  -- make resizable?
  -- refactor layouts more

  -- sun refuses to add native font-chooser support to java, so
     document that here (bug id#) and add hooks to tie into native
     font choosers.

  -- document this class.  it needs it.

  -- internationalize, and use Builder where possible
  
  -- javadoc!
*/

public class FontChooser extends JDialog {

    private String _name;
    private int _style;
    private int _size=12;

    private JTextField preview;

    private JCheckBox plainCheck, boldCheck, italicCheck;

    private boolean cancel=false;

    private void update() {
	// update preview text
	preview.setFont(new Font(_name, _style, _size));
    }

    private FontChooser(Frame owner, String title, String oldFont) {
	// dialog-stuff
	super(owner, title, true);

	// extract
	{
	    Font myFont = Font.decode(oldFont);
	    _name = myFont.getName();
	    _style = myFont.getStyle();
	    _size = myFont.getSize();
	}

	// name component
        String allFonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        // make fonts[], like allFonts[], but for each name[0] is-a letter.
        int numberOfFonts = 0;
        for (int i=0; i<allFonts.length; i++)
            if (Character.isLetter(allFonts[i].charAt(0)))
                numberOfFonts++;
        final String fonts[] = new String[numberOfFonts];
        int currentFont = 0;
        for (int i=0; i<allFonts.length; i++)
            if (Character.isLetter(allFonts[i].charAt(0)))
                fonts[currentFont++] = allFonts[i];
	// WAS: final String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
	JList nameList = new JList(fonts);
	for (int i=0; i<fonts.length; i++) // hack -- pick the font name
	    if (fonts[i].compareToIgnoreCase(_name) == 0)
		nameList.setSelectedIndex(i);
	nameList.setCellRenderer(new DefaultListCellRenderer() {
		public Component getListCellRendererComponent(JList list,
							      Object value,
							      int index,
							      boolean isSelected,
							      boolean cellHasFocus)
		{
		    String s = value.toString();
		    setText(s);
		    if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		    }
		    else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		    }
		    setEnabled(list.isEnabled());
		    setFont(new Font((String) value, Font.PLAIN, 12));
		    // PERF: NEW!
		    return this;
		}
	    });

	nameList.addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
		    _name = fonts[((JList) e.getSource()).getSelectedIndex()];
		    update();
		}
	    });
	nameList.setAlignmentX(Component.LEFT_ALIGNMENT);
	JScrollPane nameScroller = new JScrollPane(nameList);
	nameScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
	JLabel nameLabel = new JLabel("Name:");
	if (!App.platform.isMac())
	    nameLabel.setDisplayedMnemonic('N');
	nameLabel.setLabelFor(nameList);
	nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

	// style components: plain
	plainCheck = new JCheckBox("Plain", _style == Font.PLAIN);
	if (!App.platform.isMac())
	    plainCheck.setMnemonic('P');
	plainCheck.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    if (plainCheck.isSelected()) {
			// unselect bold, italic
			boldCheck.setSelected(false);
			italicCheck.setSelected(false);
			_style = Font.PLAIN;
			update();
		    } else {
			// can't unselect plain
			plainCheck.setSelected(true);
		    }
		}
	    });

	// style components: bold
	boldCheck = new JCheckBox("Bold", (_style & Font.BOLD) != 0);
	if (!App.platform.isMac())
	    boldCheck.setMnemonic('B');
	boldCheck.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    if (boldCheck.isSelected()) {
			// unselect plain
			plainCheck.setSelected(false);
		    } else {
			// plain?
			if (!italicCheck.isSelected())
			    plainCheck.setSelected(true);
		    }
		    _style ^= Font.BOLD;
		    update();
		}
	    });

	// style components: italic
	italicCheck = new JCheckBox("Italic", (_style & Font.ITALIC) != 0);
	if (!App.platform.isMac())
	    italicCheck.setMnemonic('I');
	italicCheck.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    if (italicCheck.isSelected()) {
			// unselect plain
			plainCheck.setSelected(false);
		    } else {
			// plain?
			if (!boldCheck.isSelected())
			    plainCheck.setSelected(true);
		    }
		    _style ^= Font.ITALIC;
		    update();
		}
	    });

	// size component
	JComboBox sizeCombo = new JComboBox(new String[] { "8", "9", "10", "12", "14", "18", "20", "24" });
	sizeCombo.setEditable(true);
	sizeCombo.setSelectedItem(String.valueOf(_size));
	sizeCombo.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
		    try {
			_size = Integer.parseInt((String) ((JComboBox) e.getSource()).getSelectedItem());
		    } catch (NumberFormatException nfe) {
			_size = 12;
		    }
		    update();
		}
	    });
	((JTextField) sizeCombo.getEditor().getEditorComponent()).addCaretListener(new CaretListener() {
		public void caretUpdate(CaretEvent e) {
		    try {
			_size = Integer.parseInt(((JTextField) e.getSource()).getText());
		    } catch (NumberFormatException nfe) {
			_size = 12;
		    }
		    update();
		}
	    });
	sizeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
	JLabel sizeLabel = new JLabel("Size:");
	if (!App.platform.isMac())
	    sizeLabel.setDisplayedMnemonic('S');
	sizeLabel.setLabelFor(sizeCombo);
	sizeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

	JLabel styleLabel = new JLabel("Style:");
	sizeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

	// buttons
	JButton okButton = new JButton("OK");
	okButton.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	    });

	JButton cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    cancel = true;
		    dispose();
		}
	    });

	// preview
	preview = new JTextField("sample text - abc XYZ 123");
	preview.setAlignmentX(Component.LEFT_ALIGNMENT);
	JLabel previewLabel = new JLabel("Preview:");
	previewLabel.setHorizontalTextPosition(SwingConstants.LEADING);
	previewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

	// layout
	JPanel names = Layout.boxLayoutY(nameLabel, nameScroller);

	JPanel styles = new JPanel();
	styles.setLayout(new BoxLayout(styles, BoxLayout.Y_AXIS));
	styles.add(styleLabel);
	styles.add(plainCheck);
	styles.add(boldCheck);
	styles.add(italicCheck);
	styles.add(Box.createVerticalStrut(8));
	styles.add(sizeLabel);
	styles.add(sizeCombo);
	styles.add(Box.createVerticalGlue());

	JPanel top = new JPanel();
	top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
	top.add(names);
	top.add(Box.createHorizontalStrut(8));
	top.add(styles);
	top.add(Box.createHorizontalStrut(8));
	// add size list

	JPanel buttons = Layout.buttonLayout(cancelButton, okButton);
	buttons.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

	// why's it wrapped in a borderlayout?
	JPanel pre = Layout.borderLayout(null,
					 null, preview, null,
					 null);

	JPanel box = new JPanel();
	box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
	box.add(Box.createVerticalStrut(16));
	box.add(top);
	box.add(Box.createVerticalStrut(16));
	box.add(pre);
	box.add(Box.createVerticalStrut(16));

	JPanel content = Layout.borderLayout(null,
					     Box.createHorizontalStrut(24),
					     box,
					     Box.createHorizontalStrut(24),
					     buttons);
	setContentPane(content);

	getRootPane().setDefaultButton(okButton);
	// REFACTOR: can use okcancel, if not for the non-trivial cancel operation

	// show
	update();
	pack();
//	setResizable(false);
	setModal(true);
        Center.center(this, owner);
	show();
    }

    private String getResult() throws UserCancelledException {
        if (cancel)
            throw new UserCancelledException();

	String style="plain";
	Font myFont = new Font(_name, _style, _size);
	if (myFont.isItalic() && myFont.isBold()) // not pretty
	    style = "BOLDitalic";
	else if (myFont.isItalic())
	    style = "italic";
	else if (myFont.isBold())
	    style = "BOLD";
	return myFont.getName() + "-" + style + "-" + myFont.getSize();
    }

    // should use Fonts instead of Strings, but who's counting?
    public static String showDialog(Frame owner, String title, String oldFont) throws UserCancelledException {
        FontChooser f = new FontChooser(owner, title, oldFont);
        return f.getResult();
    }
}
