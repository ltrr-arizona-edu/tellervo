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
import corina.gui.XButton;
import corina.util.OKCancel;
import corina.util.JLine;

import java.util.ResourceBundle;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.event.DocumentListener;
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

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class TruncateDialog extends JDialog {
    // data
    private Sample s;
    private Range r;

    // gui
    private JTextField tf1, tf2, tf3, tf4;
    private JLabel result;

    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

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
    DocumentListener updater1 = new DocumentListener() {
        public void changedUpdate(DocumentEvent e) { update(); }
        public void insertUpdate(DocumentEvent e) { update(); }
        public void removeUpdate(DocumentEvent e) { update(); }
        private void update() {
            updateFromNumbers();
            updateResult();
        }
    };

    // when something is typed, update everything from the years
    DocumentListener updater2 = new DocumentListener() {
        public void changedUpdate(DocumentEvent e) { update(); }
        public void insertUpdate(DocumentEvent e) { update(); }
        public void removeUpdate(DocumentEvent e) { update(); }
        private void update() {
            updateFromYears();
            updateResult();
        }
    };

    private void updateResult() {
        String rangeAndSpan = (r!=null) ? (r + " (n=" + r.span() + ")") : msg.getString("badcrop");
        result.setText(msg.getString("after") + ": " + rangeAndSpan);
    }

    private JPanel p;
    private void setup() {
        // use my own toplevel, so i can create my own border
        p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        setContentPane(p);

        // the big panel
        JPanel pri = new JPanel();
        pri.setLayout(new BoxLayout(pri, BoxLayout.Y_AXIS));
        p.add(pri, BorderLayout.CENTER);

        // secondary panel: start | end
        JPanel sec = new JPanel();
        sec.setLayout(new BoxLayout(sec, BoxLayout.X_AXIS));

        // start panel
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));

        // "Start"
        JLabel cropStart = new JLabel(msg.getString("crop_start"));
        cropStart.setHorizontalAlignment(SwingConstants.CENTER);
        cropStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        startPanel.add(cropStart);

        // "by [ xxx ] years"
        JPanel f1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // no size?
        f1.add(new JLabel("by "));
        tf1 = new JTextField("0", 4);
        tf1.getDocument().addDocumentListener(updater1);
        f1.add(tf1);
        f1.add(new JLabel(" years"));
        startPanel.add(f1);
        f1.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "to year [ xxx ]"
        JPanel f2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // no size?
        f2.add(new JLabel("to year "));
        tf2 = new JTextField(s.range.getStart().toString(), 5);
        tf2.getDocument().addDocumentListener(updater2);
        f2.add(tf2);
        startPanel.add(f2);
        f2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // end panel
        JPanel endPanel = new JPanel();
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));

        // "End"
        JLabel cropEnd = new JLabel(msg.getString("crop_end"));
        cropEnd.setHorizontalAlignment(SwingConstants.CENTER);
        cropEnd.setAlignmentX(Component.CENTER_ALIGNMENT);
        endPanel.add(cropEnd);

        // "by [ xxx ] years"
        JPanel f3 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // no size?
        f3.add(new JLabel("by "));
        tf3 = new JTextField("0", 4);
        tf3.getDocument().addDocumentListener(updater1);
        f3.add(tf3);
        f3.add(new JLabel(" years"));
        endPanel.add(f3);
        f3.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "to year [ xxx ]"
        JPanel f4 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // no size?
        f4.add(new JLabel("to year "));
        tf4 = new JTextField(s.range.getEnd().toString(), 5);
        tf4.getDocument().addDocumentListener(updater2);
        f4.add(tf4);
        endPanel.add(f4);
        f4.setAlignmentX(Component.CENTER_ALIGNMENT);

        // build secondary panel
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
        JLabel tmp = new JLabel(msg.getString("before") + ": " + s.range + " (n=" + s.range.span() + ")");
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
    }

    // returns "ok" button
    private JButton initButtons() {
        JPanel buttons = new JPanel(new ButtonLayout());
        p.add(buttons, BorderLayout.SOUTH);

        // cancel == close
        JButton cancel = new XButton("cancel");
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        buttons.add(cancel);

        // ok == apply
        JButton apply = new XButton("ok");
        apply.addActionListener(new AbstractAction() {
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
                        return msg.getString("truncate");
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
        buttons.add(apply);
        return apply;
    }

    public TruncateDialog(Sample s, JFrame owner) {
        // owner, title, modal
        super(owner, msg.getString("truncate"), true);

        // get sample, range
        this.s = s;
        r = s.range;

        // setup
        setup();
        JButton ok = initButtons();

        // ret => ok, esc => cancel
        OKCancel.addKeyboardDefaults(this, ok);

        // show it
        pack();
        setResizable(false);
        show();
    }
}
