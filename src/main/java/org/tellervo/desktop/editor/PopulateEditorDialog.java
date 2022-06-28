package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ButtonGroup;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.manip.Redate;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.tridasv2.ui.EnumComboBoxItemRenderer;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasDating;

import javax.swing.JToggleButton;


public class PopulateEditorDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	protected final static Logger log = LoggerFactory.getLogger(PopulateEditorDialog.class);

	private final JPanel contentPanel = new JPanel();
	//private NormalTridasDatingType datingType ;
	private JComboBox cboDatingType;
	private JSpinner spnStartYear;
	private JSpinner spnRingCount;
	private AbstractEditor editor;
	private boolean disableStartYearListener = false;
	private boolean disableEndYearListener = false;
	private boolean disableListeners = false;
	
	private Integer minintval = Integer.MIN_VALUE;
	private Integer maxintval = Integer.MAX_VALUE;
	private Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
	private JToggleButton btnRingsLock;
	private JToggleButton btnStartYearLock;
	private JToggleButton btnEndYearLock;
	private JSpinner spnEndYear;
	private Range range;
	private JButton btnOK;
	
	/**
	 * Create the dialog.
	 */
	public PopulateEditorDialog(AbstractEditor editor) {
	
		this.setIconImage(Builder.getApplicationIcon());
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
	
		
		//datingType = NormalTridasDatingType.ABSOLUTE;
		
		setTitle(I18n.getText("menus.edit.populateditor"));
		setModal(true);
		setBounds(100, 100, 632, 293);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][158.00,left][][][67.00][][grow]", "[][][][grow,fill]"));
		{
			JLabel lblFirstYear = new JLabel("Years:");
			contentPanel.add(lblFirstYear, "cell 0 0,alignx right");
		}
		{
			spnStartYear = new JSpinner();
			spnStartYear.setModel(new SpinnerNumberModel(new Integer(1900), minintval, maxintval, new Integer(1)));
			contentPanel.add(spnStartYear, "cell 1 0,growx");
		}
		{
			btnStartYearLock = new JToggleButton("");
			btnStartYearLock.setActionCommand("LockChanged");
			btnStartYearLock.setIcon(Builder.getIcon("lock.png", 16));
			contentPanel.add(btnStartYearLock, "cell 2 0");
		}
		{
			JLabel lblTo = new JLabel("to:");
			contentPanel.add(lblTo, "cell 3 0,alignx trailing");
		}
		{
			spnEndYear = new JSpinner();
			spnEndYear.setModel(new SpinnerNumberModel(currentYear, minintval, maxintval, new Integer(1)));
			contentPanel.add(spnEndYear, "cell 4 0,growx");
		}
		{
			btnEndYearLock = new JToggleButton("");
			btnEndYearLock.setActionCommand("LockChanged");
			btnEndYearLock.setIcon(Builder.getIcon("lock.png", 16));
			contentPanel.add(btnEndYearLock, "cell 5 0");
		}
		{
			JLabel lblNumberOfRings = new JLabel("Number of rings:");
			contentPanel.add(lblNumberOfRings, "cell 0 1,alignx right");
		}
		{
			spnRingCount = new JSpinner();
			spnRingCount.setModel(new SpinnerNumberModel(new Integer(100), new Integer(1), null, new Integer(1)));
			contentPanel.add(spnRingCount, "cell 1 1 4 1,growx");
		}
		{
			btnRingsLock = new JToggleButton("");
			btnRingsLock.setActionCommand("LockChanged");
			btnRingsLock.setSelected(true);
			btnRingsLock.setIcon(Builder.getIcon("lock.png", 16));
			contentPanel.add(btnRingsLock, "cell 5 1");
		}
		{
			JLabel lblDatingType = new JLabel("Dating type:");
			contentPanel.add(lblDatingType, "cell 0 2,alignx trailing");
		}
		{
			cboDatingType = new ComboBoxFilterable(NormalTridasDatingType.values());
			
			cboDatingType.setRenderer(new EnumComboBoxItemRenderer());
			cboDatingType.setSelectedItem(NormalTridasDatingType.ABSOLUTE);
			
			cboDatingType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//datingType = (NormalTridasDatingType) cboDatingType.getSelectedItem();
					
					validateValues();
				}
			});
			
			
			contentPanel.add(cboDatingType, "cell 1 2 5 1,growx");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnOK = new JButton("OK");
				btnOK.setActionCommand("OK");
				btnOK.addActionListener(this);
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
			}
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				btnCancel.addActionListener(this);
				buttonPane.add(btnCancel);
			}
		}
		
		this.spnStartYear.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent evt) {
				
				if(disableListeners) return;
				disableListeners = true;
				
				if(btnRingsLock.isSelected())
				{
					// Rings are locked so change end year
					recalculateEndYear();
				}
				else
				{
				
					recalculateRingCount();
				}

				disableListeners = false;

				
				validateValues();
			}
			
		});
		
		this.spnEndYear.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent evt) {
				
				if(disableListeners) return;
				disableListeners = true;
				
				if(btnRingsLock.isSelected())
				{
					// Rings locked so calculate start year
					recalculateStartYear();
				}
				else
				{
					// Start year is locked so change rings
					recalculateRingCount();
				}
				
				disableListeners = false;


				validateValues();
			}
			
		});
		
		this.spnRingCount.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				if(disableListeners) return;
				disableListeners = true;
	
				if(btnStartYearLock.isSelected())
				{
					// Start year is locked so change end year
					recalculateEndYear();

				}
				else
				{
					// End year is locked so change start year
					recalculateStartYear();

				}
				
				disableListeners = false;

				validateValues();
			}
			
			
		});
		
		
		
		ButtonGroup group = new ButtonGroup();
		group.add(btnEndYearLock);
		group.add(btnStartYearLock);
		group.add(btnRingsLock);
		setLockedGUI();
		
		btnEndYearLock.addActionListener(this);
		btnStartYearLock.addActionListener(this);
		btnRingsLock.addActionListener(this);
		
		recalculateRingCount();
		spnStartYear.setEditor(new JSpinner.NumberEditor(spnStartYear, "####"));
		spnEndYear.setEditor(new JSpinner.NumberEditor(spnEndYear, "####"));
		this.setLocationRelativeTo(editor);
		
	}

	private void setLockedGUI()
	{
		this.spnRingCount.setEnabled(true);
		this.spnStartYear.setEnabled(true);
		this.spnEndYear.setEnabled(true);
		
		if(btnRingsLock.isSelected())
		{
			spnRingCount.setEnabled(false);
		}
		else if (btnStartYearLock.isSelected())
		{
			spnStartYear.setEnabled(false);
		}
		else if (btnEndYearLock.isSelected())
		{
			spnEndYear.setEnabled(false);
		}
	}
	
	private void recalculateRingCount()
	{
		range = new Range(new Year((Integer)spnStartYear.getValue()), new Year((Integer)spnEndYear.getValue()));
		spnRingCount.setValue(range.getSpan());
	}
	
	private void recalculateStartYear()
	{
		
		Year startYear = new Year((Integer)spnEndYear.getValue());
		startYear = startYear.add(0-(Integer)spnRingCount.getValue());
		spnStartYear.setValue(Integer.parseInt(startYear.toString()));
		
	}

	private void recalculateEndYear()
	{
		Year endYear = new Year((Integer)spnStartYear.getValue());
		endYear = endYear.add((Integer)spnRingCount.getValue());
		spnEndYear.setValue(Integer.parseInt(endYear.toString()));
		
	}
	
	private void updateStartYearModel(Integer forceValue)
	{
	/*	if(disableStartYearListener) return;
		
		Integer value = forceValue;
		Integer maxValue = Integer.parseInt(range.getEnd().toString())-1;
		if(value==null) value = Integer.parseInt(range.getStart().toString());
		if(value>maxValue) value = maxValue;
		spnStartYear.setModel(new SpinnerNumberModel(value, null, maxValue, new Integer(1)));*/
	}
	
	private void updateEndYearModel(Integer forceValue)
	{
		/*if(disableEndYearListener) return;
		
		Integer value = forceValue;
		Integer minValue = Integer.parseInt(range.getStart().toString())+1;
		if(value==null) value = Integer.parseInt(range.getEnd().toString());
		if(value<minValue) value = minValue;
		Integer maxValue = Calendar.getInstance().get(Calendar.YEAR);
		
		spnEndYear.setModel(new SpinnerNumberModel(value, minValue, maxValue, new Integer(1)));*/
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
			dating.setType((NormalTridasDatingType) this.cboDatingType.getSelectedItem());
			sample.postEdit(Redate.redate(sample, range, dating));
			//sample.createEmptyValuesGroup(NormalTridasVariable.RING_WIDTH);
			

			
			this.dispose();
		}
		
		else if (event.getActionCommand().equals("Cancel"))
		{
			this.dispose();
		}
		else if (event.getActionCommand().equals("LockChanged"))
		{
			setLockedGUI();
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
	
	private void validateValues()
	{
		log.debug("Validating selections");
		
		this.btnOK.setEnabled(true);
		
		if((Integer)this.spnRingCount.getValue()<10)
		{
			this.btnOK.setEnabled(false);
		}
		
		if(this.cboDatingType.getSelectedItem().equals(NormalTridasDatingType.RELATIVE))
		{
			
		}
		else
		{
			// Can't have future dates
			
			if((Integer)this.spnEndYear.getValue()>currentYear)
			{
				this.btnOK.setEnabled(false);
			}
		}
		
		
	}



}
