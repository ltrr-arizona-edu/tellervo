package edu.cornell.dendro.corina.manip;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.editor.DecadalModel;
import edu.cornell.dendro.corina.editor.ReconcileDataView;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.Center;

public class ReconcileWindow extends XFrame implements ReconcileNotifier {
	
	private ReconcileDataView dv1, dv2;
	private Sample s1, s2;
	
	private JButton showHide; // shows/hides our ref panel
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
		saveChanges = new JButton("Save changes");
		markAsReconciled = new JButton("Finish reconcile");
		remeasure = new JButton("Remeasure selected");
		
		// we can't do these by default
		saveChanges.setEnabled(false);
		markAsReconciled.setEnabled(false);
		
		buttonPanel.add(saveChanges);
		buttonPanel.add(markAsReconciled);
		buttonPanel.add(Box.createHorizontalStrut(18));	
		buttonPanel.add(remeasure);
		buttonPanel.add(Box.createHorizontalStrut(18));		
		buttonPanel.add(showHide);

		// glue for our buttons...
		final ReconcileWindow glue = this;
		
		
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
		saveChanges.setEnabled(s1.isModified() || s2.isModified());
		markAsReconciled.setEnabled(dv1.reconciliationErrorCount() == 0 && dv2.reconciliationErrorCount() == 0);
	}
}
