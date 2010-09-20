/**
 * Created at Aug 24, 2010, 3:09:24 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import javax.swing.table.TableModel;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel
 *
 */
public interface IBulkImportTableModel extends TableModel {

	public void selectAll();
	
	public void selectNone();
	
	public Integer getSelectedCount();
	
	public HashModel[] getSelectedRows();
}
