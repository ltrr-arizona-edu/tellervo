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

import net.opengis.gml.schema.PointType;
import net.opengis.gml.schema.Pos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.command.ImportSelectedElementsCommand;
import org.tridas.io.formats.heidelberg.HeidelbergToTridasDefaults.DefaultFields;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasBedrock;
import org.tridas.schema.TridasDimensions;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasSlope;
import org.tridas.schema.TridasSoil;
import org.tridas.schema.TridasUnit;
import org.tridas.spatial.GMLPointSRSHandler;
import org.tridas.spatial.SpatialUtils;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleElementModel extends HashModel implements IBulkImportSingleRowModel{
	private static final long serialVersionUID = 1L;	
	private static final Logger log = LoggerFactory.getLogger(SingleElementModel.class);

	public static final String OBJECT = "Object code";
	public static final String TITLE = "Element code";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	public static final String FILES = "File references";
	public static final String TAXON = "Taxon";
	public static final String SHAPE = "Shape";
	public static final String HEIGHT = "Height";
	public static final String WIDTH = "Width";
	public static final String DIAMETER = "Diameter";
	public static final String DEPTH = "Depth";
	public static final String UNIT = "Unit";
	public static final String AUTHENTICITY = "Authenticity";
	public static final String WAYPOINT = "Waypoint";
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longtitude";
	public static final String LOCATION_PRECISION = "Location precision";
	public static final String LOCATION_COMMENT = "Location comment";
	public static final String LOCATION_TYPE = "Location type";
	public static final String ADDRESSLINE1 = "Address 1";
	public static final String ADDRESSLINE2 = "Address 2";
	public static final String CITY_TOWN = "City/Town";
	public static final String STATE_PROVINCE_REGION = "State/Province/Region";
	public static final String POSTCODE = "Postal Code";
	public static final String COUNTRY = "Country";
	public static final String PROCESSING = "Processing";
	public static final String MARKS = "Marks";
	public static final String ALTITUDE = "Altitude";
	public static final String SLOPE_ANGLE = "Slope Angle";
	public static final String SLOPE_AZIMUTH = "Slope Azimuth";
	public static final String SOIL_DESCRIPTION = "Soil Description";
	public static final String SOIL_DEPTH = "Soil Depth";
	public static final String BEDROCK_DESCRIPTION = "Bedrock Description";
	

	public static final String[] TABLE_PROPERTIES = {
		OBJECT, TITLE, TYPE, TAXON, COMMENTS, DESCRIPTION, FILES, LATITUDE, LONGITUDE,WAYPOINT, AUTHENTICITY,
		SHAPE, HEIGHT, WIDTH, DEPTH, DIAMETER, UNIT, 
		LOCATION_PRECISION, LOCATION_COMMENT, LOCATION_TYPE,
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
		argElement.setFiles((TridasFileList) getProperty(FILES));

		argElement.setTaxon((ControlledVoc) getProperty(TAXON));
		argElement.setShape((TridasShape) getProperty(SHAPE));
		argElement.setMarks((String) getProperty(MARKS));
		argElement.setAltitude((Double) getProperty(ALTITUDE));
		argElement.setAuthenticity((String) getProperty(AUTHENTICITY));
		argElement.setProcessing((String) getProperty(PROCESSING));
		
		
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
		
		Object latitude = getProperty(LATITUDE);
		Object longitude = getProperty(LONGITUDE);
		Object addressline1 = getProperty(ADDRESSLINE1);
		Object addressline2 = getProperty(ADDRESSLINE2);
		Object city = getProperty(CITY_TOWN);
		Object state = getProperty(STATE_PROVINCE_REGION);
		Object postcode = getProperty(POSTCODE);
		Object country = getProperty(COUNTRY);
		Object locprecision = getProperty(LOCATION_PRECISION);
		Object loccomment = getProperty(LOCATION_COMMENT);
		Object loctype = getProperty(LOCATION_TYPE);
		
		if((latitude != null && longitude != null) || 
			addressline1 !=null ||
			addressline2 !=null ||
			city != null ||
			state != null ||
			postcode != null ||
			country != null ||
			locprecision != null ||
			loccomment != null ||
			loctype != null
			){

			TridasLocation loc = new TridasLocation();

			try{
				loc.setLocationPrecision(locprecision.toString());
			} catch (Exception e)
			{
				log.warn("Error setting location precision or comment");
			}

			loc.setLocationComment((String) loccomment);	
			
			if(latitude != null && longitude != null)
			{
				TridasLocationGeometry locgeo = SpatialUtils.getWGS84LocationGeometry((Double)latitude, (Double)longitude);				
				loc.setLocationGeometry(locgeo);
			}
					
			if(addressline1 !=null ||
				addressline2 !=null ||
				city != null ||
				state != null ||
				postcode != null ||
				country != null ||
				locprecision != null ||
				loccomment != null
			)
			{
				TridasAddress addr = new TridasAddress();
				addr.setAddressLine1((String)addressline1);
				addr.setAddressLine2((String)addressline2);
				addr.setCityOrTown((String)city);
				addr.setCountry((String) country);
				addr.setPostalCode((String)postcode);
				addr.setStateProvinceRegion((String)state);
				loc.setAddress(addr);
			}
			else
			{
				loc.setAddress(null);
			}
			
			if(loctype!=null)
			{
				loc.setLocationType((NormalTridasLocationType) loctype);
			}
			
			argElement.setLocation(loc);		
		}else{
			argElement.setLocation(null);
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
		setProperty(FILES, new TridasFileList(argElement.getFiles()));

		setProperty(AUTHENTICITY, argElement.getAuthenticity());
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
		setProperty(PROCESSING, argElement.getProcessing());

		
		// I love how nested this is!
		if(argElement.getLocation() != null &&
				argElement.getLocation().getLocationGeometry() != null&&
				argElement.getLocation().getLocationGeometry().getPoint() != null &&
				argElement.getLocation().getLocationGeometry().getPoint().getPos() != null &&
				argElement.getLocation().getLocationGeometry().getPoint().getPos().getValues().size() == 2){
			
			GMLPointSRSHandler tph = new GMLPointSRSHandler(argElement.getLocation().getLocationGeometry().getPoint());
			
			if(tph.hasPointData())
			{
				setProperty(LATITUDE, tph.getWGS84LatCoord());
				setProperty(LONGITUDE, tph.getWGS84LongCoord());
			}

		}else{
			setProperty(LATITUDE, null);
			setProperty(LONGITUDE, null);
		}
		
		if(argElement.isSetLocation() &&
				argElement.getLocation().isSetLocationType())
		{
			setProperty(LOCATION_TYPE, argElement.getLocation().getLocationType());
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
