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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.schema.UserExtendableDataType;
import org.tellervo.schema.UserExtendableEntity;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSIUserDefinedField;
import org.tridas.io.util.DateUtils;
import org.tridas.io.util.DateUtils.DatePrecision;
import org.tridas.schema.Certainty;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.Date;
import org.tridas.schema.DateTime;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;
import com.dmurph.mvc.model.HashModel.PropertyType;

import edu.emory.mathcs.backport.java.util.Collections;


/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("unchecked")
public class SingleSampleModel extends HashModel implements IBulkImportSingleRowModel {
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(SingleSampleModel.class);

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
	public static final String SAMPLE_STATUS = "Sample Status";
	//public static final String CURATION_STATUS = "Curation Status";
	
	
	// radius stuff
	public static final String RADIUS_MODEL = "RADIUS_MODEL";
	
	// list of possible elements from the chosen parent object
	public static final String POSSIBLE_ELEMENTS = "POSSIBLE_ELEMENTS";
	public static final String POPULATING_ELEMENT_LIST = "POPULATING_ELEMENT_LIST";
	

	public static String[] TABLE_PROPERTIES = {
		IMPORTED, OBJECT, ELEMENT, TITLE, COMMENTS, TYPE, DESCRIPTION, FILES,
		SAMPLING_DATE, POSITION, STATE, KNOTS, BOX, EXTERNAL_ID, SAMPLE_STATUS, //CURATION_STATUS
	};
	private static boolean isUserDefinedFieldsInit = false;
	
	
	public SingleSampleModel(){
		
		
		if(isUserDefinedFieldsInit==false)
		{
			ArrayList<String> arr = new ArrayList<String>(Arrays.asList(TABLE_PROPERTIES));

			MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
		
			for(WSIUserDefinedField fld : udfdictionary)
			{
				if(fld.getAttachedto().equals(UserExtendableEntity.SAMPLE))
				{
					arr.add(fld.getLongfieldname());
				}
			}
			
			Collections.sort(arr);
			
			TABLE_PROPERTIES = arr.toArray(new String[arr.size()]);
			
			Arrays.sort(TABLE_PROPERTIES);
			
			isUserDefinedFieldsInit = true;
			
			for(String s : TABLE_PROPERTIES){
				registerProperty(s, PropertyType.READ_WRITE);
			}
			
		}
		
		
		log.debug("Number of columns is : "+TABLE_PROPERTIES.length);
		registerProperty(TABLE_PROPERTIES, PropertyType.READ_WRITE);
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
	
	public void populateToTridasSample(TridasSample argSample) throws Exception{
		argSample.setTitle((String)getProperty(TITLE));
		argSample.setComments((String)getProperty(COMMENTS));
		argSample.setType((ControlledVoc)getProperty(TYPE));
		argSample.setDescription((String)getProperty(DESCRIPTION));
		argSample.setFiles((TridasFileList) getProperty(FILES));
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
		if(getProperty(SAMPLE_STATUS) != null){
			TridasGenericField field = null;
			for(TridasGenericField gf: argSample.getGenericFields()){
				if(gf.getName().equals("tellervo.sampleStatus")){
					field = gf;
				}
			}
			if(field == null){
				field = new TridasGenericField();
				argSample.getGenericFields().add(field);
			}
			field.setName("tellervo.sampleStatus");
			field.setType("xs:string");
			field.setValue(getProperty(SAMPLE_STATUS).toString());
		}
		
		/*if(getProperty(CURATION_STATUS) != null){
			TridasGenericField field = null;
			for(TridasGenericField gf: argSample.getGenericFields()){
				if(gf.getName().equals("tellervo.curationStatus")){
					field = gf;
				}
			}
			if(field == null){
				field = new TridasGenericField();
				argSample.getGenericFields().add(field);
			}
			field.setName("tellervo.curationStatus");
			field.setType("xs:string");
			field.setValue(getProperty(CURATION_STATUS).toString());
		}*/
		
		

		// Handle all user defined fields
		MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
		for(WSIUserDefinedField fld : udfdictionary)
		{
			if(fld.getAttachedto().equals(UserExtendableEntity.SAMPLE))
			{	
				
				TridasGenericField field = null;
				for(TridasGenericField gf: argSample.getGenericFields()){
					if(gf.getName().equals(fld.getName())){
						field = gf;
					}
				}
				if(field == null){
					field = new TridasGenericField();
					argSample.getGenericFields().add(field);
				}
				
				if(fld.isSetName() && fld.isSetDatatype() && fld.isSetLongfieldname())
				{				
					field.setName(fld.getName());
					field.setType(fld.getDatatype().value());
					Object prop = getProperty(fld.getLongfieldname());
					if(prop!=null){
						field.setValue(prop.toString().replace("&", "&amp;"));
					}
					else
					{
						field.setValue(null);
						log.debug(fld.getName() + " was empty when writing to XML");
						//argSample.getGenericFields().add(field);
					}
				}
				else
				{
					log.error("Failed to write "+fld.getName()+" field to XML");
				}
				
			}
		}
		
		
		
		argSample.setIdentifier((TridasIdentifier) getProperty(IMPORTED));
		
		if(getProperty(SAMPLING_DATE)!=null)
		{
		
			DateTime mydatetime = DateUtils.parseDateTimeFromNaturalString((String) getProperty(SAMPLING_DATE));
			DatePrecision prec = DateUtils.getDatePrecision((String) getProperty(SAMPLING_DATE));
			
			Date mydate = DateUtils.dateTimeToDate(mydatetime);
			
			if(mydate==null) throw new Exception ("Unable to parse the sampling date from the information given.");
			
			if(prec!=DatePrecision.DAY)
			{
				mydate.setCertainty(Certainty.APPROXIMATELY);
			}
			else
			{
				mydate.setCertainty(Certainty.EXACT);
			}
			
			argSample.setSamplingDate(mydate);
			
			TridasGenericField field = null;
			for(TridasGenericField gf: argSample.getGenericFields()){
				if(gf.getName().equals("tellervo.samplingDatePrecision")){
					field = gf;
				}
			}
			if(field == null){
				field = new TridasGenericField();
				argSample.getGenericFields().add(field);
			}
			field.setName("tellervo.samplingDatePrecision");
			field.setType("xs:string");
			field.setValue(prec.toString());
		}
	}
	
	public void populateFromTridasSample(TridasSample argSample){
		setProperty(TITLE, argSample.getTitle());
		setProperty(COMMENTS, argSample.getComments());
		setProperty(TYPE, argSample.getType());
		setProperty(DESCRIPTION, argSample.getDescription());
		setProperty(POSITION, argSample.getPosition());
		setProperty(STATE, argSample.getState());
		setProperty(KNOTS, argSample.isKnots());
		setImported(argSample.getIdentifier());
		
		

		
		
		// Set box to null initially
		setProperty(BOX, null);
		
		// Files
		setProperty(FILES, new TridasFileList(argSample.getFiles()));
			
		boolean samplingDatePrecSet = false;
		
		// Handle Generic Fields
		TridasGenericField field = null;
		for(TridasGenericField gf: argSample.getGenericFields()){
			if(gf.getName().equals("tellervo.boxID")){
				field = gf;
			}
			else if(gf.getName().equals("tellervo.samplingDatePrecision")){
				if(argSample.isSetSamplingDate())
				{			
					samplingDatePrecSet = true;
					Date samplingdate = argSample.getSamplingDate();
					
					if(gf.getValue().toLowerCase().equals("day"))
					{
						setProperty(SAMPLING_DATE, DateUtils.getFormattedDate(samplingdate, new SimpleDateFormat("yyyy-MM-dd")));

					}
					else if(gf.getValue().toLowerCase().equals("month"))
					{
						setProperty(SAMPLING_DATE, DateUtils.getFormattedDate(samplingdate, new SimpleDateFormat("yyyy-MM")));

					}
					else if(gf.getValue().toLowerCase().equals("year"))
					{
						setProperty(SAMPLING_DATE, DateUtils.getFormattedDate(samplingdate, new SimpleDateFormat("yyyy")));

					}
					else
					{
						log.debug("Unknown sampling date precision.  Defaulting to 'day'");
						setProperty(SAMPLING_DATE, DateUtils.getFormattedDate(samplingdate, new SimpleDateFormat("yyyy-MM-dd")));

					}
					
				}
			}
			else if(gf.getName().equals("tellervo.externalID")){
				
				setProperty(EXTERNAL_ID, gf.getValue());
			}
			else if(gf.getName().equals("tellervo.sampleStatus")){
				setProperty(SAMPLE_STATUS, gf.getValue());
			}
			/*else if(gf.getName().equals("tellervo.curationStatus")){
				setProperty(CURATION_STATUS, gf.getValue());
			}*/
			else if (gf.getName().startsWith("userDefinedField"))
			{
				MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
				
				for(WSIUserDefinedField fld : udfdictionary)
				{
					if(fld.getAttachedto().equals(UserExtendableEntity.SAMPLE))
					{
						if(gf.getName().equals(fld.getName()))
						{
							
							if(!gf.isSetValue() || gf.getValue()==null || gf.getValue().length()==0) continue;
							
							try{
								Object val = null;
								if(fld.getDatatype().equals(UserExtendableDataType.XS___BOOLEAN))
								{
									val = false;
									if(gf.getValue().toLowerCase().equals("true") || gf.getValue().toLowerCase().equals("t"))
									{
										
										val = true;
									}									
								}
								else if (fld.getDatatype().equals(UserExtendableDataType.XS___FLOAT))
								{
									val = Float.parseFloat(gf.getValue());
								}
								else if (fld.getDatatype().equals(UserExtendableDataType.XS___INT))
								{
									val = Integer.parseInt(gf.getValue());
								}
								else if (fld.getDatatype().equals(UserExtendableDataType.XS___STRING))
								{
									val = gf.getValue();
								}
								else
								{
									log.error("Unsupported data type for generic field");
									
								}
								
								setProperty(fld.getLongfieldname(), val);
							} catch (NumberFormatException e)
							{
								
								log.error("Failed to cast number value in user defined field "+fld.getName());
							}

						}
					}
				}
			}
		}
		
		if(samplingDatePrecSet== false && argSample.isSetSamplingDate())
		{
			// Sampling date precision not set, but sampling date has been
			// Default to day precision and set value accordingly
			Date samplingdate = argSample.getSamplingDate();
			setProperty(SAMPLING_DATE, DateUtils.getFormattedDate(samplingdate, new SimpleDateFormat("yyyy-MM-dd")));
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
