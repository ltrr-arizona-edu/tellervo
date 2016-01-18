/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
/**
 * Created on Jul 14, 2010, 2:10:47 AM
 */
package org.tellervo.desktop.bulkdataentry.model;

import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.schema.WSIBox;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.Date;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("unchecked")
public class SingleSampleModel extends HashModel implements IBulkImportSingleRowModel {
	private static final long serialVersionUID = 1L;

	public static final String OBJECT = "Object code";
	public static final String ELEMENT = "Element code";
	public static final String TITLE = "Sample code";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	public static final String FILES = "File references";
	public static final String SAMPLING_DATE = "Sampling Date";
	public static final String POSITION = "Position";
	public static final String STATE = "State";
	public static final String KNOTS = "Knots";
	public static final String BOX = "Box";
	public static final String IMPORTED = "Imported";
	public static final String EXTERNAL_ID = "External ID";

	
	// radius stuff
	public static final String RADIUS_MODEL = "RADIUS_MODEL";
	
	// list of possible elements from the chosen parent object
	public static final String POSSIBLE_ELEMENTS = "POSSIBLE_ELEMENTS";
	public static final String POPULATING_ELEMENT_LIST = "POPULATING_ELEMENT_LIST";
	

	public static final String[] TABLE_PROPERTIES = {
		OBJECT, ELEMENT, TITLE, COMMENTS, TYPE, DESCRIPTION, FILES,
		SAMPLING_DATE, POSITION, STATE, KNOTS, BOX, EXTERNAL_ID, IMPORTED
	};
	
	
	public SingleSampleModel(){
		for(String s : TABLE_PROPERTIES){
			registerProperty(s, PropertyType.READ_WRITE);
		}
		registerProperty(IMPORTED, PropertyType.READ_ONLY, null);
		registerProperty(RADIUS_MODEL, PropertyType.READ_ONLY, null);
		registerProperty(POSSIBLE_ELEMENTS, PropertyType.READ_WRITE, new MVCArrayList<TridasElement>());
		registerProperty(POPULATING_ELEMENT_LIST, PropertyType.READ_WRITE, false);
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
	 * @see com.dmurph.mvc.support.AbstractMVCSupport#cloneImpl(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object cloneImpl(String argProperty, Object argO) {
		if(argProperty.equals(IMPORTED)){
			return null;
		}
		return super.cloneImpl(argProperty, argO);
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
	
	public void setImported(TridasIdentifier argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
	
	public TridasIdentifier getImported(){
		return (TridasIdentifier) super.getProperty(IMPORTED);
	}
	
	public SingleRadiusModel getRadiusModel(){
		return (SingleRadiusModel) super.getProperty(RADIUS_MODEL);
	}
	
	public void setRadiusModel(SingleRadiusModel argModel){
		registerProperty(RADIUS_MODEL, PropertyType.READ_ONLY, argModel);
	}
	
	public MVCArrayList<TridasElement> getPossibleElements(){
		return (MVCArrayList<TridasElement>) getProperty(POSSIBLE_ELEMENTS);
	}
	
	public boolean isElementListReady()
	{
		MVCArrayList<TridasElement> list = (MVCArrayList<TridasElement>) getProperty(POSSIBLE_ELEMENTS);
		Boolean isPopulating = (Boolean) getProperty(POPULATING_ELEMENT_LIST);
		if(isPopulating) return false;
		
		if(isPopulating==false && list.size()>0) return true;
		
		return false;
		
	}
	
	public void populateToTridasSample(TridasSample argSample){
		argSample.setTitle((String)getProperty(TITLE));
		argSample.setComments((String)getProperty(COMMENTS));
		argSample.setType((ControlledVoc)getProperty(TYPE));
		argSample.setDescription((String)getProperty(DESCRIPTION));
		argSample.setFiles((TridasFileList) getProperty(FILES));
		argSample.setSamplingDate((Date) getProperty(SAMPLING_DATE));
		argSample.setPosition((String)getProperty(POSITION));
		argSample.setState((String)getProperty(STATE));
		argSample.setKnots((Boolean)getProperty(KNOTS));

		if(getProperty(BOX) != null){
			TridasGenericField field = null;
			for(TridasGenericField gf: argSample.getGenericFields()){
				if(gf.getName().equals("tellervo.boxID")){
					field = gf;
				}
			}
			if(field == null){
				field = new TridasGenericField();
				argSample.getGenericFields().add(field);
			}
			field.setName("tellervo.boxID");
			field.setType("xs:string");
			field.setValue(((WSIBox)getProperty(BOX)).getIdentifier().getValue());
		}
		
		if(getProperty(EXTERNAL_ID) != null){
			TridasGenericField field = null;
			for(TridasGenericField gf: argSample.getGenericFields()){
				if(gf.getName().equals("tellervo.externalID")){
					field = gf;
				}
			}
			if(field == null){
				field = new TridasGenericField();
				argSample.getGenericFields().add(field);
			}
			field.setName("tellervo.externalID");
			field.setType("xs:string");
			field.setValue(getProperty(EXTERNAL_ID).toString());
		}
		
		argSample.setIdentifier((TridasIdentifier) getProperty(IMPORTED));
	}
	
	public void populateFromTridasSample(TridasSample argSample){
		setProperty(TITLE, argSample.getTitle());
		setProperty(COMMENTS, argSample.getComments());
		setProperty(TYPE, argSample.getType());
		setProperty(DESCRIPTION, argSample.getDescription());
		setProperty(FILES, argSample.getFiles());
		setProperty(SAMPLING_DATE, argSample.getSamplingDate());
		setProperty(POSITION, argSample.getPosition());
		setProperty(STATE, argSample.getState());
		setProperty(KNOTS, argSample.isKnots());
		setImported(argSample.getIdentifier());
		
		// Set box to null initially
		setProperty(BOX, null);
		
		// Get boxID generic field
		TridasGenericField field = null;

		for(TridasGenericField gf: argSample.getGenericFields()){
			if(gf.getName().equals("tellervo.boxID")){
				field = gf;
			}
			else if(gf.getName().equals("tellervo.externalID")){
				setProperty(EXTERNAL_ID, field.getValue());
			}
		}
			
		if(field != null){
			// Find the box in the dictionary using its identifier and set it in this singlesamplemodel 
			MVCArrayList<WSIBox> boxdict = Dictionary.getMutableDictionary("boxDictionary");
			for(WSIBox bx : boxdict)
			{
				if(field.getValue().equals(bx.getIdentifier().getValue()))
				{
					setProperty(BOX, bx);
					break;
				}
			}
		}
	}
}
