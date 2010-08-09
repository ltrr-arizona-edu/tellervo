/**
  * Created on Jul 13, 2010, 6:28:52 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleObjectModel extends HashModel implements ISingleRowModel{
	private static final long serialVersionUID = 4267926250106915154L;
	
	public static final String OBJECT_CODE = "Object Code";
	public static final String TITLE = "Title";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	public static final String LATITUDE = "Latitude";
	public static final String LONGTITUDE = "Longtitude";
	
	
	public static final String[] PROPERTIES = {
		OBJECT_CODE, TITLE, COMMENTS, TYPE, DESCRIPTION, LATITUDE, LONGTITUDE
	};
	
	public SingleObjectModel(){
		registerProperty(PROPERTIES, PropertyType.READ_WRITE);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, false);
	}
	
	
	public void setImported(boolean argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
}
