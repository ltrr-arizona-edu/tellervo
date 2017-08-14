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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.tellervo.desktop.bulkdataentry.control.ColumnChooserController;
import org.tellervo.desktop.bulkdataentry.control.ColumnsModifiedEvent;
import org.tellervo.desktop.bulkdataentry.control.HideColumnChooserEvent;
import org.tellervo.desktop.bulkdataentry.model.ColumnListModel;

import com.dmurph.mvc.model.MVCArrayList;


/**
 * Frameless window storing a simple two column table.  First column is a checkbox indicating whether
 * the table column name list in column 2 is enabled or not in the BDE screen. 
 * 
 * @author daniel
 *
 */
public class ColumnChooserView extends JWindow{
	private static final long serialVersionUID = 1L;
	
	private JTable checkboxList;
	private JButton okButton;
	private final TableModel tableModel;
	
	private final ColumnListModel columnChooserModel;
	private final MVCArrayList<String> columns;
	
	public ColumnChooserView(ColumnListModel argModel, JFrame argParent, Component argLocationComponent){
		super(argParent);

		columnChooserModel = argModel;
		tableModel = new TableModel();
		this.setMinimumSize(new Dimension(200,200));
		
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
		//setLocationRelativeTo(argLocationComponent);
		
		setBounds(argLocationComponent.getLocationOnScreen().x, 
				argLocationComponent.getLocationOnScreen().y+argLocationComponent.getHeight(), 
				200, 
				200);
	}
	
	public void initComponents() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setLayout(new BorderLayout());
		
		checkboxList = new JTable();
		checkboxList.setOpaque(false);
		checkboxList.setTableHeader(null);
		checkboxList.setBackground(UIManager.getColor("ToolBar.background"));
		checkboxList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		ToolbarColorRenderer renderer = new ToolbarColorRenderer();
		checkboxList.setDefaultRenderer(String.class, renderer);
		
		okButton = new JButton();
		setLayout(new BorderLayout());

		panel.add(checkboxList, "Center");
		
		add(panel, "Center");
		
		/*Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(okButton);
		add(box, "South");*/
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
		checkboxList.getColumnModel().getColumn(0).setPreferredWidth(20);
		checkboxList.getColumnModel().getColumn(0).setMaxWidth(20);
		columnChooserModel.addPropertyChangeListener(pclistener);
	}
	
	private void unlinkModel(){
		columnChooserModel.removePropertyChangeListener(pclistener);
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
				return columnChooserModel.contains(columns.get(argRowIndex));
			}
			return columns.get(argRowIndex);
		}
		
		/**
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
		@Override
		public void setValueAt(Object argAValue, int argRowIndex, int argColumnIndex) {
			Boolean val = (Boolean) argAValue;
			if(val == Boolean.FALSE){
				System.out.println("false");
				ColumnsModifiedEvent event = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_REMOVED,
						columns.get(argRowIndex),
						  columnChooserModel);
				event.dispatch();

			}else{
				System.out.println("true");
				ColumnsModifiedEvent event = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_ADDED,
						columns.get(argRowIndex),
									columnChooserModel);
				event.dispatch();
			}
		}
	}
	
	class ToolbarColorRenderer extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		   public Component getTableCellRendererComponent(JTable table, Object value,
		            boolean isSelected, boolean hasFocus, int row, int col) {

		      Component c = super.getTableCellRendererComponent(table, value,
		               isSelected, hasFocus, row, col);
		      
		      c.setForeground(Color.black);
		      
		    
		      c.setBackground(UIManager.getColor("ToolBar.dockingBackground"));
		      return c;
		   }
		
	}
}



