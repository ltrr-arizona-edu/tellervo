/**
 * Created on Jul 14, 2010, 1:36:35 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleElementModel extends HashModel {
	private static final long serialVersionUID = 1L;	
	
	public static final String OBJECT_CODE = "Object Code";
	public static final String ELEMENT_CODE = "Element Code";
	public static final String TITLE = "Title";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	public static final String TAXON = "Taxon";
	public static final String SHAPE = "Shape";
	public static final String HEIGHT = "Height";
	public static final String WIDTH = "Width";
	public static final String DEPTH = "Depth";
	public static final String UNIT = "Unit";
	public static final String LATITUDE = "Latitude";
	public static final String LONGTITUDE = "Longtitude";
	public static final String SLOPE_ANGLE = "Slope Angle";
	public static final String SLOPE_AZIMUTH = "Slope Azimuth";
	public static final String SOIL_DESCRIPTION = "Soil Description";
	public static final String SOIL_DEPTH = "Soil Depth";
	public static final String BEDROCK_DESCRIPTION = "Bedrock Description";
	public static final String IMPORTED = "Imported";

	public static final String[] PROPERTIES = {
		ELEMENT_CODE, TITLE, COMMENTS, TYPE, DESCRIPTION, TAXON,
		SHAPE, HEIGHT, WIDTH, DEPTH, UNIT, LATITUDE, LONGTITUDE,
		SLOPE_ANGLE, SLOPE_AZIMUTH, SOIL_DESCRIPTION, SOIL_DEPTH,
		BEDROCK_DESCRIPTION, IMPORTED
	};
	
	public SingleElementModel(){
		for(String s : PROPERTIES){
			if(s.equals(ELEMENT_CODE)){
				continue;
			}
			registerProperty(s, PropertyType.READ_WRITE, null);
		}
		registerProperty(ELEMENT_CODE, PropertyType.READ_ONLY);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, false);
	}
	
	public void setElementCode(String argCode){
		registerProperty(ELEMENT_CODE, PropertyType.READ_ONLY, argCode);
	}
	
	public void setImported(boolean argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
}
