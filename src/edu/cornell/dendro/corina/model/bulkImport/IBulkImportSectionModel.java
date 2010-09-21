/**
 * Created at Aug 1, 2010, 3:04:07 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import org.tridas.interfaces.ITridas;

import com.dmurph.mvc.IModel;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author daniel
 *
 */
public interface IBulkImportSectionModel extends IModel{
	public static final String COLUMN_MODEL = "columnModel";
	public static final String ROWS = "rows";
	public static final String TABLE_MODEL = "tableModel";
	public static final String IMPORTED_LIST = "importedList";
	public static final String WAYPOINT_LIST = "waypointList";
	
	
	/**
	 * goes to the table model, which handles the selected rows
	 */
	public void removeSelected();
	
	/**
	 * Gets an instance of a row object for this model;
	 * @return
	 */
	public IBulkImportSingleRowModel createRowInstance();
		
	/**
	 * Gets the table properties
	 * @return
	 */
	public String[] getModelTableProperties();
	
	/**
	 * Gets the column model
	 * @return
	 */
	public ColumnChooserModel getColumnModel();
	
	/**
	 * Gets the table model.
	 * @return
	 */
	public IBulkImportTableModel getTableModel();
	
	/**
	 * Gets the list of imported objects
	 * @return
	 */
	public MVCArrayList<? extends ITridas> getImportedList();
	
	/**
	 * Gets the string representations of the imported list
	 * @return
	 */
	public String[] getImportedListStrings();
	
	/**
	 * Convenience method so commands can just use the interface
	 * @return
	 */
	public String getImportedDynamicComboBoxKey();
	
	/**
	 * Gets the rows
	 * @return
	 */
	public MVCArrayList getRows();
}
