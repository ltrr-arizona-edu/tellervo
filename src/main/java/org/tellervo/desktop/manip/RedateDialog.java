/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

package org.tellervo.desktop.manip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.EnumSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.gui.Layout;
import org.tellervo.desktop.gui.NameVersionJustificationPanel;
import org.tellervo.desktop.gui.menus.EditorEditMenu;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.SeriesLinkUtil;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.tridasv2.ui.EnumComboBoxItemRenderer;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.util.OKCancel;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasDating;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasInterpretation;
import net.miginfocom.swing.MigLayout;
import java.awt.FlowLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;


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
	
	private final static Logger log = LoggerFactory.getLogger(RedateDialog.class);

	
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
			txtStartYear.getDocument().removeDocumentListener(startListener);

			// get text
			String value = txtEndYear.getText();

			// if it's the same, do nothing -- (not worth the code)

			// set the range
			try {
				range = range.redateEndTo(new Year(value));
				txtStartYear.setText(range.getStart().toString());
			} catch (NumberFormatException nfe) {
				txtStartYear.setText(I18n.getText("error"));
			}

			// re-enable startListener
			txtStartYear.getDocument().addDocumentListener(startListener);
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
			txtEndYear.getDocument().removeDocumentListener(endListener);

			// get text
			String value = txtStartYear.getText();

			// if it's the same, do nothing -- (not worth the code)

			// set the range
			try {
				range = range.redateStartTo(new Year(value));
				txtEndYear.setText(range.getEnd().toString());
			} catch (NumberFormatException nfe) {
				txtEndYear.setText(I18n.getText("error"));
			}

			// re-enable endListener
			txtEndYear.getDocument().addDocumentListener(endListener);
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
	private NameVersionJustificationPanel infoPanel;

	/** Text field document listeners to auto-update each other */
	private DocumentListener startListener, endListener;

	/** The Combo box that holds the dating type */
	protected JComboBox cboDatingType;
	
	/** The text boxes that hold our start/end year */
	protected JTextField txtStartYear, txtEndYear;
	private JComboBox combo;

	/**
	 * @param sample
	 * @param owner
	 * @wbp.parser.constructor
	 */
	public RedateDialog(Sample sample, JFrame owner) {
		this(sample, owner, sample.getRange());
	}
	
	/**
	 * Create a redater for a loaded sample. The "OK" button will fire a
	 * <code>sampleRedated</code> event to update other views.
	 * 
	 * @param sample
	 *            the sample to redate
	 * @param owner
	 *            the owning frame
	 * @param startRange
	 * 			  the initial range
	 */
	public RedateDialog(Sample sample, JFrame owner, Range startRange) {
		// modal
		super(owner, true);

		// get sample
		this.sample = sample;

		// determine dating type
		setupDatingType();
		
		// pass
		setup(startRange);

		// hide justification panel for new series that must be redated in place
		infoPanel.setVisible(false);
		if(sample.getIdentifier()!=null && 
				sample.getIdentifier().getValue()!=null && 
				!sample.getIdentifier().getValue().equals("newSeries"))
		{
			infoPanel.setVisible(true);
		}
		
		// all done
		pack();
		txtEndYear.requestFocusInWindow();
		
		Center.center(this, owner);
	}
	
	/**
	 * Apply a redate directly to the sample
	 * @param dating
	 */
	private void performRedateInPlace(TridasDating dating) {
		sample.postEdit(Redate.redate(sample, range, dating));
	}
	
	
	private boolean performRedate() {
		ITridasSeries series = sample.getSeries();
		TridasDating newDating;
		
		if(series.isSetInterpretation() && series.getInterpretation().isSetDating()) {
			TridasDating oldDating = series.getInterpretation().getDating();
			// create a copy!
			newDating = (TridasDating) oldDating.clone();
			oldDating.copyTo(newDating);
		}
		else
			// just make a new dating element
			newDating = new TridasDating();
		
		// set the new dating type
		newDating.setType(datingType);

		
		if(sample.getIdentifier()==null || sample.getSeries().getIdentifier().getValue().equals("newSeries"))
		{
			// This is a brand new unsaved series so *must* be redate in place
			performRedateInPlace(newDating);
			return true;
			
		}
		
			
		// if it's not derived and has no children, we can truncate in place
		log.debug("Does sample have childcount metadata? : "+sample.hasMeta(Metadata.CHILD_COUNT));
		log.debug("Child count? : "+sample.getMeta(Metadata.CHILD_COUNT, Integer.class));

		if (!sample.getSampleType().isDerived()
				&& (!sample.hasMeta(Metadata.CHILD_COUNT) || sample.getMeta(Metadata.CHILD_COUNT, Integer.class) == 0)) {

			
			String message = MessageFormat.format(I18n.getText("question.doInPlace"),
					new Object[] { I18n.getText("menus.tools.redate").toLowerCase() });
			String options[] = { I18n.getText("question.deriveNewSeries"), I18n.getText("question.redateInPlace"), I18n.getText("general.cancel")};
			
			int ret = JOptionPane.showOptionDialog(this, message, I18n.getText("question.redateInPlace")+"?", JOptionPane.DEFAULT_OPTION, 
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
		
		if(sample.getLoader() instanceof TellervoWsiTridasElement) {
			
			return Redate.performTellervoWsiRedate(sample, 
					infoPanel.getSeriesName(), infoPanel.getVersion(), infoPanel.getJustification(), 
					datingType, originalDatingType, range);
		}
		
		Alert.error(I18n.getText("error"), I18n.getText("error.couldNotRedate"));
		
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
		combo = new ComboBoxFilterable(NormalTridasDatingType.values());
		
		combo.setRenderer(new EnumComboBoxItemRenderer());
		combo.setSelectedItem(datingType);
		
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datingType = (NormalTridasDatingType) combo.getSelectedItem();
				
			}
		});
		
		return combo;
	}
	
	private void setup(Range startRange) {
		// set title
		setTitle(I18n.getText("menus.tools.redate"));

		// kill me when i'm gone
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// grab data
		range = startRange;

		// dialog is a boxlayout
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
		setContentPane(mainPanel);
		mainPanel.setLayout(new MigLayout("", "[468px,grow]", "[][grow,fill][]"));

		mainPanel.add(getTopPanel(), "cell 0 0,alignx left,aligny center");
		
				// name, version, justificaiton panel
				infoPanel = new NameVersionJustificationPanel(sample, false, true);
				mainPanel.add(infoPanel, "cell 0 1,grow");
				
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				mainPanel.add(panel, "cell 0 2,grow");
				
				JButton btnOk = new JButton("OK");
				btnOk.setFont(new Font("Dialog", Font.PLAIN, 12));
				btnOk.addActionListener(new ActionListener(){


					@Override
					public void actionPerformed(ActionEvent arg0) {

						boolean isOk = true;
						boolean rangeChanged = !sample.getRange().equals(range);

						if (isOk && (rangeChanged || datingType != originalDatingType)) {
							if(!performRedate())
								return;
						}
						sample.fireDisplayCalendarChanged();
						dispose();
					}
				});
				panel.add(btnOk);
				
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setFont(new Font("Dialog", Font.PLAIN, 12));
				
				btnCancel.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						dispose();
					}
					
				});
				panel.add(btnCancel);

		// esc => cancel, return => ok
		OKCancel.addKeyboardDefaults(btnOk);
	}

	private JPanel getTopPanel() {
		JPanel variablePanel = new JPanel();
		
		JLabel lblNewRange = new JLabel();
		txtStartYear = new JTextField(6);
		JLabel lblTo = new JLabel();
		txtEndYear = new JTextField(6);
		JLabel lblDating = new JLabel();
		cboDatingType = getDatingTypeComboBox();

		lblNewRange.setText(I18n.getText("redate.new_range") + ":");

		lblTo.setText(I18n.getText("general.to"));

		startListener = new StartListener();
		txtStartYear.setText(range.getStart().toString());
		txtStartYear.getDocument().addDocumentListener(startListener);

		endListener = new EndListener();
		txtEndYear.setText(range.getEnd().toString());
		txtEndYear.getDocument().addDocumentListener(endListener);

		lblDating.setText(I18n.getText("general.dating") + ":");
		variablePanel.setLayout(new MigLayout("", "[83px][70px][10px:10px:10px][78px][10px:10px:10px][159px]", "[19px][24px]"));
		variablePanel.add(lblNewRange, "cell 0 0,alignx left,aligny center");
		variablePanel.add(lblDating, "cell 0 1,alignx left,aligny center");
		variablePanel.add(txtStartYear, "cell 1 0,alignx left,aligny top");
		variablePanel.add(lblTo, "cell 3 0,alignx left,aligny center");
		variablePanel.add(txtEndYear, "cell 5 0,alignx left,aligny top");
		variablePanel.add(combo, "cell 1 1 5 1,growx,aligny top");

		return variablePanel;
	}
}
