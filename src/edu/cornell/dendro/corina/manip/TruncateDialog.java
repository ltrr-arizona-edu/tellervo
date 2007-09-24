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
import corina.util.OKCancel;
import corina.util.JLine;
import corina.util.DocumentListener2;
import corina.ui.Builder;
import corina.ui.I18n;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/**
   A dialog box for truncating samples.  It allows the user to crop
   either "this many years" or "back until this year" from the start
   and/or end of a sample.

   <p>This might only be a temporary solution: TSAP is alleged to have
   the ability to run crossdates, etc., on parts ("windows"?) of a
   dataset, and this would be much preferable.  So, for instance,
   "cropping" should only add new data fields "disable_pre" and
   "disable_post", being the number of data years to ignore on either
   end of the sample.  Crossdating, indexing, and everything else
   would use the disable_* fields, but the data would always be
   present.  Graphing could draw the disabled data as a dotted
   line.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class TruncateDialog extends JDialog {
    // data
    private Sample s;
    private Range r;

    // gui
    private JTextField tf1, tf2, tf3, tf4;
    private JLabel result;

    // update everything from "by /n/ years"
    private void updateFromNumbers() {
        int n1=0, n2=0;
        Year start;

        try {
            n1 = Integer.parseInt(tf1.getText());
            start = s.range.getStart().add(n1);
            n2 = Integer.parseInt(tf3.getText());
        } catch (NumberFormatException nfe) {
            r = null;
            return;
        }

        if (n1 < 0 || n2 < 0 || (n1+n2 >= s.data.size())) {
            r = null;
            return;
        }

        Year end = s.range.getEnd().add(-n2);

        r = new Range(start, end);

        tf2.getDocument().removeDocumentListener(updater2);
        tf4.getDocument().removeDocumentListener(updater2);

        tf2.setText(start.toString());
        tf4.setText(end.toString());

        tf2.getDocument().addDocumentListener(updater2);
        tf4.getDocument().addDocumentListener(updater2);
    }

    // update everything from "to year /n/"
    private void updateFromYears() {
        Year start, end;

        try {
            start = new Year(tf2.getText());
            end = new Year(tf4.getText());
        } catch (NumberFormatException nfe) {
            r = null;
            return;
        }

        int n1 = start.diff(s.range.getStart());
        int n2 = s.range.getEnd().diff(end);

        if (n1 < 0 || n2 < 0 || (n1+n2 >= s.data.size())) {
            r = null;
            return;
        }

        r = new Range(start, end);

        tf1.getDocument().removeDocumentListener(updater1);
        tf3.getDocument().removeDocumentListener(updater1);

        tf1.setText(String.valueOf(n1));
        tf3.setText(String.valueOf(n2));

        tf1.getDocument().addDocumentListener(updater1);
        tf3.getDocument().addDocumentListener(updater1);
    }

    // when something is typed, update everything from the numbers
    DocumentListener2 updater1 = new DocumentListener2() {
	    public void update(DocumentEvent e) {
		updateFromNumbers();
		updateResult();
	    }
	};

    // when something is typed, update everything from the years
    DocumentListener2 updater2 = new DocumentListener2() {
	    public void update(DocumentEvent e) {
		updateFromYears();
		updateResult();
	    }
	};

    // update "after:" text with resultant range.
    private void updateResult() {
        String rangeAndSpan;
	if (r == null)
	    rangeAndSpan = I18n.getText("badcrop");
	else
	    rangeAndSpan = r + " (n=" + r.span() + ")";

        result.setText(I18n.getText("after") + ": " + rangeAndSpan);
    }

    private JPanel setup() {
        // the big panel
        JPanel pri = new JPanel();
        pri.setLayout(new BoxLayout(pri, BoxLayout.Y_AXIS));

        // "Start"
        JLabel cropStart = new JLabel(I18n.getText("crop_start"));
        cropStart.setHorizontalAlignment(SwingConstants.CENTER);
        cropStart.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "by [ xxx ] years"
        tf1 = new JTextField("0", 4);
        tf1.getDocument().addDocumentListener(updater1);
	JPanel f1 = Layout.flowLayoutL("by ", tf1, " years");
        f1.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "to year [ xxx ]"
        tf2 = new JTextField(s.range.getStart().toString(), 5);
        tf2.getDocument().addDocumentListener(updater2);
	JPanel f2 = Layout.flowLayoutL("to year ", tf2);
        f2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // start panel
	JPanel startPanel = Layout.boxLayoutY(cropStart, f1, f2);

        // "End"
        JLabel cropEnd = new JLabel(I18n.getText("crop_end"));
        cropEnd.setHorizontalAlignment(SwingConstants.CENTER);
        cropEnd.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "by [ xxx ] years"
        tf3 = new JTextField("0", 4);
        tf3.getDocument().addDocumentListener(updater1);
	JPanel f3 = Layout.flowLayoutL("by ", tf3, " years");
        f3.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "to year [ xxx ]"
        tf4 = new JTextField(s.range.getEnd().toString(), 5);
        tf4.getDocument().addDocumentListener(updater2);
	JPanel f4 = Layout.flowLayoutL("to year ", tf4);
        f4.setAlignmentX(Component.CENTER_ALIGNMENT);

        // end panel
	JPanel endPanel = Layout.boxLayoutY(cropEnd, f3, f4);

        // build secondary panel: start | end
        JPanel sec = new JPanel();
        sec.setLayout(new BoxLayout(sec, BoxLayout.X_AXIS));
        sec.add(Box.createHorizontalStrut(16));
        sec.add(startPanel);
        sec.add(Box.createHorizontalStrut(12));
        sec.add(new JLine(JLine.VERTICAL));
        sec.add(Box.createHorizontalStrut(12));
        sec.add(endPanel);
        sec.add(Box.createHorizontalStrut(16));

        // bottom line: "after truncating..."
        result = new JLabel();
        updateResult();

        // build primary panel
        pri.add(Box.createVerticalStrut(8));
	String text = I18n.getText("before") + ": " +
	              s.range + " (n=" + s.range.span() + ")";
	JLabel tmp = new JLabel(text);
        // center the label
        tmp.setHorizontalAlignment(SwingConstants.CENTER);
        tmp.setAlignmentX(Component.CENTER_ALIGNMENT);
        pri.add(tmp);
        pri.add(Box.createVerticalStrut(8));
        pri.add(sec);
        pri.add(Box.createVerticalStrut(8));
        // center the label
        result.setHorizontalAlignment(SwingConstants.CENTER);
        result.setAlignmentX(Component.CENTER_ALIGNMENT);
        pri.add(result);
        pri.add(Box.createVerticalStrut(8));

	// return the panel
	return pri;
    }

    private void initButtons() {
        // cancel == close
        cancel = Builder.makeButton("cancel");
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });

        // ok == apply
        ok = Builder.makeButton("ok");
        ok.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                // nothing to do?
                if (r.equals(s.range)) {
                    dispose();
                    return;
                }

                // do the truncate
                final Truncate t = new Truncate(s);
                t.cropTo(r);

                // undo
                s.postEdit(new AbstractUndoableEdit() {
                    private String filename = (String) s.meta.get("filename");
                    private boolean wasMod = s.isModified();
                    public void undo() throws CannotUndoException {
                        s.meta.put("filename", filename);
                        t.uncrop();
                        s.fireSampleRedated();
                        s.fireSampleDataChanged();
                        if (!wasMod)
                            s.clearModified();
                    }
                    public void redo() throws CannotRedoException {
                        s.meta.remove("filename");
                        t.cropTo(r);
                        s.fireSampleRedated();
                        s.fireSampleDataChanged();
                        s.setModified();
                    }
                    public boolean canRedo() {
                        return true;
                    }
                    public String getPresentationName() {
                        return I18n.getText("truncate");
                    }
                });

                // clear filename
                s.meta.remove("filename");

                // fire off some events
                s.fireSampleRedated();
                s.fireSampleDataChanged(); // for grapher
                s.setModified();

                // on success, close
                dispose();
            }
        });
    }

    private JButton cancel, ok;

    public TruncateDialog(Sample s, JFrame owner) {
        // owner, title, modal
        super(owner, I18n.getText("truncate"), true);

        // get sample, range
        this.s = s;
        r = s.range;

        // setup
        JPanel guts = setup();
        initButtons();

        // create a panel for the top, with a border
	JPanel p = Layout.borderLayout(null,
				       null, guts, null,
				       Layout.buttonLayout(cancel, ok));
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        setContentPane(p);

        // ret => ok, esc => cancel
        OKCancel.addKeyboardDefaults(ok);

        // show it
        pack();
        setResizable(false);
        show();
    }
}
