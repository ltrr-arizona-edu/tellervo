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
