/**
 * Created at Jul 24, 2010, 3:33:39 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author daniel
 *
 */
public class ColumnChooserModel extends MVCArrayList<String> {	
	private static final long serialVersionUID = 1L;
	
	public final String[] possibleColumns;
	
	public ColumnChooserModel(String[] argPossibleProperties){
		possibleColumns = argPossibleProperties;
	}
}
