/**
 * Created on Jul 14, 2010, 2:10:47 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleSampleModel extends HashModel {
	private static final long serialVersionUID = 1L;

	public static final String ELEMENT_CODE = "Element Code";
	public static final String SAMPLE_CODE = "Sample Code";
	public static final String TITLE = "Title";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	public static final String SAMPLING_DATE = "Sampling Date";
	public static final String POSITION = "Position";
	public static final String STATE = "State";
	public static final String KNOTS = "Knots";
	public static final String BOX_ID = "BoxID";
	public static final String IMPORTED = "Imported";
	
	// radius stuff
	public static final String RADIUS_MODEL = "RADIUS_MODEL";

	public static final String[] PROPERTIES = {
		ELEMENT_CODE, SAMPLE_CODE, TITLE, COMMENTS, TYPE, DESCRIPTION,
		SAMPLING_DATE, POSITION, STATE, KNOTS, BOX_ID, IMPORTED
	};
	
	public SingleSampleModel(){
		for(String s : PROPERTIES){
			registerProperty(s, PropertyType.READ_WRITE);
		}
		registerProperty(SAMPLE_CODE, PropertyType.READ_ONLY);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, false);
		registerProperty(RADIUS_MODEL, PropertyType.READ_ONLY);
	}
	
	public void setSampleCode(String argCode){
		registerProperty(SAMPLE_CODE, PropertyType.READ_ONLY, argCode);
	}
	
	public void setImported(boolean argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
	
	public void setRadiusModel(SingleRadiusModel argModel){
		if(argModel != null){
			argModel.setProperty(SingleRadiusModel.SAMPLE_CODE, getProperty(SAMPLE_CODE));
		}
		registerProperty(RADIUS_MODEL, PropertyType.READ_ONLY, argModel);
	}
}