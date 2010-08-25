/**
 * Created on Aug 18, 2010, 1:11:20 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import org.tridas.schema.TridasElement;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("unchecked")
public class ElementModel extends HashModel implements IBulkImportSectionModel{
	private static final long serialVersionUID = 1L;
	
	public ElementModel(){
		registerProperty(ROWS, PropertyType.FINAL, new MVCArrayList<SingleElementModel>());
		registerProperty(COLUMN_MODEL, PropertyType.FINAL, new ColumnChooserModel());
		registerProperty(TABLE_MODEL, PropertyType.FINAL, new ElementTableModel(this));
		registerProperty(IMPORTED_LIST, PropertyType.FINAL, new MVCArrayList<TridasElement>());
	}
	
	public MVCArrayList<SingleElementModel> getRows(){
		return (MVCArrayList<SingleElementModel>) getProperty(ROWS);
	}
	
	public ColumnChooserModel getColumnModel(){
		return (ColumnChooserModel) getProperty(COLUMN_MODEL);
	}
	
	public ElementTableModel getTableModel(){
		return (ElementTableModel) getProperty(TABLE_MODEL);
	}
	
	public MVCArrayList<TridasElement> getImportedList(){
		return (MVCArrayList<TridasElement>) getProperty(IMPORTED_LIST);
	}
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#removeSelected()
	 */
	@Override
	public void removeSelected() {
		getTableModel().removeSelected();
	}
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#createRowInstance()
	 */
	@Override
	public ISingleRowModel createRowInstance() {
		return new SingleElementModel();
	}

	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#getModelTableProperties()
	 */
	@Override
	public String[] getModelTableProperties() {
		return SingleElementModel.TABLE_PROPERTIES;
	}
}