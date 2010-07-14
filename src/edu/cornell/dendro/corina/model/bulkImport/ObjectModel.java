/**
 * Created on Jul 13, 2010, 6:28:52 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class ObjectModel extends HashModel {
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
	
	public ObjectModel(){
		registerProperty(TITLE, PropertyType.READ_WRITE);
		registerProperty(COMMENTS, PropertyType.READ_WRITE);
		registerProperty(TYPE, PropertyType.READ_WRITE);
		registerProperty(DESCRIPTION, PropertyType.READ_WRITE);
		registerProperty(OBJECT_CODE, PropertyType.READ_ONLY);
		registerProperty(LATITUDE, PropertyType.READ_WRITE);
		registerProperty(LONGTITUDE, PropertyType.READ_WRITE);
	}
	
	public void setElementCode(String argCode){
		registerProperty(OBJECT_CODE, PropertyType.READ_ONLY, argCode);
	}
}
