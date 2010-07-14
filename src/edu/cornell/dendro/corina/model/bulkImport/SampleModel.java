/**
 * Created on Jul 14, 2010, 2:10:47 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SampleModel extends HashModel {
	public static final String ELEMENT_CODE = "Element Code";
	public static final String TITLE = "Title";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	public static final String SAMPLING_DATE = "Sampling Date";
	public static final String POSITION = "Position";
	public static final String STATE = "State";
	public static final String KNOTS = "Knots";
	public static final String BOX_ID = "BoxID";
	
	public static final String[] PROPERTIES = {
		ELEMENT_CODE, TITLE, COMMENTS, TYPE, DESCRIPTION,
		SAMPLING_DATE, POSITION, STATE, KNOTS, BOX_ID
	};
	
	public SampleModel(){
		for(String s : PROPERTIES){
			registerProperty(s, PropertyType.READ_WRITE);
		}
	}
}