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

package corina.gui;

import java.awt.Dimension;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;

public class ConfirmSave extends JDialog {

    // todo:
    // - move to gui.util?
    // - i18n
    // - frame as param?
    // - javadoc

    public static void showDialog(SaveableDocument doc) {
	// construct a prompt
	String prompt1 = "Do you want to save the changes you made in";
	String prompt2 = "the document \"" + doc.getDocumentTitle() + "\"?"; // ack!
	// String prompt3 = "Your changes will be lost if you don't save them.";

	// dialog
	final JDialog dlg = new JDialog((JFrame) doc, true);
	dlg.setTitle("Save Changes?");
	final JFrame glue = (JFrame) doc;

	// center it in its frame -- doesn't quite work (?)
	Dimension docsize = ((JFrame) doc).getSize();
	Dimension size = dlg.getSize(); // dialog size
	dlg.setLocationRelativeTo((JFrame) doc); // this is the default, right?
	dlg.setLocation(docsize.width/2 - size.width/2, docsize.height/2 - size.height/2);

	// button: dont save
	JButton dontSave = new JButton("Don't Save"); // dispose
	dontSave.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // close me
		    dlg.dispose();

		    // close document
		    glue.dispose();
		}
	    });

	// button: cancel
	JButton cancel = new JButton("Cancel");
	cancel.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // just close
		    dlg.dispose();
		}
	    });

	// button: save
	JButton save = new JButton("Save"); // save,dispose
	save.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // close me
		    dlg.dispose();

		    // save, then close document
		    ((SaveableDocument) glue).save();
		    glue.dispose();
		}
	    });

	// text chunk
	JPanel text = new JPanel();
	text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
	text.add(new JLabel(prompt1));
	text.add(new JLabel(prompt2));

	// layout/buttons: left-to-right
	JPanel buttons = new JPanel();
	buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
	buttons.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
	buttons.add(dontSave);
	buttons.add(Box.createHorizontalGlue());
	buttons.add(cancel);
	buttons.add(Box.createHorizontalStrut(12));
	buttons.add(save);

	// layout: box (top-to-bottom)
	Container cont = dlg.getContentPane();
	cont.add(Box.createVerticalStrut(12), BorderLayout.NORTH);
	cont.add(Box.createHorizontalStrut(12), BorderLayout.WEST);
	cont.add(Box.createHorizontalStrut(12), BorderLayout.EAST);
	cont.add(text, BorderLayout.CENTER);
	cont.add(buttons, BorderLayout.SOUTH);

	// save is default, no resizing
	dlg.getRootPane().setDefaultButton(save);
	dlg.setResizable(false);

	// pack, show
	dlg.pack();
	dlg.show();
    }
}
