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
	
	private final MVCArrayList<String> possibleColumns;
	
	public ColumnChooserModel(){
		possibleColumns = new MVCArrayList<String>();
//		for(String s: argPossibleProperties){
//			possibleColumns.add(s);
//		}
	}
	
	/**
	 * Note that this property doesn't get affected by any mvc operations on the {@link ColumnChooserModel} 
	 * object.
	 * @return
	 */
	public MVCArrayList<String> getPossibleColumns(){
		return possibleColumns;
	}
}
