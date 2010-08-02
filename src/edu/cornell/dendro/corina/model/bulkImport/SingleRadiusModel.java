/**
 * Created on Jul 16, 2010, 7:34:07 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleRadiusModel extends HashModel implements ISingleRowModel{
	private static final long serialVersionUID = 1L;
	
	public static final String SAMPLE_CODE = "Sample Code";
	public static final String RADIUS_CODE = "Radius Code";
	public static final String TITLE = "Radius Title";
	public static final String COMMENTS = "Radius Comments";
	public static final String AZIMUTH = "Azimuth";
	
	public static final String[] PROPERTIES = {
		SAMPLE_CODE, RADIUS_CODE, TITLE, COMMENTS, AZIMUTH
	};
	
	public SingleRadiusModel(){
		registerProperty(PROPERTIES, PropertyType.READ_WRITE);
		registerProperty(RADIUS_CODE, PropertyType.READ_ONLY);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, false);
	}
	
	public void setRadiusCode(String argCode){
		registerProperty(RADIUS_CODE, PropertyType.READ_ONLY, argCode);
	}
	
	public void setImported(boolean argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
}
