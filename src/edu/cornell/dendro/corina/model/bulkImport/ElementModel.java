/**
 * Created on Aug 18, 2010, 1:11:20 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author Daniel Murphy
 *
 */
public class ElementModel extends HashModel implements IBulkImportSectionModel{
	private static final long serialVersionUID = 1L;
	
	public ElementModel(){
		registerProperty(ROWS, PropertyType.FINAL, new MVCArrayList<SingleElementModel>());
		registerProperty(COLUMN_MODEL, PropertyType.FINAL, new ColumnChooserModel(SingleElementModel.TABLE_PROPERTIES));
		registerProperty(TABLE_MODEL, PropertyType.FINAL, new ElementTableModel(this));
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
}