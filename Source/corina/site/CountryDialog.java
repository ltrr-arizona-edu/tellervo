package corina.site;

import corina.gui.ButtonLayout;
import corina.util.OKCancel;

import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

// given a parent component and a "previous-value" code (2-letter string, iso-3166-ish),
// display a dialog letting the user choose a country, or "-n/s-".
// returns the code of her choice, or null for n/s.
public class CountryDialog {
    private String result;
    private final String original;

    private CountryDialog(Dialog parent, String oldCode) {
        original = oldCode;
        result = original; // cancel just disposes, so set this now

        JPanel buttonPanel = new JPanel(new ButtonLayout());
        JButton cancel = new JButton("Cancel");
        JButton ok = new JButton("OK");
        buttonPanel.add(cancel);
        buttonPanel.add(ok);

        // make list of all countries, sorted
        String names[] = Country.getAllNames();
        Arrays.sort(names);

        // prepend "-n/s-" (#'cons in 4 lines, 1 loop, double the memory!)
        String countries[] = new String[names.length + 1];
        for (int i=0; i<names.length; i++)
            countries[i+1] = names[i];
        countries[0] = "- not specified -";

        // make jlist, and select old value
        final JList countryList = new JList(countries); // static or something?
        int target=0;
        if (oldCode == null) {
            countryList.setSelectedIndex(0);
        } else {
            String oldName = Country.getName(oldCode);
            for (int i=0; i<names.length; i++) {
                if (oldName.equals(names[i])) {
                    countryList.setSelectedIndex(i+1);
                    target = i+1; // call ensureIndexIsVisible on this after packing
                    break;
                }
            }
        }

        // add "type-to-select" listener?  well, i can't find it in the mac HIG, and i think
        // mac-style is different from win32-style, anyway.  plus you can already use
        // the arrows and page up/down and home/end, so i don't think it's worth my
        // implementation time.  (if you want to add it, go ahead.)

        // create dialog and lay out components
        final JDialog d = new JDialog(parent, "Choose Country", /*modal=*/true);
        JPanel p = new JPanel(new BorderLayout());
        d.setContentPane(p);
        {
            JPanel bbb = new JPanel();
            bbb.setLayout(new BoxLayout(bbb, BoxLayout.Y_AXIS));
            JPanel fff = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fff.add(new JLabel("Choose Country:"));
            bbb.add(fff);
            bbb.add(Box.createVerticalStrut(16));
            p.add(bbb, BorderLayout.NORTH);
        }
        p.add(new JScrollPane(countryList), BorderLayout.CENTER);
        {
            JPanel bbb = new JPanel();
            bbb.setLayout(new BoxLayout(bbb, BoxLayout.Y_AXIS));
            bbb.add(Box.createVerticalStrut(18));
            bbb.add(buttonPanel);
            p.add(bbb, BorderLayout.SOUTH);
        }
        p.setBorder(BorderFactory.createEmptyBorder(24, 20, 20, 20));

        // cancel
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });

        // ok
        ok.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (countryList.getSelectedIndex() != 0) {
                    // figure out code from name, store in result
                    String name = (String) countryList.getSelectedValue();
                    result = Country.getCode(name);
                }

                d.dispose();
            }
        });

        // double-click = ok
        countryList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // is it null?
                    int index = countryList.locationToIndex(e.getPoint());
                    if (index==0) {
                        result = null;
                        d.dispose();
                        return;
                    }

                    // get name, lookup code, and close
                    String name = (String) countryList.getSelectedValue();
                    result = Country.getCode(name);
                    d.dispose();
                }
            }
        });

        // handle return/escape
        OKCancel.addKeyboardDefaults(d, ok);

        // set size
        d.pack();
        Dimension size = d.getSize();
        d.setSize(size.width, (int) (size.height*1.5)); // increase the height, so they don't have to scroll as much.
                                                            // need to make sure this isn't taller than the screen?

        // scroll to the current value, if it's not visible.
        countryList.ensureIndexIsVisible(target);

        // focus
        countryList.requestFocus(); // does this help at all?

        // whew, done.  (this blocks until dispose().)
        d.show();
    }

    // show the dialog and return the result
    public static String showDialog(Dialog parent, String oldCode) {
        CountryDialog c = new CountryDialog(parent, oldCode);
        return c.result;
    }
}
