/**
 * Created at Jul 24, 2010, 4:01:10 AM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.model.bulkImport.ColumnChooserModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectModel;

/**
 * @author daniel
 *
 */
public class ColumnChooserView extends JDialog{

	private JTable checkboxList;
	private JButton okButton;
	
	private final ColumnChooserModel model;
	private final String[] columns;
	
	public ColumnChooserView(ColumnChooserModel argModel, JFrame argParent, String[] argColumns){
		super(argParent, true);
		model = argModel;
		columns = argColumns;
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
	}
	
	public void initComponents() {
		checkboxList = new JTable();
		okButton = new JButton();
	}
	
	public void linkModel() {
		
	}
	
	public void addListeners() {

	}
	
	public void populateLocale() {

	}
	
	class TableModel extends AbstractTableModel{
		
		/**
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		@Override
		public int getColumnCount() {
			return 2;
		}

		/**
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		@Override
		public int getRowCount() {
			return columns.length;
		}
		
		/**
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int argRowIndex, int argColumnIndex) {
			return argColumnIndex == 0;
		}
		
		/**
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int argColumnIndex) {
			if(argColumnIndex == 0){
				return Boolean.class;
			}else{
				return String.class;
			}
		}

		/**
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		@Override
		public Object getValueAt(int argRowIndex, int argColumnIndex) {
			if(argColumnIndex == 0){
				return model.contains(columns[argRowIndex]);
			}
			return model.get(argRowIndex);
		}
	}
}

