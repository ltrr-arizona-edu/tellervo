/**
 * Created on Jul 17, 2010, 1:35:12 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.dmurph.mvc.model.HashModel.PropertyType;
import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.schema.WSIObjectTypeDictionary;

/**
 * @author Daniel Murphy
 *
 */
public class ObjectTableModel extends AbstractTableModel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private ObjectModel model;
	private MVCArrayList<SingleObjectModel> models;
	
	private  MVCArrayList<String> columns;
	private final HashMap<SingleObjectModel, Boolean> selected = new HashMap<SingleObjectModel, Boolean>();
	
	public ObjectTableModel(ObjectModel argModel){
		setModel(argModel);
	}
	
	public ArrayList<String> getColumns(){
		return columns;
	}
	
	@SuppressWarnings("unchecked")
	public void setModel(ObjectModel model) {
		if(this.model != null){
			models.removePropertyChangeListener(this);
			columns.removePropertyChangeListener(this);
		}
		this.model = model;
		this.models = (MVCArrayList<SingleObjectModel>) this.model.getProperty(ObjectModel.ROWS);
		this.columns = (MVCArrayList<String>) this.model.getProperty(ObjectModel.COLUMN_MODEL);
		models.addPropertyChangeListener(this);
		columns.addPropertyChangeListener(this);
		recreateSelected();
	}
	
	public void getSelected(ArrayList<SingleObjectModel> argModels){
		for(SingleObjectModel key : selected.keySet()){
			if(selected.get(key)){
				argModels.add(key);
			}
		}
	}
	
	public void setSelected(SingleObjectModel argSOM, boolean argSelected){
		if(!models.contains(argSOM)){
			throw new IllegalArgumentException("The provided model is not in this list.");
		}
		selected.put(argSOM, argSelected);
	}
	
	public void removeSelected() {
		for(SingleObjectModel som : selected.keySet()){
			models.remove(som);
			selected.remove(som);
		}
	}
	
	public void selectAll(){
		selected.clear();
		for(SingleObjectModel som : models){
			selected.put(som, true);
		}
		fireTableDataChanged();
	}
	
	public void selectNone(){
		selected.clear();
		fireTableDataChanged();
	}
	
	private void recreateSelected() {
		HashMap<SingleObjectModel, Boolean> newMap = new HashMap<SingleObjectModel, Boolean>();
		for(SingleObjectModel som : models){
			if(selected.containsKey(som)){
				newMap.put(som, selected.get(som));
			}else{
				newMap.put(som, false);
			}
		}
		selected.clear();
		selected.putAll(newMap);
	}
	
	public ObjectModel getModel() {
		return model;
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
			if(column.equals(SingleObjectModel.TYPE)){
				return WSIObjectTypeDictionary.class;
			}else if(column.equals(SingleObjectModel.IMPORTED)){
				return Boolean.class;
			}else if(column.equals(SingleObjectModel.LATITUDE)){
				return Double.class;
			}else if(column.equals(SingleObjectModel.LONGTITUDE)){
				return Double.class;
			}
			
			SingleObjectModel som = models.get(0);
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
			SingleObjectModel som = models.get(rowIndex);
			return selected.get(som);
		}
		columnIndex--;
		String column = columns.get(columnIndex);
		SingleObjectModel som = models.get(rowIndex);
		
		// make imported t/f
		if(column.equals(SingleObjectModel.IMPORTED)){
			return som.getProperty(column) != null;
		}
		return som.getProperty(column);
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object argAValue, int argRowIndex, int argColumnIndex) {
		if(argColumnIndex == 0){
			SingleObjectModel som = models.get(argRowIndex);
			selected.put(som, (Boolean) argAValue);
			return;
		}
		argColumnIndex--;
		String column = columns.get(argColumnIndex);
		if(argAValue != null && argAValue.toString().equals("")){
			argAValue = null;
		}
		SingleObjectModel som = models.get(argRowIndex);
		som.setProperty(column, argAValue);
	}
	
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
		SingleObjectModel som = models.get(rowIndex);
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
			if(prop.equals(MVCArrayList.REMOVED)){
				fireTableStructureChanged();
				recreateSelected();
			}
			else if(prop.equals(MVCArrayList.CHANGED)){
				IndexedPropertyChangeEvent event = (IndexedPropertyChangeEvent) evt;
				fireTableCellUpdated(event.getIndex(), event.getIndex());
			}
			else if(prop.equals(MVCArrayList.ADDED)){
				IndexedPropertyChangeEvent event = (IndexedPropertyChangeEvent) evt;
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
