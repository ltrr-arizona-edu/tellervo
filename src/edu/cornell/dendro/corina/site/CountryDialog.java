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

package edu.cornell.dendro.corina.site;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.OKCancel;

/**
   Display a dialog allowing the user to choose a country, or "None".

   @see edu.cornell.dendro.corina.site.Country

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class CountryDialog {

	// TODO: why's this have a weird interface?  why not
	// c = new CountryDialog(parent, oldCode), c.getCode()?

	// BUG?: if dialogs are hide-on-close by default, does that mean
	// i'll leak memory if the user brings up a CountryDialog, clicks
	// the close box, and repeats?

	private String result;

	private final String original;

	// value[0] -- what to show to mean "null".
	private final static String NONE = "None";
	
	private JDialog getDialog(Dialog parent) {
		if(parent != null)
			return new JDialog(parent, (App.platform.isMac() ? "" : I18n.getText("choose_country")), true); // true=modal
		else
			return new JDialog((Frame) null, (App.platform.isMac() ? "" : I18n.getText("choose_country")), true); // true=modal
	}

	private CountryDialog(Dialog parent, String oldCode) {
		original = oldCode;
		result = original; // cancel just disposes, so set this now

		JButton cancel = Builder.makeButton("cancel");
		JButton ok = Builder.makeButton("ok");

		// make list of all countries, sorted;
		// (assumes they're Capitalized Properly, else the
		// sorting order will look weird.)
		String names[] = Country.getAllNames();
		Arrays.sort(names);

		// prepend "None". (#'cons in 4 lines, 1 loop, double the memory!)
		String countries[] = new String[names.length + 1];
		for (int i = 0; i < names.length; i++)
			countries[i + 1] = names[i];
		countries[0] = NONE;

		// make jlist, and select old value
		final JList countryList = new JList(countries);
		int target = 0;
		if (oldCode == null) {
			countryList.setSelectedIndex(0);
		} else {
			String oldName = Country.getName(oldCode);
			for (int i = 0; i < names.length; i++) {
				if (oldName.equals(names[i])) {
					countryList.setSelectedIndex(i + 1);
					target = i + 1;
					// we'll call ensureIndexIsVisible on |target| later
					break;
				}
			}
		}

		// add "type-to-select" listener?  well, i can't find it in
		// the mac HIG, and i think mac-style is different from
		// win32-style, anyway.  plus you can already use the arrows
		// and page up/down and home/end, so i don't think it's worth
		// my implementation time.  (if you want to add it, go ahead.)

		// create dialog and lay out components
		final JDialog d = getDialog(parent);

		// REFACTOR: use vertical margins on n/s instead of wrapping in box layouts?

		JPanel n = Layout.boxLayoutY(Layout.flowLayoutL(I18n
				.getText("choose_a_country")), Box.createVerticalStrut(16));

		JPanel s = Layout.boxLayoutY(Box.createVerticalStrut(18), Layout
				.buttonLayout(cancel, ok));

		JPanel p = Layout.borderLayout(n, null, new JScrollPane(countryList),
				null, s);
		p.setBorder(BorderFactory.createEmptyBorder(12, 20, 20, 20));
		d.setContentPane(p);

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
					try {
						result = Country.getCode(name);
					} catch (IllegalArgumentException iee) {
						result = Country.badCode(name);
					}
				}

				d.dispose();
			}
		});

		// double-click = ok
		countryList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// is it null?
					int index = countryList.locationToIndex(e.getPoint());
					if (index == 0) {
						result = null;
						d.dispose();
						return;
					}

					// get name, lookup code, and close
					String name = (String) countryList.getSelectedValue();
					try {
						result = Country.getCode(name);
					} catch (IllegalArgumentException iee) {
						result = Country.badCode(name);
					}
					d.dispose();
				}
			}
		});

		// handle return/escape
		OKCancel.addKeyboardDefaults(ok);

		// set size: after pack(), increase the height, so users
		// don't have to scroll as much.
		// TODO: make sure this isn't taller than the screen!
		d.pack();
		Dimension size = d.getSize();
		d.setSize(size.width, (int) (size.height * 1.5));

		if(parent != null)
			Center.center(d, parent);
		else
			Center.center(d);

		// scroll to the current value, if it's not visible.
		countryList.ensureIndexIsVisible(target);

		// focus
		countryList.requestFocus(); // (does this help at all?)

		// whew, done.  (this blocks until dispose())
		d.show();
	}

	/**
	 Show the dialog, and return the country code the user chose.
	 (If the user cancels, returns the original code.)

	 @param parent the parent component (dialog) for this dialog
	 @param oldCode the previous code; in the list, this is
	 initially selected
	 @return the new code, or null, if the user chose "None"
	 */
	public static String showDialog(Dialog parent, String oldCode) {
		CountryDialog c = new CountryDialog(parent, oldCode);
		return c.result;
	}
}
