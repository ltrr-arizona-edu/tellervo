package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.components.table.ControlledVocDictionaryComboBox;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.JTableSpreadsheetAdapter;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasElement;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JProgressBar;

public class QuickEntrySample extends JDialog implements ActionListener, PropertyChangeListener {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private IBulkImportSectionModel sampleModel;
	private IBulkImportSectionModel elementModel;
	private JComboBox<ControlledVoc> cboSampleType;
	private JSpinner spnSampleNumber;
	private JProgressBar progressBar;
	private Task task;
	private JButton okButton;
	private JButton cancelButton;
	private final static Logger log = LoggerFactory.getLogger(QuickEntrySample.class);

	/**
	 * Create the dialog.
	 */
	public QuickEntrySample(Component parent, IBulkImportSectionModel sampleModel, IBulkImportSectionModel elementModel) {
		
		this.sampleModel = sampleModel;
		this.elementModel = elementModel;
		
		setTitle("Sample quick entry");
		this.setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][grow]", "[][][grow,fill]"));
		{
			JLabel lblType = new JLabel("Sample type:");
			contentPanel.add(lblType, "cell 0 0,alignx trailing");
		}
		{
			cboSampleType = new ControlledVocDictionaryComboBox("sampleTypeDictionary", ControlledVocRenderer.Behavior.NORMAL_ONLY);
			contentPanel.add(cboSampleType, "cell 1 0,growx");
		}
		{
			JLabel lblSampleCode = new JLabel("Number of samples per element:");
			contentPanel.add(lblSampleCode, "cell 0 1,alignx trailing");
		}
		{
			spnSampleNumber = new JSpinner();
			spnSampleNumber.setModel(new SpinnerNumberModel(1, 0, 10, 1));
			contentPanel.add(spnSampleNumber, "cell 1 1");
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.setLayout(new MigLayout("", "[150px,grow,fill][44px][70px]", "[27px]"));
				{
					progressBar = new JProgressBar();
					progressBar.setStringPainted(true);
					progressBar.setVisible(false);
					buttonPane.add(progressBar, "cell 0 0,alignx left,aligny center");
				}
				buttonPane.add(okButton, "cell 1 0,alignx left,aligny top");
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton, "cell 2 0,alignx left,aligny top");
			}
		}
		
		pack();
		this.setLocationRelativeTo(parent);
	}


	private void generateSamples()
	{
		
		okButton.setEnabled(false);
		cancelButton.setEnabled(false);
		
		// Remove the first row if it's empty
		if(sampleModel.getRows().size()==1)
		{
			IBulkImportSingleRowModel firstrow = (IBulkImportSingleRowModel) sampleModel.getRows().get(0);
			if(firstrow.getProperty(SingleSampleModel.OBJECT)==null && 
					firstrow.getProperty(SingleSampleModel.ELEMENT)==null && 
					firstrow.getProperty(SingleSampleModel.TITLE)==null)
			{
				sampleModel.getRows().remove(0);
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
			generateSamples();
		}
		else if (event.getActionCommand().equals("Cancel"))
		{
			task.done();
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
            
            int totalRows = elementModel.getRows().size()*(Integer)spnSampleNumber.getValue();
            int currentRow = 0;
            
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
    				
    				currentRow++;
    				Double prg = (double) (((double)currentRow/(double)totalRows)*100);
    				
    				setProgress(prg.intValue());
    			}
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

}
