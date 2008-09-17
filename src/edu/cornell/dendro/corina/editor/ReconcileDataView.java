package edu.cornell.dendro.corina.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.manip.ReconcileNotifier;
import edu.cornell.dendro.corina.manip.Reconciler;
import edu.cornell.dendro.corina.sample.CorinaWebElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.webdbi.ResourceIdentifier;

public class ReconcileDataView extends SampleDataView implements SampleListener {
	private JEditorPane reconcileInfo;
	private final static String reconcileIntro = "<html><font face=Dialog size=3>" +
		"<font color=green><u>Reconciling</u></font><br><font size=2>Click on a year for more information</font><p>";

	private Sample newSample, reference;
	private Reconciler reconciler;
	private ReconcileNotifier notifier;
	private TableSelectionChangeListener tableSelectionMonitor;
	
	public ReconcileDataView(Sample newSample, Sample reference) {
		super(newSample);		
		
		this.newSample = newSample;
		this.reference = reference;
		
		// create the reconciler
		reconciler = new Reconciler(newSample, reference);
		
		// create the place where we check our stuffs
		reconcileInfo = new JEditorPane();
		reconcileInfo.setContentType("text/html");
		reconcileInfo.setEditable(false);
		reconcileInfo.setPreferredSize(new Dimension(140, 100));
		reconcileInfo.setText(reconcileIntro);

		// reconcile...
		doReconciliation();

		// create our special renderer
		ReconcileRenderer rr = new ReconcileRenderer();
		for(int i = 1; i <= 10; i++)
			myTable.getColumnModel().getColumn(i).setCellRenderer(rr);		
		
		// make sure we're aware of selection changes
		tableSelectionMonitor = new TableSelectionChangeListener(myTable, this);
	    myTable.getSelectionModel().addListSelectionListener(tableSelectionMonitor);
	    myTable.getColumnModel().getSelectionModel()
	        .addListSelectionListener(tableSelectionMonitor);
	    
	    // this column is useless in this case and just takes up space!
	    myTable.getColumnModel().removeColumn(myTable.getColumnModel().getColumn(11));
		
		add(reconcileInfo, BorderLayout.EAST);
	}
	
	private void doReconciliation() {
		StringBuffer warnings = new StringBuffer();
		StringBuffer selectionInfo = new StringBuffer();
		
		// any global warnings
		if(newSample.getStart().compareTo(reference.getStart()) != 0) {
			warnings.append("- <font color=red>Samples do not have the same start years:</font>");
			warnings.append("(" + newSample.getStart() + "; ref: " + reference.getStart() + ")");
			warnings.append("<br>");
		}
		if(newSample.getData().size() != reference.getData().size()) {
			warnings.append("- <font color=red>Samples do not have the same length</font>");
			warnings.append("(" + newSample.getData().size() + "; ref: " + reference.getData().size() + ")");
			warnings.append("<br>");
		}
		if(reconciler.getFailureCount() != 0) {
			warnings.append("- <font color=red>" + reconciler.getFailureCount() + " reconciliation errors</font><br>");
		}
		if(warnings.length() != 0) {
			warnings.insert(0, "<b><u>Warnings:</u></b><br>");
			warnings.append("<p>");
		}
		
		// now, information for the currently selected point
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();
		
		if (row >= 0 && col >= 0) {
			Year y = ((DecadalModel) myModel).getYear(row, col);
			Set<Reconciler.FailureType> failures = reconciler.getFailuresForYear(y);
			
			selectionInfo.append("<b><u>" + y.toString() + ":</u></b><br>");
			
			// just some info
			int idx = y.diff(newSample.getStart());
			selectionInfo.append("ref: " + 
					((reference.getData().size() > idx) ? reference.getData().get(idx) : "<n/a>") + 
					"<br>");
			
			// now, each of the failures
			if(failures != null) {
				if(failures.contains(Reconciler.FailureType.THREEPERCENT)) 
					selectionInfo.append("- <font color=red>Not within three percent of reference</font><br>");
				if(failures.contains(Reconciler.FailureType.TRENDNEXT)) 
					selectionInfo.append("- <font color=red>Trend to next is inverse</font><br>");
				if(failures.contains(Reconciler.FailureType.TRENDPREV)) 
					selectionInfo.append("- <font color=red>Trend to previous is inverse</font><br>");
			}
		}

		reconcileInfo.setText(reconcileIntro + warnings.toString()
				+ selectionInfo.toString());		
	}
	
	public void forceReconciliation() {
		reconciler.rereconcile();
		doReconciliation();
	}
	
	public void duplicateSelectionFrom(ReconcileDataView dataview) {
		// update our selection
		int row = dataview.myTable.getSelectedRow();
		int col = dataview.myTable.getSelectedColumn();
		
		tableSelectionMonitor.setSelection(row, col);
	}
	
    private class TableSelectionChangeListener implements ListSelectionListener {
        private JTable table;
        private ReconcileDataView dataview;
        private int lastRow = -1;
        private int lastCol = -1;
        
        public TableSelectionChangeListener(JTable table, ReconcileDataView dataview) {
            this.table = table;
            this.dataview = dataview;
        }
        
        /**
         * Set the row and column without firing the listener
         * @param row
         * @param col
         */
        public void setSelection(int row, int col) {
            if(row == -1)
            	row = lastRow;
            if(col == -1)
            	col = lastCol;

            lastRow = row;
        	table.setRowSelectionInterval(row, row);
        	
        	lastCol = col;
        	table.setColumnSelectionInterval(col, col);
        	
        	doReconciliation();
        }
        
        public void valueChanged(ListSelectionEvent e) {
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
                       
            // changed?
            if(row != lastRow || col != lastCol) {
            	lastRow = row;
            	lastCol = col;
            	
            	if(notifier != null)
            		notifier.reconcileSelectionChanged(dataview);
            	
            	doReconciliation();
            }
        }
    }

	
	// This makes our table look neat!
	private class ReconcileRenderer extends DefaultTableCellRenderer {
		private final Border selBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
		private final Border noBorder = BorderFactory.createEmptyBorder();

		public ReconcileRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			Component cell = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);

			// right alignment
			super.setHorizontalAlignment(JLabel.RIGHT);

			Set<Reconciler.FailureType> failures = reconciler.getFailuresForYear(((DecadalModel) myModel).getYear(row, column));
			
			if(failures != null && !failures.isEmpty()) {
				cell.setForeground(Color.WHITE);
				cell.setBackground(Color.RED);
			} else {
				cell.setForeground(Color.BLACK);
				cell.setBackground(Color.GREEN);				
			}
			
			if(isSelected)
				setBorder(selBorder);
			else
				setBorder(noBorder);
			
			return cell;
		}
	}

	
	// SampleListener
	public void sampleRedated(SampleEvent e) {
		reconciler.rereconcile();
		doReconciliation();
		if(notifier != null)
			notifier.reconcileDataChanged(this);
		super.sampleRedated(e);
	}

	public void sampleDataChanged(SampleEvent e) {
		reconciler.rereconcile();
		doReconciliation();
		if(notifier != null)
			notifier.reconcileDataChanged(this);
		super.sampleDataChanged(e);
	}

	public void sampleMetadataChanged(SampleEvent e) {
		super.sampleMetadataChanged(e);
	}

	public void sampleElementsChanged(SampleEvent e) {
		super.sampleElementsChanged(e);
	}
	
	// ReconcileNotifier stuff
	public void setReconcileNotifier(ReconcileNotifier notifier) {
		this.notifier = notifier;
	}
}
