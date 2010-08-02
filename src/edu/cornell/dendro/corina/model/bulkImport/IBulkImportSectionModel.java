/**
 * Created at Aug 1, 2010, 3:04:07 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.IModel;

/**
 * @author daniel
 *
 */
public interface IBulkImportSectionModel {
	public static final String COLUMN_MODEL = "columnModel";
	public static final String ROWS = "rows";
	
	/**
	 * Gets an instance of a row object for this model;
	 * @return
	 */
	public ISingleRowModel createRowInstance();
}
