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
public class ObjectModel extends HashModel implements IBulkImportSectionModel{
	private static final long serialVersionUID = 1L;
	
	public static final String TABLE_MODEL = "tableModel";
	
	public ObjectModel(){
		registerProperty(ROWS, PropertyType.FINAL, new MVCArrayList<SingleObjectModel>());
		registerProperty(COLUMN_MODEL, PropertyType.FINAL, new ColumnChooserModel(SingleObjectModel.PROPERTIES));
		registerProperty(TABLE_MODEL, PropertyType.FINAL, new ObjectTableModel(this));
	}
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#createRowInstance()
	 */
	@Override
	public ISingleRowModel createRowInstance() {
		return new SingleObjectModel();
	}
}
