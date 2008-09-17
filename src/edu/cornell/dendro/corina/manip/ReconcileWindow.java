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
	
	private JButton showHide; // shows/hides our ref panel
	private JButton returnToEditor;
	private JButton saveChanges;
	private JButton markAsReconciled;
	private JButton remeasure;
	private JPanel refPanel; // the panel with the reference measurement
	
	private boolean extraIsShown = false;
	private final static String EXPAND = "Show ref";
	private final static String HIDE = "Hide ref";
	
	public ReconcileWindow(Sample s1, Sample s2) {
		JPanel content = new JPanel(new BorderLayout());
		
		setTitle("Reconciliation: " + s1.toString());

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
		
		showHide = new JButton(extraIsShown ? HIDE : EXPAND);
		saveChanges = new JButton("Save ref changes");
		markAsReconciled = new JButton("Finish reconcile");
		remeasure = new JButton("Remeasure selected");
		returnToEditor = new JButton("Return to editor");
		
		// we can't do these by default
		saveChanges.setEnabled(false);
		markAsReconciled.setEnabled(false);
		
		buttonPanel.add(markAsReconciled);
		buttonPanel.add(returnToEditor);
		buttonPanel.add(Box.createHorizontalStrut(18));	
		buttonPanel.add(remeasure);
		buttonPanel.add(Box.createHorizontalStrut(18));		
		buttonPanel.add(showHide);
		buttonPanel.add(saveChanges);

		// glue for our buttons...
		final ReconcileWindow glue = this;

		// call the 'close' method to potentially save any changes
		returnToEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				glue.close();
			}
		});
		
		markAsReconciled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// reconciling doesn't make sense if we can't do this...
				if(s2.isModified()) {
					int ret = JOptionPane.showConfirmDialog(glue, 
						"The reference sample has been modified.\nYou must save changes to continue.\n\n" +
						"Would you like to save changes to it?", 
						"Save reference?", JOptionPane.YES_NO_OPTION);
					
					if(ret == JOptionPane.YES_OPTION) {
						save();
						
						// save failed?
						if(s2.isModified())
							return;
					}
				}

				// ok, now actually mark as reconciled
				Boolean s1rec = (Boolean) s1.getMeta("isreconciled");
				Boolean s2rec = (Boolean) s2.getMeta("isreconciled");
				
				if(s1rec == null || s1rec.booleanValue() == false) {
					s1.setMeta("isreconciled", true);
					s1.setModified();
				}
				
				// well, should we mark both as reconciled?
				if(s2rec == null || s2rec.booleanValue() == false) {
					int ret = JOptionPane.showConfirmDialog(glue, 
							"The reference sample is not marked as reconciled.\n" +
							"Would you like to mark it as such?", 
							"Mark reference as reconciled?", JOptionPane.YES_NO_OPTION);
					
					if(ret == JOptionPane.YES_OPTION) {
						s2.setMeta("isreconciled", true);
						s2.setModified();
						
						// call our method that saves the reference (part of SaveableDocument)
						save();

						if(s2.isModified())
							return;
					}
				}

				int ret = JOptionPane.showConfirmDialog(glue, 
						"Would you like to save your changes?\n" +
						"(select no to save later)", 
						"Save changes?", JOptionPane.YES_NO_OPTION);
				
				if(ret == JOptionPane.YES_OPTION) {
					try {
						s1.getLoader().save(s1);
					} catch (IOException ioe) {
						Alert.error("I/O Error", "There was an error while saving the sample: \n" + ioe.getMessage());
						return;
					} catch (Exception e) {
						new Bug(e);
					}

					// set the necessary bits...
					s1.clearModified();
				}
				
				// and close!
				close();
			}
		});
		
		remeasure.addActionListener(new ActionListener() {
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

		showHide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// toggle visibility
				extraIsShown = !extraIsShown;

				// hide it and resize it
				glue.setVisible(false);
				
				showHide.setText(extraIsShown ? HIDE : EXPAND);
				refPanel.setVisible(extraIsShown);
				glue.pack();
				
				// make sure we're not freakin' huge
				int height = Toolkit.getDefaultToolkit().getScreenSize().height;
				if(glue.getHeight() > height - 100)
					glue.setSize(glue.getWidth(), height - 100);
				
				// re-visible
				glue.setVisible(true);
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
		remeasure.setEnabled(col > 1 && col < 11 && idx >= 0 && idx < s1.getData().size() && idx < s2.getData().size());
	}

	public void reconcileDataChanged(ReconcileDataView dataview) {
		ReconcileDataView dv;
		
		// we want to notify the other reconciler
		if(dataview == dv1)
			dv = dv2;
		else
			dv = dv1;
		
		dv.forceReconciliation();
		
		// enable our buttons?
		saveChanges.setEnabled(s2.isModified());
		markAsReconciled.setEnabled(dv1.reconciliationErrorCount() == 0 && dv2.reconciliationErrorCount() == 0);
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
