/**
 * Created on Jul 16, 2010, 6:44:22 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("unchecked")
public class ObjectModel extends HashModel implements IBulkImportSectionModel{
	private static final long serialVersionUID = 1L;
	
	public static final String TABLE_MODEL = "tableModel";
	
	public ObjectModel(){
		registerProperty(ROWS, PropertyType.FINAL, new MVCArrayList<SingleObjectModel>());
		registerProperty(COLUMN_MODEL, PropertyType.FINAL, new ColumnChooserModel(SingleObjectModel.PROPERTIES));
		registerProperty(TABLE_MODEL, PropertyType.FINAL, new ObjectTableModel(this));
	}
	
	public MVCArrayList<SingleObjectModel> getRows(){
		return (MVCArrayList<SingleObjectModel>) getProperty(ROWS);
	}
	
	public ColumnChooserModel getColumnModel(){
		return (ColumnChooserModel) getProperty(COLUMN_MODEL);
	}
	
	public ObjectTableModel getTableModel(){
		return (ObjectTableModel) getProperty(TABLE_MODEL);
	}
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#createRowInstance()
	 */
	@Override
	public ISingleRowModel createRowInstance() {
		return new SingleObjectModel();
	}
}
