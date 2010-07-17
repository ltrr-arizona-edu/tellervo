/**
 * Created on Jul 17, 2010, 1:35:12 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.dmurph.mvc.model.HashModel.PropertyType;
import com.dmurph.mvc.util.MVCArrayList;

/**
 * @author Daniel Murphy
 *
 */
public class ObjectTableModel extends AbstractTableModel implements PropertyChangeListener {
	
	private ObjectModel model;
	
	public ObjectTableModel(ObjectModel argModel){
		setModel(argModel);
	}
	
	public void setModel(ObjectModel model) {
		if(this.model != null){
			MVCArrayList<SingleObjectModel> models = (MVCArrayList<SingleObjectModel>) this.model.getProperty(ObjectModel.OBJECTS);
			models.removePropertyChangeListener(this);
		}
		this.model = model;
		MVCArrayList<SingleObjectModel> models = (MVCArrayList<SingleObjectModel>) this.model.getProperty(ObjectModel.OBJECTS);
		models.addPropertyChangeListener(this);
	}
	public ObjectModel getModel() {
		return model;
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return SingleObjectModel.PROPERTIES[column];
	}
	
	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return SingleObjectModel.PROPERTIES.length;
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		MVCArrayList<SingleObjectModel> models = (MVCArrayList<SingleObjectModel>) model.getProperty(ObjectModel.OBJECTS);
		SingleObjectModel som = models.get(0);
		return som.getProperty(SingleObjectModel.PROPERTIES[columnIndex]).getClass();
	}
	
	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		MVCArrayList<SingleObjectModel> models = (MVCArrayList<SingleObjectModel>) model.getProperty(ObjectModel.OBJECTS);
		return models.size();
	}
	
	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		MVCArrayList<SingleObjectModel> models = (MVCArrayList<SingleObjectModel>) model.getProperty(ObjectModel.OBJECTS);
		SingleObjectModel som = models.get(rowIndex);
		return som.getProperty(SingleObjectModel.PROPERTIES[columnIndex]);
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		MVCArrayList<SingleObjectModel> models = (MVCArrayList<SingleObjectModel>) model.getProperty(ObjectModel.OBJECTS);
		SingleObjectModel som = models.get(rowIndex);
		if(som.getPropertyType(SingleObjectModel.PROPERTIES[columnIndex]) == PropertyType.READ_ONLY){
			return false;
		}
		return true;
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		
		if(prop.equals(MVCArrayList.SIZE)){
			fireTableStructureChanged();
		}
		else if(prop.equals(MVCArrayList.ELEMENT)){
			fireTableDataChanged();
		}
	}
}
