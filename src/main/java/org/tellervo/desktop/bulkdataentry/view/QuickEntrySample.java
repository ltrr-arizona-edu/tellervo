package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.components.table.ControlledVocDictionaryComboBox;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasElement;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class QuickEntrySample extends JDialog implements ActionListener {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private IBulkImportSectionModel sampleModel;
	private IBulkImportSectionModel elementModel;
	private JComboBox<ControlledVoc> cboSampleType;
	private JSpinner spnSampleNumber;

	/**
	 * Create the dialog.
	 */
	public QuickEntrySample(Component parent, IBulkImportSectionModel sampleModel, IBulkImportSectionModel elementModel) {
		
		this.sampleModel = sampleModel;
		this.elementModel = elementModel;
		
		setTitle("Sample quick entry");
		this.setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][grow]", "[][][grow,fill]"));
		{
			JLabel lblSampleCode = new JLabel("Number of samples per element:");
			contentPanel.add(lblSampleCode, "cell 0 0,alignx trailing");
		}
		{
			spnSampleNumber = new JSpinner();
			spnSampleNumber.setModel(new SpinnerNumberModel(1, 0, 10, 1));
			contentPanel.add(spnSampleNumber, "cell 1 0");
		}
		{
			JLabel lblType = new JLabel("Sample type:");
			contentPanel.add(lblType, "cell 0 1,alignx trailing");
		}
		{
			cboSampleType = new ControlledVocDictionaryComboBox("sampleTypeDictionary");
			contentPanel.add(cboSampleType, "cell 1 1,growx");
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
		
		this.setLocationRelativeTo(parent);
	}

	@SuppressWarnings("unchecked")
	private void generateSamples()
	{
		for(Object row : elementModel.getRows())
		{
			TridasElement element = new TridasElement();
			
			
			
			SingleElementModel elementRow = (SingleElementModel) row;
			
			elementRow.populateToTridasElement(element);
			
			String[] codes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
			for(int i=0; i<((Integer)spnSampleNumber.getValue()); i++)
			{
			
				
				IBulkImportSingleRowModel sample = sampleModel.createRowInstance();
				sample.setProperty(SingleSampleModel.OBJECT, elementRow.getProperty(SingleElementModel.OBJECT));
				sample.setProperty(SingleSampleModel.ELEMENT, element);
				sample.setProperty(SingleSampleModel.TYPE, cboSampleType.getSelectedItem());
				sample.setProperty(SingleSampleModel.TITLE, codes[i]);
	
				sampleModel.getRows().add(sample);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("OK"))
		{
			generateSamples();
			this.dispose();
		}
		else if (event.getActionCommand().equals("Cancel"))
		{
			this.dispose();
		}
				
				
		
	}
	

	protected JSpinner getSpinner() {
		return spnSampleNumber;
	}
}
