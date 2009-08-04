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

package edu.cornell.dendro.corina.manip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.EnumSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasDating;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasInterpretation;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.NameVersionPanel;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.tridasv2.SeriesLinkUtil;
import edu.cornell.dendro.corina.tridasv2.ui.ComboBoxFilterable;
import edu.cornell.dendro.corina.tridasv2.ui.EnumComboBoxItemRenderer;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;
import edu.cornell.dendro.corina.wsi.corina.NewTridasIdentifier;

/**
 * A dialog which enables the user to redate a sample. You can redate either one
 * that has already been loaded or a file on disk.
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i
 *         style="color: gray">dot</i> edu&gt;
 * @author Lucas Madar
 * @version $Id$
 */
public class RedateDialog extends JDialog {
	/** A DocumentListener for the end value */
	private class EndListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			update();
		}

		public void insertUpdate(DocumentEvent e) {
			update();
		}

		public void removeUpdate(DocumentEvent e) {
			update();
		}

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

	/** A DocumentListener for the starting value */
	private class StartListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			update();
		}

		public void insertUpdate(DocumentEvent e) {
			update();
		}

		public void removeUpdate(DocumentEvent e) {
			update();
		}

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

	private static final long serialVersionUID = 1L;
	
	/** The sample we're redating */
	private Sample sample;

	/** The new range */
	private Range range;
	
	/** The original dating type (can be null) */
	private NormalTridasDatingType originalDatingType;
	
	/** The current dating type */
	private NormalTridasDatingType datingType;

	/** The name, version, and justificaiton panel */
	private NameVersionPanel info;

	/** Text field document listeners to auto-update each other */
	private DocumentListener startListener, endListener;

	/** The Combo box that holds the dating type */
	protected JComboBox cboDatingType;
	
	/** The text boxes that hold our start/end year */
	protected JTextField startField, endField;

	/**
	 * Create a redater for a loaded sample. The "OK" button will fire a
	 * <code>sampleRedated</code> event to update other views.
	 * 
	 * @param sample
	 *            the sample to redate
	 */
	public RedateDialog(Sample sample, JFrame owner) {
		// modal
		super(owner, true);

		// get sample
		this.sample = sample;

		// determine dating type
		setupDatingType();
		
		// pass
		setup();

		// all done
		pack();
		endField.requestFocusInWindow();
		
		Center.center(this, owner);
		
		setVisible(true);
	}

	/**
	 * Apply a redate directly to the sample
	 * @param dating
	 */
	private void performRedateInPlace(TridasDating dating) {
		sample.postEdit(Redate.redate(sample, range, dating));
	}
	
	/**
	 * Create a new redate on the webservice
	 * @param dating
	 * @return true on success, false otherwise
	 */
	private boolean performCorinaWsiRedate(TridasDating dating) {
		// we have to have a name and a justification
		if(!info.testAndComplainRequired(EnumSet.of(NameVersionPanel.Fields.NAME,
				NameVersionPanel.Fields.JUSTIFICATION)))
			return false;

		TridasDerivedSeries series = new TridasDerivedSeries();
		
		// set title (and version?)
		series.setTitle(info.getSeriesName());
		if(info.hasVersion())
			series.setVersion(info.getVersion());
		
		// it's a truncate
		ControlledVoc voc = new ControlledVoc();
		voc.setValue(SampleType.REDATE.toString());
		series.setType(voc);
		
		// the identifier is based on the domain from the series
		series.setIdentifier(NewTridasIdentifier.getInstance(sample.getSeries().getIdentifier()));
		
		// set the parent
		SeriesLinkUtil.addToSeries(series, sample.getSeries().getIdentifier());
		
		// now, a redate has three other parameters
		TridasInterpretation interpretation = new TridasInterpretation();
		series.setInterpretation(interpretation);
		
		// 1: Dating type (but only if it changed)
		if(datingType != originalDatingType)
			interpretation.setDating(dating);

		// 2: Relative start year
		interpretation.setFirstYear(range.getStart().tridasYearValue());
		// looks like the genericField is what's actually used?
		GenericFieldUtils.setField(series, "corina.newStartYear", Integer.parseInt(range.getStart().toString()));

		// 3: Justification
		GenericFieldUtils.setField(series, "corina.justification", info.getJustification());
		
		// make a new 'redate' dummy sample for saving
		Sample tmp = new Sample(series);		

		try {
			CorinaWsiTridasElement saver = new CorinaWsiTridasElement(series.getIdentifier());
			// here's where we do the "meat"
			if(saver.save(tmp)) {
				// put it in our menu
				OpenRecent.sampleOpened(new SeriesDescriptor(tmp));
				
				new Editor(tmp).toFront();
				
				// get out of here! :)
				return true;
			}
		} catch (IOException ioe) {
			Alert.error("Could not create redate", "Error: " + ioe.toString());
		}
		
		return false;
	}
	
	private boolean performRedate() {
		ITridasSeries series = sample.getSeries();
		TridasDating newDating;
		
		if(series.isSetInterpretation() && series.getInterpretation().isSetDating()) {
			TridasDating oldDating = series.getInterpretation().getDating();
			// create a copy!
			newDating = (TridasDating) oldDating.createCopy();
			oldDating.copyTo(newDating);
		}
		else
			// just make a new dating element
			newDating = new TridasDating();
		
		// set the new dating type
		newDating.setType(datingType);

		// if it's not derived and has no children, we can truncate in place
		if (!sample.getSampleType().isDerived()
				&& (!sample.hasMeta(Metadata.CHILD_COUNT) || sample.getMeta(Metadata.CHILD_COUNT, Integer.class) == 0)) {
			String message = "This series has no dependents. You can either\n" +
			                 "choose to redate the series in place, modifying\n" +
			                 "the data, or you can derive a new series, leaving\n" +
			                 "these measurements untouched (the default).\n\n" +
			                 "Which would you like to do?";
			String options[] = { "Derive a new series", "Redate in place", "Cancel" };
			
			int ret = JOptionPane.showOptionDialog(this, message, "Redate in place?", JOptionPane.DEFAULT_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			switch(ret) {
			case 0:
				break;
				
			// truncate in place
			case 1:
				performRedateInPlace(newDating);
				return true;
			
			// cancel
			case 2:
				return false;
			}
		}
		
		if(sample.getLoader() instanceof CorinaWsiTridasElement) {
			return performCorinaWsiRedate(newDating);
		}
		
		Alert.error("Can't redate", "I don't know how to redate this form of series.");
		
		return false;
	}
	
	private void setupDatingType() {
		ITridasSeries series = sample.getSeries();
		
		// default to relative if there's no information
		if(!series.isSetInterpretation() || !series.getInterpretation().isSetDating())
			datingType = NormalTridasDatingType.RELATIVE;
		else
			originalDatingType = datingType = series.getInterpretation().getDating().getType();
	}
	
	private JComboBox getDatingTypeComboBox() {
		final JComboBox combo = new ComboBoxFilterable(NormalTridasDatingType.values());
		
		combo.setRenderer(new EnumComboBoxItemRenderer());
		combo.setSelectedItem(datingType);
		
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datingType = (NormalTridasDatingType) combo.getSelectedItem();
			}
		});
		
		return combo;
	}
	
	private void setup() {
		// set title
		setTitle(I18n.getText("redate"));

		// kill me when i'm gone
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// grab data
		range = sample.getRange();

		// dialog is a boxlayout
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
		setContentPane(p);

		p.add(getTopPanel());
		
		p.add(Box.createVerticalStrut(8));
		p.add(new JSeparator(JSeparator.HORIZONTAL));
		p.add(Box.createVerticalStrut(8));

		// name, version, justificaiton panel
		info = new NameVersionPanel(sample, true);
		p.add(info);

		p.add(Box.createVerticalStrut(8));
		p.add(new JSeparator(JSeparator.HORIZONTAL));
		p.add(Box.createVerticalStrut(8));
		
		// cancel, ok
		JButton cancel = Builder.makeButton("cancel");
		final JButton ok = Builder.makeButton("ok");

		// (listen for cancel, ok)
		ActionListener buttonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isOk = e.getSource() == ok;
				boolean rangeChanged = !sample.getRange().equals(range);

				if (isOk && (rangeChanged || datingType != originalDatingType)) {
					if(!performRedate())
						return;
				}

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

	private JPanel getTopPanel() {
		JPanel p = new JPanel();
		
		JLabel lblNewRange = new JLabel();
		startField = new JTextField();
		JLabel lblTo = new JLabel();
		endField = new JTextField();
		JLabel lblDating = new JLabel();
		cboDatingType = getDatingTypeComboBox();

		lblNewRange.setText(I18n.getText("new_range") + ":");

		lblTo.setText(I18n.getText("to"));

		startListener = new StartListener();
		startField.setText(sample.getRange().getStart().toString());
		startField.getDocument().addDocumentListener(startListener);

		endListener = new EndListener();
		endField.setText(sample.getRange().getEnd().toString());
		endField.getDocument().addDocumentListener(endListener);

		lblDating.setText(I18n.getText("dating") + ":");

		GroupLayout layout = new GroupLayout(p);
		p.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.LEADING)
				.add(layout.createSequentialGroup()
						.add(layout.createParallelGroup(GroupLayout.LEADING)
								.add(lblNewRange)
								.add(lblDating))
								.add(18, 18, 18)
								.add(layout.createParallelGroup(GroupLayout.LEADING)
										.add(layout.createSequentialGroup()
												.add(startField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.UNRELATED)
												.add(lblTo)
												.addPreferredGap(LayoutStyle.UNRELATED)
												.add(endField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addContainerGap(89, Short.MAX_VALUE))
												.add(cboDatingType, 0, 191, Short.MAX_VALUE)))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.LEADING)
				.add(layout.createSequentialGroup()
						.addContainerGap()
						.add(layout.createParallelGroup(GroupLayout.BASELINE)
								.add(lblNewRange)
								.add(startField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.add(lblTo)
								.add(endField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.RELATED)
								.add(layout.createParallelGroup(GroupLayout.BASELINE)
										.add(lblDating)
										.add(cboDatingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		return p;
	}
}
