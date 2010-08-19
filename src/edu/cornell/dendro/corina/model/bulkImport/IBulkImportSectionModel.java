/**
 * Created at Aug 1, 2010, 3:04:07 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

/**
 * @author daniel
 *
 */
public interface IBulkImportSectionModel {
	public static final String COLUMN_MODEL = "columnModel";
	public static final String ROWS = "rows";
	public static final String TABLE_MODEL = "tableModel";
	public static final String IMPORTED_LIST = "importedList";
	
	/**
	 * goes to the table model, which handles the selected rows
	 */
	public void removeSelected();
	
	/**
	 * Gets an instance of a row object for this model;
	 * @return
	 */
	public ISingleRowModel createRowInstance();
}
