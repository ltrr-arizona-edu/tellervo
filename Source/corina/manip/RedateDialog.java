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

package corina.manip;

import corina.Year;
import corina.Range;
import corina.Sample;
import corina.gui.Layout;
import corina.gui.layouts.DialogLayout;
import corina.util.OKCancel;
import corina.util.Platform;
import corina.ui.Builder;
import corina.ui.I18n;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/**
   A dialog which enables the user to redate a sample.  You can redate
   either one that has already been loaded or a file on disk.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class RedateDialog extends JDialog {
    private Sample sample;
    private Range range;
    private boolean isAbsolute;

    private JTextField startField, endField;
    private DocumentListener startListener, endListener;

    private class StartListener implements DocumentListener {
	public void changedUpdate(DocumentEvent e) { update(); }
	public void insertUpdate(DocumentEvent e)  { update(); }
	public void removeUpdate(DocumentEvent e)  { update(); }

	private void update() {
	    // disable EndListener
	    endField.getDocument().removeDocumentListener(endListener);

	    // get text
	    String value = startField.getText();

	    // if it's the same, do nothing -- (not worth the code)

	    // set the range
	    try {
		range = range.redateStartTo(new Year(value));
		endField.setText(range.getEnd().toString());
	    } catch (NumberFormatException nfe) {
		endField.setText(I18n.getText("bad_year"));
	    }

	    // re-enable endListener
	    endField.getDocument().addDocumentListener(endListener);
	}
    }

    private class EndListener implements DocumentListener {
	public void changedUpdate(DocumentEvent e) { update(); }
	public void insertUpdate(DocumentEvent e)  { update(); }
	public void removeUpdate(DocumentEvent e)  { update(); }

	private void update() {
	    // disable StartListener
	    startField.getDocument().removeDocumentListener(startListener);

	    // get text
	    String value = endField.getText();

	    // if it's the same, do nothing -- (not worth the code)

	    // set the range
	    try {
		range = range.redateEndTo(new Year(value));
		startField.setText(range.getStart().toString());
	    } catch (NumberFormatException nfe) {
		startField.setText(I18n.getText("bad_year"));
	    }

	    // re-enable startListener
	    startField.getDocument().addDocumentListener(startListener);
	}
    }

    /**
       Create a redater for a loaded sample.  The "OK" button will
       fire a <code>sampleRedated</code> event to update other views.

       @param s the sample to redate
    */
    public RedateDialog(Sample s, JFrame owner) {
	// modal
	super(owner);
	setModal(true);

	// get sample
	sample = s;

	// pass
	setup();

	// all done
	pack();
	// (resize to make 50% wider here?)
	setResizable(false);
	show();
	endField.requestFocus();
    }

    private void setup() {
        // set title
        setTitle(I18n.getText("redate"));

        // kill me when i'm gone
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // grab data
        range = sample.range;
        isAbsolute = sample.isAbsolute();

        // dialog is a boxlayout
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        setContentPane(p);

        // controls go in a dialoglayout
        JPanel controls = new JPanel(new DialogLayout());
        p.add(controls);

        // old range
        // controls.add(new JLabel(sample.range.toString()), I18n.getText("old_range") + ":");
        // controls.add(new JLabel(I18n.getText("old_range") + " was " + sample.range));

        // redate --------------------------------------------------
        JPanel rangePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        startListener = new StartListener();
        startField = new JTextField(sample.range.getStart().toString(), 5);
        startField.getDocument().addDocumentListener(startListener);

        endListener = new EndListener();
        endField = new JTextField(sample.range.getEnd().toString(), 5);
        endField.getDocument().addDocumentListener(endListener);

        // can't do this with dialoglayout! -- firstLabel.setLabelFor(endField); // :-)

        rangePanel.add(startField);
        rangePanel.add(new JLabel(" - "));
        rangePanel.add(endField);

        controls.add(rangePanel, I18n.getText("new_range") + ":");

        // dating --------------------------------------------------
	// TODO: add builder for radiobuttons?
        ButtonGroup datingGroup = new ButtonGroup();
        JRadioButton relButton = Builder.makeRadioButton("relative");
	relButton.setSelected(!isAbsolute);
        final JRadioButton absButton = Builder.makeRadioButton("absolute");
	absButton.setSelected(isAbsolute);

        // on click (either radiobutton), set absolute (re-use listener)
        ActionListener absListener = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                isAbsolute = (e.getSource() == absButton);
            }
        };
        relButton.addActionListener(absListener);
        absButton.addActionListener(absListener);

        datingGroup.add(relButton);
        datingGroup.add(absButton);
        controls.add(relButton, "Dating:");
        controls.add(Box.createVerticalStrut(4), null); // (8)
        controls.add(absButton, "");

        // 22-jul-2002: maryanne says this might be useful:
        final JCheckBox elementsToo = new JCheckBox("Also redate all elements");
        controls.add(Box.createVerticalStrut(8), null);
        controls.add(elementsToo, "");
	// if (sample.elements == null)
	elementsToo.setEnabled(false);
        // OUCH.  that's about as dangerous a weapon as i've ever thought about handing the lusers.
	// (permanently disabled, now)

        // buttons --------------------------------------------------

        // cancel, ok
        JButton cancel = Builder.makeButton("cancel");
        final JButton ok = Builder.makeButton("ok");

        // (listen for cancel, ok)
        ActionListener buttonListener = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                boolean isOk = (e.getSource() == ok);
                boolean rangeChanged = !sample.range.equals(range);
                boolean absRelChanged = (sample.isAbsolute() != isAbsolute);

                if (isOk && (rangeChanged || absRelChanged))
                    sample.postEdit(Redate.redate(sample, range, isAbsolute ? "A" : "R"));

                dispose();
            }
        };
        cancel.addActionListener(buttonListener);
        ok.addActionListener(buttonListener);

	// in panel
        p.add(Box.createVerticalStrut(14));
        p.add(Layout.buttonLayout(cancel, ok));

        // esc => cancel, return => ok
        OKCancel.addKeyboardDefaults(ok);
    }
}
