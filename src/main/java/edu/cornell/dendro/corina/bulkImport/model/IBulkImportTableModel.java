/**
 * Created at Aug 24, 2010, 3:09:24 PM
 */
package edu.cornell.dendro.corina.bulkImport.model;

import java.util.ArrayList;

import javax.swing.table.TableModel;

/**
 * @author Daniel
 *
 */
public interface IBulkImportTableModel extends TableModel {

	public void selectAll();
	
	public void selectNone();
	
	public int getSelectedCount();
	
	public void getSelected(ArrayList<IBulkImportSingleRowModel> argModels);
}
