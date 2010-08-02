/**
 * Created at Jul 24, 2010, 4:01:10 AM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.control.bulkImport.ColumnChooserController;
import edu.cornell.dendro.corina.control.bulkImport.ColumnsModifiedEvent;
import edu.cornell.dendro.corina.control.bulkImport.HideColumnChooserEvent;
import edu.cornell.dendro.corina.model.bulkImport.ColumnChooserModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectModel;

/**
 * @author daniel
 *
 */
public class ColumnChooserView extends JDialog{

	private JTable checkboxList;
	private JButton okButton;
	private final TableModel tableModel;
	
	private final ColumnChooserModel model;
	private final String[] columns;
	
	public ColumnChooserView(ColumnChooserModel argModel, JFrame argParent){
		super(argParent, true);
		model = argModel;
		columns = argModel.possibleColumns;
		tableModel = new TableModel();
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
	}
	
	public void initComponents() {
		checkboxList = new JTable();
		okButton = new JButton();
		setLayout(new BorderLayout());
		
		add(checkboxList.getTableHeader(), "North");
		add(checkboxList, "Center");
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(okButton);
		add(box, "South");
	}
	
	PropertyChangeListener pclistener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent argEvt) {
			String prop = argEvt.getPropertyName();
			if(prop.equals(MVCArrayList.SIZE)){
				tableModel.fireTableDataChanged();
				checkboxList.repaint();
			}
		}
	};
	
	private void linkModel() {
		checkboxList.setModel(tableModel);
		
		model.addPropertyChangeListener(pclistener);
	}
	
	private void unlinkModel(){
		model.removePropertyChangeListener(pclistener);
	}
	
	private void addListeners() {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				HideColumnChooserEvent event = new HideColumnChooserEvent();
				event.dispatch();
			}
		});
	}
	
	private void populateLocale() {
		okButton.setText("Ok");
	}
	
	class TableModel extends AbstractTableModel{
		
		
		/**
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int column) {
			if(column == 0){
				return "Visible";
			}else{
				return "Column Name";
			}
		}
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
			return columns[argRowIndex];
		}
		
		/**
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
		@Override
		public void setValueAt(Object argAValue, int argRowIndex, int argColumnIndex) {
			Boolean val = (Boolean) argAValue;
			if(val == Boolean.TRUE){
				System.out.println("true");
				ColumnsModifiedEvent event = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_ADDED,
																	  columns[argRowIndex],
																	  model);
				event.dispatch();
			}else{
				System.out.println("false");
				ColumnsModifiedEvent event = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_REMOVED,
						  columns[argRowIndex],
						  model);
				event.dispatch();
			}
		}
	}
}

