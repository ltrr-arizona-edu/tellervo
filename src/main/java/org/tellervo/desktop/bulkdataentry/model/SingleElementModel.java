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
 * Created on Jul 14, 2010, 1:36:35 AM
 */
package org.tellervo.desktop.bulkdataentry.model;

import java.math.BigDecimal;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasBedrock;
import org.tridas.schema.TridasDimensions;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasSlope;
import org.tridas.schema.TridasSoil;
import org.tridas.schema.TridasUnit;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleElementModel extends HashModel implements IBulkImportSingleRowModel{
	private static final long serialVersionUID = 1L;	
	
	public static final String OBJECT = "Object code";
	public static final String TITLE = "Element code";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	//public static final String FILES = "Files";
	public static final String TAXON = "Taxon";
	public static final String SHAPE = "Shape";
	public static final String HEIGHT = "Height";
	public static final String WIDTH = "Width";
	public static final String DIAMETER = "Diameter";
	public static final String DEPTH = "Depth";
	public static final String UNIT = "Unit";
	//public static final String AUTHENTICITY = "Authenticity";
	public static final String WAYPOINT = "Waypoint";
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longtitude";
	public static final String LOCATION_PRECISION = "Location precision";
	public static final String LOCATION_COMMENT = "Location comment";
	public static final String ADDRESSLINE1 = "Address 1";
	public static final String ADDRESSLINE2 = "Address 2";
	public static final String CITY_TOWN = "City/Town";
	public static final String STATE_PROVINCE_REGION = "State/Province/Region";
	public static final String POSTCODE = "Postal Code";
	public static final String COUNTRY = "Country";
	public static final String MARKS = "Marks";
	public static final String ALTITUDE = "Altitude";
	public static final String SLOPE_ANGLE = "Slope Angle";
	public static final String SLOPE_AZIMUTH = "Slope Azimuth";
	public static final String SOIL_DESCRIPTION = "Soil Description";
	public static final String SOIL_DEPTH = "Soil Depth";
	public static final String BEDROCK_DESCRIPTION = "Bedrock Description";
	

	public static final String[] TABLE_PROPERTIES = {
		OBJECT, TITLE, TYPE, TAXON, COMMENTS, DESCRIPTION, LATITUDE, LONGITUDE,WAYPOINT,
		SHAPE, HEIGHT, WIDTH, DEPTH, UNIT, 
		LOCATION_PRECISION, LOCATION_COMMENT,
		ADDRESSLINE1, ADDRESSLINE2,	CITY_TOWN, STATE_PROVINCE_REGION, 
		POSTCODE, COUNTRY, SLOPE_ANGLE, SLOPE_AZIMUTH, SOIL_DESCRIPTION, SOIL_DEPTH,
		BEDROCK_DESCRIPTION, MARKS, ALTITUDE,
	};
	
	public SingleElementModel(){
		registerProperty(TABLE_PROPERTIES, PropertyType.READ_WRITE);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, null);
	}
	
	public void setImported(TridasIdentifier argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
	
	public TridasIdentifier getImported(){
		return (TridasIdentifier) getProperty(IMPORTED);
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
	
	public void populateToTridasElement(TridasElement argElement){

		argElement.setIdentifier((TridasIdentifier) getProperty(IMPORTED));
		argElement.setTitle( (String) getProperty(TITLE));
		argElement.setComments( (String) getProperty(COMMENTS));
		
		argElement.setType((ControlledVoc) getProperty(TYPE));
		argElement.setDescription((String) getProperty(DESCRIPTION));
		argElement.setTaxon((ControlledVoc) getProperty(TAXON));
		argElement.setShape((TridasShape) getProperty(SHAPE));
		argElement.setMarks((String) getProperty(MARKS));
		argElement.setAltitude((Double) getProperty(ALTITUDE));
		
		TridasDimensions d = new TridasDimensions();

		d.setWidth((BigDecimal)getProperty(WIDTH));
		d.setHeight((BigDecimal)getProperty(HEIGHT));
		d.setDiameter((BigDecimal)getProperty(DIAMETER));
		d.setDepth((BigDecimal)getProperty(DEPTH));
		d.setUnit((TridasUnit) getProperty(UNIT));
		if(d.getWidth() != null || d.getHeight() != null || d.getDepth() != null || d.getDiameter() != null
				|| d.getUnit() != null){
			argElement.setDimensions(d);
		}else{
			argElement.setDimensions(null);
		}
		
		// i love how nested this is!
		if(argElement.getLocation() != null &&
				argElement.getLocation().getLocationGeometry() != null&&
				argElement.getLocation().getLocationGeometry().getPoint() != null &&
				argElement.getLocation().getLocationGeometry().getPoint().getPos() != null &&
				argElement.getLocation().getLocationGeometry().getPoint().getPos().getValues().size() == 2){
			setProperty(LATITUDE, argElement.getLocation().getLocationGeometry().getPoint().getPos().getValues().get(0));
			setProperty(LONGITUDE, argElement.getLocation().getLocationGeometry().getPoint().getPos().getValues().get(1));
		}else{
			setProperty(LATITUDE, null);
			setProperty(LONGITUDE, null);
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetAddress() &&
				argElement.getLocation().getAddress().isSetAddressLine1())
		{
			setProperty(ADDRESSLINE1, argElement.getLocation().getAddress().getAddressLine1());
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetAddress() &&
				argElement.getLocation().getAddress().isSetAddressLine2())
		{
			setProperty(ADDRESSLINE2, argElement.getLocation().getAddress().getAddressLine2());
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetAddress() &&
				argElement.getLocation().getAddress().isSetCityOrTown())
		{
			setProperty(CITY_TOWN, argElement.getLocation().getAddress().getCityOrTown());
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetAddress() &&
				argElement.getLocation().getAddress().isSetCountry())
		{
			setProperty(COUNTRY, argElement.getLocation().getAddress().getCountry());
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetAddress() &&
				argElement.getLocation().getAddress().isSetPostalCode())
		{
			setProperty(POSTCODE, argElement.getLocation().getAddress().getPostalCode());
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetAddress() &&
				argElement.getLocation().getAddress().isSetStateProvinceRegion())
		{
			setProperty(STATE_PROVINCE_REGION, argElement.getLocation().getAddress().getStateProvinceRegion());
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetLocationPrecision())
		{
			setProperty(LOCATION_PRECISION, argElement.getLocation().getLocationPrecision());
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetLocationComment())
		{
			setProperty(LOCATION_COMMENT, argElement.getLocation().getLocationComment());
		}
		TridasSlope slope = new TridasSlope();
		slope.setAngle((Integer)getProperty(SLOPE_ANGLE));
		slope.setAzimuth((Integer) getProperty(SLOPE_AZIMUTH));
		if(slope.getAngle() != null || slope.getAzimuth() != null){
			argElement.setSlope(slope);
		}else{
			argElement.setSlope(null);
		}
		
		TridasSoil soil = new TridasSoil();
		soil.setDepth((Double) getProperty(SOIL_DEPTH));
		soil.setDescription((String) getProperty(DESCRIPTION));
		if(soil.getDepth() != null || soil.getDescription() != null){
			argElement.setSoil(soil);
		}else{
			argElement.setSoil(null);
		}
		
		TridasBedrock bedrock = new TridasBedrock();
		bedrock.setDescription((String) getProperty(BEDROCK_DESCRIPTION));
		if(bedrock.getDescription() != null){
			argElement.setBedrock(bedrock);
		}else{
			argElement.setBedrock(null);
		}
	}
	
	public void populateFromTridasElement(TridasElement argElement){
		setImported(argElement.getIdentifier());
		
		setProperty(TITLE, argElement.getTitle());
		setProperty(COMMENTS, argElement.getComments());
		setProperty(TYPE, argElement.getType());
		setProperty(DESCRIPTION, argElement.getDescription());
		setProperty(TAXON, argElement.getTaxon());
		setProperty(SHAPE, argElement.getShape());
		if(argElement.getDimensions() != null){
			TridasDimensions d = argElement.getDimensions();
			setProperty(HEIGHT, d.getHeight());
			setProperty(WIDTH, d.getWidth());
			setProperty(DIAMETER, d.getDiameter());
			setProperty(DEPTH, d.getDepth());
			setProperty(UNIT, d.getUnit());
		}
		
		// I love how nested this is!
		if(argElement.getLocation() != null &&
				argElement.getLocation().getLocationGeometry() != null&&
				argElement.getLocation().getLocationGeometry().getPoint() != null &&
				argElement.getLocation().getLocationGeometry().getPoint().getPos() != null &&
				argElement.getLocation().getLocationGeometry().getPoint().getPos().getValues().size() == 2){
			setProperty(LATITUDE, argElement.getLocation().getLocationGeometry().getPoint().getPos().getValues().get(0));
			setProperty(LONGITUDE, argElement.getLocation().getLocationGeometry().getPoint().getPos().getValues().get(1));
		}else{
			setProperty(LATITUDE, null);
			setProperty(LONGITUDE, null);
		}
		
		if(argElement.getSlope() != null){
			setProperty(SLOPE_ANGLE, argElement.getSlope().getAngle());
			setProperty(SLOPE_AZIMUTH, argElement.getSlope().getAzimuth());
		}
		
		if(argElement.getSoil() != null){
			setProperty(SOIL_DEPTH, argElement.getSoil().getDepth());
			setProperty(SOIL_DESCRIPTION, argElement.getSoil().getDescription());
		}
		
		if(argElement.getBedrock() != null){
			setProperty(BEDROCK_DESCRIPTION, argElement.getBedrock().getDescription());
		}
	}
}
