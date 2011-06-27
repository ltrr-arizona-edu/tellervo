/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
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

package edu.cornell.dendro.corina.gui;

import edu.cornell.dendro.corina.util.OKCancel;
// import edu.cornell.dendro.corina.util.JLinedLabel; -- FUTURE
import edu.cornell.dendro.corina.ui.Builder;

import java.awt.Dimension;
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

@SuppressWarnings("serial")
public class ConfirmSave extends JDialog {
    // don't instantiate me
    private ConfirmSave() { }

    // todo:
    // - move to gui.util?
    // - (more) i18n
    // - frame as param?
    // - javadoc

    public static void showDialog(SaveableDocument doc) {
	// construct a prompt
	String prompt1 = "Do you want to save the changes you made in";
	String prompt2 = "the document \"" + doc.getDocumentTitle() + "\"?"; // ack!
	// String prompt3 = "Your changes will be lost if you don't save them.";  (smaller?)
 // REFACTOR: create a multi-line jlabel, and make this a messageformat

	// dialog
	final JDialog dlg = new JDialog((JFrame) doc, true);
	dlg.setTitle("Save Changes?");
	final JFrame glue = (JFrame) doc;

	// center it in its frame -- doesn't quite work (?)
	Dimension docsize = ((JFrame) doc).getSize();
	Dimension size = dlg.getSize(); // dialog size
	dlg.setLocationRelativeTo((JFrame) doc); // this is the default, right?
	dlg.setLocation(docsize.width/2 - size.width/2, docsize.height/2 - size.height/2);// REFACTOR: move this code to Center.java!

	// button: dont save
	JButton dontSave = Builder.makeButton("general.dont_save"); // dispose
	dontSave.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // close me
		    dlg.dispose();

		    // close document
		    glue.dispose();
		}
	    });

	// button: cancel
	JButton cancel = Builder.makeButton("general.cancel");
	cancel.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // just close me
		    dlg.dispose();
		}
	    });

	// button: save
	JButton save = Builder.makeButton("general.saveChanges"); // save, dispose
	save.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // close me
		    dlg.dispose();

		    // save, then close document
		    ((SaveableDocument) glue).save();
		    
		    // only close the dialog if they actually went through with the save
		    if(((SaveableDocument) glue).isSaved())
		    	glue.dispose();
		}
	    });

        // text chunk
	JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
	// text.add(new JLinedLabel(prompt1 + "\n" + prompt2)); // doesn't work yet: jlinedlabel centers everything
	text.add(new JLabel(prompt1));
	text.add(new JLabel(prompt2));
        text.add(Box.createVerticalStrut(10));
	// REFACTOR: just say text = Layout.boxLayoutY(prompt1, prompt2, Box.createV(10))

        // layout/buttons: left-to-right
	JPanel buttons = Layout.buttonLayout(dontSave, null, cancel, save);

        // layout: box (top-to-bottom)
	JPanel cont = Layout.borderLayout(null,
					  null, text, null,
					  buttons);
        cont.setBorder(BorderFactory.createEmptyBorder(15, 24, 20, 24)); // FIXME: too big!
        dlg.setContentPane(cont);

        // ret/esc
        OKCancel.addKeyboardDefaults(save);

        // pack, disable sizing, show
        dlg.pack();
        dlg.setResizable(false);
        dlg.setVisible(true);
    }
}
