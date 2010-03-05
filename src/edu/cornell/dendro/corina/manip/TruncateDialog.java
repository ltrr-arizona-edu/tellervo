//
//This file is part of Corina.
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.EnumSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasDerivedSeries;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.NameVersionJustificationPanel;
import edu.cornell.dendro.corina.gui.RangeSlider;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.tridasv2.SeriesLinkUtil;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.DocumentListener2;
import edu.cornell.dendro.corina.util.JLine;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.Years;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;
import edu.cornell.dendro.corina.wsi.corina.NewTridasIdentifier;

/**
 * A dialog box for truncating samples. It allows the user to crop either
 * "this many years" or "back until this year" from the start and/or end of a
 * sample.
 * 
 * <p>
 * This might only be a temporary solution: TSAP is alleged to have the ability
 * to run crossdates, etc., on parts ("windows"?) of a dataset, and this would
 * be much preferable. So, for instance, "cropping" should only add new data
 * fields "disable_pre" and "disable_post", being the number of data years to
 * ignore on either end of the sample. Crossdating, indexing, and everything
 * else would use the disable_* fields, but the data would always be present.
 * Graphing could draw the disabled data as a dotted line.
 * </p>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i
 *         style="color: gray">dot</i> edu&gt;
 * @author Lucas Madar
 * @version $Id$
 */
public class TruncateDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	/** The series that we're truncating */
	private Sample s;
	/** The range of the new truncation, or null if invalid */
	private Range r;

	/** The text fields for entry */
	private JTextField tfRelStart, tfAbsStart, tfRelEnd, tfAbsEnd;
	
	/** The text field that shows the resultant truncation range */
	private JLabel result;
	
	private NameVersionJustificationPanel info;

	// when something is typed, update everything from the numbers
	private DocumentListener2 relativeUpdater = new DocumentListener2() {
		@Override
		public void update(DocumentEvent e) {
			updateFromNumbers();
			updateResult();
		}
	};

	// when something is typed, update everything from the years
	private DocumentListener2 absoluteUpdater = new DocumentListener2() {
		@Override
		public void update(DocumentEvent e) {
			updateFromYears();
			updateResult();
		}
	};
	
	// when the slider changes...
	private ChangeListener sliderUpdater = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			updateFromSlider();
			updateResult();
		}
	};

	/** The slider we use to visually display range */
	private RangeSlider slider;

	/** Our buttons */
	private JButton cancel, ok;

	/** The cell modifier to apply to the editor we're attached to, to visually show truncation results */
	private TruncationCellModifier cellModifier;
	

	/**
	 * Create a new Truncate Dialog for the given series
	 * @param s the series to truncate
	 * @param owner our owner window
	 */
	public TruncateDialog(Sample s, JFrame owner) {
		// owner, title, modal
		super(owner, I18n.getText("menus.tools.truncate"), true);

		// get sample, range
		this.s = s;
		r = s.getRange();

		// create a cell modifier for editor
		cellModifier = new TruncationCellModifier(r);

		if(owner instanceof Editor)
			((Editor) owner).getSampleDataView().addCellModifier(cellModifier);
		
		// setup
		JPanel guts = setup();
		initButtons();

		// create a panel for the top, with a border
		JPanel p = Layout.borderLayout(null, null, guts, null, Layout
				.buttonLayout(cancel, ok));
		p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
		setContentPane(p);

		// ret => ok, esc => cancel
		OKCancel.addKeyboardDefaults(ok);

		// show it
		pack();
		setResizable(false);
		Center.center(this);
		setVisible(true);

		// clean up
		if(owner instanceof Editor)
			((Editor) owner).getSampleDataView().removeCellModifier(cellModifier);
	}

	/**
	 * Apply the truncation as a new derived series on the server
	 * @return true on success
	 */
	private boolean applyCorinaWsiTruncation() {
		// we have to have a name and a justification
		if(!info.testAndComplainRequired(EnumSet.of(NameVersionJustificationPanel.Fields.NAME,
				NameVersionJustificationPanel.Fields.JUSTIFICATION)))
			return false;

		TridasDerivedSeries series = new TridasDerivedSeries();
		
		// set title (and version?)
		series.setTitle(info.getSeriesName());
		if(info.hasVersion())
			series.setVersion(info.getVersion());
		
		// it's a truncate
		ControlledVoc voc = new ControlledVoc();
		voc.setValue(SampleType.TRUNCATE.toString());
		series.setType(voc);
		
		// the identifier is based on the domain from the series
		series.setIdentifier(NewTridasIdentifier.getInstance(s.getSeries().getIdentifier()));
		
		// set the parent
		SeriesLinkUtil.addToSeries(series, s.getSeries().getIdentifier());
		
		// now, a truncate has three other parameters
		// 1: Relative start year (in interpretation)
		// 2: Relative end year
		// 3: Justification
		
		// relative start year = new start - old start
		int relStartYear = r.getStart().diff(s.getRange().getStart());
		// relative end year = new end - old start
		int relEndYear = r.getEnd().diff(s.getRange().getStart());
		
		GenericFieldUtils.setField(series, "corina.newStartYear", relStartYear);
		GenericFieldUtils.setField(series, "corina.newEndYear", relEndYear);
		GenericFieldUtils.setField(series, "corina.justification", info.getJustification());
		
		// make a new 'truncate' dummy sample for saving
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
			Alert.error(I18n.getText("error"), I18n.getText("error.couldNotTrundate") +": " + ioe.toString());
		}
		
		return false;
	}
	
	/**
	 * Apply the truncation
	 * @return true on success
	 */
	private boolean applyTruncation() {
		// if it's not derived and has no children, we can truncate in place
		if (!s.getSampleType().isDerived()
				&& (!s.hasMeta(Metadata.CHILD_COUNT) || s.getMeta(Metadata.CHILD_COUNT, Integer.class) == 0)) {
			String message = MessageFormat.format(I18n.getText("question.doInPlace"),
					new Object[] { I18n.getText("menus.tools.truncate").toLowerCase() });
			String options[] = { I18n.getText("question.deriveNewSeries"), I18n.getText("question.truncateInPlace"), I18n.getText("general.cancel")};
			
			int ret = JOptionPane.showOptionDialog(this, message, I18n.getText("question.truncateInPlace")+"?", JOptionPane.DEFAULT_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			switch(ret) {
			case 0:
				break;
				
			// truncate in place
			case 1:
				applyTruncationInPlace();
				return true;
			
			// cancel
			case 2:
				return false;
			}
		}
		
		if(s.getLoader() instanceof CorinaWsiTridasElement) {
			return applyCorinaWsiTruncation();
		}
		
		Alert.error(I18n.getText("error"), I18n.getText("error.unableToTruncate"));
		
		return false;
	}

	/**
	 * Apply the truncation directly to the series
	 */
	private void applyTruncationInPlace() {
		// do the truncate
		final Truncate t = new Truncate(s);
		t.cropTo(r);

		// undo
		s.postEdit(new AbstractUndoableEdit() {
			private static final long serialVersionUID = 1L;

			private String filename = (String) s.getMeta("filename");
			private boolean wasMod = s.isModified();

			@Override
			public boolean canRedo() {
				return true;
			}

			@Override
			public String getPresentationName() {
				return I18n.getText("menus.tools.truncate");
			}

			@Override
			public void redo() throws CannotRedoException {
				s.removeMeta("filename");
				t.cropTo(r);
				s.fireSampleRedated();
				s.fireSampleDataChanged();
				s.setModified();
			}

			@Override
			public void undo() throws CannotUndoException {
				s.setMeta("filename", filename);
				t.uncrop();
				s.fireSampleRedated();
				s.fireSampleDataChanged();
				if (!wasMod)
					s.clearModified();
			}
		});

		// clear filename
		s.removeMeta("filename");

		// fire off some events
		s.setModified();
		s.fireSampleRedated();
		s.fireSampleDataChanged(); // for grapher
	}
	
	/**
	 * Set up our button behaviors
	 */
	private void initButtons() {
		// cancel == close
		cancel = Builder.makeButton("general.cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		// ok == apply
		ok = Builder.makeButton("general.ok");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// nothing to do?
				if (r.equals(s.getRange())) {
					dispose();
					return;
				}

				if(applyTruncation())
					dispose();
			}
		});
	}
	
	/**
	 * Set up the UI
	 * @return a JPanel for the content of the dialog
	 */
	private JPanel setup() {
		// the big panel
		JPanel pri = new JPanel();
		pri.setLayout(new BoxLayout(pri, BoxLayout.Y_AXIS));
		
		// "Start"
		JLabel cropStart = new JLabel(I18n.getText("truncate.crop_start"));
		cropStart.setHorizontalAlignment(SwingConstants.CENTER);
		cropStart.setAlignmentX(Component.CENTER_ALIGNMENT);

		// "by [ xxx ] years"
		tfRelStart = new JTextField("0", 4);
		tfRelStart.getDocument().addDocumentListener(relativeUpdater);
		JPanel f1 = Layout.flowLayoutL(I18n.getText("general.by")+" ", tfRelStart, " " + I18n.getText("general.years"));
		f1.setAlignmentX(Component.CENTER_ALIGNMENT);

		// "to year [ xxx ]"
		tfAbsStart = new JTextField(s.getRange().getStart().toString(), 5);
		tfAbsStart.getDocument().addDocumentListener(absoluteUpdater);
		JPanel f2 = Layout.flowLayoutL("to year ", tfAbsStart);
		f2.setAlignmentX(Component.CENTER_ALIGNMENT);

		// start panel
		JPanel startPanel = Layout.boxLayoutY(cropStart, f1, f2);

		// "End"
		JLabel cropEnd = new JLabel(I18n.getText("truncate.crop_end"));
		cropEnd.setHorizontalAlignment(SwingConstants.CENTER);
		cropEnd.setAlignmentX(Component.CENTER_ALIGNMENT);

		// "by [ xxx ] years"
		tfRelEnd = new JTextField("0", 4);
		tfRelEnd.getDocument().addDocumentListener(relativeUpdater);
		JPanel f3 = Layout.flowLayoutL(I18n.getText("general.by")+" ", tfRelEnd, " " + I18n.getText("general.years"));
		f3.setAlignmentX(Component.CENTER_ALIGNMENT);

		// "to year [ xxx ]"
		tfAbsEnd = new JTextField(s.getRange().getEnd().toString(), 5);
		tfAbsEnd.getDocument().addDocumentListener(absoluteUpdater);
		JPanel f4 = Layout.flowLayoutL(I18n.getText("truncate.to_year") + " ", tfAbsEnd);
		f4.setAlignmentX(Component.CENTER_ALIGNMENT);

		// end panel
		JPanel endPanel = Layout.boxLayoutY(cropEnd, f3, f4);

		// build secondary panel: start | end
		JPanel sec = new JPanel();
		sec.setLayout(new BoxLayout(sec, BoxLayout.X_AXIS));
		sec.add(Box.createHorizontalStrut(16));
		sec.add(startPanel);
		sec.add(Box.createHorizontalStrut(12));
		sec.add(new JLine(SwingConstants.VERTICAL));
		sec.add(Box.createHorizontalStrut(12));
		sec.add(endPanel);
		sec.add(Box.createHorizontalStrut(16));

		// bottom line: "after truncating..."
		result = new JLabel();
		updateResult();

		// build primary panel
		pri.add(Box.createVerticalStrut(8));
		String text = I18n.getText("truncate.seriesIsNow") + ": " + s.getRange() + " (n="
				+ s.getRange().span() + ")";
		JLabel tmp = new JLabel(text);
		// center the label
		tmp.setHorizontalAlignment(SwingConstants.CENTER);
		tmp.setAlignmentX(Component.CENTER_ALIGNMENT);
		pri.add(tmp);
		pri.add(Box.createVerticalStrut(8));
		pri.add(sec);
		pri.add(Box.createVerticalStrut(8));

		// add a slider
		int startYear = Integer.valueOf(r.getStart().toString());
		int endYear = startYear + r.span();
		slider = new RangeSlider(startYear, endYear);
		slider.setValue(startYear);
		slider.setUpperValue(endYear);
		slider.addChangeListener(sliderUpdater);
		
		pri.add(slider);
		pri.add(Box.createVerticalStrut(8));


		
		// center the label
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		pri.add(result);
		pri.add(Box.createVerticalStrut(8));

		pri.add(new JSeparator(JSeparator.HORIZONTAL));
		pri.add(Box.createVerticalStrut(8));
		
		pri.add(setupNamingAndJustification());
		
		pri.add(Box.createVerticalStrut(8));
		pri.add(new JSeparator(JSeparator.HORIZONTAL));
		
		pri.add(Box.createVerticalStrut(12));
		// return the panel
		return pri;
	}
	
	private JPanel setupNamingAndJustification() {
		info = new NameVersionJustificationPanel(s, false, true);
		return info;
	}
	
	/** 
	 * update everything from "by /n/ years" 
	 */
	private void updateFromNumbers() {
		int n1 = 0, n2 = 0;
		Year start;

		try {
			n1 = Integer.parseInt(tfRelStart.getText());
			start = s.getRange().getStart().add(n1);
			n2 = Integer.parseInt(tfRelEnd.getText());
		} catch (NumberFormatException nfe) {
			r = null;
			return;
		}

		if (n1 < 0 || n2 < 0 || (n1 + n2 >= s.getData().size())) {
			r = null;
			return;
		}

		Year end = s.getRange().getEnd().add(-n2);

		r = new Range(start, end);

		tfAbsStart.getDocument().removeDocumentListener(absoluteUpdater);
		tfAbsEnd.getDocument().removeDocumentListener(absoluteUpdater);

		tfAbsStart.setText(start.toString());
		tfAbsEnd.setText(end.toString());

		tfAbsStart.getDocument().addDocumentListener(absoluteUpdater);
		tfAbsEnd.getDocument().addDocumentListener(absoluteUpdater);
		
		slider.removeChangeListener(sliderUpdater);
		slider.setValue(Integer.valueOf(start.toString()));
		slider.setUpperValue(Integer.valueOf(end.toString()));
		slider.addChangeListener(sliderUpdater);
	}

	/**
	 * Update everything from the slider
	 */
	private void updateFromSlider() {
		Year start, end;
		
		start = Years.valueOf(slider.getValue());
		end = Years.valueOf(slider.getUpperValue() - 1);

		int n1 = start.diff(s.getRange().getStart());
		int n2 = s.getRange().getEnd().diff(end);
		
		// no error checks, because our slider can't go off from the original scale
		
		r = new Range(start, end);
		
		// well, don't let the span get to be zero; that breaks things.
		if(r.span() == 0) {
			r = null;
			return;
		}
		
		// first set of boxes
		tfRelStart.getDocument().removeDocumentListener(relativeUpdater);
		tfRelEnd.getDocument().removeDocumentListener(relativeUpdater);

		tfRelStart.setText(String.valueOf(n1));
		tfRelEnd.setText(String.valueOf(n2));

		tfRelStart.getDocument().addDocumentListener(relativeUpdater);
		tfRelEnd.getDocument().addDocumentListener(relativeUpdater);
		
		// second set of boxes
		tfAbsStart.getDocument().removeDocumentListener(absoluteUpdater);
		tfAbsEnd.getDocument().removeDocumentListener(absoluteUpdater);

		tfAbsStart.setText(start.toString());
		tfAbsEnd.setText(end.toString());

		tfAbsStart.getDocument().addDocumentListener(absoluteUpdater);
		tfAbsEnd.getDocument().addDocumentListener(absoluteUpdater);
	}
	/**
	 * update everything from "to year /n/"
	 */
	private void updateFromYears() {
		Year start, end;

		try {
			start = new Year(tfAbsStart.getText());
			end = new Year(tfAbsEnd.getText());
		} catch (NumberFormatException nfe) {
			r = null;
			return;
		}

		int n1 = start.diff(s.getRange().getStart());
		int n2 = s.getRange().getEnd().diff(end);

		if (n1 < 0 || n2 < 0 || (n1 + n2 >= s.getData().size())) {
			r = null;
			return;
		}

		r = new Range(start, end);

		tfRelStart.getDocument().removeDocumentListener(relativeUpdater);
		tfRelEnd.getDocument().removeDocumentListener(relativeUpdater);

		tfRelStart.setText(String.valueOf(n1));
		tfRelEnd.setText(String.valueOf(n2));

		tfRelStart.getDocument().addDocumentListener(relativeUpdater);
		tfRelEnd.getDocument().addDocumentListener(relativeUpdater);
		
		slider.removeChangeListener(sliderUpdater);
		slider.setValue(Integer.valueOf(start.toString()));
		slider.setUpperValue(Integer.valueOf(end.toString()));
		slider.addChangeListener(sliderUpdater);
	}
	
	/**
	 * update "after:" text with resultant range.
	 */
	private void updateResult() {
		String rangeAndSpan;
		if (r == null) {
			rangeAndSpan = I18n.getText("error");
			cellModifier.updateRange(s.getRange());
		}
		else {
			rangeAndSpan = r + " (n=" + r.span() + ")";
			cellModifier.updateRange(r);
		}

		result.setText(I18n.getText("truncate.afterTruncating") + ": " + rangeAndSpan);
	}
}
