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
import corina.gui.ButtonLayout;

import java.util.ResourceBundle;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/**
   A dialog which enables the user to redate a sample, either one that
   has already been loaded or a file on disk.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class RedateDialog extends JDialog {

    private Sample sample;
    private Range range;
    private boolean isAbsolute;

    private JTextField startField, endField;
    private DocumentListener startListener, endListener;

    private ResourceBundle msg = ResourceBundle.getBundle("RedateBundle");

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
		endField.setText(msg.getString("bad_year"));
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
		startField.setText(msg.getString("bad_year"));
	    }

	    // re-enable startListener
	    startField.getDocument().addDocumentListener(startListener);
	}
    }

    /** Create a redater for a loaded sample.  The "OK" button will
	fire a <code>sampleRedated</code> event to update other views.
	@param s the sample to redate */
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
	setResizable(false);
	show();
	endField.requestFocus();
    }

    private void setup() {
	// set title
	setTitle(msg.getString("redate"));

	// kill me when i'm gone
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	// grab data
	range = sample.range;
	isAbsolute = sample.isAbsolute();

	// layout
	JPanel myPanel = new JPanel();
	GridBagLayout myGrid = new GridBagLayout();
	myPanel.setLayout(myGrid);
	getContentPane().add(myPanel, BorderLayout.CENTER);
	GridBagConstraints myConstraints = new GridBagConstraints();
	myConstraints.anchor = GridBagConstraints.NORTHWEST;
	myConstraints.insets = new Insets(3, 3, 3, 3);

	// more spacing!
	getContentPane().add(Box.createVerticalStrut(12), BorderLayout.NORTH);
	getContentPane().add(Box.createHorizontalStrut(20), BorderLayout.EAST);
	getContentPane().add(Box.createHorizontalStrut(20), BorderLayout.WEST);

	// redate --------------------------------------------------
	JLabel firstLabel = new JLabel(msg.getString("range") + ":", SwingConstants.TRAILING);
	firstLabel.setVerticalAlignment(SwingConstants.CENTER);
	firstLabel.setDisplayedMnemonic('G');
	myConstraints.gridx = 0;
	myConstraints.gridy = 0;
	myConstraints.gridheight = 2;
	myPanel.add(firstLabel, myConstraints);
	myConstraints.gridheight = 1;

	JPanel rangePanel = new JPanel(); // default=FlowLayout

	startListener = new StartListener();
	startField = new JTextField(sample.range.getStart().toString(), 5);
	startField.getDocument().addDocumentListener(startListener);

	endListener = new EndListener();
	endField = new JTextField(sample.range.getEnd().toString(), 5);
	endField.getDocument().addDocumentListener(endListener);

	firstLabel.setLabelFor(endField); // :-)

	rangePanel.add(startField);
	rangePanel.add(new JLabel(" - "));
	rangePanel.add(endField);

	myConstraints.gridx = 1;
	myConstraints.gridy = 0;
	myPanel.add(rangePanel, myConstraints);

	// dating --------------------------------------------------
	JLabel dating = new JLabel(msg.getString("dating") + ":", SwingConstants.TRAILING);
	myConstraints.gridx = 0;
	myConstraints.gridy = 2;
	myConstraints.gridheight = 2;
	myPanel.add(dating, myConstraints);
	myConstraints.gridheight = 1;

	ButtonGroup datingGroup = new ButtonGroup();
	JRadioButton relButton = new JRadioButton(msg.getString("relative"), !isAbsolute);
	relButton.setMnemonic(msg.getString("relative_key").charAt(0));
	final JRadioButton absButton = new JRadioButton(msg.getString("absolute"), isAbsolute);
	absButton.setMnemonic(msg.getString("absolute_key").charAt(0));

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
	myConstraints.gridx = 1;
	myPanel.add(relButton, myConstraints);
	myConstraints.gridy = 3;
	myPanel.add(absButton, myConstraints);

        // buttons --------------------------------------------------
        JPanel buttons = new JPanel(new ButtonLayout());
        getContentPane().add(buttons, BorderLayout.SOUTH);

	// cancel, ok
	JButton cancel = new JButton(msg.getString("cancel"));
	final JButton ok = new JButton(msg.getString("ok"));
	buttons.add(cancel);
	buttons.add(ok);

	// (listen for cancel, ok)
	ActionListener buttonListener = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource()==ok &&
			(!sample.range.equals(range) || sample.isAbsolute()!=isAbsolute))
			sample.postEdit(Redate.redate(sample, range, isAbsolute ? "A" : "R"));
		    dispose();
		}
	    };

	cancel.addActionListener(buttonListener);
	ok.addActionListener(buttonListener);

	// some nice spacing
	buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

	// esc => cancel
	addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			dispose();
		}
	    });

	// ret => ok
	getRootPane().setDefaultButton(ok);
    }

}
