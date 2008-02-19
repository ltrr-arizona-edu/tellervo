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

package edu.cornell.dendro.corina.io;

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.layouts.DialogLayout;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.Help;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.TextClipboard;
import edu.cornell.dendro.corina.util.PureStringWriter;
import edu.cornell.dendro.corina.browser.FileLength;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.formats.Filetype;
import edu.cornell.dendro.corina.formats.PackedFileType;

import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;

/*
 notes on this implementation:

 -- users find it informative, and even fun, to use this dialog.  every single person i've seen
 has figured out how to use it instantly without being told.  (another dialog that shares these
 traits is the old mac color chooser.)  all signs of a good interface, i think.

 -- it was fairly fast to program, given (1) that i'd been working with swing for over a year,
 and (2) that i had a collection of classes (buttonlayout, dialoglayout, okcancel, filelength,
 overwrite) ready to use, in addition to the actual data and i/o routines.

 -- this means to me that (1) swing has a steep learning curve, and (2) it's missing a bit of
 basic functionality that should be included, and would make programmers' lives easier.

 -- unfortunately, given the strict separation between the language, the default class library,
 and user classes, i can't just extend java to include features i want; they'll always be second-
 class citizens (so to speak).

 -- it doesn't behave exactly the same on windows and mac.  i don't know why it doesn't scroll to
 the top on windows.  (windows users don't seem to notice minor bugs, anyway.)
 */

/**
 A dialog allowing the user to choose a file format to export.

 <h2>Left to do:</h2>
 <ul>
 <li>Move this class to corina.gui
 <li>Handle IOE intelligently (e.g., when it can't load an element)
 <li>Get rid of "size" field; it probably does more harm than good
 <li>Run exporters in their own thread? (Spreadsheet format can take a little while)
 <li>Javadoc
 <li>Use Filetype.getDefaultExtension(), so clicking "Ok" here gives you
 a default of "<old-filename>.<sug-ext>"
 <li>Make it resizable (JTextArea doesn't resize -- bug in DialogLayout?)
 <li>Should 2-column use a zero-year?
 <li>Width is broken with 1.4.1: DialogLayout's fault
 <li>Is it "Filetype", or "Format"?  I'd say the latter.
 <li>"Copy" should be "Copy to Clipboard", but shouldn't force the other buttons to be wider
 <li>"Copy" button should be in the middle, not next to "Help" (or on the right)
 <li>Cache results?  creating a huge StringBuffer takes a non-trivial amount of time
 <li>Sample param should be "sample", not "s"
 </ul>

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$
 */
public class ExportDialog extends JDialog {

	// exporters for raw, summed files -- this seems slightly awkward,
	// but it's the SimplestThing right now
	private static final String EXPORTERS_RAW[] = new String[] {
			"edu.cornell.dendro.corina.formats.CorinaXML",
			"edu.cornell.dendro.corina.formats.Tucson", 
			"edu.cornell.dendro.corina.formats.TucsonSimple",
			"edu.cornell.dendro.corina.formats.TwoColumn", 
			"edu.cornell.dendro.corina.formats.Corina",
			"edu.cornell.dendro.corina.formats.TRML", 
			"edu.cornell.dendro.corina.formats.HTML",
			"edu.cornell.dendro.corina.formats.Heidelberg", 
			"edu.cornell.dendro.corina.formats.Hohenheim",
			"edu.cornell.dendro.corina.formats.TSAPMatrix", 
			"edu.cornell.dendro.corina.formats.MultiColumn", };

	private static final String EXPORTERS_SUM[] = new String[] {
			"edu.cornell.dendro.corina.formats.CorinaXML",
			"edu.cornell.dendro.corina.formats.Tucson", 
			"edu.cornell.dendro.corina.formats.TucsonSimple",
			"edu.cornell.dendro.corina.formats.PackedTucson", 
			"edu.cornell.dendro.corina.formats.TwoColumn",
			"edu.cornell.dendro.corina.formats.Corina", 
			"edu.cornell.dendro.corina.formats.TRML",
			"edu.cornell.dendro.corina.formats.HTML", 
			"edu.cornell.dendro.corina.formats.Heidelberg",
			"edu.cornell.dendro.corina.formats.Hohenheim", 
			"edu.cornell.dendro.corina.formats.TSAPMatrix",
			"edu.cornell.dendro.corina.formats.RangesOnly", };
	
	// these exporters implement 'PackedFileType' and can save more than one sample to a file.
	private static final String EXPORTERS_PACKED[] = new String[] {
		"edu.cornell.dendro.corina.formats.PackedTucson", 
		"edu.cornell.dendro.corina.formats.Spreadsheet", };

	// exporters i'm using
	private String exporters[];

	private JComboBox popup;

	private JLabel size;

	private JTextArea preview;

	private JButton ok;

	// ugh. gross. we can export a sample, OR a sample list...
	private Sample sample;
	private List sample_list;

	private void commonSetup(String[] exporterList) {
		// filetype popup
		exporters = exporterList; // (sample.elements == null ? EXPORTERS_RAW : EXPORTERS_SUM);
		int n = exporters.length;

		String v[] = new String[n];
		for (int i = 0; i < n; i++) {
			try {
				Filetype f = (Filetype) Class.forName(exporters[i])
						.newInstance();
				v[i] = f.toString();
			} catch (Exception e) {
				Bug.bug(e);
			}
		}

		popup = new JComboBox(v);
		popup.setMaximumRowCount(12); // i have 9 now, and this shouldn't scroll for so few
		popup.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				updatePreview();
			}
		});

		// size preview
		size = new JLabel("");

		// text preview
		preview = new JTextArea(12, 80) {
			@Override
			public boolean isManagingFocus() { // what's this for?  i've forgotten, document me!
				return false;
			}
		};

		// switch to monospaced font
		Font oldFont = preview.getFont();
		preview.setFont(new Font("monospaced", oldFont.getStyle(), oldFont
				.getSize()));

		// not editable
		preview.setEditable(false);

		// in a panel
		JPanel tuples = new JPanel(new DialogLayout());
		tuples.add(popup, I18n.getText("filetype"));
		// tuples.add(size, I18n.getText("size"));
		tuples.add(new JScrollPane(preview), I18n.getText("export_preview"));

		// buttons
		JButton help = Builder.makeButton("help");
		Help.addToButton(help, "exporting");
		// BETTER?: look at state of filetype popup, and present appropriate format page

		JButton copy = Builder.makeButton("copy");
		copy.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				TextClipboard.copy(preview.getText());
			}
		});
		JButton cancel = Builder.makeButton("cancel");

		// button actions
		cancel.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// just close
				dispose();
			}
		});

		ok = Builder.makeButton("ok");
		
		// in a panel
		JPanel buttons = Layout.buttonLayout(help, copy, null, cancel, ok);
		buttons.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

		JPanel main = Layout.borderLayout(null, null, tuples, null, buttons);
		main.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
		setContentPane(main);

		OKCancel.addKeyboardDefaults(ok);

		// initial view
		updatePreview();
	}
		
	/**
	 Create and display a sample-export dialog.

	 @param s the sample to export
	 @param parent the window which holds the sample to export
	 */
	public ExportDialog(Sample s, Frame parent) {
		super(parent, I18n.getText("export"), true);
		this.sample = s;
		this.sample_list = null;

		commonSetup((sample.getElements() == null ? EXPORTERS_RAW : EXPORTERS_SUM));

		ok.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// close dialog
				dispose();

				String format = exporters[popup.getSelectedIndex()];
				new Exporter().saveSingleSample(sample, format);
			}
		});		

		// show
		pack();
		setResizable(false);
		show();
	}

	/**
	 Create and display a sample-export dialog.

	 @param samples the list of samples to export
	 @param parent window
	 */
	public ExportDialog(List samples, Frame parent, boolean savePacked) {
		super(parent, I18n.getText("export"), true);

		if(savePacked) {
			this.sample = null;
			this.sample_list = samples;
			
			commonSetup(EXPORTERS_PACKED);
		}
		else {
			this.sample = (Sample) samples.get(0);
			this.sample_list = samples;
			
			commonSetup(sample.getElements() == null ? EXPORTERS_RAW : EXPORTERS_SUM);
			
			setTitle(getTitle() + " [first sample shown, choose format for all]");
		}
		
		final boolean bGlue = savePacked;
		ok.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {
					// if the sample list exists, and there's no individual sample, we want
					// a packed file. 
					
					String format = exporters[popup.getSelectedIndex()];

					if(bGlue == true)
						new Exporter().savePackedSample(sample_list, format);
					else
						new Exporter().saveMultiSample(sample_list, format);

				} catch (Exception ex) {
					Bug.bug(ex);
				}

				// close dialog
				dispose();
			}
		});		

		// show
		pack();
		setResizable(false);
		show();
	}
	
	// use the same StringWriter for all previews, because that way it uses the same StringBuffer
	private StringWriter writer = new PureStringWriter(10240); // 10K

	// FIXME: sometimes the preview takes a while to create.  for example,
	// a spreadsheet view of a 100-element master needs to load all 100
	// elements.  yeowch.  i'll need to run this in a separate thread for that!

	private void updatePreview() {
		int i = popup.getSelectedIndex();
		try {
			// save to buffer
			setCursor(new Cursor(Cursor.WAIT_CURSOR)); // this could take a second...
			Filetype f = (Filetype) Class.forName(exporters[i]).newInstance();
			StringBuffer buf = writer.getBuffer();
			buf.delete(0, buf.length()); // clear it
			BufferedWriter b = new BufferedWriter(writer);
			
			// if the sample list exists, and there's no individual sample, we want
			// a packed file. Check for stupidity first, though!
			if(sample_list != null && sample == null && f instanceof PackedFileType)
				((PackedFileType)f).saveSamples(sample_list, b);
			else
				f.save(sample, b);
			b.close();
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ok, done with the slow part

			// update views
			preview.setText(buf.toString());
			size.setText(new FileLength(buf.length()).toString());
			ok.setEnabled(true);

			// move cursor to start -- this scrolls to the top, as well
			preview.setCaretPosition(0);
		} catch (IOException ioe) {
			// problem saving it -- bug
			Bug.bug(ioe);
		} catch (Exception e) {
			// problem creating the filetype -- bug
			Bug.bug(e);
		}
	}
}
