package edu.cornell.dendro.corina.manip;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.table.DefaultTableModel;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.editor.DecadalModel;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.Center;

public class ReconcileWindow extends XFrame implements ReconcileNotifier, SaveableDocument {
	
	private ReconcileDataView dv1, dv2;
	private Sample s1, s2;
    private Sample originals1; // for aborting.
    private Sample originals2; // for aborting.
	
	private JButton btnShowHideRef; // shows/hides our ref panel
	private JButton btnCancel;
	private JButton btnFinish;
	private JButton btnViewType;
	private JButton btnRemeasure;
	private JPanel refPanel; // the panel with the reference measurement
	private JSeparator sepLine;
	
	private boolean extraIsShown = false;
	private final static String EXPAND = "Show reference";
	private final static String HIDE = "Hide reference";
	
	public ReconcileWindow(Sample s1, Sample s2) {
		JPanel content = new JPanel(new BorderLayout());
		
		setTitle("Reconciliation: " + s1.toString());

		// create a copy of our samples before we even touch them
		// we keep s2 now for future compatibility, when we might have multiple
		// windows referring to s2 open
		originals1 = new Sample();
		originals2 = new Sample();
		Sample.copy(s1, originals1);
		Sample.copy(s2, originals2);		

		this.s1 = s1;
		this.s2 = s2;
		dv1 = new ReconcileDataView(s1, s2);
		dv2 = new ReconcileDataView(s2, s1);
		
		dv1.setReconcileNotifier(this);
		dv2.setReconcileNotifier(this);
		
		JPanel reconcilePanel = new JPanel();
		reconcilePanel.setLayout(new BoxLayout(reconcilePanel, BoxLayout.Y_AXIS));
		
		refPanel = new JPanel(new BorderLayout());
		refPanel.add(createReconcilePane(s2, dv2), BorderLayout.CENTER);
		refPanel.setVisible(extraIsShown);
		
		
		reconcilePanel.add(createReconcilePane(s1, dv1));
		reconcilePanel.add(createButtonPanel());
		reconcilePanel.add(refPanel);
		
		content.add(reconcilePanel, BorderLayout.CENTER);
		
		setContentPane(content);
		
		pack();
		setVisible(true);
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		btnShowHideRef = new JButton(extraIsShown ? HIDE : EXPAND);
		btnFinish = new JButton("Finish");
		btnRemeasure = new JButton("Remeasure selected ring");
		btnCancel = new JButton("Cancel");
		btnViewType = new JButton("Graph");
		sepLine = new javax.swing.JSeparator();
		
		// TODO - hide until implemented
		btnViewType.setVisible(false);
		
		sepLine.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		// Only enable apply button immediately if there are no errors at all, otherwise disable
		if (dv1.reconciliationErrorCount()==0){
			btnFinish.setEnabled(true);
		} else {
			btnFinish.setEnabled(false);
		}
			
		// glue for our buttons...
		final ReconcileWindow glue = this;

		// Layout the buttons
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(buttonPanel);
        buttonPanel.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(btnViewType)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnShowHideRef)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnRemeasure)                
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 361, Short.MAX_VALUE)
                .add(btnFinish)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnCancel))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, sepLine, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(sepLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnShowHideRef)
                    .add(btnCancel)
                    .add(btnFinish)
                    .add(btnRemeasure)
                    .add(btnViewType))
                .addContainerGap(26, Short.MAX_VALUE))
        );			
	
		
        btnViewType.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae){
        		// TODO 	
        		// Toggle between Graph and Table views
        	}
        });
        
		// Cancel should loose any unsaved changes so reload sample from db
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				if(s1.isModified() || s2.isModified()) {
					int ret = JOptionPane.showConfirmDialog(glue, 
							"Are you sure you want to abandon all \nthe changes you have made?\n\n" +
							"Click 'no' to return to the reconciler\nor 'yes' to confirm.\n\n",
							"Abandon changes?", JOptionPane.YES_NO_OPTION);
						
					if (ret == JOptionPane.YES_OPTION) {
						// restore our originals
						Sample.copy(originals1, s1);
						Sample.copy(originals2, s2);

						// let anything watching them know they changed
						s1.fireSampleMetadataChanged();
						s2.fireSampleMetadataChanged();
						
						// and go away
						dispose();
					} else {
						// Return to reconciler
						return;
					}
				}
				else {
					// not modified, just go away
					dispose();
				}
			}
		});
		
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				// This button should save changes to both current and
				// reference measurement and if there are completely
				// reconciled, then the isreconciled flags should be set
								
				// Mark as reconciled if neither measurement has errors otherwise make sure flag is set to false
				Boolean s1rec = (Boolean) s1.getMeta("isreconciled");
				Boolean s2rec = (Boolean) s2.getMeta("isreconciled");
				if (dv1.reconciliationErrorCount() == 0 && dv2.reconciliationErrorCount() == 0){
					if(s1rec == null || s1rec.booleanValue() == false) {
						s1.setMeta("isreconciled", true);
						s1.setModified();
					}
					
					if(s2rec == null || s2rec.booleanValue() == false) {
						s2.setMeta("isreconciled", true);
						s2.setModified();					
					}
				} else {
					if(s1rec == null || s1rec.booleanValue() == true) {
						s1.setMeta("isreconciled", false);
						s1.setModified();
					}
					
					if(s2rec == null || s2rec.booleanValue() == true) {
						s2.setMeta("isreconciled", false);
						s2.setModified();					
					}
				}

				// Save the current series
				if(s1.isModified())
				{
					try {
						s1.getLoader().save(s1);
					} catch (IOException ioe) {
						Alert.error("I/O Error", "There was an error while saving the reference measurement series: \n" + ioe.getMessage());
						return;
					} catch (Exception e) {
						new Bug(e);
					}
				}

				// Now save the reference series
				if(s2.isModified())
				{
					try {
						s2.getLoader().save(s2);
					} catch (IOException ioe) {
						Alert.error("I/O Error", "There was an error while saving the current measurement series: \n" + ioe.getMessage());
						return;
					} catch (Exception e) {
						new Bug(e);
					}
				}

				// Warn user if there are errors remaining.
				int nErrors;
				if ((nErrors = dv1.reconciliationErrorCount()) > 0) {
					Alert.message("Reconcilitation Error", "There "
							+ (nErrors > 1 ? "are" : "is") + " still "
							+ nErrors + " error" + (nErrors > 1 ? "s" : "") 
							+ " remaining.\nPlease remember to fix this later!");
				}
				
				// set the necessary bits...
				s1.clearModified();
				s2.clearModified();
				
				// and close!
				close();								
			
			}
		});
		
		btnRemeasure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int col = dv1.myTable.getSelectedColumn();
				int row = dv1.myTable.getSelectedRow();

				Year y = ((DecadalModel) dv1.myTable.getModel()).getYear(row, col);
				
				ReconcileMeasureDialog dlg = new ReconcileMeasureDialog(glue, true, s1, s2, y);
				
				Center.center(dlg, glue);
				dlg.setVisible(true);

				// did we get a value?
				Integer value = dlg.getFinalValue();
				if(value != null) {
					// kludge in some updates!
					((DecadalModel) dv1.myTable.getModel()).setValueAt(value, row, col);
					((DecadalModel) dv2.myTable.getModel()).setValueAt(value, row, col);
					
					dv1.myTable.setColumnSelectionInterval(col, col);
					dv1.myTable.setRowSelectionInterval(row, row);
					dv2.myTable.setColumnSelectionInterval(col, col);
					dv2.myTable.setRowSelectionInterval(row, row);
				}
			}
		});

		btnShowHideRef.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// toggle visibility
				extraIsShown = !extraIsShown;

				// hide it and resize it
				glue.setVisible(false);
				
				btnShowHideRef.setText(extraIsShown ? HIDE : EXPAND);
				refPanel.setVisible(extraIsShown);
				glue.pack();
				
				// make sure we're not freakin' huge
				int height = Toolkit.getDefaultToolkit().getScreenSize().height;
				if(glue.getHeight() > height - 100)
					glue.setSize(glue.getWidth(), height - 100);
				
				// re-visible
				glue.setVisible(true);
				
				// re-center 
				Center.center(glue);
			}
		});
		
		return buttonPanel;
	}
	
	private JPanel createReconcilePane(Sample s, ReconcileDataView dv) {
		JPanel p = new JPanel();
		
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p.setBorder(BorderFactory.createTitledBorder(s.toString()));
		
		p.add(dv);
		
		return p;
	}

	public void reconcileSelectionChanged(ReconcileDataView dataview) {
		ReconcileDataView dv;
		
		// we want to notify the other reconciler
		if(dataview == dv1)
			dv = dv2;
		else
			dv = dv1;
		
		dv.duplicateSelectionFrom(dataview);
		
		// enable if it's a valid index into both datasets!
		Year y = dv1.getSelectedYear();
		int col = dv1.myTable.getSelectedColumn();
		int idx = y.diff(s1.getStart());
		btnRemeasure.setEnabled(col > 1 && col < 11 && idx >= 0 && idx < s1.getData().size() && idx < s2.getData().size());
	}

	public void reconcileDataChanged(ReconcileDataView dataview) {
		ReconcileDataView dv;
		
		// we want to notify the other reconciler
		if(dataview == dv1)
			dv = dv2;
		else
			dv = dv1;
		
		dv.forceReconciliation();
		
		// enable our apply button?
		if(s1.isModified() || s2.isModified()) btnFinish.setEnabled(true);
	}

	// SaveableDocument
	public String getDocumentTitle() {
		return "reference sample " + (String) s2.getMeta("title");
	}

	public String getFilename() {
		return null;
	}

	public Object getSaverClass() {
		return null;
	}

	public boolean isNameChangeable() {
		return false;
	}

	public boolean isSaved() {
		return !s2.isModified();
	}

	public void save() {
		// now, actually try and save the sample
		try {
			s2.getLoader().save(s2);
		} catch (IOException ioe) {
			Alert.error("I/O Error", "There was an error while saving the sample: \n" + ioe.getMessage());
			return;
		} catch (Exception e) {
			new Bug(e);
		}

		// set the necessary bits...
		s2.clearModified();
	}
	
	public void setFilename(String fn) {
	}
}
