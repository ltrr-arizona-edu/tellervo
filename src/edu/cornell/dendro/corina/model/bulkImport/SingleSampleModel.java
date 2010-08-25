/**
 * Created on Jul 14, 2010, 2:10:47 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.Date;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleSampleModel extends HashModel implements ISingleRowModel {
	private static final long serialVersionUID = 1L;

	public static final String ELEMENT = "Parent Element";
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

	public static final String[] TABLE_PROPERTIES = {
		ELEMENT, TITLE, COMMENTS, TYPE, DESCRIPTION,
		SAMPLING_DATE, POSITION, STATE, KNOTS, BOX_ID, IMPORTED
	};
	
	public SingleSampleModel(){
		for(String s : TABLE_PROPERTIES){
			registerProperty(s, PropertyType.READ_WRITE);
		}
		registerProperty(IMPORTED, PropertyType.READ_ONLY, null);
		registerProperty(RADIUS_MODEL, PropertyType.READ_ONLY);
	}
	
	/**
	 * @see com.dmurph.mvc.model.HashModel#setProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public synchronized Object setProperty(String argKey, Object argProperty) {
		if(getPropertyType(argKey) != null){
			return super.setProperty(argKey, argProperty);
		}
		if(getRadiusModel() != null){
			SingleRadiusModel rmodel = getRadiusModel();
			if(rmodel.getPropertyType(argKey) != null){
				return rmodel.setProperty(argKey, argProperty);
			}
		}
		return super.setProperty(argKey, argProperty);
	}
	
	/**
	 * @see com.dmurph.mvc.model.HashModel#getProperty(java.lang.String)
	 */
	@Override
	public synchronized Object getProperty(String argKey) {
		if(getPropertyType(argKey) != null){
			return super.getProperty(argKey);
		}
		if(getRadiusModel() != null){
			SingleRadiusModel rmodel = getRadiusModel();
			if(rmodel.getPropertyType(argKey) != null){
				return rmodel.getProperty(argKey);
			}
		}
		return super.getProperty(argKey);
	}
	
	public void setImported(boolean argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
	
	public SingleRadiusModel getRadiusModel(){
		return (SingleRadiusModel) getProperty(RADIUS_MODEL);
	}
	
	public void setRadiusModel(SingleRadiusModel argModel){
		registerProperty(RADIUS_MODEL, PropertyType.READ_ONLY, argModel);
	}
	
	public void populateToTridasSample(TridasSample argSample){
		argSample.setTitle((String)getProperty(TITLE));
		argSample.setComments((String)getProperty(COMMENTS));
		argSample.setType((ControlledVoc)getProperty(TYPE));
		argSample.setDescription((String)getProperty(DESCRIPTION));
		argSample.setSamplingDate((Date) getProperty(SAMPLING_DATE));
		argSample.setPosition((String)getProperty(POSITION));
		argSample.setState((String)getProperty(STATE));
		argSample.setKnots((Boolean)getProperty(KNOTS));
		
		if(getProperty(BOX_ID) != null){
			TridasGenericField field = null;
			for(TridasGenericField gf: argSample.getGenericFields()){
				if(gf.getName().equals("corina.boxID")){
					field = gf;
				}
			}
			if(field == null){
				field = new TridasGenericField();
				argSample.getGenericFields().add(field);
			}
			field.setValue((String) getProperty(BOX_ID));
		}
		
		argSample.setIdentifier((TridasIdentifier) getProperty(IMPORTED));
	}
	
	public void populateFromTridasSample(TridasSample argSample){
		setProperty(TITLE, argSample.getTitle());
		setProperty(COMMENTS, argSample.getComments());
		setProperty(TYPE, argSample.getType());
		setProperty(DESCRIPTION, argSample.getDescription());
		setProperty(SAMPLING_DATE, argSample.getSamplingDate());
		setProperty(POSITION, argSample.getPosition());
		setProperty(STATE, argSample.getState());
		setProperty(KNOTS, argSample.isKnots());
		
		TridasGenericField field = null;
		for(TridasGenericField gf: argSample.getGenericFields()){
			if(gf.getName().equals("corina.boxID")){
				field = gf;
			}
		}
		if(field != null){
			setProperty(BOX_ID, field.getValue());
		}else{
			setProperty(BOX_ID, null);
		}
		
		setProperty(IMPORTED, argSample.getIdentifier());
	}
}