package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.bulkdataentry.view.QuickEntrySample.Task;
import org.tellervo.desktop.components.table.ControlledVocDictionaryComboBox;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.dbbrowse.TridasObjectRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ArrayListModel;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;
import javax.swing.JTextPane;
import javax.swing.JProgressBar;

public class QuickEntryElement extends JDialog implements ActionListener, PropertyChangeListener {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JSpinner spnFirstElementCode;
	private JSpinner spnNumberOfElements;
	private JComboBox cboTaxon;
	private JComboBox cboElementType;
	private JComboBox cboObject;
	private IBulkImportSectionModel model;
	protected ArrayListModel<TridasObject> objModel = new ArrayListModel<TridasObject>();
	private JLabel lblPrefix;
	private JProgressBar progressBar;
	private JButton okButton;
	private JButton cancelButton;
	private Task task;

	
	/**
	 * Create the dialog.
	 */
	@SuppressWarnings("unchecked")
	public QuickEntryElement(Component parent, IBulkImportSectionModel model) {
		this.model = model;
		setTitle("Element quick entry");
		lblPrefix = new JLabel("");
		this.setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 371, 243);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][grow]", "[][][][30px:30px][30px:30px][grow]"));
		{
			JLabel lblObject = new JLabel("Parent object:");
			contentPanel.add(lblObject, "cell 0 0,alignx trailing");
		}
		{
			cboObject = new JComboBox();	
	    	TridasObjectRenderer rend = new TridasObjectRenderer();
	    	cboObject.setRenderer(rend);
	    	objModel = new ArrayListModel<TridasObject>(App.tridasObjects.getObjectList());
	    	cboObject.setModel(objModel); 	
			contentPanel.add(cboObject, "cell 1 0,growx");
			
			
			cboObject.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					TridasObjectEx obj = (TridasObjectEx) cboObject.getSelectedItem();
					lblPrefix.setText(obj.getLabCode()+"-");
				}
				
			});
			
			
			if(cboObject.getItemCount()>0) cboObject.setSelectedIndex(0);

		}
		{
			JLabel lblElementType = new JLabel("Element type:");
			contentPanel.add(lblElementType, "cell 0 1,alignx trailing");
		}
		{
			cboElementType = new ControlledVocDictionaryComboBox("elementTypeDictionary", ControlledVocRenderer.Behavior.NORMAL_ONLY);
			contentPanel.add(cboElementType, "cell 1 1,growx");
		}
		{
			JLabel lblTaxon = new JLabel("Taxon:");
			contentPanel.add(lblTaxon, "cell 0 2,alignx trailing");
		}
		{
			cboTaxon = new ControlledVocDictionaryComboBox("taxonDictionary", ControlledVocRenderer.Behavior.NORMAL_ONLY);
			contentPanel.add(cboTaxon, "cell 1 2,growx");
		}
		{
			JLabel lblFirstElementCode = new JLabel("First element code:");
			contentPanel.add(lblFirstElementCode, "cell 0 3");
		}
		{
			
			contentPanel.add(lblPrefix, "flowx,cell 1 3");
		}
		{
			spnFirstElementCode = new JSpinner();
			spnFirstElementCode.setMinimumSize(new Dimension(100, 5));
			spnFirstElementCode.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			contentPanel.add(spnFirstElementCode, "cell 1 3");
		}
		{
			JLabel lblNumberOfElements = new JLabel("Number of elements to create:");
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
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.setLayout(new MigLayout("", "[150px,grow][44px][70px]", "[27px]"));
				{
					progressBar = new JProgressBar();
					progressBar.setStringPainted(true);
					progressBar.setVisible(false);
					buttonPane.add(progressBar, "cell 0 0,growx,aligny center");
				}
				buttonPane.add(okButton, "cell 1 0,alignx left,aligny top");
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton, "cell 2 0,alignx left,aligny top");
			}
		}
		
		pack();
		this.setLocationRelativeTo(parent);
	}

	
	@SuppressWarnings("unchecked")
	private void generateElements()
	{
		okButton.setEnabled(false);
		cancelButton.setEnabled(false);
		
		if(model.getRows().size()==1)
		{
			IBulkImportSingleRowModel firstrow = (IBulkImportSingleRowModel) model.getRows().get(0);
			if(firstrow.getProperty(SingleElementModel.OBJECT)==null && 
					firstrow.getProperty(SingleElementModel.TITLE)==null)
			{
				model.getRows().remove(0);
			}
		}
		

		progressBar.setVisible(true);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		
        task = new Task();
    
        task.addPropertyChangeListener(this);
        task.execute();		

	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("OK"))
		{
			generateElements();
		}
		else if (event.getActionCommand().equals("Cancel"))
		{
			this.dispose();
		}
	}
	
	   class Task extends SwingWorker<Void, Void> {
	        /*
	         * Main task. Executed in background thread.
	         */
	        @Override
	        public Void doInBackground() {
	        	
	            //Initialize progress property.
	            setProgress(0);
	            
	            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	           
	            
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
    				

    				Double prg = (double) (((double)i/(double)numberOfElements)*100);
    				
    				setProgress(prg.intValue());
	    		}
	            
	       
	            return null;
	        }
	 
	        /*
	         * Executed in event dispatching thread
	         */
	        @Override
	        public void done() {            
	            setCursor(null); //turn off the wait cursor
				dispose();
	    
	        }
	    }

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			 if ("progress" == evt.getPropertyName()) {			 
		            int progress = (Integer) evt.getNewValue();
		            progressBar.setValue(progress);
		     } 
			
		}

	public JButton getOkButton() {
		return okButton;
	}
	public JButton getCancelButton() {
		return cancelButton;
	}
}
