/**
 * Created on Aug 18, 2010, 1:12:40 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasUnit;

import com.dmurph.mvc.model.MVCArrayList;
import com.dmurph.mvc.model.HashModel.PropertyType;

import edu.cornell.dendro.corina.schema.WSIElementTypeDictionary;
import edu.cornell.dendro.corina.schema.WSITaxonDictionary;

/**
 * @author Daniel Murphy
 *
 */
public class ElementTableModel extends AbstractTableModel implements PropertyChangeListener, IBulkImportTableModel {
	private static final long serialVersionUID = 1L;
	
	private ElementModel model;
	private MVCArrayList<SingleElementModel> models;
	
	private  MVCArrayList<String> columns;
	private final HashMap<SingleElementModel, Boolean> selected = new HashMap<SingleElementModel, Boolean>();
	
	public ElementTableModel(ElementModel argModel){
		setModel(argModel);
	}
	
	public ArrayList<String> getColumns(){
		return columns;
	}
	
	@SuppressWarnings("unchecked")
	public void setModel(ElementModel model) {
		if(this.model != null){
			models.removePropertyChangeListener(this);
			columns.removePropertyChangeListener(this);
		}
		this.model = model;
		this.models = (MVCArrayList<SingleElementModel>) this.model.getProperty(ElementModel.ROWS);
		this.columns = (MVCArrayList<String>) this.model.getProperty(ElementModel.COLUMN_MODEL);
		models.addPropertyChangeListener(this);
		columns.addPropertyChangeListener(this);
		recreateSelected();
	}
	
	public void getSelected(ArrayList<SingleElementModel> argModels){
		for(SingleElementModel key : selected.keySet()){
			if(selected.get(key)){
				argModels.add(key);
			}
		}
	}
	
	public void setSelected(SingleElementModel argSOM, boolean argSelected){
		if(!models.contains(argSOM)){
			throw new IllegalArgumentException("The provided model is not in this list.");
		}
		selected.put(argSOM, argSelected);
	}
	
	public void removeSelected(ArrayList<SingleElementModel> argRemoved) {
		Iterator<SingleElementModel> it = selected.keySet().iterator();
		while(it.hasNext()){
			SingleElementModel som = it.next();
			if(! selected.get(som)){
				continue; // if it's not selected
			}
			if(som.getImported() != null){
				int response = JOptionPane.showConfirmDialog(BulkImportModel.getInstance().getMainView(),
						"The element you are removing has been imported.  If any samples reference this object" +
						" as a parent, then they will no longer be able to be imported.  Still remove?", "Warning",
						JOptionPane.OK_CANCEL_OPTION);
				if( response != JOptionPane.OK_OPTION){
					continue;
				}
			}
			// careful, as changing the models list causes recreateSelected to be called, so we want to make sure
			// that we remove from the selected list first.
			it.remove();
			models.remove(som);
			argRemoved.add(som);
		}
	}
	
	public void selectAll(){
		selected.clear();
		for(SingleElementModel som : models){
			selected.put(som, true);
		}
		fireTableDataChanged();
	}
	
	public void selectNone(){
		selected.clear();
		fireTableDataChanged();
	}
	
	private void recreateSelected() {
		for(SingleElementModel som : models){
			if(!selected.containsKey(som)){
				selected.put(som, true);
			}
		}
	}
	
	public ElementModel getModel() {
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
			Class<?> c = getClassFromColumn(column);
			if(c != null){
				return c;
			}
			
			SingleElementModel som = models.get(0);
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
	
	private Class<?> getClassFromColumn(String argColumn){
		if(argColumn.equals(SingleElementModel.TYPE)){
			return WSIElementTypeDictionary.class;
		}else if(argColumn.equals(SingleElementModel.IMPORTED)){
			return Boolean.class;
		}else if(argColumn.equals(SingleElementModel.OBJECT)){
			return TridasObject.class;
		}else if(argColumn.equals(SingleElementModel.DEPTH)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleElementModel.WIDTH)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleElementModel.DIAMETER)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleElementModel.HEIGHT)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleElementModel.LATITUDE)){
			return Double.class;
		}else if(argColumn.equals(SingleElementModel.LONGTITUDE)){
			return Double.class;
		}else if(argColumn.equals(SingleElementModel.SLOPE_ANGLE)){
			return Integer.class;
		}else if(argColumn.equals(SingleElementModel.SLOPE_AZIMUTH)){
			return Integer.class;
		}else if(argColumn.equals(SingleElementModel.SOIL_DEPTH)){
			return Double.class;
		}else if(argColumn.equals(SingleElementModel.SHAPE)){
			return TridasShape.class;
		}else if(argColumn.equals(SingleElementModel.TAXON)){
			return WSITaxonDictionary.class;
		}else if(argColumn.equals(SingleElementModel.UNIT)){
			return TridasUnit.class;
		}
		return null;
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
			SingleElementModel som = models.get(rowIndex);
			return selected.get(som);
		}
		columnIndex--;
		String column = columns.get(columnIndex);
		SingleElementModel som = models.get(rowIndex);
		
		// make imported t/f
		Object o = som.getProperty(column);
		if(column.equals(SingleElementModel.IMPORTED)){
			return o != null;
		}
		
		return o;
		
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object argAValue, int argRowIndex, int argColumnIndex) {
		if(argColumnIndex == 0){
			SingleElementModel som = models.get(argRowIndex);
			selected.put(som, (Boolean) argAValue);
			return;
		}
		argColumnIndex--;
		String column = columns.get(argColumnIndex);
		if(argAValue != null && argAValue.toString().equals("")){
			argAValue = null;
		}
		SingleElementModel som = models.get(argRowIndex);
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
		SingleElementModel som = models.get(rowIndex);
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