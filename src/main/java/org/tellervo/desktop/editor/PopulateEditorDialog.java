package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.manip.Redate;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.tridasv2.ui.EnumComboBoxItemRenderer;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasDating;
import org.tellervo.desktop.Year;
import javax.swing.JTextField;


public class PopulateEditorDialog extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();
	private NormalTridasDatingType datingType ;
	private SampleDataView dataView;
	private JTextField startField;
	private JSpinner spnRingCount;
	private Window parent;
	/** Text field document listeners to auto-update each other */
	private DocumentListener startListener, endListener;
	private JTextField endField;
	private Range range;
	
	/**
	 * Create the dialog.
	 */
	public PopulateEditorDialog( Window parent, SampleDataView dataView) {
	
		super(parent);
		setResizable(false);
		this.dataView = dataView;
		this.parent = parent;
		
		try{
			if(dataView.getSample().getRingWidthData().size()>0)	
			{
				Alert.error(parent, "Error", "Unable to initialize as data grid has already been initialized!");
				dispose();
				return;
			}
		} catch (Exception e)
		{
			
		}
	
		
		datingType = NormalTridasDatingType.RELATIVE;
		
		setTitle(I18n.getText("menus.edit.populateditor"));
		setModal(true);
		setBounds(100, 100, 364, 166);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][74.00,left][][67.00][grow]", "[][][][grow,fill]"));
		{
			JLabel lblNumberOfRings = new JLabel("Number of rings:");
			contentPanel.add(lblNumberOfRings, "cell 0 0,alignx right");
		}
		{
			spnRingCount = new JSpinner();
			spnRingCount.setModel(new SpinnerNumberModel(new Integer(100), new Integer(1), null, new Integer(1)));
			contentPanel.add(spnRingCount, "cell 1 0 3 1,growx");
		}
		{
			JLabel lblFirstYear = new JLabel("Years:");
			contentPanel.add(lblFirstYear, "cell 0 1,alignx right");
		}
		{
			startField = new JTextField();
			startField.setText("1001");
			contentPanel.add(startField, "cell 1 1,growx");
		}
		{
			JLabel lblTo = new JLabel("to:");
			contentPanel.add(lblTo, "cell 2 1,alignx trailing");
		}
		{
			endField = new JTextField();
			endField.setText("1002");
			contentPanel.add(endField, "cell 3 1,growx");
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
		
		range = new Range(new Year("1001"), (int)spnRingCount.getValue() );
		
		startListener = new StartListener();
		startField.setText(range.getStart().toString());
		startField.getDocument().addDocumentListener(startListener);

		endListener = new EndListener();
		endField.setText(range.getEnd().toString());
		endField.getDocument().addDocumentListener(endListener);
		
		spnRingCount.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// set the range
				try {
					range = new Range(new Year(range.getStart().toString()), (int)spnRingCount.getValue());				
					endField.setText(range.getEnd().toString());
				} catch (NumberFormatException nfe) {
					endField.setText(I18n.getText("error"));
				}

			}
			
		});
		
		this.setLocationRelativeTo(parent);
		
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getActionCommand().equals("OK"))
		{
			dataView.insertYears(0, (int) this.spnRingCount.getValue(), 0 ,2);
			Sample sample = dataView.getSample();
			Range range = sample.getRange();
			range = range.redateStartTo(new Year(this.startField.getText()));
			TridasDating dating = new TridasDating();
			dating.setType(datingType);
			sample.postEdit(Redate.redate(sample, range, dating));
			this.dispose();
		}
		
		if (event.getActionCommand().equals("Cancel"))
		{
			this.dispose();
		}
	}
	
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
				range = new Range(new Year(range.getStart().toString()), (int)spnRingCount.getValue());				
				range = range.redateEndTo(new Year(value));
				startField.setText(range.getStart().toString());
			} catch (NumberFormatException nfe) {
				startField.setText(I18n.getText("error"));
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

			// if it's the same, do nothing -- (not worth the code)

			// set the range
			try {
				range = new Range(new Year(startField.getText()), (int)spnRingCount.getValue());				
				endField.setText(range.getEnd().toString());
			} catch (NumberFormatException nfe) {
				endField.setText(I18n.getText("error"));
			}

			// re-enable endListener
			endField.getDocument().addDocumentListener(endListener);
		}
	}


}
