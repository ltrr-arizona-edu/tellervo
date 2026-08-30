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
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.SwingUtilities;
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
	private final IdentityHashMap<IBulkImportSingleRowModel, Boolean> selected = new IdentityHashMap<IBulkImportSingleRowModel, Boolean>();
	private final Set<IBulkImportSingleRowModel> listenedRows = Collections.newSetFromMap(
			new IdentityHashMap<IBulkImportSingleRowModel, Boolean>());
	private final PropertyChangeListener rowPropertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			dispatchPropertyChange(evt, true);
		}
	};
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
			detachRowListeners();
			models.removePropertyChangeListener(this);
			columns.removePropertyChangeListener(this);
		}
		this.model = model;
		this.models = (MVCArrayList<IBulkImportSingleRowModel>) this.model.getRows();
		this.columns = (MVCArrayList<String>) this.model.getColumnModel();
		models.addPropertyChangeListener(this);
		columns.addPropertyChangeListener(this);
		syncRowListeners();
		recreateSelected();
	}
	
	public MVCArrayList<IBulkImportSingleRowModel> getAllSingleRowModels()
	{
		return models;
	}
	
	public void getSelected(ArrayList<IBulkImportSingleRowModel> argModels){
		for(IBulkImportSingleRowModel row : models){
			if(Boolean.TRUE.equals(selected.get(row))){
				argModels.add(row);
			}
		}
	}
	
	public void setSelected(IBulkImportSingleRowModel argSOM, boolean argSelected){
		int row = indexOfRow(argSOM);
		if(row < 0){
			throw new IllegalArgumentException("The provided model is not in this list.");
		}
		selected.put(argSOM, argSelected);
		fireTableCellUpdatedOnEdt(argSOM, 0);
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
				int row = indexOfRow(som);
				if(row >= 0){
					models.remove(row);
				}
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
		
		Set<IBulkImportSingleRowModel> currentRows = Collections.newSetFromMap(
				new IdentityHashMap<IBulkImportSingleRowModel, Boolean>());
		currentRows.addAll(models);

		// remove any rows that aren't in the model anymore
		Iterator<IBulkImportSingleRowModel> it = selected.keySet().iterator();
		while(it.hasNext()){
			if(!currentRows.contains(it.next())){
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
			//return som.getProperty(column) != null;
			Object val = som.getProperty(column);
			if(val instanceof ImportStatus)
			{
				return val;
			}
			else if (val==null)
			{
				return ImportStatus.LOCAL;
			}
			else 
			{
				if(som instanceof SingleObjectModel)
				{
					if(((SingleObjectModel) som).isDirty())
					{
						return ImportStatus.IMPORTED_WITH_LOCAL_EDITS;
					}
				}
				if(som instanceof SingleElementModel)
				{
					if(((SingleElementModel) som).isDirty())
					{
						return ImportStatus.IMPORTED_WITH_LOCAL_EDITS;
					}
				}
				if(som instanceof SingleSampleModel)
				{
					if(((SingleSampleModel) som).isDirty())
					{
						return ImportStatus.IMPORTED_WITH_LOCAL_EDITS;
					}
				}
				
				return ImportStatus.IMPORTED;

			}

			
			
		}
		return som.getProperty(column);
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 * 
	 * 
	 * NB Make sure the Column Index is the index of *ALL* columns, not just the columns in view
	 * 
	 */
	@Override
	public void setValueAt(Object argAValue, int argRowIndex, int argColumnIndex){
 		
		
		
		IBulkImportSingleRowModel som = models.get(argRowIndex);
		if(argColumnIndex == 0){
			selected.put(som, (Boolean) argAValue);
			fireTableCellUpdatedOnEdt(som, 0);
			return;
		}
		argColumnIndex--;
		
		// TODO: this all should go to a command, as it's modifying the model.
		
		
		
		
		String column = columns.get(argColumnIndex);
		if(argAValue != null && argAValue.toString().equals("")){
			argAValue = null;
		}
		
		setValueAt(argAValue, column, som, argRowIndex);

		
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
		
		log.debug("-----");
		log.debug("Column name : "+column);
		log.debug("Property : " +som.getProperty(column));
		log.debug("PropetyType : " + som.getPropertyType(column));
		log.debug("-----");
		
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
		dispatchPropertyChange(evt, false);
	}

	private void dispatchPropertyChange(final PropertyChangeEvent evt, final boolean rowEvent) {
		if(!SwingUtilities.isEventDispatchThread()){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					handlePropertyChange(evt, rowEvent);
				}
			});
			return;
		}
		handlePropertyChange(evt, rowEvent);
	}

	private void handlePropertyChange(PropertyChangeEvent evt, boolean rowEvent) {
		if(rowEvent){
			handleRowPropertyChange(evt);
			return;
		}

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
				
				syncRowListeners();
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
				
				syncRowListeners();
				recreateSelected();
			}
			else if(prop.equals(MVCArrayList.CHANGED)){
				IndexedPropertyChangeEvent event = (IndexedPropertyChangeEvent) evt;
				syncRowListeners();
				fireTableRowsUpdated(event.getIndex(), event.getIndex());
			}
			else if(prop.equals(MVCArrayList.ADDED_ALL)){
				MVCPropertiesAddedEvent event = (MVCPropertiesAddedEvent) evt;
				syncRowListeners();
				fireTableRowsInserted(event.getStartIndex(), event.getEndIndex());
				recreateSelected();
			}
			else if(prop.equals(MVCArrayList.ADDED)){
				IndexedPropertyChangeEvent event = (IndexedPropertyChangeEvent) evt;
				syncRowListeners();
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

	private void handleRowPropertyChange(PropertyChangeEvent evt) {
		if(!(evt.getSource() instanceof IBulkImportSingleRowModel)){
			return;
		}

		IBulkImportSingleRowModel rowModel = (IBulkImportSingleRowModel) evt.getSource();
		int row = indexOfRow(rowModel);
		if(row < 0){
			return;
		}

		String property = evt.getPropertyName();
		int modelColumn = columns.indexOf(property);
		if(modelColumn >= 0){
			fireTableCellUpdated(row, modelColumn + 1);
		}

		// Imported status is derived from both the identifier and dirty state.
		if(com.dmurph.mvc.IModel.DIRTY.equals(property)){
			int importedColumn = columns.indexOf(IBulkImportSingleRowModel.IMPORTED);
			if(importedColumn >= 0){
				fireTableCellUpdated(row, importedColumn + 1);
			}
		}
	}

	private void syncRowListeners() {
		Set<IBulkImportSingleRowModel> currentRows = Collections.newSetFromMap(
				new IdentityHashMap<IBulkImportSingleRowModel, Boolean>());
		currentRows.addAll(models);

		Iterator<IBulkImportSingleRowModel> listened = listenedRows.iterator();
		while(listened.hasNext()){
			IBulkImportSingleRowModel row = listened.next();
			if(!currentRows.contains(row)){
				row.removePropertyChangeListener(rowPropertyChangeListener);
				listened.remove();
			}
		}

		for(IBulkImportSingleRowModel row : currentRows){
			if(listenedRows.add(row)){
				row.addPropertyChangeListener(rowPropertyChangeListener);
			}
		}
	}

	private void detachRowListeners() {
		for(IBulkImportSingleRowModel row : listenedRows){
			row.removePropertyChangeListener(rowPropertyChangeListener);
		}
		listenedRows.clear();
	}

	private int indexOfRow(IBulkImportSingleRowModel target) {
		for(int i = 0; i < models.size(); i++){
			if(models.get(i) == target){
				return i;
			}
		}
		return -1;
	}

	private void fireTableCellUpdatedOnEdt(final IBulkImportSingleRowModel rowModel,
			final int column) {
		if(SwingUtilities.isEventDispatchThread()){
			int row = indexOfRow(rowModel);
			if(row >= 0){
				fireTableCellUpdated(row, column);
			}
		}else{
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					int row = indexOfRow(rowModel);
					if(row >= 0){
						fireTableCellUpdated(row, column);
					}
				}
			});
		}
	}
}
