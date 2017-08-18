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
package org.tellervo.desktop.bulkdataentry.model;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.command.ImportSelectedObjectsCommand;

import com.dmurph.mvc.model.HashModel.PropertyType;
import com.dmurph.mvc.model.MVCArrayList;
import com.dmurph.mvc.support.MVCPropertiesAddedEvent;
import com.dmurph.mvc.support.MVCPropertiesRemovedEvent;
import com.dmurph.mvc.support.MVCPropertyRemovedEvent;

/**
 * A specialist type of TableModel used by the BulkDataEntry 
 * 
 * @author Daniel Murphy
 */
public abstract class AbstractBulkImportTableModel extends AbstractTableModel implements PropertyChangeListener, IBulkImportTableModel {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(AbstractBulkImportTableModel.class);

	private IBulkImportSectionModel model;
	private MVCArrayList<IBulkImportSingleRowModel> models;
	
	private MVCArrayList<String> columns;
	private final HashMap<IBulkImportSingleRowModel, Boolean> selected = new HashMap<IBulkImportSingleRowModel, Boolean>();
	private boolean recreateSelectedLock = false;
	
	public AbstractBulkImportTableModel(IBulkImportSectionModel argModel){
		setModel(argModel);
	}
	
	/**
	 * Get a list of all the columns regardless of whether they are enabled or not
	 * 
	 * @return
	 */
	public ArrayList<String> getColumns(){
		return columns;
	}
	
	public void addRow()
	{
		IBulkImportSingleRowModel newrow = model.createRowInstance();
		model.getRows().add(newrow);
		
	}
	
	@SuppressWarnings("unchecked")
	public void setModel(IBulkImportSectionModel model) {
		if(this.model != null){
			models.removePropertyChangeListener(this);
			columns.removePropertyChangeListener(this);
		}
		this.model = model;
		this.models = (MVCArrayList<IBulkImportSingleRowModel>) this.model.getRows();
		this.columns = (MVCArrayList<String>) this.model.getColumnModel();
		models.addPropertyChangeListener(this);
		columns.addPropertyChangeListener(this);
		recreateSelected();
	}
	
	public MVCArrayList<IBulkImportSingleRowModel> getAllSingleRowModels()
	{
		return models;
	}
	
	public void getSelected(ArrayList<IBulkImportSingleRowModel> argModels){
		for(IBulkImportSingleRowModel key : selected.keySet()){
			if(selected.get(key)){
				argModels.add(key);
			}
		}
	}
	
	public void setSelected(IBulkImportSingleRowModel argSOM, boolean argSelected){
		if(!models.contains(argSOM)){
			throw new IllegalArgumentException("The provided model is not in this list.");
		}
		selected.put(argSOM, argSelected);
	}
	
	/**
	 * Removes the selected objects
	 * @param argRemovedObjects where to put the objects that were removed
	 */
	public void removeSelected( ArrayList<IBulkImportSingleRowModel> argRemovedObjects) {
		recreateSelectedLock = true;
		
		boolean alreadyWarned =false;
		try{
			Iterator<IBulkImportSingleRowModel> it = selected.keySet().iterator();
			while(it.hasNext()){
				IBulkImportSingleRowModel som = it.next();
				if(! selected.get(som)){
					continue; // if it's not selected
				}
				
				// Surely this isn't necessary? Commenting out for now.
				/*if(som.getImported() != null && alreadyWarned==false){
					int response = JOptionPane.showConfirmDialog(BulkImportModel.getInstance().getMainView(),
							"The object you are removing has been imported.  If any elements reference this object" +
							" as a parent, then they will no longer be able to be imported.  Still remove?", "Warning",
							JOptionPane.OK_CANCEL_OPTION);
					if( response != JOptionPane.OK_OPTION){
						return;
					}
					alreadyWarned=true;
				}*/
				
				// careful, as changing the models list causes recreateSelected to be called, so we want to make sure
				// that we remove from the selected list first.
				it.remove();
				models.remove(som);
				argRemovedObjects.add(som);
			}
		}finally{
			recreateSelectedLock = false;
			recreateSelected();
		}
	}
	
	
	public void selectAll(){
		selected.clear();
		for(IBulkImportSingleRowModel som : models){
			selected.put(som, true);
		}
		fireTableDataChanged();
	}
	
	public void selectNone(){
		selected.clear();
		fireTableDataChanged();
	}
	
	
	private void recreateSelected() {
		if(recreateSelectedLock){
			return;
		}
		// add any missing rows from model
		for(IBulkImportSingleRowModel som : models){
			if(!selected.containsKey(som)){
				selected.put(som, true);
			}
		}
		
		// remove any rows that aren't in the model anymore
		Iterator<IBulkImportSingleRowModel> it = selected.keySet().iterator();
		while(it.hasNext()){
			if(!models.contains(it.next())){
				it.remove();
			}
		}
	}
	
	/**
	 * Return the count of rows that are selected
	 * 
	 * @return
	 */
	public int getSelectedCount(){
		int count = 0;
		
		try{
			Iterator<IBulkImportSingleRowModel> it = selected.keySet().iterator();
			while(it.hasNext()){
				IBulkImportSingleRowModel som = it.next();
				if(selected.get(som)){
					count++;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return count;
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		if(column == 0){
			return "Selected";
		}
		return columns.get(column-1);
	}
	
	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return columns.size()+1;
	}
	

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == 0){
			return Boolean.class;
		}
		columnIndex--;
		if(models.size() == 0){
			return String.class;
		}else{
			String column = columns.get(columnIndex);
			
			// for combo box stuff
			Class<?> cls = getColumnClass(column);
			if(cls != null){
				return cls;
			}
			
			IBulkImportSingleRowModel som = models.get(0);
			if(som == null){
				return Object.class;
			}
			Object o = som.getProperty(column);
			if(o == null){
				return Object.class;
			}
			return o.getClass();
		}
	}
	
	/**
	 * 
	 * @param argColumn
	 * @return null if nothing specified
	 */
	public abstract Class<?> getColumnClass(String argColumn);
	
	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return models.size();
	}
	

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0){
			IBulkImportSingleRowModel som = models.get(rowIndex);
			return selected.get(som);
		}
		columnIndex--;
		String column = columns.get(columnIndex);
		IBulkImportSingleRowModel som = models.get(rowIndex);
		
		// make imported t/f
		if(column.equals(IBulkImportSingleRowModel.IMPORTED)){
			return som.getProperty(column) != null;
		}
		return som.getProperty(column);
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object argAValue, int argRowIndex, int argColumnIndex) {
 		IBulkImportSingleRowModel som = models.get(argRowIndex);
		if(argColumnIndex == 0){
			selected.put(som, (Boolean) argAValue);
			return;
		}
		argColumnIndex--;
		
		// TODO: this all should go to a command, as it's modifying the model.
		String column = columns.get(argColumnIndex);
		if(argAValue != null && argAValue.toString().equals("")){
			argAValue = null;
		}
		
		setValueAt(argAValue, column, som, argRowIndex);
		
		if(som instanceof SingleObjectModel)
		{
			if(((SingleObjectModel) som).isDirty())
			{
				log.debug("Dirty");
			}
			else
			{
				log.debug("Clean");
			}
		}
		
	}
	
	public abstract void setValueAt(Object argAValue, String argColumn, IBulkImportSingleRowModel argModel, int argRowIndex);


	/**
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex == 0){
			return true;
		}
		columnIndex--;
		
		String column = columns.get(columnIndex);
		IBulkImportSingleRowModel som = models.get(rowIndex);
		if(som.getPropertyType(column) == PropertyType.READ_WRITE){
			return true;
		}
		return false;
	}
	


	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		Object source = evt.getSource();
		if(source == models){
			if(prop.equals(MVCArrayList.REMOVED_ALL)){
				MVCPropertiesRemovedEvent event = (MVCPropertiesRemovedEvent) evt;
				if(event.isIndexed()){
					fireTableRowsDeleted(event.getStartIndex(), event.getEndIndex());
				}
				else{
					fireTableDataChanged();
				}
				
				recreateSelected();
			}
			else if(prop.equals(MVCArrayList.REMOVED)){
				MVCPropertyRemovedEvent event = (MVCPropertyRemovedEvent) evt;
				if(event.isIndexed()){
					fireTableRowsDeleted(event.getIndex(), event.getIndex());
				}
				else{
					fireTableDataChanged();
				}
				
				recreateSelected();
			}
			else if(prop.equals(MVCArrayList.CHANGED)){
				IndexedPropertyChangeEvent event = (IndexedPropertyChangeEvent) evt;
				fireTableCellUpdated(event.getIndex(), event.getIndex());
			}
			else if(prop.equals(MVCArrayList.ADDED_ALL)){
				MVCPropertiesAddedEvent event = (MVCPropertiesAddedEvent) evt;
				fireTableRowsInserted(event.getStartIndex(), event.getStartIndex());
				recreateSelected();
			}
			else if(prop.equals(MVCArrayList.ADDED)){
				IndexedPropertyChangeEvent event = (IndexedPropertyChangeEvent) evt;
				System.out.println(event.getIndex());
				fireTableRowsInserted(event.getIndex(), event.getIndex());
				recreateSelected();
			}
		}
		else if(source == columns){
			if(prop.equals(MVCArrayList.CHANGED)){
				fireTableStructureChanged();
			}
			else if(prop.equals(MVCArrayList.SIZE)){
				fireTableStructureChanged();
			}
		}
	}
}
