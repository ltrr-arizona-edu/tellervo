package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.components.table.ControlledVocDictionaryComboBox;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.dbbrowse.TridasObjectRenderer;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ArrayListModel;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasObject;

public class QuickEntryElement extends JDialog implements ActionListener {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JSpinner spnFirstElementCode;
	private JSpinner spnNumberOfElements;
	private JComboBox<ControlledVoc> cboTaxon;
	private JComboBox<ControlledVoc> cboElementType;
	private JComboBox<TridasObject> cboObject;
	private IBulkImportSectionModel model;
	protected ArrayListModel<TridasObject> objModel = new ArrayListModel<TridasObject>();

	
	/**
	 * Create the dialog.
	 */
	@SuppressWarnings("unchecked")
	public QuickEntryElement(Component parent, IBulkImportSectionModel model) {
		this.model = model;
		setTitle("Element quick entry");
		this.setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][grow]", "[][][][][][grow]"));
		{
			JLabel lblObject = new JLabel("Parent object:");
			contentPanel.add(lblObject, "cell 0 0,alignx trailing");
		}
		{
			cboObject = new JComboBox<TridasObject>();	
	    	TridasObjectRenderer rend = new TridasObjectRenderer();
	    	cboObject.setRenderer(rend);
	    	objModel = new ArrayListModel<TridasObject>(App.tridasObjects.getObjectList());
	    	cboObject.setModel(objModel);
	    	cboObject.setSelectedItem(null);
		
			
			contentPanel.add(cboObject, "cell 1 0,growx");
		}
		{
			JLabel lblElementType = new JLabel("Element type:");
			contentPanel.add(lblElementType, "cell 0 1,alignx trailing");
		}
		{
			cboElementType = new ControlledVocDictionaryComboBox("elementTypeDictionary");
			contentPanel.add(cboElementType, "cell 1 1,growx");
		}
		{
			JLabel lblTaxon = new JLabel("Taxon:");
			contentPanel.add(lblTaxon, "cell 0 2,alignx trailing");
		}
		{
			cboTaxon = new ControlledVocDictionaryComboBox("taxonDictionary");
			
			contentPanel.add(cboTaxon, "cell 1 2,growx");
		}
		{
			JLabel lblFirstElementCode = new JLabel("First element code:");
			contentPanel.add(lblFirstElementCode, "cell 0 3");
		}
		{
			spnFirstElementCode = new JSpinner();
			spnFirstElementCode.setMinimumSize(new Dimension(100, 5));
			spnFirstElementCode.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			contentPanel.add(spnFirstElementCode, "cell 1 3");
		}
		{
			JLabel lblNumberOfElements = new JLabel("Number of elements:");
			contentPanel.add(lblNumberOfElements, "cell 0 4");
		}
		{
			spnNumberOfElements = new JSpinner();
			spnNumberOfElements.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spnNumberOfElements.setMinimumSize(new Dimension(100, 5));
			contentPanel.add(spnNumberOfElements, "cell 1 4");
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
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		this.setLocationRelativeTo(parent);
	}

	
	@SuppressWarnings("unchecked")
	private void generateElements()
	{
		Integer firstElementCode = (Integer) spnFirstElementCode.getValue();
		Integer numberOfElements = (Integer) spnNumberOfElements.getValue();
		
		for(int i=firstElementCode; i<(firstElementCode+numberOfElements); i++)
		{
			 IBulkImportSingleRowModel element = model.createRowInstance();
			element.setProperty(SingleElementModel.TYPE, cboElementType.getSelectedItem());
			element.setProperty(SingleElementModel.OBJECT, cboObject.getSelectedItem());
			element.setProperty(SingleElementModel.TAXON, cboTaxon.getSelectedItem());

			element.setProperty(SingleElementModel.TITLE, i+"");
			model.getRows().add(element);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("OK"))
		{
			generateElements();
			this.dispose();
		}
		else if (event.getActionCommand().equals("Cancel"))
		{
			this.dispose();
		}
				
				
		
	}

	protected JSpinner getSpnFirstElementCode() {
		return spnFirstElementCode;
	}
	protected JSpinner getSpnNumberOfElements() {
		return spnNumberOfElements;
	}
	protected JComboBox<?> getCboTaxon() {
		return cboTaxon;
	}
	public JComboBox<?> getElementType() {
		return cboElementType;
	}
	public JComboBox<?> getObject() {
		return cboObject;
	}
}
