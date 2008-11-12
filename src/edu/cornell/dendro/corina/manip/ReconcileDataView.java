package edu.cornell.dendro.corina.manip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.lowagie.text.Font;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.editor.DecadalModel;
import edu.cornell.dendro.corina.editor.SampleDataView;
import edu.cornell.dendro.corina.sample.CorinaWebElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.webdbi.ResourceIdentifier;

public class ReconcileDataView extends SampleDataView implements SampleListener {
	private JEditorPane reconcileInfo;
	private final static String reconcileIntro = "<html><br>";

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
		warnings.append("<b><u>General details</u></b><br><br>");
		if(reconciler.getFailureCount() != 0) {
			warnings.append("<font color=red><b>" + reconciler.getFailureCount() +
					" errors remaining</b></font><br><p>");
		}
		else
		{
			warnings.append("<b>No reconcilitation errors</b><br><p>");
		}
		
		if(newSample.getStart().compareTo(reference.getStart()) != 0) {
			warnings.append("<font color=red><b>Warning!<b><br>Samples do not begin in the same year - ");
			warnings.append(newSample.getStart() + " compared to " + reference.getStart());
			warnings.append(".</font><br><p>");
		}
		if(newSample.getData().size() != reference.getData().size()) {
			warnings.append("<font color=red><b>Warning!</b><br>Samples are different lengths - ");
			warnings.append(newSample.getData().size() + " years compared with " + reference.getData().size());
			warnings.append(".</font><br><p>");
		}

		
		//if(warnings.length() != 0) {
		//	warnings.insert(0, "<b><u>Errors:</u></b><br>");
		//	warnings.append("<p>");
		//}
		
		// now, information for the currently selected point
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();
		
		if (row >= 0 && col >= 0) {
			Year y = ((DecadalModel) myModel).getYear(row, col);

			
			int idx = y.diff(newSample.getStart());
			
			if(idx >= 0) {
				Set<Reconciler.FailureType> failures = reconciler.getFailuresForYear(y);
			
				selectionInfo.append("<br><b><u>Details for year " + y.toString() + "</u></b><br><br>");
			
				// just some info
				selectionInfo.append("Current value : " + newSample.getData().get(idx) + "<br>");
				selectionInfo.append("Reference value : " + 
						((reference.getData().size() > idx) ? reference.getData().get(idx) : "<n/a>") + 
						"<br><br>");
			
				// now, each of the failures
				if(failures != null) {
					if(failures.contains(Reconciler.FailureType.THREEPERCENT)) {
						// calculate min and max
						float val = ((Number) reference.getData().get(idx)).floatValue();
						float threepercent = val * 0.03f;
						String minmax = (int) Math.floor(val - threepercent) + " and " +
							(int) Math.ceil(val + threepercent);
						
						selectionInfo.append("<font color=red><b>Outside Error Margin</b><br>This value is outside "
								+ "the acceptable 3% error margin. The current value should be between "
								+ minmax + ".</font><br><br>");
					}
					if(failures.contains(Reconciler.FailureType.TRENDNEXT)) 
						selectionInfo.append("<font color=red><b>Trend Error</b><br>The trend to the next year "
								+ "is different than the reference measurement series.</font><br><br>");
					if(failures.contains(Reconciler.FailureType.TRENDPREV)) 
						selectionInfo.append("<font color=red><b>Trend Error</b><br>The trend to the previous year "
								+ "is different than the reference measurement series.</font><br><br>");
				}
			}
		}

		reconcileInfo.setText(reconcileIntro + warnings.toString()
				+ selectionInfo.toString());		
	}
	
	public void forceReconciliation() {
		reconciler.rereconcile();
		doReconciliation();
	}
	
	public int reconciliationErrorCount() {
		return reconciler.getFailureCount();
	}
	
	public void duplicateSelectionFrom(ReconcileDataView dataview) {
		// update our selection
		int row = dataview.myTable.getSelectedRow();
		int col = dataview.myTable.getSelectedColumn();
		
		tableSelectionMonitor.setSelection(row, col);
	}
	
	/**
	 * From http://exampledepot.com/egs/javax.swing.table/Vis.html
	 * @param table
	 * @param rowIndex
	 * @param vColIndex
	 */
	public void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport)table.getParent();
    
        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
    
        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();
    
        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x-pt.x, rect.y-pt.y);
    
        // Scroll the area into view
        viewport.scrollRectToVisible(rect);
	}
	
    private class TableSelectionChangeListener implements ListSelectionListener {
        private JTable table;
        private ReconcileDataView dataview;
        private int lastRow;
        private int lastCol;
        
        public TableSelectionChangeListener(JTable table, ReconcileDataView dataview) {
            this.table = table;
            this.dataview = dataview;
            
            lastRow = table.getSelectedRow();
            lastCol = table.getSelectedColumn();
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

            if(row < 0 || row >= table.getRowCount())
            	return;
            
            if(col < 0 || col > table.getColumnCount())
            	return;
            
            lastRow = row;
        	table.setRowSelectionInterval(row, row);
        	
        	lastCol = col;
        	table.setColumnSelectionInterval(col, col);
        	
        	doReconciliation();
        	scrollToVisible(table, row, col);
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
	
			// Set default selection border
			if(isSelected) {
				setBorder(selBorder);				
			}
			
			// Right alignment
			super.setHorizontalAlignment(JLabel.RIGHT);

			
			// Determine how we paint the cells depending on type of errors	
			// - 3% error only = Red background
			// - Trend error only = Red border
			// - Both errors = Red background and border
			// - No errors = Green
			
			Set<Reconciler.FailureType> failures = reconciler.getFailuresForYear(((DecadalModel) myModel).getYear(row, column));		
			if(failures != null && !failures.isEmpty()) {
				// There are failures
				
				Integer  borderWidth = 1;
				Color myBorderColor = Color.RED;
				// Enhance border width if cell is also selected
				if (isSelected)	borderWidth = 2;
				
				if(failures.contains(Reconciler.FailureType.THREEPERCENT)) {
					// 3% error so paint cell red 
					cell.setForeground(Color.WHITE);
					cell.setBackground(Color.RED);
					
					// Overide border color if selected otherwise it is invisible
					if (isSelected) myBorderColor = Color.BLACK;
					
				} else {
					// Just trend errors so paint cell white 
					cell.setForeground(Color.BLACK);
					cell.setBackground(Color.WHITE);					
				}
				
				
				if(failures.contains(Reconciler.FailureType.TRENDPREV)) {
					// Just trend to previous so no border on left of cell
					setBorder(BorderFactory.createMatteBorder(borderWidth, 0, borderWidth, borderWidth, myBorderColor));
				}
				if(failures.contains(Reconciler.FailureType.TRENDNEXT)) {
					// Just trend to next so no border on right of cell
					setBorder(BorderFactory.createMatteBorder(borderWidth, borderWidth, borderWidth, 0, myBorderColor));
				}
				if(( failures.contains(Reconciler.FailureType.TRENDPREV)) && (failures.contains(Reconciler.FailureType.TRENDNEXT)) ) {
					// Trends to next and previous so no border on left OR right
					setBorder(BorderFactory.createMatteBorder(borderWidth, 0, borderWidth, 0, myBorderColor));
				}	
				
			} else {
				// No errors so paint green
				cell.setForeground(Color.BLACK);
				cell.setBackground(Color.GREEN);
			}
			

			
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
