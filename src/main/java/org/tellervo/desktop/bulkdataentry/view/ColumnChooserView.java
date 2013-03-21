/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
/**
 * Created at Jul 24, 2010, 4:01:10 AM
 */
package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.bulkdataentry.control.ColumnChooserController;
import org.tellervo.desktop.bulkdataentry.control.ColumnsModifiedEvent;
import org.tellervo.desktop.bulkdataentry.control.HideColumnChooserEvent;
import org.tellervo.desktop.bulkdataentry.model.ColumnChooserModel;

import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author daniel
 *
 */
public class ColumnChooserView extends JWindow{
	private static final long serialVersionUID = 1L;
	
	private JTable checkboxList;
	private JButton okButton;
	private final TableModel tableModel;
	
	private final ColumnChooserModel model;
	private final MVCArrayList<String> columns;
	
	public ColumnChooserView(ColumnChooserModel argModel, JFrame argParent, Component argLocationComponent){
		super(argParent);

		model = argModel;
		tableModel = new TableModel();
		columns = argModel.getPossibleColumns();
		columns.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				String name = argEvt.getPropertyName();
				if(!name.equals(MVCArrayList.DIRTY)){
					tableModel.fireTableStructureChanged();
				}
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				unlinkModel();
			}
		});
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
		pack();
		setLocationRelativeTo(argLocationComponent);
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
	
	private class TableModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;

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
			return columns.size();
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
				return model.contains(columns.get(argRowIndex));
			}
			return columns.get(argRowIndex);
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
						columns.get(argRowIndex),
									model);
				event.dispatch();
			}else{
				System.out.println("false");
				ColumnsModifiedEvent event = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_REMOVED,
						columns.get(argRowIndex),
						  model);
				event.dispatch();
			}
		}
	}
}

