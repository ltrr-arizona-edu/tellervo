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

package corina.prefs;

import corina.gui.UserCancelledException;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.AbstractAction;
import javax.swing.SwingConstants;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class FontChooser extends JDialog {

    private String _name;
    private int _style;
    private int _size=12;

    private Font myFont;

    private JTextField preview;

    JCheckBox plainCheck, boldCheck, italicCheck;

    private boolean cancel=false;

    private void update() {
	// font
	Font myFont = new Font(_name, _style, _size);

	// update preview text
	preview.setFont(myFont);
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
	final String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	JList nameList = new JList(fonts);
	for (int i=0; i<fonts.length; i++) // hack -- pick the font name
	    if (fonts[i].compareToIgnoreCase(_name) == 0)
		nameList.setSelectedIndex(i);
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
	nameLabel.setDisplayedMnemonic('N');
	nameLabel.setLabelFor(nameList);
	nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

	// style components: plain
	plainCheck = new JCheckBox("Plain", _style == Font.PLAIN);
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
	JPanel names = new JPanel();
	names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));
	names.add(nameLabel);
	names.add(nameScroller);

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

	JPanel buttons = new JPanel();
	buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
	buttons.add(cancelButton);
	buttons.add(okButton);

	JPanel pre = new JPanel();
	pre.setLayout(new BorderLayout());
	pre.add(preview, BorderLayout.CENTER);

	JPanel box = new JPanel();
	box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
	box.add(Box.createVerticalStrut(16));
	box.add(top);
	box.add(Box.createVerticalStrut(16));
	box.add(pre);
	box.add(Box.createVerticalStrut(16));

	// (spacers on the sides)
	getContentPane().add(Box.createHorizontalStrut(24), BorderLayout.WEST);
	getContentPane().add(Box.createHorizontalStrut(24), BorderLayout.EAST);

	getContentPane().add(box, BorderLayout.CENTER);
	getContentPane().add(buttons, BorderLayout.SOUTH);
	getRootPane().setDefaultButton(okButton);

	// show
	update();
	pack();
	setResizable(false);
	setModal(true);
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
