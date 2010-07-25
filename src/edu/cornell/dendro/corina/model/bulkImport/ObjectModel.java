/**
 * Created on Jul 16, 2010, 6:44:22 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.util.MVCArrayList;

/**
 * @author Daniel Murphy
 *
 */
public class ObjectModel extends HashModel{
	private static final long serialVersionUID = 1L;
	
	public static final String OBJECTS = "objects";
	public static final String TABLE_MODEL = "tableModel";
	public static final String COLUMN_MODEL = "columnModel";
	
	public ObjectModel(){
		registerProperty(OBJECTS, PropertyType.FINAL, new MVCArrayList<SingleObjectModel>());
		registerProperty(COLUMN_MODEL, PropertyType.FINAL, new ColumnChooserModel());
		registerProperty(TABLE_MODEL, PropertyType.FINAL, new ObjectTableModel(this));
	}
}
