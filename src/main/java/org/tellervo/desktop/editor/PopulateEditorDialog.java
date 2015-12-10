package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.manip.Redate;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.tridasv2.ui.EnumComboBoxItemRenderer;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasDating;


public class PopulateEditorDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private NormalTridasDatingType datingType ;
	private JSpinner spnStartYear;
	private JSpinner spnRingCount;
	private AbstractEditor editor;
	private boolean disableStartYearListener = false;
	private boolean disableEndYearListener = false;
	
	private JSpinner spnEndYear;
	private Range range;
	
	/**
	 * Create the dialog.
	 */
	public PopulateEditorDialog(AbstractEditor editor) {
	
		this.editor = editor;
		setResizable(false);
		range = new Range(new Year(Calendar.getInstance().get(Calendar.YEAR)-99), new Year(Calendar.getInstance().get(Calendar.YEAR)));

		
		try{
			if(editor.getSample().getRingWidthData().size()>0)	
			{
				Alert.error(editor, "Error", "Unable to initialize as data grid has already been initialized!");
				dispose();
				return;
			}
		} catch (Exception e)
		{
			
		}
	
		
		datingType = NormalTridasDatingType.ABSOLUTE;
		
		setTitle(I18n.getText("menus.edit.populateditor"));
		setModal(true);
		setBounds(100, 100, 364, 269);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][74.00,left][][67.00][grow]", "[][][][grow,fill]"));
		{
			JLabel lblFirstYear = new JLabel("Years:");
			contentPanel.add(lblFirstYear, "cell 0 0,alignx right");
		}
		{
			spnStartYear = new JSpinner();
			contentPanel.add(spnStartYear, "cell 1 0,growx");
		}
		{
			JLabel lblTo = new JLabel("to:");
			contentPanel.add(lblTo, "cell 2 0,alignx trailing");
		}
		{
			spnEndYear = new JSpinner();
			contentPanel.add(spnEndYear, "cell 3 0,growx");
		}
		{
			JLabel lblNumberOfRings = new JLabel("Number of rings:");
			contentPanel.add(lblNumberOfRings, "cell 0 1,alignx right");
		}
		{
			spnRingCount = new JSpinner();
			spnRingCount.setEnabled(false);
			spnRingCount.setModel(new SpinnerNumberModel(new Integer(100), new Integer(1), null, new Integer(1)));
			contentPanel.add(spnRingCount, "cell 1 1 3 1,growx");
		}
		{
			JLabel lblDatingType = new JLabel("Dating type:");
			contentPanel.add(lblDatingType, "cell 0 2,alignx trailing");
		}
		{
			final JComboBox cboDatingType = new ComboBoxFilterable(NormalTridasDatingType.values());
			
			cboDatingType.setRenderer(new EnumComboBoxItemRenderer());
			cboDatingType.setSelectedItem(datingType);
			
			cboDatingType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					datingType = (NormalTridasDatingType) cboDatingType.getSelectedItem();
				}
			});
			
			
			contentPanel.add(cboDatingType, "cell 1 2 3 1,growx");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		
		this.spnStartYear.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent evt) {
				recalculateRingCount();
				
				if(((Integer)spnStartYear.getValue())<((Integer)spnEndYear.getValue()))
				{
					updateEndYearModel((Integer)spnEndYear.getValue());
				}

			}
			
		});
		
		this.spnEndYear.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent evt) {
				recalculateRingCount();
				
				if(((Integer)spnEndYear.getValue())>((Integer)spnStartYear.getValue()))
				{
					updateStartYearModel((Integer)spnStartYear.getValue());
				}

			}
			
		});
		
		updateEndYearModel(null);
		updateStartYearModel(null);
		spnStartYear.setEditor(new JSpinner.NumberEditor(spnStartYear, "####"));
		spnEndYear.setEditor(new JSpinner.NumberEditor(spnEndYear, "####"));
		this.setLocationRelativeTo(editor);
		
	}

	private void recalculateRingCount()
	{
		range = new Range(new Year((Integer)spnStartYear.getValue()), new Year((Integer)spnEndYear.getValue()));
		spnRingCount.setValue(range.getSpan());
	}

	private void updateStartYearModel(Integer forceValue)
	{
		if(disableStartYearListener) return;
		
		Integer value = forceValue;
		Integer maxValue = Integer.parseInt(range.getEnd().toString())-1;
		if(value==null) value = Integer.parseInt(range.getStart().toString());
		if(value>maxValue) value = maxValue;
		spnStartYear.setModel(new SpinnerNumberModel(value, null, maxValue, new Integer(1)));
	}
	
	private void updateEndYearModel(Integer forceValue)
	{
		if(disableEndYearListener) return;
		
		Integer value = forceValue;
		Integer minValue = Integer.parseInt(range.getStart().toString())+1;
		if(value==null) value = Integer.parseInt(range.getEnd().toString());
		if(value<minValue) value = minValue;
		Integer maxValue = Calendar.getInstance().get(Calendar.YEAR);
		
		spnEndYear.setModel(new SpinnerNumberModel(value, minValue, maxValue, new Integer(1)));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if(event.getActionCommand().equals("OK"))
		{

			editor.getSeriesDataMatrix().insertYears(0, Integer.parseInt(spnRingCount.getValue().toString()), 0 ,2);
			Sample sample = editor.getSample();
			Range range = sample.getRange();
			range = range.redateStartTo(new Year((Integer) this.spnStartYear.getValue()));
			TridasDating dating = new TridasDating();
			dating.setType(datingType);
			sample.postEdit(Redate.redate(sample, range, dating));
			//sample.createEmptyValuesGroup(NormalTridasVariable.RING_WIDTH);
			

			
			this.dispose();
		}
		
		if (event.getActionCommand().equals("Cancel"))
		{
			this.dispose();
		}
	}
	
	/*private class EndListener implements DocumentListener {
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
				range = new Range(new Year(range.getStart().toString()), Integer.parseInt(spnRingCount.getValue().toString()));	
				range = range.redateEndTo(new Year(value));
				startField.setText(range.getStart().toString());
			} catch (NumberFormatException nfe) {
				startField.setText(I18n.getText("error"));
			}

			// re-enable startListener
			startField.getDocument().addDocumentListener(startListener);
		}
	}*/

	/** A DocumentListener for the starting value */
	/*private class StartListener implements DocumentListener {
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

			// if it's the same, do nothing -- (not worth the code)

			// set the range
			try {
				range = new Range(new Year(startField.getText()), Integer.parseInt(spnRingCount.getValue().toString()));				
				endField.setText(range.getEnd().toString());
			} catch (NumberFormatException nfe) {
				endField.setText(I18n.getText("error"));
			}

			// re-enable endListener
			endField.getDocument().addDocumentListener(endListener);
		}
	}*/


}
